package asg.jcodec.containers.mkv;

import static asg.jcodec.common.IOUtils.readFileToByteArray;
import static asg.jcodec.containers.mkv.CuesIndexer.CuePointMock.make;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Assert;
import org.junit.Test;

import asg.jcodec.common.IOUtils;
import asg.jcodec.containers.mkv.CuesIndexer.CuePointMock;
import asg.jcodec.containers.mkv.ebml.Element;
import asg.jcodec.containers.mkv.ebml.MasterElement;
import asg.jcodec.containers.mkv.ebml.UnsignedIntegerElement;
import asg.jcodec.containers.mkv.elements.Cluster;

public class CuesIndexerTest {

    @Test
    public void test() throws IOException {
        File file = new File("src/test/resources/mkv/cues.ebml");
        byte[] rawCue = readFileToByteArray(file);
        CuesIndexer indexer = new CuesIndexer(0, 1);
        indexer.add(make(new byte[] { 0x00, 0x01 }, 0, 1000));
        indexer.add(make(new byte[] { 0x00, 0x02 }, 1209, 2000));
        indexer.add(make(new byte[] { 0x00, 0x02 }, 1443, 1240));
        MasterElement cues = indexer.createCues();
        ByteBuffer bb = cues.mux();

        Assert.assertArrayEquals(rawCue, bb.array());
    }

    public void testMock() throws IOException {
        MKVTestSuite suite = MKVTestSuite.read();
        if (!suite.isSuitePresent())
            Assert.fail("MKV test suite is missing, please download from http://www.matroska.org/downloads/test_w1.html, and save to the path recorded in src/test/resources/mkv/suite.properties");
        FileInputStream inputStream = new FileInputStream(suite.test5);
        FileChannel iFS = inputStream.getChannel();
        SimpleEBMLParser p = new SimpleEBMLParser(iFS);
        try {
            p.parse();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        Cluster[] ccs = Type.findAll(p.getTree(), Cluster.class, Type.Segment, Type.Cluster);
        Element seekHead = Type.findFirst(p.getTree(), Type.Segment, Type.SeekHead);
        Element info = Type.findFirst(p.getTree(), Type.Segment, Type.Info);
        Element tracks = Type.findFirst(p.getTree(), Type.Segment, Type.Tracks);
        long shs = seekHead.getSize();
        long is = info.getSize();
        long ts = tracks.getSize();
        System.out.println("shs = "+shs+" is = "+is+" ts = "+ts+" sum "+(shs+is+ts));
        CuesIndexer indexer = new CuesIndexer(shs + is + ts, 1);
        Element origCues = Type.findFirst(p.getTree(), Type.Segment, Type.Cues);
        System.out.println("cues offset: "+origCues.offset);
        for (Cluster c : ccs)
            indexer.add(CuePointMock.make(c));
        
        MasterElement cues = indexer.createCues();
        Assert.assertEquals(417, cues.getSize());
        
        UnsignedIntegerElement cueClusterPosition = (UnsignedIntegerElement) Type.findFirst(cues, Type.Cues, Type.CuePoint, Type.CueTrackPositions, Type.CueClusterPosition);
        Assert.assertEquals(1038, cueClusterPosition.get());
        
        UnsignedIntegerElement origCueClusterPosition = (UnsignedIntegerElement) Type.findFirst(origCues, Type.Cues, Type.CuePoint, Type.CueTrackPositions, Type.CueClusterPosition);
        Assert.assertEquals(cueClusterPosition.get(), origCueClusterPosition.get());
    }
}
