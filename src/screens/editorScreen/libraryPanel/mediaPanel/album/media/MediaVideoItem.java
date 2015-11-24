package src.screens.editorScreen.libraryPanel.mediaPanel.album.media;

import static asg.jcodec.common.NIOUtils.readableFileChannel;
import static asg.jcodec.common.NIOUtils.writableFileChannel;
import static asg.jcodec.containers.mp4.TrackType.VIDEO;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImagingOpException;
import java.awt.image.PixelGrabber;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import asg.jcodec.api.FrameGrab;
import asg.jcodec.api.JCodecException;
import asg.jcodec.common.FileChannelWrapper;
import asg.jcodec.common.JCodecUtil;
import asg.jcodec.common.NIOUtils;
import asg.jcodec.common.SeekableByteChannel;
import asg.jcodec.containers.mp4.Brand;
import asg.jcodec.containers.mp4.MP4Packet;
import asg.jcodec.containers.mp4.boxes.AudioSampleEntry;
import asg.jcodec.containers.mp4.demuxer.AbstractMP4DemuxerTrack;
import asg.jcodec.containers.mp4.demuxer.MP4Demuxer;
import asg.jcodec.containers.mp4.muxer.FramesMP4MuxerTrack;
import asg.jcodec.containers.mp4.muxer.MP4Muxer;
import asg.jcodec.containers.mp4.muxer.PCMMP4MuxerTrack;




import src.ImagePanel;
import src.MainApplet;
import src.Project;
import src.dataStore.DataStore;
import src.multiThreading.threads.DecodingThread;
import src.renderer.Renderer;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.thirdPartyLibraries.Frame;
import src.thirdPartyLibraries.GifView;
import src.thirdPartyLibraries.Scalr;
import src.thirdPartyLibraries.Scalr.Method;
import src.thirdPartyLibraries.Scalr.Mode;
import src.util.Misc;
import src.video.decodeManager.AviHelper;
import src.video.decodeManager.DecodeManager;
import src.video.decodeManager.Mp4Helper;

public class MediaVideoItem extends MediaItem {

	public Image previewImage;
	
	public BufferedImage thumbnail;
	public double framerate;
	
	public int[] fullPixels;
	public BufferedImage fullImage;
	
	public MediaVideoItem instance;

	
	public byte[] frameDecoded;
	
	public static int thumbnailWidth = 454;
	public static int thumbnailHeight = 255;
	
	public boolean createdPreviewPixels = false;
	
	public Image sourceImage = null;
	
	public int formatType;
	public String getReferenceName(){
		return ""+directory.hashCode()+"";
	}
	
	
	public int totalFrames = 0;
	
	
	
	public boolean busy = false;
	
	public DecodeManager videoDecoder;
	
	public BufferedImage requestFrame(int frameIndex){
		while(true){
			if(!busy){
				busy = true;
				try {
					BufferedImage image = videoDecoder.requestFrame(frameIndex);
					if(frameIndex == 0){
						thumbnail = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 67, null);
					}
					busy = false;
					return image;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				//System.out.println("Busy");
			}
		}
	}
	
	public Image getFullFrame(int frameIndex, int width, int height){

		try {

			if(formatType == 1){
				//BufferedImage image = requestFrame(frameIndex);
				
				
				System.out.println("got full frame: "+frameIndex);
				
				System.out.println("start image");
				BufferedImage image = DecodingThread.requestFrame(this, frameIndex);
				System.out.println("got image");
				
				Image resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, width, null);
				if(resizedImage.getHeight(null) >= Project.getScaledHeight()){
					resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, height, null);
				}
				return resizedImage;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
	
	public static Image getImageFromFile(File file){
		if(file.exists()){
			Image image = null;
			image = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
			while(image == null){
				
			}
			return image;
		}
		return null;
	}
	
	public Image getPreviewFrame(int frameIndex){
		try {
		if(formatType == 0 || formatType == 1 || formatType == MediaManager.FORMAT_AVI){
			
		int width;
		int height;
		int[] pixels;
		
		//
		File tempFile = new File(DataStore.getCache()+"/"+getReferenceName()+"/"+frameIndex+".png");
		
		Image image = null;
		if(tempFile.exists()){
			
			
			image = Toolkit.getDefaultToolkit().getImage(tempFile.getAbsolutePath());
			ImageIcon imageicon = new ImageIcon(image);
			width = imageicon.getIconWidth();
			height = imageicon.getIconHeight();
			pixels = new int[width * height];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
			pixelgrabber.grabPixels();
		}
		
		if(image == null){
			BufferedImage unloadedImage = new BufferedImage(Project.getScaledWidth(), Project.getScaledHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = unloadedImage.getGraphics();
			g.setColor(new Color(0,0,0));
			g.fillRect(0, 0, 71, 56);
			g.setColor(new Color(255,255,255));
			g.drawString("Importing...", 3, 30);
			image = unloadedImage;
		}
		
		
		System.out.println("image: "+image);
		
		Image resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, Project.getScaledWidth(), null);
		if(resizedImage.getHeight(null) >= Project.getScaledHeight()){
			resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, Project.getScaledHeight(), null);
		}
		
		return resizedImage;
		}
		
		
		
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void generatePreviewFile(){
		/*
		 * Decoding GIF
		 */
		try {
			GifView gifView = new GifView();
			gifView.setGifImage(Misc.getBytesFromFile(new File(directory)));
			gifView.start();
			
			int index = 0;
			
			//DataOutputStream os = new DataOutputStream(new FileOutputStream(DataStore.getCache()+"/"+getReferenceName()+".dat"));
			
			totalFrames = gifView.gifDecoder.frameList.size();
			
			for (Frame frame : gifView.gifDecoder.frameList) {
				BufferedImage bufferedImage = ImagePanel.getBI(frame.pixels, frame.width, frame.height);
				File picture = new File(DataStore.getCache()+"/"+getReferenceName()+"/"+index+".png");
			    ImageIO.write(bufferedImage, "png", picture);
				index++;
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * Returns the full image data from the original source (no compression etc.)
	 * @param width2 - The width that we need it to be.
	 * @param height2 - The height that we need it to be.
	 * @return
	 */
	public Image getFullImage(int width2, int height2){
		if(sourceImage != null){
			return sourceImage;
		}
		int width;
		int height;
		int[] pixels;
		Image finalImage = null;
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(directory);
			ImageIcon imageicon = new ImageIcon(image);
			width = imageicon.getIconWidth();
			height = imageicon.getIconHeight();
			pixels = new int[width * height];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
			pixelgrabber.grabPixels();
			Image resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, width2, null);
			if(resizedImage.getHeight(null) >= height2){
				resizedImage = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, height2, null);
			}
			finalImage = resizedImage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sourceImage = finalImage;
		return finalImage;
	}
	
	
	
	
	public void decodeFrame(int frameIndex){
		try {
			if(new File(DataStore.getCache()+"/"+getReferenceName()+"/"+frameIndex+".png").exists()){
				return;
			}
			BufferedImage image = requestFrame(frameIndex);
			//Image resizedImage = image;
			Image resizedImage = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, 300, null);
			if(resizedImage.getHeight(null) >= Project.getScaledHeight()){
				resizedImage = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, 300, null);
			}
			ImageIO.write(ImagePanel.getBI(resizedImage), "png", new File(DataStore.getCache()+"/"+getReferenceName()+"/"+frameIndex+".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void decodeAudio(FileChannelWrapper input){
	      /*  try {
	        	
	            MP4Demuxer demuxer = new MP4Demuxer(input);
	            MP4Muxer muxer = new MP4Muxer(output, Brand.MOV);

	            List<AbstractMP4DemuxerTrack> at = demuxer.getAudioTracks();
	            List<PCMMP4MuxerTrack> audioTracks = new ArrayList<PCMMP4MuxerTrack>();
	            for (AbstractMP4DemuxerTrack demuxerTrack : at) {
	                PCMMP4MuxerTrack att = muxer.addUncompressedAudioTrack(((AudioSampleEntry) demuxerTrack.getSampleEntries()[0]).getFormat());
	                audioTracks.add(att);
	                att.setEdits(demuxerTrack.getEdits());
	                att.setName(demuxerTrack.getName());
	            }

	            AbstractMP4DemuxerTrack vt = demuxer.getVideoTrack();
	            FramesMP4MuxerTrack video = muxer.addTrackForCompressed(VIDEO, (int) vt.getTimescale());
	            // vt.open(input);
	            video.setTimecode(muxer.addTimecodeTrack((int) vt.getTimescale()));
	            video.setEdits(vt.getEdits());
	            video.addSampleEntries(vt.getSampleEntries());
	            MP4Packet pkt = null;
	            while ((pkt = (MP4Packet)vt.nextFrame()) != null) {
	               // pkt = processFrame(pkt);
	                video.addFrame(pkt);

	                for (int i = 0; i < at.size(); i++) {
	                    AudioSampleEntry ase = (AudioSampleEntry) at.get(i).getSampleEntries()[0];
	                    int frames = (int) (ase.getSampleRate() * pkt.getDuration() / vt.getTimescale());
	                    MP4Packet apkt = (MP4Packet)at.get(i).nextFrame();
	                    audioTracks.get(i).addSamples(apkt.getData());
	                }
	            }

	            muxer.writeHeader();
	        } finally {
	            if (input != null)
	                input.close();
	            if (output != null)
	                output.close();
	        }*/
	}
	
	
	public MediaVideoItem(final String directory, Album album, int formatType) {
		super(directory, album);
		//createPreviewPixels();


		instance = this;

		File dir = new File(DataStore.getCache()+"/"+getReferenceName()+"");
		if(!dir.exists()){
			dir.mkdir();
		}

		this.formatType = formatType;

		new Thread() {
			public void run() {
				try {


					if(formatType == 1){
						videoDecoder = new Mp4Helper(instance, directory);
					}
					
					if(formatType == MediaManager.FORMAT_AVI){
						videoDecoder = new AviHelper(instance, directory);
					}



					frameDecoded = new byte[totalFrames];

					File tempFile = new File(DataStore.getCache()+"/"+getReferenceName()+"/"+0+".png");

					Image image = null;
					if(tempFile.exists()){
						image = Toolkit.getDefaultToolkit().getImage(tempFile.getAbsolutePath());
						ImageIcon imageicon = new ImageIcon(image);
						int width = imageicon.getIconWidth();
						int height = imageicon.getIconHeight();
						int[] pixels = new int[width * height];
						PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
						pixelgrabber.grabPixels();
						thumbnail = Scalr.resize(ImagePanel.getBI(image), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 67, null);
					}





					DecodingThread.updateDecodingPriority();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	
	
	

	@Override
	public BufferedImage getThumbnail() {
		if(thumbnail == null){
			BufferedImage unloadedImage = new BufferedImage(71, 56, BufferedImage.TYPE_INT_ARGB);
			Graphics g = unloadedImage.getGraphics();
			g.setColor(new Color(0,0,0));
			g.fillRect(0, 0, 71, 56);
			g.setColor(new Color(255,255,255));
			g.drawString("Importing...", 3, 30);
			return unloadedImage;
		}
		return thumbnail;
	}


	
	
}
