package src.screens.editorScreen.timeline.track;

import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;

public class TextTrack extends Track {
//
	public TextTrack() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addTrackItem(MediaItem mediaItem, int mediaStartPosition, int mediaDuration, int trackStartPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTrackType() {
		// TODO Auto-generated method stub
		return "Text";
	}

	@Override
	public int getTrackHeight() {
		// TODO Auto-generated method stub
		return 40;
	}

}
