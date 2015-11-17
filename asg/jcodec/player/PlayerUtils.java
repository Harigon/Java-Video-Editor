package asg.jcodec.player;

import asg.jcodec.codecs.pcm.PCMDecoder;
import asg.jcodec.codecs.s302.S302MDecoder;
import asg.jcodec.common.AudioDecoder;
import asg.jcodec.player.filters.MediaInfo;

public class PlayerUtils {
    public static AudioDecoder getAudioDecoder(String fourcc, MediaInfo.AudioInfo info) {
        if ("sowt".equals(fourcc) || "in24".equals(fourcc) || "twos".equals(fourcc) || "in32".equals(fourcc))
            return new PCMDecoder(info);
        else if ("s302".equals(fourcc))
            return new S302MDecoder();
        else
            return null;
    }
}
