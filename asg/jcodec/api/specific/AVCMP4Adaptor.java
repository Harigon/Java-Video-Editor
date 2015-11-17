package asg.jcodec.api.specific;

import static asg.jcodec.codecs.h264.H264Utils.splitMOVPacket;

import asg.jcodec.codecs.h264.H264Decoder;
import asg.jcodec.codecs.h264.H264Utils;
import asg.jcodec.codecs.h264.mp4.AvcCBox;
import asg.jcodec.common.model.Packet;
import asg.jcodec.common.model.Picture;
import asg.jcodec.containers.mp4.MP4Packet;
import asg.jcodec.containers.mp4.boxes.Box;
import asg.jcodec.containers.mp4.boxes.LeafBox;
import asg.jcodec.containers.mp4.boxes.PixelAspectExt;
import asg.jcodec.containers.mp4.boxes.SampleEntry;
import asg.jcodec.containers.mp4.demuxer.AbstractMP4DemuxerTrack;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * 
 * High level frame grabber helper.
 * 
 * @author The JCodec project
 * 
 */
public class AVCMP4Adaptor implements ContainerAdaptor {

    private H264Decoder decoder;
    private SampleEntry[] ses;
    private AvcCBox avcCBox;
    private int curENo;

    public AVCMP4Adaptor(SampleEntry[] ses) {
        this.ses = ses;
        this.curENo = -1;
    }

    public AVCMP4Adaptor(AbstractMP4DemuxerTrack vt) {
        this(((AbstractMP4DemuxerTrack) vt).getSampleEntries());
    }

    public Picture decodeFrame(Packet packet, int[][] data) {
        updateState(packet);

        //System.out.println("fuck1");
        
        Picture pic = ((H264Decoder) decoder).decodeFrame(H264Utils.splitMOVPacket(packet.getData(), avcCBox), data);
        PixelAspectExt pasp = Box.findFirst(ses[curENo], PixelAspectExt.class, "pasp");

        if (pasp != null) {
            // TODO: transform
        }

        return pic;
    }

    private void updateState(Packet packet) {
        int eNo = ((MP4Packet) packet).getEntryNo();
        if (eNo != curENo) {
            curENo = eNo;
            avcCBox = new AvcCBox();
            avcCBox.parse(Box.findFirst(ses[curENo], LeafBox.class, "avcC").getData());
            decoder = new H264Decoder();
            ((H264Decoder) decoder).addSps(avcCBox.getSpsList());
            ((H264Decoder) decoder).addPps(avcCBox.getPpsList());
        }
    }

    @Override
    public boolean canSeek(Packet pkt) {
        updateState(pkt);
        return H264Utils.idrSlice(splitMOVPacket(pkt.getData(), avcCBox));
    }
}