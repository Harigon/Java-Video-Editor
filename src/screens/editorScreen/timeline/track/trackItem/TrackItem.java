package src.screens.editorScreen.timeline.track.trackItem;

import java.util.ArrayList;

import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.timeline.track.Track;
import src.screens.editorScreen.timeline.track.TrackObject;
import src.screens.editorScreen.timeline.track.trackItem.itemEffect.ItemEffect;
import src.screens.editorScreen.timeline.track.trackItem.itemEffect.itemVideoEffect.ItemVideoEffect;
import src.screens.editorScreen.timeline.track.trackItem.itemEffect.itemVideoEffect.itemVideoEffects.ColourSeperator;

public abstract class TrackItem extends TrackObject {
//
	public int mediaStartPosition;
	public int trackStartPosition;
	public int mediaDuration;
	
	
	
	public ArrayList<ItemEffect> itemEffects = new ArrayList<ItemEffect>(100);
	
	public MediaItem mediaItem;
	public Track track;
	
	public TrackItem(MediaItem mediaItem){
		this.mediaItem = mediaItem;
		
		//ItemVideoEffect videoEffect = new ColourSeperator();
		
		//itemEffects.add(videoEffect);
		
		
		
	}
	
	
}
