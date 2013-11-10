package Main.UserRegistration;


import Main.Colours;
import Main.MainSketch;
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
	UserRegistration userRegistration;
	
	Zone drawUser1, drawUser2;
		
	public UserCreation(TouchClient client, MainSketch sketch, UserProfilePicker profilePicker, UserRegistration userRegistration){
		applet = client.getParent();
		this.sketch = sketch;
		this.client = client;
		this.profilePicker = profilePicker;
		this.userRegistration = userRegistration;

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
			

		}

		userRegistration.selectLang(user);
	}
}
