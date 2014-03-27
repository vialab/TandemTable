package TandemTable.sections.login;
import TandemTable.Colours;
import TandemTable.Sketch;
import processing.core.PApplet;
import processing.core.PImage;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.ImageZone;
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
	public ProfileCreation profileCreation;

	boolean langSelect1 = false, langSelect2 = false;

	// If the learners have logged in and selected a language
	public boolean loggedIn = false;
	
	// Language buttons
	ImageZone english1, french1, portuguese1, spanish1, english2, french2, portuguese2, spanish2;
	// Language button text
	TextZone engText1, frenText1, portText1, spanText1, engText2, frenText2, portText2, spanText2;
	
	TextZone login1, login2, newUser1, newUser2, newLang1, lastLang1, newLang2, lastLang2, cancel1, cancel2;

	//Zone responsible for graphics rendered during title screen
	RectZone bkgZone;
	//Zones used in the title screen
	TextZone titleZone1, titleZone2;

	String pickedLang1, pickedLang2;

	int minSize;
	long swipeFlag = -1;
	
	// Language button images
	PImage engImg, frenchImg, portImg, spanImg;

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
		
		getFlagImages();

		createLoadingScreen();
		createCancel();
		profilePicker = new ProfilePicker(client, sketch, this);
		profileCreation = new ProfileCreation(client, sketch, profilePicker, this);
		createLanguageButtons();
		createNewLastLangButtons();

	}
	
	/**
	 * Loads the flag images for language buttons
	 */
	public void getFlagImages() {
		engImg = sketch.loadImage("england.jpg");
		frenchImg = sketch.loadImage("france.jpg");
		portImg = sketch.loadImage("brazil.jpg");
		spanImg = sketch.loadImage("spain.jpg");
	}

	/**
	 * Create the title screen and load the profile images
	 */
	public void createLoadingScreen(){
		final int circX1 = sketch.getWidth()/4;
		final int circX2 = (int) (sketch.getWidth()/1.4);
		final int circY = sketch.getHeight()/2;
		final int circDiam = (int) (sketch.getWidth()*1.1);

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

					if(profileCreation.drawUser1 != null){ 
						sketch.client.removeZone(profileCreation.drawUser1);
						profileCreation.saveImg1 = false;
					}

					cancel1.setActive(false);
					newLang1.setActive(false);
					lastLang1.setActive(false);

					deactivateLanguageButtons(1);
					resetLanguageColours(1);
					
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
		cancel1.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue());
		cancel1.setGestureEnabled("TAP", true, true);
		cancel1.setDrawBorder(false);
		cancel1.setActive(false);
		client.addZone(cancel1);

		cancel2 = new TextZone(applet.getX() + 1, applet.getY() + 2, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Cancel", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){
					if(profileCreation.drawUser2 != null){ 
						sketch.client.removeZone(profileCreation.drawUser2);
						profileCreation.saveImg2 = false;
					}

					cancel2.setActive(false);
					newLang2.setActive(false);
					lastLang2.setActive(false);

					deactivateLanguageButtons(2);
					resetLanguageColours(2);
					
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
		cancel2.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue());
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
		login1.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue());
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
		newUser1.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue());
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
		login2.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue());
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
		newUser2.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue());
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
		newLang1.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue());
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
		lastLang1.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue());
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
		newLang2.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue());
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
		lastLang2.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue());
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
	 * Language button tap action
	 */
	public void langTap(int user, String lang) {
		if(user == 1) {
			if(!langSelect1){
				
				if(lang.equalsIgnoreCase("English")) {
					english1.setFilterColor(Colours.languageFilter);
					english1.setFilterColFlag(true);
					engText1.setTextColour(Colours.languageFilter);
				} else if(lang.equalsIgnoreCase("French")) {
					french1.setFilterColor(Colours.languageFilter);
					french1.setFilterColFlag(true);
					frenText1.setTextColour(Colours.languageFilter);
				} else if(lang.equalsIgnoreCase("Portuguese")) {
					portuguese1.setFilterColor(Colours.languageFilter);
					portuguese1.setFilterColFlag(true);
					portText1.setTextColour(Colours.languageFilter);
				} else if(lang.equalsIgnoreCase("Spanish")) {
					spanish1.setFilterColor(Colours.languageFilter);
					spanish1.setFilterColFlag(true);
					spanText1.setTextColour(Colours.languageFilter);
				}
				
				
				newUser1.setActive(false);
				langSelect1 = true;
				pickedLang1 = lang;

				if(langSelect2){
					loggedIn = true;
					removeZones();
					sketch.initializeMainScreen(pickedLang1, pickedLang2);
				}
			}

		} else if(user == 2) {
			if(!langSelect2){
				if(lang.equalsIgnoreCase("English")) {
					english2.setFilterColor(Colours.languageFilter);
					english2.setFilterColFlag(true);
					engText2.setTextColour(Colours.languageFilter);
				} else if(lang.equalsIgnoreCase("French")) {
					french2.setFilterColor(Colours.languageFilter);
					french2.setFilterColFlag(true);
					frenText2.setTextColour(Colours.languageFilter);
				} else if(lang.equalsIgnoreCase("Portuguese")) {
					portuguese2.setFilterColor(Colours.languageFilter);
					portuguese2.setFilterColFlag(true);
					portText2.setTextColour(Colours.languageFilter);
				} else if(lang.equalsIgnoreCase("Spanish")) {
					spanish2.setFilterColor(Colours.languageFilter);
					spanish2.setFilterColFlag(true);
					spanText2.setTextColour(Colours.languageFilter);
				}
				
				
				newUser2.setActive(false);
				langSelect2 = true;
				pickedLang2 = lang;

				if(langSelect1){
					loggedIn = true;
					removeZones();
					sketch.initializeMainScreen(pickedLang1, pickedLang2);
				}
			}
		}
	}
	/**
	 * Create the language selection buttons
	 */
	public void createLanguageButtons(){
		//*******************************************
		//             User 1
		//*******************************************
		english1 = new ImageZone(engImg, applet.screenWidth/2-2*sketch.buttonWidth-2, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight) {
			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(1, "English");
				}
				e.setHandled(tappableHandled);
			}
		};
		
		english1.setGestureEnabled("TAP", true, true);
		english1.setDrawBorder(false);
		english1.setActive(false);
		client.addZone(english1);
		
		engText1 = new TextZone(applet.screenWidth/2-2*sketch.buttonWidth-2, applet.screenHeight/2+sketch.buttonHeight*2, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "English", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(1, "English");
				}
				e.setHandled(tappableHandled);
			}
		};

		engText1.setTextColour(Colours.languageText.getRed(), Colours.languageText.getGreen(), Colours.languageText.getBlue());
		engText1.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue(), 0);
		engText1.setGestureEnabled("TAP", true, true);
		engText1.setDrawBorder(false);
		engText1.setActive(false);
		client.addZone(engText1);

		//French language Button
		french1 = new ImageZone(frenchImg, applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight) {
			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(1, "French");
				}
				e.setHandled(tappableHandled);
			}
		};
		
		french1.setGestureEnabled("TAP", true, true);
		french1.setDrawBorder(false);
		french1.setActive(false);
		client.addZone(french1);
		
		frenText1 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight*2, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "French", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(1, "French");
				}
				e.setHandled(tappableHandled);
			}
		};

		frenText1.setTextColour(Colours.languageText.getRed(), Colours.languageText.getGreen(), Colours.languageText.getBlue());
		frenText1.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue(), 0);
		frenText1.setGestureEnabled("TAP", true, true);
		frenText1.setDrawBorder(false);
		frenText1.setActive(false);
		client.addZone(frenText1);

		//Portuguese language Button
		portuguese1 = new ImageZone(portImg, applet.screenWidth/2, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight) { 
			
			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(1, "Portuguese");
				}
				e.setHandled(tappableHandled);
			}
		};

		portuguese1.setGestureEnabled("TAP", true, true);
		portuguese1.setDrawBorder(false);
		portuguese1.setActive(false);
		client.addZone(portuguese1);
		
		portText1 = new TextZone(applet.screenWidth/2, applet.screenHeight/2+sketch.buttonHeight*2, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Portuguese", sketch.textSize, "CENTER", "CENTER") {
			
			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(1, "Portuguese");
				}
				e.setHandled(tappableHandled);
			}
		};

		portText1.setTextColour(Colours.languageText.getRed(), Colours.languageText.getGreen(), Colours.languageText.getBlue());
		portText1.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue(), 0);
		portText1.setGestureEnabled("TAP", true, true);
		portText1.setDrawBorder(false);
		portText1.setActive(false);
		client.addZone(portText1);

		//Spanish language Button
		spanish1 = new ImageZone(spanImg, applet.screenWidth/2 + sketch.buttonWidth +1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight) { 
			
			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(1, "Spanish");
				}
				e.setHandled(tappableHandled);
			}
		};

		spanish1.setGestureEnabled("TAP", true, true);
		spanish1.setDrawBorder(false);
		spanish1.setActive(false);
		client.addZone(spanish1);
		
		spanText1 = new TextZone(applet.screenWidth/2 + sketch.buttonWidth +1, applet.screenHeight/2+sketch.buttonHeight*2, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Spanish", sketch.textSize, "CENTER", "CENTER"){
			
			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(1, "Spanish");
				}
				e.setHandled(tappableHandled);
			}
		};

		spanText1.setTextColour(Colours.languageText.getRed(), Colours.languageText.getGreen(), Colours.languageText.getBlue());
		spanText1.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue(), 0);
		spanText1.setGestureEnabled("TAP", true, true);
		spanText1.setDrawBorder(false);
		spanText1.setActive(false);
		client.addZone(spanText1);

		//*******************************************
		//             User 2
		//*******************************************
		english2 = new ImageZone(engImg, applet.screenWidth/2-2*sketch.buttonWidth-2, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight) {
			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(2, "English");
				}
				e.setHandled(tappableHandled);
			}
		};
		
		english2.setGestureEnabled("TAP", true, true);
		english2.setDrawBorder(false);
		english2.rotate((float) Colours.PI);
		english2.setActive(false);
		client.addZone(english2);
		
		
		engText2 = new TextZone(applet.screenWidth/2-2*sketch.buttonWidth-2, applet.screenHeight/2 - 3*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "English", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(2, "English");
				}
				e.setHandled(tappableHandled);
			}
		};

		engText2.setTextColour(Colours.languageText.getRed(), Colours.languageText.getGreen(), Colours.languageText.getBlue());
		engText2.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue(), 0);
		engText2.setGestureEnabled("TAP", true, true);
		engText2.setDrawBorder(false);
		engText2.rotate((float) Colours.PI);
		engText2.setActive(false);
		client.addZone(engText2);

		//French language Button
		french2 = new ImageZone(frenchImg, applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight) { 

			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(2, "French");
				}
				e.setHandled(tappableHandled);
			}
		};

		french2.setGestureEnabled("TAP", true, true);
		french2.setDrawBorder(false);
		french2.rotate((float) Colours.PI);
		french2.setActive(false);
		client.addZone(french2);
		
		frenText2 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2 - 3*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "French", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(2, "French");
				}
				e.setHandled(tappableHandled);
			}
		};

		frenText2.setTextColour(Colours.languageText.getRed(), Colours.languageText.getGreen(), Colours.languageText.getBlue());
		frenText2.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue(), 0);
		frenText2.setGestureEnabled("TAP", true, true);
		frenText2.setDrawBorder(false);
		frenText2.rotate((float) Colours.PI);
		frenText2.setActive(false);
		client.addZone(frenText2);

		//Portuguese language Button
		portuguese2 = new ImageZone(portImg, applet.screenWidth/2, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight) {
			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(2, "Portuguese");
				}
				e.setHandled(tappableHandled);
			}
		};

		portuguese2.setDrawBorder(false);
		portuguese2.setGestureEnabled("TAP", true, true);
		portuguese2.rotate((float) Colours.PI);
		portuguese2.setActive(false);
		client.addZone(portuguese2);
		
		portText2 = new TextZone(applet.screenWidth/2, applet.screenHeight/2 - 3*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Portuguese", sketch.textSize, "CENTER", "CENTER") {
			
			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(2, "Portuguese");
				}
				e.setHandled(tappableHandled);
			}
		};

		portText2.setTextColour(Colours.languageText.getRed(), Colours.languageText.getGreen(), Colours.languageText.getBlue());
		portText2.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue(), 0);
		portText2.setDrawBorder(false);
		portText2.setGestureEnabled("TAP", true, true);
		portText2.rotate((float) Colours.PI);
		portText2.setActive(false);
		client.addZone(portText2);

		//Spanish language Button
		spanish2 = new ImageZone(spanImg, applet.screenWidth/2 + sketch.buttonWidth +1, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight) {
			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(2, "Spanish");
				}
				e.setHandled(tappableHandled);
			}
		};
		
		spanish2.setDrawBorder(false);
		spanish2.setGestureEnabled("TAP", true, true);
		spanish2.rotate((float) Colours.PI);
		spanish2.setActive(false);
		client.addZone(spanish2);
		
		spanText2 = new TextZone(applet.screenWidth/2 + sketch.buttonWidth +1, applet.screenHeight/2 - 3*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Spanish", sketch.textSize, "CENTER", "CENTER") {
			public void tapEvent(TapEvent e){
				if (isTappable()){
					langTap(2, "Spanish");
				}
				e.setHandled(tappableHandled);
			}
		};
		
		spanText2.setTextColour(Colours.languageText.getRed(), Colours.languageText.getGreen(), Colours.languageText.getBlue());
		spanText2.setColour(Colours.loginBColor.getRed(), Colours.loginBColor.getGreen(), Colours.loginBColor.getBlue(), 0);
		spanText2.setDrawBorder(false);
		spanText2.setGestureEnabled("TAP", true, true);
		spanText2.rotate((float) Colours.PI);
		spanText2.setActive(false);
		client.addZone(spanText2);
	}
	
	
	public void deactivateLanguageButtons(int user) {
		if(user == 1){
			english1.setActive(false);
			french1.setActive(false);
			portuguese1.setActive(false);
			spanish1.setActive(false);
			
			engText1.setActive(false);
			frenText1.setActive(false);
			spanText1.setActive(false);
			portText1.setActive(false);

		} else if (user == 2){
			english2.setActive(false);
			french2.setActive(false);
			portuguese2.setActive(false);
			spanish2.setActive(false);
			
			engText2.setActive(false);
			frenText2.setActive(false);
			spanText2.setActive(false);
			portText2.setActive(false);
		}
	}
	
	/**
	 * Activate the language buttons
	 * @param user
	 */
	public void activateLanguageButtons(int user){
		if(user == 1){
			english1.setActive(true);
			french1.setActive(true);
			portuguese1.setActive(true);
			spanish1.setActive(true);
			
			engText1.setActive(true);
			frenText1.setActive(true);
			spanText1.setActive(true);
			portText1.setActive(true);

		} else if (user == 2){
			english2.setActive(true);
			french2.setActive(true);
			portuguese2.setActive(true);
			spanish2.setActive(true);
			
			engText2.setActive(true);
			frenText2.setActive(true);
			spanText2.setActive(true);
			portText2.setActive(true);
		}
	}

	
	// Resets the colours of the language buttons
	public void resetLanguageColours(int user) {
		if(user == 1){
			spanish1.setFilterColFlag(false);
			portuguese1.setFilterColFlag(false);
			french1.setFilterColFlag(false);
			english1.setFilterColFlag(false);
			
			engText1.setTextColour(Colours.languageText);
			frenText1.setTextColour(Colours.languageText);
			portText1.setTextColour(Colours.languageText);
			spanText1.setTextColour(Colours.languageText);
		} else if(user == 2) {
			spanish2.setFilterColFlag(false);
			portuguese2.setFilterColFlag(false);
			french2.setFilterColFlag(false);
			english2.setFilterColFlag(false);
			
			engText2.setTextColour(Colours.languageText);
			frenText2.setTextColour(Colours.languageText);
			portText2.setTextColour(Colours.languageText);
			spanText2.setTextColour(Colours.languageText);
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
		spanish1.setActive(false);
		spanish2.setActive(false);
		portuguese1.setActive(false);
		portuguese2.setActive(false);
		
		engText1.setActive(false);
		engText2.setActive(false);
		frenText1.setActive(false);
		frenText2.setActive(false);
		spanText1.setActive(false);
		spanText2.setActive(false);
		portText1.setActive(false);
		portText2.setActive(false);

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
		resetLanguageColours(1);
		resetLanguageColours(2);
		activateLanguageButtons(1);
		activateLanguageButtons(2);
		
		//english1.pullToTop();
		

		//sketch.mainSection.leftCenterLineFlag = true;
		sketch.mainSection.centerLineFlag = true;
		
		langSelect1 = false;
		langSelect2 = false;
		
		sketch.mainLogger.log("New Language");
	}
}
