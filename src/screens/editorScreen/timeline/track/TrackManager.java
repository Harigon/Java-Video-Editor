package src.screens.editorScreen.timeline.track;

import java.util.ArrayList;

import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
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
		
		int furthestPoint = 0;//This integer contains the furthest point on the track.
		
		/*
		 * Store a local version of the global track-item array-list (To prevent any concurrency issues).
		 */
		ArrayList<TrackItem> localTrackItems = (ArrayList<TrackItem>) track.trackItems.clone();

		/*
		 * Return as 0 if the track items are null.
		 */
		if(localTrackItems == null){
			return 0;
		}
	
		/*
		 * Loop through every item on the track.
		 */
		for(TrackItem trackItem : localTrackItems){
	
			/*
			 * If item is null, continue to next item.
			 */
			if(trackItem == null){
				continue;
			}
	
			/*
			 * Get the point which this item ends on the track.
			 */
			int endX = trackItem.getTrackStartPosition()+trackItem.mediaDuration;
			
			/*
			 * If the furthest point of this item is higher than any of the
			 * items checked so far, update the furtherPoint integer to this
			 * one.
			 */
			if(endX > furthestPoint){
				furthestPoint = endX;
			}
		}
		return furthestPoint;//Return the furthest point.
	}
	
	
	/**
	 * @return Splits an item
	 */
	public static void splitItem(TrackItem trackItem, int splitPosition){
		
		
		/*
		 * Get necessary objects needed to perform this
		 * operation.
		 */
		Track track = TrackManager.getTrackForItem(trackItem);	
		
		int splitFramePosition = trackItem.mediaStartPosition + splitPosition;
		
		/*
		 * Establish the frame positions of the two new segments
		 * which the track item will be split by.
		 * 
		 * (Media frame positions not track positions) 
		 */
		int firstItemStartFrame = trackItem.mediaStartPosition;
		int firstItemEndFrame = splitFramePosition-1;
		int secondItemStartFrame = splitFramePosition;
		int secondItemEndFrame = firstItemStartFrame+trackItem.mediaDuration;
		

		/*
		 * Is the split position invalid?
		 */
		if(splitFramePosition <= firstItemStartFrame || splitFramePosition >= secondItemEndFrame){
			return;
		}

		int secondDuration = secondItemEndFrame-secondItemStartFrame;
		int secondItemTrackStartPos = trackItem.getTrackStartPosition() + splitPosition;

		
		/*
		 * Transform the track item into segment 1
		 */
		trackItem.mediaDuration = firstItemEndFrame-firstItemStartFrame;
		
		/*
		 * Create a new track item for segment 2 and 
		 * add it to the timeline after segment 1.
		 */
		track.addTrackItem(trackItem.mediaItem, secondItemStartFrame, secondDuration, secondItemTrackStartPos);
		
	}
	
	/**
	 * @return Removes an item
	 */
	public static void removeItem(TrackItem item){

		ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();

		if(localTracks != null){

			for(Track trackInstance : localTracks){
				
				
				ArrayList<TrackItem> localTrackItems = (ArrayList<TrackItem>) trackInstance.trackItems.clone();
				
				/*
				 * Loop through every item on the track.
				 */
				for(TrackItem trackItem : localTrackItems){
			
					/*
					 * If item is null, continue to next item.
					 */
					if(trackItem == null){
						continue;
					}
					
					if(trackItem == item){
						
						Transition transition = TransitionManager.itemPartOfAnyTransition(item);
						
						if(transition != null){
							TransitionManager.removeTransition(transition);
						}
						
						trackInstance.trackItems.remove(item);
					}
				}
			}
		}
		
	}
	
	/**
	 * My program must not allow track items to be placed on top of each other on the same track.
	 * This method will check whether this is occurring or not.
	 * 
	 * @param item - The track item to validate.
	 * @return - Is the item position valid?
	 */
	public static boolean validTrackItemPosition(TrackItem item){
		
		/*
		 * Make sure that the item is not null.
		 */
		if(item != null){
	
			/*
			 * Get the track positions of the item.
			 */
			int mainItemStartX = item.getTrackStartPosition();
			int mainItemEndX = item.mediaDuration+mainItemStartX;
	
			
			/*
			 * Make sure that the editor actually contains any tracks.
			 */
			if(TimelineManager.tracks != null){
				
				/*
				 * Grab the track which the item is on.
				 */
				Track trackInstance = TrackManager.getTrackForItem(item);
	
				/*
				 * Check if item is on the correct type of track (i.e image on image track)
				 */
				if(item instanceof TrackImageItem && !(trackInstance instanceof VideoTrack)){
					return false;
				}
				if(item instanceof TrackAudioItem && !(trackInstance instanceof AudioTrack)){
					return false;
				}
				if(item instanceof TrackTextItem && !(trackInstance instanceof TextTrack)){
					return false;
				}
	
				/*
				 * Loop through every other item on the track. (Not the main item)
				 */
				for(TrackItem trackItem : trackInstance.trackItems){
	
					/*
					 * Make sure that this item is NOT the main item.
					 */
					if(trackItem == item){
						continue;
					}
	
					/*
					 * Get the track positions of the item.
					 */
					int startX = trackItem.getTrackStartPosition();
					int endX = trackItem.mediaDuration + startX;
	
	
	
					/*
					 * Make sure that the main item is not positioned ON TOP or is not touching
					 * this item.
					 */
					if((mainItemStartX >= startX && mainItemStartX <= endX) || (mainItemEndX >= startX && mainItemStartX < startX)){
						return false;//Return false if this is the case, as the position is invalid.
					}
	
				}
	
			}}
		return true;//Return true, as no problems have been found with the position.
	}

	

	/**
	 * @param trackItem - The item to find a track for.
	 * @return The track containing that item.
	 */
	public static Track getTrackForItem(TrackItem trackItem){
		/*
		 * Make sure that the editor actually contains any tracks.
		 */
		if(TimelineManager.tracks != null){
			
			/*
			 * Loop through every track.
			 */
			for(Track trackInstance : TimelineManager.tracks){
				
				/*
				 * Loop through every item on that track.
				 */
				for(TrackItem item : trackInstance.trackItems){
					
					/*
					 * Check if the item is the one we are looking for.
					 */
					if(item == trackItem){
						return trackInstance;//Return the track containing the item.
					}
				}
			}
		}
		return null;//Return null as no track was found containing the item.
	}

	/**
	 * @param trackItem - The item to move.
	 * @param newTrack - The track to move it to.
	 */
	public static void moveItemToNewTrack(TrackItem trackItem, Track newTrack){
		/*
		 * Remove item from current track.
		 */
		getTrackForItem(trackItem).trackItems.remove(trackItem);
		
		/*
		 * Add item to new track.
		 */
		newTrack.trackItems.add(trackItem);
	}

	/**
	 * This method returns any track item that is found within a specific region
	 * on the time-line.
	 * 
	 * @param track - The track to use.
	 * @param xFrom - X position to check from. (PIXELS)
	 * @param xTo - X position to check to. (PIXELS)
	 * @return - The first item that was found.
	 */
	public static TrackItem getItemAtTrackRegion(Track track, int xFrom, int xTo){
		/*
		 * Make sure that the track actually exists.
		 */
		if(track != null){
			
			/*
			 * Loop through every item on that track.
			 */
			for(TrackItem trackItem : track.trackItems){
				
				/*
				 * Get the track positions of the item and convert them to screen position.
				 */
				int startX = TrackManager.trackPositionToPixelPosition(trackItem.getTrackStartPosition());
				int endX = TrackManager.trackPositionToPixelPosition(trackItem.mediaDuration + trackItem.getTrackStartPosition());
				
				/*
				 * Check if this item is within the boundary / region.
				 */
				if((xFrom >= startX && xFrom <= endX) || (xTo <= endX && xTo >= startX)){
					return trackItem;//Return the item if so.
				}
			}
		}
		return null;//Return null as no item has been found within the region/boundary.
	}

	/**
	 * This method returns the closest item that is found after a specific
	 * position on the track.
	 * 
	 * @param track - The track to use.
	 * @param position - The X position to check from.
	 * @return - The next item found after that position.
	 */
	public static TrackItem getNextItemAfterPosition(Track track, int position){
		TrackItem closest = null;//This variable will store the closest item.
		
		/*
		 * Make sure that the editor actually contains any tracks.
		 */
		if(TimelineManager.tracks != null){
			
			/*
			 * Loop through every item on the track.
			 */
			for(TrackItem trackItem : track.trackItems){
				
				/*
				 * Make sure that the item is beyond the position to check from.
				 */
				if(trackItem.getTrackStartPosition() > position){
					
					/*
					 * Assign item to the 'closest variable', if it is indeed
					 * the closest found so far.
					 */
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
		return closest;//return the closest item.
	}

	/**
	 * This method returns how far the closest item is, from a specific
	 * position.
	 * 
	 * @param track - The track to use.
	 * @param position - The X position to check from.
	 * @return The distance from the next item on that track.
	 */
	public static int getDistanceFromNextItem(Track track, int position){
		/*
		 * Get the closest item after the position.
		 */
		TrackItem closest = getNextItemAfterPosition(track, position);
		
		/*
		 * Calculate the distance.
		 */
		int distance = -1;
		if(closest != null){
			distance = closest.getTrackStartPosition() - position;
		}
		
		return distance;//Return the calculated distance.
	
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
