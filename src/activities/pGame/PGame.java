package activities.pGame;

import java.awt.Color;

import main.Colours;
import main.Languages;
import main.MainSketch;


import processing.core.PConstants;
import processing.core.PImage;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.ImageZone;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.TextZone;

public class PGame {
	MainSketch sketch;
	
	int topicIndex, offset, level = 0;;
	
	final int NUM_IMAGES = 6;
	final int MAX_IMAGES = 6;
	final int NUM_TAGS = 10;
	final int ROWS = 2;
	final int COLUMNS = 3;
	
	PImage[] pImg = new PImage[NUM_IMAGES];
	
	String[] imgTags1 = new String[NUM_IMAGES];
	String[] imgTags2 = new String[NUM_IMAGES];
	
	boolean[] completed;
	boolean msgBoxFlag = false;
	boolean[] flag1;
	boolean[] flag2;
	
	boolean nextLevel = false, started = false;
	//Animator animMsgBox;

	RectZone[] box1 = new RectZone[NUM_IMAGES];
	RectZone[] box2 = new RectZone[NUM_IMAGES];

	TextZone[] tagZones1 = new TextZone[NUM_IMAGES]; 
	TextZone[] tagZones2 = new TextZone[NUM_IMAGES];
	
	ImageZone[] imgs;
	TextZone license;
	RectZone bottomBox, topBox, msgBox;
	
	String lang1, lang2;//, play1, play2;
	
	String[] tagArray1 = null;
	String[] tagArray2 = null;
	String[] tags;
	
	public PGame(MainSketch sketch, int topicIndex, String lang1, String lang2){
		this.sketch = sketch;
		this.topicIndex = topicIndex;
		this.lang1 = lang1;
		this.lang2 = lang2;
		
		
		
		
		createBoxes();

		//for(int i = 0; i < NUM_IMAGES; i++){
		//	flag1[i] = false;
		//	flag2[i] = false;
		//}

		String[] scrambled = null;
		//String topic = null;
		
		if(lang1.equalsIgnoreCase("English") || lang2.equalsIgnoreCase("English")){	
			scrambled = sketch.scrambleStrings(Languages.topicsExpandedE[topicIndex]);
			//topic = sketch.topicsE[topicIndex];
		} else if(lang1.equalsIgnoreCase("French") || lang2.equalsIgnoreCase("French")){
			scrambled = sketch.scrambleStrings(Languages.topicsExpandedF[topicIndex]);
			//topic = sketch.topicsF[topicIndex];

		}
		

		if(lang1.equalsIgnoreCase("English")){	
			tagArray1 = Languages.tagsE[topicIndex];
			//play1 = sketch.playAgainE;
		} else if(lang1.equalsIgnoreCase("French")){
			tagArray1 = Languages.tagsF[topicIndex];
			//play1 = sketch.playAgainF;

		}
		
		if(lang2.equalsIgnoreCase("English")){	
			tagArray2 = Languages.tagsE[topicIndex];
			//play2 = sketch.playAgainE;
		} else if(lang2.equalsIgnoreCase("French")){
			tagArray2 = Languages.tagsF[topicIndex];
			//play2 = sketch.playAgainF;

		}
		
		

		tags = new String[NUM_TAGS];
		for(int i = 0; i < scrambled.length; i++){

			tags[i] = scrambled[i];
		}
		
		
		createLayout();
		createMsgBox();
		loadImages(0);
		String licenseText = "Images: FreeDigitalPhotos.net";
		
		float height = sketch.textSize/2;
		sketch.textFont(Colours.pFont, height);
		float tWidth = sketch.textWidth(licenseText);

		license = new TextZone(sketch.getWidth()-tWidth, sketch.getHeight()-height, tWidth, height, Colours.pFont, licenseText, height, "LEFT", "BOTTOM");
		license.setDrawBorder(false);
		sketch.client.addZone(license);
	}
	
	public void createTagZones(){
		
		if(started){
			for(int i = 0; i < tagZones1.length; i++){
				sketch.client.removeZone(tagZones1[i]);
			}
			
			for(int i = 0; i < tagZones2.length; i++){
				sketch.client.removeZone(tagZones2[i]);
			}
		}
		int boxHeight = sketch.getHeight()/5;
		float textSize = sketch.getHeight()/40;
		completed = new boolean[NUM_IMAGES];
		flag1 = new boolean[NUM_IMAGES];
		flag2 = new boolean[NUM_IMAGES];
		
		float xOff = 0;
		String[] numbers = {"0", "2", "4", "5", "1", "3"};
		numbers = sketch.scrambleStrings(numbers);
		
		sketch.textFont(Colours.pFont, textSize);
		sketch.textAlign(PConstants.LEFT, PConstants.BOTTOM);
		float spaceWidth = sketch.textWidth(" ");
		float dist = (sketch.getWidth()-sketch.lineX)/12;
		float x = sketch.lineX +dist/2;
		float y = sketch.getHeight()-boxHeight/2;
		float threshXY = sketch.getHeight()/40;

		for(int j = 0; j < numbers.length; j++){
			int tag = Integer.parseInt(numbers[j]);
			String c = imgTags1[tag].trim();
			float textWidth = sketch.textWidth(c);
			float zoneWidth = (textWidth + spaceWidth);


			tagZones1[tag] = new TextZone(x + xOff, y, zoneWidth, textSize, Colours.pFont, c, textSize, "LEFT", "BOTTOM");
			tagZones1[tag].setDrawBorder(false);
			tagZones1[tag].setGestureEnabled("Drag", true);
			tagZones1[tag].setThresholdTouch(threshXY, threshXY);
			sketch.client.addZone(tagZones1[tag]);
			xOff += dist*2;
		}
		
		x = sketch.getWidth()- dist - dist/2;
		y = boxHeight/2 - textSize;
		xOff = 0;
		
		for(int j = 0; j < numbers.length; j++){
			int tag = Integer.parseInt(numbers[j]);
			String c = imgTags2[tag].trim();
			float textWidth = sketch.textWidth(c);
			float zoneWidth = (textWidth + spaceWidth);

			tagZones2[tag] = new TextZone(x + xOff, y, zoneWidth, textSize, Colours.pFont, c, textSize, "LEFT", "BOTTOM");
			tagZones2[tag].setDrawBorder(false);
			tagZones2[tag].rotate((float) Colours.PI);
			tagZones2[tag].setGestureEnabled("Drag", true);
			tagZones2[tag].setThresholdTouch(threshXY, threshXY);

			sketch.client.addZone(tagZones2[tag]);
			xOff -= dist*2;
		}

	started = true;

	}
	
	public void inactivateTagZones(){
		
		//for(int i = 0; i < completed.length; i++){
		//	completed[i] = false;
		//}
		for(int i = 0; i < NUM_IMAGES; i++){
			tagZones1[i].setActive(false);
			tagZones2[i].setActive(false);
		}
		
		
		
	}

	public void createBoxes(){
		int rows = 3;
		int boxHeight = sketch.getHeight()/5;
		float spaceY = (sketch.getHeight()- boxHeight*2)/rows;
		int spacingY =  (int) (0.2*spaceY);
		//int spaceX = sketch.getWidth()/100;


		bottomBox = new RectZone(sketch.lineX, sketch.getHeight()-boxHeight+spacingY, sketch.getWidth()-sketch.lineX , boxHeight-spacingY);
		bottomBox.setColour(new Color(0, 0, 200, 30));
		bottomBox.setDrawBorder(false);
		sketch.client.addZone(bottomBox);

		topBox = new RectZone(sketch.lineX, 0, sketch.getWidth()-sketch.lineX, boxHeight-spacingY);
		topBox.setColour(new Color(0, 0, 200, 30));
		topBox.setDrawBorder(false);
		sketch.client.addZone(topBox);


	}

	public void createMsgBox(){
		/*float width = (sketch.getWidth() - sketch.lineX)/3;
		float x = sketch.lineX + (sketch.getWidth() - sketch.lineX)/2 - width/2;
		float height = sketch.getHeight()/4;
		float y = sketch.getHeight()/2 + height;*/

		msgBox = new RectZone(sketch.lineX, 0, sketch.getWidth()-sketch.lineX, sketch.getHeight(), sketch.radius){
			public void tapEvent(TapEvent e){
				//if(nextLevel){
					nextLevel = false;
					this.setActive(false);
					//animMsgBox.stop();
					if(level < 2){
						level++;
					} else {
						level = 0;
					}
					
					for(TextZone z: tagZones1){
						sketch.client.removeZone(z);
					}

					for(TextZone z: tagZones2){
						sketch.client.removeZone(z);
					}
					
					loadImages(NUM_IMAGES*level);
				//} else {
				//	animMsgBox.start();
				//	nextLevel = true;
				//}
				e.setHandled(true);
			}
			
			public void drawZone(){
				super.drawZone();
				sketch.fill(0);
				sketch.textFont(Colours.pFont, sketch.textSize);
				float textWidth = sketch.textWidth(sketch.learner1.playAgain);

				sketch.text(sketch.learner1.playAgain, (sketch.getWidth()-sketch.lineX)/2 + sketch.lineX, sketch.getHeight()/2 + sketch.textSize*4);
				
				textWidth = sketch.textWidth(sketch.learner2.playAgain);
				sketch.pushMatrix();
				sketch.translate(sketch.getWidth()-sketch.lineX - textWidth,  sketch.getHeight()/2 - sketch.textSize*4);
				sketch.rotate((float) Colours.PI);
				sketch.text(sketch.learner2.playAgain, 0, 0);
				sketch.popMatrix();
			}
		};
		msgBox.setColour(Colours.currentColour);
		msgBox.setDrawBorder(false);
		msgBox.setActive(false);
		msgBox.setGestureEnabled("Tap", true);
		sketch.client.addZone(msgBox);
		
		//animMsgBox = PropertySetter.createAnimator(sketch.layout.animationTime, msgBox, 
		//		"colour", new ColourEval(), Colours.currentColour, Colours.selectedZone);

		//animMsgBox.setRepeatBehavior(RepeatBehavior.REVERSE);
		//animMsgBox.setRepeatCount(Animator.INFINITE);
	}


	public void createLayout(){

		int boxHeight = (int) (sketch.getHeight()/4.4);
		float totalWidth = sketch.getWidth()-sketch.lineX;

		float spaceX = totalWidth/COLUMNS;
		float spaceY = (sketch.getHeight()- boxHeight*2)/ROWS;

		int spacingX =  (int) (0.2*spaceX);
		int spacingY =  (int) (0.2*spaceY);

		int myX = sketch.lineX + spacingX/2;
		int myY = boxHeight;
		int myWidth = (int) (0.8 * spaceX);
		int myHeight = (int) (0.8 * spaceY);
		imgs = new ImageZone[ROWS*COLUMNS];

		for(int j = 0; j < ROWS; ++j){
			for(int i = 0; i < COLUMNS; ++i){
				//if(!Getter.failed){
					final int index = j*COLUMNS + i;

					imgs[index] = new ImageZone(myX, myY, myWidth, myHeight){
						public void tapEvent(TapEvent e){
							this.rotate((float) Colours.PI);
							e.setHandled(true);
						}


					};
					imgs[index].setColour(0, 0, 0);
					if(index < 3){//5 || (index < 10 && index % 2 == 0)){
						imgs[index].rotate((float) Colours.PI);
					}
					imgs[index].setGestureEnabled("Tap", true);

					int size = sketch.getWidth()/100;
					imgs[index].setShadowX(-size);
					imgs[index].setShadowY(-size);
					imgs[index].setShadowW(size*2);
					imgs[index].setShadowH(size*2);
					imgs[index].setDrawOnlyImage(false);
					imgs[index].setShadow(false);


					sketch.client.addZone(imgs[index]);

					box1[index] = new RectZone(myX, (float) (myY + myHeight +(myHeight * 0.2 - myHeight/6)), myWidth, myHeight/6){
						public void drawZone(){
							super.drawZone();


							if(started){
								TextZone t = tagZones1[index];
								if(this.contains(t.getXTimesMatrix() + t.getWidth()/2, t.getYTimesMatrix() + t.getHeight()/2) && !flag1[index]){
									if(t.getNumIds() == 0){
										if(!flag2[index]){
											imgs[index].setShadowColour(Color.ORANGE);
											completed[index] = false;

										} else {
											imgs[index].setShadowColour(Color.GREEN);
											completed[index] = true;
										}
										
										boolean notDone = false;
										for(boolean flag: completed){
											if(!flag){
												notDone = true;
											}
										}
										
										if(!notDone){
											
											if(!msgBoxFlag){
												inactivateTagZones();
												msgBox.setActive(true);
												msgBoxFlag = true;
											}
										} else {

											imgs[index].setShadow(true);
											flag1[index] = true;
										}
										t.setGestureEnabled("Drag", false);

										t.resetMatrix();
										t.setChanged(true);
										t.setX(box1[index].getX() + box1[index].getWidth()/2 - t.getWidth()/2);
										t.setY(box1[index].getY() + box1[index].getHeight()/2 - t.getHeight()/2);
									}
								} 
							}

						}
					};
					box1[index].setBorderColour(0);
					box1[index].setBorderWeight(5);
					box1[index].setColour(255, 255, 255);
					sketch.client.addZone(box1[index]);

					box2[index] = new RectZone(myX, (float) (myY - (myHeight * 0.2)), myWidth, myHeight/6){
						public void drawZone(){
							super.drawZone();

							if(started){
								TextZone t = tagZones2[index];
								if(this.contains(t.getXTimesMatrix() - t.getWidth()/2, t.getYTimesMatrix() - t.getHeight()/2) && !flag2[index]){
									if(t.getNumIds() == 0){
										if(!flag1[index]){
											imgs[index].setShadowColour(Color.ORANGE);
											completed[index] = false;

										} else {
											imgs[index].setShadowColour(Color.GREEN);
											completed[index] = true;
										}
										
										boolean notDone = false;
										for(boolean flag: completed){
											if(!flag){
												notDone = true;
											}
										}
										
										if(!notDone){
											
											if(!msgBoxFlag){
												inactivateTagZones();
												msgBox.setActive(true);
												msgBoxFlag = true;
											}
										} else {
										
											imgs[index].setShadow(true);
											flag2[index] = true;
										}
										t.setGestureEnabled("Drag", false);
										
										

										t.resetMatrix();
										t.setChanged(true);
										t.setX(box2[index].getX() + box2[index].getWidth()/2 - t.getWidth()/2);
										t.setY(box2[index].getY() + box2[index].getHeight()/2 - t.getHeight()/2);
										t.rotate((float) Colours.PI);

									}
								}
							}
						}
					};
					box2[index].setBorderColour(0);
					box2[index].setBorderWeight(5);
					box2[index].setColour(255, 255, 255);
					sketch.client.addZone(box2[index]);
					myX += myWidth + spacingX;
				//}
			}
			myX = sketch.lineX + spacingX/2;
			myY = myY + myHeight + spacingY*2;
		}
	}
	
	public void loadImages(int offset){
		this.offset = offset;
		getImages();
		createTagZones();
		msgBoxFlag = false;
		for(ImageZone iZone: imgs){
			iZone.setShadow(false);
		}
		
	}

	public void getImages(){
		
		
		
		
		for(int j = 0; j < ROWS; ++j){
			for(int i = 0; i < COLUMNS; ++i){
					int index = j*COLUMNS + i;
					int indexOffset = index + offset;

					imgTags1[index] = tagArray1[indexOffset];
					imgTags2[index] = tagArray2[indexOffset];
					
					String topic = Languages.topicsE[topicIndex].toLowerCase();
					
					pImg[index] = sketch.loadImage("./topic_pics/" + topic + "/" + indexOffset + ".jpg");
					//System.out.println("./topic_pics/" + topic + "/" + index+offset + ".jpg");
					imgs[index].setImage(pImg[index]);
		
				
			}
		}
	}

	public void removeZones(){
		started = false;
		sketch.client.removeZone(bottomBox);
		sketch.client.removeZone(topBox);
		sketch.client.removeZone(license);
		sketch.client.removeZone(msgBox);


		//if(!Getter.failed){
			for(ImageZone z: imgs){
				sketch.client.removeZone(z);
			}

			for(TextZone z: tagZones1){
				sketch.client.removeZone(z);
			}

			for(TextZone z: tagZones2){
				sketch.client.removeZone(z);
			}

			for(RectZone z: box1){
				sketch.client.removeZone(z);
			}

			for(RectZone z: box2){
				sketch.client.removeZone(z);
			}

		//}
	}		
}




