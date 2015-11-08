package src.screens.editorScreen.timeline.track.trackItem.itemEffect.itemVideoEffect.itemVideoEffects;


import src.screens.editorScreen.libraryPanel.chromaKey.ColourInstance;
import src.screens.editorScreen.libraryPanel.chromaKey.ColourProfileInstance;
import src.screens.editorScreen.libraryPanel.chromaKey.TriggerInstance;
import src.screens.editorScreen.libraryPanel.chromaKey.TriggerManager;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.trackItem.itemEffect.itemVideoEffect.ItemVideoEffect;

public class ColourSeperator extends ItemVideoEffect {

	@Override
	public boolean isFullFrameRequired() {
		return true;
	}
	
	
	public int[] pixels;

	@Override
	public int[] renderFrame(int[] pixels, int width, int height, TrackItem trackItem) {
		
		this.pixels = pixels.clone();
		
		colour();
		return this.pixels.clone();
	}
	
	
	
	public void colour(){
		for (int i = 0; i < pixels.length; i++) {

			int red = (pixels[i] >> 16) & 0xFF;
			int green = (pixels[i] >> 8) & 0xFF;
			int blue = pixels[i] & 0xFF;

			double smallestDistance = -1;
			int smallestColour = -1;
			
			boolean filtered = false;
			

			
			
			for(TriggerInstance triggerInstance : TriggerManager.triggers){
				ColourProfileInstance colourProfile = triggerInstance.colourProfile;
				
				
				
				for(ColourInstance allowedColour : colourProfile.allowedColours){
					
					//System.out.println("checking allowed: "+allowedColour.colour+": "+smallestDistance);
					
					int colourRGB = allowedColour.colour;
					int constantRed = (colourRGB >> 16) & 0xFF;
					int constantGreen = (colourRGB >> 8) & 0xFF;
					int constantBlue = colourRGB & 0xFF;
					int dR = red - constantRed, dG = green - constantGreen, dB = blue - constantBlue;	
					double distance = dR * dR + dG * dG + dB * dB;
					long rmean = ( (long)red + (long)constantRed ) / 2;
					long r = (long)red - (long)constantRed;
					long g = (long)green - (long)constantGreen;
					long b = (long)blue - (long)constantBlue;
					
					double distance2 = Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8));
					if(distance2 < smallestDistance || smallestDistance == -1){
						smallestDistance = distance2;
						smallestColour = colourRGB;
						filtered = false;
					}
				}
				
				for(ColourInstance filteredColour : colourProfile.filteredColours){
					//System.out.println("checking filtered: "+filteredColour.colour);
					
					
					int colourRGB = filteredColour.colour;
					int constantRed = (colourRGB >> 16) & 0xFF;
					int constantGreen = (colourRGB >> 8) & 0xFF;
					int constantBlue = colourRGB & 0xFF;
					int dR = red - constantRed, dG = green - constantGreen, dB = blue - constantBlue;	
					double distance = dR * dR + dG * dG + dB * dB;
					long rmean = ( (long)red + (long)constantRed ) / 2;
					long r = (long)red - (long)constantRed;
					long g = (long)green - (long)constantGreen;
					long b = (long)blue - (long)constantBlue;
					
					double distance2 = Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8));
					if(distance2 < smallestDistance || smallestDistance == -1){
						smallestDistance = distance2;
						smallestColour = colourRGB;
						filtered = true;
					}
				}
				
				
				
			}
			
			if(filtered){
				pixels[i] = 0x3D0DFF;
			}
			/*
			if(smallestColour == Colour.fixedColours[Colour.COLOUR_GREEN]){
				pixels[i] = Colour.fixedColours[Colour.COLOUR_RED];
			}
			if(smallestColour == Colour.fixedColours[Colour.COLOUR_BLUE]){
				pixels[i] = Colour.fixedColours[Colour.COLOUR_RED];
				//pixels[i] = smallestColour;
			}*/
		}

	}

	
	
	
	
	
	
}
