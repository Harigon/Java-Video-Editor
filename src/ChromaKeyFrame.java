package src;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


import src.multiThreading.threads.RenderingThread;
import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.chromaKey.ChromaKey;
import src.screens.editorScreen.libraryPanel.chromaKey.ColourProfileManager;


@SuppressWarnings("serial")
public class ChromaKeyFrame extends JPanel
{
	
	
	public ChromaKeyFrame() {
	//	buttons.add(new ButtonInstance(this, 5, 5));
	}
	
	public ChromaKeyFrame(Image img)
	{
		this.setBackground(Color.white);
	}
	
	public int mouseX;
	public int mouseY;
	
	public void setMousePosition(int x, int y){
		mouseX = x;
		mouseY = y;
		
	}
	
	public void mousePressed(int x, int y){
		
		if(ChromaKeyFrame.addColour){
		
		if(lastBufferedImage != null){
			try {
				ImageIcon imageicon = new ImageIcon(lastBufferedImage);
				int width = imageicon.getIconWidth();
				int height = imageicon.getIconHeight();
				int pixels[] = new int[width * height];
				PixelGrabber pixelgrabber = new PixelGrabber(lastBufferedImage, 0, 0, width,height, pixels, 0, width);
				pixelgrabber.grabPixels();
				
				int pixel = pixels[coordinateToPixel(x, y, width)];
				
				
				
				if(MainApplet.getInstance().getjComboBox12().getSelectedIndex() == 0){
					ColourProfileManager.getSelectedColourProfileInstance().addFilteredColour(pixel);
				} else {
					ColourProfileManager.getSelectedColourProfileInstance().addAllowedColour(pixel);
				}
				ChromaKeyFrame.addColour = false;
				lastBufferedImage = null;
				lastFrame = -1;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		}
	}
	
	public static int pixelToY(int pixelIndex, int width){
		return pixelIndex / width;
	}
	
	public static int coordinateToPixel(int x, int y, int width){
		int pixelIndex = (y*width)+x;
		return pixelIndex;
	}
	
	
	public static void update(){
		
		if(MainApplet.getInstance() == null){
			return;
		}
		if(MainApplet.getInstance().getChromaKeyFrame1() == null){
			return;
		}
		
		MainApplet.getInstance().getChromaKeyFrame1().repaint();
		
		if(MainApplet.getInstance().getChromaKeyFrame1() != null){
			MainApplet.getInstance().getChromaKeyFrame1().setPreferredSize(new Dimension(1920,1080));
			MainApplet.getInstance().getChromaKeyFrame1().revalidate();
			
			
		}
		
	}
	
	public static int lastFrame;
	public static BufferedImage lastBufferedImage;
	
	public static boolean addColour = false;
	
	@Override
	protected void paintComponent(Graphics g)
	{	


		JPanel i = this;


		super.paintComponent(g);

		Dimension size = this.getSize();

		double panelWidth = size.getWidth()-12;
		double panelHeight = size.getHeight()-2;


		double divider = Project.projectWidth / panelWidth;

		if(Project.projectHeight / divider > panelHeight){
			divider = Project.projectHeight / panelHeight;
		}


		int videoWidth = (int) (Project.projectWidth / divider);
		int videoHeight = (int) (Project.projectHeight / divider);




		int videoX = (size.width /2) - (videoWidth/2);
		int videoY = (size.height /2) - (videoHeight/2);

		g.drawRect((size.width /2) - (videoWidth/2), (size.height /2) - (videoHeight/2), videoWidth-1, videoHeight-1);

		int currentFrame = src.screens.editorScreen.timeline.TimelineManager.timelinePosition;

		byte status;

		
		
		if(lastFrame != currentFrame){
		//
		int width = 1920;
		int height = 1080;
			//int width = ImagePanel.getVideoWidth();
			//int height = ImagePanel.getVideoHeight();
			
		int[] pixels = Renderer.renderFrame(currentFrame, true, width, height, true);
		BufferedImage bufferedImage = ImagePanel.getBI(pixels, width, height);
		if(bufferedImage == null){
			bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			bufferedImage.getGraphics().drawRect(30,30,100,100);
		}
		
		lastFrame = currentFrame;
		lastBufferedImage = bufferedImage;
		}
		

		

		if(lastBufferedImage != null){
			Image image2 = lastBufferedImage;
			g.drawImage(image2, 0, 0, this);
		} else {
			g.setColor(new Color(0,0,0));
			g.fillRect(videoX, videoY, Project.getScaledWidth(), Project.getScaledHeight());
		}
	}
	
	public static BufferedImage getBI(int[] pixels, int width, int height) {
		BufferedImage finalimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        Image piximg =  Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, pixels,0,width));
        finalimg.getGraphics().drawImage(piximg, 0, 0, null);
        return finalimg;
    }
	
	public static BufferedImage getBI(Image image) {
		
		BufferedImage finalimg = null;
		

		try {
			finalimg = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
			
      finalimg.getGraphics().drawImage(image, 0, 0, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			///e.printStackTrace();
		}
        return finalimg;
    }
}
