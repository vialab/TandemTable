package Main.UserRegistration;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import processing.core.PApplet;
import vialab.simpleMultiTouch.TextZone;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.Zone;
import vialab.simpleMultiTouch.events.TapEvent;
import Main.Colours;
import Main.MainSketch;

public class UserRegistration {


	int minSize;

	long swipeFlag = -1;

	PApplet applet;
	TouchClient client;
	MainSketch sketch;
	UserProfilePicker profilePicker;
	UserSupport userSupport;
	UserCreation userCreation;

	FileWriter writer;
	BufferedWriter bWriter;

	boolean langSelect1 = false, langSelect2 = false;

	Zone english1, french1, portuguese1, spanish1, english2, french2, portuguese2, spanish2;

	String pickedLang1, pickedLang2;


	public void initialize(TouchClient client, MainSketch sketch){
		applet = client.getParent();
		this.sketch = sketch;
		this.client = client;

		profilePicker = new UserProfilePicker(client, sketch, this);
		userCreation = new UserCreation(client, sketch, profilePicker, this);
		userSupport = new UserSupport(client, sketch);

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
							userCreation.newUser1.setActive(false);
							userSupport.help1.setActive(false);
							langSelect1 = true;
							pickedLang1 = "English";

							if(langSelect2){
								removeZones();
								enterMainScreen();
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
							userCreation.newUser1.setActive(false);
							userSupport.help1.setActive(false);
							langSelect1 = true;
							pickedLang1 = "French";

							if(langSelect2){
								
								removeZones();
								enterMainScreen();
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
					sketch.radius, Colours.pFont, "Portuguese", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (tappable){
						if(!langSelect1){
							((TextZone) portuguese1).setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
							userCreation.newUser1.setActive(false);
							userSupport.help1.setActive(false);
							langSelect1 = true;
							pickedLang1 = "German";

							if(langSelect2){
								
								removeZones();
								enterMainScreen();
							}
						}

						e.setHandled(tappableHandled);

					}
				}
			};

			((TextZone) portuguese1).setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
			((TextZone) portuguese1).setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
			//TODO
			//portuguese1.setGestureEnabled("TAP", true, true);
			portuguese1.setDrawBorder(false);
			client.addZone(portuguese1);

			//Spanish language Button
			spanish1 = new TextZone(applet.screenWidth/2 + sketch.buttonWidth +1, applet.screenHeight/2+sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "Spanish", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (tappable){
						if(!langSelect1){
							((TextZone) spanish1).setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
							userCreation.newUser1.setActive(false);
							userSupport.help1.setActive(false);
							langSelect1 = true;
							pickedLang1 = "Spanish";

							if(langSelect2){
								
								removeZones();
								enterMainScreen();
							}
						}

						e.setHandled(tappableHandled);

					}
				}
			};

			((TextZone) spanish1).setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
			((TextZone) spanish1).setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
			//TODO
			//spanish1.setGestureEnabled("TAP", true, true);
			spanish1.setDrawBorder(false);
			client.addZone(spanish1);
		} else if (user == 2){

			english2 = new TextZone(applet.screenWidth/2-2*sketch.buttonWidth-2, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "English", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (tappable){
						if(!langSelect2){
							((TextZone) english2).setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
							userCreation.newUser2.setActive(false);
							userSupport.help2.setActive(false);
							langSelect2 = true;
							pickedLang2 = "English";

							if(langSelect1){
								
								removeZones();
								enterMainScreen();
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
							userCreation.newUser2.setActive(false);
							userSupport.help2.setActive(false);
							langSelect2 = true;
							pickedLang2 = "French";

							if(langSelect1){
								
								removeZones();
								enterMainScreen();
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
					sketch.radius, Colours.pFont, "Portuguese", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (tappable){
						if(!langSelect2){
							((TextZone) portuguese2).setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
							userCreation.newUser2.setActive(false);
							userSupport.help2.setActive(false);
							langSelect2 = true;
							pickedLang2 = "German";

							if(langSelect1){
								
								removeZones();
								enterMainScreen();
							}
						}

						e.setHandled(tappableHandled);

					}
				}
			};

			((TextZone) portuguese2).setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
			((TextZone) portuguese2).setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
			//TODO
			//portuguese2.setGestureEnabled("TAP", true, true);
			portuguese2.setDrawBorder(false);
			portuguese2.rotate((float) Colours.PI);

			client.addZone(portuguese2);

			//Spanish language Button
			spanish2 = new TextZone(applet.screenWidth/2 + sketch.buttonWidth +1, applet.screenHeight/2 - 2*sketch.buttonHeight, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "Spanish", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (tappable){
						if(!langSelect2){
							((TextZone) spanish2).setColour(Colours.selectedZone.getRed(), Colours.selectedZone.getGreen(), Colours.selectedZone.getBlue());
							userCreation.newUser2.setActive(false);
							userSupport.help2.setActive(false);
							langSelect2 = true;
							pickedLang2 = "Spanish";

							if(langSelect1){
								
								removeZones();
								enterMainScreen();
							}
						}

						e.setHandled(tappableHandled);

					}
				}
			};

			((TextZone) spanish2).setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
			((TextZone) spanish2).setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
			//TODO
			//spanish2.setGestureEnabled("TAP", true, true);
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
		client.removeZone(userCreation.newUser1);
		client.removeZone(userCreation.newUser2);
		client.removeZone(userCreation.drawUser1);
		client.removeZone(userCreation.drawUser2);

		client.removeZone(userSupport.help1);
		client.removeZone(userSupport.help2);
	}

	public void enterMainScreen(){

		try {
			writer = new FileWriter(".\\data\\users\\info\\" + profilePicker.chosenProfile1 + ".user");
			bWriter = new BufferedWriter(writer);
			bWriter.write(pickedLang1);
			bWriter.newLine();
			bWriter.write(Integer.toString(profilePicker.index1));
			bWriter.flush();
			bWriter.close();
			writer.close();
			
			writer = new FileWriter(".\\data\\users\\info\\" + profilePicker.chosenProfile2 + ".user");
			bWriter = new BufferedWriter(writer);
			bWriter.write(pickedLang2);
			bWriter.newLine();
			bWriter.write(Integer.toString(profilePicker.index2));
			bWriter.flush();
			bWriter.close();
			writer.close();
			
			sketch.initializeMainScreen(pickedLang1, pickedLang2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
