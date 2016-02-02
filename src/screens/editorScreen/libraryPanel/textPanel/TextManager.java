package src.screens.editorScreen.libraryPanel.textPanel;

import java.awt.Color;

import src.MainApplet;
import src.TextMediaPanel;
import src.Timeline;
import src.TransitionMediaPanel;
import src.renderer.Renderer;
import src.screens.editorScreen.timeline.track.trackItem.TrackTextItem;
import src.screens.editorScreen.timeline.track.transition.Transition;

public class TextManager {

	public static String[] categoryNames = new String[]{"Fades/Dissolves"};
	
	
	public static String[][] transitionNames = new String[][]{{"Text 1", "Text 2"}};
	
	
	public static void fontChange(){
		TrackTextItem selectedText = getSelectedText();
		if(selectedText == null){
			return;
		}
		selectedText.fontSize = (int) MainApplet.getInstance().getjSpinner1().getValue();
		rerender();
	}
	
	public static void textAreaChange(){
		TrackTextItem selectedText = getSelectedText();
		if(selectedText == null){
			return;
		}
		selectedText.text = MainApplet.getInstance().getjTextArea1().getText();
		selectedText.x = MainApplet.getInstance().getjSlider1().getValue();
		selectedText.y = MainApplet.getInstance().getjSlider2().getValue();
		selectedText.colour = MainApplet.getInstance().getjColorChooser1().getColor().getRGB();
		
		selectedText.fontType = MainApplet.getInstance().getjComboBox15().getSelectedIndex();
		selectedText.styleType = MainApplet.getInstance().getjComboBox16().getSelectedIndex();

		
		//System.out.println("size: "+selectedText.fontSize);
		//
		//selectedText.fontName
		
		//updateTransitionInfoPanel();
		rerender();
	}
	
	public static TrackTextItem getSelectedText(){
		if(Timeline.selectedTrackObject instanceof TrackTextItem){
			TrackTextItem item = (TrackTextItem) Timeline.selectedTrackObject;
			return item;
		}
		return null;
	}
	
	public static void rerender(){
		
		TrackTextItem item = getSelectedText();
		
		for(int frameIndex = item.getTrackStartPosition(); frameIndex < item.trackStartPosition+item.mediaDuration+1; frameIndex++){
			if(frameIndex+1 < Renderer.renderingStatus.length){
				Renderer.renderingStatus[frameIndex] = 0;
			}
		}
	}
	
	
	/**
	 * Refreshes the panel with updated data.
	 */
	public static void updateTransitionInfoPanel(){
		
		
		
		if(Timeline.selectedTrackObject instanceof TrackTextItem){
			TrackTextItem textItem = (TrackTextItem) Timeline.selectedTrackObject;
			System.out.println("Updating. : "+MainApplet.getInstance().getjTextField1().getText());
			
			MainApplet.getInstance().getjTextArea1().setText(textItem.text);
			MainApplet.getInstance().getjTextArea1().setEnabled(true);
			
			
			MainApplet.getInstance().getjSlider1().setValue(textItem.x);
			MainApplet.getInstance().getjSlider2().setValue(textItem.y);
			
			MainApplet.getInstance().getjComboBox16().setSelectedIndex(textItem.styleType);
			MainApplet.getInstance().getjComboBox15().setSelectedIndex(textItem.fontType);
			
			MainApplet.getInstance().getjSpinner1().setValue(textItem.fontSize);
			
			MainApplet.getInstance().getjColorChooser1().setColor(new Color(textItem.colour));
			
			
			System.out.println("spinner value: "+(int) MainApplet.getInstance().getjSpinner1().getValue());
			
			//MainApplet.getInstance().getjTextField1().
			
		} else if (TextMediaPanel.selectedTransition != -1 && MainApplet.getInstance().getjList1().getSelectedIndex() != -1) {
			
			//MainApplet.getInstance().getjTextArea1().setEnabled(false);
			//MainApplet.getInstance().getjLabel2().setText(transitionNames[MainApplet.getInstance().getjList1().getSelectedIndex()][TransitionMediaPanel.selectedTransition]);
		}
	}
	
	
}
