package src.screens.editorScreen.libraryPanel.mediaPanel.album;

import java.util.ArrayList;

import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;

public class Album {

	public String albumName;
	
	public ArrayList<MediaItem> mediaItems = new ArrayList<MediaItem>(100);
	
	/**
	 * @param Album name
	 */
	public Album(String name){
		albumName = name;
	}
	
	
}
