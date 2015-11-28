package src;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

import javax.swing.JPanel;

import src.MainApplet;
import src.MainApplet;
import src.MediaPanel;
import src.TransitionMediaPanel;
import src.dataStore.DataStore;
import src.multiThreading.threads.DecodingThread;
import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaVideoItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaAudioItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.previewPanel.PreviewPanelManager;
import src.screens.editorScreen.timeline.track.Track;
import src.screens.editorScreen.timeline.track.TrackObject;
import src.screens.editorScreen.timeline.track.VideoTrack;
import src.screens.editorScreen.timeline.track.AudioTrack;
import src.screens.editorScreen.timeline.track.trackItem.TrackImageItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackVideoItem;
import src.screens.editorScreen.timeline.track.transition.Transition;
import src.screens.editorScreen.timeline.track.transition.TransitionManager;


@SuppressWarnings("serial")
public class Timeline extends JPanel
{
	
	public int mouseX;
	public int mouseY;
	
	public int itemDraggedOffset;
	public int itemDraggedLastX;
	public int itemDraggedLastMouseX;
	public Track itemDraggedLastTrack;
	
	public TrackItem draggedTrackItem;
	
	public TrackItem draggedStuckToLeft;
	public int draggedStickDelayLeft;
	
	public TrackItem draggedStuckToRight;
	public int draggedStickDelayRight;
	
	public TrackItem croppedTrackItem;
	public int itemCroppedLastDuration;
	
	public static int itemTrimHover;

	public Timeline() {
	//	buttons.add(new ButtonInstance(this, 5, 5));
	}
	
	public Timeline(Image img)
	{
		this.setBackground(Color.white);
	}
	
	public static void update(){
		MainApplet.getInstance().getTimeline2().repaint();
	}
	
	public TrackItem hoveredTrackItem;
	public static TrackObject selectedTrackObject;
	

	public Transition hoveredTransition;
	
	public void setMousePosition(int x, int y){
		mouseX = x;
		mouseY = y;
		
	}
	
	public void keyPressed(int keyId){
		System.out.println("keyID: "+keyId);
	}
	
	public void mousePressed(int x, int y){




		if(hoveredTransition != null){
			selectedTrackObject = hoveredTransition;
			TransitionMediaPanel.selectedTransition = -1;
		} else {
			selectedTrackObject = hoveredTrackItem;
		}
		

		if(selectedTrackObject instanceof TrackItem){
		
		draggedTrackItem = (TrackItem) selectedTrackObject;
		}
		
		if(itemTrimHover == 2){
			croppedTrackItem = hoveredTrackItem;
			itemCroppedLastDuration = croppedTrackItem.mediaDuration;
		}
		
		

		if(draggedTrackItem != null){

			
			
			Track track = src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(draggedTrackItem);

			//re-add so its at the end of the list
			track.trackItems.remove(draggedTrackItem);
			track.trackItems.add(draggedTrackItem);

			itemDraggedOffset = x-src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(draggedTrackItem.getTrackStartPosition());
			itemDraggedLastX = draggedTrackItem.trackStartPosition;
			itemDraggedLastTrack = track;
			itemDraggedLastMouseX = x;
		}

	}
	
	
	@Override
	protected void paintComponent(Graphics g)
	{	

            try {
            
		hoveredTransition = null;
		hoveredTrackItem = null;
		itemTrimHover = 0;
		MediaPanel.transitionValidPosition = false;
		TransitionMediaPanel.transitionValidPosition = false;
		
		//System.out.println("mouse X: "+)


		//client.clientInstance.fullGameScreen.drawGraphics(i, g, k);

		//JPanel i = this;

		//img = Toolkit.getDefaultToolkit().getImage("C:/Users/Harry/Pictures/h.png");
		super.paintComponent(g);

		Dimension size = this.getSize();


		int timelineLength = src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(Renderer.pixels.length);




		int startX = 20;


		int timelineY = 0;

		if(MainApplet.getInstance() != null){
			if(MainApplet.getInstance().getjScrollPane2() != null){
				if(MainApplet.getInstance().getjScrollPane2().getVerticalScrollBar() != null){
					startX = 20-MainApplet.getInstance().getjScrollPane2().getVerticalScrollBar().getValue();
				}
			}

			
			src.screens.editorScreen.timeline.TimelineManager.updateSize();

		}





		//System.out.println("start: "+startX);

		int count = 0;

		if(src.screens.editorScreen.timeline.TimelineManager.tracks != null){

			
			/*
			 * Store a local version of the global track array-list (To prevent any concurrency issues).
			 */
			ArrayList<Track> tempTracks = (ArrayList<Track>) src.screens.editorScreen.timeline.TimelineManager.tracks.clone();
			
			for(Track trackInstance : tempTracks){
				//g.drawRect(1, 20, 50, 50);


				int trackHeight = trackInstance.getTrackHeight();

				int x = 0;
				int y = startX+0;

				startX = y+trackHeight;

				

				g.setColor(new Color(136, 136, 136));
				g.fillRect(x, y, ((int)size.getWidth())-1, trackHeight);
				g.setColor(new Color(171, 171, 171));
				g.drawRect(x, y, ((int)size.getWidth())-1, trackHeight);
				g.setColor(new Color(105, 105, 105));
				g.drawRect(x+1, y+1, (((int)size.getWidth())-1)-2, trackHeight-2);
				
				
				
				if(mouseX > x && mouseY > y && mouseX < x+size.getWidth() && mouseY < y+trackHeight){
					
					int hoveredX = src.screens.editorScreen.timeline.track.TrackManager.pixelPositionToTrackPosition(mouseX);
					if(src.screens.editorScreen.timeline.track.TrackManager.getItemAtTrackRegion(trackInstance, mouseX, mouseX+30) == null){
						
						
						if((trackInstance instanceof VideoTrack && (MediaPanel.draggedMediaItem instanceof MediaImageItem || MediaPanel.draggedMediaItem instanceof MediaVideoItem)) || (trackInstance instanceof AudioTrack && MediaPanel.draggedMediaItem instanceof MediaAudioItem)){
						MediaPanel.transitionValidPosition = true;
						MediaPanel.track = trackInstance;
						MediaPanel.xPosition = hoveredX;
						}
					}
				}
				
				if(draggedTrackItem != null){
					if(!trackInstance.trackItems.contains(draggedTrackItem)){
						if(mouseY >= y && mouseY <= y+trackHeight){
							src.screens.editorScreen.timeline.track.TrackManager.moveItemToNewTrack(draggedTrackItem, trackInstance);
						}
					}
				}


				for(TrackItem trackItem : trackInstance.trackItems){

					int itemX = (src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(trackItem.trackStartPosition));
					int itemLength = (src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(trackItem.mediaDuration-(trackItem.trackStartPosition-trackItem.getTrackStartPosition()))-2);
					int itemY = y+4;
					int itemHeight = trackHeight-8;

					
					for(Transition transition : trackInstance.transitions){
//
						
						
						if(transition.item1 == trackItem){
							//int startPoint = trackPositionToPixelPosition((transition.item1.trackStartPosition+transition.item1.mediaDuration)-transition.duration);
							
							
							int duration = src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(transition.duration);
							int start2 = (itemX+itemLength)-duration;
							
							
							
							
							if(mouseX >= start2 && mouseY >= itemY && mouseX <= start2+duration && mouseY <= itemY+itemHeight){
								hoveredTransition = transition;
							}
							
							if(transition == selectedTrackObject){
								g.setColor(new Color(255, 255, 0));
								g.drawRect(start2, itemY, duration, itemHeight);
							} else {
								g.setColor(new Color(0, 0, 0));
								g.drawRect(start2, itemY, duration, itemHeight);
							}
							
							
							
							g.setColor(new Color(201, 201, 217));
							g.fillRect((start2)+1, itemY+1, duration-1, itemHeight-1);
							
							
							g.setColor(new Color(58, 110, 165));
							g.fillRect((start2)+3, itemY+3, duration-5, itemHeight-5);
							
							
							itemLength = (itemLength-duration)-1;
							
						}//
					
					}

					
					if(mouseX >= itemX && mouseY >= itemY && mouseX <= itemX+itemLength && mouseY <= itemY+itemHeight){
						hoveredTrackItem = trackItem;
						
						
						TrackItem possibleTransitionItem = src.screens.editorScreen.timeline.track.TrackManager.getItemAtTrackRegion(trackInstance, itemX+itemLength+1, (itemX+itemLength)+1);
						
						
						//boolean unused = TransitionManager.itemPartOfAnyTransition(trackItem) == null && TransitionManager.itemPartOfAnyTransition(possibleTransitionItem) == null;
						boolean unused = !TransitionManager.itemsTogetherInTransition(trackItem, possibleTransitionItem);
						
						if(TransitionMediaPanel.hoveredTransition != TransitionMediaPanel.draggedTransition && possibleTransitionItem != null && mouseX > (itemX+itemLength)-10 && unused){

							
							
							
							
							TransitionMediaPanel.transitionValidPosition = true;
							
							
							
							
							TransitionMediaPanel.item1 = trackItem;
							TransitionMediaPanel.item2 = possibleTransitionItem;
							
							//System.out.println("???");


						} else {
							TransitionMediaPanel.transitionValidPosition = false;
						}
					}
					
					
					
					int rightTrimEndX = itemX+itemLength;
					if(src.screens.editorScreen.timeline.track.TrackManager.getItemAtTrackRegion(trackInstance, itemX+itemLength+1, (itemX+itemLength)+5) != null){
						//System.out.println("yo1");
						rightTrimEndX = itemX+itemLength+5;
					}
					
					int leftTrimStartX = itemX;
					if(src.screens.editorScreen.timeline.track.TrackManager.getItemAtTrackRegion(trackInstance, itemX-5, itemX) == null){
						
						//System.out.println("yo1");
						
						leftTrimStartX = itemX-5;
					}
					
					if(mouseX >= (itemX+itemLength)-5 && mouseY >= itemY && mouseX <= rightTrimEndX && mouseY <= itemY+itemHeight){
						itemTrimHover = 2;
						hoveredTrackItem = trackItem;
					}
					
					if(mouseX >= leftTrimStartX && mouseY >= itemY && mouseX <= itemX+5 && mouseY <= itemY+itemHeight){
						itemTrimHover = 1;
						hoveredTrackItem = trackItem;
					}
					
					

					
					
					if(trackItem == draggedTrackItem){
						
						if(draggedTrackItem.trackStartPosition != itemDraggedLastX || src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(draggedTrackItem) != itemDraggedLastTrack){
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
						}
					}
					
					
					
					
					
					//if(trackItem instanceof TrackImageItem){
						
					//}
					//
					//if(trackItem.mediaItem instanceof MediaImageItem){
						
					//}
					//g.drawImage(trackItem.mediaItem.
					

						if(trackItem == selectedTrackObject){
							g.setColor(new Color(255, 255, 0));
							g.drawRect(itemX, itemY, itemLength, itemHeight);
						} else {
							g.setColor(new Color(0, 0, 0));
							g.drawRect(itemX, itemY, itemLength, itemHeight);
						}
					
					
					if(trackItem == draggedTrackItem){
						
						int currentPosition = trackItem.getTrackStartPosition();
						
						for(TrackItem trackItem2 : trackInstance.trackItems){
							int endingPosition = trackItem2.getTrackStartPosition()+trackItem2.mediaDuration;
							
							if(currentPosition == endingPosition+1){
							//	System.out.println("together: "+endingPosition+", "+currentPosition);
							}
							
						}
						
						
						
						
						if(!src.screens.editorScreen.timeline.track.TrackManager.validTrackItemPosition(draggedTrackItem)){
							g.setColor(new Color(147, 0, 0));
							g.fillRect(itemX+1, itemY+1, itemLength-1, itemHeight-1);
						} else {
							g.setColor(new Color(201, 201, 217));
							g.fillRect(itemX+1, itemY+1, itemLength-1, itemHeight-1);
							
						}
					} else {
						g.setColor(new Color(201, 201, 217));
						g.fillRect(itemX+1, itemY+1, itemLength-1, itemHeight-1);
						
					}

					drawTrackItemThumbnails(g, trackInstance, trackItem, itemX, itemY, itemLength);
					
					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					
					
					
					
					
					
				}


				


				count++;
			}



			/*
			 * Top
			 */

			/*
			 * Rendering line
			 */
			g.setColor(new Color(0, 0, 0));
			g.drawRect(1, 1, (src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(Renderer.pixels.length))-1, 7);
			for(int index = 0; index < Renderer.renderingStatus.length; index++){
				
				if(Renderer.renderingStatus != null){
					if(Renderer.renderingStatus[index] == 0){
						g.setColor(new Color(148, 24, 18));
					}
					if(Renderer.renderingStatus[index] == 1){
						g.setColor(new Color(206, 206, 0));
					}
					if(Renderer.renderingStatus[index] == 2){
						g.setColor(new Color(0, 128, 0));
					}
				}
				
				g.fillRect(src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(index), 1+1, 1, 7-1);
			}
			
			
			
			g.setColor(new Color(0, 0, 0));
			g.drawRect(1, 10, (src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(Renderer.pixels.length))-1, 7);
			for(int index = 0; index < Renderer.renderingStatus.length; index++){
				
				if(DecodingThread.isFrameDecoded(index)){
					g.setColor(new Color(128, 128, 0));
				} else {
					g.setColor(new Color(12, 73, 97));
				}

				g.fillRect(src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(index), 10+1, 1, 7-1);
			}
			

			/*
			 * line
			 */
			g.setColor(new Color(0, 0, 0));
			g.fillRect(src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(src.screens.editorScreen.timeline.TimelineManager.timelinePosition), 0, 1, 400);




		}

            } catch (Exception e){
                e.printStackTrace();
            }

	}
	
	
	public void drawTrackItemThumbnails(Graphics g, Track trackInstance, TrackItem trackItem, int itemX, int itemY, int itemLength){
		
		if((trackInstance instanceof VideoTrack && (trackItem instanceof TrackImageItem || trackItem instanceof TrackVideoItem) ) || trackInstance == null){
			
			
			BufferedImage image = null;
			
			if(trackItem instanceof TrackVideoItem){
				
				if(trackItem.mediaItem instanceof MediaVideoItem){
					MediaVideoItem mediaVideoItem = (MediaVideoItem) trackItem.mediaItem;
					image = mediaVideoItem.getThumbnail();
				}
				
				
			}

			
			if(trackItem instanceof TrackImageItem){
				MediaImageItem mediaImageItem = (MediaImageItem) trackItem.mediaItem;
				image = mediaImageItem.getThumbnail();
			}
			
			int width = image.getWidth();
			
			int spaceNeeded = (width*2)+6;
			
			int spaceNeeded2 = width+6;
			
			if(itemLength >= spaceNeeded){
				g.drawImage(image, itemX+3, itemY+3, null);
			}
			
			if(itemLength >= spaceNeeded2){
				g.drawImage(image, ((itemX+itemLength)-width)-2, itemY+3, null);
			} else {
				int newWidth = itemLength-6;
				try {
					g.drawImage(cropImage(image, newWidth, image.getHeight()), ((itemX+itemLength)-newWidth)-2, itemY+3, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		}
	}
	
	public void mouseClick(int x, int y){
		
	}
	
	private BufferedImage cropImage(BufferedImage src, int width, int height) {
	      BufferedImage dest = src.getSubimage(0, 0, width, height);
	      return dest; 
	   }

	
}
