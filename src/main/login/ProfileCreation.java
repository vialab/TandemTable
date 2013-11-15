package main.login;

import main.Colours;
import main.MainSketch;
import processing.core.PApplet;
import processing.core.PImage;
import vialab.simpleMultiTouch.Touch;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.zones.ImageZone;
import vialab.simpleMultiTouch.zones.RotatableDrawZone;

/**
 * Class responsible for creating and managing the
 * avatar creation by the users.
 *
 */
public class ProfileCreation {
	PApplet applet;
	TouchClient client;
	MainSketch sketch;
	ProfilePicker profilePicker;
	LoginScreen loginScreen;
	RotatableDrawZone drawUser1, drawUser2;
	ImageZone profileImg1, profileImg2;

	/**
	 * Initializes the profile creation object
	 * 
	 * @param client
	 * @param sketch
	 * @param profilePicker
	 * @param loginScreen
	 */
	public ProfileCreation(TouchClient client, MainSketch sketch, ProfilePicker profilePicker, LoginScreen loginScreen){
		applet = TouchClient.getPApplet();
		this.sketch = sketch;
		this.client = client;
		this.profilePicker = profilePicker;
		this.loginScreen = loginScreen;

	}

	/**
	 * Creates the canvases for the users to draw their avatar
	 * @param user
	 */
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
						loadProfileImage(1, drawUser1.getImage());
						loginScreen.activateLanguageButtons(1);
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
						loadProfileImage(2, drawUser2.getImage());
						loginScreen.activateLanguageButtons(2);
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

	/**
	 * Load the user drawn image (user's avatar) into a ImageZone
	 * @param user
	 * @param img
	 */
	public void loadProfileImage(int user, PImage img){

		if(user == 1){
			if(profileImg1 == null){
				profileImg1 = new ImageZone(applet.screenWidth-2*profilePicker.imgSize, applet.screenHeight-2*profilePicker.imgSize, 2*profilePicker.imgSize, 2*profilePicker.imgSize);
				client.addZone(profileImg1);
			}
			profileImg1.setImage(img);
			profileImg1.setActive(true);

			//profileImg1.setImage(applet.loadImage("\\users\\images\\user" + (drawUser1.getFilesCount()) + ".png"));

			//loginScreen.cancel1.setActive(false);

		} else if (user == 2) {
			if(profileImg2 == null) {
				profileImg2 = new ImageZone(applet.screenWidth-2*profilePicker.imgSize, 0, 2*profilePicker.imgSize, 2*profilePicker.imgSize);
				//profileImg2 = new ImageZone(0, applet.getHeight()/2 + 10, 2*profilePicker.imgSize, 2*profilePicker.imgSize);
				profileImg2.rotate((float) Colours.PI);
				client.addZone(profileImg2);		
			}
			profileImg2.setImage(img);
			profileImg2.setActive(true);
			//.setImage(applet.loadImage("\\users\\images\\user" + (drawUser2.getFilesCount()) + ".png"));
			//loginScreen.cancel2.setActive(false);

		}

	}
}
