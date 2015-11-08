package src.screens.editorScreen.libraryPanel.mediaPanel.album.media;

import java.awt.image.BufferedImage;

import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;

public abstract class MediaItem {
//
	public String directory;
	public Album thisAlbum;
	
	public MediaItem(String directory, Album album){
		this.directory = directory;
		this.thisAlbum = album;
	}
	
	public abstract BufferedImage getThumbnail();
	
}
