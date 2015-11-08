package src.screens.editorScreen.libraryPanel.chromaKey;

import java.util.ArrayList;

public class ColourProfileInstance {

	
	public ArrayList<ColourInstance> allowedColours = new ArrayList<ColourInstance>(100);
	public ArrayList<ColourInstance> filteredColours = new ArrayList<ColourInstance>(100);
	
	
	public String name;
	
	public ColourProfileInstance(String name){
		this.name = name;
	}
	
	public void addAllowedColour(int colour){
		ColourInstance colourInstance = new ColourInstance(colour);
		allowedColours.add(colourInstance);
	}
	public void addFilteredColour(int colour){
		ColourInstance colourInstance = new ColourInstance(colour);
		filteredColours.add(colourInstance);
	}
	
	
}
