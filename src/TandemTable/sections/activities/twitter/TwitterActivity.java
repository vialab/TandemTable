package TandemTable.sections.activities.twitter;



import java.awt.Color;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import processing.core.PConstants;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.events.VSwipeEvent;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.TextZone;
import TandemTable.ColourEval;
import TandemTable.Colours;
import TandemTable.Languages;
import TandemTable.Sketch;
import TandemTable.sections.mainSection.MainSection;

import com.memetix.mst.language.Language;

public class TwitterActivity {
	TouchClient client;
	Sketch sketch;
	MainSection layoutManager;
	VideoController videoController;

	boolean errorFlag = false;
	boolean started = false;
	boolean canceled = false;

	int spaceX;
	int tweetIndex1 = 0;
	int tweetIndex2 = 0;
	int topicIndex;
	int widthOffset;
	int textYOffset;
	int tweetTextSize;
	int width;
	int height;
	int x;
	int middleX;
	int middleWidth;
	int lastUser = 0;
	
	int pageOffset1 = 1;
	int pageOffset2 = 1;
	
	Color[][] contentVisual1, contentVisual2;


	String topic1 = "";
	String topic2 = "";
	String langQuery1 ="";
	String langQuery2 ="";
	String tweetsS1 = "";
	String tweetsS2 = "";
	String middleText = " ";
	String selectedWord, topicExpanded1, topicExpanded2;


	TextZone tweetWord1, tweetWord2;
	TextZone[][] tweetZones1, tweetZones2;
	TextZone lastHighlightZone;
	RectZone[] background1, background2;
	RectZone middleTweet, swipeBackground1, swipeBackground2;

	TwitterGetter tg;
	Language langTranslate1, langTranslate2;

	final int NUM_TWEETS = 2;
	final int MAX_TWEETS = 100;

	Animator animMiddleTweet;
	Animator animTweet1;
	Animator animTweet2;
	
	String lang1, lang2;

	public TwitterActivity(Sketch sketch, MainSection layoutManager, int topicIndex, String lang1, String lang2){
		this.client = sketch.client;
		this.sketch = sketch;
		this.layoutManager = layoutManager;
		spaceX = sketch.getWidth() - sketch.lineX;
		this.topicIndex = topicIndex;
		this.lang1 = lang1;
		this.lang2 = lang2;



		if(lang1.equalsIgnoreCase("English")){
			langQuery1 = "en";// lang:en";
			tweetsS1 = Languages.tweetWordE;
			topic1 = Languages.topicsE[topicIndex];
			langTranslate1 = Language.ENGLISH;

			topicExpanded1 = "";
			String[] scrambled = sketch.scrambleStrings(Languages.topicsExpandedE[topicIndex]);

			for(String s: scrambled){
				topicExpanded1 += s + " OR ";
			}

			topicExpanded1 = topicExpanded1.substring(0, topicExpanded1.length() - 4);
		} else if(lang1.equalsIgnoreCase("French")){
			langQuery1 = "fr";//" lang:fr";
			tweetsS1 = Languages.tweetWordF;
			topic1 = Languages.topicsF[topicIndex];
			langTranslate1 = Language.FRENCH;

			topicExpanded1 = "";

			String[] scrambled = sketch.scrambleStrings(Languages.topicsExpandedF[topicIndex]);

			for(String s: scrambled){
				topicExpanded1 += s + " OR ";
			}

			topicExpanded1 = topicExpanded1.substring(0, topicExpanded1.length() - 4);
		}

		if(lang2.equalsIgnoreCase("English")){
			langQuery2 = "en";// lang:en";
			tweetsS2 = Languages.tweetWordE;
			topic2 = Languages.topicsE[topicIndex];
			langTranslate2 = Language.ENGLISH;

			topicExpanded2 = "";
			String[] scrambled = sketch.scrambleStrings(Languages.topicsExpandedE[topicIndex]);

			for(String s: scrambled){
				topicExpanded2 += s + " OR ";
			}


			topicExpanded2 = topicExpanded2.substring(0, topicExpanded2.length() - 4);

		} else if(lang2.equalsIgnoreCase("French")){
			langQuery2 = "fr";//" lang:fr";
			tweetsS2 = Languages.tweetWordF;
			topic2 = Languages.topicsF[topicIndex];
			langTranslate2 = Language.FRENCH;

			topicExpanded2 = "";

			String[] scrambled = sketch.scrambleStrings(Languages.topicsExpandedF[topicIndex]);

			for(String s: scrambled){
				topicExpanded2 += s + " OR ";
			}


			topicExpanded2 = topicExpanded2.substring(0, topicExpanded2.length() - 4);
		}
		tweetZones1 = new TextZone[NUM_TWEETS][];
		tweetZones2 = new TextZone[NUM_TWEETS][];
		background1 = new RectZone[NUM_TWEETS];
		background2 = new RectZone[NUM_TWEETS];
		contentVisual1 = new Color[NUM_TWEETS][];
		contentVisual2 = new Color[NUM_TWEETS][];


		tweetTextSize = sketch.getHeight()/40;
		widthOffset = (int) (0.20*(sketch.getWidth()-sketch.lineX));
		textYOffset = (int) (6.5*tweetTextSize);
		width = (int) (sketch.getWidth()- sketch.lineX - 2*widthOffset);
		height = (int) (5.5*tweetTextSize);
		x = sketch.lineX + widthOffset;
		middleX = x;
		middleWidth = width;
		
		
		
		createTweetWordButtons();
		videoController = new VideoController(sketch, this);
		


		tg = new TwitterGetter(sketch, this);
		//tg.setPriority(Thread.MAX_PRIORITY);
		tg.start();


	}

	public void createBackground4Swipe(){
		float halfH = sketch.getHeight()/2;
		int y1 = (int)(halfH + (halfH*0.1));
		int halfH2 = (int) (halfH - (halfH*0.1));
		int width = sketch.getWidth()-sketch.lineX;

		swipeBackground1 = new RectZone(sketch.lineX, y1, width, halfH, sketch.radius){
			public void vSwipeEvent(VSwipeEvent e){
				if (!errorFlag){
					tg.currentGetter1 = null;

					removeTweets(1);
					if(tweetIndex1 >= tg.tweets1.size() - NUM_TWEETS - (tg.tweets1.size() % NUM_TWEETS)){//MAX_TWEETS - NUM_TWEETS - (MAX_TWEETS % NUM_TWEETS)){
						if(e.getSwipeType() == 1){
							if(pageOffset1 < sketch.NUM_PAGES){
								pageOffset1++;
							} else {
								pageOffset1 = 1;
							}
							tg.getTweets(1, topicExpanded1, false, false);
							
						} else if(tweetIndex1 == 0){
							tweetIndex1 = NUM_TWEETS;
						} else {
							tweetIndex1 -= NUM_TWEETS;
						}
					} else {
						if(e.getSwipeType() == 1){
							tweetIndex1 += NUM_TWEETS;
						} else if(tweetIndex1 == 0){
							tweetIndex1 = NUM_TWEETS;
						} else {
							tweetIndex1 -= NUM_TWEETS;
						}
					}
					for(int k = 0; k < NUM_TWEETS; k++){
						for(ContentGetter cg: tg.hmap1[k].values()){
							cg.cancel();
							cg.removeZones();


						}
					}

					tg.createTweetZones(1);

					e.setHandled(tappableHandled);
				}
			}
		};
		swipeBackground1.setDrawBorder(false);
		swipeBackground1.setGestureEnabled("VSWIPE", true);
		swipeBackground1.setVSwipeDist(sketch.tSwipeThreshold);
		swipeBackground1.setColour(255, 255, 255);
		sketch.client.addZone(swipeBackground1);


		swipeBackground2 = new RectZone(sketch.lineX, 0, width, halfH2, sketch.radius){
			public void vSwipeEvent(VSwipeEvent e){
				if (!errorFlag){
					tg.currentGetter2 = null;

					removeTweets(2);
					if(tweetIndex2 >= tg.tweets2.size() - NUM_TWEETS - (tg.tweets2.size() % NUM_TWEETS)){
						if(e.getSwipeType() == -1){
							if(pageOffset2 < sketch.NUM_PAGES){
								pageOffset2++;
							} else {
								pageOffset2 = 1;
							}
							tg.getTweets(2, topicExpanded2, false, false);
							
						} else if(tweetIndex2 == 0){
							tweetIndex2 = NUM_TWEETS;
						} else {
							tweetIndex2 -= NUM_TWEETS;
						}
					} else {
						if(e.getSwipeType() == -1){
							tweetIndex2 += NUM_TWEETS;
						} else if(tweetIndex2 == 0){
							tweetIndex2 = NUM_TWEETS;
						} else {
							tweetIndex2 -= NUM_TWEETS;
						}
					}

					for(int k = 0; k < NUM_TWEETS; k++){
						for(ContentGetter cg: tg.hmap2[k].values()){
							cg.cancel();
							cg.removeZones();


						}
					}

					tg.createTweetZones(2);

					e.setHandled(tappableHandled);
				}
			}
		};
		swipeBackground2.setDrawBorder(false);
		swipeBackground2.setGestureEnabled("VSWIPE", true);
		swipeBackground2.setVSwipeDist(sketch.tSwipeThreshold);
		swipeBackground2.setColour(255, 255, 255);
		sketch.client.addZone(swipeBackground2);
	}
	public void createMiddleTweet(){



		middleTweet = new RectZone(x, sketch.getHeight()/2-height/2, width/2, height, sketch.radius){
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
			}

			public void tapEvent(TapEvent e){
				if(started){
					tg.translateMiddleWord();
				} else {
					setMiddleTweet("Tap A Word!");
				}
				e.setHandled(true);
			}
		};

		middleTweet.setColour(Colours.boundingBox.getRed(), Colours.boundingBox.getGreen(), Colours.boundingBox.getBlue());
		middleTweet.setShadow(true);
		middleTweet.setShadowColour(Colours.shadow.getRed(), Colours.shadow.getGreen(), Colours.shadow.getBlue());
		middleTweet.setStroke(false);
		middleTweet.setDrawBorder(false);
		middleTweet.setShadowX(-sketch.shadowOffset);
		middleTweet.setShadowY(-sketch.shadowOffset);
		middleTweet.setShadowW(2*sketch.shadowOffset);
		middleTweet.setShadowH(2*sketch.shadowOffset);
		middleTweet.setGestureEnabled("Tap", true);
		setMiddleTweet("");
		client.addZone(middleTweet);

		animMiddleTweet = PropertySetter.createAnimator(500, middleTweet, 
				"Colour", new ColourEval(), Colours.boundingBox, Color.ORANGE);


		animMiddleTweet.setRepeatBehavior(RepeatBehavior.REVERSE);
		animMiddleTweet.setRepeatCount(Animator.INFINITE);


	}

	public void createTweetBackgrounds(){

		for(int i = 0; i < NUM_TWEETS; i++){
			int y1 = (int)((sketch.getHeight()/2 + (i+1.13)*textYOffset) );
			int y2 = (int)((sketch.getHeight()/2 - (i+1.13)*textYOffset)) - height;
			int xShadow = x/60;

			background1[i] = new RectZone(x, y1, width, height, sketch.radius);

			background1[i].setColour(Colours.boundingBox.getRed(), Colours.boundingBox.getGreen(), Colours.boundingBox.getBlue());
			background1[i].setShadow(true);
			background1[i].setShadowColour(Colours.shadow.getRed(), Colours.shadow.getGreen(), Colours.shadow.getBlue());
			background1[i].setStroke(false);
			background1[i].setDrawBorder(false);
			background1[i].setShadowX(xShadow);
			background1[i].setShadowY((int)((sketch.getHeight()/2 + (i + 1.2)*textYOffset)) - y1);
			//background1[i].setVSwipeDist(sketch.tSwipeThreshold);
			//background1[i].setGestureEnabled("VSwipe", true);
			client.addZone(background1[i]);

			background2[i]= new RectZone(x, y2, width, height, sketch.radius);

			background2[i].setColour(Colours.boundingBox.getRed(), Colours.boundingBox.getGreen(), Colours.boundingBox.getBlue());
			background2[i].setShadow(true);
			background2[i].setShadowColour(Colours.shadow.getRed(), Colours.shadow.getGreen(), Colours.shadow.getBlue());
			background2[i].setStroke(false);
			background2[i].setDrawBorder(false);
			background2[i].setShadowX(-xShadow);
			background2[i].setShadowY((int)((sketch.getHeight()/2 - (i+1.2)*textYOffset)- height) - y2);
			//background2[i].setVSwipeDist(sketch.tSwipeThreshold);
			//background2[i].setGestureEnabled("VSwipe", true);
			client.addZone(background2[i]);

		}


	}


	public void setMiddleTweet(String str){
		middleText = str;
		int middleSpace = sketch.getWidth()/20;
		sketch.textFont(Colours.pFont, tweetTextSize*2);
		middleX = (int) (sketch.lineX + (sketch.getWidth()-sketch.lineX)/2 - sketch.textWidth(str)/2 - middleSpace);
		middleWidth = (int) (sketch.textWidth(str) + middleSpace*2);
	}




	public void createTweetWordButtons(){

		tweetWord1 = new TextZone(layoutManager.buttonX, layoutManager.buttonYb2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, tweetsS1, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable() && !errorFlag){
					tg.queryWord(selectedWord, 1);
					removeTweets(1);
					tg.createTweetZones(1);
					this.setGestureEnabled("Tap", false);
					this.setColour(Colours.fadedOutZone);
					this.setTextColour(Colours.fadedText);
					e.setHandled(tappableHandled);

				}
			}
		};

		tweetWord1.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
		tweetWord1.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
		tweetWord1.setDrawBorder(false);
		client.addZone(tweetWord1);

		animTweet1 = PropertySetter.createAnimator(500, tweetWord1, 
				"Colour", new ColourEval(), Colours.unselectedZone, Color.ORANGE);


		animTweet1.setRepeatBehavior(RepeatBehavior.REVERSE);
		animTweet1.setRepeatCount(Animator.INFINITE);


		tweetWord2 = new TextZone(layoutManager.buttonX, layoutManager.buttonYt2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, tweetsS2, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable() && !errorFlag){
					tg.queryWord(selectedWord, 2);
					removeTweets(2);
					tg.createTweetZones(2);
					this.setGestureEnabled("Tap", false);
					this.setColour(Colours.fadedOutZone);
					this.setTextColour(Colours.fadedText);
					e.setHandled(tappableHandled);
				}
			}
		};

		tweetWord2.rotate((float) (Colours.PI));
		tweetWord2.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
		tweetWord2.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
		tweetWord2.setDrawBorder(false);
		client.addZone(tweetWord2);

		animTweet2 = PropertySetter.createAnimator(500, tweetWord2, 
				"Colour", new ColourEval(), Colours.unselectedZone, Color.ORANGE);


		animTweet2.setRepeatBehavior(RepeatBehavior.REVERSE);
		animTweet2.setRepeatCount(Animator.INFINITE);

	}

	public void removeTweets(int user){
		for(int i = 0; i < NUM_TWEETS; i++){
			//String text = "";

			if(user == 1){
				/*if(!errorFlag){
					text = "@" + tweets1.get(i+tweetIndex1).getFromUser() + ":" + "\n" + tweets1.get(i+tweetIndex1).getText();
				} else {
					text = "Twitter Server Error. Try again later.";
				}
				String[] s = text.split("[ \t\n\f\r]+");*/
				if(tweetZones1[i] != null){
					for(int j = 0; j < tweetZones1[i].length; j++){
						client.removeZone(tweetZones1[i][j]);
					}
				}
			} else if (user == 2){
				/*if(!errorFlag){
					text = "@" + tweets2.get(i+tweetIndex2).getFromUser() + ":"  + "\n" + tweets2.get(i+tweetIndex2).getText();
				} else {
					text = "Twitter Server Error. Try again later.";
				}
				String[] s = text.split("[ \t\n\f\r]+");*/
				if(tweetZones2[i] != null){
					for(int j = 0; j < tweetZones2[i].length; j++){
						client.removeZone(tweetZones2[i][j]);
					}
				}
			}
		}
	}

	public void removeZones(){
		canceled = true;
		client.removeZone(tweetWord1);
		client.removeZone(tweetWord2);
		client.removeZone(middleTweet);
		client.removeZone(tg.loading);
		client.removeZone(swipeBackground1);
		client.removeZone(swipeBackground2);

		for(int i = 0; i < NUM_TWEETS; i++){
			client.removeZone(background1[i]);
			client.removeZone(background2[i]);
		}
		removeTweets(1);
		removeTweets(2);
		videoController.removeZones();

		
		if(lastHighlightZone != null){
			client.removeZone(lastHighlightZone);
		}
		
		
		for(int k = 0; k < NUM_TWEETS; k++){
			if(tg.hmap1[k] != null){
				for(ContentGetter cg: tg.hmap1[k].values()){
					cg.cancel();
					cg.removeZones();
	
				}
			}
			if(tg.hmap2[k] != null){
				for(ContentGetter cg: tg.hmap2[k].values()){
					cg.cancel();
					cg.removeZones();
	
				}
			}
		}

	}


}
