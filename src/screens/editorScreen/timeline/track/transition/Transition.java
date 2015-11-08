package src.screens.editorScreen.timeline.track.transition;

import src.screens.editorScreen.timeline.track.TrackObject;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;

public abstract class Transition extends TrackObject {

	public TrackItem item1;
	public TrackItem item2;
	public int duration;
	
	//Item 1 and item 2 can be in any order
	public Transition(TrackItem item1, TrackItem item2){
		this.item1 = item1;
		this.item2 = item2;
	}
	
	
	/**
	 * @param currentPixels - Pixels of current frame.
	 * @param nextPixels - Pixels of the first frame of next item.
	 * @param distanceFromNext - Distance (frames) from the next item.
	 * @param transitionDuration - Duration (frames) of the transition.
	 * @param width - Width of the render size.
	 * @param height - Height of the render size.
	 * @return
	 */
	public abstract int[] renderTransitionFrame(int[] currentPixels, int[] nextPixels, int distanceFromNext, int transitionDuration, int width, int height);
	
	
	
	
	
	
}
