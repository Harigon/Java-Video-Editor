package asg.jcodec.common;

import java.io.IOException;

import asg.jcodec.common.model.Packet;

public interface DemuxerTrack {

    Packet nextFrame() throws IOException;

    boolean gotoFrame(long i);

    long getCurFrame();

    void seek(double second);

}
