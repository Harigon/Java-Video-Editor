package src.screens.editorScreen.libraryPanel.chromaKey;

import javax.swing.JOptionPane;

import src.ChromaKeyFrame;
import src.MainApplet;
import src.screens.editorScreen.libraryPanel.mediaPanel.MediaPanelManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaManager;

public class ChromaKey {

	
	public static int selectedColourProfile = 0;
	public static int selectedTrigger = 0;
	
	public static void addColour(){
		ChromaKeyFrame.addColour = true;
		
		
		
		
	}
	
	public static void newColourProfile(){
		String name = JOptionPane.showInputDialog(null, "Enter a profile name (Max 30 characters)");
		if(name == null){//Make sure the iser 
			JOptionPane.showMessageDialog(null, "Invalid name", "Error", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(name.equals("")){//
			JOptionPane.showMessageDialog(null, "Invalid name", "Error", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(name.length() > 30){
			JOptionPane.showMessageDialog(null, "Name too long!", "Error", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		ColourProfileManager.addColourProfile(name, true);
		refreshProfileNameList();
	}
	
	
	
	public static void newTrigger(){
		String name = JOptionPane.showInputDialog(null, "Enter a trigger name (Max 30 characters)");
		if(name == null){//Make sure the iser 
			JOptionPane.showMessageDialog(null, "Invalid name", "Error", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(name.equals("")){//
			JOptionPane.showMessageDialog(null, "Invalid name", "Error", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(name.length() > 30){
			JOptionPane.showMessageDialog(null, "Name too long!", "Error", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		TriggerManager.addColourProfile(name, true);
		refreshTriggerNameList();
	}
	
	public static void refresh(){
		refreshProfileNameList();
		refreshTriggerNameList();
	}
	
	
	
	public static void refreshProfileNameList(){
		if(ColourProfileManager.getSelectedColourProfileInstance() != null){
			MainApplet.getInstance().getjTextField8().setText(ColourProfileManager.getSelectedColourProfileInstance().name);
			MainApplet.getInstance().getjTextField8().setEnabled(true);
		} else {
			MainApplet.getInstance().getjTextField8().setText("");
			MainApplet.getInstance().getjTextField8().setEnabled(false);
		}
		String[] names = new String[ColourProfileManager.colourProfiles.size()];
		for(int index = 0; index < ColourProfileManager.colourProfiles.size(); index++){
			if(ColourProfileManager.colourProfiles.get(index).name.length() == 0){
				names[index] = "Unnamed";
			} else {
				names[index] = ColourProfileManager.colourProfiles.get(index).name;
			}
		}
		MainApplet.getInstance().getjComboBox11().setModel(new javax.swing.DefaultComboBoxModel(names));
		
		if(ColourProfileManager.colourProfiles.size() > 0)
		MainApplet.getInstance().getjComboBox11().setSelectedIndex(selectedColourProfile);

	}
	
	public static void refreshTriggerNameList(){
		if(TriggerManager.getSelectedTrigger() != null){
			MainApplet.getInstance().getjTextField9().setText(TriggerManager.getSelectedTrigger().name);
			MainApplet.getInstance().getjTextField9().setEnabled(true);
		} else {
			MainApplet.getInstance().getjTextField9().setText("");
			MainApplet.getInstance().getjTextField9().setEnabled(false);
		}
		
		String[] names = new String[TriggerManager.triggers.size()];
		for(int index = 0; index < TriggerManager.triggers.size(); index++){
			if(TriggerManager.triggers.get(index).name.length() == 0){
				names[index] = "Unnamed";
			} else {
				names[index] = TriggerManager.triggers.get(index).name;
			}
		}
		MainApplet.getInstance().getjComboBox13().setModel(new javax.swing.DefaultComboBoxModel(names));
		
		if(TriggerManager.triggers.size() > 0)
		MainApplet.getInstance().getjComboBox13().setSelectedIndex(selectedTrigger);

	}
	
	
}
