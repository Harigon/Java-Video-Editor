package src.screens.editorScreen.libraryPanel.mediaPanel;

import java.util.ArrayList;

import src.MainApplet;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaManager;

public class MediaPanelManager {
public static ArrayList<Album> albums = new ArrayList<Album>(100);
	
	/**
	 * @param name - The name of the album.
	 * @param switchToAlbum - Visually display this album once its been created?
	 * @return
	 */
	public static Album createAlbum(String name, boolean switchToAlbum){
		Album album = new Album(name);
		albums.add(album);
		if(switchToAlbum){
			MediaManager.selectedAlbum = albums.indexOf(album);
		}
		MainApplet.getInstance().refreshAlbumNameList();
		return album;
	}
}
