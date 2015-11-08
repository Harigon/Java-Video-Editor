package src.multiThreading.threads;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import src.ChromaKeyFrame;
import src.MainApplet;
import src.MediaPanel;
import src.Timeline;
import src.TransitionMediaPanel;
import src.dataStore.DataStore;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;


public class TaskThread {
	

	public static final long[] aLongArray7 = new long[10];
	public static int delayTime = 20;
	public static int fps;
	static int minDelay = 1;
	
	public static Image cursorImage;
	public static Image lastCursorImage;
	//public static int 
	
	
	public static boolean editorRunning = false;
	
	
	/**
	 * Refreshes the size of the editor panel to match the size of the program (i.e resized)
	 */
	public static void updateSizes(){
			MainApplet.getInstance().getjPanel16().setBounds(0, 0, MainApplet.getInstance().getjLayeredPane1().getWidth(), MainApplet.getInstance().getjLayeredPane1().getHeight());
			MainApplet.getInstance().getjPanel16().repaint();
	}
	
	/**
	 * Carries out any tasks required by this thread.
	 */
	public static void processTasks(){
		MainApplet.getInstance().processEvents();
		Timeline.update();
		TransitionMediaPanel.update();
		MediaPanel.update();

		
		if(MainApplet.getInstance().getjPanel16().getWidth() != MainApplet.getInstance().getjLayeredPane1().getWidth() || MainApplet.getInstance().getjPanel16().getHeight() != MainApplet.getInstance().getjLayeredPane1().getHeight()){
			updateSizes();
		}
		
		
		if(editorRunning == false){
		//	NewJApplet.instance.getjPanel15().setBounds(0, 0, NewJApplet.instance.getjLayeredPane1().getWidth(), NewJApplet.instance.getjLayeredPane1().getHeight());
		//NewJApplet.instance.getjInternalFrame2().toFront();
		
		MainApplet.getInstance().getjLayeredPane1().moveToFront(MainApplet.getInstance().getjPanel15());
		
		//NewJApplet.instance.getjPanel15().repaint();
		
		
		
		} else {
			//NewJApplet.instance.getjPanel16().setBounds(0, 0, NewJApplet.instance.getjLayeredPane1().getWidth(), NewJApplet.instance.getjLayeredPane1().getHeight());
			//NewJApplet.instance.getjPanel16().repaint();
		
		}
		
		
		
		
		
		if(TransitionMediaPanel.draggedTransition != -1){
			if(TransitionMediaPanel.hoveredTransition != TransitionMediaPanel.draggedTransition){
				if(TransitionMediaPanel.transitionValidPosition){
					Toolkit toolkit = Toolkit.getDefaultToolkit(); 
					Cursor cursor = toolkit.createCustomCursor(DataStore.spriteLoadFile(25), new Point(10,0), "cursor4");
					MainApplet.getInstance().setCursor(cursor);
				} else {
					Toolkit toolkit = Toolkit.getDefaultToolkit(); 
					Cursor cursor = toolkit.createCustomCursor(DataStore.spriteLoadFile(24), new Point(10,0), "cursor5");
					MainApplet.getInstance().setCursor(cursor);
				}
			}
		} else if(MediaPanel.draggedMediaItem != null){
			if(MediaPanel.hoveredMediaItem != MediaPanel.draggedMediaItem){
				if(MediaPanel.transitionValidPosition){
					Toolkit toolkit = Toolkit.getDefaultToolkit(); 
					Cursor cursor = toolkit.createCustomCursor(DataStore.spriteLoadFile(25), new Point(10,0), "cursor6");
					MainApplet.getInstance().setCursor(cursor);
				} else {
					Toolkit toolkit = Toolkit.getDefaultToolkit(); 
					Cursor cursor = toolkit.createCustomCursor(DataStore.spriteLoadFile(24), new Point(10,0), "cursor6");
					MainApplet.getInstance().setCursor(cursor);
				}
			} 
		} else if(Timeline.itemTrimHover == 2){
			Toolkit toolkit = Toolkit.getDefaultToolkit(); 
			Cursor cursor = toolkit.createCustomCursor(DataStore.spriteLoadFile(23), new Point(10,0), "cursor3");
			MainApplet.getInstance().setCursor(cursor);
			
		} else if(ChromaKeyFrame.addColour){
			Toolkit toolkit = Toolkit.getDefaultToolkit(); 
			Cursor cursor = toolkit.createCustomCursor(DataStore.spriteLoadFile(29), new Point(0,12), "cursor7");
			MainApplet.getInstance().setCursor(cursor);
		} else {
			MainApplet.getInstance().setCursor(null);
		}
		
		
		src.screens.editorScreen.timeline.TimelineManager.updateTimelineLength();
		
		//NewJApplet.instance.getTimeline1().paintComponent(NewJApplet.instance.getTimeline1().getGraphics());

	
		//NewJApplet.instance.getjScrollPane3().setSize(NewJApplet.instance.getjScrollPane3().getWidth(), 300);
	
		//NewJApplet.instance.getjScrollPane3().setSize(NewJApplet.instance.getjScrollPane3().getWidth(), 200);
		//NewJApplet.instance.getjScrollPane3().validate();
		
		//System.out.println(NewJApplet.instance.getjScrollPane3().getVisibleRect().height);
		
	}
	
	
	
	
	
	
	
	
}
