package src.screens.editorScreen.timeline.track;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;

import src.MainApplet;
import src.TrackManagerPanel;
import src.dataStore.DataStore;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.timeline.TimelineManager;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.transition.Transition;

public abstract class Track {
//
	
	public int trackType;
	public Track instance;
	public javax.swing.JButton downButton;
	public javax.swing.JButton upButton;
	public src.CustomButton customButton2;
	
	public ArrayList<TrackItem> trackItems = new ArrayList<TrackItem>(100);
	
	public ArrayList<Transition> transitions = new ArrayList<Transition>(100);

	
	public abstract String getTrackType();
	public abstract int getTrackHeight();
	
	public Track(){
		instance = this;
		//this.trackType = trackType;
		downButton = new javax.swing.JButton();
		upButton = new javax.swing.JButton();
		customButton2 = new src.CustomButton();
		
		 customButton2.setText("Selected Media");
	        customButton2.setIcon(DataStore.getImageIcon(9));
	        customButton2.setOffset(0);
	        customButton2.setSelectedIcon(DataStore.getImageIcon(8));
	        customButton2.setRolloverEnabled(true); // turn on before rollovers work
	        customButton2.setRolloverIcon(DataStore.getImageIcon(7));
	        customButton2.setBorder(null);
		
		
		upButton.setBorder(null);
		upButton.setIcon(DataStore.getImageIcon(17));
		upButton.setSelectedIcon(DataStore.getImageIcon(19));
		upButton.setRolloverEnabled(true); // turn on before rollovers work
		upButton.setRolloverIcon(DataStore.getImageIcon(18));
		
		
		upButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	int position = TimelineManager.tracks.indexOf(instance);
            	if(position > 0){
            		Collections.swap(TimelineManager.tracks, position, position-1);
            		TrackManagerPanel.update();
            		src.Timeline.update();
            	}
            	
            }
        });
		
		downButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	int position = TimelineManager.tracks.indexOf(instance);
            	if(position < TimelineManager.tracks.size()-1){
            		Collections.swap(TimelineManager.tracks, position, position+1);
            		TrackManagerPanel.update();
            		src.Timeline.update();
            	}
            	
            }
        });
		
		downButton.setBorder(null);
		downButton.setIcon(DataStore.getImageIcon(20));
		downButton.setSelectedIcon(DataStore.getImageIcon(21));
		downButton.setRolloverEnabled(true); // turn on before rollovers work
		downButton.setRolloverIcon(DataStore.getImageIcon(22));
		
		
		
		MainApplet.getInstance().getTrackManagerPanel1().add(downButton);
		MainApplet.getInstance().getTrackManagerPanel1().add(upButton);
		MainApplet.getInstance().getTrackManagerPanel1().add(customButton2);
		
		
	}
	
	public abstract void addTrackItem(MediaItem mediaItem, int mediaStartPosition, int mediaDuration, int trackStartPosition);
	

	
	
}
