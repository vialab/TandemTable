package Main.UserRegistration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import Main.Colours;
import Main.MainSketch;


import processing.core.PApplet;
import processing.core.PImage;
import vialab.simpleMultiTouch.ImageZone;
import vialab.simpleMultiTouch.RectZone;
import vialab.simpleMultiTouch.TextZone;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.Zone;
import vialab.simpleMultiTouch.events.HSwipeEvent;
import vialab.simpleMultiTouch.events.TapEvent;

public class UserProfilePicker {

	PApplet applet;
	TouchClient client;
	MainSketch sketch;
	UserRegistration userRegistration;

	Zone[] arrows, userImg;
	Zone swipe1, swipe2, newLang1, lastLang1, newLang2, lastLang2;
	PImage[] images;
	FileReader reader;
	BufferedReader bReader;

	int numProfiles, minSize;
	int index1 = 0;
	int index2 = 0;
	int chosenProfile1 = -1;
	int chosenProfile2 = -1;

	public UserProfilePicker(TouchClient client, MainSketch sketch, UserRegistration userRegistration){
		applet = client.getParent();
		this.sketch = sketch;
		this.client = client;
		this.userRegistration = userRegistration;

		arrows = new Zone[4];
		userImg = new Zone[14];
		minSize = applet.screenWidth/15;


		loadImages();
		//create images starting at index 0
		createImages();
		loadArrows();

	}

	public void loadArrows(){

		int arrowSize = applet.screenHeight/10;
		int leftSideX = applet.getX();
		int rightSizeX = applet.screenWidth-arrowSize;
		int arrowBY = applet.getY() + applet.screenHeight/2 + arrowSize/2;
		int arrowTY = applet.getY() + applet.screenHeight/2 -arrowSize - arrowSize/2;

		//For User1
		arrows[0] = new ImageZone(applet.loadImage("lArrow.png"), leftSideX, arrowBY, arrowSize, arrowSize){
			public void tapEvent(TapEvent e){
				if (tappable){
					if (index1 > 0){
						index1--;
					} else {
						index1 = numProfiles;
					}

					//load images starting at index
					loadImages(1, index1);
					e.setHandled(tappableHandled);

				}
			}
		};

		arrows[1] = new ImageZone(applet.loadImage("rArrow.png"), rightSizeX, arrowBY, arrowSize, arrowSize){
			public void tapEvent(TapEvent e){

				if (tappable){
					if(index1 == numProfiles){
						index1 = 0;
					} else {
						index1++;
					}
					//load images starting at index
					loadImages(1, index1);
					e.setHandled(tappableHandled);
				}
			}
		};
		arrows[0].setGestureEnabled("Tap", true, true);
		arrows[1].setGestureEnabled("Tap", true, true);

		arrows[0].setDrawBorder(false);
		arrows[1].setDrawBorder(false);
		client.addZone(arrows[0]);
		client.addZone(arrows[1]);

		//For User2
		arrows[2] = new ImageZone(applet.loadImage("rArrow.png"), rightSizeX, arrowTY, arrowSize, arrowSize){
			public void tapEvent(TapEvent e){
				if (tappable){
					if (index2 > 0){
						index2--;
					} else {
						index2 = numProfiles;
					}

					//load images starting at index
					loadImages(2, index2);
					e.setHandled(tappableHandled);

				}
			}
		};

		arrows[3] = new ImageZone(applet.loadImage("lArrow.png"), leftSideX, arrowTY, arrowSize, arrowSize){
			public void tapEvent(TapEvent e){

				if (tappable){
					if(index2 == numProfiles){
						index2 = 0;
					} else {
						index2++;
					}
					//load images starting at index
					loadImages(2, index2);
					e.setHandled(tappableHandled);
				}
			}
		};
		arrows[2].setGestureEnabled("Tap", true, true);
		arrows[3].setGestureEnabled("Tap", true, true);

		arrows[2].setDrawBorder(false);
		arrows[3].setDrawBorder(false);
		client.addZone(arrows[2]);
		client.addZone(arrows[3]);


	}

	public void loadImages(){
		numProfiles = new File(".\\data\\users\\images").list().length - 2;
		images = new PImage[numProfiles+1];

		for(int i = 0; i <= numProfiles; i++){
			images[i] = applet.loadImage("\\users\\images\\user" + (i) + ".png");
		}
	}

	public void createImages(){
		int loadIndex = 0;
		int x = 0;

		int y = applet.screenHeight/2 + sketch.yOffset;
		int width = 0;
		int height = 0;

		swipe1 = new RectZone(0, applet.getHeight()/2, applet.getWidth(), minSize*2 + sketch.yOffset){
			public void hSwipeEvent(HSwipeEvent e){

				if (index1 > 0 && index1 <= numProfiles && e.getSwipeType() == 1){
					index1--;
				} else if(index1 < numProfiles && e.getSwipeType() == -1){
					index1++;
				} else if (index1 == 0) {
					index1 = numProfiles;
				} else if (index1 == numProfiles) {
					index1 = 0;
				}

				//load images starting at index
				loadImages(1, index1);
				e.setHandled(hSwipeableHandled);
			}

		};
		swipe1.setGestureEnabled("HSwipe", true, true);
		swipe1.setDrawBorder(false);
		swipe1.setHSwipeDist(sketch.qSwipeThreshold);
		client.addZone(swipe1);

		swipe2 = new RectZone(0, applet.getHeight()/2-minSize*2 - sketch.yOffset, applet.getWidth(), minSize*2 + sketch.yOffset){
			public void hSwipeEvent(HSwipeEvent e){

				if (index2 > 0 && index2 <= numProfiles && e.getSwipeType() == -1){
					index2--;
				} else if(index2 < numProfiles && e.getSwipeType() == 1){
					index2++;
				} else if (index2 == 0) {
					index2 = numProfiles;
				} else if (index2 == numProfiles) {
					index2 = 0;
				}

				//load images starting at index
				loadImages(2, index2);
				e.setHandled(hSwipeableHandled);
			}

		};
		swipe2.setGestureEnabled("HSwipe", true, true);
		swipe2.setDrawBorder(false);
		swipe2.setHSwipeDist(sketch.qSwipeThreshold);
		client.addZone(swipe2);


		//For user1
		for(int i = 0; i < 7; i++){



			if (i== 0){
				x = (i+1)*applet.screenWidth/10;
				width = minSize;
				height = minSize;
			} else if (i == 1){
				x = (i+1)*applet.screenWidth/10;
				width = (int)(minSize*1.2);
				height = (int)(minSize*1.2);
			} else if (i == 2){
				x = (i+1)*applet.screenWidth/10 + minSize/5;
				width = (int)(minSize*1.5);
				height = (int)(minSize*1.5);
			} else if (i == 3){
				x = (i+1)*applet.screenWidth/10 + (int)(minSize/2);
				width = minSize*2;
				height = minSize*2;
			} else if (i == 4){
				//x = (int)(applet.screenWidth - 3*applet.screenWidth/12 - minSize*3);
				width = (int)(minSize*1.5);
				height = (int)(minSize*1.5);
				x = applet.getWidth() - (i+1)*applet.screenWidth/10 - minSize/5+width;
			} else if (i == 5){
				//x = (int)(applet.screenWidth - 3*applet.screenWidth/12 - minSize*1.2);
				width = (int)(minSize*1.2);
				height = (int)(minSize*1.2);
				x = applet.getWidth() - 2*applet.getWidth()/10 - width;
			} else if (i == 6){
				x = applet.screenWidth - 2*applet.screenWidth/12;
				width = minSize;
				height = minSize;
			}		

			if (i == 3){
				//the index of the picture to load
				userImg[i] = new ImageZone(images[loadIndex], x, y, width, height){
					public void tapEvent(TapEvent e){
						if (tappable && index1 != chosenProfile2){
							chosenProfile1 = index1;
							removeUserProfilesPicker(1);
							e.setHandled(tappableHandled);
						}
					}
				};
				userImg[i].setGestureEnabled("TAP", true, true);
				userImg[i].setDrawBorder(true);
				userImg[i].setBorderColour(Colours.zoneBorder);
				userImg[i].setBorderWeight(10);
			} else {
				userImg[i] = new ImageZone(images[loadIndex], x, y, width, height){
					public void hSwipeEvent(HSwipeEvent e){

						if (index1 > 0 && index1 <= numProfiles && e.getSwipeType() == 1){
							index1--;
						} else if(index1 < numProfiles && e.getSwipeType() == -1){
							index1++;
						} else if (index1 == 0) {
							index1 = numProfiles;
						} else if (index1 == numProfiles) {
							index1 = 0;
						}

						//load images starting at index
						loadImages(1, index1);
						e.setHandled(hSwipeableHandled);
					}

				};
				userImg[i].setGestureEnabled("HSwipe", true, true);
				userImg[i].setHSwipeDist(sketch.qSwipeThreshold);
				userImg[i].setDrawBorder(false);
			}

			client.addZone(userImg[i]);

			if(loadIndex == numProfiles){
				loadIndex = 0;
			} else {
				loadIndex++;
			}
		}

		loadIndex = 0;
		x = 0;
		width = 0;
		height = 0;

		//For user2
		for(int i = 0; i < 7; i++){
			if (i== 0){
				width = minSize;
				height = minSize;
				y = applet.getHeight()/2-height - sketch.yOffset;
				x = applet.getWidth() - (i+1)*applet.screenWidth/10-width;
			} else if (i == 1){

				width = (int)(minSize*1.2);
				height = (int)(minSize*1.2);
				y = applet.getHeight()/2-height - sketch.yOffset;

				x = applet.getWidth() - (i+1)*applet.screenWidth/10-width;
			} else if (i == 2){
				width = (int)(minSize*1.5);
				height = (int)(minSize*1.5);
				y = applet.getHeight()/2-height - sketch.yOffset;

				x = applet.getWidth() - (i+1)*applet.screenWidth/10 - minSize/5-width;
			} else if (i == 3){

				width = minSize*2;
				height = minSize*2;
				y = applet.getHeight()/2-height - sketch.yOffset;

				x = applet.getWidth() - (i+1)*applet.screenWidth/10 - (int)(minSize/2)-width;
			} else if (i == 4){

				width = (int)(minSize*1.5);
				height = (int)(minSize*1.5);
				y = applet.getHeight()/2-height - sketch.yOffset;
				x = (2)*applet.screenWidth/10 + minSize/5 + width;
				//x = (int) (3*applet.screenWidth/12 + minSize*1.5-width);
			} else if (i == 5){
				width = (int)(minSize*1.2);
				height = (int)(minSize*1.2);
				y = applet.getHeight()/2-height - sketch.yOffset;
				x = applet.getX() + 2*applet.getWidth()/10;
				//x = (int) (3*applet.screenWidth/12 + minSize/2)-width;
			} else if (i == 6){
				width = minSize;
				height = minSize;
				y = applet.getHeight()/2-height - sketch.yOffset;

				x = 2*applet.screenWidth/12 - width;
			}		

			if (i == 3){
				//the index of the picture to load
				userImg[i+7] = new ImageZone(images[loadIndex], x, y, width, height){
					public void tapEvent(TapEvent e){
						if (tappable && index2 != chosenProfile1){
							chosenProfile2 = index2;
							removeUserProfilesPicker(2);
							userRegistration.userCreation.zoneFlipped = true;
							e.setHandled(tappableHandled);
						}
					}
				};
				userImg[i+7].setGestureEnabled("TAP", true, true);
				userImg[i+7].setBorderColour(Colours.zoneBorder);
				userImg[i+7].setBorderWeight(10);
			} else {
				userImg[i+7] = new ImageZone(images[loadIndex], x, y, width, height){
					public void hSwipeEvent(HSwipeEvent e){

						if (index2 > 0 && index2 <= numProfiles && e.getSwipeType() == -1){
							index2--;
						} else if(index2 < numProfiles && e.getSwipeType() == 1){
							index2++;
						} else if (index2 == 0) {
							index2 = numProfiles;
						} else if (index2 == numProfiles) {
							index2 = 0;
						}

						//load images starting at index
						loadImages(2, index2);
						e.setHandled(hSwipeableHandled);
					}

				};
				userImg[i+7].setGestureEnabled("HSwipe", true, true);
				userImg[i+7].setDrawBorder(false);
				userImg[i+7].setHSwipeDist(sketch.qSwipeThreshold);
			}

			userImg[i+7].rotate((float) Colours.PI);
			client.addZone(userImg[i+7]);

			if(loadIndex == numProfiles){
				loadIndex = 0;
			} else {
				loadIndex++;
			}
		}
	}

	public void loadImages(int user, int loadIndex){
		for(int i = 0; i < 7; i++){
			if(user == 1){
				((ImageZone) userImg[i]).setImage(images[loadIndex]);
			} else {
				((ImageZone) userImg[i+7]).setImage(images[loadIndex]);
			}
			if(loadIndex == numProfiles){
				loadIndex = 0;
			} else {
				loadIndex++;
			}
		}
	}

	public void removeUserProfilesPicker(int user){
		for(int i = 0; i < 7; i++){
			if(user ==1){
				userImg[i].setActive(false);
			} else {
				userImg[i+7].setActive(false);
			}
		}

		if(user == 1){
			arrows[0].setActive(false);
			arrows[1].setActive(false);
			swipe1.setActive(false);

			userImg[3].setXYWH(applet.screenWidth-2*minSize, applet.screenHeight-2*minSize, 2*minSize, 2*minSize);
			userImg[3].setGestureEnabled("TAP", false);
			userImg[3].setActive(true);

			newLang1 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "New Language", sketch.textSize, "CENTER", "CENTER"){
				public void tapEvent(TapEvent e){

					if (tappable){
						this.setGestureEnabled("Tap", false);
						newLang1.setActive(false);
						lastLang1.setActive(false);
						userRegistration.selectLang(1);
						e.setHandled(tappableHandled);
					}
				}
			};
			((TextZone) newLang1).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			((TextZone) newLang1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
			newLang1.setGestureEnabled("Tap", true);

			newLang1.setDrawBorder(false);
			client.addZone(newLang1);

			lastLang1 = new TextZone(applet.screenWidth/2 +1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "Last Language Used", sketch.textSize, "CENTER", "CENTER"){
				public void tapEvent(TapEvent e){

					if (tappable){
						this.setGestureEnabled("Tap", false);
						newLang1.setActive(false);
						lastLang1.setActive(false);
						readProfile(1);
						e.setHandled(tappableHandled);
					}
				}
			};
			((TextZone) lastLang1).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			((TextZone) lastLang1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
			lastLang1.setGestureEnabled("Tap", true);

			lastLang1.setDrawBorder(false);
			client.addZone(lastLang1);

		} else {
			arrows[2].setActive(false);
			arrows[3].setActive(false);
			swipe2.setActive(false);

			userImg[3+7].setXYWH(applet.getX(), applet.screenHeight-4*minSize, 2*minSize, 2*minSize);
			userImg[3+7].setGestureEnabled("TAP", false);
			userImg[3+7].setActive(true);

			newLang2 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2-2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "New Language", sketch.textSize, "CENTER", "CENTER"){
				public void tapEvent(TapEvent e){

					if (tappable){
						newLang2.setActive(false);
						lastLang2.setActive(false);
						this.setGestureEnabled("Tap", false);
						userRegistration.selectLang(2);						
						e.setHandled(tappableHandled);
					}
				}
			};
			((TextZone) newLang2).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			((TextZone) newLang2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
			newLang2.rotate((float) Colours.PI);
			newLang2.setGestureEnabled("Tap", true);
			newLang2.setDrawBorder(false);
			client.addZone(newLang2);

			lastLang2 = new TextZone(applet.screenWidth/2+1, applet.screenHeight/2-2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "Last Language Used", sketch.textSize, "CENTER", "CENTER"){
				public void tapEvent(TapEvent e){

					if (tappable){
						newLang2.setActive(false);
						lastLang2.setActive(false);
						this.setGestureEnabled("Tap", false);
						readProfile(2);
						e.setHandled(tappableHandled);
					}
				}
			};
			((TextZone) lastLang2).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			((TextZone) lastLang2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
			lastLang2.setGestureEnabled("Tap", true);

			lastLang2.setDrawBorder(false);
			lastLang2.rotate((float) Colours.PI);
			client.addZone(lastLang2);
		}
	}

	public void readProfile(int user){
		try {	
			String lastLangUsed;
			long[] fakeCursors = {1, 1};
			userRegistration.selectLang(user);
			if(user == 1){

				reader = new FileReader(".\\data\\users\\info\\" + chosenProfile1 + ".user");
				bReader = new BufferedReader(reader);
				lastLangUsed = bReader.readLine();

				if(lastLangUsed.equalsIgnoreCase("English")){
					userRegistration.english1.tapEvent(new TapEvent(0,0, fakeCursors, 1));
				} else if(lastLangUsed.equalsIgnoreCase("French")){
					userRegistration.french1.tapEvent(new TapEvent(0,0, fakeCursors, 1));
				} else if(lastLangUsed.equalsIgnoreCase("German")){
					//TODO
				} else if(lastLangUsed.equalsIgnoreCase("Spanish")){
					//TODO
				}   
			} else if (user == 2){
				reader = new FileReader(".\\data\\users\\info\\" + chosenProfile2 + ".user");
				bReader = new BufferedReader(reader);
				lastLangUsed = bReader.readLine();
				
				if(lastLangUsed.equalsIgnoreCase("English")){
					userRegistration.english2.tapEvent(new TapEvent(0,0, fakeCursors, 1));
				} else if(lastLangUsed.equalsIgnoreCase("French")){
					userRegistration.french2.tapEvent(new TapEvent(0,0, fakeCursors, 1));
				} else if(lastLangUsed.equalsIgnoreCase("German")){
					//TODO
				} else if(lastLangUsed.equalsIgnoreCase("Spanish")){
					//TODO
				}   
			}
			bReader.close();
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
