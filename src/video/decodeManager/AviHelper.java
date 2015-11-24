package src.video.decodeManager;

import java.awt.image.BufferedImage;
import java.io.File;

import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaVideoItem;
import src.video.avi.decoder.AVI_Reader;

public class AviHelper extends DecodeManager {

	public AVI_Reader avi;

	public AviHelper(MediaVideoItem mediaVideoItem, String directory)
			throws Exception {
		super(mediaVideoItem, directory);
		
		
		avi = new AVI_Reader();
		avi.run(new File(directory));
		
		
		mediaVideoItem.totalFrames = avi.getSize();
		mediaVideoItem.framerate = avi.getFrameRate();
		
	}

	@Override
	public BufferedImage requestFrame(int frameIndex) {
		
		System.out.println("Requesting frame: "+frameIndex);
		
		BufferedImage bi = avi.getFrame(frameIndex);
		return bi;
	}

}
