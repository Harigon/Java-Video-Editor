package src.multiThreading.threads;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import src.ImagePanel;
import src.IntroScreen;
import src.Project;
import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.mediaPanel.MediaPanelManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaVideoItem;
import src.screens.editorScreen.timeline.track.Track;
import src.screens.editorScreen.timeline.track.trackItem.TrackAudioItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackVideoItem;


public class DecodingThread {
	
	
	public static boolean turnedOn = false;
	
	
	public static BufferedImage requestedFrame = null;
	public static MediaVideoItem requestedMediaItem = null;
	public static int requestedLocalFrame = -1;
	public static BufferedImage requestFrame(MediaVideoItem item, int localFrame){
		requestedFrame = null;
		requestedMediaItem = item;
		
		//System.out.println("Requested media item set to: "+item);
		
		requestedLocalFrame = localFrame;
		
		//int i = 0;
		
		while(true){
			//i+=1;
			//if(i == 5000000){
				//System.out.println("wating: "+requestedFrame);
				//i = 0;
			//}
			//
			
			if(requestedFrame != null){
				System.out.println("requestedFrame: "+requestedFrame);
				return requestedFrame;
			}
		}
		
	}

	public static boolean isFrameDecoded(int frameId){
		ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();
		if(localTracks != null){
			for(Track trackInstance : localTracks){
				ArrayList<TrackItem> localTrackItems = (ArrayList<TrackItem>) trackInstance.trackItems.clone();
				if(localTrackItems == null){
					continue;
				}
				for(TrackItem trackItem : localTrackItems){
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
					int endX = trackItem.trackStartPosition+trackItem.mediaDuration;

					/*
					 * Check if any of the item is positioned at the frame we want to render!
					 */
					if(frameId >= startX && frameId <= endX){
						if(trackItem instanceof TrackVideoItem && trackItem.mediaItem instanceof MediaVideoItem){
							MediaVideoItem mediaVideoItem = (MediaVideoItem) trackItem.mediaItem;
							int localFrameId = frameId-trackItem.trackStartPosition;

							if(mediaVideoItem.frameDecoded != null){

								if(mediaVideoItem.frameDecoded.length > localFrameId){
									if(mediaVideoItem.frameDecoded[localFrameId] == 2){
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	
	public static boolean decodeFramesPastPointer(){
		ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();
		if(localTracks != null){
			for(Track trackInstance : localTracks){
				ArrayList<TrackItem> localTrackItems = (ArrayList<TrackItem>) trackInstance.trackItems.clone();
				if(localTrackItems == null){
					continue;
				}
				for(TrackItem trackItem : localTrackItems){
					if(trackItem == null){
						continue;
					}
					if(trackItem instanceof TrackAudioItem){
						continue;
					}


					if(trackItem instanceof TrackVideoItem && trackItem.mediaItem instanceof MediaVideoItem){
						TrackVideoItem trackVideoItem = (TrackVideoItem) trackItem;
						/*
						 * Get the frame/X positions of the item on the track.
						 */
						int startX = trackItem.trackStartPosition;
						int endX = trackItem.trackStartPosition+trackItem.mediaDuration;

						/*
						 * Check if any of the item is positioned at the frame we want to render!
						 */
						if(src.screens.editorScreen.timeline.TimelineManager.timelinePosition <= endX){
							MediaVideoItem mediaVideoItem = (MediaVideoItem) trackVideoItem.mediaItem;
							int localFrameId = src.screens.editorScreen.timeline.TimelineManager.timelinePosition-trackItem.trackStartPosition;


							if(mediaVideoItem.frameDecoded != null){
								
								if(localFrameId < 0){
									localFrameId = 0;
								}
								
								if(localFrameId < mediaVideoItem.frameDecoded.length && localFrameId >= 0){


									//itemToDecode = mediaVideoItem;
									//decodeFromPos = localFrameId;


									for(int index = localFrameId; index < mediaVideoItem.frameDecoded.length; index++){
										if(mediaVideoItem.frameDecoded[index] == 0){
											itemToDecode = mediaVideoItem;
											decodeFromPos = index;
											//System.out.println("set to: "+localFrameId);
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	
	public static MediaVideoItem itemToDecode;
	public static int decodeFromPos = 0;
	
	
	public static boolean busy = false;
	
	public static void updateDecodingPriority(){
		while(true){
			if(!busy){
				busy = true;

				itemToDecode = null;
				decodeFromPos = 0;

				if(!decodeFramesPastPointer()){
					if(!decodeFramesBeforePointer()){
						if(!decodeFrameInAlbum()){

						}
					}
				}
				//System.out.println("updated");
				busy = false;
				return;
			}
		}
	}

	
	public static boolean decodeFramesBeforePointer(){
		ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();
		if(localTracks != null){
			for(Track trackInstance : localTracks){
				ArrayList<TrackItem> localTrackItems = (ArrayList<TrackItem>) trackInstance.trackItems.clone();
				if(localTrackItems == null){
					continue;
				}
				for(TrackItem trackItem : localTrackItems){
					if(trackItem == null){
						continue;
					}
					if(trackItem instanceof TrackAudioItem){
						continue;
					}


					if(trackItem instanceof TrackVideoItem  && trackItem.mediaItem instanceof MediaVideoItem){
						TrackVideoItem trackVideoItem = (TrackVideoItem) trackItem;
						/*
						 * Get the frame/X positions of the item on the track.
						 */
						int startX = trackItem.trackStartPosition;
						int endX = trackItem.trackStartPosition+trackItem.mediaDuration;

						/*
						 * Check if any of the item is positioned at the frame we want to render!
						 */


						MediaVideoItem mediaVideoItem = (MediaVideoItem) trackVideoItem.mediaItem;
						int localFrameId = src.screens.editorScreen.timeline.TimelineManager.timelinePosition-trackItem.trackStartPosition;


						if(mediaVideoItem.frameDecoded != null){
							
							//System.out.println("hello")
							

							
							if(localFrameId < mediaVideoItem.frameDecoded.length && localFrameId >= 0){
								for(int index = 0; index < mediaVideoItem.frameDecoded.length; index++){
									if(mediaVideoItem.frameDecoded[index] == 0){
										//mediaVideoItem.decodeFrame(index);
										//mediaVideoItem.frameDecoded[index] = 2;
										itemToDecode = mediaVideoItem;
										decodeFromPos = index;
										System.out.println("set to: "+localFrameId);
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static TrackItem renderFromTrackPosition(MediaVideoItem mediaVideoItem, int position){
		ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();
		if(localTracks != null){
			for(Track trackInstance : localTracks){
				ArrayList<TrackItem> localTrackItems = (ArrayList<TrackItem>) trackInstance.trackItems.clone();
				if(localTrackItems == null){
					continue;
				}
				for(TrackItem trackItem : localTrackItems){
					if(trackItem == null){
						continue;
					}
					if(trackItem instanceof TrackAudioItem){
						continue;
					}

					
					if(trackItem instanceof TrackVideoItem){
						TrackVideoItem trackVideoItem = (TrackVideoItem) trackItem;
						if(trackVideoItem.mediaItem == mediaVideoItem){
							/*
							 * Get the frame/X positions of the item on the track.
							 */
							int startX = trackItem.trackStartPosition;
							int endX = trackItem.trackStartPosition+trackItem.mediaDuration;

							/*
							 * Check if any of the item is positioned at the frame we want to render!
							 */
							if(position >= startX && position <= endX){
								return trackItem;
							}
						}
					}
					
					
				}
			}
		}
		return null;
	}
	
	public static boolean decodeFrameInAlbum(){
		for(Album album : MediaPanelManager.albums){
			for(MediaItem mediaItem : album.mediaItems){
				if(mediaItem instanceof MediaVideoItem){
					MediaVideoItem mediaVideoItem = (MediaVideoItem) mediaItem;

					
					//int startPoint = 0;
					
					//TrackItem renderFromTrackPosition = renderFromTrackPosition(mediaVideoItem, src.screens.editorScreen.timeline.TimelineManager.timelinePosition);
					//if(renderFromTrackPosition != null && ){
						//startPoint = src.screens.editorScreen.timeline.TimelineManager.timelinePosition-renderFromTrackPosition.trackStartPosition;
					//}
					
					if(mediaVideoItem.frameDecoded != null){
					
					for(int index = 0; index < mediaVideoItem.frameDecoded.length; index++){
						if(mediaVideoItem.frameDecoded[index] == 0){
							//mediaVideoItem.decodeFrame(index);
							//mediaVideoItem.frameDecoded[index] = 2;
							
							itemToDecode = mediaVideoItem;
							decodeFromPos = 0;
							
							return true;
						}
					}
					}
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Carries out any rendering related processes.
	 */
	public static void processTasks(){
		if(turnedOn){
			
			//System.out.println("requestedMediaItem: "+requestedMediaItem+", requestedFrame "+requestedFrame);
			
			if(requestedMediaItem != null && requestedFrame == null){
				System.out.println("Yes");
				requestedFrame = requestedMediaItem.requestFrame(requestedLocalFrame);
				try {
					ImageIcon imageicon = new ImageIcon(requestedFrame);
					int width = imageicon.getIconWidth();
					int height = imageicon.getIconHeight();
					int[] pixels = new int[width * height];
					PixelGrabber pixelgrabber = new PixelGrabber(requestedFrame, 0, 0, width, height, pixels, 0, width);
					pixelgrabber.grabPixels();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Requested frame set to: "+requestedFrame);
				requestedMediaItem = null;
				
				System.out.println("requestedMediaItem Set to null");
				
				requestedLocalFrame = -1;
			}
			
			
			
			/*
			 * Normal frame decoding
			 */
			if(itemToDecode != null){
				MediaVideoItem mediaVideoItem = itemToDecode;
				int position = decodeFromPos;
				if(mediaVideoItem.frameDecoded != null){
					for(int index = position; index < mediaVideoItem.frameDecoded.length; index++){
						
						
						if(requestedMediaItem != null && requestedFrame == null){
							break;
						}
						
						if(mediaVideoItem.frameDecoded[index] == 2){
							//updateDecodingPriority();
							break;
						}
						
						if(mediaVideoItem.frameDecoded[index] == 0){
							mediaVideoItem.decodeFrame(index);
							mediaVideoItem.frameDecoded[index] = 2;
							
							
							
							ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();
							if(localTracks != null){
								for(Track trackInstance : localTracks){
									ArrayList<TrackItem> localTrackItems = (ArrayList<TrackItem>) trackInstance.trackItems.clone();
									if(localTrackItems == null){
										continue;
									}
									for(TrackItem trackItem : localTrackItems){
										if(trackItem == null){
											continue;
										}
										if(trackItem instanceof TrackAudioItem){
											continue;
										}
										if(trackItem.mediaItem == mediaVideoItem){
											int start = trackItem.trackStartPosition;
											Renderer.renderingStatus[start+index] = 0;
										}
									}
								}
							}
							
							
						}
						if(mediaVideoItem != itemToDecode || position != decodeFromPos){
							break;
						}
						
					}
					updateDecodingPriority();
					//if(itemToDecode == mediaVideoItem){
					//System.out.println("same one");
					///	
					//}
				}
			}
		}
	}
	
	
	
	
	
	
	
	
}
