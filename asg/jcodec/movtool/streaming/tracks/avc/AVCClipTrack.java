package asg.jcodec.movtool.streaming.tracks.avc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import asg.jcodec.codecs.h264.H264Decoder;
import asg.jcodec.codecs.h264.H264Encoder;
import asg.jcodec.codecs.h264.H264Utils;
import asg.jcodec.codecs.h264.H264Utils.SliceHeaderTweaker;
import asg.jcodec.codecs.h264.encode.ConstantRateControl;
import asg.jcodec.codecs.h264.io.model.Frame;
import asg.jcodec.codecs.h264.io.model.NALUnit;
import asg.jcodec.codecs.h264.io.model.NALUnitType;
import asg.jcodec.codecs.h264.io.model.PictureParameterSet;
import asg.jcodec.codecs.h264.io.model.SeqParameterSet;
import asg.jcodec.codecs.h264.io.model.SliceHeader;
import asg.jcodec.codecs.h264.mp4.AvcCBox;
import asg.jcodec.common.NIOUtils;
import asg.jcodec.common.model.ColorSpace;
import asg.jcodec.common.model.Picture;
import asg.jcodec.containers.mp4.MP4Util;
import asg.jcodec.containers.mp4.boxes.SampleDescriptionBox;
import asg.jcodec.containers.mp4.boxes.SampleEntry;
import asg.jcodec.containers.mp4.boxes.VideoSampleEntry;
import asg.jcodec.movtool.streaming.VirtualPacket;
import asg.jcodec.movtool.streaming.VirtualTrack;
import asg.jcodec.movtool.streaming.tracks.ClipTrack;
import asg.jcodec.movtool.streaming.tracks.VirtualPacketWrapper;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * 
 * Clips AVC track replacing the remainder of a GOP at cut point with I-frames
 * 
 * @author The JCodec project
 * 
 */
public class AVCClipTrack extends ClipTrack {

    private AvcCBox avcC;
    private ConstantRateControl rc;
    private int mbW;
    private int mbH;
    private VideoSampleEntry se;
    private int frameSize;
    private SeqParameterSet encSPS;
    private PictureParameterSet encPPS;

    public AVCClipTrack(VirtualTrack src, int frameFrom, int frameTo) {
        super(src, frameFrom, frameTo);

        SampleEntry origSE = src.getSampleEntry();
        if (!"avc1".equals(origSE.getFourcc()))
            throw new RuntimeException("Not an AVC source track");

        rc = new ConstantRateControl(1024);
        H264Encoder encoder = new H264Encoder(rc);
        avcC = H264Utils.parseAVCC((VideoSampleEntry) origSE);
        SeqParameterSet sps = H264Utils.readSPS(NIOUtils.duplicate(avcC.getSpsList().get(0)));

        mbW = sps.pic_width_in_mbs_minus1 + 1;
        mbH = H264Utils.getPicHeightInMbs(sps);

        encSPS = encoder.initSPS(H264Utils.getPicSize(sps));
        encSPS.seq_parameter_set_id = 1;
        encPPS = encoder.initPPS();
        encPPS.seq_parameter_set_id = 1;
        encPPS.pic_parameter_set_id = 1;

        avcC.getSpsList().add(H264Utils.writeSPS(encSPS, 128));
        avcC.getPpsList().add(H264Utils.writePPS(encPPS, 20));

        se = (VideoSampleEntry) MP4Util.cloneBox(origSE, 2048, SampleDescriptionBox.FACTORY);
        se.removeChildren("avcC");
        se.add(avcC);

        frameSize = rc.calcFrameSize(mbW * mbH);
        frameSize += frameSize >> 4;
    }

    protected List<VirtualPacket> getGop(VirtualTrack src, int from) throws IOException {
        VirtualPacket packet = src.nextPacket();

        List<VirtualPacket> head = new ArrayList<VirtualPacket>();
        while (packet != null && packet.getFrameNo() < from) {
            if (packet.isKeyframe())
                head.clear();
            head.add(packet);
            packet = src.nextPacket();
        }
        List<VirtualPacket> tail = new ArrayList<VirtualPacket>();
        while (packet != null && !packet.isKeyframe()) {
            tail.add(packet);
            packet = src.nextPacket();
        }

        List<VirtualPacket> gop = new ArrayList<VirtualPacket>();
        GopTranscoder tr = new GopTranscoder(head, tail);
        for (int i = 0; i < tail.size(); i++)
            gop.add(new TranscodePacket(tail.get(i), tr, i));

        gop.add(packet);

        return gop;
    }

    public class GopTranscoder {

        private List<VirtualPacket> tail;
        private List<VirtualPacket> head;
        private List<ByteBuffer> result;

        public GopTranscoder(List<VirtualPacket> head, List<VirtualPacket> tail) {
            this.head = head;
            this.tail = tail;
        }

        public List<ByteBuffer> transcode() throws IOException {
            H264Decoder decoder = new H264Decoder();
            decoder.addSps(avcC.getSpsList());
            decoder.addPps(avcC.getPpsList());
            Picture buf = Picture.create(1920, 1088, ColorSpace.YUV420);
            Frame dec = null;
            for (VirtualPacket virtualPacket : head) {
                dec = decoder.decodeFrame(H264Utils.splitMOVPacket(virtualPacket.getData(), avcC), buf.getData());
            }
            H264Encoder encoder = new H264Encoder(rc);
            ByteBuffer tmp = ByteBuffer.allocate(frameSize);

            List<ByteBuffer> result = new ArrayList<ByteBuffer>();
            for (VirtualPacket pkt : tail) {
                dec = decoder.decodeFrame(H264Utils.splitMOVPacket(pkt.getData(), avcC), buf.getData());

                tmp.clear();
                ByteBuffer res = encoder.encodeFrame(tmp, dec);
                ByteBuffer out = ByteBuffer.allocate(frameSize);
                processFrame(res, out);

                result.add(out);
            }

            return result;
        }

        private void processFrame(ByteBuffer in, ByteBuffer out) {
            SliceHeaderTweaker st = new H264Utils.SliceHeaderTweaker() {
                @Override
                protected void tweak(SliceHeader sh) {
                    sh.pic_parameter_set_id = 1;
                }
            };

            ByteBuffer dup = in.duplicate();
            while (dup.hasRemaining()) {
                ByteBuffer buf = H264Utils.nextNALUnit(dup);
                if (buf == null)
                    break;

                NALUnit nu = NALUnit.read(buf);
                if (nu.type == NALUnitType.IDR_SLICE) {
                    ByteBuffer sp = out.duplicate();
                    out.putInt(0);
                    nu.write(out);
                    st.run(buf, out, nu, encSPS, encPPS);
                    sp.putInt(out.position() - sp.position() - 4);
                }
            }

            if (out.remaining() >= 5) {
                out.putInt(out.remaining() - 4);
                new NALUnit(NALUnitType.FILLER_DATA, 0).write(out);
            }
            out.clear();
        }

        public synchronized List<ByteBuffer> getResult() throws IOException {
            if (result == null)
                result = transcode();

            return result;
        }
    }

    @Override
    public SampleEntry getSampleEntry() {
        return se;
    }

    public class TranscodePacket extends VirtualPacketWrapper {

        private GopTranscoder tr;
        private int off;

        public TranscodePacket(VirtualPacket src, GopTranscoder tr, int off) {
            super(src);

            this.tr = tr;
            this.off = off;
        }

        @Override
        public ByteBuffer getData() throws IOException {
            return NIOUtils.duplicate(tr.getResult().get(off));
        }

        @Override
        public int getDataLen() throws IOException {
            return frameSize;
        }

        @Override
        public boolean isKeyframe() {
            return true;
        }
    }
}