/*
 * @(#)PNGMovWriter.java
 *
 * $Date: 2012-03-14 17:27:16 +0000 (Wed, 14 Mar 2012) $
 *
 * Copyright (c) 2012 by Jeremy Wood.
 * All rights reserved.
 *
 * The copyright of this software is owned by Jeremy Wood. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Jeremy Wood. For details see accompanying license terms.
 * 
 * This software is probably, but not necessarily, discussed here:
 * http://javagraphics.java.net/
 * 
 * That site should also contain the most recent official version
 * of this software.  (See the SVN repository for more details.)
 */
package src.thirdPartyLibraries.movLibrary;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;

/** A MovWriter that encodes frames as a series of PNG images.
 */
public class PNGMovWriter extends MovWriter {

	public PNGMovWriter(File file) throws IOException {
		super(file);
	}

	@Override
	protected VideoSampleDescriptionEntry getVideoSampleDescriptionEntry() {
		return VideoSampleDescriptionEntry.createPNGDescription( videoTrack.w, videoTrack.h);
	}

	@Override
	protected void writeFrame(OutputStream out, BufferedImage image,
			Map<String, Object> settings) throws IOException {
		if(!ImageIO.write(image, "png", out))
			throw new IOException("writed failed");
	}
}
