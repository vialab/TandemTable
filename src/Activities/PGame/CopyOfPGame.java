package Activities.PGame;

import java.awt.Color;

import vialab.simpleMultiTouch.ImageZone;
import vialab.simpleMultiTouch.RectZone;
import vialab.simpleMultiTouch.TextZone;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.events.TapEvent;
import Main.Colours;
import Main.MainSketch;




public class CopyOfPGame {
	MainSketch sketch;
	PGameGetter pGameGetter;
	
	
	String imgTags = "";
	String[] tags;
	//String[] tagsArray;

	int lineX, topicIndex;
	boolean searched = false;
	
	final int NUM_IMAGES = 6;
	final int MAX_IMAGES = 6;
	final int NUM_TAGS = 10;
	final int ROWS = 2;
	final int COLUMNS = 3;

	boolean[] completed = new boolean[NUM_IMAGES];
	boolean[] flag1 = new boolean[NUM_IMAGES];
	boolean[] flag2 = new boolean[NUM_IMAGES];

	RectZone[] box1 = new RectZone[NUM_IMAGES];
	RectZone[] box2 = new RectZone[NUM_IMAGES];

	TextZone[] tagZones1, tagZones2;
	ImageZone[] imgs;
	
	TextZone msgBox1, msgBox2;
	RectZone bottomBox, topBox;


	public CopyOfPGame(TouchClient client, MainSketch sketch, int lineX, int topicIndex, String lang1, String lang2){
		this.sketch = sketch;
		this.lineX = lineX;
		this.topicIndex = topicIndex;

		createWordBoxes();

		for(int i = 0; i < NUM_IMAGES; i++){
			flag1[i] = false;
			flag2[i] = false;
		}

		String[] scrambled = null;
		
		if(lang1.equalsIgnoreCase("English") || lang2.equalsIgnoreCase("English")){	
			scrambled = sketch.scrambleTopic(sketch.topicsExpandedE[topicIndex]);
		} else if(lang1.equalsIgnoreCase("French") || lang2.equalsIgnoreCase("French")){
			scrambled = sketch.scrambleTopic(sketch.topicsExpandedF[topicIndex]);
		}

		tags = new String[NUM_TAGS];
		for(int i = 0; i < scrambled.length; i++){

			tags[i] = scrambled[i];
		}
	
		//tagsArray = new String[ROWS*COLUMNS];

		pGameGetter = new PGameGetter(client, sketch, this, lineX);
		pGameGetter.start();
		
		createLayout();
		createMsgBox1();
		createMsgBox2();



	}
	
	public void createTagZones(){
		int rows = 3;
		int boxHeight = sketch.getHeight()/5;
		float spaceY = (sketch.getHeight()- boxHeight*2)/rows;
		int spacingY =  (int) (0.2*spaceY);
		int spaceX = sketch.getWidth()/100;

		int totalWidth = sketch.getWidth()-(lineX + spaceX);
		int yLineSpace = 5;

		float textSize = sketch.getHeight()/40;
		String regex = "&-&";

		tagZones1 = TextZone.createWordZones(lineX + spaceX, sketch.getHeight()-boxHeight+spacingY, imgTags, Colours.pFont, textSize, totalWidth, regex, yLineSpace, false);
		for(TextZone t: tagZones1){
			t.setDrawBorder(false);
			t.setGestureEnabled("Drag", true);
			sketch.client.addZone(t);
		}


		tagZones2 = TextZone.createWordZones(sketch.getWidth()-(lineX*2), boxHeight-spacingY, imgTags, Colours.pFont, textSize, totalWidth, regex, yLineSpace, true);
		for(TextZone t: tagZones2){
			t.setDrawBorder(false);
			t.rotate((float) Colours.PI);
			t.setGestureEnabled("Drag", true);
			sketch.client.addZone(t);
		}

	}
	
	public void removeTagZones(){
		
		for(int i = 0; i < completed.length; i++){
			completed[i] = false;
		}
		
		for(int i = 0; i < tagZones1.length; i++){
			sketch.client.removeZone(tagZones1[i]);
		}
		
		for(int i = 0; i < tagZones2.length; i++){
			sketch.client.removeZone(tagZones2[i]);
		}
		
	}

	public void createWordBoxes(){
		int rows = 3;
		int boxHeight = sketch.getHeight()/5;
		float spaceY = (sketch.getHeight()- boxHeight*2)/rows;
		int spacingY =  (int) (0.2*spaceY);
		//int spaceX = sketch.getWidth()/100;


		bottomBox = new RectZone(lineX, sketch.getHeight()-boxHeight+spacingY, sketch.getWidth()-lineX , boxHeight-spacingY);
		bottomBox.setColour(new Color(0, 0, 200, 30));
		bottomBox.setDrawBorder(false);
		sketch.client.addZone(bottomBox);

		topBox = new RectZone(lineX, 0, sketch.getWidth()-lineX, boxHeight-spacingY);
		topBox.setColour(new Color(0, 0, 200, 30));
		topBox.setDrawBorder(false);
		sketch.client.addZone(topBox);


	}

	public void createMsgBox1(){
		float width = (sketch.getWidth() - lineX)/3;
		float x = lineX + (sketch.getWidth() - lineX)/2 - width/2;
		float height = sketch.getHeight()/4;
		float y = sketch.getHeight()/2 + height;

		msgBox1 = new TextZone(x, y, width, height, sketch.radius, Colours.pFont, "You Won!", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){
				sketch.client.removeZone(this);
				if(searched){
					pGameGetter.pageOffset = pGameGetter.pageOffset;
					pGameGetter.search();
					
				}
				searched = true;
				e.setHandled(true);
			}
		};
		msgBox1.setColour(Colours.currentColour);
		msgBox1.setDrawBorder(false);
		msgBox1.setActive(false);
		msgBox1.setGestureEnabled("Tap", true);
		sketch.client.addZone(msgBox1);
	}

	public void createMsgBox2(){
		float width = (sketch.getWidth() - lineX)/3;
		float x = lineX + (sketch.getWidth() - lineX)/2 - width/2;
		float height = sketch.getHeight()/4;
		float y = height;

		msgBox2 = new TextZone(x, y, width, height, sketch.radius, Colours.pFont, "You Won!", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){
				sketch.client.removeZone(this);
				if(searched){
					pGameGetter.pageOffset = pGameGetter.pageOffset;
					pGameGetter.search();
					
				}
				searched = true;
				e.setHandled(true);
			}
		};
		msgBox2.setColour(Colours.currentColour);
		msgBox2.setDrawBorder(false);
		msgBox2.setActive(false);
		msgBox2.rotate((float) Colours.PI);
		msgBox2.setGestureEnabled("Tap", true);
		sketch.client.addZone(msgBox2);
	}

	public void createLayout(){

		int boxHeight = (int) (sketch.getHeight()/4.4);
		float totalWidth = sketch.getWidth()-lineX;

		float spaceX = totalWidth/COLUMNS;
		float spaceY = (sketch.getHeight()- boxHeight*2)/ROWS;

		int spacingX =  (int) (0.2*spaceX);
		int spacingY =  (int) (0.2*spaceY);

		int myX = lineX + spacingX/2;
		int myY = boxHeight;
		int myWidth = (int) (0.8 * spaceX);
		int myHeight = (int) (0.8 * spaceY);
		imgs = new ImageZone[ROWS*COLUMNS];

		for(int j = 0; j < ROWS; ++j){
			for(int i = 0; i < COLUMNS; ++i){
				if(!pGameGetter.failed){
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

					box1[index] = new RectZone(myX + myWidth/4, (float) (myY + myHeight +(myHeight * 0.2 - myHeight/6)), myWidth/2, myHeight/6){
						public void drawZone(){
							super.drawZone();


							if(tagZones1 != null){
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
											removeTagZones();
											msgBox1.setActive(true);
											msgBox2.setActive(true);
										}

										imgs[index].setShadow(true);
										flag1[index] = true;
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

					box2[index] = new RectZone(myX + myWidth/4, (float) (myY - (myHeight * 0.2)), myWidth/2, myHeight/6){
						public void drawZone(){
							super.drawZone();

							if(tagZones2 != null){
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
											removeTagZones();
											msgBox1.setActive(true);
											msgBox2.setActive(true);
										}
										
										imgs[index].setShadow(true);
										flag2[index] = true;
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
				}
			}
			myX = lineX + spacingX/2;
			myY = myY + myHeight + spacingY*2;
		}
	}

	public void removeZones(){
		sketch.client.removeZone(bottomBox);
		sketch.client.removeZone(topBox);

		if(!pGameGetter.failed){
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

		}
	}		
}




