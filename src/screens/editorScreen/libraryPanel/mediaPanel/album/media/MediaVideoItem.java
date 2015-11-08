package src.screens.editorScreen.libraryPanel.mediaPanel.album.media;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImagingOpException;
import java.awt.image.PixelGrabber;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;






import src.ImagePanel;
import src.MainApplet;
import src.Project;
import src.dataStore.DataStore;
import src.multiThreading.threads.DecodingThread;
import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.thirdPartyLibraries.Scalr;
import src.thirdPartyLibraries.Scalr.Method;
import src.thirdPartyLibraries.Scalr.Mode;
import src.util.Misc;

public class MediaVideoItem extends MediaItem {

	public Image previewImage;
	
	public BufferedImage thumbnail;
	public double framerate;
	
	public int[] fullPixels;
	public BufferedImage fullImage;
	
	

	
	public byte[] frameDecoded;
	
	public static int thumbnailWidth = 454;
	public static int thumbnailHeight = 255;
	
	public boolean createdPreviewPixels = false;
	
	public Image sourceImage = null;
	
	public int formatType;
	public String getReferenceName(){
		return ""+directory.hashCode()+"";
	}
	
	
	public int totalFrames = 0;
	
	public int lastFrame = -1;
	
	public boolean busy = false;

	public Image getFullFrame(int frameIndex, int width, int height){

		try {

			if(formatType == 1){
				//BufferedImage image = requestFrame(frameIndex);
				
				
				System.out.println("got full frame: "+frameIndex);
				
				System.out.println("start image");
				BufferedImage image = DecodingThread.requestFrame(this, frameIndex);
				System.out.println("got image");
				
				Image resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, width, null);
				if(resizedImage.getHeight(null) >= Project.getScaledHeight()){
					resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, height, null);
				}
				return resizedImage;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
	
	public static Image getImageFromFile(File file){
		if(file.exists()){
			Image image = null;
			image = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
			while(image == null){
				
			}
			return image;
		}
		return null;
	}
	
	public Image getPreviewFrame(int frameIndex){
		
		return null;
	}
	
	public void generatePreviewFile(){
		/*
		 * Decoding GIF
		 */
		try {


		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
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
	
	
	
	
	public void decodeFrame(int frameIndex){
	}
	
	
	
	public MediaVideoItem(final String directory, Album album, int formatType) {
		super(directory, album);
		//createPreviewPixels();
		this.formatType = formatType;
		if(formatType == 0){
			generatePreviewFile();
		}
		

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



	public BufferedImage requestFrame(int requestedLocalFrame) {
		// TODO Auto-generated method stub
		return null;
	}


	
	
}
