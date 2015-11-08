package src.screens.editorScreen.timeline.track.trackItem;

import java.util.ArrayList;

import src.screens.editorScreen.timeline.track.Track;

public class TrackItemManager {

	
	public static TrackItem getTrackForTimelinePosition(int position){
		ArrayList<Track> localTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();
		if(localTracks != null){
			for(Track trackInstance : localTracks){
				ArrayList<TrackItem> localTrackItems = (ArrayList<TrackItem>) trackInstance.trackItems.clone();
				if(localTrackItems == null){
					continue;
				}
				for(TrackItem trackItem : localTrackItems){
					if(trackItem == null){
						continue;
					}
					if(trackItem instanceof TrackAudioItem){
						continue;
					}

					/*
					 * Get the frame/X positions of the item on the track.
					 */
					int startX = trackItem.trackStartPosition;
					int endX = trackItem.trackStartPosition+trackItem.mediaDuration;

					/*
					 * Check if any of the item is positioned at the frame we want to render!
					 */
					if(position >= startX && position <= endX){
						return trackItem;
					}
				}
			}
		}
		return null;
	}
	
}
