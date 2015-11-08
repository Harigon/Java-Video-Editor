package src.screens.editorScreen.libraryPanel.mediaPanel.album.media;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImagingOpException;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;


import src.ImagePanel;
import src.Project;
import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.thirdPartyLibraries.Scalr;
import src.thirdPartyLibraries.Scalr.Method;
import src.thirdPartyLibraries.Scalr.Mode;

public class MediaImageItem extends MediaItem {

	public Image previewImage;
	
	public BufferedImage thumbnail;
	
	public int[] fullPixels;
	public BufferedImage fullImage;
	
	public static int thumbnailWidth = 454;
	public static int thumbnailHeight = 255;
	
	public boolean createdPreviewPixels = false;
	
	public Image sourceImage = null;
	
	/**
	 * Returns the full image data from the original source (no compression etc.)
	 * @param width2 - The width that we need it to be.
	 * @param height2 - The height that we need it to be.
	 * @return
	 */
	public Image getFullImage(int width2, int height2){
		if(sourceImage != null){
			return sourceImage;
		}
		int width;
		int height;
		int[] pixels;
		Image finalImage = null;
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(directory);
			ImageIcon imageicon = new ImageIcon(image);
			width = imageicon.getIconWidth();
			height = imageicon.getIconHeight();
			pixels = new int[width * height];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
			pixelgrabber.grabPixels();
			Image resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, width2, null);
			if(resizedImage.getHeight(null) >= height2){
				resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, height2, null);
			}
			finalImage = resizedImage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sourceImage = finalImage;
		return finalImage;
	}
	
	
	/**
	 * Scale the image and store it for preview purposes.
	 */
	public void createPreviewPixels(){

		
		int width;
		int height;
		int[] pixels;
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(directory);
			ImageIcon imageicon = new ImageIcon(image);
			width = imageicon.getIconWidth();
			height = imageicon.getIconHeight();
			pixels = new int[width * height];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
			pixelgrabber.grabPixels();
			Image resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, Project.getScaledWidth(), null);
			if(resizedImage.getHeight(null) >= Project.getScaledHeight()){
				resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, Project.getScaledHeight(), null);
			}
			previewImage = resizedImage;
			thumbnail = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 67, null);
			createdPreviewPixels = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public MediaImageItem(String directory, Album album) {
		super(directory, album);
		createPreviewPixels();
	}


	@Override
	public BufferedImage getThumbnail() {
		if(thumbnail == null){
			BufferedImage unloadedImage = new BufferedImage(71, 56, BufferedImage.TYPE_INT_ARGB);
			Graphics g = unloadedImage.getGraphics();
			g.setColor(new Color(0,0,0));
			g.fillRect(0, 0, 71, 56);
			g.setColor(new Color(255,255,255));
			g.drawString("Importing...", 3, 30);
			return unloadedImage;
		}
		return thumbnail;
	}


	
	
}
