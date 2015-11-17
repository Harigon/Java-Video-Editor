package asg.jcodec.containers.mp4;

import java.io.IOException;

import asg.jcodec.common.NIOUtils;
import asg.jcodec.common.SeekableByteChannel;
import asg.jcodec.containers.mp4.boxes.AliasBox;
import asg.jcodec.containers.mp4.boxes.ChunkOffsets64Box;
import asg.jcodec.containers.mp4.boxes.ChunkOffsetsBox;
import asg.jcodec.containers.mp4.boxes.DataInfoBox;
import asg.jcodec.containers.mp4.boxes.DataRefBox;
import asg.jcodec.containers.mp4.boxes.MediaInfoBox;
import asg.jcodec.containers.mp4.boxes.NodeBox;
import asg.jcodec.containers.mp4.boxes.SampleEntry;
import asg.jcodec.containers.mp4.boxes.TrakBox;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * 
 * @author The JCodec project
 * 
 */
public class ChunkWriter {
    private long[] offsets;
    private SampleEntry[] entries;
    private SeekableByteChannel[] inputs;
    private int curChunk;
    private SeekableByteChannel out;
    byte[] buf = new byte[8092];
    private TrakBox trak;

    public ChunkWriter(TrakBox trak, SeekableByteChannel[] inputs, SeekableByteChannel out) {
        entries = NodeBox.findAll(trak, SampleEntry.class, "mdia", "minf", "stbl", "stsd", null);
        ChunkOffsetsBox stco = NodeBox.findFirst(trak, ChunkOffsetsBox.class, "mdia", "minf", "stbl", "stco");
        ChunkOffsets64Box co64 = NodeBox.findFirst(trak, ChunkOffsets64Box.class, "mdia", "minf", "stbl", "co64");
        int size;
        if (stco != null)
            size = stco.getChunkOffsets().length;
        else
            size = co64.getChunkOffsets().length;
        this.inputs = inputs;

        offsets = new long[size];
        this.out = out;
        this.trak = trak;
    }

    public void apply() {
        NodeBox stbl = NodeBox.findFirst(trak, NodeBox.class, "mdia", "minf", "stbl");
        stbl.removeChildren("stco", "co64");

        stbl.add(new ChunkOffsets64Box(offsets));
        cleanDrefs(trak);
    }

    private void cleanDrefs(TrakBox trak) {
        MediaInfoBox minf = trak.getMdia().getMinf();
        DataInfoBox dinf = trak.getMdia().getMinf().getDinf();
        if (dinf == null) {
            dinf = new DataInfoBox();
            minf.add(dinf);
        }

        DataRefBox dref = dinf.getDref();
        if (dref == null) {
            dref = new DataRefBox();
            dinf.add(dref);
        }

        dref.getBoxes().clear();
        dref.add(AliasBox.createSelfRef());

        for (SampleEntry entry : NodeBox.findAll(trak, SampleEntry.class, "mdia", "minf", "stbl", "stsd", null)) {
            entry.setDrefInd((short) 1);
        }
    }

    private SeekableByteChannel getInput(Chunk chunk) {
        SampleEntry se = entries[chunk.getEntry() - 1];
        return inputs[se.getDrefInd() - 1];
    }

    public void write(Chunk chunk) throws IOException {
        SeekableByteChannel input = getInput(chunk);
        input.position(chunk.getOffset());
        long pos = out.position();

        out.write(NIOUtils.fetchFrom(input, (int) chunk.getSize()));
        offsets[curChunk++] = pos;
    }
}