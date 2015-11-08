package src.dataStore;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.util.Hashtable;

import javax.swing.ImageIcon;

public class DataStore {

	
	/**
	 * Returns the location of the stored data on the hard-drive.
	 */
	public static String getLocation(){
		if(new File("./DataStore/").exists()){
			return "./DataStore/";
		}
		return System.getProperty("user.home") + "/DataStore/";
	}
	
	
	/**
	 * Returns the location of the cache.
	 */
	public static String getCache(){
		return getLocation() + "Cache/";
	}
	
	/**
	 * Returns the location of the sound directory.
	 */
	public static String getSounds(){
		return getLocation() + "Sounds/";
	}
	
	/**
	 * Returns the location of the image directory.
	 */
	public static String getImages(){
		return getLocation() + "Images/";
	}
	
	
	/**
	 * Gets an ImageIcon object from image ID.
	 * @param id - The image ID to use.
	 */
	public static ImageIcon getImageIcon(int id){
		ImageIcon image = new ImageIcon(DataStore.getImages()+""+id+".png");
		return image;
	}
	
	public static Hashtable<Integer, Image> imageCache = new Hashtable<Integer, Image>();
	
	
	/**
	 * Gets an Image object from image ID
	 * @param id - The image ID to use.
	 */
	public static Image spriteLoadFile(int id)
	{
		Image image = null;
		if (imageCache.containsKey(id)) {
			return imageCache.get(id);
		}
		image = makeColorTransparent(Toolkit.getDefaultToolkit().getImage(DataStore.getImages()+""+id+".png"));
		imageCache.put(id, image);
		return image;
	}
	
	
	/**
	 * Makes a specific colour transparent in an Image.
	 * 
	 * Credits: http://stackoverflow.com/questions/665406/how-to-make-a-color-transparent-in-a-bufferedimage-and-save-as-png
	 * 
	 * @param im - The Image to use.
	 */
	public static Image makeColorTransparent(Image im) {
    	ImageFilter filter = new RGBImageFilter() {

    		Color color = new Color(255,0,255);
    		
    		// the color we are looking for... Alpha bits are set to opaque
    		public int markerRGB = color.getRGB() | 0xFF000000;

    		public final int filterRGB(int x, int y, int rgb) {
    			if ((rgb | 0xFF000000) == markerRGB) {
    				// Mark the alpha bits as zero - transparent
    				return 0x00FFFFFF & rgb;
    			} else {
    				// nothing to do
    				return rgb;
    			}
    		}
    	};
    	ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
    	return Toolkit.getDefaultToolkit().createImage(ip);
	}
	
	
}
