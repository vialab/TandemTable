package Main.UserRegistration;

import Main.Colours;
import Main.MainSketch;
import processing.core.PApplet;
import vialab.simpleMultiTouch.TextZone;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.Zone;
import vialab.simpleMultiTouch.events.TapEvent;

public class UserSupport {
	PApplet applet;
	TouchClient client;
	MainSketch sketch;
	
	Zone help1, help2;
	
	public UserSupport(TouchClient client, MainSketch sketch){
		applet = client.getParent();
		this.sketch = sketch;
		this.client = client;
		createHelpButtons();
	}
	
	public void createHelpButtons(){

		//New Help1 Button
		help1 = new TextZone(applet.getX()+1, applet.screenHeight - 2*sketch.buttonHeight-2, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Help", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (tappable){
					//TODO
					e.setHandled(tappableHandled);

				}
			}
		};

		((TextZone) help1).setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
		((TextZone) help1).setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
		help1.setGestureEnabled("TAP", true, true);
		help1.setDrawBorder(false);
		client.addZone(help1);

		//New Help2 Button
		help2 = new TextZone(applet.getX() + 1, applet.getY() + sketch.buttonHeight + 2, sketch.buttonWidth, sketch.buttonHeight, 
				sketch.radius, Colours.pFont, "Help", sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (tappable){
					//TODO
					e.setHandled(tappableHandled);

				}
			}
		};

		help2.rotate((float) (Colours.PI));
		((TextZone) help2).setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
		((TextZone) help2).setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
		help2.setGestureEnabled("TAP", true, true);
		help2.setDrawBorder(false);
		client.addZone(help2);
	}
}
