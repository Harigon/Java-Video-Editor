package src.screens.editorScreen.libraryPanel.exportPanel;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;

import javax.swing.ImageIcon;

import src.ImagePanel;
import src.MainApplet;
import src.Project;
import src.renderer.Renderer;
import src.screens.editorScreen.newProjectPanel.NewProject;
import src.thirdPartyLibraries.movLibrary.JPEGMovWriter;
import src.thirdPartyLibraries.movLibrary.MovWriter;
import src.thirdPartyLibraries.movLibrary.PNGMovWriter;
import src.util.Misc;

public class FilmTest {

	
	public static void main(String[] args){
		
		
		
		try {
			
			
			File[] files = Misc.selectFolder().listFiles();
			
			File file = Misc.saveToDirectory("MOV File (*.mov)", new String[] {"mov", ".mov"});

			
			
			if(file == null){
				return;
			}
			

			
			//NewJApplet.instance.getjPanel16().setEnabled(false);
			
			
			
			//int qualitylol = 100-MainApplet.getInstance().getjSlider3().getValue();
		//	float quality = (float) ((double)qualitylol/100);
			//System.out.println("quality: "+quality);


			long time = System.currentTimeMillis();
			MovWriter anim = null;
			int framesWritten = 0;



			int fps = 50;



			anim = new PNGMovWriter(file);

			//anim = new JPEGMovWriter(file, quality);


			float frameDuration = 1f/(fps);


			int width = 1280;
			int height = 720;
			



			for(int frameIndex = 0; frameIndex < files.length; frameIndex++){


				int[] pixels;
				int width2;
				int height2;
				Image image = Toolkit.getDefaultToolkit().getImage(files[frameIndex].getAbsolutePath());
				ImageIcon imageicon = new ImageIcon(image);
				width2 = imageicon.getIconWidth();
				height2 = imageicon.getIconHeight();
				pixels = new int[width2 * height2];
				PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width2, height2, pixels, 0, width2);
				pixelgrabber.grabPixels();


				BufferedImage bufferedImage = ImagePanel.getBI(pixels, width2, height2);
				if(bufferedImage == null){
					bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
					bufferedImage.getGraphics().drawRect(30,30,100,100);
				}
				anim.addFrame(frameDuration, bufferedImage, null);
				System.out.println("added: "+frameIndex);
			}
			anim.close(false);

			time = System.currentTimeMillis()-time;
			System.out.println("wrote "+framesWritten+" frames: "+file.getAbsolutePath()+" ("+(time)/1000.0+" s)");

			System.gc ();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
}
