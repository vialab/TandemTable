package Main.UserRegistration;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import processing.core.PApplet;
import vialab.simpleMultiTouch.RotatableDrawZone;
import vialab.simpleMultiTouch.TextZone;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.Zone;
import vialab.simpleMultiTouch.events.TapEvent;
import Main.Colours;
import Main.MainSketch;

public class LoginScreen {


	int minSize;

	long swipeFlag = -1;

	PApplet applet;
	TouchClient client;
	MainSketch sketch;
	UserProfilePicker profilePicker;
	UserCreation userCreation;

	FileWriter writer;
	BufferedWriter bWriter;

	boolean langSelect1 = false, langSelect2 = false;

	Zone english1, french1, portuguese1, spanish1, english2, french2, portuguese2, spanish2, 
		login1, login2, newUser1, newUser2;

	TextZone titleZone1, titleZone2;
	String pickedLang1, pickedLang2;


	public void initialize(TouchClient client, MainSketch sketch){
		applet = client.getParent();
		this.sketch = sketch;
		this.client = client;

		createLoadingScreen();
		profilePicker = new UserProfilePicker(client, sketch, this);
		userCreation = new UserCreation(client, sketch, profilePicker, this);

	}

	/**
	 * Create the title screen and load the profile images
	 */
	public void createLoadingScreen(){
		titleZone1 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "TandemTable", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (tappable){
					createLoginButtons();
					client.removeZone(this);
					client.removeZone(titleZone2);

					e.setHandled(tappableHandled);
				}
			}
		};
		
		titleZone1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		titleZone1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		titleZone1.setGestureEnabled("Tap", true);

		titleZone1.setDrawBorder(false);
		client.addZone(titleZone1);
		
		titleZone2 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "TandemTable", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (tappable){
					createLoginButtons();
					client.removeZone(this);
					client.removeZone(titleZone1);

					e.setHandled(tappableHandled);
				}
			}
		};
		
		titleZone2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		titleZone2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		titleZone2.setGestureEnabled("Tap", true);

		titleZone2.setDrawBorder(false);
		client.addZone(titleZone2);
		
		
		
	}
	
	public void createLoginButtons(){
		//*****************
		// User 1
		//*****************
		//Login button for user 1
		login1 = new TextZone(applet.screenWidth/2-sketch.buttonWidth-1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Login", sketch.textSize, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){

				if (tappable){
					
					login1.setActive(false);
					newUser1.setActive(false);
					profilePicker.activateProfilePicker(1);
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

				if (tappable){
					login1.setActive(false);
					newUser1.setActive(false);
					userCreation.createCancel(1);
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

				if (tappable){
					login2.setActive(false);
					newUser2.setActive(false);
					profilePicker.activateProfilePicker(2);
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

				if (tappable){
					login2.setActive(false);
					newUser2.setActive(false);
					userCreation.createCancel(2);
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


	public void selectLang(int user){
		//English language Button
		if(user == 1){
			
			english1 = new TextZone(applet.screenWidth/2-2*sketch.buttonWidth-2, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "English", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (tappable){
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
					if (tappable){
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
					if (tappable){
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
					if (tappable){
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

		client.removeZone(profilePicker.newLang1);
		client.removeZone(profilePicker.lastLang1);
		client.removeZone(profilePicker.newLang2);
		client.removeZone(profilePicker.lastLang2);
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
