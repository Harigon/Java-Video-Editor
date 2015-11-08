package src.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import src.MainApplet;






public class Misc {

	//
	public static JFileChooser fc = new JFileChooser();
	
	
	/**
	 * Formats an integer into a two digit value display, returns as String.
	 * (I.e 5 would become 05, and 16 would remain as 16 because its already two digits.
	 * @param number - The integer that needs converting.
	 * 
	 * Credits: http://www.coderanch.com/t/422920/java/java/Display-integer-always-two-digit
	 */
	public static String formatNumber(int number){
		java.text.DecimalFormat nft = new  
		java.text.DecimalFormat("#00.###");  
		nft.setDecimalSeparatorAlwaysShown(false);  
		return nft.format(number);
	}
	
	public static File loadFromFile(final String description, String[] extensions) throws IOException {
		fc = new JFileChooser("C:/Users/I'm/Desktop/Visual Cross");
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		
		if(description != null && extensions != null){
		
		fc.setFileFilter(new FileTypeFilter(description, extensions));
		}
		int response = fc.showOpenDialog(MainApplet.getInstance());
		if (response == JFileChooser.APPROVE_OPTION) {
			File selected = fc.getSelectedFile();
			return selected;
		}
		return null;
	}
	
	public static File selectFolder() throws IOException {
		fc = new JFileChooser("C:/Users/I'm/Desktop");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int response = fc.showOpenDialog(MainApplet.getInstance());
		if (response == JFileChooser.APPROVE_OPTION) {
			File selected = fc.getSelectedFile();
			return selected;
		}
		return null;
	}
	
	public static File saveToDirectory(final String description, String[] extensions) throws IOException {
		fc = new JFileChooser("./");
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setFileFilter(new FileTypeFilter(description, extensions));
		int response = fc.showSaveDialog(MainApplet.getInstance());
		if (response == JFileChooser.APPROVE_OPTION) {
			File selected = fc.getSelectedFile();
			
		
			if(!selected.getAbsolutePath().endsWith("."+extensions[0])){
				return new File(selected.getAbsolutePath()+"."+extensions[0]);
			}
			
			return selected;
		}
		return null;
	}
	
	public static File saveToDirectory2(final String description, String[] extensions) throws IOException {
		JFileChooser fileChooser = new JFileChooser(".");
	    FileFilter filter1 = new ExtensionFileFilter(description, new String[] { "JPG", "JPEG" });
	    fileChooser.setFileFilter(filter1);
	    int status = fileChooser.showOpenDialog(null);
	    if (status == JFileChooser.APPROVE_OPTION) {
	      File selectedFile = fileChooser.getSelectedFile();
	      System.out.println(selectedFile.getParent());
	      System.out.println(selectedFile.getName());
	      return selectedFile;
	    } else if (status == JFileChooser.CANCEL_OPTION) {
	      System.out.println(JFileChooser.CANCEL_OPTION);
	    }
	    return null;
	}
	
	/*
	 * Credits: http://www.java-tips.org/java-se-tips/java.io/reading-a-file-into-a-byte-array.html
	 */
	public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
	
	/*
	 * http://www.java2s.com/Code/JavaAPI/javax.swing/JFileChoosersetFileFilterFileFilterfilter.htm
	 */
	public static class ExtensionFileFilter extends FileFilter {
		  String description;

		  String extensions[];

		  public ExtensionFileFilter(String description, String extension) {
		    this(description, new String[] { extension });
		  }

		  public ExtensionFileFilter(String description, String extensions[]) {
		    if (description == null) {
		      this.description = extensions[0];
		    } else {
		      this.description = description;
		    }
		    this.extensions = (String[]) extensions.clone();
		    toLower(this.extensions);
		  }

		  private void toLower(String array[]) {
		    for (int i = 0, n = array.length; i < n; i++) {
		      array[i] = array[i].toLowerCase();
		    }
		  }

		  public String getDescription() {
		    return description;
		  }

		  public boolean accept(File file) {
		    if (file.isDirectory()) {
		      return true;
		    } else {
		      String path = file.getAbsolutePath().toLowerCase();
		      for (int i = 0, n = extensions.length; i < n; i++) {
		        String extension = extensions[i];
		        if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
		          return true;
		        }
		      }
		    }
		    return false;
		  }
	
	}
	
	public static class FileTypeFilter extends javax.swing.filechooser.FileFilter {
		String description;

		String extensions[];

		public FileTypeFilter(String description, String extension) {
			this(description, new String[]{extension});
		}

		public FileTypeFilter(String description, String extensions[]) {
			if (description == null) {
				this.description = extensions[0];
			} else {
				this.description = description;
			}
			this.extensions = (String[]) extensions.clone();
			toLower(this.extensions);
		}

		private void toLower(String array[]) {
			for (int i = 0, n = array.length; i < n; i++) {
				array[i] = array[i].toLowerCase();
			}
		}

		public String getDescription() {
			return description;
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				String path = file.getAbsolutePath().toLowerCase();
				for (int i = 0, n = extensions.length; i < n; i++) {
					String extension = extensions[i];
					if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
						return true;
					}
				}
			}
			return false;
		}
	}
	
	
}
