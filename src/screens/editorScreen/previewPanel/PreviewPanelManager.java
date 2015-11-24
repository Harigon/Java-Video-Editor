package src.screens.editorScreen.previewPanel;

import src.MainApplet;
import src.Timeline;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;

public class PreviewPanelManager {

	
	public static boolean selectedItemView = false;
	
	/**
	 * Updates the GUI with correct states.
	 */
	public static void refreshViewModeButtons(){

		if(selectedItemView){
			MainApplet.instance.getCustomButton2().setSelected(true);
			MainApplet.instance.getCustomButton1().setSelected(false);
		} else {
			MainApplet.instance.getCustomButton2().setSelected(false);
			MainApplet.instance.getCustomButton1().setSelected(true);
		}
	}
	
	
}
