package src;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

import javax.swing.JPanel;




import src.dataStore.DataStore;
import src.multiThreading.threads.TaskThread;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;
import src.thirdPartyLibraries.Scalr;
import src.util.ImageUtils;


@SuppressWarnings("serial")
public class IntroScreen extends JPanel
{

	
	public IntroScreen() {
	//	buttons.add(new ButtonInstance(this, 5, 5));
	}
	
	public IntroScreen(Image img)
	{
		this.setBackground(Color.white);
	}
	
	
	public static void update(){
		
		if(MainApplet.getInstance() == null){
			return;
		}
		if(MainApplet.getInstance().getIntroScreen2() == null){
			return;
		}
		
		MainApplet.getInstance().getIntroScreen2().repaint();
	}
	
	public static BufferedImage lastBufferedImage;
	public static boolean animationRunning = false;
	
	
	public static BufferedImage backgroundImage;
	public static BufferedImage scaledBackgroundImage;
	
	
	public static int lastWidth;
	public static int lastHeight;
	
	public static int frameId = 0;
	
	public static int startDelay = 100;
	
	@Override
	protected void paintComponent(Graphics g)
	{	
		
		
		
		JPanel i = this;
		
		//img = Toolkit.getDefaultToolkit().getImage("C:/Users/Harry/Pictures/h.png");
		super.paintComponent(g);
		
		
		
		
		if(animationRunning == false && frameId == 0 && startDelay == 0){
			IntroScreen.startIntroAnimation();
		}

			Dimension size = this.getSize();

			int panelWidth = (int) size.getWidth();
			int panelHeight = (int) size.getHeight();
			
			
			g.setColor(new Color(0,0,0));
			g.fillRect(0, 0, panelWidth, panelHeight);
			
			double divider = Project.projectWidth / panelWidth;
			
			if(Project.projectHeight / divider > panelHeight){
				divider = Project.projectHeight / panelHeight;
			}
			
			
			int videoWidth = (int) (Project.projectWidth / divider);
			int videoHeight = (int) (Project.projectHeight / divider);
			
			
			int videoX = (size.width /2) - (videoWidth/2);
			int videoY = (size.height /2) - (videoHeight/2);
			
			
			if(animationRunning){
				
				//System.out.println(frameId);
				int fontSize = panelWidth/20;

				g.setColor(new Color(255,255,255));
				
				g.setFont(new Font("TimesRoman", Font.ITALIC, fontSize));
				g.drawString("", panelWidth/2, panelHeight/2);
				
				
				if(panelWidth < backgroundImage.getWidth() && panelHeight < backgroundImage.getHeight()){
					g.drawImage(backgroundImage, 0, 0, null);

				} else {
					if(lastWidth != panelWidth || lastHeight != panelHeight || scaledBackgroundImage == null){
						if(panelWidth > panelHeight){
							scaledBackgroundImage = Scalr.resize(backgroundImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, (int) panelWidth, null);
						} else {
							scaledBackgroundImage = Scalr.resize(backgroundImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, (int) panelHeight, null);
						}
					}
					g.drawImage(scaledBackgroundImage, 0, 0, null);

				}
				
				
				
				
				
				
				
				
				
				if(frameId > 0){
					
					int value = frameId;
					
					if(value < 0){
						value = 0;
					}
					
					
					
					int max = 2400;
					
					if(value > max){
						value = max;
					}
					
					//System.out.println("value: "+value);
					
					int startX = (int) ((panelWidth/2)+(panelWidth/4));
					
					int startY = panelHeight;
					
					
					int endX = 0;
					int endY = 0;
					
					int startScale = panelWidth/10;
					int endScale = panelWidth*4;
					//if(endScale < startScale){
						//endScale = startScale;
					//}
					
					//int startX = 500;
					
					//int startY = 500;
					
					
					
					//int endX = 0;
					

					
					//int endY = 0;
					
					//int divider2 = (int) ((double)startX*max);
					
					//System.out.println(divider2);
					
					//int logoX = (int) (value*((double)endX*max))+startX;
					
					int logoX = (int) (startX - (value*((double)startX/max)));
					
					int logoY = (int) (startY - (value*((double)startY/max)));
					
					//int logoY = (int) (value*((double)endX*max))+startY;
					
					//logoX = startY-logoX;
					
					
					//System.out.println("logoX: "+logoX);
					
					//int logoY = (int) (value*((double)endY/max))+startY;
					
					int scale = (int) (value*((double)endScale/max))+startScale;
					

					//int scale = 
					
					//int logoX = 
					
				BufferedImage logo = ImageUtils.toBufferedImage(DataStore.spriteLoadFile(27));
				
				
				double panelWidth2 = scale;
				double panelHeight2 = logo.getHeight();
				
		
				double divider2 = logo.getWidth() / panelWidth2;
				
				if(logo.getHeight() / divider2 > panelHeight2){
					divider = logo.getHeight() / panelHeight2;
				}
				
				
				int videoWidth2 = (int) (logo.getWidth() / divider);
				int videoHeight2 = (int) (logo.getHeight() / divider);
				
			//	Image logo2 = logo;
				
			//	Image logo2 = logo.getScaledInstance(videoWidth2, videoHeight2, Image.SCALE_FAST);
				
				
				//logo = ImagePanel.getBI(logo2);
				logo = Scalr.resize(logo, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, scale, null);
				
				
				
				if(frameId > 500){
					int value2 = frameId - 500;
					if(value2 < 0){
						value2 = 0;
					}
					
					
					
					int max2 = 300;
					
					if(value2 > max2){
						value2 = max2;
					}
					
					int startAlpha = 255;
					int endAlpha = 0;
					
					
					
					int alphav = (int) (startAlpha - (value2*((double)startAlpha/max2)));
					
					double alpha = (double)alphav/255;

					
					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
					
				} else if(frameId > 0){
					int value2 = frameId ;
					if(value2 < 0){
						value2 = 0;
					}
					
					
					
					int max2 = 500;
					
					if(value2 > max2){
						value2 = max2;
					}
					
					int startAlpha = 0;
					int endAlpha = 255;
					
					
					
					int alphav = (int) (value2*((double)endAlpha/max2))+startAlpha;
					
					double alpha = (double)alphav/255;

					
					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
					
				}
				
				g.drawImage(logo, logoX-(logo.getWidth()/2)-(panelWidth/10), (logoY-(logo.getHeight()/2))-(panelHeight/4), null);
				}
				
				
				if(frameId > 0){
					int value2 = frameId;
					
					
					if(value2 < 0){
						value2 = 0;
					}
					
					
					
					int max2 = 400;
					
					if(value2 > max2){
						value2 = max2;
					}
					
					int startAlpha = 255;
					int endAlpha = 0;
					
					
					
					int alphav = (int) (startAlpha - (value2*((double)startAlpha/max2)));
					
					double alpha = (double)alphav/255;
					
					
					
					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
					
					
					
					g.setColor(new Color(0,0,0));
					
					
					g.fillRect(0, 0, panelWidth, panelHeight);
					
					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1));
					
				}
				
				if(frameId > 700){
					int value2 = frameId - 700;
					
					
					if(value2 < 0){
						value2 = 0;
					}
					
					
					
					int max2 = 200;
					
					if(value2 > max2){
						value2 = max2;
					}
					
					int startAlpha = 0;
					int endAlpha = 255;
					
					
					
					int alphav = (int) (value2*((double)endAlpha/max2))+startAlpha;
					
					double alpha = (double)alphav/255;
					
					
					
					
					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
					
					
					
					
				}
				
				if(frameId > 900){
					int value2 = frameId - 900;
					
					
					if(value2 < 0){
						value2 = 0;
					}
					
					
					
					int max2 = 200;
					
					if(value2 > max2){
						value2 = max2;
					}
					
					int startAlpha = 255;
					int endAlpha = 0;
					
					
					
					int alphav = (int) (startAlpha - (value2*((double)startAlpha/max2)));
					
					double alpha = (double)alphav/255;
					
					
					
					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
					

				}
				
				if(frameId > 700){
					g.setColor(new Color(255,255,255));
					String text = "The future of video editing";
					
					
					g.drawString(text, (size.width /2)-((text.length()*3))-(size.width /5), (size.height/2)+4);
				}
				
				
				if(frameId > 900){
					int value2 = frameId - 900;
					
					
					if(value2 < 0){
						value2 = 0;
					}
					
					
					
					int max2 = 300;
					
					if(value2 > max2){
						value2 = max2;
					}
					
					int startAlpha = 0;
					int endAlpha = 255;
					
					
					
					int alphav = (int) (value2*((double)endAlpha/max2))+startAlpha;
					
					double alpha = (double)alphav/255;
					
					
					
					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
					
					
					
					g.setColor(new Color(0,0,0));
					
					
					g.fillRect(0, 0, panelWidth, panelHeight);
					
				}
				
			}
			if(frameId >= 1250){
				animationRunning = false;
				TaskThread.editorRunning = true;
				MainApplet.getInstance().getjPanel15().setVisible(false);
			//	MainApplet.getInstance().getjPanel16().setVisible(true);
				
				MainApplet.getInstance().getjPanel30().setVisible(true);
				
				
				//NewJApplet.instance.getjInternalFrame1().setVisible(true);
			//	NewJApplet.instance.getjLayeredPane1().moveToFront(NewJApplet.instance.getjPanel16());
				

			}
			
			
			
			
	
			lastWidth = (int) panelWidth;
			lastHeight = (int) panelHeight;
			
			
			
			
	}
	
	
	public static void startIntroAnimation(){
		
		if(animationRunning){
			return;
		}
		MainApplet.getInstance().getjPanel15().setVisible(true);
		//NewJApplet.instance.getjLayeredPane1().moveToFront(NewJApplet.instance.getjPanel15());
		MainApplet.getInstance().getjPanel16().hide();
		backgroundImage = ImageUtils.toBufferedImage(DataStore.spriteLoadFile(26));
		
		
		//int fontSize = 50;

		//NewJApplet.instance.getIntroScreen1().getGraphics().setColor(new Color(255,255,255));
		
		//NewJApplet.instance.getIntroScreen1().getGraphics().setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
		
		frameId = 0;
		
		
		
		animationRunning = true;

	}
	
	public static Image rotateImage(BufferedImage image2, double radius){
		
		
		
		double rotationRequired = Math.toRadians(radius);
		
		double sin = Math.abs(Math.sin(rotationRequired)), cos = Math.abs(Math.cos(rotationRequired));
	    int w = image2.getWidth(), h = image2.getHeight();
		int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
		

		 GraphicsConfiguration gc = ImageUtils.getDefaultConfiguration();
		BufferedImage result = gc.createCompatibleImage(image2.getWidth(), image2.getHeight(), Transparency.TRANSLUCENT);

		Graphics2D g2d = result.createGraphics();
        g2d.translate(image2.getWidth() / 2, image2.getHeight() / 2);
        g2d.rotate(rotationRequired);
        g2d.translate(-image2.getWidth(null) / 2, -image2.getHeight(null) / 2);
        g2d.drawImage(image2, 0, 0, null);
        return result;
	}
	
	
}
