package src.screens.editorScreen.timeline.track;

import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackAudioItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackImageItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
//
public class AudioTrack extends Track{

	public AudioTrack() {
		super();
	}



	@Override
	public void addTrackItem(MediaItem mediaItem, int mediaStartPosition,
			int mediaDuration, int trackStartPosition) {
		TrackObject trackObject = new TrackAudioItem(mediaItem);
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
		return "Audio";
	}



	@Override
	public int getTrackHeight() {
		// TODO Auto-generated method stub
		return 40;
	}



}
