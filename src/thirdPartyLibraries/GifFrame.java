package src.thirdPartyLibraries;

import java.awt.image.BufferedImage;

public class GifFrame {

        public GifFrame(BufferedImage im, int del) {
                image = im;
                delay = del;
        }
        
        public GifFrame(String name,int del){
            imageName = name;
            delay = del;
        }
        

        public BufferedImage image;

        public int delay;

        public String imageName = null;
        

        public GifFrame nextFrame = null;
}