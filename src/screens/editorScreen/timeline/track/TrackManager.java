package src.screens.editorScreen.timeline.track;

import java.util.ArrayList;

import src.screens.editorScreen.timeline.TimelineManager;
import src.screens.editorScreen.timeline.track.trackItem.TrackAudioItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackImageItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackTextItem;
import src.screens.editorScreen.timeline.track.transition.Transition;
import src.screens.editorScreen.timeline.track.transition.TransitionManager;

public class TrackManager {

	/**
	 * @param track - The track to use.
	 * @return The furthest point an item is on the track.
	 */
	public static int getFurthestTrackPoint(Track track){
		
		
		int furthestPoint = 0;
		
		/*
		 * Store a local version of the global track-item array-list (To prevent any concurrency issues).
		 */
		ArrayList<TrackItem> localTrackItems = (ArrayList<TrackItem>) track.trackItems.clone();
	
	
		if(localTrackItems == null){
			return 0;
		}
	
		for(TrackItem trackItem : localTrackItems){
	
			if(trackItem == null){
				continue;
			}
	
			int endX = trackItem.getTrackStartPosition()+trackItem.mediaDuration;
			if(endX > furthestPoint){
				furthestPoint = endX;
			}
		}
		return furthestPoint;
	}

	/**
	 * @param item - The track item to validate.
	 * @return - Is the item position valid?
	 */
	public static boolean validTrackItemPosition(TrackItem item){
		if(item != null){
	
			int draggedStartX = item.trackStartPosition;
			int draggedEndY = item.mediaDuration+draggedStartX - (item.trackStartPosition-item.getTrackStartPosition());
	
			if(TimelineManager.tracks != null){
	
	
				Track trackInstance = TrackManager.getTrackForItem(item);
	
				
				if(item instanceof TrackImageItem && !(trackInstance instanceof VideoTrack)){
					return false;
				}
				if(item instanceof TrackAudioItem && !(trackInstance instanceof AudioTrack)){
					return false;
				}
				if(item instanceof TrackTextItem && !(trackInstance instanceof TextTrack)){
					return false;
				}
	
				for(TrackItem trackItem : trackInstance.trackItems){
	
					if(trackItem == item){
						continue;
					}
	
					int startX = trackItem.trackStartPosition;
					int endX = trackItem.mediaDuration + startX - (trackItem.trackStartPosition-trackItem.getTrackStartPosition());;
	
	
					Transition transition = TransitionManager.itemPartOfAnyTransition(trackItem);
	
					//boolean isTransition = false;
					
					//if(transition != null){
						//if(transition.item2 == trackItem){
							//isTransition = true;
						//}
					//}
					
					if(((draggedStartX >= startX && draggedStartX <= endX) /*&& !isTransition*/) || (draggedEndY >= startX && draggedStartX < startX)){
						return false;
					}
	
				}
	
			}}
		return true;
	}
	
	
	
	

	/**
	 * @param trackItem - The item to find a track for.
	 * @return The track containing that item.
	 */
	public static Track getTrackForItem(TrackItem trackItem){
		if(TimelineManager.tracks != null){
			for(Track trackInstance : TimelineManager.tracks){
				for(TrackItem item : trackInstance.trackItems){
					if(item == trackItem){
						return trackInstance;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param trackItem - The item to move.
	 * @param newTrack - The track to move it to.
	 */
	public static void moveItemToNewTrack(TrackItem trackItem, Track newTrack){
		getTrackForItem(trackItem).trackItems.remove(trackItem);
		newTrack.trackItems.add(trackItem);
	}

	/**
	 * @param track - The track to use.
	 * @param xFrom - X position to check from.
	 * @param xTo - X position to check to.
	 * @return - The first item that was found.
	 */
	public static TrackItem getItemAtTrackRegion(Track track, int xFrom, int xTo){
		if(track != null){
			for(TrackItem trackItem : track.trackItems){
				int startX = TrackManager.trackPositionToPixelPosition(trackItem.getTrackStartPosition());
				int endX = TrackManager.trackPositionToPixelPosition(trackItem.mediaDuration + trackItem.getTrackStartPosition());
				if((xFrom >= startX && xFrom <= endX) || (xTo <= endX && xTo >= startX)){
					return trackItem;
				}
			}
		}
		return null;
	}

	/**
	 * @param track - The track to use.
	 * @param position - The X position to check from.
	 * @return - The next item found after that position.
	 */
	public static TrackItem getNextItemAfterPosition(Track track, int position){
		TrackItem closest = null;
		if(TimelineManager.tracks != null){
			for(TrackItem trackItem : track.trackItems){
				if(trackItem.getTrackStartPosition() > position){
					if(closest != null){
						if(trackItem.getTrackStartPosition() < closest.getTrackStartPosition()){
							closest = trackItem;
						}
					} else {
						closest = trackItem;
					}
				}
			}
		}
		return closest;
	}

	/**
	 * @param track - The track to use.
	 * @param position - The X position to check from.
	 * @return The distance from the next item on that track.
	 */
	public static int getDistanceFromNextItem(Track track, int position){
		TrackItem closest = getNextItemAfterPosition(track, position);
		int distance = -1;
		if(closest != null){
			distance = closest.getTrackStartPosition() - position;
		}
		return distance;
	
	}

	/**
	 * @param position - Track position.
	 * @return Pixel position.
	 */
	public static int trackPositionToPixelPosition(int position){
		return (int) ((position/TimelineManager.framesPerPixel)+2);
	}

	/**
	 * @param position - Track position.
	 * @return Pixel position.
	 */
	public static int trackPositionToPixelPosition2(int position){
		return (int) ((position/TimelineManager.framesPerPixel)+2);
	}

	/**
	 * @param position - Pixel position.
	 * @return Track position.
	 */
	public static int pixelPositionToTrackPosition(int position){
		position -= 2;
		
		return (int) ((position*TimelineManager.framesPerPixel));
	}

}
