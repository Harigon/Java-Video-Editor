package src.screens.editorScreen.libraryPanel.transitionPanel;

import src.MainApplet;
import src.Timeline;
import src.TransitionMediaPanel;
import src.screens.editorScreen.timeline.track.transition.Transition;

public class TransitionManager {

	public static String[] categoryNames = new String[]{"Fades/Dissolves"};
	
	
	public static String[][] transitionNames = new String[][]{{"Fade", "Wipe Down"}};
	
	
	
	
	
	/**
	 * Refreshes the panel with updated data.
	 */
	public static void updateTransitionInfoPanel(){
		if(Timeline.selectedTrackObject instanceof Transition){
			Transition transition = (Transition) Timeline.selectedTrackObject;
			MainApplet.getInstance().getjTextField1().setText(""+transition.duration+"");
			
			MainApplet.getInstance().getjTextField1().setEnabled(true);
			
		} else if (TransitionMediaPanel.selectedTransition != -1 && MainApplet.getInstance().getjList1().getSelectedIndex() != -1) {
			
			MainApplet.getInstance().getjTextField1().setEnabled(false);
			MainApplet.getInstance().getjLabel2().setText(transitionNames[MainApplet.getInstance().getjList1().getSelectedIndex()][TransitionMediaPanel.selectedTransition]);
		}
	}
	
	
}
