package asg.jcodec.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import asg.jcodec.common.NIOUtils;
import asg.jcodec.common.SeekableByteChannel;
import asg.jcodec.common.model.Size;
import asg.jcodec.containers.mp4.Brand;
import asg.jcodec.containers.mp4.MP4Packet;
import asg.jcodec.containers.mp4.TrackType;
import asg.jcodec.containers.mp4.muxer.FramesMP4MuxerTrack;
import asg.jcodec.containers.mp4.muxer.MP4Muxer;

public class SequenceMuxer {
    private SeekableByteChannel ch;
    private FramesMP4MuxerTrack outTrack;
    private int frameNo;
    private MP4Muxer muxer;
    private Size size;

    public SequenceMuxer(File out) throws IOException {
        this.ch = NIOUtils.writableFileChannel(out);

        // Muxer that will store the encoded frames
        muxer = new MP4Muxer(ch, Brand.MP4);

        // Add video track to muxer
        outTrack = muxer.addTrackForCompressed(TrackType.VIDEO, 25);
    }

    public void encodeImage(File png) throws IOException {
        if (size == null) {
            BufferedImage read = ImageIO.read(png);
            size = new Size(read.getWidth(), read.getHeight());
        }
        // Add packet to video track
        outTrack.addFrame(new MP4Packet(NIOUtils.fetchFrom(png), frameNo, 25, 1, frameNo, true, null, frameNo, 0));

        frameNo++;
    }

    public void finish() throws IOException {
        // Push saved SPS/PPS to a special storage in MP4
        outTrack.addSampleEntry(MP4Muxer.videoSampleEntry("png ", size, "JCodec"));

        // Write MP4 header and finalize recording
        muxer.writeHeader();
        NIOUtils.closeQuietly(ch);
    }
}
