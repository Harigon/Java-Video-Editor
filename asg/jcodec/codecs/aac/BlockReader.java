package asg.jcodec.codecs.aac;

import static asg.jcodec.codecs.aac.BlockType.TYPE_END;

import asg.jcodec.codecs.aac.blocks.Block;
import asg.jcodec.common.io.BitReader;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * 
 * Reads blocks of AAC frame
 * 
 * @author The JCodec project
 * 
 */
public class BlockReader {

    public Block nextBlock(BitReader bits) {
        BlockType type = BlockType.fromCode(bits.readNBit(3));
        if (type == TYPE_END)
            return null;

        int id = (int) bits.readNBit(4);

        return null;
    }
}
