package main.login;
import main.Colours;
import main.MainSketch;
import processing.core.PApplet;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.TextZone;
import vialab.simpleMultiTouch.zones.Zone;

/**
 * Class responsible for initializing the login screen
 *
 */
public class LoginScreen {
	PApplet applet;
	TouchClient client;
	MainSketch sketch;
	ProfilePicker profilePicker;
	UserCreation userCreation;



	boolean langSelect1 = false, langSelect2 = false;

	Zone english1, french1, portuguese1, spanish1, english2, french2, portuguese2, spanish2, 
	login1, login2, newUser1, newUser2, newLang1, lastLang1, newLang2, lastLang2, cancel1, cancel2;

	//Zone responsible for graphics rendered during title screen
	RectZone bkgZone;
	//Zones used in the title screen
	TextZone titleZone1, titleZone2;

	String pickedLang1, pickedLang2;

	int minSize;
	long swipeFlag = -1;

	public void initialize(TouchClient client, MainSketch sketch){
		applet = TouchClient.getPApplet();
		this.sketch = sketch;
		this.client = client;

		createLoadingScreen();
		createCancel();
		profilePicker = new ProfilePicker(client, sketch, this);
		userCreation = new UserCreation(client, sketch, profilePicker, this);

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
				if (getTappable()){
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
				if (getTappable()){
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
				if (getTappable()){
					
					if(userCreation.drawUser1 != null){userCreation.drawUser1.setActive(false);}
					
					cancel1.setActive(false);
					profilePicker.removeUserProfilesPicker(1);
					userCreation.loginScreen.login1.setActive(true);
					userCreation.loginScreen.newUser1.setActive(true);
					e.setHandled(tappableHandled);

				}
			}
		};

		((TextZone) cancel1).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		((TextZone) cancel1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		cancel1.setGestureEnabled("TAP", true, true);
		cancel1.setDrawBorder(false);
		cancel1.setActive(false);
		client.addZone(cancel1);

		cancel2 = new TextZone(applet.getX() + 1, applet.getY() + 2, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Cancel", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable()){
					if(userCreation.drawUser2 != null){userCreation.drawUser2.setActive(false);}
					
					cancel2.setActive(false);
					profilePicker.removeUserProfilesPicker(2);
					userCreation.loginScreen.login2.setActive(true);
					userCreation.loginScreen.newUser2.setActive(true);
					e.setHandled(tappableHandled);

				}
			}
		};

		cancel2.rotate((float) (Colours.PI));
		((TextZone) cancel2).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		((TextZone) cancel2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		cancel2.setGestureEnabled("TAP", true, true);
		cancel2.setDrawBorder(false);
		cancel2.setActive(false);
		client.addZone(cancel2);

	}

	public void createLoginButtons(){
		//*****************
		// User 1
		//*****************
		//Login button for user 1
		login1 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Login", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (getTappable()){

					login1.setActive(false);
					newUser1.setActive(false);
					profilePicker.activateProfilePicker(1);
					cancel1.setActive(true);
					e.setHandled(tappableHandled);
				}
			}
		};
		((TextZone) login1).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		((TextZone) login1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		login1.setGestureEnabled("Tap", true);

		login1.setDrawBorder(false);
		client.addZone(login1);

		newUser1 = new TextZone(applet.screenWidth/2 +1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "New User", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (getTappable()){
					login1.setActive(false);
					newUser1.setActive(false);
					cancel1.setActive(true);
					userCreation.createDrawUser(1);
					e.setHandled(tappableHandled);
				}
			}
		};
		((TextZone) newUser1).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		((TextZone) newUser1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
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

				if (getTappable()){
					login2.setActive(false);
					newUser2.setActive(false);
					profilePicker.activateProfilePicker(2);
					cancel2.setActive(true);
					e.setHandled(tappableHandled);
				}
			}
		};
		((TextZone) login2).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		((TextZone) login2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		login2.rotate((float) Colours.PI);
		login2.setGestureEnabled("Tap", true);
		login2.setDrawBorder(false);
		client.addZone(login2);

		newUser2 = new TextZone(applet.screenWidth/2+1, applet.screenHeight/2-2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "New User", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (getTappable()){
					login2.setActive(false);
					newUser2.setActive(false);
					cancel2.setActive(true);
					userCreation.createDrawUser(2);						
					e.setHandled(tappableHandled);
				}
			}
		};
		((TextZone) newUser2).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		((TextZone) newUser2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		newUser2.setGestureEnabled("Tap", true);

		newUser2.setDrawBorder(false);
		newUser2.rotate((float) Colours.PI);
		client.addZone(newUser2);
	}

	/**
	 * Creates the language option buttons
	 * @param user
	 */
	public void createLanguageOptions(int user){

		if(user == 1){

			newLang1 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "New Language", sketch.textSize, "CENTER", "CENTER"){
				public void tapEvent(TapEvent e){

					if (getTappable()){
						this.setGestureEnabled("Tap", false);
						newLang1.setActive(false);
						lastLang1.setActive(false);
						createLanguageButtons(1);
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

					if (getTappable()){
						this.setGestureEnabled("Tap", false);
						newLang1.setActive(false);
						lastLang1.setActive(false);
						profilePicker.readProfile(1);
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

			newLang2 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2-2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "New Language", sketch.textSize, "CENTER", "CENTER"){
				public void tapEvent(TapEvent e){

					if (getTappable()){
						newLang2.setActive(false);
						lastLang2.setActive(false);
						this.setGestureEnabled("Tap", false);
						createLanguageButtons(2);						
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

					if (getTappable()){
						newLang2.setActive(false);
						lastLang2.setActive(false);
						this.setGestureEnabled("Tap", false);
						profilePicker.readProfile(2);
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

	/**
	 * Create the language selection buttons
	 * @param user
	 */
	public void createLanguageButtons(int user){
		//English language Button
		if(user == 1){

			english1 = new TextZone(applet.screenWidth/2-2*sketch.buttonWidth-2, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "English", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (getTappable()){
						if(!langSelect1){
							((TextZone) english1).setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
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

			((TextZone) english1).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			((TextZone) english1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
			english1.setGestureEnabled("TAP", true, true);
			english1.setDrawBorder(false);
			client.addZone(english1);

			//French language Button
			french1 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "French", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (getTappable()){
						if(!langSelect1){
							((TextZone) french1).setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
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

			((TextZone) french1).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			((TextZone) french1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
			french1.setGestureEnabled("TAP", true, true);
			french1.setDrawBorder(false);
			client.addZone(french1);

			//Portuguese language Button
			portuguese1 = new TextZone(applet.screenWidth/2, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "Portuguese", sketch.textSize, "CENTER", "CENTER");

			((TextZone) portuguese1).setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
			((TextZone) portuguese1).setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
			portuguese1.setDrawBorder(false);
			client.addZone(portuguese1);

			//Spanish language Button
			spanish1 = new TextZone(applet.screenWidth/2 + sketch.buttonWidth +1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "Spanish", sketch.textSize, "CENTER", "CENTER");
			((TextZone) spanish1).setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
			((TextZone) spanish1).setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
			spanish1.setDrawBorder(false);
			client.addZone(spanish1);

		} else if (user == 2){

			english2 = new TextZone(applet.screenWidth/2-2*sketch.buttonWidth-2, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "English", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (getTappable()){
						if(!langSelect2){
							((TextZone) english2).setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
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

			((TextZone) english2).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			((TextZone) english2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
			english2.setGestureEnabled("TAP", true, true);
			english2.setDrawBorder(false);
			english2.rotate((float) Colours.PI);
			client.addZone(english2);

			//French language Button
			french2 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "French", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (getTappable()){
						if(!langSelect2){
							((TextZone) french2).setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
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

			((TextZone) french2).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			((TextZone) french2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
			french2.setGestureEnabled("TAP", true, true);
			french2.setDrawBorder(false);
			french2.rotate((float) Colours.PI);

			client.addZone(french2);

			//Portuguese language Button
			portuguese2 = new TextZone(applet.screenWidth/2, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "Portuguese", sketch.textSize, "CENTER", "CENTER");

			((TextZone) portuguese2).setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
			((TextZone) portuguese2).setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
			portuguese2.setDrawBorder(false);
			portuguese2.rotate((float) Colours.PI);

			client.addZone(portuguese2);

			//Spanish language Button
			spanish2 = new TextZone(applet.screenWidth/2 + sketch.buttonWidth +1, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "Spanish", sketch.textSize, "CENTER", "CENTER");

			((TextZone) spanish2).setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
			((TextZone) spanish2).setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
			spanish2.setDrawBorder(false);
			spanish2.rotate((float) Colours.PI);

			client.addZone(spanish2);
		}
	}


	public void removeZones(){

		english1.setActive(false);
		english2.setActive(false);
		french1.setActive(false);
		french2.setActive(false);
		spanish1.setActive(false);
		spanish2.setActive(false);
		portuguese1.setActive(false);
		portuguese2.setActive(false);


		((TextZone) spanish1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		((TextZone) spanish2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		((TextZone) portuguese1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		((TextZone) portuguese2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		((TextZone) french1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		((TextZone) french2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		((TextZone) english1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		((TextZone) english2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());

		client.removeZone(profilePicker.arrows[0]);
		client.removeZone(profilePicker.arrows[1]);
		client.removeZone(profilePicker.arrows[2]);
		client.removeZone(profilePicker.arrows[3]);
		client.removeZone(profilePicker.swipe1);
		client.removeZone(profilePicker.swipe2);

		for(Zone z: profilePicker.userImg){
			client.removeZone(z);
		}

		client.removeZone(newLang1);
		client.removeZone(lastLang1);
		client.removeZone(newLang2);
		client.removeZone(lastLang2);
		client.removeZone(newUser1);
		client.removeZone(newUser2);
		client.removeZone(userCreation.drawUser1);
		client.removeZone(userCreation.drawUser2);

	}

	public void chooseNewLang(){
		english1.setActive(true);
		english2.setActive(true);
		french1.setActive(true);
		french2.setActive(true);
		spanish1.setActive(true);
		spanish2.setActive(true);
		portuguese1.setActive(true);
		portuguese2.setActive(true);


		langSelect1 = false;
		langSelect2 = false;
	}
}
