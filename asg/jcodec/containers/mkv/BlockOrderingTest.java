package asg.jcodec.containers.mkv;

import static asg.jcodec.common.IOUtils.readFileToByteArray;
import static asg.jcodec.containers.mkv.Type.Block;
import static asg.jcodec.containers.mkv.Type.BlockGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import asg.jcodec.common.IOUtils;
import asg.jcodec.containers.mkv.ebml.Element;
import asg.jcodec.containers.mkv.ebml.MasterElement;
import asg.jcodec.containers.mkv.ebml.UnsignedIntegerElement;
import asg.jcodec.containers.mkv.elements.BlockElement;
import asg.jcodec.containers.mkv.elements.Cluster;

public class BlockOrderingTest {

    @Test
    public void testMuxingFixedLacingFrame() throws IOException {
        File file = new File("src/test/resources/mkv/fixed_lacing_simple_block.ebml");
        byte[] rawFrame = IOUtils.readFileToByteArray(file);
        BlockElement be = new BlockElement(Type.SimpleBlock.id);
        be.offset = 0x00;
        be.size = 0xC05;
        be.dataOffset = 0x03;
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            FileChannel channel = fileInputStream.getChannel();
            channel.position(be.dataOffset);
            be.readData(channel);
            be.readFrames(channel);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
        Assert.assertEquals(rawFrame.length, be.getSize());
        Assert.assertArrayEquals(rawFrame, be.mux().array());
    }

    @Test
    public void testMuxingEbmlLacingFrame() throws IOException {
        File file = new File("src/test/resources/mkv/ebml_lacing_block.ebml");
        byte[] rawFrame = readFileToByteArray(file);
        BlockElement be = new BlockElement(Type.Block.id);
        be.offset = 0x00;
        be.size = 0xF22;
        be.dataOffset = 0x03;
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            FileChannel channel = fileInputStream.getChannel();
            channel.position(be.dataOffset);
            be.readData(channel);
            be.readFrames(channel);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
        Assert.assertEquals(rawFrame.length, be.getSize());
        Assert.assertArrayEquals(rawFrame, be.mux().array());
    }

    @Test
    public void testMuxingXiphLacingFrame() throws IOException {
        File file = new File("src/test/resources/mkv/xiph_lacing_block.ebml");
        byte[] rawFrame = readFileToByteArray(file);
        BlockElement be = new BlockElement(Type.Block.id);
        be.offset = 0x00;
        be.size = 0x353;
        be.dataOffset = 0x03;
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            FileChannel channel = fileInputStream.getChannel();
            channel.position(be.dataOffset);
            be.readData(channel);
            be.readFrames(channel);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
        Assert.assertEquals(rawFrame.length, be.getSize());
        Assert.assertArrayEquals(rawFrame, be.mux().array());
    }
    
    @Test
    public void testMuxingNoLacingFrame() throws IOException {
        File file = new File("src/test/resources/mkv/no_lacing_simple_block.ebml");
        byte[] rawFrame = readFileToByteArray(file);
        BlockElement be = new BlockElement(Type.SimpleBlock.id);
        be.offset = 0x00;
        be.size = 0x304;
        be.dataOffset = 0x03;
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            FileChannel channel = fileInputStream.getChannel();
            channel.position(be.dataOffset);
            be.readData(channel);
            be.readFrames(channel);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
        Assert.assertEquals(be.size, be.getDataSize());
        Assert.assertEquals(rawFrame.length, be.getSize());
        Assert.assertArrayEquals(rawFrame, be.mux().array());
    }
    
    public void test() throws IOException {
        MKVTestSuite suite = MKVTestSuite.read();
        if (!suite.isSuitePresent())
            Assert.fail("MKV test suite is missing, please download from http://www.matroska.org/downloads/test_w1.html, and save to the path recorded in src/test/resources/mkv/suite.properties");
        System.out.println("Scanning file: " + suite.test1.getAbsolutePath());

        FileInputStream inputStream = new FileInputStream(suite.test1);
        try {
            SimpleEBMLParser reader = new SimpleEBMLParser(inputStream.getChannel());
            reader.parse();
            printTracks(reader.getTree());
            printTimecodes(reader.getTree());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private void printTracks(List<MasterElement> tree) {
        for (UnsignedIntegerElement nr : Type.findAll(tree, UnsignedIntegerElement.class, Type.Segment, Type.Tracks, Type.TrackEntry, Type.TrackNumber)) {
            System.out.println("Track nr:" + nr.get());
        }
    }

    private void printTimecodes(List<MasterElement> tree) {
        Cluster[] clusters = Type.findAll(tree, Cluster.class, Type.Segment, Type.Cluster);
        for (Cluster c : clusters) {
            long ctc = ((UnsignedIntegerElement) Type.findFirst(c, Type.Cluster, Type.Timecode)).get();
            long bks = 0;
            for (Element e : c.children) {
                if (e instanceof BlockElement) {
                    BlockElement block = (BlockElement) e;
                    int btc = block.timecode;
                    block.absoluteTimecode = btc+ctc;
                    System.out.println("        Block timecode: " + btc + " absoluete timecode: " + (btc + ctc) + " track: " + block.trackNumber + " offset: " + e.offset +" lacing "+block.lacing);
                    bks++;
                } else if (Type.BlockGroup.equals(e.type)) {
                    BlockElement be = (BlockElement) Type.findFirst(e, BlockGroup, Block);
                    be.absoluteTimecode = be.timecode + ctc;
                    System.out.println("        Block Group timecode: " + be.timecode + " absoluete timecode: " + (be.timecode + ctc) + " track: " + be.trackNumber + " offset: " +be.offset+" lacing "+be.lacing);
                    bks++;
                }
            }
            System.out.println("    Cluster timecode: " + ctc + " offset: " + c.offset + " bks: " + bks);
        }
    }

}
