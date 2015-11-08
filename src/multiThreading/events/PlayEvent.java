package src.multiThreading.events;

import src.multiThreading.Event;

public class PlayEvent extends Event{
	
public static boolean playing = false;

		public PlayEvent() {
			super(1000/30);
		}

		@Override
		public void execute() {
			if(playing){
				src.screens.editorScreen.timeline.TimelineManager.incrementTimelinePosition();
				
			}
		}
}
