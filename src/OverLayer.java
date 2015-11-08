package src;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

import javax.swing.JPanel;

import src.screens.editorScreen.libraryPanel.mediaPanel.MediaPanelManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaManager;
import src.screens.editorScreen.libraryPanel.transitionPanel.TransitionManager;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.transition.Transition;


@SuppressWarnings("serial")
public class OverLayer extends JPanel
{
	
	
	
	public OverLayer() {
	//	buttons.add(new ButtonInstance(this, 5, 5));
	}
	
	public OverLayer(Image img)
	{
		this.setBackground(Color.white);
	}
	
	
	public static void update(){
		MainApplet.getInstance().getMediaPanel1().repaint();
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g)
	{	

		
	}

}
