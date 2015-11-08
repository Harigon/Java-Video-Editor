package src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.mediaPanel.MediaPanelManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaAudioItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaManager;
import src.screens.editorScreen.timeline.TimelineManager;
import src.screens.editorScreen.timeline.track.AudioTrack;
import src.screens.editorScreen.timeline.track.TextTrack;
import src.screens.editorScreen.timeline.track.Track;
import src.screens.editorScreen.timeline.track.VideoTrack;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.transition.Transition;
import src.screens.editorScreen.timeline.track.transition.transitions.Fade;
import src.screens.editorScreen.timeline.track.transition.transitions.WipeDown;
import src.util.Misc;

public class DataManager {

	public static ArrayList<byte[]> undoHistory = new ArrayList<byte[]>();//Stores all previous program states
	public static ArrayList<byte[]> redoHistory = new ArrayList<byte[]>();//Stores all previous undo'd-program states
	
	
	/**
	 * Writes the program data to a byte array.
	 */
	public static byte[] writeDataToByteArray(){
		try {

			
			/*
			 * DataOutputStream is a built in java class which allows data to be written to an output.
			 * -In this case the output is a ByteArrayOutputStream Class.  This is also a built in java class
			 * which can return all of the written data as a single byte array.
			 */
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			DataOutputStream stream = new DataOutputStream(output);
			
			
			
			/*
			 * Write project details
			 */
			stream.writeInt(Project.projectWidth);
			stream.writeInt(Project.projectHeight);
			stream.writeDouble(Project.frameRate);
			
			
			
			
			
			/*
			 * Write media library
			 */
			stream.writeInt(MediaPanelManager.albums.size());
			
			for(Album album : MediaPanelManager.albums){
				
				stream.writeUTF(album.albumName);
				stream.writeInt(album.mediaItems.size());
				
				for(MediaItem mediaItem : album.mediaItems){
					stream.writeUTF(mediaItem.directory);
					if(mediaItem instanceof MediaImageItem){
						stream.writeInt(0);
					}
					if(mediaItem instanceof MediaAudioItem){
						stream.writeInt(1);
					}
					
				}
				
			}
			
			/*
			 * Write time-line track information
			 */
			stream.writeInt(src.screens.editorScreen.timeline.TimelineManager.tracks.size());
			
			for(Track track : src.screens.editorScreen.timeline.TimelineManager.tracks){
				if(track instanceof VideoTrack){
					stream.writeByte(0);
				}
				if(track instanceof AudioTrack){
					stream.writeByte(1);
				}
				if(track instanceof TextTrack){
					stream.writeByte(2);
				}
				
				/*
				 * Loop through and write each track item
				 */
				stream.writeInt(track.trackItems.size());
				for(TrackItem trackItem : track.trackItems){
					stream.writeInt(MediaPanelManager.albums.indexOf(trackItem.mediaItem.thisAlbum));
					stream.writeInt(trackItem.mediaItem.thisAlbum.mediaItems.indexOf(trackItem.mediaItem));
					stream.writeInt(trackItem.trackStartPosition);
					stream.writeInt(trackItem.mediaStartPosition);
					stream.writeInt(trackItem.mediaDuration);
				}
				
				/*
				 * Loop through and write each transition on the track
				 */
				stream.writeInt(track.transitions.size());
				for(Transition transition : track.transitions){
					
					if(transition instanceof Fade){
						stream.writeByte(0);
					}
					if(transition instanceof WipeDown){
						stream.writeByte(1);
					}
					
					
					stream.writeInt(track.trackItems.indexOf(transition.item1));
					stream.writeInt(track.trackItems.indexOf(transition.item2));
					stream.writeInt(transition.duration);
					
				}
				
			}
			
			
			
			
			return output.toByteArray();//Return a single byte array containing all of the data.

		} catch (Exception e){
			e.printStackTrace();
		}

		return null;//Something went wrong with the writing process, so return as null.
	}
	
	/**
	 * Loads/reads the program saved data from a file.
	 */
	public static void loadFromFile(){

		
		/*
		 * Run this process on a separate thread, to stop it freezing the GUI.
		 */
		new Thread() {
			public void run() {

				try {
					/*
					 * File is a built in java class which allows a hard-disk file to be represented as a variable/object.
					 * This variable/object can then be used with other built-in methods to read data.
					 */
					File file = Misc.loadFromFile("DAT File (*.dat)", new String[]{"dat", ".dat"});

					
					if(file == null){
						return;
					}
					
					/*
					 * Setup the progress bar
					 */
					MainApplet.instance.getjLabel19().setText("");
					MainApplet.instance.getjLabel20().setText("0%");
					MainApplet.instance.getjProgressBar1().setValue(0);
					MainApplet.instance.getjLabel21().setText("Importing albums...");
					MainApplet.instance.getjInternalFrame2().setTitle("Importing");
					MainApplet.instance.getjInternalFrame2().setVisible(true);
					
					
					if(file.exists()){//Make sure the file actually exists for attempting to read from it.
						byte[] dataBytes = Misc.getBytesFromFile(file);//Converts the file into an array of bytes.
						readFromByteArray(dataBytes);//Load the file, with the array of bytes as the parameter.
					}
					
					MainApplet.instance.getjInternalFrame2().setVisible(false);//Hide progress bar
					MainApplet.getInstance().getjPanel16().setVisible(true);//Show editor screen
				} catch (Exception e){
					e.printStackTrace();
				}
			}


		}.start();
	}
	
	
	/**
	 * Loads/reads the program saved data from a byte array.
	 * @param dataBytes - The bytes containing the file data.
	 */
	public static void readFromByteArray(byte[] dataBytes){
		try {
			
			
			/*
			 * DataInputStream is a built in java class which allows data to be read from an input.
			 * -In this case the input is a ByteArrayInputStream Class.  This is also a built in java class
			 * which converts a normal array of bytes into an input stream for the 'DataInputStream' to use.
			 */
			ByteArrayInputStream input = new ByteArrayInputStream(dataBytes);
			DataInputStream stream = new DataInputStream(input);
			
			/*
			 * Read project details
			 */
			int projectWidth = stream.readInt();
			int projectHeight = stream.readInt();
			double frameRate = stream.readDouble();
			Project.setupProject(projectWidth, projectHeight, frameRate);
			
			
			/*
			 * Read media library
			 */
			int totalAlbums = stream.readInt();
			MediaPanelManager.albums.clear();
			for(int albumIndex = 0; albumIndex < totalAlbums; albumIndex++){
				String albumName = stream.readUTF();
				
				Album album = MediaPanelManager.createAlbum(albumName, false);
				
				int totalMediaItems = stream.readInt();
				
				
				MainApplet.instance.getjLabel19().setText("Album "+(albumIndex+1)+" / "+totalAlbums);
				
				for(int mediaItemIndex = 0; mediaItemIndex < totalMediaItems; mediaItemIndex++){
					
					
					
					int currentPoints = (mediaItemIndex+1);
					int goalPoints = (totalMediaItems);
					int percent = (int) (currentPoints * 100) / goalPoints; 
					if(percent > 100){
						percent = 100;
					}
					if(percent < 0){
						percent = 0;
					}
					
					MainApplet.instance.getjLabel20().setText(percent+"%");
					
					MainApplet.instance.getjProgressBar1().setValue(percent);
					
					
					String directory = stream.readUTF();
					
					int mediaItemType = stream.readInt();
					
					MediaManager.addFile(new File(directory), album);
				}
				
			}
			
			/*
			 * Read time-line track information
			 */
			int totalTracks = stream.readInt();
			src.screens.editorScreen.timeline.TimelineManager.tracks.clear();
			for(int trackIndex = 0; trackIndex < totalTracks; trackIndex++){
				int trackType = stream.readByte();
				
				Track track = null;
				
				if(trackType == 0){
					track = new VideoTrack();
				}
				if(trackType == 1){
					track = new AudioTrack();
				}
				if(trackType == 2){
					track = new TextTrack();
				}
				src.screens.editorScreen.timeline.TimelineManager.tracks.add(track);
				
				/*
				 * Loop through and read each track item
				 */
				int totalTrackItems = stream.readInt();
				track.trackItems.clear();
				for(int trackItemIndex = 0; trackItemIndex < totalTrackItems; trackItemIndex++){
					Album album = MediaPanelManager.albums.get(stream.readInt());
					MediaItem mediaItem = album.mediaItems.get(stream.readInt());
					
					int trackStartPosition = stream.readInt();
					int mediaStartPosition = stream.readInt();
					int mediaDuration = stream.readInt();
					
					track.addTrackItem(mediaItem, mediaStartPosition, mediaDuration, trackStartPosition);
					
				}
				
				/*
				 * Loop through and read each transition on the track
				 */
				
				int totalTransitions = stream.readInt();
				track.transitions.clear();
				for(int transitionIndex = 0; transitionIndex < totalTransitions; transitionIndex++){
					
					Transition transition = null;
					byte transitionType = stream.readByte();
					
					
					TrackItem item1 = track.trackItems.get(stream.readInt());
					TrackItem item2 = track.trackItems.get(stream.readInt());
					
					if(transitionType == 0){
						transition = new Fade(item1, item2);
					}
					if(transitionType == 1){
						transition = new WipeDown(item1, item2);
					}
					
					
					System.out.println("item1: "+item1.trackStartPosition+", item2: "+item2.trackStartPosition);
					
					transition.duration = stream.readInt();
					
					track.transitions.add(transition);
				}
				
				
			}
			
			
			
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves the current program state.
	 * @param saveToFile TODO
	 * @param addUndoPoint - Adds an undo check point, allowing the program to return to this state in the future.
	 */
	public static void saveData(boolean addUndoPoint){
		byte[] writtenData = writeDataToByteArray();//Converts the current state of the program into a byte array.
		
		/*
		 * This section is responsible for actually storing a copy of the programs current state.
		 * -This state can be loaded again later on, if the user wants to undo a change.
		 */
		if(addUndoPoint){
			undoHistory.add(writtenData);//Adds the current state of the program to a global list (arraylist)
			/*
			 * The small section below ensures that the undo system won't consume too much memory.
			 * -If 20 previous states of the program already exist, it removes the first one to make room for this new one.
			 * (This means that the program will only store a maximum of 20 previous states.)
			 */
			if(undoHistory.size() == 20){
				undoHistory.remove(0);//Remove from global list
			}
			/*
			 * 
			 * Anything stored in the list below is no longer needed because this list contains future states of the previous
			 * program state. As we now have a new program state, we no longer have any future states of this.
			 * (This list now contains future states which are for the wrong program state, so we need to clear it.)
			 */
			redoHistory.clear();//Clear this data as its no longer relevant/needed.
		}
	}
	
	/**
	 * Saves the current program state.
	 * @param saveToFile TODO
	 * @param addUndoPoint - Adds an undo check point, allowing the program to return to this state in the future.
	 */
	public static void saveDataToFile(){
		byte[] writtenData = writeDataToByteArray();//Converts the current state of the program into a byte array.
		writeToFile(writtenData);
	}
	
	/**
	 * Writes the program saved data to a file.
	 * @param dataBytes - The bytes containing the file data.
	 */
	public static void writeToFile(byte[] dataBytes){
		try {
			/*
			 * DataOutputStream is a built in java class which allows data to be written to an output.
			 * -In this case the output is a FileOutputStream Class.  This is also a built in java class
			 * which can write all of the stream data to a file on the hard-disk.
			 */
			
			File file = Misc.saveToDirectory("DAT File (*.dat)", new String[]{"dat", ".dat"});
			
			FileOutputStream output = new FileOutputStream(file);
			DataOutputStream stream = new DataOutputStream(output);
			stream.write(dataBytes);
			stream.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
}
