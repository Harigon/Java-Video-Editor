package asg.jcodec.codecs.h264;

import static asg.jcodec.common.JCodecUtil.getAsIntArray;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import asg.jcodec.codecs.h264.io.model.Frame;
import asg.jcodec.common.JCodecUtil;
import asg.jcodec.common.NIOUtils;
import asg.jcodec.common.model.ColorSpace;
import asg.jcodec.common.model.Packet;
import asg.jcodec.common.model.Picture;

public class TestInter {

    @Test
    public void testMBlockCABAC1() throws IOException {
        MappedH264ES es = new MappedH264ES(NIOUtils.fetchFrom(new File("src/test/resources/h264/cabac/p_1/seq.264")));
        H264Decoder dec = new H264Decoder();

        Picture[] out = {
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()),
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()),
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()) };

        ByteBuffer yuv = NIOUtils.fetchFrom(new File("src/test/resources/h264/cabac/p_1/seq_dec.yuv"));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[0].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[0].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[0].getPlaneData(2));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[1].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[1].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[1].getPlaneData(2));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[2].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[2].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[2].getPlaneData(2));
    }

    @Test
    public void testMBlockCABAC2() throws IOException {
        MappedH264ES es = new MappedH264ES(NIOUtils.fetchFrom(new File("src/test/resources/h264/cabac/p_2/seq.264")));
        H264Decoder dec = new H264Decoder();

        Picture[] out = {
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()),
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()),
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()) };

        ByteBuffer yuv = NIOUtils.fetchFrom(new File("src/test/resources/h264/cabac/p_2/seq_dec.yuv"));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[0].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[0].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[0].getPlaneData(2));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[1].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[1].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[1].getPlaneData(2));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[2].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[2].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[2].getPlaneData(2));
    }
    
    @Test
    public void testMBlockCAVLC1() throws IOException {
        MappedH264ES es = new MappedH264ES(NIOUtils.fetchFrom(new File("src/test/resources/h264/cavlc/p_1/seq.264")));
        H264Decoder dec = new H264Decoder();

        Picture[] out = {
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()),
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()),
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()) };

        ByteBuffer yuv = NIOUtils.fetchFrom(new File("src/test/resources/h264/cavlc/p_1/seq_dec.yuv"));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[0].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[0].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[0].getPlaneData(2));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[1].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[1].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[1].getPlaneData(2));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[2].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[2].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[2].getPlaneData(2));
    }

    @Test
    public void testMBlockCAVLC2() throws IOException {
        MappedH264ES es = new MappedH264ES(NIOUtils.fetchFrom(new File("src/test/resources/h264/cavlc/p_2/seq.264")));
        H264Decoder dec = new H264Decoder();

        Picture[] out = {
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()),
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()),
                dec.decodeFrame(es.nextFrame().getData(), Picture.create(32, 32, ColorSpace.YUV420).getData()) };

        ByteBuffer yuv = NIOUtils.fetchFrom(new File("src/test/resources/h264/cavlc/p_2/seq_dec.yuv"));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[0].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[0].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[0].getPlaneData(2));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[1].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[1].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[1].getPlaneData(2));
        Assert.assertArrayEquals(getAsIntArray(yuv, 1024), out[2].getPlaneData(0));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[2].getPlaneData(1));
        Assert.assertArrayEquals(getAsIntArray(yuv, 256), out[2].getPlaneData(2));
    }
}