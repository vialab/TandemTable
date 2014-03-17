package TandemTable.sections.activities.pictures;


import org.jdesktop.animation.timing.Animator;

import TandemTable.Colours;
import TandemTable.Languages;
import TandemTable.Sketch;

import com.aetrion.flickr.photos.PhotoList;

import processing.core.PImage;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.zones.GarbageZone;
import vialab.simpleMultiTouch.zones.ImageZone;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.Zone;

public class PictureActivity {
	Sketch sketch;
	PictureActivity pAct = this;
	String lang1, lang2, tagsStr;
	String[] tags, topic;
	PhotoList list;
	PictureGetter pg;
	int spaceX, topicIndex;

	final int NUM_IMAGES = 4;
	final int MAX_IMAGES = 120;
	final int NUM_TAGS = 10;

	boolean morePicsSelect1 = false;
	boolean morePicsSelect2 = false;
	boolean canceled = false;
	boolean failed = false;

	int currentPicsIndex = 0, pageOffset = 1;
	int gbw;

	Animator[] animMorePics = new Animator[2];

	ImageZone[] imgs;
	//Zone morePics1, morePics2;
	GarbageZone gbZone1, gbZone2;
	RectZone loading;
	int imgSize;
	
	// Name of the garbage bin picture
	String gbPic = "gb-blue.jpg";

	public PictureActivity(Sketch sketch, int topicIndex, String lang1, String lang2) {
		this.sketch = sketch;
		imgSize = sketch.getWidth()/5;
		spaceX = sketch.getWidth() - sketch.lineX;
		this.topicIndex = topicIndex;
		this.lang1 = lang1;
		this.lang2 = lang2;
		gbw = sketch.getWidth()/10;
		imgs = new ImageZone[NUM_IMAGES];
		
		String[] scrambled = null;
		topic = new String[1];
		
		if(lang1.equalsIgnoreCase("English") || lang2.equalsIgnoreCase("English")){	
			scrambled = sketch.scrambleStrings(Languages.topicsExpandedE[topicIndex]);
			topic[0] = Languages.topicsE[topicIndex]; 
		} else if(lang1.equalsIgnoreCase("French") || lang2.equalsIgnoreCase("French")){
			scrambled = sketch.scrambleStrings(Languages.topicsExpandedF[topicIndex]);
			topic[0] = Languages.topicsF[topicIndex]; 
		} else if(lang1.equalsIgnoreCase("Portuguese") || lang2.equalsIgnoreCase("Portuguese")){
			scrambled = sketch.scrambleStrings(Languages.topicsExpandedP[topicIndex]);
			topic[0] = Languages.topicsP[topicIndex]; 
		} else if(lang1.equalsIgnoreCase("Spanish") || lang2.equalsIgnoreCase("Spanish")){
			scrambled = sketch.scrambleStrings(Languages.topicsExpandedS[topicIndex]);
			topic[0] = Languages.topicsS[topicIndex]; 
		}

		tags = new String[NUM_TAGS];
		for(int i = 0; i < scrambled.length; i++){

			tags[i] = scrambled[i];
		}
		//createMorePicturesButtons();
		createGBZone();
		createImgZones();
		pg = new PictureGetter(sketch, this);
		pg.start();
	}

	public void createGBZone(){
		PImage img = sketch.loadImage(gbPic);

		gbZone1 = new GarbageZone(sketch.client, sketch.getWidth()-gbw, sketch.getHeight()-gbw, gbw, gbw, img){
			public void gbTrigger(Zone z){
				z.setActive(false);
				z.reset();
				TouchClient.picker.removeMapping(z);
				
				if(!canceled){
					PImage img = null;
					while(img == null) {
						img = pg.loadImage();
					
						if(img != null){
							setImgZone((ImageZone)z, img);
						}
					}
				}
			}
		};
		//gbZone1.setBorderWeight(20);
		gbZone1.setDrawBorder(false);
		sketch.client.addZone(gbZone1);

		gbZone2 = new GarbageZone(sketch.client, sketch.getWidth()-gbw, 0, gbw, gbw, img){
			public void gbTrigger(Zone z){
				z.setActive(false);
				z.reset();
				TouchClient.picker.removeMapping(z);
				
				if(!canceled){
					PImage img = null;
					while(img == null) {
						img = pg.loadImage();
					
						if(img != null){
							setImgZone((ImageZone)z, img);
						}
					}
				}
			}
		};
		//gbZone2.setBorderWeight(20);
		gbZone2.setDrawBorder(false);
		gbZone2.rotate((float) Colours.PI);
		sketch.client.addZone(gbZone2);
	}
	
	public void createImgZones(){
		for(int i = 0; i < NUM_IMAGES; i++){
			int[] pos = findRandXY(imgSize);
			PImage pImage = null;
			imgs[i] = new ImageZone(pImage, pos[0], pos[1], imgSize, imgSize);
			imgs[i].setActive(false);
			sketch.client.addZone(imgs[i]);

		}

	}
	
	public void setImgZone(ImageZone z, PImage pImage){
		TouchClient.picker.removeMapping(z);
		z.resetMatrix();
		z.setImage(pImage);
		int[] pos = findRandXY(imgSize);
		z.setXY(pos[0], pos[1]);
		float rot = (float) (0 + Math.random( ) * (2*Colours.PI + 1));
		z.rotate(rot);
		z.setGestureEnabled("Drag", true);
		//z.setGestureEnabled("Pinch", true);
		z.setGestureEnabled("Rotate", true);

		//z.setImgRotated(true);
		//z.setImgRotateAmount((float) Colours.PI);
		
		z.setActive(true);
	}

	public synchronized void removeImgs(){
		sketch.client.removeZone(loading);
		
		for(int i = 0; i < imgs.length; i++){
			if(imgs[i] != null){
				sketch.client.removeZone(imgs[i]);
			}
		}
	}

	//public void removeMPButtons(){
	//	sketch.client.removeZone(morePics1);
	//	sketch.client.removeZone(morePics2);
	//}

	public synchronized void removeZones(){
		canceled = true;
		removeImgs();
		sketch.client.removeZone(gbZone1);
		sketch.client.removeZone(gbZone2);
		//removeMPButtons();
	}
	
	private int[] findRandXY(int imgSize){
		int x = 0, y = 0;
		do {
			x = (int) (sketch.lineX+imgSize/10 + Math.random() * (sketch.getWidth()-imgSize - sketch.lineX-imgSize/10 + 1));
			y = (int) (imgSize/10 + Math.random() * (sketch.getHeight()-imgSize - imgSize/10 + 1));
		} while(x+imgSize >= sketch.getWidth() - gbw && (y <= gbw) || y+imgSize  >= sketch.getHeight()-gbw);
		//System.out.println(x + " " + y + " " + sketch.getWidth() + " " + picAct.gbw);
		return new int[] {x, y};
	}


	/*public void createMorePicturesButtons(){
		String s = "";

		//More Pictures Button for user 1
		if(lang1.equalsIgnoreCase("English")){
			s = English.morePics;
		} else if(lang1.equalsIgnoreCase("French")){
			s = French.morePics;
		}

		morePics1 = new TextZone(layoutManager.buttonX, layoutManager.buttonYb2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, s, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (tappable){

					if(!morePicsSelect1 && !morePicsSelect2){
						animMorePics[1].start();
						this.setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
						morePicsSelect1 = true;
					} else if (morePicsSelect1 && !morePicsSelect2){
						animMorePics[1].stop();
						((TextZone) morePics2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
						((TextZone) morePics1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
						morePicsSelect1 = false;
					} else if (morePicsSelect2){
						animMorePics[0].stop();
						animMorePics[1].stop();
						((TextZone) morePics1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
						((TextZone) morePics2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
						morePicsSelect2 = false;
						morePicsSelect1 = false;
						pg.morePics();
					}

					e.setHandled(tappableHandled);

				}
			}
		};

		((TextZone) morePics1).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		((TextZone) morePics1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		morePics1.setGestureEnabled("TAP", true, true);
		morePics1.setDrawBorder(false);
		sketch.client.addZone(morePics1);

		animMorePics[0] = PropertySetter.createAnimator(layoutManager.animationTime, morePics1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animMorePics[0].setRepeatBehavior(RepeatBehavior.REVERSE);
		animMorePics[0].setRepeatCount(Animator.INFINITE);

		//More Pictures Button for user 2
		if(lang2.equalsIgnoreCase("English")){
			s = English.morePics;
		} else if(lang2.equalsIgnoreCase("French")){
			s = French.morePics;
		}

		morePics2 = new TextZone(layoutManager.buttonX, layoutManager.buttonYt2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, s, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (tappable){

					if(!morePicsSelect1 && !morePicsSelect2){
						animMorePics[0].start();
						this.setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
						morePicsSelect2 = true;
					} else if (morePicsSelect2 && !morePicsSelect1){
						animMorePics[0].stop();
						((TextZone) morePics2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
						((TextZone) morePics1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
						morePicsSelect2 = false;
					} else if (morePicsSelect1){
						animMorePics[0].stop();
						animMorePics[1].stop();
						((TextZone) morePics1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
						((TextZone) morePics2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
						morePicsSelect2 = false;
						morePicsSelect1 = false;
						pg.morePics();

					}

					e.setHandled(tappableHandled);

				}
			}
		};

		morePics2.rotate((float) (Colours.PI));
		((TextZone) morePics2).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		((TextZone) morePics2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		morePics2.setGestureEnabled("TAP", true, true);
		morePics2.setDrawBorder(false);
		sketch.client.addZone(morePics2);

		animMorePics[1] = PropertySetter.createAnimator(layoutManager.animationTime, morePics2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animMorePics[1].setRepeatBehavior(RepeatBehavior.REVERSE);
		animMorePics[1].setRepeatCount(Animator.INFINITE);
	}*/
}
