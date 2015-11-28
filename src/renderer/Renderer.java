package src.renderer;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;

import javax.swing.ImageIcon;


import src.ImagePanel;
import src.Project;
import src.frameRateConverter.FrameRateConverter;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaVideoItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackAudioItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackImageItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackVideoItem;
import src.screens.editorScreen.timeline.track.Track;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.trackItem.itemEffect.ItemEffect;
import src.screens.editorScreen.timeline.track.trackItem.itemEffect.itemVideoEffect.ItemVideoEffect;
import src.screens.editorScreen.timeline.track.transition.Transition;
import src.thirdPartyLibraries.Scalr;

/**
 * @author Harry
 *
 */
public class Renderer {
	
	/*
	 * Declare global variables.
	 */
	public static int[][] pixels;//This stores all of the frames for the entire time-line.
	public static byte[] renderingStatus;//Stores the rendering status of each frame.
	
	/**
	 * Setup the renderer.
	 */
	public static void initRenderer(){
		pixels = new int[4000][];
		renderingStatus = new byte[4000];
	}
	
	/**
	 * @param frameId - The frame to render.
	 * @param includeTransitions - Render transitions as well?
	 * @param width - The width to render the frame.
	 * @param height - The height to render the frame.
	 * @param fullQuality - Render the frame using the original full-quality source?
	 * @return - Pixel array of the rendered frame.
	 */
	public static int[] renderFrame(int frameId, boolean includeTransitions, int width, int height, boolean fullQuality){
		
		/*
		 * Declare the pixel array.  This array will be used to store the final frame after
		 * the rendering is complete, and the method will return this back.
		 */
		int[] pixels = null;
		
		/*
		 * Set-up a try/catch in case any errors occur, this will prevent any exceptions
		 * from crashing the entire program/thread.
		 */
		try {
			
			
			/*
			 * Store a local version of the global track array-list (To prevent any concurrency issues).
			 */
			ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();

			/*
			 * Make sure the array-list is not null.
			 */
			if(localTracks != null){
				
				/*
				 * Loop through each track.
				 */
				for(Track trackInstance : localTracks){

					/*
					 * Store a local version of the global track-items array-list (To prevent any concurrency issues).
					 */
					ArrayList<TrackItem> localTrackItems = (ArrayList<TrackItem>) trackInstance.trackItems.clone();


					/*
					 * If the track is null, skip+continue to the next one.
					 */
					if(localTrackItems == null){
						continue;
					}
					
					
					/*
					 * Loop through each track item.
					 */
					for(TrackItem trackItem : localTrackItems){

						/*
						 * If the track-item is null, skip+continue to the next one.
						 */
						if(trackItem == null){
							continue;
						}
						
						if(trackItem instanceof TrackAudioItem){
							continue;
						}

						/*
						 * Get the frame/X positions of the item on the track.
						 */
						int startX = trackItem.trackStartPosition;
						int endX = trackItem.getTrackStartPosition()+trackItem.mediaDuration;

						if(!includeTransitions){
							startX = trackItem.getTrackStartPosition();
							endX = trackItem.getTrackStartPosition()+trackItem.mediaDuration;
						}
						
						/*
						 * Check if any of the item is positioned at the frame we want to render!
						 */
						if(frameId >= startX && frameId <= endX){
							
							
							boolean isItem2 = false;
							if(!includeTransitions){
								
								
								
								
								for(Transition transition : trackInstance.transitions){
									/*
									 * Is the transition part of this item we are rendering?
									 */
									if(transition.item1 == trackItem){
										isItem2 = true;
									}
								}
								
							}
							if(isItem2){
							continue;
							}
							//System.out.println("Track: "+trackItem.mediaItem.directory);
							
							Image previewImage = null;
							BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							
							
							if(trackItem instanceof TrackImageItem){


								/*
								 * Grab the preview image from the item.
								 */
								MediaImageItem mediaImageItem = (MediaImageItem) trackItem.mediaItem;

								previewImage = mediaImageItem.previewImage;

								/*
								 * If the fullQuality boolean is true, we want to use the original source,
								 * instead of the compressed and down-scaled preview image.
								 */
								if(fullQuality){
									previewImage = mediaImageItem.getFullImage(width, height);
								}
							}
							
							if(trackItem instanceof TrackVideoItem){

								int mediaFrameId = (frameId-trackItem.getTrackStartPosition())+trackItem.mediaStartPosition;
								
								if(trackItem.mediaItem instanceof MediaVideoItem){
								
								/*
								 * Grab the preview image from the item.
								 */
								MediaVideoItem mediaVideoItem = (MediaVideoItem) trackItem.mediaItem;
								//System.out.println("renderr frame: "+frameId);
								//previewImage = mediaVideoItem.getPreviewFrame(frameId-trackItem.trackStartPosition);
								
								
								
								previewImage = FrameRateConverter.getFrame(mediaVideoItem, mediaFrameId, /*mediaVideoItem.framerate*/ Project.frameRate, Project.frameRate, width, height, fullQuality);
								//System.out.println("got frame: "+frameId);
								
								
								/*
								 * If the fullQuality boolean is true, we want to use the original source,
								 * instead of the compressed and down-scaled preview image.
								 */
								if(fullQuality){
									
									/*
									previewImage = null;
									
									
									while(previewImage == null){
										
									previewImage = mediaVideoItem.getFullFrame(frameId-trackItem.trackStartPosition, width, height);
									
									}*/
									previewImage = FrameRateConverter.getFrame(mediaVideoItem, mediaFrameId, mediaVideoItem.framerate, Project.frameRate, width, height, fullQuality);
									
								}
								}
								
								
								
								
								
								
							}
							
							
							
							
							
							
							/*
							 * Apply item effects
							 */
							for(ItemEffect itemEffect : trackItem.itemEffects){
								if(itemEffect instanceof ItemVideoEffect){
									ItemVideoEffect itemVideoEffect = (ItemVideoEffect) itemEffect;

									
									
									if(itemVideoEffect.isFullFrameRequired() && !fullQuality){
										if(trackItem instanceof TrackVideoItem){
											MediaVideoItem mediaVideoItem = (MediaVideoItem) trackItem.mediaItem;
											
											
											
											Image fullImage = null;
											
											while(fullImage == null){
												fullImage = mediaVideoItem.getFullFrame(frameId-trackItem.getTrackStartPosition(), width, height);
											}
											
											BufferedImage renderedEffectImage = itemVideoEffect.renderFrame(fullImage, trackItem);
											
											
											Image resizedImage = Scalr.resize(renderedEffectImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, Project.getScaledWidth(), null);
											if(resizedImage.getHeight(null) >= Project.getScaledHeight()){
												resizedImage = Scalr.resize(renderedEffectImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, Project.getScaledHeight(), null);
											}
											
											
											previewImage = resizedImage;
											
										}
									} else {
										
										if(previewImage != null){
										BufferedImage renderedEffectImage = itemVideoEffect.renderFrame(previewImage, trackItem);
										previewImage = renderedEffectImage;
										} else {
											System.out.println("shit its null");
										}
									}
									
									
								}
							}
							
							
							
							/*
							 * Add the image to the final frame.
							 * (Also deal with any aspect ratio issues by 'letterboxing'.)
							 */
							if(width != previewImage.getWidth(null) && height == previewImage.getHeight(null)){
								//Keep aspect ratio by allowing black bars to be at the sides.
								frame.getGraphics().drawImage(previewImage, (width/2)-(previewImage.getWidth(null)/2), 0, null);
							}
							if(height != previewImage.getHeight(null) && width == previewImage.getWidth(null)){
								//Keep aspect ratio by allowing black bars to be at the top and bottom.
								frame.getGraphics().drawImage(previewImage, 0, (height/2)-(previewImage.getHeight(null)/2), null);
							}
							if(height == previewImage.getHeight(null) && width == previewImage.getWidth(null)){
								//Aspect ratio of preview image is the same as output aspect ratio, so draw normally.
								frame.getGraphics().drawImage(previewImage, 0, 0, null);
							}	
							
							/*
							 * Convert the frame into an array of pixels.
							 */
							ImageIcon imageicon = new ImageIcon(frame);
							width = imageicon.getIconWidth();
							height = imageicon.getIconHeight();
							pixels = new int[width * height];
							PixelGrabber pixelgrabber = new PixelGrabber(frame, 0, 0, width, height, pixels, 0, width);
							pixelgrabber.grabPixels();
							pixels = pixels.clone();
							
							
							
							
							
							
							
							
							/*
							 * Carry out any transition rendering if boolean is true.
							 */
							if(includeTransitions){
								
								/*
								 * Loop through each transition, and correct any potential problems
								 * before we do the actual rendering.  (I.e order issues)
								 * (Item 1 must ALWAYS be positioned before Item 2. We can fix that here.)
								 */
								for(Transition transition : trackInstance.transitions){
									if(transition.item1.trackStartPosition > transition.item2.trackStartPosition){
										TrackItem temp = transition.item2;
										transition.item2 = transition.item1;
										transition.item1 = temp;
									}
								}
								
								/*
								 * Loop through each transition on this track.
								 */
								for(Transition transition : trackInstance.transitions){
									
									/*
									 * Is the transition part of this item we are rendering?
									 */
									if(transition.item1 == trackItem){
										
										
										
										/*
										 * Is the transition involved with the frame id we are actually rendering?
										 */
										if(frameId >= (transition.item1.getTrackStartPosition()+transition.item1.mediaDuration)-transition.duration){
											
											
											/*
											 * Once again, make sure that item 1 is positioned BEFORE item2.
											 */
											if(transition.item1.getTrackStartPosition() < transition.item2.getTrackStartPosition()){
												
													/*
													 * Get the distance from the next item.
													 */
													int distanceFromOtherClip = (transition.item2.trackStartPosition-frameId)-1;

		
													//int hmm = (transition.item2.trackStartPosition-frameId) + transition.duration;
													
													/*
													 * This is the first frame of the item we are transitioning TO.
													 * (RECURSION is also being used here, to render another
													 *  frame that is needed to render this frame!)
													 */
													
													int[] item2Pixels = renderFrame(frameId, false, width, height, fullQuality).clone();//recurring method

													/*
													 * Apply the transition to the frame, and return+assign the new pixel array.
													 */
													pixels = transition.renderTransitionFrame(pixels, item2Pixels, distanceFromOtherClip, transition.duration, width, height);
													
													//pixels = item2Pixels;
													
													if(pixels == null){
														System.out.println("WARNING: null transition frame.");
													}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {//Print out any exceptions.
			e.printStackTrace();
		}
		return pixels;
	}
		
}
