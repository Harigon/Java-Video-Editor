package asg.jcodec.player.filters;

import asg.jcodec.common.model.ColorSpace;
import asg.jcodec.common.model.Picture;
import asg.jcodec.common.model.Rational;

/**
 * This class is part of JCodec ( www.jcodec.org )
 * This software is distributed under FreeBSD License
 * 
 * Video output interface
 * 
 * @author The JCodec project
 * 
 */
public interface VideoOutput {

    void show(Picture pic, Rational rational);
    
    ColorSpace getColorSpace();

}
