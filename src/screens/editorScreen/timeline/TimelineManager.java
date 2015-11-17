package src.screens.editorScreen.timeline;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import src.ImagePanel;
import src.MainApplet;
import src.Timeline;
import src.dataStore.DataStore;
import src.multiThreading.threads.DecodingThread;
import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaAudioItem;
import src.screens.editorScreen.previewPanel.PreviewPanelManager;
import src.screens.editorScreen.timeline.track.AudioTrack;
import src.screens.editorScreen.timeline.track.TextTrack;
import src.screens.editorScreen.timeline.track.Track;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.TrackManager;
import src.screens.editorScreen.timeline.track.VideoTrack;
import src.screens.editorScreen.timeline.track.trackItem.TrackAudioItem;
import src.thirdPartyLibraries.audio.jl.player.advanced.AdvancedPlayer;

/**
 * @author Harry
 *
 */
public class TimelineManager {

	
	public static ArrayList<Track> tracks = new ArrayList<Track>(100);
	
	
	public static int timelinePosition = 50;
	
	
	/**
	 * Sets the position/frame to play from.
	 * @param position - The frame to show.
	 */
	public static void setTimelinePosition(int position){
	
		if(PreviewPanelManager.selectedItemView){
			if(Timeline.selectedTrackObject != null){
				if(Timeline.selectedTrackObject instanceof TrackItem){
					TrackItem trackItem = (TrackItem) Timeline.selectedTrackObject;
					if(position > trackItem.trackStartPosition+trackItem.mediaDuration || position < trackItem.trackStartPosition){
						position = trackItem.trackStartPosition;
					}
				}
			}
		}
		timelinePosition = position;
		ImagePanel.generateFullQualityFrame();
		
		DecodingThread.updateDecodingPriority();
		//if(timelinePosition)
		
		ImagePanel.update();
		int position2 = src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(position);
		int hmm = MainApplet.getInstance().getjScrollPane3().getHorizontalScrollBar().getVisibleAmount()+MainApplet.getInstance().getjScrollPane3().getHorizontalScrollBar().getValue();
		if(position2 > hmm){
			MainApplet.getInstance().getjScrollPane3().getHorizontalScrollBar().setValue(position2);
		}
	}
	
	/**
	 * Increase the position by 1.
	 */
	public static void incrementTimelinePosition(){
		setTimelinePosition(timelinePosition+1);
		
	//	Track track = TrackManager.g
		
		//if(timelinePosition)
		
		Track track = TimelineManager.getFirstAudioTrack();
		System.out.println("playing");
		for(TrackItem item : track.trackItems){
			if(item instanceof TrackAudioItem){
				final TrackAudioItem audioItem = (TrackAudioItem) item;
				if(audioItem.advancedPlayer == null){
					
					
					
				if(timelinePosition > item.trackStartPosition){
					
					MediaAudioItem mediaAudioItem = (MediaAudioItem) audioItem.mediaItem;
					
					try {
						BufferedInputStream bis;
						bis = new BufferedInputStream(new FileInputStream(new File(mediaAudioItem.directory)));
						
						audioItem.advancedPlayer = new AdvancedPlayer(bis, 0);
						/*
						 * run in new thread to play in background
						 */
						new Thread() {
							public void run() {
								try { 
									
									audioItem.advancedPlayer.play();
								}
								catch (Exception e) { 
									System.out.println(e); 
								}
							}
						}.start();
					
					//lol
					} catch (Exception e) {
					}
				}
				}
			}
		}
		
	}
	
	/**
	 * @return The farthest point on the time-line which contains an item.
	 */
	public static int getFurthestTimelinePoint(){
		int furthestPoint = 0;
		
		/*
		 * Store a local version of the global track array-list (To prevent any concurrency issues).
		 */
		ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();

		if(localTracks != null){
			for(Track trackInstance : localTracks){
			
				int endX = TrackManager.getFurthestTrackPoint(trackInstance);
				if(endX > furthestPoint){
					furthestPoint = endX;
				}
			}
		}
		return furthestPoint;
		
	}
	
	/**
	 * @return The first track which is an Image track.
	 */
	public static Track getFirstImageTrack(){
		/*
		 * Store a local version of the global track array-list (To prevent any concurrency issues).
		 */
		ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();

		if(localTracks != null){
			for(Track trackInstance : localTracks){
			
				if(trackInstance instanceof VideoTrack){
					return trackInstance;
				}
			}
		}
		return null;
		
	}
	
	/**
	 * @return The first track which is an Audio track.
	 */
	public static Track getFirstAudioTrack(){
		/*
		 * Store a local version of the global track array-list (To prevent any concurrency issues).
		 */
		ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();

		if(localTracks != null){
			for(Track trackInstance : localTracks){
			
				if(trackInstance instanceof AudioTrack){
					return trackInstance;
				}
			}
		}
		return null;
		
	}
	
	/**
	 * Rebuild the length of the time-line to match the items placed on it.
	 */
	public static void updateTimelineLength(){
		int furthestPoint = getFurthestTimelinePoint();

		/*
		 * Store a local version of the global track array-list (To prevent any concurrency issues).
		 */
		ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();

		if(localTracks != null){

			
			if(Renderer.pixels.length != furthestPoint+1){
				int[][] temp = Renderer.pixels;
				byte[] temp2 = Renderer.renderingStatus;
				
				
				Renderer.pixels = new int[furthestPoint+1][];
				Renderer.renderingStatus = new byte[furthestPoint+1];
				
				for(int index = 0; index < Renderer.pixels.length; index++){
					
					if(index+1 < temp.length){
						Renderer.pixels[index] = temp[index];
						Renderer.renderingStatus[index] = temp2[index];
					} else {
						Renderer.pixels[index] = null;
						Renderer.renderingStatus[index] = 0;
					}
				}
				
				//updateSize();
				MainApplet.getInstance().getjScrollPane3().getHorizontalScrollBar().setValue(MainApplet.getInstance().getjScrollPane3().getHorizontalScrollBar().getMaximum()+50);
				
				//NewJApplet.instance.getjScrollPane3().revalidate();
				
				//NewJApplet.instance.getTimeline2().setBounds(0,0,Renderer.pixels.length,NewJApplet.instance.getTimeline2().getHeight());
				//NewJApplet.instance.getTimeline2().repaint();
				
				
				
			}
			
			
			
			
			
			if(src.screens.editorScreen.timeline.TimelineManager.timelinePosition > Renderer.pixels.length-1){
				src.screens.editorScreen.timeline.TimelineManager.timelinePosition = Renderer.pixels.length-1;
			}
			
			
			//NewJApplet.instance.getjPanel16().setBounds(0, 0, NewJApplet.instance.getjLayeredPane1().getWidth(), NewJApplet.instance.getjLayeredPane1().getHeight());
			//NewJApplet.instance.getjPanel16().repaint();
			
		}
		
		
		
		
		
		
		
		if(furthestPoint != Renderer.pixels.length){
			//System.out.println(furthestPoint+" / "+Renderer.pixels.length);
		}
		
		
		
		
	}
	
	/**
	 * Resize the panel to match the size of the timeline.
	 */
	public static void updateSize(){
		if(MainApplet.getInstance().getTimeline2() != null){
			MainApplet.getInstance().getTimeline2().setPreferredSize(new Dimension(src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(Renderer.pixels.length)+3,20));
			MainApplet.getInstance().getTimeline2().revalidate();
			
			
		}
	}
	
	/**
	 * Add a video track.
	 */
	public static void addVideoTrack(){
		Track track = new VideoTrack();
		tracks.add(track);
	}
	
	
	/**
	 * Add an audio track.
	 */
	public static void addAudioTrack(){
		Track track = new AudioTrack();
		tracks.add(track);
	}
	
	/**
	 * Add a text track.
	 */
	public static void addTextTrack(){
		Track track = new TextTrack();
		tracks.add(track);
	}
	
	
	
	

	public static double framesPerPixel = 1;
	
	
}
