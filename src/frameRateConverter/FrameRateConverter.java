package src.frameRateConverter;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

import src.ImagePanel;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaVideoItem;

public class FrameRateConverter {

	
	
	
	public static Image getFrame (MediaVideoItem mediaVideoItem, int frameId, double existingFrameRate, double newFrameRate, int width, int height, boolean fullQuality){

		
		if(newFrameRate == existingFrameRate){
			Image currentImage = null;
			
			if(fullQuality){
				while(currentImage == null){
					currentImage = mediaVideoItem.getFullFrame(frameId, width, height);
				}
			} else {
				currentImage = mediaVideoItem.getPreviewFrame(frameId);
			}
			return currentImage;
		}
		
		
		
		if (newFrameRate > existingFrameRate){
			double hmm = (double) newFrameRate/existingFrameRate; 

			double lowestPosition;
			double highestPosition;
			int startKeyFrame;

			double position = 0;
			int index = 0;
			
			
			

			while (true){
				double nextPosition = position+hmm;

				if (frameId >= position && frameId < nextPosition){
					lowestPosition = position;
					highestPosition = nextPosition; 
					startKeyFrame = index;
					break;
				}
				position = nextPosition;
				index++;
			}

			//Image startingKeyFrame = mediaVideoItem.getPreviewFrame(startKeyFrame);

			//Image endKeyFrame = mediaVideoItem.getPreviewFrame(startKeyFrame+1);


			
			double distanceFromNext = highestPosition-frameId;
			double transitionDuration = highestPosition-lowestPosition;
			
			int transparency = (int) (distanceFromNext * (255/transitionDuration));

			transparency = 255-transparency;
			double divider = (double)transparency/255;

			Image currentImage = null;
			Image nextImage = null;
			
			if(fullQuality){
				while(currentImage == null){
					currentImage = mediaVideoItem.getFullFrame(startKeyFrame, width, height);
				}
				
				while(nextImage == null){
					nextImage = mediaVideoItem.getFullFrame(startKeyFrame+1, width, height);
				}
			} else {
				currentImage = mediaVideoItem.getPreviewFrame(startKeyFrame);
				nextImage = mediaVideoItem.getPreviewFrame(startKeyFrame+1);
			}
			
			
			//System.out.println("Frame id: "+frameId+", image1: "+startKeyFrame+", image2: "+(startKeyFrame+1)+", distanceFromNext: "+distanceFromNext);
			

			BufferedImage combined = new BufferedImage(currentImage.getWidth(null), currentImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

			// paint both images, preserving the alpha channels
			Graphics g = combined.getGraphics();
			g.drawImage(currentImage, 0, 0, null);

			if(divider >= 0 && divider <= 1)
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) divider));

			g.drawImage(nextImage, 0, 0, null);

			return combined;



		}
		return null;

	}
	
	
	
	
	
}
