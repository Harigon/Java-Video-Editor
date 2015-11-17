package asg.jcodec.containers.mkv;

import static asg.jcodec.containers.mkv.Type.Cluster;
import static asg.jcodec.containers.mkv.Type.Segment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import asg.jcodec.common.IOUtils;
import asg.jcodec.containers.mkv.ebml.MasterElement;
import asg.jcodec.containers.mkv.ebml.UnsignedIntegerElement;
import asg.jcodec.containers.mkv.elements.Cluster;

public class SAEParserTest {
    
    MKVTestSuite suite;
    
    @Before
    public void setUp() throws IOException{
        suite = MKVTestSuite.read();
        if (!suite.isSuitePresent())
            Assert.fail("MKV test suite is missing, please download from http://www.matroska.org/downloads/test_w1.html, and save to the path recorded in src/test/resources/mkv/suite.properties");
    }

    public void test() throws IOException {
        System.out.println("Scanning file: " + suite.test1.getAbsolutePath());

        FileInputStream fileInputStream = new FileInputStream(suite.test1);
        SimpleEBMLParser reader = new SimpleEBMLParser(fileInputStream.getChannel());
        reader.parse();
        IOUtils.closeQuietly(fileInputStream);
    }

    public void testAll() throws IOException {
        for (File aFile : suite.allTests()) {
            System.out.println("Scanning file: " + aFile.getAbsolutePath());
            FileInputStream stream = new FileInputStream(aFile);
            SimpleEBMLParser reader = new SimpleEBMLParser(stream.getChannel());
            try {
                reader.parse();
            } finally {
                IOUtils.closeQuietly(stream);
            }
            UnsignedIntegerElement[] durations = Type.findAll(reader.getTree(), UnsignedIntegerElement.class, Type.Segment, Type.Cluster, Type.BlockGroup, Type.BlockDuration);
            for (UnsignedIntegerElement d : durations)
                System.out.println("block duration: "+d.get());
        }
    }

    public void testFind() throws IOException {
        System.out.println("Scanning file: " + suite.test5.getAbsolutePath());
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(suite.test5);
            FileChannel iFS = stream.getChannel();
            SimpleEBMLParser reader = new SimpleEBMLParser(iFS);
            reader.parse();
            // reader.printParsedTree();
            MasterElement[] allSegments = Type.findAll(reader.getTree(), MasterElement.class, Segment);
            Assert.assertNotNull(allSegments);
            Assert.assertEquals(1, allSegments.length);
            
            Cluster[] allClusters = Type.findAll(reader.getTree(), Cluster.class, Segment, Cluster);
            Assert.assertNotNull(allClusters);
            Assert.assertEquals(36, allClusters.length);
        } finally {
            stream.close();
        }
    }
    
}
