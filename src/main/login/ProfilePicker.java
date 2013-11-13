package main.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import main.Colours;
import main.MainSketch;
import processing.core.PApplet;
import processing.core.PImage;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.events.HSwipeEvent;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.ImageZone;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.Zone;

public class ProfilePicker {

	PApplet applet;
	TouchClient client;
	MainSketch sketch;
	LoginScreen loginScreen;

	ImageZone[] arrows, userImg;
	Zone swipe1, swipe2;
	PImage[] images;
	FileReader reader;
	BufferedReader bReader;

	int numProfiles, minSize;
	int index1 = 0;
	int index2 = 0;
	int chosenProfile1 = -1;
	int chosenProfile2 = -1;
	
	final int IMG_ONSCR = 7;

	public ProfilePicker(TouchClient client, MainSketch sketch, LoginScreen loginScreen){
		applet = TouchClient.getPApplet();
		this.sketch = sketch;
		this.client = client;
		this.loginScreen = loginScreen;

		arrows = new ImageZone[4];
		userImg = new ImageZone[14];
		minSize = applet.screenWidth/15;


		loadPImages();
		createSwipeAreas();
		createArrows();

		//create images starting at index 0
		createProfileImgZones();
	}

	public void createArrows(){

		int arrowSize = applet.screenHeight/10;
		int leftSideX = applet.getX();
		int rightSizeX = applet.screenWidth-arrowSize;
		int arrowBY = applet.getY() + applet.screenHeight/2 + arrowSize/2;
		int arrowTY = applet.getY() + applet.screenHeight/2 -arrowSize - arrowSize/2;


		//For User1

		arrows[0] = new ImageZone(applet.loadImage("lArrow.png"), leftSideX, arrowBY, arrowSize, arrowSize){
			public void tapEvent(TapEvent e){
				if (getTappable()){
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

				if (getTappable()){
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
		arrows[0].setActive(false);
		arrows[1].setActive(false);
		client.addZone(arrows[0]);
		client.addZone(arrows[1]);


		//For User2
		arrows[2] = new ImageZone(applet.loadImage("rArrow.png"), rightSizeX, arrowTY, arrowSize, arrowSize){
			public void tapEvent(TapEvent e){
				if (getTappable()){
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

				if (getTappable()){
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
		arrows[2].setActive(false);
		arrows[3].setActive(false);

		client.addZone(arrows[2]);
		client.addZone(arrows[3]);



	}

	/**
	 * Creates a RectZone used to sense horizontal swipes over
	 * the profile picker for each user
	 */
	public void createSwipeAreas(){


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
		swipe1.setActive(false);
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
		swipe2.setActive(false);
		client.addZone(swipe2);
	}

	public void createProfileImgZones(){
		int loadIndex = 0;
		int x = 0;

		int y = applet.screenHeight/2 + sketch.yOffset;
		int width = 0;
		int height = 0;

		//*****************
		//For user1
		//*****************
		for(int i = 0; i < IMG_ONSCR; i++){

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
				width = (int)(minSize*1.5);
				height = (int)(minSize*1.5);
				x = applet.getWidth() - (i+1)*applet.screenWidth/10 - minSize/5+width;
			} else if (i == 5){
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
						if (getTappable() && index1 != chosenProfile2){
							chosenProfile1 = index1;
							removeUserProfilesPicker(1);
							loginScreen.createLanguageOptions(1);
							userImg[3].setXYWH(applet.screenWidth-2*minSize, applet.screenHeight-2*minSize, 2*minSize, 2*minSize);
							userImg[3].setGestureEnabled("TAP", false);
							userImg[3].setActive(true);
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

			userImg[i].setActive(false);
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

		//*****************
		//For user2
		//*****************
		for(int i = 0; i < IMG_ONSCR; i++){
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
			} else if (i == 5){
				width = (int)(minSize*1.2);
				height = (int)(minSize*1.2);
				y = applet.getHeight()/2-height - sketch.yOffset;
				x = applet.getX() + 2*applet.getWidth()/10;
			} else if (i == 6){
				width = minSize;
				height = minSize;
				y = applet.getHeight()/2-height - sketch.yOffset;

				x = 2*applet.screenWidth/12 - width;
			}		

			if (i == 3){
				//the index of the picture to load
				userImg[i+IMG_ONSCR] = new ImageZone(images[loadIndex], x, y, width, height){
					public void tapEvent(TapEvent e){
						if (getTappable() && index2 != chosenProfile1){
							chosenProfile2 = index2;
							removeUserProfilesPicker(2);
							loginScreen.createLanguageOptions(2);

							userImg[3 + IMG_ONSCR].setXYWH(applet.getX(), applet.screenHeight-4*minSize, 2*minSize, 2*minSize);
							userImg[3 + IMG_ONSCR].setGestureEnabled("TAP", false);
							userImg[3 + IMG_ONSCR].setActive(true);
							e.setHandled(tappableHandled);
						}
					}
				};
				userImg[i+IMG_ONSCR].setGestureEnabled("TAP", true, true);
				userImg[i+IMG_ONSCR].setBorderColour(Colours.zoneBorder);
				userImg[i+IMG_ONSCR].setBorderWeight(10);
			} else {
				userImg[i+IMG_ONSCR] = new ImageZone(images[loadIndex], x, y, width, height){
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
				userImg[i+IMG_ONSCR].setGestureEnabled("HSwipe", true, true);
				userImg[i+IMG_ONSCR].setDrawBorder(false);
				userImg[i+IMG_ONSCR].setHSwipeDist(sketch.qSwipeThreshold);
			}

			userImg[i+IMG_ONSCR].rotate((float) Colours.PI);
			userImg[i+IMG_ONSCR].setActive(false);
			client.addZone(userImg[i+IMG_ONSCR]);

			if(loadIndex == numProfiles){
				loadIndex = 0;
			} else {
				loadIndex++;
			}
		}
	}

	/**
	 * Loads the profile images into PImages
	 */
	public void loadPImages(){
		numProfiles = new File(".\\data\\users\\images").list().length - 2;
		images = new PImage[numProfiles+1];

		for(int i = 0; i <= numProfiles; i++){
			images[i] = applet.loadImage("\\users\\images\\user" + (i) + ".png");
		}
	}

	/**
	 * Activates the profile picker zones for the specified user
	 * @param user
	 */
	public void activateProfilePicker(int user){
		if(user == 1){
			arrows[0].setActive(true);
			arrows[1].setActive(true);
			swipe1.setActive(true);

			for(int i = 0; i < IMG_ONSCR; i ++){
				userImg[i].setActive(true);
			}

		} else {
			arrows[2].setActive(true);
			arrows[3].setActive(true);
			swipe2.setActive(true);

			for(int i = 0; i < IMG_ONSCR; i ++){
				userImg[i+IMG_ONSCR].setActive(true);
			}
		}


	}

	/**
	 * Load the profile pictures into the ImageZones
	 * @param user
	 * @param loadIndex
	 */
	public void loadImages(int user, int loadIndex){
		
		for(int i = 0; i < IMG_ONSCR; i++){
			if(user == 1){
				userImg[i].setImage(images[loadIndex]);
				
			} else {
				userImg[i+IMG_ONSCR].setImage(images[loadIndex]);
			}
			
			if(loadIndex == numProfiles){
				loadIndex = 0;
			} else {
				loadIndex++;
			}
		}
	}

	/**
	 * Disables the profile picker zones
	 * @param user
	 */
	public void removeUserProfilesPicker(int user){
		for(int i = 0; i < IMG_ONSCR; i++){
			if(user == 1){
				userImg[i].setActive(false);
			} else {
				userImg[i+IMG_ONSCR].setActive(false);
			}
		}

		if(user == 1){
			arrows[0].setActive(false);
			arrows[1].setActive(false);
			swipe1.setActive(false);


		} else {
			arrows[2].setActive(false);
			arrows[3].setActive(false);
			swipe2.setActive(false);

		}
	}

	public void readProfile(int user){
		try {	
			String lastLangUsed;
			long[] fakeCursors = {1, 1};
			loginScreen.createLanguageButtons(user);
			if(user == 1){

				reader = new FileReader(".\\data\\users\\info\\" + chosenProfile1 + ".user");
				bReader = new BufferedReader(reader);
				lastLangUsed = bReader.readLine();

				if(lastLangUsed.equalsIgnoreCase("English")){
					loginScreen.english1.tapEvent(new TapEvent(0,0, fakeCursors, 1));
				} else if(lastLangUsed.equalsIgnoreCase("French")){
					loginScreen.french1.tapEvent(new TapEvent(0,0, fakeCursors, 1));
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
					loginScreen.english2.tapEvent(new TapEvent(0,0, fakeCursors, 1));
				} else if(lastLangUsed.equalsIgnoreCase("French")){
					loginScreen.french2.tapEvent(new TapEvent(0,0, fakeCursors, 1));
				} /*else if(lastLangUsed.equalsIgnoreCase("German")){
					//TODO
				} else if(lastLangUsed.equalsIgnoreCase("Spanish")){
					//TODO
				}  */ 
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

//TODO
//TODO
//TODO
//TODO
/**
 * 
				//Sets the ImageZone to be 'disabled' when the other user
				//has already chosen it as their profile picture
				if(index1 == loadIndex  && loadIndex == chosenProfile2){
					System.out.println(loadIndex + " " + index1 + " " +  chosenProfile2);
					userImg[3].setFilterColFlag(true);
				} else {
					userImg[i].setFilterColFlag(false);
				}
				**/
