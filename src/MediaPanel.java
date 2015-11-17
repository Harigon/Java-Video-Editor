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

import src.dataStore.DataStore;
import src.screens.editorScreen.libraryPanel.mediaPanel.MediaPanelManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaAudioItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaVideoItem;
import src.screens.editorScreen.libraryPanel.transitionPanel.TransitionManager;
import src.screens.editorScreen.timeline.track.Track;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.transition.Transition;


@SuppressWarnings("serial")
public class MediaPanel extends JPanel
{
	
	public int mouseX;
	public int mouseY;
	
	
	
	public void setMousePosition(int x, int y){
		mouseX = x;
		mouseY = y;
		
		
	}
	
	
	public void mousePressed(int x, int y){
		
		
		
		
		
		if(addButtonHovered){
			draggedMediaItem = hoveredMediaItem;
		} else {
			selectedMediaItem = hoveredMediaItem;
			if(Timeline.selectedTrackObject instanceof Transition){
				Timeline.selectedTrackObject = null;
			}
		}
		
		
		
	}

	
	public MediaPanel() {
	//	buttons.add(new ButtonInstance(this, 5, 5));
	}
	
	public MediaPanel(Image img)
	{
		this.setBackground(Color.white);
	}
	
	
	public static void update(){
		MainApplet.getInstance().getMediaPanel1().repaint();
	}
	
	public static MediaItem selectedMediaItem = null;
	public static MediaItem hoveredMediaItem = null;
	
	public static MediaItem draggedMediaItem = null;
	
	public static boolean transitionValidPosition = false;
	
	public static boolean addButtonHovered = false;
	
	public static Track track;
	public static int xPosition;
	
	public static BufferedImage lastBufferedImage;
	

	
	@Override
	protected void paintComponent(Graphics g)
	{	

            try {
                
            
            
            
		if(true){
			//return;
		}

		JPanel i = this;
		hoveredMediaItem = null;
		addButtonHovered = false;

		//img = Toolkit.getDefaultToolkit().getImage("C:/Users/Harry/Pictures/h.png");




		super.paintComponent(g);

                

		Dimension size = this.getSize();

		int panelWidth = (int) size.getWidth();
		int panelHeight = (int) size.getHeight();


		int transitionWidth = 80-8;
		int transitionHeight = 80-8;

		int totalPerRow = (panelWidth/(transitionWidth+4))-1;

		int centerX = panelWidth/totalPerRow;
		int startX = 5;
		int startY = 5;

		int space = transitionWidth+4;

		int rowCount = 0;
		int height = 0;

                
		if(MediaPanelManager.albums.size() > MediaManager.selectedAlbum){

			Album album = MediaPanelManager.albums.get(MediaManager.selectedAlbum);


			if(album != null){


				/*
				 * Store a local version of the global media-items array-list (To prevent any concurrency issues).
				 */
				ArrayList<MediaItem> tempMediaItems = (ArrayList<MediaItem>) album.mediaItems.clone();
				for(MediaItem mediaItem : tempMediaItems){



					if(mediaItem == selectedMediaItem){
						g.setColor(new Color(255, 255, 0));
					} else {
						g.setColor(new Color(0, 0, 0));

					}

					int transitionStartX = startX+(rowCount*space);
					int transitionStartY = startY+(height*space);


					g.drawRect(transitionStartX, transitionStartY, transitionWidth, transitionHeight);





						Image image2 = null;


						if(mediaItem instanceof MediaImageItem){
							MediaImageItem mediaImageItem = (MediaImageItem) mediaItem;
							image2 = mediaImageItem.getThumbnail().getScaledInstance((int)71, (int)56, Image.SCALE_FAST);
						}
						if(mediaItem instanceof MediaVideoItem){
							MediaVideoItem mediaVideoItem = (MediaVideoItem) mediaItem;
							image2 = mediaVideoItem.getThumbnail().getScaledInstance((int)71, (int)56, Image.SCALE_FAST);
						}
						if(mediaItem instanceof MediaAudioItem){
							image2 = DataStore.spriteLoadFile(28);
						}


						g.drawImage(image2, transitionStartX+1, transitionStartY+1, null);




						{

							int buttonHeight = 15;
							int buttonWidth = transitionWidth;
							int buttonX = transitionStartX;
							int buttonY = (transitionStartY+transitionHeight)-buttonHeight;


							g.setColor(new Color(0, 0, 0));
							g.drawRect(buttonX+1, buttonY, buttonWidth-2, 1);




							if(mouseX >= buttonX && mouseY >= buttonY && mouseX <= buttonX+buttonWidth && mouseY <= buttonY+buttonHeight){

								addButtonHovered = true;
								g.setColor(new Color(170, 0, 170));
								g.fillRect(buttonX+1, buttonY+1, buttonWidth-1, buttonHeight-1);
							} else {

								g.setColor(new Color(121, 0, 121));
								g.fillRect(buttonX+1, buttonY+1, buttonWidth-1, buttonHeight-1);
							}



							g.setColor(new Color(0, 0, 0));
							g.drawString("Add to track", buttonX+4, (buttonY+(buttonHeight))-3);

						}

						if(mouseX >= transitionStartX && mouseY >= transitionStartY && mouseX <= transitionStartX+transitionWidth && mouseY <= transitionStartY+transitionHeight){
							hoveredMediaItem = mediaItem;
						}


					
					//


					if(rowCount+1 == totalPerRow){
						rowCount = 0;
						height++;
					} else {
						rowCount++;
					}
				}
			}

		}
                
                
		//NewJApplet.instance.getMediaPanel1().set
		MainApplet.getInstance().getMediaPanel1().setPreferredSize(new Dimension(MainApplet.getInstance().getjScrollPane8().getWidth()-20,height*(transitionHeight+10)));
		MainApplet.getInstance().getMediaPanel1().revalidate();
                } catch (Exception e) {
                
                }
	}
            
            

}
