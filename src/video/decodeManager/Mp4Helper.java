package src.video.decodeManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import asg.jcodec.api.FrameGrab;
import asg.jcodec.common.FileChannelWrapper;
import asg.jcodec.common.JCodecUtil;
import asg.jcodec.common.NIOUtils;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaVideoItem;

public class Mp4Helper extends DecodeManager{

	
	public FrameGrab frameGrab = null;
	public int lastFrame = -1;
	
	
	public Mp4Helper(MediaVideoItem mediaVideoItem, String directory) throws Exception {
		super(mediaVideoItem, directory);
		
		FileChannelWrapper ch2 = null;
		ch2 = NIOUtils.readableFileChannel(new File(directory));

		//double startSec = 51.632;
		frameGrab = new FrameGrab(ch2);

		mediaVideoItem.totalFrames = (int) frameGrab.demuxer.getVideoTrack().getFrameCount();

		double scale = frameGrab.demuxer.getVideoTrack().getTimescale();
		double duration = frameGrab.demuxer.getVideoTrack().getDuration().getNum();

		mediaVideoItem.framerate = (double) (scale/duration);
		mediaVideoItem.framerate = 23;
		
		System.out.println("fps: "+mediaVideoItem.framerate+", scale: "+scale+", dur: "+duration);
		
	}

	@Override
	public BufferedImage requestFrame(int frameIndex) throws Exception {
		BufferedImage image;
		if(frameIndex == lastFrame+1){
			image = frameGrab.getFrame();
			System.out.println("grabbed next frame");
		} else {
			image = JCodecUtil.toBufferedImage(frameGrab.seekToFramePrecise(frameIndex).getNativeFrame());
			System.out.println("wag1");
		}
		lastFrame = frameIndex;
		return image;
	}

}
