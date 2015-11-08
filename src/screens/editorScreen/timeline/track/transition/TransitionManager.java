package src.screens.editorScreen.timeline.track.transition;

import src.Timeline;
import src.TransitionMediaPanel;
import src.renderer.Renderer;
import src.screens.editorScreen.timeline.track.Track;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;

public class TransitionManager {

	
	/**
	 * @param item1 - Item to check.
	 * @return - The transition which the item is apart of (if any)
	 */
	public static Transition itemPartOfAnyTransition(TrackItem item1){
		if(item1 == null){
			return null;
		}
		Track track = src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(item1);
		for(Transition transition : track.transitions){

			if(transition.item1 == item1 || transition.item2 == item1){
				return transition;
			}
		}
		return null;
	}
	
	/**
	 * @param item1 - Item 1 to check.
	 * @param item2 - Item 2 to check.
	 * @return - Are the items included together in any transitions?
	 */
	public static boolean itemsTogetherInTransition(TrackItem item1, TrackItem item2){
		if(item1 == null){
			return false;
		}
		if(item2 == null){
			return false;
		}
		Track track = src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(item1);
		Track track2 = src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(item2);
		
		if(track != track2){
			return false;
		}
		
		for(Transition transition : track.transitions){

			if(transition.item1 == item1 && transition.item2 == item1){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @param item
	 * @return Is the item at a valid position?
	 */
	public static boolean itemValidPosition(TrackItem item){
		Transition transition = itemPartOfAnyTransition(item);
		return validPositions(transition);
	}
	
	/**
	 * @param transition - The transition to check.
	 * @return Are the items inside this transition in a valid order?
	 */
	public static boolean validPositions(Transition transition){
		return itemsValidPosition(transition.item1, transition.item2);
	}
	
	/**
	 * @param item1
	 * @param item2
	 * @return Are items at valid positions?
	 */
	public static boolean itemsValidPosition(TrackItem item1, TrackItem item2){

		Track track1 = src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(item1);
		Track track2 = src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(item2);

		if(track1 == track2){

			if((item1.trackStartPosition+item1.mediaDuration)+1 == item2.trackStartPosition){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param transition
	 * @return The track which contains this transition.
	 */
	public static Track getTrackForTransition(Transition transition){
		if(src.screens.editorScreen.timeline.TimelineManager.tracks != null){
			for(Track trackInstance : src.screens.editorScreen.timeline.TimelineManager.tracks){
				for(Transition transitionInstance : trackInstance.transitions){
					if(transitionInstance == transition){
						return trackInstance;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @param transition - The transition to remove.
	 */
	public static void removeTransition(Transition transition){
		src.screens.editorScreen.timeline.track.transition.TransitionManager.getTrackForTransition(transition).transitions.remove(transition);
		for(int frameIndex = transition.item1.trackStartPosition; frameIndex < transition.item2.trackStartPosition+transition.item2.mediaDuration+1; frameIndex++){
			if(frameIndex+1 < Renderer.renderingStatus.length){
				Renderer.renderingStatus[frameIndex] = 0;
				
			}
		}
	}
	
	
	/**
	 * Checks a track item and if its position is invalid, removes any transitions containing it.
	 * @param trackItem - Item to check.
	 */
	public static void checkItem(TrackItem trackItem){
		Transition transition = src.screens.editorScreen.timeline.track.transition.TransitionManager.itemPartOfAnyTransition(trackItem);
		if(transition != null){
			if(!src.screens.editorScreen.timeline.track.transition.TransitionManager.validPositions(transition)){
				src.screens.editorScreen.timeline.track.transition.TransitionManager.removeTransition(transition);
			}
		}
	}
	
	
	
}
