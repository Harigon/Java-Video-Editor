package src.screens.editorScreen.timeline.track.transition.transitions;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

import src.ImagePanel;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.transition.Transition;

public class WipeDown extends Transition {

	public WipeDown(TrackItem item1, TrackItem item2) {
		super(item1, item2);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see src.screens.editorScreen.timeline.track.transition.Transition#renderTransitionFrame(int[], int[], int, int, int, int)
	 */
	@Override
	public int[] renderTransitionFrame(int[] currentPixels, int[] nextPixels, int distanceFromNext, int transitionDuration, int width3, int height3) {
		try {
			int height2 = (int) (distanceFromNext * (height3/transitionDuration));

			height2 = height3-height2;
			//double divider = (double)transparency/255;

			Image currentImage = ImagePanel.getBI(currentPixels, width3, height3);
			Image nextImage = ImagePanel.getBI(nextPixels, width3, height3);

			BufferedImage combined = new BufferedImage(width3, height3, BufferedImage.TYPE_INT_ARGB);

			// paint both images, preserving the alpha channels
			Graphics g = combined.getGraphics();
			g.drawImage(currentImage, 0, 0, null);

			//((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) divider));

			g.drawImage(nextImage, 0, height2-height3, null);

			ImageIcon imageicon = new ImageIcon(combined);
			int width = imageicon.getIconWidth();
			int height = imageicon.getIconHeight();
			int[] pixels = new int[width * height];
			PixelGrabber pixelgrabber = new PixelGrabber(combined, 0, 0, width, height, pixels, 0, width);
			pixelgrabber.grabPixels();

			pixels = pixels.clone();

			return pixels;

		} catch (Exception e) {
			e.printStackTrace();
		}



		return null;
	}

}
