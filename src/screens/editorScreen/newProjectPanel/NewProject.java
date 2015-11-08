package src.screens.editorScreen.newProjectPanel;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.JOptionPane;


import src.ImagePanel;
import src.MainApplet;
import src.Project;
import src.renderer.Renderer;
import src.util.Misc;
import sun.misc.GC;

/**
 * @author Harry
 *
 */
/**
 * @author Harry
 *
 */
public class NewProject {

	
	public static int selectedVideoSizeOption = 0;
	
	public static int fps = 30;
	
	public static double[] frameRates = {29.970, 25.000, 24.000, 23.976};
	public static int[][] dimensions = {{1920,1080},{1680,1050},{1600,1024},{1400,1050},{1280,1024},{1366,768},{1280,720},{1024,768},{800,600},{640,480}};
	
	
	public static boolean returnToVideoEditor = false;
	
	/**
	 * Opens the panel.
	 * @param returnToVideoEditor - If the cancel button is clicked, return to editor screen.
	 */
	public static void open(boolean returnToVideoEditor){
		MainApplet.getInstance().getjPanel15().setVisible(false);
		MainApplet.getInstance().getjPanel16().setVisible(false);
		MainApplet.getInstance().getjPanel30().setVisible(false);
		
		MainApplet.getInstance().getjPanel32().setVisible(true);
		NewProject.returnToVideoEditor = returnToVideoEditor;
		NewProject.refresh();
		updateVideoSizePanel();
	}
	
	/**
	 * Apply the data.
	 */
	public static void ok(){
		
		
		if(MainApplet.getInstance().getjLabel25().getForeground().getRed() == 204){
			JOptionPane.showMessageDialog(null, "One or more fields contain invalid or missing data!", "Error", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		
		MainApplet.getInstance().getjPanel32().setVisible(false);
		MainApplet.getInstance().getjPanel16().setVisible(true);
		
		int fps = MainApplet.getInstance().getjComboBox10().getSelectedIndex();
		int dimension = MainApplet.getInstance().getjComboBox9().getSelectedIndex();
		

		Project.setupProject(dimensions[dimension][0], dimensions[dimension][1], frameRates[fps]);
		
	}
	
	
	/**
	 * Cancel and close the panel.
	 */
	public static void cancel(){
		if(NewProject.returnToVideoEditor){
			MainApplet.getInstance().getjPanel32().setVisible(false);
			MainApplet.getInstance().getjPanel16().setVisible(true);
		} else {
			MainApplet.getInstance().getjPanel32().setVisible(false);
			MainApplet.getInstance().getjPanel30().setVisible(true);
		}
	}
	
	
	public static String projectName;
	
	
	
	/**
	 * Updates the panel with latest data.
	 */
	public static void refresh(){
		MainApplet.getInstance().getjTextField11().setText(projectName);
		refreshStars();
	}
	
	/**
	 * Refreshes the star/validation labels.
	 */
	public static void refreshStars(){
		MainApplet.getInstance().getjLabel25().setForeground(new Color(204,0,0));


		
		if(projectName != null){
			if(projectName.length() >= 1 && projectName.length() <= 15){
				MainApplet.getInstance().getjLabel25().setForeground(new Color(0,204,0));
			}
		}
		
		

	}
	
	/**
	 * Refreshes panel with correct states/data.
	 */
	public static void updateVideoSizePanel(){
		
		if(selectedVideoSizeOption == 0){
			MainApplet.getInstance().getjComboBox9().setEnabled(true);
		} else {
			MainApplet.getInstance().getjComboBox9().setEnabled(false);
		}
		
		
	}
	
	
	
}
