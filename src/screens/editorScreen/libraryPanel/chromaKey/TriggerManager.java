package src.screens.editorScreen.libraryPanel.chromaKey;

import java.util.ArrayList;

public class TriggerManager {

	
	public static ArrayList<TriggerInstance> triggers = new ArrayList<TriggerInstance>(100);
	
	
	public static void addColourProfile(String name, boolean switchToAlbum){
		TriggerInstance trigger = new TriggerInstance(name);
		triggers.add(trigger);
		if(switchToAlbum){
			ChromaKey.selectedTrigger = triggers.indexOf(trigger);
		}
		trigger.colourProfile = ColourProfileManager.colourProfiles.get(0);
	}
	
	
	public static TriggerInstance getSelectedTrigger(){
		if(triggers.size() > ChromaKey.selectedTrigger){
			return triggers.get(ChromaKey.selectedTrigger);
		}
		return null;
	}
	
	
	
	
}
