package src.thirdPartyLibraries;



import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;






/**
 * @hide
 * @author liao
 *
 */
public class GifDecoder{


        public static final int STATUS_PARSING = 0;

        public static final int STATUS_FORMAT_ERROR = 1;

        public static final int STATUS_OPEN_ERROR = 2;

        public static final int STATUS_FINISH = -1;
        
        InputStream in;
        private int status;

        public int width; // full image width
        public int height; // full image height
        private boolean gctFlag; // global color table used
        private int gctSize; // size of global color table
        private int loopCount = 1; // iterations; 0 = repeat forever

        private int[] gct; // global color table
        private int[] lct; // local color table
        private int[] act; // active color table

        private int bgIndex; // background color index
        private int bgColor; // background color
        private int lastBgColor; // previous bg color
        private int pixelAspect; // pixel aspect ratio

        private boolean lctFlag; // local color table flag
        private boolean interlace; // interlace flag
        private int lctSize; // local color table size

        private int ix, iy, iw, ih; // current image rectangle
        private int lrx, lry, lrw, lrh;
        private BufferedImage image; // current frame
        private BufferedImage lastImage; // previous frame
        private GifFrame currentFrame = null;

        private boolean isShow = false;
        

        private byte[] block = new byte[256]; // current data block
        private int blockSize = 0; // block size

        // last graphic control extension info
        private int dispose = 0;
        // 0=no action; 1=leave in place; 2=restore to bg; 3=restore to prev
        private int lastDispose = 0;
        private boolean transparency = false; // use transparent color
        private int delay = 0; // delay in milliseconds
        private int transIndex; // transparent color index

        private static final int MaxStackSize = 4096;
        // max decoder pixel stack size

        // LZW decoder working arrays
        private short[] prefix;
        private byte[] suffix;
        private byte[] pixelStack;
        private byte[] pixels;

        private GifFrame gifFrame; // frames read from current file
        private int frameCount;


        
        
        byte[] gifData = null;

        private String imagePath = null;

        private boolean cacheImage = false;
        
        
        public GifDecoder(){
        }
        
//      public GifDecoder(byte[] data,GifAction act){
//              gifData = data;
//              action = act;
//                
//      }
//      
//      public GifDecoder(InputStream is,GifAction act){
//              in = is;
//              action = act;
//              
//      }

        public void setGifImage(byte[] data){
            gifData = data;
        }
        
        public void setGifImage(InputStream is){
            in = is;
        }
        

        
        
        
        
       
        
     
        
       /*(rivate void saveImage(BufferedImage image,String name){
            try{
                File f = new File(imagePath + File.separator + name + ".png");
                FileOutputStream fos = new FileOutputStream(imagePath + File.separator + getDir() + ".png");
                image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }catch(Exception ex){
                
            }
        }*/
        
        
        public static void SaveImage2(int width, int height, int ai[], String s) {
    		BufferedImage bufferedimage = new BufferedImage(width, height, 1);
    		bufferedimage.setRGB(0, 0, width, height, ai, 0, width);
    		Graphics2D graphics2d = bufferedimage.createGraphics();
    		graphics2d.dispose();
    		File picture = null;
    		try {
    			picture = new File("C:/Users/Harry/workspace/JavaVideoTesting/fuck: 24.png");
    			//ImageIO.write(bufferedimage, "png", picture);
    			
    		} catch (Exception ioexception) {
    			System.out.println("error!");
    			ioexception.printStackTrace();
    		}
    		System.out.println(picture.getAbsolutePath()+" exists: "+picture.exists()+"");
    	}
        
        public static void saveImage(BufferedImage bufferedimage, String s) {
        	
        	// System.out.println("saving: "+("frame: "+frameCount+".png"));
        	 
        	
    		//BufferedImage bufferedimage = new BufferedImage(width, height, 1);
    		///bufferedimage.setRGB(0, 0, width, height, ai, 0, width);
    		Graphics2D graphics2d = bufferedimage.createGraphics();
    		graphics2d.dispose();
    		try {
    			File picture = new File(s);
    			System.out.println("saving: "+s+", "+bufferedimage);
    			ImageIO.write(bufferedimage, "png", picture);
    		} catch (IOException ioexception) {
    			System.out.println("error!");
    			ioexception.printStackTrace();
    		}
    	}


        

        public int getDelay(int n) {
                delay = -1;
                if ((n >= 0) && (n < frameCount)) {
                        // delay = ((GifFrame) frames.elementAt(n)).delay;
                        GifFrame f = getFrame(n);
                        if (f != null)
                                delay = f.delay;
                }
                return delay;
        }
        

        public int[] getDelays(){
                GifFrame f = gifFrame;
                int[] d = new int[frameCount];
                int i = 0;
                while(f != null && i < frameCount){
                        d[i] = f.delay;
                        f = f.nextFrame;
                        i++;
                }
                return d;
        }
        


        public int getFrameCount() {
                return frameCount;
        }

        public BufferedImage getImage() {
                return getFrameImage(0);
        }

        public int getLoopCount() {
                return loopCount;
        }

        private void setPixels() {
                int[] dest = new int[width * height];
                // fill in starting image contents based on last image's dispose code
                if (lastDispose > 0) {
                        if (lastDispose == 3) {
                                // use image before last
                                int n = frameCount - 2;
                                if (n > 0) {
                                        lastImage = getFrameImage(n - 1);
                                } else {
                                        lastImage = null;
                                }
                        }
                        if (lastImage != null) {
                                //lastImage.getPixels(dest, 0, width, 0, 0, width, height);
                                lastImage.getRGB(0, 0, width, height, dest, 0, width);
                                // copy pixels
                                if (lastDispose == 2) {
                                        // fill last image rect area with background color
                                        int c = 0;
                                        if (!transparency) {
                                                c = lastBgColor;
                                        }
                                        for (int i = 0; i < lrh; i++) {
                                                int n1 = (lry + i) * width + lrx;
                                                int n2 = n1 + lrw;
                                                for (int k = n1; k < n2; k++) {
                                                        dest[k] = c;
                                                }
                                        }
                                }
                        }
                }

                // copy each source line to the appropriate place in the destination
                int pass = 1;
                int inc = 8;
                int iline = 0;
                for (int i = 0; i < ih; i++) {
                        int line = i;
                        if (interlace) {
                                if (iline >= ih) {
                                        pass++;
                                        switch (pass) {
                                        case 2:
                                                iline = 4;
                                                break;
                                        case 3:
                                                iline = 2;
                                                inc = 4;
                                                break;
                                        case 4:
                                                iline = 1;
                                                inc = 2;
                                        }
                                }
                                line = iline;
                                iline += inc;
                        }
                        line += iy;
                        if (line < height) {
                                int k = line * width;
                                int dx = k + ix; // start of line in dest
                                int dlim = dx + iw; // end of dest line
                                if ((k + width) < dlim) {
                                        dlim = k + width; // past dest edge
                                }
                                int sx = i * iw; // start of line in source
                                while (dx < dlim) {
                                        // map color and insert in destination
                                        int index = ((int) pixels[sx++]) & 0xff;
                                        int c = act[index];
                                        if (c != 0) {
                                                dest[dx] = c;
                                        }
                                        dx++;
                                }
                        }
                }
                
                
                /*
                 * Grabbing pixels
                 */
                
                
                image.setRGB(0, 0, width, height, dest, 0, width);
                
                
                addFrame(dest, width, height);
               // System.out.println("added frame: "+frameCount+", length: "+dest.length);
                
               // SaveImage2(width, height, dest.clone(), "fuck: "+frameCount+".png");
               // System.out.println("saving: "+frameCount+", length: "+dest.length);
               // for(int index = 0; index < dest.length; index++){
                	//System.out.println("pixel["+index+"]: "+dest[index]);
               // }
                
                
                
               // try {
					//PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, dest, 0, width);
					//pixelgrabber.grabPixels();
				//} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
			//	}
                
                //image = Bitmap.createBitmap(dest, width, height, Config.ARGB_4444);
                
                
                
                
                
        }

        
        public ArrayList<Frame> frameList = new ArrayList<Frame>(100);
    	
    	public void addFrame(int[] pixels, int width, int height){
    		Frame frame = new Frame();
    		frame.pixels = pixels.clone();
    		frame.width = width;
    		frame.height = height;
    		frameList.add(frame);
    	}

        public BufferedImage getFrameImage(int n) {
                GifFrame frame = getFrame(n);   
                if (frame == null)
                        return null;
                else
                        return frame.image;
        }


        public GifFrame getCurrentFrame(){
                return currentFrame;
        }
        

        public GifFrame getFrame(int n) {
                GifFrame frame = gifFrame;
                int i = 0;
                while (frame != null) {
                        if (i == n) {
                                return frame;
                        } else {
                                frame = frame.nextFrame;
                        }
                        i++;
                }
                return null;
        }



        public GifFrame next() {        
                if(isShow == false){
                        isShow = true;
                        return gifFrame;
                }else{
                        if(currentFrame == null)
                                return null;
                        if(status == STATUS_PARSING){
                                if(currentFrame.nextFrame != null)
                                        currentFrame = currentFrame.nextFrame;                  
                                //currentFrame = gifFrame;
                        }else{                  
                                currentFrame = currentFrame.nextFrame;
                                if (currentFrame == null) {
                                        currentFrame = gifFrame;
                                }
                        }
//                      if(cacheImage){
//                          Log.d("===", "cache");
//                          try{
//                              currentFrame.image = BitmapFactory.decodeFile(imagePath + File.separator + currentFrame.imageName);
//                          }catch(Exception ex){
//                              ex.printStackTrace();
//                          }
//                      }
                        return currentFrame;
                }
        }

        int readByte(){
                in = new ByteArrayInputStream(gifData);
                gifData = null;
                return readStream();
        }
        
        
        int readStream(){
                init();
                if(in != null){
                        readHeader();
                        if(!err()){
                                readContents();
                                if(frameCount < 0){
                                        status = STATUS_FORMAT_ERROR;
                                }else{
                                        status = STATUS_FINISH;
                                }
                        }
                        try {
                                in.close();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                        
                }else {
                        status = STATUS_OPEN_ERROR;
                }
                return status;
        }

        private void decodeImageData() {
                int NullCode = -1;
                int npix = iw * ih;
                int available, clear, code_mask, code_size, end_of_information, in_code, old_code, bits, code, count, i, datum, data_size, first, top, bi, pi;

                if ((pixels == null) || (pixels.length < npix)) {
                        pixels = new byte[npix]; // allocate new pixel array
                }
                if (prefix == null) {
                        prefix = new short[MaxStackSize];
                }
                if (suffix == null) {
                        suffix = new byte[MaxStackSize];
                }
                if (pixelStack == null) {
                        pixelStack = new byte[MaxStackSize + 1];
                }
                // Initialize GIF data stream decoder.
                data_size = read();
                clear = 1 << data_size;
                end_of_information = clear + 1;
                available = clear + 2;
                old_code = NullCode;
                code_size = data_size + 1;
                code_mask = (1 << code_size) - 1;
                for (code = 0; code < clear; code++) {
                        prefix[code] = 0;
                        suffix[code] = (byte) code;
                }

                // Decode GIF pixel stream.
                datum = bits = count = first = top = pi = bi = 0;
                for (i = 0; i < npix;) {
                        if (top == 0) {
                                if (bits < code_size) {
                                        // Load bytes until there are enough bits for a code.
                                        if (count == 0) {
                                                // Read a new data block.
                                                count = readBlock();
                                                if (count <= 0) {
                                                        break;
                                                }
                                                bi = 0;
                                        }
                                        datum += (((int) block[bi]) & 0xff) << bits;
                                        bits += 8;
                                        bi++;
                                        count--;
                                        continue;
                                }
                                // Get the next code.
                                code = datum & code_mask;
                                datum >>= code_size;
                                bits -= code_size;

                                // Interpret the code
                                if ((code > available) || (code == end_of_information)) {
                                        break;
                                }
                                if (code == clear) {
                                        // Reset decoder.
                                        code_size = data_size + 1;
                                        code_mask = (1 << code_size) - 1;
                                        available = clear + 2;
                                        old_code = NullCode;
                                        continue;
                                }
                                if (old_code == NullCode) {
                                        pixelStack[top++] = suffix[code];
                                        old_code = code;
                                        first = code;
                                        continue;
                                }
                                in_code = code;
                                if (code == available) {
                                        pixelStack[top++] = (byte) first;
                                        code = old_code;
                                }
                                while (code > clear) {
                                        pixelStack[top++] = suffix[code];
                                        code = prefix[code];
                                }
                                first = ((int) suffix[code]) & 0xff;
                                // Add a new string to the string table,
                                if (available >= MaxStackSize) {
                                        break;
                                }
                                pixelStack[top++] = (byte) first;
                                prefix[available] = (short) old_code;
                                suffix[available] = (byte) first;
                                available++;
                                if (((available & code_mask) == 0)
                                                && (available < MaxStackSize)) {
                                        code_size++;
                                        code_mask += available;
                                }
                                old_code = in_code;
                        }

                        // Pop a pixel off the pixel stack.
                        top--;
                        pixels[pi++] = pixelStack[top];
                        i++;
                }
                for (i = pi; i < npix; i++) {
                        pixels[i] = 0; // clear missing pixels
                }
        }

        private boolean err() {
                return status != STATUS_PARSING;
        }

        private void init() {
                status = STATUS_PARSING;
                frameCount = 0;
                gifFrame = null;
                gct = null;
                lct = null;
        }

        private int read() {
                int curByte = 0;
                try {
                        
                        curByte = in.read();
                } catch (Exception e) {
                        status = STATUS_FORMAT_ERROR;
                }
                return curByte;
        }

        
        
        
        private int readBlock() {
                blockSize = read();
                int n = 0;
                if (blockSize > 0) {
                        try {
                                int count = 0;
                                while (n < blockSize) {
                                        count = in.read(block, n, blockSize - n);
                                        if (count == -1) {
                                                break;
                                        }
                                        n += count;
                                }
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                        if (n < blockSize) {
                                status = STATUS_FORMAT_ERROR;
                        }
                }
                return n;
        }

        private int[] readColorTable(int ncolors) {
                int nbytes = 3 * ncolors;
                int[] tab = null;
                byte[] c = new byte[nbytes];
                int n = 0;
                try {
                        n = in.read(c);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                if (n < nbytes) {
                        status = STATUS_FORMAT_ERROR;
                } else {
                        tab = new int[256]; // max size to avoid bounds checks
                        int i = 0;
                        int j = 0;
                        while (i < ncolors) {
                                int r = ((int) c[j++]) & 0xff;
                                int g = ((int) c[j++]) & 0xff;
                                int b = ((int) c[j++]) & 0xff;
                                tab[i++] = 0xff000000 | (r << 16) | (g << 8) | b;
                        }
                }
                return tab;
        }

        private void readContents() {
                // read GIF file content blocks
                boolean done = false;
                while (!(done || err())) {
                        int code = read();
                        switch (code) {
                        case 0x2C: // image separator
                                readImage();
                                break;
                        case 0x21: // extension
                                code = read();
                                switch (code) {
                                case 0xf9: // graphics control extension
                                        readGraphicControlExt();
                                        break;
                                case 0xff: // application extension
                                        readBlock();
                                        String app = "";
                                        for (int i = 0; i < 11; i++) {
                                                app += (char) block[i];
                                        }
                                        if (app.equals("NETSCAPE2.0")) {
                                                readNetscapeExt();
                                        } else {
                                                skip(); // don't care
                                        }
                                        break;
                                default: // uninteresting extension
                                        skip();
                                }
                                break;
                        case 0x3b: // terminator
                                done = true;
                                break;
                        case 0x00: // bad byte, but keep going and see what happens
                                break;
                        default:
                                status = STATUS_FORMAT_ERROR;
                        }
                }
                GifView.isRun = false;
               // GifView.drawThread.stop();
        }

        private void readGraphicControlExt() {
                read(); // block size
                int packed = read(); // packed fields
                dispose = (packed & 0x1c) >> 2; // disposal method
                if (dispose == 0) {
                        dispose = 1; // elect to keep old image if discretionary
                }
                transparency = (packed & 1) != 0;
                delay = readShort() * 10; // delay in milliseconds
                transIndex = read(); // transparent color index
                read(); // block terminator
        }

        private void readHeader() {
                String id = "";
                for (int i = 0; i < 6; i++) {
                        id += (char) read();
                }
                if (!id.startsWith("GIF")) {
                        status = STATUS_FORMAT_ERROR;
                        return;
                }
                readLSD();
                if (gctFlag && !err()) {
                        gct = readColorTable(gctSize);
                        bgColor = gct[bgIndex];
                }
        }

        private void readImage() {
        	
        	//System.out.println("reading");
        	
                ix = readShort(); // (sub)image position & size
                iy = readShort();
                iw = readShort();
                ih = readShort();
                int packed = read();
                lctFlag = (packed & 0x80) != 0; // 1 - local color table flag
                interlace = (packed & 0x40) != 0; // 2 - interlace flag
                // 3 - sort flag
                // 4-5 - reserved
                lctSize = 2 << (packed & 7); // 6-8 - local color table size
                if (lctFlag) {
                        lct = readColorTable(lctSize); // read table
                        act = lct; // make local table active
                } else {
                        act = gct; // make global table active
                        if (bgIndex == transIndex) {
                                bgColor = 0;
                        }
                }
                int save = 0;
                if (transparency) {
                        save = act[transIndex];
                        act[transIndex] = 0; // set transparent color if specified
                }
                if (act == null) {
                        status = STATUS_FORMAT_ERROR; // no color table defined
                }
                if (err()) {
                        return;
                }
                decodeImageData(); // decode pixel data
                skip();
                if (err()) {
                        return;
                }
                frameCount++;
                // create new image to receive frame data
                
                
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                
               // image = Bitmap.createBitmap(width, height, Config.ARGB_4444);
                
                
                
                image = bufferedImage;
                setPixels(); // transfer pixel data to image
               // saveImage(image, "frame: "+frameCount+".png");
                
                
        
                if (transparency) {
                        act[transIndex] = save;
                }
                resetFrame();
               // action.parseOk(true, frameCount);
        }

        private void readLSD() {
                // logical screen size
                width = readShort();
                height = readShort();
                // packed fields
                int packed = read();
                gctFlag = (packed & 0x80) != 0; // 1 : global color table flag
                // 2-4 : color resolution
                // 5 : gct sort flag
                gctSize = 2 << (packed & 7); // 6-8 : gct size
                bgIndex = read(); // background color index
                pixelAspect = read(); // pixel aspect ratio
        }

        private void readNetscapeExt() {
                do {
                        readBlock();
                        if (block[0] == 1) {
                                // loop count sub-block
                                int b1 = ((int) block[1]) & 0xff;
                                int b2 = ((int) block[2]) & 0xff;
                                loopCount = (b2 << 8) | b1;
                        }
                } while ((blockSize > 0) && !err());
        }

        private int readShort() {
                // read 16-bit value, LSB first
                return read() | (read() << 8);
        }

        private void resetFrame() {
                lastDispose = dispose;
                lrx = ix;
                lry = iy;
                lrw = iw;
                lrh = ih;
                lastImage = image;
                lastBgColor = bgColor;
                dispose = 0;
                transparency = false;
                delay = 0;
                lct = null;
        }

        /**
         * Skips variable length blocks up to and including next zero length block.
         */
        private void skip() {
                do {
                        readBlock();
                } while ((blockSize > 0) && !err());
        }
}