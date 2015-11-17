package src.thirdPartyLibraries.audio;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import src.dataStore.DataStore;
import src.thirdPartyLibraries.audio.jl.player.advanced.AdvancedPlayer;



public class MP3 {
	private String filename;

	// constructor that takes the name of an MP3 file
	public MP3(String filename) {
		this.filename = filename;
	}


	public static final ArrayList<SoundMP3> MP3s = new ArrayList<SoundMP3>(10000);

	
	
	public static final int MP3_LOBBY_BACKGROUND = 0;
	public static final int MP3_TIMES_ALMOST_UP = 10;
	public static final int MP3_HALF_TIME = 11;
	public static final int MP3_NUKE_ANNOUNCEMENT1 = 30;
	public static final int MP3_NUKE_ANNOUNCEMENT2 = 31;
	public static final int MP3_MAIN_CHARACTER_DIEING = 32;


	public static void stopMP3Id(int musicId){
		for(SoundMP3 mp3 : MP3s) {
			if(mp3.mp3Id == musicId){
				mp3.player.close();
				destroyMp3(mp3);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void destroyMp3(SoundMP3 mp3){
		try {
			MP3s.remove(mp3);
			mp3.mp3Id = -1;
			//if(mp3.thread != null){
			//mp3.thread.stop();
			//}
			//mp3.player.close();
			//if(mp3.player != null){
			//mp3.player = null;
			//}

			mp3 = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static InputStream[] storedInputStreams = new InputStream[100];


	public static SoundMP3 getMp3(int mp3Id){
		for(SoundMP3 mp3 : MP3s) {
			if(mp3.mp3Id == mp3Id){
				return mp3;
			}
		}
		return null;
	}

	public static void stopSong(int id){
		for(SoundMP3 mp3 : MP3s) {
			if(mp3.mp3Id == id){
				mp3.player.close();
			}
		}
	}

	public static void sendMp3(final int mp3Id, int decreasedVolume){

		boolean lol = true;
		if(lol){
			//return;
		}

		try {

			SoundMP3 existingSoundMp3 = getMp3(mp3Id);
			if(existingSoundMp3 != null){
				destroyMp3(existingSoundMp3);
				MP3s.remove(existingSoundMp3);
			}

			BufferedInputStream bis;
			bis = new BufferedInputStream(new FileInputStream(new File(DataStore.getSounds()+""+mp3Id+".mp3")));


			//final SoundMP3 soundMp3 = getMp3(mp3Id) == null ? new SoundMP3(mp3Id, new Player(bis)) : getMp3(mp3Id);
			final SoundMP3 soundMp3 = new SoundMP3(mp3Id, new AdvancedPlayer(bis, decreasedVolume));


			/*
			 * run in new thread to play in background
			 */
			new Thread() {
				public void run() {
					try { 
						soundMp3.thread = this;
						soundMp3.player.play();
					}
					catch (Exception e) { 
						System.out.println(e); 
					}
				}
			}.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}


	public static byte[][] storedFileBytes = new byte[100][0];

	/*public static InputStream getInputStream(int requestIndex){
		try {

			if(storedFileBytes[requestIndex].length == 0){

				int index = -1;
				int fileCount;
				long[] fileSizes;
				String[] fileNames;
				byte[] fileBytes = null;
				ZipFile zipfile = new ZipFile(signlink.cacheRoot() + "1.dat");
				DataInputStream datainputstream = new DataInputStream(zipfile.getInputStream(new ZipEntry("1.data")));
				fileCount = datainputstream.readInt();
				datainputstream.readInt();
				fileSizes = new long[fileCount];
				fileNames = new String[fileCount];
				for (int i = 0; i < fileCount; i++){
					fileNames[i] = datainputstream.readUTF();
					fileSizes[i] = datainputstream.readLong();
				}
				for (int i = 0; i < fileNames.length; i++){
					if (fileNames[i].equalsIgnoreCase("index" + "_" + requestIndex))
					{
						index = i;
						break;
					}
				}
				for (int i = 0; i < fileCount; i++){
					fileBytes = new byte[(int)(fileSizes[i])];
					try {
						datainputstream.readFully(fileBytes, 0, fileBytes.length);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (i == index)
					{
						datainputstream.close();
						break;
					}
				}
				storedFileBytes[requestIndex] = fileBytes;
			}




			InputStream is = new ByteArrayInputStream(storedFileBytes[requestIndex]);
			return is;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}*/

}