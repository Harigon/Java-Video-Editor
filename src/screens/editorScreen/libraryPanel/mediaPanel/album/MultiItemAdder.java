package src.screens.editorScreen.libraryPanel.mediaPanel.album;

import src.MainApplet;

public class MultiItemAdder {

	
	public static int imageDuration = 100;
	
	public static void open(Album album){
		
		imageDuration = 100;
		
		MainApplet.instance.getjInternalFrame1().setVisible(true);
		
		MainApplet.instance.getjLabel10().setText("Total items: "+album.mediaItems.size());
		
		/*
		 * Image settings
		 */
		MainApplet.instance.getjTextField6().setText("100");
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
}
