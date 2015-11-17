package asg.jcodec.containers.mkv.ebml;

import junit.framework.Assert;

import org.junit.Test;

import asg.jcodec.containers.mkv.Type;

public class FloatElementTest {

    @Test
    public void test() {
        FloatElement durationElem = (FloatElement) Type.createElementByType(Type.Duration);
        durationElem.set(5 * 1000.0);
        
        Assert.assertEquals(5000.0, durationElem.get(),  0.0001);
        
    }

}
