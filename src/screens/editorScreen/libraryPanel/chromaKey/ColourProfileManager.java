package src.screens.editorScreen.libraryPanel.chromaKey;

import java.util.ArrayList;

import src.screens.editorScreen.libraryPanel.mediaPanel.MediaPanelManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaManager;

public class ColourProfileManager {

	
	public static ArrayList<ColourProfileInstance> colourProfiles = new ArrayList<ColourProfileInstance>(100);
	
	
	public static void addColourProfile(String name, boolean switchToAlbum){
		ColourProfileInstance colourProfileInstance = new ColourProfileInstance(name);
		colourProfiles.add(colourProfileInstance);
		if(switchToAlbum){
			ChromaKey.selectedColourProfile = colourProfiles.indexOf(colourProfileInstance);
		}
		
		
	//	ColourInstance colour = new 
		
	}
	
	
	public static ColourProfileInstance getSelectedColourProfileInstance(){
		if(colourProfiles.size() > ChromaKey.selectedColourProfile){
			return colourProfiles.get(ChromaKey.selectedColourProfile);
		}
		return null;
	}
	
	
}
