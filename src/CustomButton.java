package src;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

//

@SuppressWarnings("serial")
public class CustomButton extends JToggleButton
{

	public CustomButton() {
	}
	
	private String text = "";
	
	private int xOffset = 0;
	
	public void setOffset(int x){
		xOffset = x;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public CustomButton(Image img)
	{
		this.setBackground(Color.white);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{	

		
		
		//System.out.println("painted");
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
			
		//	g.drawRect((size.width /2), (size.height /2) - (videoHeight/2), videoWidth, videoHeight);

			Graphics2D g2 = (Graphics2D) g.create();
			g2.setPaint(Color.BLACK);
			g2.drawString(text, (size.width /2)-((text.length()*3))+xOffset, (size.height/2)+4);
		
	}
}
