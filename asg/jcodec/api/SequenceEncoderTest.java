package asg.jcodec.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

public class SequenceEncoderTest {

    public static void main(String args[]) throws IOException {
    	
    	
    	
    	
    	 SequenceEncoder enc = new SequenceEncoder(new File("C:/Users/Harry/DataStore/enc.mov"));
    	File [] files = new File("C:/Users/Harry/DataStore/Cache").listFiles();
        for (int i = 0; i < files.length; i++){
            if (files[i].isFile()){ //this line weeds out other directories/folders
               
            	
            	BufferedImage image = ImageIO.read(files[i]);
                enc.encodeImage(image);
            	
            	System.out.println("encoding: "+i);
            	
            }
        }

        enc.finish();
        
        System.out.println("done");
    }
}
