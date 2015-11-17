package src.screens.editorScreen.libraryPanel.exportPanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;



import src.ImagePanel;
import src.MainApplet;
import src.Project;
import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.mediaPanel.MediaPanelManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaAudioItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaImageItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaManager;
import src.screens.editorScreen.newProjectPanel.NewProject;
import src.screens.editorScreen.timeline.track.Track;
import src.thirdPartyLibraries.AnimatedGifEncoder;
import src.thirdPartyLibraries.movLibrary.JPEGMovWriter;
import src.thirdPartyLibraries.movLibrary.MovWriter;
import src.util.Misc;
import src.screens.editorScreen.timeline.track.AudioTrack;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.trackItem.TrackAudioItem;
import sun.misc.GC;

public class ExportManager {

	
	public static int selectedVideoSizeOption = 0;
	
	public static int outputFormat = 0;
	
	public static int fps = 30;
	
	public static String[] saveToDirectoryFiles = new String[] {"MOV File (*.mov)"};
	public static String[][] directoryExtentsions = new String[][] {{"mov", ".mov"}, {"avi", ".avi"}};
	
	/**
	 * Starts the export process.
	 */
	public static void startExport(){

		
		/*
		 * Run this process on a separate thread, to stop it freezing the GUI.
		 */
		new Thread() {
			public void run() {


				try {
					
					
					
					
					File file = Misc.saveToDirectory(saveToDirectoryFiles[outputFormat], directoryExtentsions[outputFormat]);

					
					
					if(file == null){
						return;
					}
					
					cleanup();
					
					//NewJApplet.instance.getjPanel16().setEnabled(false);
					
					MainApplet.instance.getjLabel19().setText("Frame 0 / "+(Renderer.pixels.length));
					MainApplet.instance.getjLabel20().setText("0%");
					MainApplet.instance.getjProgressBar1().setValue(0);
					MainApplet.instance.getjLabel21().setText("Rendering video...");
					MainApplet.instance.getjInternalFrame2().setTitle("Renderer");
					
					MainApplet.instance.getjInternalFrame2().setVisible(true);
					
					int qualitylol = 100-MainApplet.getInstance().getjSlider3().getValue();
					float quality = (float) ((double)qualitylol/100);
					System.out.println("quality: "+quality);


					long time = System.currentTimeMillis();
					MovWriter anim = null;
					int framesWritten = 0;



					double fps = ExportManager.fps;
					fps = Project.frameRate;


					//anim = new PNGMovWriter(file);

					anim = new JPEGMovWriter(file, quality);


					float frameDuration = (float) (1f/(fps));


					int width = 1280;
					int height = 720;
					
					
					if(selectedVideoSizeOption == 0){
						width = Project.projectWidth;
						height = Project.projectHeight;
					}
					if(selectedVideoSizeOption == 1){
						
						int dimension = MainApplet.getInstance().getjComboBox2().getSelectedIndex();
						

						width = NewProject.dimensions[dimension][0];
						height = NewProject.dimensions[dimension][1];
					}

					
					/*
					 * Write audio
					 */
					//File audioFile = new File("C:/Users/Harry/Music/The waters echo/splashing stopped.wav");
					//AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
					//anim.addAudioTrack(audioIn, 0);

					
					MainApplet.instance.getjLabel19().setText("Writing audio data");
					for(Track trackInstance : src.screens.editorScreen.timeline.TimelineManager.tracks){
						if(trackInstance instanceof AudioTrack){
							for(TrackItem trackItem : trackInstance.trackItems){
								if(trackItem instanceof TrackAudioItem){
									TrackAudioItem trackAudioItem = (TrackAudioItem) trackItem;
									MediaItem mediaItem = trackAudioItem.mediaItem;
									if(mediaItem instanceof MediaAudioItem){
										MediaAudioItem mediaAudioItem = (MediaAudioItem) mediaItem;
										try {
											ByteArrayInputStream input = new ByteArrayInputStream(mediaAudioItem.getWAV());
											AudioInputStream audioIn = AudioSystem.getAudioInputStream(input);
											anim.addAudioTrack(audioIn, trackAudioItem.trackStartPosition);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					}



					for(int frameIndex = 0; frameIndex < Renderer.pixels.length; frameIndex++){

						MainApplet.instance.getjLabel19().setText("Frame "+(frameIndex+1)+" / "+(Renderer.pixels.length));

						src.screens.editorScreen.timeline.TimelineManager.setTimelinePosition(frameIndex);
						
						int currentPoints = (frameIndex+1);
						int goalPoints = (Renderer.pixels.length);
						int percent = (int) (currentPoints * 100) / goalPoints; 
						if(percent > 100){
							percent = 100;
						}
						if(percent < 0){
							percent = 0;
						}
						
						MainApplet.instance.getjLabel20().setText(percent+"%");
						
						MainApplet.instance.getjProgressBar1().setValue(percent);
						
						int[] pixels = Renderer.renderFrame(frameIndex, true, width, height, true);
						BufferedImage bufferedImage = ImagePanel.getBI(pixels, width, height);
						if(bufferedImage == null){
							bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
							bufferedImage.getGraphics().drawRect(30,30,100,100);
						}
						
						
						//SaveImage(width, height, pixels, "./"+frameIndex+".png");
						
						anim.addFrame(frameDuration, bufferedImage, null);
					}
					anim.close(false);

					time = System.currentTimeMillis()-time;
					System.out.println("wrote "+framesWritten+" frames: "+file.getAbsolutePath()+" ("+(time)/1000.0+" s)");
					MainApplet.instance.getjInternalFrame2().setVisible(false);
					cleanup();
					System.gc ();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}


		}.start();


	}
	
	public static void SaveImage(int width, int height, int ai[], String s) {
		BufferedImage bufferedimage = new BufferedImage(width, height, 1);
		bufferedimage.setRGB(0, 0, width, height, ai, 0, width);
		Graphics2D graphics2d = bufferedimage.createGraphics();
		graphics2d.dispose();
		try {
			File picture = new File(s);
			ImageIO.write(bufferedimage, "png", picture);
		} catch (IOException ioexception) {
			System.out.println("error!");
			ioexception.printStackTrace();
		}
	}
	
	/**
	 * Removes any temporary data generated by the export from memory as its no longer needed.
	 */
	public static void cleanup(){


		/*
		 * Store a local version of the global album array-list (To prevent any concurrency issues).
		 */
		ArrayList<Album> tempAlbums = (ArrayList<Album>) MediaPanelManager.albums.clone();

		for(Album album : tempAlbums){

			ArrayList<MediaItem> tempMediaItems = (ArrayList<MediaItem>) album.mediaItems.clone();

			for(MediaItem mediaItem : tempMediaItems){
				if(mediaItem instanceof MediaImageItem){
					MediaImageItem mediaImageItem = (MediaImageItem) mediaItem;
					if(!mediaImageItem.createdPreviewPixels){
						mediaImageItem.sourceImage = null;
					}
				}
			}
		}

	}
	
	/**
	 * Update the states of any components on the export panel.
	 */
	public static void updateVideoSizePanel(){
		
		
		if(selectedVideoSizeOption == 0){
			MainApplet.getInstance().getjRadioButton1().setSelected(true);
		} else {
			MainApplet.getInstance().getjRadioButton1().setSelected(false);
		}
		
		if(selectedVideoSizeOption == 1){
			MainApplet.getInstance().getjRadioButton3().setSelected(true);
			MainApplet.getInstance().getjComboBox2().setEnabled(true);
		} else {
			MainApplet.getInstance().getjRadioButton3().setSelected(false);
			MainApplet.getInstance().getjComboBox2().setEnabled(false);
		}
		
		if(selectedVideoSizeOption == 2){
			MainApplet.getInstance().getjRadioButton4().setSelected(true);
			MainApplet.getInstance().getjTextField4().setEnabled(true);
			MainApplet.getInstance().getjTextField5().setEnabled(true);
			MainApplet.getInstance().getjLabel6().setEnabled(true);
			MainApplet.getInstance().getjLabel7().setEnabled(true);
			MainApplet.getInstance().getjCheckBox3().setEnabled(true);
		} else {
			MainApplet.getInstance().getjRadioButton4().setSelected(false);
			MainApplet.getInstance().getjTextField4().setEnabled(false);
			MainApplet.getInstance().getjTextField5().setEnabled(false);
			MainApplet.getInstance().getjLabel6().setEnabled(false);
			MainApplet.getInstance().getjLabel7().setEnabled(false);
			MainApplet.getInstance().getjCheckBox3().setEnabled(false);
		}
		
	}
	
	public static void exportToGif(){
		AnimatedGifEncoder e = new AnimatedGifEncoder();
		e.start("hmm.gif");
		e.setDelay(30);   // 1 frame per sec (1000)
		e.setRepeat(0);

		
		
		for(int frameIndex = 0; frameIndex < 700; frameIndex++){
			
			int[] pixels = Renderer.renderFrame(frameIndex, true, 454, 255, false);
			BufferedImage bufferedImage = ImagePanel.getBI(pixels, 454, 255);
			if(bufferedImage == null){
				bufferedImage = new BufferedImage(454, 255, BufferedImage.TYPE_INT_RGB);
				bufferedImage.getGraphics().drawRect(30,30,100,100);
			}
			e.addFrame(bufferedImage);
			System.out.println("added frame: "+frameIndex);
		}
		
		
		
		e.finish();
	}
	
	
}
