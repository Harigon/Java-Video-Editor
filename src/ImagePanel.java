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


import src.multiThreading.threads.DecodingThread;
import src.multiThreading.threads.RenderingThread;
import src.renderer.Renderer;


@SuppressWarnings("serial")
public class ImagePanel extends JPanel
{
	
	
	public ImagePanel() {
	//	buttons.add(new ButtonInstance(this, 5, 5));
	}
	
	public ImagePanel(Image img)
	{
		this.setBackground(Color.white);
	}
	
	
	public static void update(){
		
		if(MainApplet.getInstance() == null){
			return;
		}
		if(MainApplet.getInstance().getImagePanel1() == null){
			return;
		}
		
		MainApplet.getInstance().getImagePanel1().repaint();
		
		ChromaKeyFrame.update();
		
		
	}
	
	public static BufferedImage lastBufferedImage;
	
	public static BufferedImage fullQualityFrame;
	
	
	public static boolean generatingFullQualityFrame = false;
	
	public static void generateFullQualityFrame(){
		
		
		if(true){
			return;
		}
		
		
		fullQualityFrame = null;
		
		if(!generatingFullQualityFrame){
			generatingFullQualityFrame = true;
			
			new Thread() {
				public void run() {
					try {
						int width = getVideoWidth();
						int height = getVideoHeight();

						
						System.out.println("starting frame.");
						int[] pixels = Renderer.renderFrame(src.screens.editorScreen.timeline.TimelineManager.timelinePosition, true, width, height, true);

						
						System.out.println("generated frame.");
						
						BufferedImage bufferedImage = ImagePanel.getBI(pixels, width, height);

						fullQualityFrame = bufferedImage;
						generatingFullQualityFrame = false;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();


		}
	}
	
	
	
	
	public static int getVideoWidth(){
		Dimension size = MainApplet.getInstance().getImagePanel1().getSize();

		double panelWidth = size.getWidth()-12;
		double panelHeight = size.getHeight()-2;


		double divider = Project.projectWidth / panelWidth;

		if(Project.projectHeight / divider > panelHeight){
			divider = Project.projectHeight / panelHeight;
		}


		int videoWidth = (int) (Project.projectWidth / divider);
		int videoHeight = (int) (Project.projectHeight / divider);
		return videoWidth;
	}
	public static int getVideoHeight(){
		Dimension size = MainApplet.getInstance().getImagePanel1().getSize();

		double panelWidth = size.getWidth()-12;
		double panelHeight = size.getHeight()-2;


		double divider = Project.projectWidth / panelWidth;

		if(Project.projectHeight / divider > panelHeight){
			divider = Project.projectHeight / panelHeight;
		}


		int videoWidth = (int) (Project.projectWidth / divider);
		int videoHeight = (int) (Project.projectHeight / divider);
		return videoHeight;
	}
	
	public static int lastFrame;
	
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

		BufferedImage frame = RenderingThread.getFrame(currentFrame);

		if(frame != null){
			lastBufferedImage = frame;
		}
		
		if(fullQualityFrame != null){
			lastBufferedImage = fullQualityFrame;
		}
		
		if(lastFrame != currentFrame){
			
			
			
			lastFrame = currentFrame;
		}

		if(lastBufferedImage != null){
			Image image2 = lastBufferedImage.getScaledInstance((int)videoWidth, (int)videoHeight, Image.SCALE_FAST);
			g.drawImage(image2, videoX, videoY, this);
		} else {
			g.setColor(new Color(0,0,0));
			g.fillRect(videoX, videoY, Project.getScaledWidth(), Project.getScaledHeight());
		}
	}
	
	public static BufferedImage getBI(int[] pixels, int width, int height) {
		BufferedImage finalimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        Image piximg = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, pixels,0,width));
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
			//e.printStackTrace();
		}
        return finalimg;
    }
}
