package src.screens.editorScreen.libraryPanel.mediaPanel.album.media;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import src.MainApplet;
import src.MediaPanel;
import src.dataStore.DataStore;
import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.mediaPanel.MediaPanelManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.MultiItemAdder;
import src.screens.editorScreen.timeline.TimelineManager;
import src.screens.editorScreen.timeline.track.Track;
import src.screens.editorScreen.timeline.track.TrackManager;
import src.util.Misc;


public class MediaManager {
	//
	//public static ArrayList<MediaItem> mediaItems = new ArrayList<MediaItem>(100);
	
	
	//public static Album selectedAlbum = null;
	
	public static int selectedAlbum = 0;
	
	/**
	 * @param album - The album to add the image to.
	 * @param directory - The hard-drive directory of the image.
	 */
	public static void addImage(Album album, String directory){
		MediaItem mediaItem = new MediaImageItem(directory, album);
		album.mediaItems.add(mediaItem);
	}
	

	
	public static void addVideo(Album album, String directory, int formatType){
		MediaItem mediaItem = new MediaVideoItem(directory, album, formatType);
		album.mediaItems.add(mediaItem);
	}
	
	/**
	 * @param album - The album to add the audio to.
	 * @param directory - The hard-drive directory of the audio.
	 */
	public static void addAudio(Album album, String directory){
		MediaItem mediaItem = new MediaAudioItem(directory, album);
		album.mediaItems.add(mediaItem);
	}
	
	/**
	 * Automatically adds all of the items from selected album to the time-line.
	 */
	public static void addAllToTimeline(){


		final Track track = TimelineManager.getFirstImageTrack();

		if(track == null){
			System.out.println("No available tracks.");
			return;
		}


		if(getSelectedAlbum() != null){
			//MultiItemAdder.open(getSelectedAlbum());



			try{



				for(MediaItem mediaItem : getSelectedAlbum().mediaItems){




					if(mediaItem instanceof MediaImageItem){
						MediaImageItem mediaImageItem = (MediaImageItem) mediaItem;


						MediaPanel.track.addTrackItem(mediaImageItem, 0, 100, TrackManager.getFurthestTrackPoint(track)+1);
					}
				}


				MainApplet.instance.getjInternalFrame2().setVisible(false);

			} catch (Exception e){
				e.printStackTrace();
			}


		}

	}
	
	/**
	 * @return The Album which has been selected on the GUI combo-box.
	 */
	public static Album getSelectedAlbum(){
		
		if(MediaPanelManager.albums.size() > selectedAlbum){
			return MediaPanelManager.albums.get(selectedAlbum);
		}
		
		return null;
	}
	
	/**
	 * Imports media from a file.
	 */
	public static void importMedia(){
		try {
			File file = Misc.loadFromFile(null, null);
			addFile(file, MediaPanelManager.albums.get(selectedAlbum));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param file - The file to add.
	 * @param album - The album to add the file too.
	 */
	public static void addFile(File file, Album album){
		
		if(file == null){
			return;
		}
		
		if (!file.isDirectory()) {

			String name = file.getName();
			int pos = name.lastIndexOf('.');
			String ext = name.substring(pos+1);

			if(ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg")){
				MediaManager.addImage(album, file.getAbsolutePath());
			}
			
			if(ext.equalsIgnoreCase("gif")){
				MediaManager.addVideo(album, file.getAbsolutePath(), 0);
			}
			
			if(ext.equalsIgnoreCase("mp4")){
				MediaManager.addVideo(album, file.getAbsolutePath(), 1);
			}
			
			if(ext.equalsIgnoreCase("mov")){
				MediaManager.addVideo(album, file.getAbsolutePath(), 1);
			}
			
			if(ext.equalsIgnoreCase("mp3")){
				MediaManager.addAudio(album, file.getAbsolutePath());
			}
		}
	}
	
	/**
	 * Import an entire folder and add all of the files.
	 */
	public static void importFolder(){

		/*
		 * Run this process on a separate thread, to stop it freezing the GUI.
		 */
		new Thread() {
			public void run() {


				try {
					File[] files = Misc.selectFolder().listFiles();

					if(files == null){
						return;
					}

					MainApplet.instance.getjLabel19().setText("");
					MainApplet.instance.getjLabel20().setText("0%");
					MainApplet.instance.getjProgressBar1().setValue(0);
					MainApplet.instance.getjLabel21().setText("Importing items...");
					MainApplet.instance.getjInternalFrame2().setTitle("Importing");

					MainApplet.instance.getjInternalFrame2().setVisible(true);


					int count = 0;


					for (File file : files) {
						int currentPoints = (count+1);
						int goalPoints = files.length;
						int percent = (int) (currentPoints * 100) / goalPoints; 
						if(percent > 100){
							percent = 100;
						}
						if(percent < 0){
							percent = 0;
						}

						MainApplet.instance.getjLabel20().setText(percent+"%");

						MainApplet.instance.getjProgressBar1().setValue(percent);

						addFile(file, MediaPanelManager.albums.get(selectedAlbum));
						count++;
					}
					MainApplet.instance.getjInternalFrame2().setVisible(false);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}


		}.start();
	}
	
	
	
	
}
