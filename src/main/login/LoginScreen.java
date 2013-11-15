package main.login;
import main.Colours;
import main.Sketch;
import processing.core.PApplet;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.TextZone;
import vialab.simpleMultiTouch.zones.Zone;

/**
 * Class responsible for initializing the login screen
 * where users choose a previously created avatar or 
 * where they can create their own. Target languages are
 * selected after that.
 *
 */
public class LoginScreen {
	PApplet applet;
	TouchClient client;
	Sketch sketch;
	ProfilePicker profilePicker;
	ProfileCreation profileCreation;

	boolean langSelect1 = false, langSelect2 = false;

	TextZone english1, french1, portuguese1, german1, english2, french2, portuguese2, german2, 
	login1, login2, newUser1, newUser2, newLang1, lastLang1, newLang2, lastLang2, cancel1, cancel2;

	//Zone responsible for graphics rendered during title screen
	RectZone bkgZone;
	//Zones used in the title screen
	TextZone titleZone1, titleZone2;

	String pickedLang1, pickedLang2;

	int minSize;
	long swipeFlag = -1;

	/**
	 * Initialize the login screen
	 * Starts the program
	 * @param client
	 * @param sketch
	 */
	public void initialize(TouchClient client, Sketch sketch){
		applet = TouchClient.getPApplet();
		this.sketch = sketch;
		this.client = client;

		createLoadingScreen();
		createCancel();
		profilePicker = new ProfilePicker(client, sketch, this);
		profileCreation = new ProfileCreation(client, sketch, profilePicker, this);
		createLanguageButtons();
		createNewLastLangButtons();

	}

	/**
	 * Create the title screen and load the profile images
	 */
	public void createLoadingScreen(){
		final int circX1 = sketch.getWidth()/4;
		final int circX2 = (int) (sketch.getWidth()/1.5);
		final int circY = sketch.getHeight()/3;
		final int circDiam = sketch.getWidth();

		bkgZone = new RectZone(0, 0, sketch.getWidth(), sketch.getHeight()){
			public void drawZone(){
				sketch.fill(Colours.circTitle[0].getRed(), Colours.circTitle[0].getGreen(), Colours.circTitle[0].getBlue());
				sketch.stroke(Colours.circTitle[0].getRed(), Colours.circTitle[0].getGreen(), Colours.circTitle[0].getBlue());
				sketch.ellipse(circX1, circY, circDiam, circDiam);     

				sketch.fill(Colours.circTitle[1].getRed(), Colours.circTitle[1].getGreen(), Colours.circTitle[1].getBlue());
				sketch.stroke(Colours.circTitle[1].getRed(), Colours.circTitle[1].getGreen(), Colours.circTitle[1].getBlue());
				sketch.ellipse(circX2, circY, circDiam, circDiam);     
			}			
		};

		bkgZone.setDrawBorder(false);
		bkgZone.setGestureEnabled("TAP", true, true);
		client.addZone(bkgZone);

		titleZone1 = new TextZone(0, applet.screenHeight/2, sketch.getWidth(), sketch.getHeight()/2, 
				Colours.pFont, "TandemTable", sketch.textSize*4, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){
				if (isTappable()){
					createLoginButtons();
					client.removeZone(bkgZone);
					client.removeZone(this);
					client.removeZone(titleZone2);

					e.setHandled(tappableHandled);
				}
			}
		};

		titleZone1.setTextColour(Colours.titleColor.getRed(), Colours.titleColor.getGreen(), Colours.titleColor.getBlue(), Colours.titleColor.getAlpha());
		titleZone1.setGestureEnabled("Tap", true);
		titleZone1.setDrawBorder(false);
		client.addZone(titleZone1);

		titleZone2 = new TextZone(0, 0, sketch.getWidth(), sketch.getHeight()/2, 
				Colours.pFont, "TandemTable", sketch.textSize*4, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){
				if (isTappable()){
					createLoginButtons();
					client.removeZone(bkgZone);
					client.removeZone(this);
					client.removeZone(titleZone1);

					e.setHandled(tappableHandled);
				}
			}
		};

		titleZone2.setTextColour(Colours.titleColor.getRed(), Colours.titleColor.getGreen(), Colours.titleColor.getBlue());
		titleZone2.rotate((float) Colours.PI);
		titleZone2.setGestureEnabled("Tap", true);
		titleZone2.setDrawBorder(false);
		client.addZone(titleZone2);
	}

	/**
	 * Create cancel buttons
	 */
	public void createCancel(){
		cancel1 = new TextZone(applet.getX()+1, applet.screenHeight - sketch.buttonHeight-2, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Cancel", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){

					if(profileCreation.drawUser1 != null){profileCreation.drawUser1.setActive(false);}

					cancel1.setActive(false);
					newLang1.setActive(false);
					lastLang1.setActive(false);

					//if(profilePicker.userImg[profileCreation.MID] != null) profilePicker.userImg[profileCreation.MID].setXYWH(profilePicker.midX1, profilePicker.midY1, profilePicker.imgSize*2, profilePicker.imgSize*2);

					english1.setActive(false);
					french1.setActive(false);
					portuguese1.setActive(false);
					german1.setActive(false);
					
					english1.setGestureEnabled("TAP", true);
					french1.setGestureEnabled("TAP", true);
					
					english1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
					french1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
					
					profilePicker.userImg[profilePicker.lastDisabled2].setFilterColFlag(false);
					
					if(profileCreation.profileImg1 != null) profileCreation.profileImg1.setActive(false);
					langSelect1 = false;
					pickedLang1 = "";

					profilePicker.chosenProfile1 = -1;
					profilePicker.userImg[profilePicker.MID].setGestureEnabled("TAP", true);

					profilePicker.removeUserProfilesPicker(1);
					login1.setActive(true);
					newUser1.setActive(true);
					e.setHandled(tappableHandled);

				}
			}
		};

		cancel1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		cancel1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		cancel1.setGestureEnabled("TAP", true, true);
		cancel1.setDrawBorder(false);
		cancel1.setActive(false);
		client.addZone(cancel1);

		cancel2 = new TextZone(applet.getX() + 1, applet.getY() + 2, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Cancel", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){
					if(profileCreation.drawUser2 != null){profileCreation.drawUser2.setActive(false);}

					cancel2.setActive(false);
					newLang2.setActive(false);
					lastLang2.setActive(false);

					english2.setActive(false);
					french2.setActive(false);
					portuguese2.setActive(false);
					german2.setActive(false);

					english2.setGestureEnabled("TAP", true);
					french2.setGestureEnabled("TAP", true);
					
					english2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
					french2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
					
					profilePicker.chosenProfile2 = -1;
					profilePicker.userImg[profilePicker.MID + profilePicker.IMG_ONSCR].setGestureEnabled("TAP", true);

					//if(profilePicker.userImg[profileCreation.MID + profilePicker.IMG_ONSCR] != null) profilePicker.userImg[profileCreation.MID + profilePicker.IMG_ONSCR].setXYWH(profilePicker.midX2, profilePicker.midY2, profilePicker.imgSize*2, profilePicker.imgSize*2);
					profilePicker.removeUserProfilesPicker(2);
					
					profilePicker.userImg[profilePicker.lastDisabled1].setFilterColFlag(false);
					
					if(profileCreation.profileImg2 != null) profileCreation.profileImg2.setActive(false);
					langSelect2 = false;
					pickedLang2 = "";

					login2.setActive(true);
					newUser2.setActive(true);
					e.setHandled(tappableHandled);

				}
			}
		};

		cancel2.rotate((float) (Colours.PI));
		cancel2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		cancel2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		cancel2.setGestureEnabled("TAP", true, true);
		cancel2.setDrawBorder(false);
		cancel2.setActive(false);
		client.addZone(cancel2);

	}

	/**
	 * Create the login and new user buttons
	 */
	public void createLoginButtons(){
		//*****************
		// User 1
		//*****************
		//Login button for user 1
		login1 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Login", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (isTappable()){

					login1.setActive(false);
					newUser1.setActive(false);
					profilePicker.activateProfilePicker(1);
					cancel1.setActive(true);
					e.setHandled(tappableHandled);
				}
			}
		};
		login1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		login1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		login1.setGestureEnabled("Tap", true);

		login1.setDrawBorder(false);
		client.addZone(login1);

		newUser1 = new TextZone(applet.screenWidth/2 +1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "New User", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (isTappable()){
					login1.setActive(false);
					newUser1.setActive(false);
					cancel1.setActive(true);
					profileCreation.createDrawUser(1);
					e.setHandled(tappableHandled);
				}
			}
		};
		newUser1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		newUser1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		newUser1.setGestureEnabled("Tap", true);

		newUser1.setDrawBorder(false);
		client.addZone(newUser1);

		//*******************
		// User 2
		//******************
		//Login button for user 2
		login2 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2-2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Login", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (isTappable()){
					login2.setActive(false);
					newUser2.setActive(false);
					profilePicker.activateProfilePicker(2);
					cancel2.setActive(true);
					e.setHandled(tappableHandled);
				}
			}
		};
		login2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		login2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		login2.rotate((float) Colours.PI);
		login2.setGestureEnabled("Tap", true);
		login2.setDrawBorder(false);
		client.addZone(login2);

		newUser2 = new TextZone(applet.screenWidth/2+1, applet.screenHeight/2-2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "New User", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (isTappable()){
					login2.setActive(false);
					newUser2.setActive(false);
					cancel2.setActive(true);
					profileCreation.createDrawUser(2);						
					e.setHandled(tappableHandled);
				}
			}
		};
		newUser2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		newUser2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		newUser2.setGestureEnabled("Tap", true);

		newUser2.setDrawBorder(false);
		newUser2.rotate((float) Colours.PI);
		client.addZone(newUser2);
	}

	/**
	 * Creates the language option buttons
	 */
	public void createNewLastLangButtons(){
		
		//*************************************
		//         User 1
		//*************************************
		newLang1 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "New Language", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (isTappable()){
					newLang1.setActive(false);
					lastLang1.setActive(false);
					activateLanguageButtons(1);
					e.setHandled(tappableHandled);
				}
			}
		};
		newLang1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		newLang1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		newLang1.setGestureEnabled("Tap", true);
		newLang1.setActive(false);
		newLang1.setDrawBorder(false);
		client.addZone(newLang1);
		
		lastLang1 = new TextZone(applet.screenWidth/2 +1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Last Language Used", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (isTappable()){
					newLang1.setActive(false);
					lastLang1.setActive(false);
					profilePicker.readProfile(1);
					e.setHandled(tappableHandled);
				}
			}
		};
		lastLang1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		lastLang1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		lastLang1.setGestureEnabled("Tap", true);
		lastLang1.setActive(false);
		lastLang1.setDrawBorder(false);
		client.addZone(lastLang1);
		
		//*************************************
		//         User 2
		//*************************************
		
		newLang2 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2-2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "New Language", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (isTappable()){
					newLang2.setActive(false);
					lastLang2.setActive(false);
					activateLanguageButtons(2);						
					e.setHandled(tappableHandled);
				}
			}
		};
		newLang2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		newLang2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		newLang2.rotate((float) Colours.PI);
		newLang2.setGestureEnabled("Tap", true);
		newLang2.setDrawBorder(false);
		newLang2.setActive(false);
		client.addZone(newLang2);

		lastLang2 = new TextZone(applet.screenWidth/2+1, applet.screenHeight/2-2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Last Language Used", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (isTappable()){
					newLang2.setActive(false);
					lastLang2.setActive(false);
					profilePicker.readProfile(2);
					e.setHandled(tappableHandled);
				}
			}
		};
		lastLang2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		lastLang2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		lastLang2.setGestureEnabled("Tap", true);
		lastLang2.setActive(false);
		lastLang2.setDrawBorder(false);
		lastLang2.rotate((float) Colours.PI);
		client.addZone(lastLang2);
	}
	
	
	/**
	 * Activate the 'new language' and 'last language' buttons
	 * @param user
	 */
	public void activateNewLastButtons(int user){

		if(user == 1){
			newLang1.setActive(true);
			lastLang1.setActive(true);
		} else {
			newLang2.setActive(true);
			lastLang2.setActive(true);
		}
	}

	/**
	 * Create the language selection buttons
	 */
	public void createLanguageButtons(){
		//*******************************************
		//             User 1
		//*******************************************
		english1 = new TextZone(applet.screenWidth/2-2*sketch.buttonWidth-2, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "English", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){
					if(!langSelect1){
						english1.setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
						newUser1.setActive(false);
						langSelect1 = true;
						pickedLang1 = "English";

						if(langSelect2){
							removeZones();
							sketch.initializeMainScreen(pickedLang1, pickedLang2);
						}
					}

					e.setHandled(tappableHandled);

				}
			}
		};

		english1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		english1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		english1.setGestureEnabled("TAP", true, true);
		english1.setDrawBorder(false);
		english1.setActive(false);
		client.addZone(english1);

		//French language Button
		french1 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "French", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){
					if(!langSelect1){
						french1.setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
						newUser1.setActive(false);
						langSelect1 = true;
						pickedLang1 = "French";

						if(langSelect2){

							removeZones();
							sketch.initializeMainScreen(pickedLang1, pickedLang2);
						}
					}

					e.setHandled(tappableHandled);

				}
			}
		};

		french1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		french1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		french1.setGestureEnabled("TAP", true, true);
		french1.setDrawBorder(false);
		french1.setActive(false);
		client.addZone(french1);

		//Portuguese language Button
		portuguese1 = new TextZone(applet.screenWidth/2, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Portuguese", sketch.textSize, "CENTER", "CENTER");

		portuguese1.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
		portuguese1.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
		portuguese1.setDrawBorder(false);
		portuguese1.setActive(false);
		client.addZone(portuguese1);

		//Spanish language Button
		german1 = new TextZone(applet.screenWidth/2 + sketch.buttonWidth +1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "German", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){
				if (isTappable()){
					if(!langSelect1){
						german1.setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
						newUser1.setActive(false);
						langSelect1 = true;
						pickedLang1 = "German";

						if(langSelect2){

							removeZones();
							sketch.initializeMainScreen(pickedLang1, pickedLang2);
						}
					}

					e.setHandled(tappableHandled);

				}
			}
		};

		german1.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
		german1.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
		german1.setGestureEnabled("TAP", true, true);
		german1.setDrawBorder(false);
		german1.setActive(false);
		client.addZone(german1);

		//*******************************************
		//             User 2
		//*******************************************
		english2 = new TextZone(applet.screenWidth/2-2*sketch.buttonWidth-2, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "English", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){
					if(!langSelect2){
						english2.setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
						newUser2.setActive(false);
						langSelect2 = true;
						pickedLang2 = "English";

						if(langSelect1){

							removeZones();
							sketch.initializeMainScreen(pickedLang1, pickedLang2);
						}
					}

					e.setHandled(tappableHandled);

				}
			}
		};

		english2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		english2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		english2.setGestureEnabled("TAP", true, true);
		english2.setDrawBorder(false);
		english2.rotate((float) Colours.PI);
		english2.setActive(false);
		client.addZone(english2);

		//French language Button
		french2 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "French", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){
					if(!langSelect2){
						french2.setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
						newUser2.setActive(false);
						langSelect2 = true;
						pickedLang2 = "French";

						if(langSelect1){

							removeZones();
							sketch.initializeMainScreen(pickedLang1, pickedLang2);
						}
					}

					e.setHandled(tappableHandled);

				}
			}
		};

		french2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		french2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		french2.setGestureEnabled("TAP", true, true);
		french2.setDrawBorder(false);
		french2.rotate((float) Colours.PI);
		french2.setActive(false);
		client.addZone(french2);

		//Portuguese language Button
		portuguese2 = new TextZone(applet.screenWidth/2, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Portuguese", sketch.textSize, "CENTER", "CENTER");

		portuguese2.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
		portuguese2.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
		portuguese2.setDrawBorder(false);
		portuguese2.rotate((float) Colours.PI);
		portuguese2.setActive(false);
		client.addZone(portuguese2);

		//Spanish language Button
		german2 = new TextZone(applet.screenWidth/2 + sketch.buttonWidth +1, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "German", sketch.textSize, "CENTER", "CENTER");

		german2.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
		german2.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
		german2.setDrawBorder(false);
		german2.rotate((float) Colours.PI);
		german2.setActive(false);
		client.addZone(german2);
	}
	/**
	 * Activate the language buttons
	 * @param user
	 */
	public void activateLanguageButtons(int user){
		//English language Button
		if(user == 1){
			english1.setActive(true);
			french1.setActive(true);
			portuguese1.setActive(true);
			german1.setActive(true);

		} else if (user == 2){
			english2.setActive(true);
			french2.setActive(true);
			portuguese2.setActive(true);
			german2.setActive(true);
		}
	}


	/**
	 * Removes all of the zones associated with the login screen
	 */
	public void removeZones(){
		english1.setActive(false);
		english2.setActive(false);
		french1.setActive(false);
		french2.setActive(false);
		german1.setActive(false);
		german2.setActive(false);
		portuguese1.setActive(false);
		portuguese2.setActive(false);


		german1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		german2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		portuguese1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		portuguese2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		french1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		french2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		english1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		english2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());

		client.removeZone(profilePicker.arrows[0]);
		client.removeZone(profilePicker.arrows[1]);
		client.removeZone(profilePicker.arrows[2]);
		client.removeZone(profilePicker.arrows[3]);
		client.removeZone(profilePicker.swipe1);
		client.removeZone(profilePicker.swipe2);

		for(Zone z: profilePicker.userImg){
			client.removeZone(z);
		}

		
		client.removeZone(profileCreation.profileImg1);
		client.removeZone(profileCreation.profileImg2);
		client.removeZone(profileCreation.drawUser1);
		client.removeZone(profileCreation.drawUser2);
		client.removeZone(newLang1);
		client.removeZone(lastLang1);
		client.removeZone(newLang2);
		client.removeZone(lastLang2);
		client.removeZone(newUser1);
		client.removeZone(newUser2);
		client.removeZone(login1);
		client.removeZone(login2);
		client.removeZone(cancel1);
		client.removeZone(cancel2);
	}

	/**
	 *  Resets the language options
	 */
	public void chooseNewLang(){
		english1.setActive(true);
		english2.setActive(true);
		french1.setActive(true);
		french2.setActive(true);
		german1.setActive(true);
		german2.setActive(true);
		portuguese1.setActive(true);
		portuguese2.setActive(true);


		langSelect1 = false;
		langSelect2 = false;
	}
}
