package src.screens.editorScreen.timeline.track;

import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaVideoItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackImageItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackVideoItem;

public class VideoTrack extends Track {
//
	public VideoTrack() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addTrackItem(MediaItem mediaItem, int mediaStartPosition,
			int mediaDuration, int trackStartPosition) {
		
		TrackObject trackObject = null;
		if(mediaItem instanceof MediaImageItem){
			trackObject = new TrackImageItem(mediaItem);
		}
		if(mediaItem instanceof MediaVideoItem){
			trackObject = new TrackVideoItem(mediaItem);
		}
		
		
		
		TrackItem trackItem = (TrackItem) trackObject;
		
		trackItem.trackStartPosition = trackStartPosition;
		trackItem.mediaDuration = mediaDuration;
		trackItem.mediaStartPosition = mediaStartPosition;
		
		//System.out.println("added yo: "+trackItem);
		this.trackItems.add(trackItem);
		
		
	}

	@Override
	public String getTrackType() {
		// TODO Auto-generated method stub
		return "Video";
	}

	@Override
	public int getTrackHeight() {
		// TODO Auto-generated method stub
		return 80;
	}



}
