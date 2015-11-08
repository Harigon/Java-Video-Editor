package src.multiThreading.threads;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import src.ImagePanel;
import src.IntroScreen;
import src.Project;
import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;


public class RenderingThread {
	
	
	public static boolean turnedOn = false;
	
	
	public static int requestedFrame = -1;
	
	
	
	/**
	 * Returns a BufferedImage frame.
	 * @param frameId - The frame to get.
	 */
	public static BufferedImage getFrame(int frameId){
		if(Renderer.renderingStatus != null && Renderer.renderingStatus.length > frameId){
			int status = Renderer.renderingStatus[frameId];


			if(status == 2){
				if(Renderer.pixels[frameId] != null){
					BufferedImage bufferedImage = ImagePanel.getBI(Renderer.pixels[frameId], Project.getScaledWidth(), Project.getScaledHeight());
					return bufferedImage;
				} else {
					BufferedImage blank = new BufferedImage(Project.getScaledWidth(), Project.getScaledHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics g = blank.getGraphics();
					g.setColor(new Color(0,0,0));
					g.fillRect(0, 0, Project.getScaledWidth(), Project.getScaledHeight());
					return blank;
				}
			}
		}
		ImagePanel.update();

		return null;

	}
	
	
	/**
	 * Carries out any rendering related processes.
	 */
	public static void processTasks(){
		if(turnedOn){
			
			if(IntroScreen.animationRunning){
				IntroScreen.frameId++;
			} else {
				if(IntroScreen.startDelay > 0){
					IntroScreen.startDelay--;
				}
			}
			
			for(int frameIndex = 0; frameIndex < Renderer.pixels.length; frameIndex++){
				

				if(frameIndex >= src.screens.editorScreen.timeline.TimelineManager.timelinePosition && frameIndex < src.screens.editorScreen.timeline.TimelineManager.timelinePosition+60){

					if(Renderer.renderingStatus[frameIndex] == 0){
						
						
						
						Renderer.pixels[frameIndex] = Renderer.renderFrame(frameIndex, true, Project.getScaledWidth(), Project.getScaledHeight(), false);
						//if(Renderer.pixels[frameIndex] != null)
						if(frameIndex+1 < Renderer.renderingStatus.length){
							Renderer.renderingStatus[frameIndex] = 2;
						}
					}
				} else {
					Renderer.pixels[frameIndex] = null;
					Renderer.renderingStatus[frameIndex] = 0;
				}

			}
		}

	}
	
	
	
	
	
	
	
	
}
