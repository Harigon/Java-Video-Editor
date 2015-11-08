package src;

import java.awt.Component;

import javax.swing.JApplet;
import javax.swing.JFrame;

public class Main extends MainApplet {
//
	  public static void main(String[] args) {
	    JApplet applet = new MainApplet();
	    JFrame frame = new JFrame("Visual Cross");
	    frame.getContentPane().add(applet);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    applet.init();
	    frame.pack();
	    frame.setSize(800, 500);
	    frame.setVisible(true);
	    frame.show();
	}
}
