package src.screens.editorScreen.timeline.track.trackItem.itemEffect.itemVideoEffect;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

import src.ImagePanel;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.trackItem.itemEffect.ItemEffect;

public abstract class ItemVideoEffect extends ItemEffect{

	
	public abstract boolean isFullFrameRequired();
	
	
	public abstract int[] renderFrame(int[] pixels, int width, int height, TrackItem trackItem);
	
	
	public BufferedImage renderFrame(Image image, TrackItem trackItem){
		try {
			BufferedImage frame = ImagePanel.getBI(image);
			
			int[] pixels;
			int width;
			int height;
			
			ImageIcon imageicon = new ImageIcon(frame);
			width = imageicon.getIconWidth();
			height = imageicon.getIconHeight();
			pixels = new int[width * height];
			PixelGrabber pixelgrabber = new PixelGrabber(frame, 0, 0, width, height, pixels, 0, width);
			pixelgrabber.grabPixels();
			pixels = pixels.clone();
			
			
			
			pixels = renderFrame(pixels, width, height, trackItem);
			
			
			BufferedImage finalFrame = ImagePanel.getBI(pixels, width, height);
			
			return finalFrame;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
