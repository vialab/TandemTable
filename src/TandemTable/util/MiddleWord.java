package TandemTable.util;

import java.awt.Color;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import com.memetix.mst.language.Language;

import processing.core.PConstants;
import vialab.simpleMultiTouch.events.TapAndHoldEvent;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.TextZone;
import TandemTable.Colours;
import TandemTable.Sketch;
import TandemTable.sections.activities.headlines.HeadlineGetter;
import TandemTable.sections.activities.twitter.TwitterGetter;

public class MiddleWord {

	Sketch sketch;
	int widthOffset;
	float tweetTextSize;
	int middleX, middleWidth;
	public RectZone middleZone;
	public Animator animMiddleZone;
	public String middleText = " ";
	int textSize;
	TwitterGetter tg;
	HeadlineGetter hg;
	public boolean started = false;
	int middleAnimTime = 800;
	
	// For selecting translation language
	TextZone[] transButtons1 = new TextZone[Sketch.NUM_LANGS];
	TextZone[] transButtons2 = new TextZone[Sketch.NUM_LANGS];
	boolean[] transFlags1 = new boolean[Sketch.NUM_LANGS];
	boolean[] transFlags2 = new boolean[Sketch.NUM_LANGS];
	Animator[] animT1, animT2;
	public RectZone fadedBack;
	
	boolean sameLang;
	
	public MiddleWord(final Sketch sketch, TwitterGetter tg, boolean sameLang) {
		this.sketch = sketch;
		this.tg = tg;
		this.sameLang = sameLang;
		createMiddleWord();
	}
	
	public MiddleWord(final Sketch sketch, HeadlineGetter hg, boolean sameLang) {
		this.sketch = sketch;
		this.hg = hg;
		this.sameLang = sameLang;
		createMiddleWord();
	}
	
	public void createMiddleWord() {
		widthOffset = (int) (0.20*(sketch.getWidth()-sketch.lineX));
		tweetTextSize = sketch.getHeight()/40;
		textSize = sketch.getHeight()/50;
		
		int width = (int) (sketch.getWidth()- sketch.lineX - 2*widthOffset);
		final int height = (int) (5.5*tweetTextSize);
		int x = sketch.lineX + widthOffset;
		middleX = x;
		middleWidth = width;

		middleZone = new RectZone(x, sketch.getHeight()/2-height/2, width/2, height, sketch.radius){
			public void drawZone(){
				super.drawZone();
				this.setX(middleX);
				this.setWidth(middleWidth);
				sketch.fill(0);
				sketch.textFont(Colours.pFont, tweetTextSize*2);
				sketch.textAlign(PConstants.LEFT, PConstants.BOTTOM);
				sketch.text(middleText, this.getX() + (this.getWidth() - sketch.textWidth(middleText))/2, this.getY() + 7*this.getHeight()/8);

				sketch.pushMatrix();
				sketch.translate(this.getX() + (this.getWidth() + sketch.textWidth(middleText))/2, this.getY() + this.getHeight()/8);
				sketch.rotate((float) Colours.PI);
				sketch.text(middleText, 0, 0);
				sketch.popMatrix();
				sketch.textFont(Colours.pFont, textSize);
			}

			public void tapEvent(TapEvent e){
				if(started){
					if(tg != null) {
						tg.translateMiddleWord();
					} else if(hg != null) {
						hg.translateMiddleWord();
					}
				} else {
					setMiddleText("Tap A Word!");
				}
				e.setHandled(true);
			}
			
			public void tapAndHoldEvent(TapAndHoldEvent e) {
				createTransButtons(height);	
				setGestureEnabled("TapAndHold", false);
			}
		};

		middleZone.setColour(Colours.boundingBox.getRed(), Colours.boundingBox.getGreen(), Colours.boundingBox.getBlue());
		middleZone.setShadow(true);
		middleZone.setShadowColour(Colours.shadow.getRed(), Colours.shadow.getGreen(), Colours.shadow.getBlue());
		middleZone.setStroke(false);
		middleZone.setDrawBorder(false);
		middleZone.setShadowX(-sketch.shadowOffset);
		middleZone.setShadowY(-sketch.shadowOffset);
		middleZone.setShadowW(2*sketch.shadowOffset);
		middleZone.setShadowH(2*sketch.shadowOffset);
		middleZone.setGestureEnabled("Tap", true);
		
		if(sameLang) {
			middleZone.setGestureEnabled("TapAndHold", true);
		}
		
		middleZone.setHoldDurationMilli(1000);
		setMiddleText(" ");
		sketch.client.addZone(middleZone);

		animMiddleZone = PropertySetter.createAnimator(middleAnimTime, middleZone, 
				"Colour", new ColourEval(), Colours.boundingBox, Color.ORANGE);


		animMiddleZone.setRepeatBehavior(RepeatBehavior.REVERSE);
		animMiddleZone.setRepeatCount(Animator.INFINITE);
	}
	
	public void setMiddleText(String str){
		middleText = str;
		int middleSpace = sketch.getWidth()/20;
		sketch.textFont(Colours.pFont, tweetTextSize*2);
		middleX = (int) (sketch.lineX + (sketch.getWidth()-sketch.lineX)/2 - sketch.textWidth(str)/2 - middleSpace);
		middleWidth = (int) (sketch.textWidth(str) + middleSpace*2);
	}
	
	public void createTransButtons(float height) {
		fadedBack = new RectZone(sketch.lineX, 0, sketch.getWidth() - sketch.lineX, sketch.getHeight());
		fadedBack.setDrawBorder(false);
		fadedBack.setColour(new Color(50, 50, 50, 150));
		sketch.client.addZone(fadedBack);
		
		float middle = sketch.lineX + (sketch.getWidth() - sketch.lineX)/2;
		float space = 10;
		animT1 = new Animator[Sketch.NUM_LANGS];
		animT2 = new Animator[Sketch.NUM_LANGS];
		
		transButtons1[0] = new TextZone(middle - sketch.buttonWidth - space*2, sketch.getHeight()/2 + height/2, sketch.buttonWidth/2, sketch.buttonHeight/2, sketch.radius, Colours.pFont, "English", sketch.textSize/2, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e) {
				tapTrans(0, 1);
			}
		};
		
		transButtons1[0].setColour(Colours.learner1TalkBar);
		transButtons1[0].setDrawBorder(false);
		transButtons1[0].setGestureEnabled("Tap", true);
		sketch.client.addZone(transButtons1[0]);
		
		animT1[0] = PropertySetter.createAnimator(sketch.mainSection.animationTime, transButtons1[0], 
				"colour", new ColourEval(), Colours.learner1TalkBar, Colours.selectedZone);

		animT1[0].setRepeatBehavior(RepeatBehavior.REVERSE);
		animT1[0].setRepeatCount(Animator.INFINITE);
		
		transButtons1[1] = new TextZone(middle - sketch.buttonWidth/2 - space, sketch.getHeight()/2 + height/2, sketch.buttonWidth/2, sketch.buttonHeight/2, sketch.radius, Colours.pFont, "French", sketch.textSize/2, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e) {
				tapTrans(1, 1);
			}
		};
		
		transButtons1[1].setColour(Colours.learner1TalkBar);
		transButtons1[1].setDrawBorder(false);
		transButtons1[1].setGestureEnabled("Tap", true);
		sketch.client.addZone(transButtons1[1]);
		
		animT1[1] = PropertySetter.createAnimator(sketch.mainSection.animationTime, transButtons1[1], 
				"colour", new ColourEval(), Colours.learner1TalkBar, Colours.selectedZone);

		animT1[1].setRepeatBehavior(RepeatBehavior.REVERSE);
		animT1[1].setRepeatCount(Animator.INFINITE);
		
		transButtons1[2] = new TextZone(middle + space, sketch.getHeight()/2 + height/2, sketch.buttonWidth/2, sketch.buttonHeight/2, sketch.radius, Colours.pFont, "Portuguese", sketch.textSize/2, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e) {
				tapTrans(2, 1);
			}
		};
		
		transButtons1[2].setColour(Colours.learner1TalkBar);
		transButtons1[2].setDrawBorder(false);
		transButtons1[2].setGestureEnabled("Tap", true);
		sketch.client.addZone(transButtons1[2]);
		
		animT1[2] = PropertySetter.createAnimator(sketch.mainSection.animationTime, transButtons1[2], 
				"colour", new ColourEval(), Colours.learner1TalkBar, Colours.selectedZone);

		animT1[2].setRepeatBehavior(RepeatBehavior.REVERSE);
		animT1[2].setRepeatCount(Animator.INFINITE);
		
		transButtons1[3] = new TextZone(middle + sketch.buttonWidth/2 + space*2, sketch.getHeight()/2 + height/2, sketch.buttonWidth/2, sketch.buttonHeight/2, sketch.radius, Colours.pFont, "Spanish", sketch.textSize/2, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e) {
				tapTrans(3, 1);
			}
		};
		
		transButtons1[3].setColour(Colours.learner1TalkBar);
		transButtons1[3].setDrawBorder(false);
		transButtons1[3].setGestureEnabled("Tap", true);
		sketch.client.addZone(transButtons1[3]);
		
		animT1[3] = PropertySetter.createAnimator(sketch.mainSection.animationTime, transButtons1[3], 
				"colour", new ColourEval(), Colours.learner1TalkBar, Colours.selectedZone);

		animT1[3].setRepeatBehavior(RepeatBehavior.REVERSE);
		animT1[3].setRepeatCount(Animator.INFINITE);
		
		transButtons2[0] = new TextZone(middle - sketch.buttonWidth - space*2, sketch.getHeight()/2 - height, sketch.buttonWidth/2, sketch.buttonHeight/2, sketch.radius, Colours.pFont, "English", sketch.textSize/2, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e) {
				tapTrans(0, 2);
			}
		};
		
		transButtons2[0].rotate((float) (Colours.PI));
		transButtons2[0].setColour(Colours.learner2TalkBar);
		transButtons2[0].setDrawBorder(false);
		transButtons2[0].setGestureEnabled("Tap", true);
		sketch.client.addZone(transButtons2[0]);
		
		animT2[0] = PropertySetter.createAnimator(sketch.mainSection.animationTime, transButtons2[0], 
				"colour", new ColourEval(), Colours.learner2TalkBar, Colours.selectedZone);

		animT2[0].setRepeatBehavior(RepeatBehavior.REVERSE);
		animT2[0].setRepeatCount(Animator.INFINITE);
		
		transButtons2[1] = new TextZone(middle - sketch.buttonWidth/2 - space, sketch.getHeight()/2 - height, sketch.buttonWidth/2, sketch.buttonHeight/2, sketch.radius, Colours.pFont, "French", sketch.textSize/2, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e) {
				tapTrans(1, 2);
			}
		};
		
		transButtons2[1].rotate((float) (Colours.PI));
		transButtons2[1].setColour(Colours.learner2TalkBar);
		transButtons2[1].setDrawBorder(false);
		transButtons2[1].setGestureEnabled("Tap", true);
		sketch.client.addZone(transButtons2[1]);
		
		animT2[1] = PropertySetter.createAnimator(sketch.mainSection.animationTime, transButtons2[1], 
				"colour", new ColourEval(), Colours.learner2TalkBar, Colours.selectedZone);

		animT2[1].setRepeatBehavior(RepeatBehavior.REVERSE);
		animT2[1].setRepeatCount(Animator.INFINITE);
		
		transButtons2[2] = new TextZone(middle + space, sketch.getHeight()/2 - height, sketch.buttonWidth/2, sketch.buttonHeight/2, sketch.radius, Colours.pFont, "Portuguese", sketch.textSize/2, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e) {
				tapTrans(2, 2);
			}
		};
		
		transButtons2[2].rotate((float) (Colours.PI));
		transButtons2[2].setColour(Colours.learner2TalkBar);
		transButtons2[2].setDrawBorder(false);
		transButtons2[2].setGestureEnabled("Tap", true);
		sketch.client.addZone(transButtons2[2]);
		
		animT2[2] = PropertySetter.createAnimator(sketch.mainSection.animationTime, transButtons2[2], 
				"colour", new ColourEval(), Colours.learner2TalkBar, Colours.selectedZone);

		animT2[2].setRepeatBehavior(RepeatBehavior.REVERSE);
		animT2[2].setRepeatCount(Animator.INFINITE);
		
		transButtons2[3] = new TextZone(middle  + sketch.buttonWidth/2 + space*2, sketch.getHeight()/2 - height, sketch.buttonWidth/2, sketch.buttonHeight/2, sketch.radius, Colours.pFont, "Spanish", sketch.textSize/2, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e) {
				tapTrans(3, 2);
			}
		};
		
		transButtons2[3].rotate((float) (Colours.PI));
		transButtons2[3].setColour(Colours.learner2TalkBar);
		transButtons2[3].setDrawBorder(false);
		transButtons2[3].setGestureEnabled("Tap", true);
		sketch.client.addZone(transButtons2[3]);
		
		animT2[3] = PropertySetter.createAnimator(sketch.mainSection.animationTime, transButtons2[3], 
				"colour", new ColourEval(), Colours.learner2TalkBar, Colours.selectedZone);

		animT2[3].setRepeatBehavior(RepeatBehavior.REVERSE);
		animT2[3].setRepeatCount(Animator.INFINITE);
		
		
	}
	
	public void tapTrans(int index, int user) {		
		for(int i = 0; i < Sketch.NUM_LANGS; i++) {
			if(index != i && (transFlags1[i] || transFlags2[i])) {
				animT2[i].stop();
				animT1[i].stop();
				transButtons1[i].setColour(Colours.learner1TalkBar);
				transButtons2[i].setColour(Colours.learner2TalkBar);

				transFlags1[i] = false;
				transFlags2[i] = false;
			}
		}
		
		if(user == 1) {
			if(transFlags2[index]){
				animT1[index].stop();
				transButtons1[index].setColour(Colours.learner1TalkBar);
				transButtons2[index].setColour(Colours.learner2TalkBar);
				setTransLang(index);
				removeTransButtons();
			} else if(!transFlags1[index]){
				animT2[index].start();
				transButtons1[index].setColour(Colours.selectedZone);
				transFlags1[index] = true;
			} else if(transFlags1[index]){
				animT2[index].stop();
				transButtons1[index].setColour(Colours.learner1TalkBar);
				transButtons2[index].setColour(Colours.learner2TalkBar);
				transFlags1[index] = false;
			}
		} else if(user == 2) {
			if(transFlags1[index]){
				animT2[index].stop();
				transButtons1[index].setColour(Colours.learner1TalkBar);
				transButtons2[index].setColour(Colours.learner2TalkBar);
				setTransLang(index);
				removeTransButtons();
			} else if(!transFlags2[index]){
				animT1[index].start();
				transButtons2[index].setColour(Colours.selectedZone);
				transFlags2[index] = true;
			} else if(transFlags2[index]){
				animT1[index].stop();
				transButtons1[index].setColour(Colours.learner1TalkBar);
				transButtons2[index].setColour(Colours.learner2TalkBar);
				transFlags2[index] = false;
			}
		}
	}
	
	public void setTransLang(int index) {
		if(index == 0) {
			sketch.transLanguage = Language.ENGLISH;
		} else if(index == 1) {
			sketch.transLanguage = Language.FRENCH;
		} else if(index == 2) {
			sketch.transLanguage = Language.PORTUGUESE;
		} else if(index == 3) {
			sketch.transLanguage = Language.SPANISH;
		}
		
		middleZone.setGestureEnabled("TapAndHold", true);
	}
	
	public void removeTransButtons() {
		sketch.client.removeZone(fadedBack);
		
		for(int i = 0; i < Sketch.NUM_LANGS; i++) {
			animT1[i].stop();
			animT2[i].stop();
			sketch.client.removeZone(transButtons1[i]);
			sketch.client.removeZone(transButtons2[i]);
			transFlags1[i] = false;
			transFlags2[i] = false;
		}
	}
}
