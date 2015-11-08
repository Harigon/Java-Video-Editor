package src.multiThreading.threads;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaManager;


public class WorkThread {
	

	
	/**
	 * Start the thread.
	 */
	public static void run() {
		/*
		 * Run this process on a separate thread, to stop it freezing the GUI.
		 */
		new Thread() {
			public void run() {
				while(true) {
					processTasks();
				}
			}
		}.start();
	}
	
	public static boolean turnedOn = true;
	
	
	public static int requestedFrame = -1;
	
	
	
	
	/**
	 * Carry out any tasks required by this thread.
	 */
	public static void processTasks(){
		if(turnedOn){

			
			
			
			/*if(MediaManager.getSelectedAlbum() != null){
				if(MediaManager.getSelectedAlbum().mediaItems != null){

					ArrayList<MediaItem> tempMediaItems = (ArrayList<MediaItem>) MediaManager.getSelectedAlbum().mediaItems.clone();
					
					for(MediaItem mediaItem : tempMediaItems){
						if(mediaItem instanceof MediaImageItem){
							MediaImageItem mediaImageItem = (MediaImageItem) mediaItem;
							if(!mediaImageItem.createdPreviewPixels){
								//mediaImageItem.createPreviewPixels();
							}
						}
					}
				}
			}*/

		}

	}
	
	
	
	
	
	
	
	
}
