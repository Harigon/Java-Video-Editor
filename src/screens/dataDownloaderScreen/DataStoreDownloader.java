package src.screens.dataDownloaderScreen;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URL;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.Enumeration;

import src.MainApplet;
import src.dataStore.DataStore;


public class DataStoreDownloader {



	public static final int BUFFER = 1024;

	/*
	 * Only things you need to change
	 *
	 */
	public static final int VERSION = 1; // Version of cache
	public static String downloadURL = "https://dl.dropbox.com/u/2428061/DataStore.zip"; // Link to cache

	public static String fileToExtract = DataStore.getLocation() + getArchivedName();





	/**]
	 * Downloads the data.
	 * @return Whether the data was downloaded or not.
	 */
	public static boolean downloadRequiredData() {
		
		try {
			MainApplet.instance.getjProgressBar2().setValue(0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			File location = new File(DataStore.getLocation());
			File version = new File(DataStore.getLocation() + "/cacheVersion" + VERSION + ".dat");

			if(!location.exists()) {
				location.mkdir();
				downloadFile(downloadURL, getArchivedName());
				unZip();
				BufferedWriter versionFile = new BufferedWriter(new FileWriter(DataStore.getLocation() + "/cacheVersion" + VERSION + ".dat"));
				versionFile.close();
				return true;
			} else {
				if(!version.exists()) {
					downloadFile(downloadURL, getArchivedName());
					unZip();
					BufferedWriter versionFile = new BufferedWriter(new FileWriter(DataStore.getLocation() + "/cacheVersion" + VERSION + ".dat"));
					versionFile.close();
					return true;
				}
			}
		} catch(Exception e) {

		}
		return false;
	}

	public static void downloadFile(String adress, String localFileName) {
		OutputStream out = null;
		URLConnection conn;
		InputStream in = null;

		try {

			URL url = new URL(adress);
			
			
			
			
			out = new BufferedOutputStream(new FileOutputStream(DataStore.getLocation() + "/" +localFileName)); 

			
			
			conn = url.openConnection();
			in = conn.getInputStream(); 

			byte[] data = new byte[BUFFER]; 

			int numRead;
			long numWritten = 0;
			int length = conn.getContentLength();


			while((numRead = in.read(data)) != -1) {
				out.write(data, 0, numRead);
				numWritten += numRead;
				int percentage = (int)(((double)numWritten / (double)length) * 100D);
				MainApplet.instance.getjProgressBar2().setValue(percentage);
				MainApplet.instance.getjLabel23().setText(percentage+"%");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public static String getArchivedName() {
		int lastSlashIndex = downloadURL.lastIndexOf('/');
		if (lastSlashIndex >= 0 
				&& lastSlashIndex < downloadURL.length() -1) { 
			return downloadURL.substring(lastSlashIndex + 1);
		} else {
			System.err.println("error retreiving archivaed name.");
		}
		return "";
	}



	public static void unZip() {

		try {
			InputStream in = 
				new BufferedInputStream(new FileInputStream(fileToExtract));
			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry e;

			while((e=zin.getNextEntry()) != null) {

				if(e.isDirectory()) {
					(new File(DataStore.getLocation() + e.getName())).mkdir();
				} else {

					if (e.getName().equals(fileToExtract)) {
						unzip(zin, fileToExtract);
						break;
					}
					unzip(zin, DataStore.getLocation() + e.getName());
				}
			}
			zin.close();

		} catch(Exception e) {
		}
	}

	public static void unzip(ZipInputStream zin, String s) 
	throws IOException {

		FileOutputStream out = new FileOutputStream(s);
		byte [] b = new byte[BUFFER];
		int len = 0;

		while ((len = zin.read(b)) != -1) {
			out.write(b,0,len);
		}
		out.close();
	}
}