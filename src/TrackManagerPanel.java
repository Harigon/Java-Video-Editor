package src;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

import javax.swing.JPanel;

import src.screens.editorScreen.timeline.track.Track;


@SuppressWarnings("serial")
public class TrackManagerPanel extends JPanel
{

	public TrackManagerPanel() {
	//	buttons.add(new ButtonInstance(this, 5, 5));//
	}
	
	public TrackManagerPanel(Image img)
	{
		this.setBackground(Color.white);
	}
	
	public static void update(){
		MainApplet.getInstance().getTrackManagerPanel1().repaint();
	}
	

	
	@Override
	protected void paintComponent(Graphics g)
	{	
		
		

		
		JPanel i = this;
		

		super.paintComponent(g);

			Dimension size = this.getSize();

			
			int startX = 20;

			int count = 0;
			
		
			
			for(Track trackInstance : src.screens.editorScreen.timeline.TimelineManager.tracks){

				
				int trackHeight = trackInstance.getTrackHeight();
				
				int x = 0;
				int y = startX+0;
				
				startX = y+trackHeight;
				
				
				
				g.setColor(new Color(136, 136, 136));
				g.fillRect(x, y, 400, trackHeight);
				g.setColor(new Color(171, 171, 171));
				g.drawRect(x, y, 400, trackHeight);
				g.setColor(new Color(105, 105, 105));
				g.drawRect(x+1, y+1, 400-2, trackHeight-2);
				

				Graphics2D g2 = (Graphics2D) g.create();
				g2.setPaint(Color.BLACK);
				g2.drawString(trackInstance.getTrackType()+" Track", 5, y+15);
				
	
					trackInstance.upButton.setBounds(x+86, (y+trackHeight)-21, 24, 20);

				
				trackInstance.downButton.setBounds(x+110, (y+trackHeight)-21, 24, 20);
				
				
		        

				
				
				count++;
			}
			
			
			
	
	}
	
	public void mouseClick(int x, int y){
		
	}

	
}
