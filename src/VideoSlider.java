package src;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JPanel;




@SuppressWarnings("serial")
public class VideoSlider extends JPanel
{
	
	public VideoSlider() {
	//	buttons.add(new ButtonInstance(this, 5, 5));//
	}
	
	public VideoSlider(Image img)
	{
		this.setBackground(Color.white);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{	
		JPanel i = this;
		
		
		
		
		//img = Toolkit.getDefaultToolkit().getImage("C:/Users/Harry/Pictures/h.png");
		super.paintComponent(g);

			Dimension size = this.getSize();

			double panelWidth = size.getWidth()-2;
			double panelHeight = size.getHeight()-2;
			
			
			double divider = Project.projectWidth / panelWidth;
			
			if(Project.projectHeight / divider > panelHeight){
				divider = Project.projectHeight / panelHeight;
			}
			
			
			int videoWidth = (int) (Project.projectWidth / divider);
			int videoHeight = (int) (Project.projectHeight / divider);
			
			g.drawRect((size.width /2) - (videoWidth/2), (size.height /2) - (videoHeight/2), videoWidth, videoHeight);

			
		
	}
}
