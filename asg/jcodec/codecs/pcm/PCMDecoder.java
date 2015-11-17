package asg.jcodec.codecs.pcm;

import java.nio.ByteBuffer;

import asg.jcodec.common.AudioDecoder;
import asg.jcodec.common.NIOUtils;
import asg.jcodec.common.model.AudioBuffer;
import asg.jcodec.player.filters.MediaInfo;
import asg.jcodec.player.filters.MediaInfo.AudioInfo;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * 
 * @author The JCodec project
 * 
 */
public class PCMDecoder implements AudioDecoder {

    private MediaInfo.AudioInfo audioInfo;

    public PCMDecoder(AudioInfo audioInfo) {
        this.audioInfo = audioInfo;
    }

    public AudioBuffer decodeFrame(ByteBuffer frame, ByteBuffer dst) {
        ByteBuffer dup = dst.duplicate();
        NIOUtils.write(dst, frame);

        dup.flip();
        return new AudioBuffer(dup, audioInfo.getFormat(), dup.remaining() / audioInfo.getFormat().getFrameSize());
    }
}
