package org.jcodec.common;

import java.io.IOException;


/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * 
 * @author The JCodec project
 * 
 */
public interface SeekableDemuxerTrack extends DemuxerTrack {

    boolean gotoFrame(long i) throws IOException;
    
    boolean gotoSyncFrame(long i) throws IOException;

    long getCurFrame();

    void seek(double second) throws IOException;
}
