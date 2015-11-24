package src.video.decodeManager;

import java.awt.image.BufferedImage;

import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaVideoItem;

public abstract class DecodeManager {

	public MediaVideoItem videoItem;
	
	public DecodeManager(MediaVideoItem mediaVideoItem, String directory) throws Exception {
	}
	
	public abstract BufferedImage requestFrame(int frameIndex) throws Exception;
	
	
	
}
