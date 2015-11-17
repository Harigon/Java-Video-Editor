package asg.jcodec.containers.mp4;

import static asg.jcodec.common.IOUtils.readFileToByteArray;
import static asg.jcodec.common.NIOUtils.readableFileChannel;
import static asg.jcodec.common.NIOUtils.rwFileChannel;
import static asg.jcodec.common.NIOUtils.writableFileChannel;
import static asg.jcodec.common.model.ColorSpace.RGB;
import static asg.jcodec.containers.mp4.TrackType.SOUND;
import static asg.jcodec.containers.mp4.TrackType.VIDEO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Assert;

import asg.jcodec.codecs.prores.ProresDecoder;
import asg.jcodec.codecs.wav.WavHeader;
import asg.jcodec.codecs.wav.WavHeader.FmtChunk;
import asg.jcodec.common.IOUtils;
import asg.jcodec.common.NIOUtils;
import asg.jcodec.common.SeekableByteChannel;
import asg.jcodec.common.model.Packet;
import asg.jcodec.common.model.Picture;
import asg.jcodec.containers.mp4.MP4Util.Atom;
import asg.jcodec.containers.mp4.boxes.AudioSampleEntry;
import asg.jcodec.containers.mp4.boxes.MovieBox;
import asg.jcodec.containers.mp4.boxes.VideoSampleEntry;
import asg.jcodec.containers.mp4.boxes.EndianBox.Endian;
import asg.jcodec.containers.mp4.demuxer.AbstractMP4DemuxerTrack;
import asg.jcodec.containers.mp4.demuxer.MP4Demuxer;
import asg.jcodec.containers.mp4.muxer.FramesMP4MuxerTrack;
import asg.jcodec.containers.mp4.muxer.MP4Muxer;
import asg.jcodec.containers.mp4.muxer.PCMMP4MuxerTrack;
import asg.jcodec.scale.AWTUtil;
import asg.jcodec.scale.Yuv422pToRgb;

public class TestDemuxer {

    private static void testAll(File src, File base) throws Exception {
        MP4Demuxer demuxer = new MP4Demuxer(readableFileChannel(src));
        AbstractMP4DemuxerTrack vt = demuxer.getVideoTrack();
        ProresDecoder decoder = new ProresDecoder();

        long duration = vt.getDuration().getNum();
        long frameCount = vt.getFrameCount();

        for (int t = 0;; t++) {
            randomPts(vt, decoder, duration, new Yuv422pToRgb(2, 0), base);
            // randomFrame(vt, decoder, frameCount, transform, dest);
        }
    }

    private static void randomPts(AbstractMP4DemuxerTrack vt, ProresDecoder decoder, long duration, Yuv422pToRgb transform,
            File base) throws IOException {

        long pts = (long) (Math.random() * duration);
        vt.seek(pts);
        for (int i = 0; i < 10; i++) {
            Packet frames = vt.nextFrame();
            Picture pic = decoder.decodeFrame(frames.getData(), allocBuffer(vt));
            Picture dest = Picture.create(pic.getWidth(), pic.getHeight(), RGB);
            transform.transform(pic, dest);

            ImageIO.write(AWTUtil.toBufferedImage(dest), "jpg", new File(base, "pts_" + pts + "_" + i + ".jpg"));
        }
    }

    private static int[][] allocBuffer(AbstractMP4DemuxerTrack vt) {
        VideoSampleEntry vse = (VideoSampleEntry) vt.getSampleEntries()[0];
        int size = (int) ((11 * vse.getWidth() * vse.getHeight()) / 10);
        return new int[][] { new int[size], new int[size], new int[size] };
    }

    private static void randomFrame(AbstractMP4DemuxerTrack vt, ProresDecoder decoder, long frameCount, Yuv422pToRgb transform,
            File base) throws IOException {
        long frame = (long) (Math.random() * frameCount);
        System.out.println(frame);
        vt.gotoFrame((int) frame);
        for (int i = 0; i < 10; i++) {
            Packet frames = vt.nextFrame();
            Picture pic = decoder.decodeFrame(frames.getData(), allocBuffer(vt));
            Picture dest = Picture.create(pic.getWidth(), pic.getHeight(), RGB);

            transform.transform(pic, dest);
            ImageIO.write(AWTUtil.toBufferedImage(dest), "jpg", new File(base, "frm_" + frame + "_" + i + ".jpg"));
        }
    }

    private static void testAudio(File src, File wavFile) throws Exception {
        MP4Demuxer demuxer = new MP4Demuxer(readableFileChannel(src));
        AbstractMP4DemuxerTrack demuxerTrack = demuxer.getAudioTracks().get(0);

        FileOutputStream fos = new FileOutputStream(wavFile);
        FileChannel ch = fos.getChannel();

        AudioSampleEntry se = (AudioSampleEntry) demuxerTrack.getSampleEntries()[0];

        WavHeader wav = new WavHeader("RIFF", 40, "WAVE", new FmtChunk((short) 1, (short) se.getChannelCount(),
                (int) se.getSampleRate(), (int) se.getSampleRate() * se.getBytesPerFrame(),
                (short) se.getBytesPerFrame(), (short) ((se.getBytesPerFrame() / se.getChannelCount()) << 3)), 44,
                se.getBytesPerFrame() * demuxerTrack.getFrameCount());
        wav.write(fos);

        while (true) {
            Packet packet = demuxerTrack.nextFrame();
            ch.write(packet.getData());
        }
    }

    private static void testProres(File base) throws Exception {
        ProresDecoder decoder = new ProresDecoder();
        for (int i = 1;; i++) {
            System.out.println(i);
            ByteBuffer buffer = NIOUtils.fetchFrom(new File(base, String.format("frame%08d.raw", i)));

            int sz = 1920 * 1080 * 2;
            decoder.decodeFrame(buffer, new int[][] { new int[sz], new int[sz], new int[sz] });
        }
    }

    private static void testVideo(File src, File base) throws IOException, FileNotFoundException {
        int startFn = 7572;
        MP4Demuxer demuxer = new MP4Demuxer(readableFileChannel(src));
        AbstractMP4DemuxerTrack vt = demuxer.getVideoTrack();
        vt.gotoFrame(startFn);
        for (int i = 0;; i++) {
            byte[] expected = readFileToByteArray(new File(base, String.format("frame%08d.raw", i + startFn
                    + 1)));
            Packet pkt = vt.nextFrame();
            Assert.assertArrayEquals(expected, NIOUtils.toArray(pkt.getData()));
            System.out.print(".");
            if ((i % 100) == 0)
                System.out.println();
        }
    }

    private static void testAudioMuxer(File wav, File out) throws IOException {
        WavHeader header = WavHeader.read(wav);
        RandomAccessFile in = new RandomAccessFile(wav, "r");
        in.seek(header.dataOffset);
        FileChannel ch = in.getChannel();
        MP4Muxer muxer = new MP4Muxer(writableFileChannel(out));
        PCMMP4MuxerTrack track = muxer.addTrackForUncompressed(SOUND, 48000, 1, 3,
                MP4Muxer.audioSampleEntry("in24", 1, 3, 1, 48000, Endian.LITTLE_ENDIAN));

        ByteBuffer buffer = ByteBuffer.allocate(3 * 24000);
        while (ch.read(buffer) != -1) {
            track.addSamples(buffer);
        }
        muxer.writeHeader();
    }

    private static void testRemux(File src, File dst) throws Exception {
        MP4Muxer muxer = new MP4Muxer(writableFileChannel(dst));

        MP4Demuxer demuxer1 = new MP4Demuxer(readableFileChannel(src));
        AbstractMP4DemuxerTrack vt1 = demuxer1.getVideoTrack();

        FramesMP4MuxerTrack outTrack = muxer.addTrackForCompressed(VIDEO, (int) vt1.getTimescale());
        outTrack.addSampleEntry(vt1.getSampleEntries()[0]);
        for (int i = 0; i < vt1.getFrameCount(); i++) {
            outTrack.addFrame((MP4Packet)vt1.nextFrame());
        }

        muxer.writeHeader();
    }

    private static void storeMdat(File src, File dst) throws Exception {
        List<Atom> rootAtoms = MP4Util.getRootAtoms(readableFileChannel(src));
        long mdatOff = -1, mdatSize = 0;
        for (Atom atom : rootAtoms) {
            if ("mdat".equals(atom.getHeader().getFourcc())) {
                mdatOff = atom.getOffset();
                mdatOff += atom.getHeader().headerSize();
                mdatSize = atom.getHeader().getBodySize();
            }
        }
        if (mdatOff == -1) {
            System.out.println("no mdat");
            return;
        }

        InputStream in = new BufferedInputStream(new FileInputStream(src));
        in.skip(mdatOff);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(dst));

        for (int i = 0; i < mdatSize; i++) {
            out.write(in.read());
        }
    }

    private static void narrowDown(File src, File dst) throws Exception {
        SeekableByteChannel rw = rwFileChannel(dst);
        SeekableByteChannel inp = readableFileChannel(src);
        List<Atom> rootAtoms = MP4Util.getRootAtoms(inp);
        for (Atom atom : rootAtoms) {
            if ("moov".equals(atom.getHeader().getFourcc())) {
                MovieBox box = (MovieBox) atom.parseBox(inp);
                MP4Util.writeMovie(rw, box);
            } else {
                atom.copy(inp, rw);
            }
        }
        inp.close();
        rw.close();
    }
}