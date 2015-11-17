package src.thirdPartyLibraries;




import java.awt.image.BufferedImage;
import java.io.InputStream;




/**
 * GifView<br>
 * @author liao
 *
 */
public class GifView {


        public GifDecoder gifDecoder = null;

        private BufferedImage currentImage = null;
        
        public static boolean isRun = true;
        
        private boolean pause = false;
        

        
        private GifImageType animationType = GifImageType.SYNC_DECODER;

        /**
         * @author liao
         *
         */
        public enum GifImageType{
                WAIT_FINISH (0),
                SYNC_DECODER (1),
                COVER (2);
                
                GifImageType(int i){
                        nativeInt = i;
                }
                final int nativeInt;
        }
        

    

    


    private void setGifDecoderImage(byte[] gif){

        if(gifDecoder == null){
            gifDecoder = new GifDecoder();
        }
        gifDecoder.setGifImage(gif);
     //   gifDecoder.start();
    }
    

    private void setGifDecoderImage(InputStream is){

        if(gifDecoder == null){
            gifDecoder = new GifDecoder();
        }
        gifDecoder.setGifImage(is);
       // gifDecoder.start();
        
        
    }
    
    


    

    public void setGifImage(byte[] gif){
        setGifDecoderImage(gif);
    }
    

    public void setGifImage(InputStream is){
        setGifDecoderImage(is);
    }
    

    public void setGifImage(int resId){
        //Resources r = getResources();
        //InputStream is = r.openRawResource(resId);
        //setGifDecoderImage(is);
    }
    


    

    public void setGifImageType(GifImageType type){
        if(gifDecoder == null)
                animationType = type;
    }
  

    



    public void start(){
    	if(gifDecoder == null){
            return;
    }
    while(isRun){
    	
    	if(gifDecoder.in != null){
    		gifDecoder.readStream();
    }else if(gifDecoder.gifData != null){
    	gifDecoder.readByte();
    }
    	
        if(gifDecoder.getFrameCount() == 1){
            GifFrame f = gifDecoder.next();
            currentImage = f.image;
        //gifDecoder.free();
            
            break;
        }
    if (pause == false) {
        GifFrame frame = gifDecoder.next();

        if (frame == null) {
            try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            continue;
        }
        if (frame.image != null)
            currentImage = frame.image;
       // else if (frame.imageName != null) {
           // currentImage = BitmapFactory.decodeFile(frame.imageName);
        //}
        long sp = frame.delay;
        
    } else {
    	try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            }
    }
    }
    
    
}