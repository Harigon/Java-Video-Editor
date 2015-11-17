package src.screens.editorScreen.libraryPanel.mediaPanel.album.media;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import src.dataStore.DataStore;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.thirdPartyLibraries.mp3transform.main.org.mp3transform.wav.WavConverter;
import src.util.Misc;

public  class MediaAudioItem extends MediaItem{
//
	public MediaAudioItem(String directory, Album album) {
		super(directory, album);
		// TODO Auto-generated constructor stub
	}
	
	public byte[] wavBytes = null;
	
	public byte[] getWAV(){

		if(wavBytes != null){
			return wavBytes;
		}

		String in = directory;
		String out = DataStore.getLocation()+"temp.wav";

		try {
			WavConverter.convert(in, out);

			File file = new File(out);

			if(file.exists()){
				wavBytes = Misc.getBytesFromFile(file);//Converts the file into an array of bytes.
				file.delete();
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wavBytes;
	}

	@Override
	public BufferedImage getThumbnail() {
		// TODO Auto-generated method stub
		return null;
	}

}
