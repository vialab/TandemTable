package TandemTable.sections.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import TandemTable.Colours;
import TandemTable.Sketch;
import processing.core.PApplet;
import processing.core.PImage;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.events.HSwipeEvent;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.ImageZone;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.Zone;

/**
 * Load previously created avatars into ImageZones to allow 
 * the user to select their profile for use
 *
 */
public class ProfilePicker {

	PApplet applet;
	TouchClient client;
	Sketch sketch;
	LoginScreen loginScreen;

	ImageZone[] arrows, userImg;
	Zone swipe1, swipe2;
	PImage[] images;
	FileReader reader;
	BufferedReader bReader;

	//number of profiles in the system
	int numProfiles;
	//index of the last Image Zone for 
	//the user profiles (numProfiles - 1)
	int lastZoneIndex;
	//size of the profile pictures
	int imgSize;
	//middle profile picture's position
	int midX1, midY1, midX2, midY2;

	int index1 = 0;
	int index2 = 0;

	//Profiles chosen by the user
	int chosenProfile1 = -1;
	int chosenProfile2 = -1;

	//last disabled profiles
	int lastDisabled1, lastDisabled2;

	//Number of profiles on screen
	final int IMG_ONSCR = 7;

	//Middle ImageZone (0-6)
	final int MID = 3;

	/**
	 * Constructor for the profile picker
	 * @param client
	 * @param sketch
	 * @param loginScreen
	 */
	public ProfilePicker(TouchClient client, Sketch sketch, LoginScreen loginScreen){
		applet = TouchClient.getPApplet();
		this.sketch = sketch;
		this.client = client;
		this.loginScreen = loginScreen;

		arrows = new ImageZone[4];
		userImg = new ImageZone[14];
		imgSize = applet.screenWidth/15;


		loadImages();
		createSwipeAreas();
		createArrows();

		//create images starting at index 0
		createProfileImgZones();
	}

	/**
	 * Loads the arrow images into ImageZones
	 */
	public void createArrows(){

		int arrowSize = applet.screenHeight/10;
		int leftSideX = applet.getX();
		int rightSizeX = applet.screenWidth-arrowSize;
		int arrowBY = applet.getY() + applet.screenHeight/2 + arrowSize/2;
		int arrowTY = applet.getY() + applet.screenHeight/2 -arrowSize - arrowSize/2;


		//For User1

		arrows[0] = new ImageZone(applet.loadImage("lArrow.png"), leftSideX, arrowBY, arrowSize, arrowSize){
			public void tapEvent(TapEvent e){
				if (isTappable()){
					if (index1 > 0){
						index1--;
					} else {
						index1 = lastZoneIndex;
					}

					//load images starting at index
					setImages(1, index1);
					//if(chosenProfile2 != -1) disableProfile(1);
					e.setHandled(tappableHandled);

				}
			}
		};

		arrows[1] = new ImageZone(applet.loadImage("rArrow.png"), rightSizeX, arrowBY, arrowSize, arrowSize){
			public void tapEvent(TapEvent e){

				if (isTappable()){
					if(index1 == lastZoneIndex){
						index1 = 0;
					} else {
						index1++;
					}
					//load images starting at index
					setImages(1, index1);
					//if(chosenProfile2 != -1) disableProfile(1);
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
				if (isTappable()){
					if (index2 > 0){
						index2--;
					} else {
						index2 = lastZoneIndex;
					}

					//load images starting at index
					setImages(2, index2);
					//if(chosenProfile1 != -1) disableProfile(2);
					e.setHandled(tappableHandled);

				}
			}
		};

		arrows[3] = new ImageZone(applet.loadImage("lArrow.png"), leftSideX, arrowTY, arrowSize, arrowSize){
			public void tapEvent(TapEvent e){

				if (isTappable()){
					if(index2 == lastZoneIndex){
						index2 = 0;
					} else {
						index2++;
					}
					//load images starting at index
					setImages(2, index2);
					//if(chosenProfile1 != -1) disableProfile(2);
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


		swipe1 = new RectZone(0, applet.getHeight()/2, applet.getWidth(), imgSize*2 + sketch.yOffset){
			public void hSwipeEvent(HSwipeEvent e){

				if (index1 > 0 && index1 <= lastZoneIndex && e.getSwipeType() == 1){
					index1--;
				} else if(index1 < lastZoneIndex && e.getSwipeType() == -1){
					index1++;
				} else if (index1 == 0) {
					index1 = lastZoneIndex;
				} else if (index1 == lastZoneIndex) {
					index1 = 0;
				}

				//load images starting at index
				setImages(1, index1);
				//if(chosenProfile2 != -1) disableProfile(1);
				e.setHandled(hSwipeableHandled);
			}

		};

		swipe1.setGestureEnabled("HSwipe", true, true);
		swipe1.setDrawBorder(false);
		swipe1.setHSwipeDist(sketch.qSwipeThreshold);
		swipe1.setActive(false);
		client.addZone(swipe1);

		swipe2 = new RectZone(0, applet.getHeight()/2-imgSize*2 - sketch.yOffset, applet.getWidth(), imgSize*2 + sketch.yOffset){
			public void hSwipeEvent(HSwipeEvent e){

				if (index2 > 0 && index2 <= lastZoneIndex && e.getSwipeType() == -1){
					index2--;
				} else if(index2 < lastZoneIndex && e.getSwipeType() == 1){
					index2++;
				} else if (index2 == 0) {
					index2 = lastZoneIndex;
				} else if (index2 == lastZoneIndex) {
					index2 = 0;
				}

				//load images starting at index
				setImages(2, index2);
				//if(chosenProfile1 != -1) disableProfile(2);
				e.setHandled(hSwipeableHandled);
			}

		};
		swipe2.setGestureEnabled("HSwipe", true, true);
		swipe2.setDrawBorder(false);
		swipe2.setHSwipeDist(sketch.qSwipeThreshold);
		swipe2.setActive(false);
		client.addZone(swipe2);
	}

	/**
	 * Create the ImageZones used in the profile picker
	 */
	public void createProfileImgZones(){
		int loadIndex = 0;
		int x = 0;

		int y = midY1 = applet.screenHeight/2 + sketch.yOffset;
		int width = 0;
		int height = 0;

		//*************************************************
		//   For user 1
		//*************************************************
		for(int i = 0; i < IMG_ONSCR; i++){

			if(i > 3){
				x = (int) ((i+1)*applet.screenWidth/9.2 + imgSize);
			} else {
				x = (int) ((i+1)*applet.screenWidth/9.2);
				if(i == 3){
					midX1 = x;
				}
			}
			width = imgSize;
			height = imgSize;

			if (i == 3){
				width *= 2;
				height *= 2;
			}

			final int ii = i;

			//the index of the picture to load
			userImg[i] = new ImageZone(images[loadIndex], x, y, width, height){
				public void tapEvent(TapEvent e){
					if (ii == 3 && isTappable() && index1 != chosenProfile2){
						chosenProfile1 = index1;
						//if(chosenProfile2 == -1) disableProfile(2);
						removeUserProfilesPicker(1);
						loginScreen.activateNewLastButtons(1);
						loginScreen.profileCreation.loadProfileImage(1, this.getImage());
						e.setHandled(tappableHandled);
					}
				}

				public void hSwipeEvent(HSwipeEvent e){


					if (index1 > 0 && index1 <= lastZoneIndex && e.getSwipeType() == 1){
						index1--;
					} else if(index1 < lastZoneIndex && e.getSwipeType() == -1){
						index1++;
					} else if (index1 == 0) {
						index1 = lastZoneIndex;
					} else if (index1 == lastZoneIndex) {
						index1 = 0;
					}

					//load images starting at index
					setImages(1, index1);
				//	if(chosenProfile2 != -1) disableProfile(1);
					e.setHandled(hSwipeableHandled);
				}

			};

			if(i == 3){
				userImg[i].setGestureEnabled("TAP", true, true);
				userImg[i].setDrawBorder(true);
				userImg[i].setBorderColour(Colours.zoneBorder);
				userImg[i].setBorderWeight(10);
			} else {
				userImg[i].setDrawBorder(false);
			}

			userImg[i].setGestureEnabled("HSwipe", true, true);
			userImg[i].setHSwipeDist(sketch.qSwipeThreshold);
			userImg[i].setActive(false);
			userImg[i].setTapUp(true);
			client.addZone(userImg[i]);

			if(loadIndex == lastZoneIndex){
				loadIndex = 0;
			} else {
				loadIndex++;
			}
		}	

		//*************************************************
		//    For user 2
		//*************************************************
		loadIndex = 0;
		x = 0;
		width = 0;
		height = 0;


		for(int i = 0; i < IMG_ONSCR; i++){
			width = imgSize;
			height = imgSize;
			y = applet.getHeight()/2-height - sketch.yOffset;

			if(i > 3){
				x = (int) ((i+1)*applet.screenWidth/9.2 + imgSize);
			} else {
				x = (int) ((i+1)*applet.screenWidth/9.2);

				if(i == 3){
					midX2 = x;
					midY2 = y - height;
				}
			}


			if (i == 3){
				y -= height;
				width *= 2;
				height *= 2;
			}

			final int ii = i;

			//the index of the picture to load
			userImg[i+IMG_ONSCR] = new ImageZone(images[loadIndex], x, y, width, height){
				public void tapEvent(TapEvent e){
					if (ii == 3 && isTappable() && index2 != chosenProfile1){
						chosenProfile2 = index2;
						//if(chosenProfile1 == -1) disableProfile(1);
						removeUserProfilesPicker(2);
						loginScreen.activateNewLastButtons(2);
						loginScreen.profileCreation.loadProfileImage(2, this.getImage());
						e.setHandled(tappableHandled);
					}


				}

				public void hSwipeEvent(HSwipeEvent e){

					if (index2 > 0 && index2 <= lastZoneIndex && e.getSwipeType() == -1){
						index2--;
					} else if(index2 < lastZoneIndex && e.getSwipeType() == 1){
						index2++;
					} else if (index2 == 0) {
						index2 = lastZoneIndex;
					} else if (index2 == lastZoneIndex) {
						index2 = 0;
					}

					//load images starting at index
					setImages(2, index2);
					//if(chosenProfile1 != -1) disableProfile(2);
					e.setHandled(hSwipeableHandled);
				}
			};
			
			if(ii == 3){
				userImg[i+IMG_ONSCR].setGestureEnabled("TAP", true, true);
				userImg[i+IMG_ONSCR].setBorderColour(Colours.zoneBorder);
				userImg[i+IMG_ONSCR].setBorderWeight(10);
			} else {
				userImg[i+IMG_ONSCR].setDrawBorder(false);
				userImg[i+IMG_ONSCR].setHSwipeDist(sketch.qSwipeThreshold);
			}


			userImg[i+IMG_ONSCR].setGestureEnabled("HSwipe", true, true);
			userImg[i+IMG_ONSCR].rotate((float) Colours.PI);
			userImg[i+IMG_ONSCR].setActive(false);
			client.addZone(userImg[i+IMG_ONSCR]);

			if(loadIndex == lastZoneIndex){
				loadIndex = 0;
			} else {
				loadIndex++;
			}
		}
	}

	/**
	 * Loads the profile images into PImages
	 */
	public void loadImages(){
		numProfiles = new File(".\\data\\users\\images").list().length;
		lastZoneIndex = numProfiles - 1;
		images = new PImage[numProfiles];

		for(int i = 0; i < numProfiles; i++){
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
	public void setImages(int user, int loadIndex){		
		//Load Profile Pictures into the ImageZones
		for(int i = 0; i < IMG_ONSCR; i++){
			if(user == 1){
				userImg[i].setImage(images[loadIndex]);
			} else {
				userImg[i+IMG_ONSCR].setImage(images[loadIndex]);
			}

			if(loadIndex == lastZoneIndex){
				loadIndex = 0;
			} else {
				loadIndex++;
			}
		}
	}

	/**
	 * Disable the profile picture that has already been chosen
	 * by the other user
	 * @param user The user's image zone is disabled
	 * @param loadIndex
	 */
	/*public void disableProfile(int user){

			if(user == 1){// && loadIndex < IMG_ONSCR){
				System.out.println("Disable user 1: " + index1 + " " + chosenProfile2 + "  " + numProfiles);
				userImg[lastDisabled1].setFilterColFlag(false);
				int index = 3 - index1;
				
				if(index >= 0 || index <= -3 ) {
					System.out.println(index);
					if(index <= -(numProfiles - IMG_ONSCR)) {
						index = 6 - index1%6 ;
					}
					System.out.println(index);
					userImg[index].setFilterColFlag(true);
					lastDisabled1 = index;
				}
				
				
			} else {
				
				userImg[lastDisabled2].setFilterColFlag(false);
				int index = chosenProfile1 + 3 - index2;
				
				if(index >= 0 || index <= -3 ) {
					System.out.println(index);
					if(index <= -(numProfiles - IMG_ONSCR)) {
						index = 6 - index1%6 ;
					}
					System.out.println(index);
					userImg[index + IMG_ONSCR].setFilterColFlag(true);
					lastDisabled2 = index + IMG_ONSCR;
				}

			//if(user == 2){// && loadIndex < IMG_ONSCR){
				//System.out.println("Disable user 2: " + loadIndex + " " + chosenProfile1 + "  " + numProfiles);
				//userImg[chosenProfile1 + IMG_ONSCR + 3].setFilterColFlag(true);
				//userImg[lastDisabled2].setFilterColFlag(false);
				//lastDisabled2 = chosenProfile1 + IMG_ONSCR + 3;
			}
		//}
	}*/

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

	/**
	 * Read-in user profiles ('#.user')
	 * Used currently to allow the user to select their 
	 * last target language used from their last 
	 * learning session.
	 * 
	 * @param user
	 */
	public void readProfile(int user){
		try {	
			String lastLangUsed;
			long[] fakeCursors = {1, 1};
			loginScreen.activateLanguageButtons(user);
			if(user == 1){

				reader = new FileReader(".\\data\\users\\info\\" + chosenProfile1 + ".user");
				bReader = new BufferedReader(reader);
				lastLangUsed = bReader.readLine();

				if(lastLangUsed.equalsIgnoreCase("English")){
					loginScreen.english1.tapEvent(new TapEvent(0,0, fakeCursors, 1));
				} else if(lastLangUsed.equalsIgnoreCase("French")){
					loginScreen.french1.tapEvent(new TapEvent(0,0, fakeCursors, 1));
				} 
			} else if (user == 2){
				reader = new FileReader(".\\data\\users\\info\\" + chosenProfile2 + ".user");
				bReader = new BufferedReader(reader);
				lastLangUsed = bReader.readLine();

				if(lastLangUsed.equalsIgnoreCase("English")){
					loginScreen.english2.tapEvent(new TapEvent(0,0, fakeCursors, 1));
				} else if(lastLangUsed.equalsIgnoreCase("French")){
					loginScreen.french2.tapEvent(new TapEvent(0,0, fakeCursors, 1));
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