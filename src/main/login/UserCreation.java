package main.login;

import main.Colours;
import main.MainSketch;
import processing.core.PApplet;
import vialab.simpleMultiTouch.ImageZone;
import vialab.simpleMultiTouch.RotatableDrawZone;
import vialab.simpleMultiTouch.TextZone;
import vialab.simpleMultiTouch.Touch;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.Zone;
import vialab.simpleMultiTouch.events.TapEvent;

public class UserCreation {
	PApplet applet;
	TouchClient client;
	MainSketch sketch;
	UserProfilePicker profilePicker;
	LoginScreen loginScreen;

	Zone drawUser1, drawUser2, cancel1, cancel2;
	

	public UserCreation(TouchClient client, MainSketch sketch, UserProfilePicker profilePicker, LoginScreen loginScreen){
		applet = client.getParent();
		this.sketch = sketch;
		this.client = client;
		this.profilePicker = profilePicker;
		this.loginScreen = loginScreen;

	}

	public void createCancel(int user){
		if(user == 1){
			cancel1 = new TextZone(applet.getX()+1, applet.screenHeight - sketch.buttonHeight-2, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "Cancel", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (tappable){
						drawUser1.setActive(false);
						cancel1.setActive(false);
						loginScreen.login1.setActive(true);
						loginScreen.newUser1.setActive(true);
						e.setHandled(tappableHandled);

					}
				}
			};

			((TextZone) cancel1).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			((TextZone) cancel1).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
			cancel1.setGestureEnabled("TAP", true, true);
			cancel1.setDrawBorder(false);
			client.addZone(cancel1);
		} else {
			//New Help2 Button
			cancel2 = new TextZone(applet.getX() + 1, applet.getY() + 2, sketch.buttonWidth, sketch.buttonHeight, 
					sketch.radius, Colours.pFont, "Cancel", sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (tappable){
						drawUser2.setActive(false);
						cancel2.setActive(false);
						loginScreen.login2.setActive(true);
						loginScreen.newUser2.setActive(true);
						e.setHandled(tappableHandled);

					}
				}
			};

			cancel2.rotate((float) (Colours.PI));
			((TextZone) cancel2).setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			((TextZone) cancel2).setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
			cancel2.setGestureEnabled("TAP", true, true);
			cancel2.setDrawBorder(false);
			client.addZone(cancel2);
		}
	}

	public void createDrawUser(int user){

		int dX = applet.screenWidth/4;
		int dY1 = applet.screenHeight/2 + sketch.yOffset;
		int dWidth = applet.screenWidth/2;
		int dHeight = applet.screenHeight/2 - sketch.yOffset;
		int dY2 = applet.getY();


		if(user == 1){
			//Create a drawing zone for user1 to create his user image
			drawUser1 = new RotatableDrawZone(dX, dY1, dWidth, dHeight, Colours.pFont, 0, sketch.textSize, client){
				public boolean addTouch(Touch t){
					super.addTouch(t);
					if(((RotatableDrawZone) drawUser1).getSavedStatus() == true){
						drawUser1.setActive(false);
						loadDrawnImage(1);
					}
					return false;


				}

			};
			((RotatableDrawZone) drawUser1).setSaveDir("\\users\\images\\");
			drawUser1.setBorderWeight(5);
			client.addZone(drawUser1);
		} else {
			//Create a drawing zone for user2 to create his user image
			drawUser2 = new RotatableDrawZone(dX, dY2, dWidth, dHeight, Colours.pFont, 0, sketch.textSize, client){
				public boolean addTouch(Touch t){
					super.addTouch(t);

					if(((RotatableDrawZone) drawUser2).getSavedStatus() == true){
						drawUser2.setActive(false);
						loadDrawnImage(2);
					}
					return false;
				}
			};
			((RotatableDrawZone) drawUser2).setSaveDir("\\users\\images\\");
			drawUser2.setBorderWeight(5);
			((RotatableDrawZone) drawUser2).setFlipped(true);
			((RotatableDrawZone) drawUser2).rotate((float) Colours.PI);
			client.addZone(drawUser2);
		}
	}

	public void loadDrawnImage(int user){

		if(user == 1){

			if(profilePicker.userImg[3] == null){
				profilePicker.userImg[3] = new ImageZone(0, 0, 1, 1);
				client.addZone(profilePicker.userImg[3]);

			}

			profilePicker.userImg[3].setXYWH(applet.screenWidth-2*profilePicker.minSize, applet.screenHeight-2*profilePicker.minSize, 2*profilePicker.minSize, 2*profilePicker.minSize);
			((ImageZone) profilePicker.userImg[3]).setImage(applet.loadImage("\\users\\images\\user" + (((RotatableDrawZone) drawUser1).getFilesCount()) + ".png"));
			profilePicker.userImg[3].setGestureEnabled("TAP", false);
			profilePicker.userImg[3].setActive(true);

			client.removeZone(cancel1);

		} else if (user == 2) {
			if(profilePicker.userImg[3+7] == null){
				profilePicker.userImg[3+7] = new ImageZone(0, 0, 1, 1);
				client.addZone(profilePicker.userImg[3+7]);
				profilePicker.userImg[3+7].setXYWH(applet.screenWidth-2*profilePicker.minSize, 0, 2*profilePicker.minSize, 2*profilePicker.minSize);

			}

			profilePicker.userImg[3+7].rotate((float) Colours.PI);
			((ImageZone) profilePicker.userImg[3+7]).setImage(applet.loadImage("\\users\\images\\user" + (((RotatableDrawZone) drawUser2).getFilesCount()) + ".png"));
			profilePicker.userImg[3+7].setGestureEnabled("TAP", false);
			profilePicker.userImg[3+7].setActive(true);
			
			client.removeZone(cancel2);

		}

		
		loginScreen.selectLang(user);
	}
}
