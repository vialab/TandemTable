package TandemTable.sections.activities.twitter;



import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.events.VSwipeEvent;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.TextZone;
import TandemTable.Colours;
import TandemTable.Languages;
import TandemTable.Sketch;
import TandemTable.sections.mainSection.MainSection;
import TandemTable.util.ColourEval;
import TandemTable.util.MiddleWord;

import com.memetix.mst.language.Language;

public class TwitterActivity {
	TouchClient client;
	Sketch sketch;
	MainSection mainSection;
	VideoController videoController;

	boolean errorFlag = false;
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
	String tweetsS1 = "";
	String tweetsS2 = "";
	String selectedWord, topicExpanded1, topicExpanded2;


	TextZone tweetWord1, tweetWord2;
	TextZone[][] tweetZones1, tweetZones2;
	TextZone lastHighlightZone;
	// Tweet containers
	RectZone[] background1, background2;
	RectZone swipeBackground1, swipeBackground2;

	MiddleWord middleZone;
	TwitterGetter tg;
	Language langTranslate1, langTranslate2;

	final int NUM_TWEETS = 2;
	final int MAX_TWEETS = 100;

	Animator animTweet1;
	Animator animTweet2;
	

	public TwitterActivity(Sketch sketch, MainSection mainSection, int topicIndex, String lang1, String lang2){
		this.client = sketch.client;
		this.sketch = sketch;
		this.mainSection = mainSection;
		spaceX = sketch.getWidth() - sketch.lineX;
		this.topicIndex = topicIndex;
		
		


		if(lang1.equalsIgnoreCase("English")){
			tweetsS1 = Languages.tweetWordE;
			topic1 = Languages.topicsE[topicIndex];
			langTranslate1 = Language.ENGLISH;

			setTExpandOrder(1, Languages.topicsExpandedE);
		} else if(lang1.equalsIgnoreCase("French")){
			tweetsS1 = Languages.tweetWordF;
			topic1 = Languages.topicsF[topicIndex];
			langTranslate1 = Language.FRENCH;

			setTExpandOrder(1, Languages.topicsExpandedF);
		} else if(lang1.equalsIgnoreCase("Portuguese")){
			tweetsS1 = Languages.tweetWordP;
			topic1 = Languages.topicsP[topicIndex];
			langTranslate1 = Language.PORTUGUESE;

			setTExpandOrder(1, Languages.topicsExpandedP);
		} else if(lang1.equalsIgnoreCase("Spanish")){
			tweetsS1 = Languages.tweetWordS;
			topic1 = Languages.topicsS[topicIndex];
			langTranslate1 = Language.SPANISH;

			setTExpandOrder(1, Languages.topicsExpandedS);
		}

		if(lang2.equalsIgnoreCase("English")){
			tweetsS2 = Languages.tweetWordE;
			topic2 = Languages.topicsE[topicIndex];
			langTranslate2 = Language.ENGLISH;

			setTExpandOrder(2, Languages.topicsExpandedE);

		} else if(lang2.equalsIgnoreCase("French")){
			tweetsS2 = Languages.tweetWordF;
			topic2 = Languages.topicsF[topicIndex];
			langTranslate2 = Language.FRENCH;

			setTExpandOrder(2, Languages.topicsExpandedF);
		} else if(lang2.equalsIgnoreCase("Portuguese")){
			tweetsS2 = Languages.tweetWordP;
			topic2 = Languages.topicsP[topicIndex];
			langTranslate2 = Language.PORTUGUESE;

			setTExpandOrder(2, Languages.topicsExpandedP);
		} else if(lang2.equalsIgnoreCase("Spanish")){
			tweetsS2 = Languages.tweetWordS;
			topic2 = Languages.topicsS[topicIndex];
			langTranslate2 = Language.SPANISH;

			setTExpandOrder(2, Languages.topicsExpandedS);
		}
		
		tweetZones1 = new TextZone[NUM_TWEETS][];
		tweetZones2 = new TextZone[NUM_TWEETS][];
		background1 = new RectZone[NUM_TWEETS];
		background2 = new RectZone[NUM_TWEETS];
		contentVisual1 = new Color[NUM_TWEETS][];
		contentVisual2 = new Color[NUM_TWEETS][];

		mainSection.contentPrompt1.setText(sketch.learner1.tweetPrompts[0]);
		mainSection.contentPrompt2.setText(sketch.learner2.tweetPrompts[0]);

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

	public void setTExpandOrder(int user, String[][] strList) {
		if(user == 1) {
			topicExpanded1 = "";
			String[] scrambled = sketch.scrambleStrings(strList[topicIndex]);

			for(String s: scrambled){
				topicExpanded1 += s + " OR ";
			}

			topicExpanded1 = topicExpanded1.substring(0, topicExpanded1.length() - 4);
			
			try {
				topicExpanded1 = URLEncoder.encode(topicExpanded1, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("Encoding query string failed.");
				e.printStackTrace();
			}
		} else if (user == 2) {
			topicExpanded2 = "";
			String[] scrambled = sketch.scrambleStrings(strList[topicIndex]);

			for(String s: scrambled){
				topicExpanded2 += s + " OR ";
			}

			topicExpanded2 = topicExpanded2.substring(0, topicExpanded2.length() - 4);
			
			try {
				topicExpanded2 = URLEncoder.encode(topicExpanded2, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("Encoding query string failed.");
				e.printStackTrace();
			}
		}
	}
	
	public void createBackground4Swipe(){
		float halfH = sketch.getHeight()/2 - sketch.strokeW;
		float y1 = sketch.getHeight()/2 + sketch.strokeW; //int y1 = (int)(halfH + (halfH*0.1));
		//int halfH2 = (int) (halfH - (halfH*0.1));
		float width = sketch.getWidth() - (sketch.lineX + sketch.strokeW);

		swipeBackground1 = new RectZone(sketch.lineX + sketch.strokeW, y1, width, halfH, sketch.radius){
			public void vSwipeEvent(VSwipeEvent e){
				if (!errorFlag){
					tg.contGetter1 = null;

					removeTweets(1);
					
					fadeTweetWordButton(1);
					
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
		swipeBackground1.setColour(Colours.backgroundColour);
		sketch.client.addZone(swipeBackground1);


		swipeBackground2 = new RectZone(sketch.lineX + sketch.strokeW, 0, width, halfH, sketch.radius){
			public void vSwipeEvent(VSwipeEvent e){
				if (!errorFlag){
					tg.contGetter2 = null;

					removeTweets(2);
					
					fadeTweetWordButton(2);
					
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
		swipeBackground2.setColour(Colours.backgroundColour);
		sketch.client.addZone(swipeBackground2);
	}
	public void createMiddleTweet(){
		boolean sameLang = false;
		
		if(langTranslate1 == langTranslate2) {
			sameLang = true;
		}
		
		middleZone = new MiddleWord(sketch, tg, sameLang);
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


	public void createTweetWordButtons(){

		tweetWord1 = new TextZone(mainSection.buttonX, mainSection.buttonYb2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, tweetsS1, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable() && !errorFlag){
					tg.queryWord(selectedWord, 1);
					removeTweets(1);
					tg.createTweetZones(1);

					fadeTweetWordButton(1);
					
					e.setHandled(tappableHandled);

				}
			}
		};

		tweetWord1.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
		tweetWord1.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
		tweetWord1.setDrawBorder(false);
		client.addZone(tweetWord1);

		animTweet1 = PropertySetter.createAnimator(500, tweetWord1, 
				"Colour", new ColourEval(), Colours.unselectedZone, Colours.searchTweet);


		animTweet1.setRepeatBehavior(RepeatBehavior.REVERSE);
		animTweet1.setRepeatCount(Animator.INFINITE);


		tweetWord2 = new TextZone(mainSection.buttonX, mainSection.buttonYt2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, tweetsS2, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable() && !errorFlag){
					tg.queryWord(selectedWord, 2);
					removeTweets(2);
					tg.createTweetZones(2);

					fadeTweetWordButton(2);
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
				"Colour", new ColourEval(), Colours.unselectedZone, Colours.searchTweet);


		animTweet2.setRepeatBehavior(RepeatBehavior.REVERSE);
		animTweet2.setRepeatCount(Animator.INFINITE);

	}
	
	public void fadeTweetWordButton(int user) {
		if(user == 1) {
			tweetWord1.setGestureEnabled("Tap", false);
			tweetWord1.setColour(Colours.fadedOutZone);
			tweetWord1.setTextColour(Colours.fadedText);
		} else if(user ==2) {
			tweetWord2.setGestureEnabled("Tap", false);
			tweetWord2.setColour(Colours.fadedOutZone);
			tweetWord2.setTextColour(Colours.fadedText);
		}
	}

	public void removeTweets(int user){
		for(int i = 0; i < NUM_TWEETS; i++){

			if(user == 1){

				if(tweetZones1[i] != null){
					for(int j = 0; j < tweetZones1[i].length; j++){
						client.removeZone(tweetZones1[i][j]);
					}
				}
			} else if (user == 2){

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
		
		if(middleZone.fadedBack != null) {
			middleZone.removeTransButtons();
		}
		
		client.removeZone(tweetWord1);
		client.removeZone(tweetWord2);
		client.removeZone(middleZone.middleZone);
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
