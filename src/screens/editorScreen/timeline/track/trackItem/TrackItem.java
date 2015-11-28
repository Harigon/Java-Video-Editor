package src.screens.editorScreen.timeline.track.trackItem;

import java.util.ArrayList;

import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.timeline.track.Track;
import src.screens.editorScreen.timeline.track.TrackObject;
import src.screens.editorScreen.timeline.track.trackItem.itemEffect.ItemEffect;
import src.screens.editorScreen.timeline.track.trackItem.itemEffect.itemVideoEffect.ItemVideoEffect;
import src.screens.editorScreen.timeline.track.trackItem.itemEffect.itemVideoEffect.itemVideoEffects.ColourSeperator;
import src.screens.editorScreen.timeline.track.transition.Transition;
import src.screens.editorScreen.timeline.track.transition.TransitionManager;

public abstract class TrackItem extends TrackObject {
//
	public int mediaStartPosition;
	public int trackStartPosition;
	public int mediaDuration;
	
	public int getTrackStartPosition(){
		int position = trackStartPosition;
		Transition transition = TransitionManager.itemPartOfAnyTransition(this);
		if(transition != null){
			if(transition.item2 == this){
				position -= transition.duration;
				//System.out.println("Position: "+position+", instead of: "+trackStartPosition);
			}
		}
		return position;
	}
	
	public ArrayList<ItemEffect> itemEffects = new ArrayList<ItemEffect>(100);
	
	public MediaItem mediaItem;
	public Track track;
	
	public TrackItem(MediaItem mediaItem){
		this.mediaItem = mediaItem;
		
		//ItemVideoEffect videoEffect = new ColourSeperator();
		
		//itemEffects.add(videoEffect);
		
		
		
	}
	
	
}
