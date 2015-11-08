package src;

public class Project {

	public static int projectWidth = 1280;
	public static int projectHeight = 720;
	public static double frameRate = 30;
	//
	
	
	
	/**
	 * Sets up the project with the specified dimensions and frame rate.
	 * @param width - The width of the video.
	 * @param height - The height of the video.
	 * @param fps - The frame rate which the video will run at.
	 */
	public static void setupProject(int width, int height, double fps){
		/*
		 * Update the variables
		 */
		Project.projectWidth = width;
		Project.projectHeight = height;
		Project.frameRate = fps;
		
		MainApplet.instance.getjTextField2().setText(width+" x "+height);//Update the dimensions in the export tab.
		
	}
	
	
	/**
	 * Returns a scaled width value from the original dimensions (Maintains aspect ratio)
	 */
	public static int getScaledWidth(){
		double divider = Project.projectWidth / ((Double)454.0);
		
		if(Project.projectHeight / divider > ((Double)282.0)){
			divider = Project.projectHeight / ((Double)282.0);
		}
		return (int) (Project.projectWidth / divider);
	}
	
	/**
	 * Returns a scaled height value from the original dimensions (Maintains aspect ratio)
	 */
	public static int getScaledHeight(){
		double divider = Project.projectWidth / ((Double)454.0);
		
		if(Project.projectHeight / divider > ((Double)282.0)){
			divider = Project.projectHeight / ((Double)282.0);
		}

		return (int) (Project.projectHeight / divider);
	}
	
	
}
