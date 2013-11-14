package main.login;

import main.Colours;
import main.MainSketch;
import processing.core.PApplet;
import processing.core.PImage;
import vialab.simpleMultiTouch.Touch;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.zones.ImageZone;
import vialab.simpleMultiTouch.zones.RotatableDrawZone;

public class UserCreation {
	PApplet applet;
	TouchClient client;
	MainSketch sketch;
	ProfilePicker profilePicker;
	LoginScreen loginScreen;
	RotatableDrawZone drawUser1, drawUser2;

	public UserCreation(TouchClient client, MainSketch sketch, ProfilePicker profilePicker, LoginScreen loginScreen){
		applet = TouchClient.getPApplet();
		this.sketch = sketch;
		this.client = client;
		this.profilePicker = profilePicker;
		this.loginScreen = loginScreen;

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
					if(drawUser1.getCapturedStatus() == true){
						drawUser1.setActive(false);
						loadDrawnImage(1, drawUser1.getImage());
					}
					return false;


				}

			};
			drawUser1.setSaveDir("\\users\\images\\");
			drawUser1.setBorderWeight(5);
			client.addZone(drawUser1);
		} else {
			//Create a drawing zone for user2 to create his user image
			drawUser2 = new RotatableDrawZone(dX, dY2, dWidth, dHeight, Colours.pFont, 0, sketch.textSize, client){
				public boolean addTouch(Touch t){
					super.addTouch(t);

					if(drawUser2.getCapturedStatus() == true){
						drawUser2.setActive(false);
						loadDrawnImage(2, drawUser2.getImage());
					}
					return false;
				}
			};
			drawUser2.setSaveDir("\\users\\images\\");
			drawUser2.setBorderWeight(5);
			drawUser2.setFlipped(true);
			drawUser2.rotate((float) Colours.PI);
			client.addZone(drawUser2);
		}
	}

	public void loadDrawnImage(int user, PImage img){

		if(user == 1){

			if(profilePicker.userImg[3] == null){
				profilePicker.userImg[3] = new ImageZone(0, 0, 1, 1);
				client.addZone(profilePicker.userImg[3]);
			}

			profilePicker.userImg[3].setXYWH(applet.screenWidth-2*profilePicker.minSize, applet.screenHeight-2*profilePicker.minSize, 2*profilePicker.minSize, 2*profilePicker.minSize);
			profilePicker.userImg[3].setImage(img);
			//profilePicker.userImg[3].setImage(applet.loadImage("\\users\\images\\user" + (drawUser1.getFilesCount()) + ".png"));
			profilePicker.userImg[3].setGestureEnabled("TAP", false);
			profilePicker.userImg[3].setActive(true);

			loginScreen.cancel1.setActive(false);

		} else if (user == 2) {
			if(profilePicker.userImg[3 + 7] == null){
				profilePicker.userImg[3 + 7] = new ImageZone(0, 0, 1, 1);
				client.addZone(profilePicker.userImg[3 + 7]);
				//profilePicker.userImg[3 + 7].setXYWH(applet.screenWidth-2*profilePicker.minSize, 0, 2*profilePicker.minSize, 2*profilePicker.minSize);

			}
			//profilePicker.userImg[3 + 7].setXYWH(applet.screenWidth-2*profilePicker.minSize, 0, 2*profilePicker.minSize, 2*profilePicker.minSize);
			profilePicker.userImg[3 + 7].setXYWH(0, applet.getHeight()/2 + 10, 2*profilePicker.minSize, 2*profilePicker.minSize);

			//profilePicker.userImg[3 + 7].rotate((float) Colours.PI);
			profilePicker.userImg[3 + 7].setImage(img);
			//((ImageZone) profilePicker.userImg[3 + profilePicker.IMG_ONSCR]).setImage(applet.loadImage("\\users\\images\\user" + (drawUser2.getFilesCount()) + ".png"));
			profilePicker.userImg[3 + 7].setGestureEnabled("TAP", false);
			profilePicker.userImg[3 + 7].setActive(true);
			
			loginScreen.cancel2.setActive(false);

		}

		
		loginScreen.createLanguageButtons(user);
	}
}
