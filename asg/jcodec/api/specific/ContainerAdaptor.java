package asg.jcodec.api.specific;

import asg.jcodec.common.model.Packet;
import asg.jcodec.common.model.Picture;

public interface ContainerAdaptor {

    Picture decodeFrame(Packet packet, int[][] data);

    boolean canSeek(Packet data);

}
