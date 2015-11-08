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

import src.screens.editorScreen.libraryPanel.transitionPanel.TransitionManager;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.transition.Transition;


@SuppressWarnings("serial")
public class TransitionMediaPanel extends JPanel
{
	
	public int mouseX;
	public int mouseY;
	
	
	
	public void setMousePosition(int x, int y){
		mouseX = x;
		mouseY = y;
		
	}
	
	public void mousePressed(int x, int y){
		
		
		
		
		
		if(addButtonHovered){
			draggedTransition = hoveredTransition;
		} else {
			selectedTransition = hoveredTransition;
			if(Timeline.selectedTrackObject instanceof Transition){
				Timeline.selectedTrackObject = null;
			}
		}
		
		
		
	}

	
	public TransitionMediaPanel() {
	//	buttons.add(new ButtonInstance(this, 5, 5));
	}
	
	public TransitionMediaPanel(Image img)
	{
		this.setBackground(Color.white);
	}
	
	
	public static void update(){
		MainApplet.getInstance().getTransitionMediaPanel1().repaint();
	}
	
	public static int selectedTransition = -1;
	public static int hoveredTransition = -1;
	
	public static int draggedTransition = -1;
	
	public static boolean transitionValidPosition = false;
	
	public static boolean addButtonHovered = false;
	
	public static TrackItem item1;
	public static TrackItem item2;
	
	public static BufferedImage lastBufferedImage;
	
	@Override
	protected void paintComponent(Graphics g)
	{	
		
		if(true){
			//return;
		}
		
		JPanel i = this;
		hoveredTransition = -1;
		addButtonHovered = false;
		
		//img = Toolkit.getDefaultToolkit().getImage("C:/Users/Harry/Pictures/h.png");
		super.paintComponent(g);

			Dimension size = this.getSize();

			int panelWidth = (int) size.getWidth();
			int panelHeight = (int) size.getHeight();
			
			
			int transitionWidth = 80-8;
			int transitionHeight = 80-8;
			
			
			
			if(MainApplet.getInstance().getjList1().getSelectedIndex() == -1 || TransitionManager.transitionNames == null){
				return;
			}
			try {
				TransitionManager.updateTransitionInfoPanel();
				int totalPerRow = (panelWidth/(transitionWidth+4))-1;
				
				int centerX = panelWidth/totalPerRow;
				int startX = (centerX-(centerX/2));
				int startY = 20;
				
				int space = transitionWidth+4;
				
				int rowCount = 0;
				int height = 0;
				
				for(int index = 0; index < 400; index++){
					
					if(index == selectedTransition){
						g.setColor(new Color(255, 255, 0));
					} else {
						g.setColor(new Color(0, 0, 0));
						
					}
					
					int transitionStartX = startX+(rowCount*space);
					int transitionStartY = startY+(height*space);
					
					
					g.drawRect(transitionStartX, transitionStartY, transitionWidth, transitionHeight);
					
					
					
					if(index < TransitionManager.transitionNames[MainApplet.getInstance().getjList1().getSelectedIndex()].length){
					g.setColor(new Color(58, 110, 165));
					g.fillRect(transitionStartX+1, transitionStartY+1, transitionWidth-1, transitionHeight-1);
					
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
						hoveredTransition = index;
					}
					
					g.setColor(new Color(0, 0, 0));
					
					String name = TransitionManager.transitionNames[MainApplet.getInstance().getjList1().getSelectedIndex()][index];
					
					g.drawString(name, ((transitionStartX+(transitionWidth) /2))-((name.length()*3))-3, (transitionStartY+(30)));
					}
					
					
					
					if(rowCount+1 == totalPerRow){
						rowCount = 0;
						height++;
					} else {
						rowCount++;
					}
					
					
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
			
			//System.out.println("width: "+panelWidth);
		
	}

}
