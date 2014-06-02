package TandemTable.sections.mainSection;
import java.awt.Color;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.TextZone;
import vialab.simpleMultiTouch.zones.Zone;
import TandemTable.Colours;
import TandemTable.Languages;
import TandemTable.Sketch;
import TandemTable.sections.activities.headlines.HeadlinesActivity;
import TandemTable.sections.activities.pGame.PGame;
import TandemTable.sections.activities.pictures.PictureActivity;
import TandemTable.sections.activities.twitter.TwitterActivity;
import TandemTable.sections.activities.videos.VideoActivity;
import TandemTable.util.AudioIn;
import TandemTable.util.ColourEval;

import com.memetix.mst.translate.Translate;

/**
 * Main section of TandemTable. In this section, users are
 * shown a node-link diagram which holds discussion topics 
 * within each node. Users may select a topic by placing
 * two fingers on the node at the same time. Users can then
 * go into the different associated activity sections.
 */
public class MainSection {
	Sketch sketch;
	
	public Graph graph;
	public TwitterActivity twitterAct;
	public HeadlinesActivity headlinesAct;
	PictureActivity pictureAct;
	VideoActivity videoAct;
	PGame pGameAct;
	

	Animator[] animA1, animA2;
	Animator[] animSwitchTopic = new Animator[2];
	Animator[] animSwitchAct = new Animator[2];
	Animator[] animNewLang = new Animator[2];
	public Animator[] animContentPrompt = new Animator[2];

	public int animationTime = 1400;
	
	// Activity zones
	TextZone[] activityB1;
	TextZone[] activityB2;
	
	// Content Prompts
	public TextZone contentPrompt1, contentPrompt2;
	public RectZone cPromptOverlay1, cPromptOverlay2;
	
	// Speaking amount text
	public TextZone[] utterVisText;
	// Height of UtterVis
	float heightBar;
	// Border offset for UtterVis
	float borderDif;

	Zone newLang1, newLang2;
	public Zone switchAct1, switchAct2;

	public boolean leftCenterLineFlag = true;
	public boolean centerLineFlag = false;
	boolean verticalLineFlag = true;
	boolean edgesFlag = false;


	boolean activityBFlag = false;
	
	boolean bottomBSelect1 = false;
	boolean bottomBSelect2 = false;
	boolean newLangSelect1 = false;
	boolean newLangSelect2 = false;
	boolean twitterFlag = false;
	boolean youtubeFlag = false;
	
	int selectedTopicIndex = -1;
	//String selectedTopic = "";

	//For textZone buttons
	public int buttonX;
	public int buttonYb; //bottom button y position
	public int buttonYt; //top button y position

	// for middle button
	public int buttonYb2; 
	public int buttonYt2;

	public int buttonYb3;
	public int buttonYt3;
	public int buttonYb4;
	public int buttonYt4;

	// for activity buttons
	int actBHeight;
	int actBWidth;
	//int questionIndex1 = 0;
	
	// Size of the graph nodes
	float sizeNode;

	String chosenActivity = null;
	boolean[] activityFlags1;
	boolean[] activityFlags2;
	Color[] activityBars1, activityBars2;

	float textOffsetY, textOffsetX, lastNodeX, lastNodeY;

	String lang1, lang2;


	/**
	 * Main section of TandemTable. In this section, users are
	 * shown a node-link diagram which holds discussion topics 
	 * within each node. Users may select a topic by placing
	 * two fingers on the node at the same time. Users can then
	 * go into the different associated activity sections.
	 * 
	 * @param sketch
	 * @param lang1
	 * @param lang2
	 */
	public MainSection(Sketch sketch, String lang1, String lang2){
		//this.sketch.client = sketch.client;
		this.sketch = sketch;
		//this.sketch = sketch.client.getParent();
		this.lang1 = lang1;
		this.lang2 = lang2;
		textOffsetY = sketch.getHeight()/20;
		textOffsetX = sketch.getWidth()/30;
		sizeNode = sketch.getWidth()/10;

		animA1 = new Animator[Sketch.NUM_ACTIVITIES];
		animA2 = new Animator[Sketch.NUM_ACTIVITIES];
		activityB1 = new TextZone[Sketch.NUM_ACTIVITIES];
		activityB2 = new TextZone[Sketch.NUM_ACTIVITIES];
		activityFlags1 = new boolean[Sketch.NUM_ACTIVITIES];
		activityFlags2 = new boolean[Sketch.NUM_ACTIVITIES];
		activityBars1 = new Color[Sketch.NUM_TOPICS];
		activityBars2 = new Color[Sketch.NUM_TOPICS];

		// Set up translator
		Translate.setClientId(Colours.clientId);
		Translate.setClientSecret(Colours.clientSecret);
		
		//Set Up Languages
		sketch.learner1 = new Languages(lang1);
		sketch.learner2 = new Languages(lang2);
		
		heightBar = sizeNode/5;
		borderDif = 4;
		
		/////////////////////////////////
		// Set up talking amount text
		////////////////////////////////
		utterVisText = new TextZone[2];
		float utterTextSize = sketch.textSize/2;
		utterVisText[0] = new TextZone(0, sketch.getHeight()/2 + (float) (sizeNode/1.5) + utterTextSize, sketch.lineX, sizeNode/5, Colours.pFont, sketch.learner1.utterVis, utterTextSize, "CENTER", "CENTER");
		utterVisText[0].setTextColour(Colours.unselectedZone);
		utterVisText[0].setDrawBorder(false);
		sketch.client.addZone(utterVisText[0]);
		
		utterVisText[1] = new TextZone(0, sketch.getHeight()/2 - (float) (sizeNode/1.5) - utterTextSize - heightBar, sketch.lineX, sizeNode/5, Colours.pFont, sketch.learner2.utterVis, utterTextSize, "CENTER", "CENTER");
		utterVisText[1].rotate((float) (Colours.PI));
		utterVisText[1].setTextColour(Colours.unselectedZone);
		utterVisText[1].setDrawBorder(false);
		sketch.client.addZone(utterVisText[1]);

		if(!sketch.recordAudio) {
			//utterVisText[0].setActive(false);
			//utterVisText[1].setActive(false);			
		}
		

		buttonX = sketch.getX()+1;
		buttonYb = sketch.screenHeight - sketch.buttonHeight-1;
		buttonYt = sketch.getY() +1;
		buttonYb2 = sketch.screenHeight - 2*sketch.buttonHeight-2;
		buttonYt2 = sketch.getY() + sketch.buttonHeight + 2;

		buttonYb3 = sketch.screenHeight - 3*sketch.buttonHeight-3;
		buttonYt3 = sketch.getY() + 2*sketch.buttonHeight + 3;
		buttonYb4 = sketch.screenHeight - 4*sketch.buttonHeight-4;
		buttonYt4 = sketch.getY() + 3*sketch.buttonHeight + 4;


		actBHeight = (int) (sketch.getHeight()/6.5);
		actBWidth = (int)((sketch.getWidth()-sketch.lineX)/5.1);
		
		if(sketch.removeVideoAct) {
			actBWidth = (int)((sketch.getWidth()-sketch.lineX)/4.1);
		}
		
	}
	
	/**
	 * Used to draw the different lines used in the different sections/activities
	 */
	public void drawLayout(){
		if(verticalLineFlag){
			sketch.strokeWeight(sketch.strokeW);
			sketch.stroke(Colours.lineColour.getRed(), Colours.lineColour.getGreen(), Colours.lineColour.getBlue());
			sketch.line(sketch.lineX, sketch.getY(), sketch.lineX, sketch.getHeight());
		}

		if(leftCenterLineFlag){
			sketch.strokeWeight(sketch.strokeW);
			sketch.stroke(Colours.lineColour.getRed(), Colours.lineColour.getGreen(), Colours.lineColour.getBlue());
			sketch.line(sketch.getX(), sketch.getHeight()/2, sketch.lineX, sketch.getHeight()/2);
		}

		//Half Screen separator
		if(centerLineFlag){
			sketch.strokeWeight(sketch.strokeW);
			sketch.stroke(Colours.lineColour.getRed(), Colours.lineColour.getGreen(), Colours.lineColour.getBlue());
			sketch.line(sketch.lineX, sketch.getHeight()/2, sketch.getWidth(), sketch.getHeight()/2);
		}
		//graph edges (lines between nodes)
		if (edgesFlag){
			sketch.strokeWeight(sketch.strokeW);
			sketch.stroke(Colours.lineColour.getRed(), Colours.lineColour.getGreen(), Colours.lineColour.getBlue());
			graph.displayEdges();
		}
		
		
		///////////////////////////////////////
		// Drawing speaking amount
		///////////////////////////////////////
		if(sketch.recordAudio) {

			sketch.rectMode(Sketch.CENTER);
			
			// Containing rectangle
			sketch.strokeWeight(sketch.strokeW);
			sketch.noFill();
			sketch.stroke(Colours.lineColour.getRed(), Colours.lineColour.getGreen(), Colours.lineColour.getBlue());
			
			
			float yDifference = (float) (sizeNode/1.5);
			//Above node
			sketch.rect(sketch.lineX/2, sketch.getHeight()/2 + yDifference, sizeNode + borderDif, heightBar/2 + borderDif);
			//Below node
			sketch.rect(sketch.lineX/2, sketch.getHeight()/2 - yDifference, sizeNode + borderDif, heightBar/2 + borderDif);
			
			
			float barHeight1 = 0, barHeight2 = 0, utterRatio1 = 0, utterRatio2 = 0,
					lastUtterRatio1 = 0, lastUtterRatio2 = 0;
			// Amount of talking for each learner
			/*float utterRateShort1 = sketch.audioIn[0].getUtterRateShort();
			float utterRateLong1 = sketch.audioIn[0].getUtterRateLong();
			
			if(utterRateShort1 > utterRateLong1/AudioIn.utterRateTimeRatio) {
				utterRateLong1 = utterRateShort1*AudioIn.utterRateTimeRatio;
			}
			
			float utterRateShort2 = sketch.audioIn[1].getUtterRateShort();
			float utterRateLong2 = sketch.audioIn[1].getUtterRateLong();
			
			if(utterRateShort2 > utterRateLong2/AudioIn.utterRateTimeRatio) {
				utterRateLong2 = utterRateShort2*AudioIn.utterRateTimeRatio;
			}
			
			float lrgUtterRateLong;
			if(utterRateLong1 >= utterRateLong2) {
				lrgUtterRateLong = utterRateLong1;
			} else {
				lrgUtterRateLong = utterRateLong2;
			}
			
			lrgUtterRateLong = lrgUtterRateLong/AudioIn.utterRateTimeRatio;
					
			if(lrgUtterRateLong < 1) {
				lrgUtterRateLong = 1;
			}*/
			
			//System.out.println(utterRateShort1 + " " + utterRateLong1 + " " + utterRateShort2 + " " + utterRateLong2 + " " + lrgUtterRateLong);
			utterRatio1 = ((float)sketch.audioIn[0].getUtterTimeLong())/AudioIn.utterVisTimeLong;
			utterRatio2 = ((float)sketch.audioIn[1].getUtterTimeLong())/AudioIn.utterVisTimeLong;
			
			lastUtterRatio1 = ((float)sketch.audioIn[0].getLastUtterTimeLong())/AudioIn.utterVisTimeLong;
			lastUtterRatio2 = ((float)sketch.audioIn[1].getLastUtterTimeLong())/AudioIn.utterVisTimeLong;
			
			
			barHeight1 = (utterRatio1 * AudioIn.WEIGHTED_CUR + lastUtterRatio1 * AudioIn.WEIGHTED_LAST)*sizeNode;
			barHeight2 = (utterRatio2 * AudioIn.WEIGHTED_CUR + lastUtterRatio2 * AudioIn.WEIGHTED_LAST)*sizeNode;
			
			System.out.println(sketch.audioIn[0].getUtterTimeLong() + " " + barHeight1);
			System.out.println(sketch.audioIn[1].getUtterTimeLong() + " " + barHeight2);

			/*int talkAmount1 = sketch.audioIn[0].getTalkingAmount();
			int talkAmount2 = sketch.audioIn[1].getTalkingAmount();
			
			if(talkAmount1 > talkAmount2) {
				barHeight1 = sizeNode;
				barHeight2 = ((float) (talkAmount2)/talkAmount1)*sizeNode;
			} else if(talkAmount1 < talkAmount2){
				barHeight2 = sizeNode;
				barHeight1 = ((float) (talkAmount1)/talkAmount2)*sizeNode;
			} else {
				barHeight1 = barHeight2 = sizeNode;
			}*/
			
			
			sketch.noStroke();
			// Learner 1's bar
			sketch.fill(Colours.learner1TalkBar.getRed(), Colours.learner1TalkBar.getGreen(), Colours.learner1TalkBar.getBlue());
			//Above topic node
			sketch.rect(sketch.lineX/2, sketch.getHeight()/2 + yDifference, barHeight1, heightBar/2);
			
			// Learner 2's bar
			sketch.fill(Colours.learner2TalkBar.getRed(), Colours.learner2TalkBar.getGreen(), Colours.learner2TalkBar.getBlue());
			//Below topic node
			sketch.rect(sketch.lineX/2, sketch.getHeight()/2 - yDifference, barHeight2, heightBar/2);

			sketch.rectMode(Sketch.CORNER);
		}
	}	
	
	public void createMainScreen() {
		centerLineFlag = false;
		createSwitchLanguageButtons();
		createGraph();
		createActivityButtons();
		
		if(sketch.recordAudio) {
			long timeNow = System.currentTimeMillis();
			sketch.audioIn[0].setTimeLastContent(timeNow);
			sketch.audioIn[1].setTimeLastContent(timeNow);
		}
		
		createContentPrompts();
	}
	
	public void createContentPrompts() {
		float dif = (float) (sketch.buttonWidth*0.05);
		float width = sketch.buttonWidth - dif*2;
		int animTime = animationTime*2;
		float buttonHeight = (float) (sketch.buttonHeight/1.5);
		float y1 = buttonYb3 + (sketch.buttonHeight - buttonHeight);
		
		contentPrompt1 = new TextZone(buttonX + dif, y1, width, buttonHeight, Colours.pFont, sketch.learner1.topicPrompts[0], sketch.textSize/2, "CENTER", "CENTER");
		
		contentPrompt1.setTextColour(Colours.contentPromptC);
		contentPrompt1.setDrawBorder(false);
		contentPrompt1.setActive(true);
		sketch.client.addZone(contentPrompt1);
		
		cPromptOverlay1 = new RectZone(buttonX + dif, y1, width, buttonHeight);
		
		cPromptOverlay1.setColour(Colours.backgroundColour);
		cPromptOverlay1.setDrawBorder(false);
		cPromptOverlay1.setActive(true);
		sketch.client.addZone(cPromptOverlay1);
		
		animContentPrompt[0] = PropertySetter.createAnimator(animTime, cPromptOverlay1, 
				"colour", new ColourEval(), Colours.backgroundColour, Colours.backgroundColour0Alpha);

		animContentPrompt[0].setRepeatBehavior(RepeatBehavior.REVERSE);
		animContentPrompt[0].setRepeatCount(1);
		
		contentPrompt2 = new TextZone(buttonX + dif, buttonYt3, width, buttonHeight, Colours.pFont, sketch.learner2.topicPrompts[0], sketch.textSize/2, "CENTER", "CENTER");
		
		contentPrompt2.rotate((float) (Colours.PI));
		contentPrompt2.setTextColour(Colours.contentPromptC);
		contentPrompt2.setDrawBorder(false);
		contentPrompt2.setActive(true);
		sketch.client.addZone(contentPrompt2);
		
		cPromptOverlay2 = new RectZone(buttonX + dif, buttonYt3, width, buttonHeight);
		
		cPromptOverlay2.setColour(Colours.backgroundColour);
		cPromptOverlay2.rotate((float) (Colours.PI));
		cPromptOverlay2.setDrawBorder(false);
		cPromptOverlay2.setActive(true);
		sketch.client.addZone(cPromptOverlay2);
		
		animContentPrompt[1] = PropertySetter.createAnimator(animTime, cPromptOverlay2, 
				"colour", new ColourEval(), Colours.backgroundColour, Colours.backgroundColour0Alpha);

		animContentPrompt[1].setRepeatBehavior(RepeatBehavior.REVERSE);
		animContentPrompt[1].setRepeatCount(1);
	}

	/**
	 * Creates the switch target language buttons
	 */
	public void createSwitchLanguageButtons(){

		//New language Button for user 1
		newLang1 = new TextZone(buttonX, buttonYb, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.newLang, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){

					if(!newLangSelect1 && !newLangSelect2){
						animNewLang[1].start();
						this.setColour(Colours.selectedZone);
						newLangSelect1 = true;
					} else if (newLangSelect1 && !newLangSelect2){
						animNewLang[1].stop();
						((TextZone) newLang2).setColour(Colours.unselectedZone);
						((TextZone) newLang1).setColour(Colours.unselectedZone);
						newLangSelect1 = false;
					} else if (newLangSelect2 && !newLangSelect1){
						animNewLang[0].stop();
						animNewLang[1].stop();
						((TextZone) newLang1).setColour(Colours.unselectedZone);
						((TextZone) newLang2).setColour(Colours.unselectedZone);
						newLangSelect2 = false;
						newLangSelect1 = false;
						removeAllItems();

						sketch.login.chooseNewLang();
					}

					e.setHandled(tappableHandled);

				}
			}
		};

		//((TextZone) newLang1).setTextColour(Colours.fadedText);
		//((TextZone) newLang1).setColour(Colours.fadedOutZone);

		((TextZone) newLang1).setTextColour(Colours.zoneText);
		((TextZone) newLang1).setColour(Colours.unselectedZone);
		newLang1.setGestureEnabled("TAP", true, true);
		newLang1.setDrawBorder(false);
		sketch.client.addZone(newLang1);

		animNewLang[0] = PropertySetter.createAnimator(animationTime, newLang1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animNewLang[0].setRepeatBehavior(RepeatBehavior.REVERSE);
		animNewLang[0].setRepeatCount(Animator.INFINITE);

		//New language Button for user 2
		newLang2 = new TextZone(buttonX, buttonYt, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.newLang, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){

					if(!newLangSelect1 && !newLangSelect2){
						animNewLang[0].start();
						this.setColour(Colours.selectedZone);
						newLangSelect2 = true;
					} else if (newLangSelect2 && !newLangSelect1){
						animNewLang[0].stop();
						((TextZone) newLang2).setColour(Colours.unselectedZone);
						((TextZone) newLang1).setColour(Colours.unselectedZone);
						newLangSelect2 = false;
					} else if (newLangSelect1 & !newLangSelect2){
						animNewLang[0].stop();
						animNewLang[1].stop();
						((TextZone) newLang1).setColour(Colours.unselectedZone);
						((TextZone) newLang2).setColour(Colours.unselectedZone);
						newLangSelect2 = false;
						newLangSelect1 = false;
						removeAllItems();
						sketch.login.chooseNewLang();
					}

					e.setHandled(tappableHandled);

				}
			}
		};

		//((TextZone) newLang2).setTextColour(Colours.fadedText);
		//((TextZone) newLang2).setColour(Colours.fadedOutZone);

		newLang2.rotate((float) (Colours.PI));
		((TextZone) newLang2).setTextColour(Colours.zoneText);
		((TextZone) newLang2).setColour(Colours.unselectedZone);
		newLang2.setGestureEnabled("TAP", true, true);
		newLang2.setDrawBorder(false);
		sketch.client.addZone(newLang2);

		animNewLang[1] = PropertySetter.createAnimator(animationTime, newLang2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animNewLang[1].setRepeatBehavior(RepeatBehavior.REVERSE);
		animNewLang[1].setRepeatCount(Animator.INFINITE);
	}
	
	/**
	 * Create the node-link diagram used to encapsulate the topic suggestions
	 */
	public void createGraph(){
		graph = new Graph(sketch, this, sketch.lineX, sizeNode);
		centerLineFlag = false;
		edgesFlag = true;

	}
	
	
	/** Enable the activity buttons that are associated with the node (topic) that
	 * has been chosen by the users
	 * @param index
	 * @param topic
	 * @param randFlag
	 * @param randIndex
	 */
	public void enableActivityButtons(int index, String topic, boolean randFlag, int randIndex){
		selectedTopicIndex = index;
		activityBFlag = true;

		for(int i = 0; i < Sketch.NUM_ACTIVITIES; i++){


			if(activityFlags1[i] == true){
				animA2[i].stop();
				activityFlags1[i] = false;
			}
			if(activityFlags2[i] == true){
				animA1[i].stop();
				activityFlags2[i] = false;
			}

			if(randFlag){
				if(graph.randActs[randIndex][i] == 1){
					activityB1[i].setGestureEnabled("Tap", false);
					activityB2[i].setGestureEnabled("Tap", false);
					activityB1[i].setTextColour(Colours.fadedText);
					activityB2[i].setTextColour(Colours.fadedText);
				} else {
					activityB1[i].setGestureEnabled("Tap", true);
					activityB2[i].setGestureEnabled("Tap", true);
					activityB1[i].setColour(Colours.unselectedZone);
					activityB2[i].setColour(Colours.unselectedZone);
					activityB1[i].setTextColour(Colours.zoneText);
					activityB2[i].setTextColour(Colours.zoneText);
				}

			} else {
				activityB1[i].setGestureEnabled("Tap", true);
				activityB2[i].setGestureEnabled("Tap", true);
				activityB1[i].setColour(Colours.unselectedZone);
				activityB2[i].setColour(Colours.unselectedZone);
				activityB1[i].setTextColour(Colours.zoneText);
				activityB2[i].setTextColour(Colours.zoneText);
			}
		}
		
		if(sketch.deactivateVideo) {
			activityB1[3].setGestureEnabled("Tap", false);
			activityB2[3].setGestureEnabled("Tap", false);
			activityB1[3].setTextColour(Colours.fadedText);
			activityB2[3].setTextColour(Colours.fadedText);
			activityB1[3].setColour(Colours.fadedOutZone);
			activityB2[3].setColour(Colours.fadedOutZone);

		}

	}

	/**
	 * Switches back to the main interface. The user has
	 * chosen to leave the current activity
	 * @param activity
	 */
	public void switchActivity(String activity){

		graph.nodes[graph.lastSelectedNode].setX(lastNodeX);
		graph.nodes[graph.lastSelectedNode].setY(lastNodeY);

		contentPrompt1.setText(sketch.learner1.topicPrompts[0]);
		contentPrompt2.setText(sketch.learner2.topicPrompts[0]);
		
		
		newLang1.setActive(true);
		newLang2.setActive(true);
		//leftCenterLineFlag = true;
		verticalLineFlag = true;

		//Twitter
		if (activity.equalsIgnoreCase(Languages.activitiesE[0])){
			twitterFlag = false;
			twitterAct.removeZones();

			//News Headlines
		} else if (activity.equalsIgnoreCase(Languages.activitiesE[1])){
			headlinesAct.removeZones();

			//Pictures
		} else if (activity.equalsIgnoreCase(Languages.activitiesE[2])){
			pictureAct.removeZones();


			//Videos
		} else if (activity.equalsIgnoreCase(Languages.activitiesE[3])){
			youtubeFlag = false;
			videoAct.removeZones();

			//Web Search
		} else if (activity.equalsIgnoreCase(Languages.activitiesE[4])){
			pGameAct.removeZones();
		}

		for(int i = 0; i < Sketch.NUM_ACTIVITIES; i++){
			if(sketch.removeVideoAct && i == 3) {
				continue;
			}
			activityB1[i].setActive(true);
			activityB2[i].setActive(true);
		}
		
		edgesFlag = true;
		centerLineFlag = false;
		graph.activateNodes();


		switchAct1.setActive(false);
		switchAct2.setActive(false);

	}



	/**
	 * Creates the activity buttons used to start
	 * an activity whose content is related to the
	 * chosen topic of discussion.
	 */
	public void createActivityButtons(){

		for(int i = 0; i < Sketch.NUM_TOPICS; i++){
			activityBars1[i] = Colours.fadedText;
			activityBars2[i] = Colours.fadedText;
		}
		
		// User 1
		int offset = 2;

		int x;
		int y = sketch.getHeight() - actBHeight - offset;

		for(int i = 0; i < Sketch.NUM_ACTIVITIES; i++){
			final int index = i;

			x = (int) (sketch.getWidth()/5.8) + index*(int)((sketch.getWidth()-sketch.lineX)/5.0);
			
			if(sketch.removeVideoAct) {
				if(i == 4) {
					x = (int) (sketch.getWidth()/5.8) + (index - 1)*(int)((sketch.getWidth()-sketch.lineX)/4.0);
				} else {
					x = (int) (sketch.getWidth()/5.8) + index*(int)((sketch.getWidth()-sketch.lineX)/4.0);
				}
			}
			
			activityB1[i] = new TextZone(x, y, actBWidth, actBHeight,
					Colours.pFont, sketch.learner1.activities[index], sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (isTappable() && activityBFlag){
						for(int j = 0; j < Sketch.NUM_ACTIVITIES; j++){
							if(j != index && (activityFlags1[j] == true || activityFlags2[j] == true)){
								animA2[j].stop();
								animA1[j].stop();
								activityB1[j].setColour(Colours.unselectedZone);
								activityB2[j].setColour(Colours.unselectedZone);

								activityFlags2[j] = false;
								activityFlags1[j] = false;
							}

						}
						if(activityFlags2[index]){
							animA1[index].stop();
							activityB2[index].setColour(Colours.unselectedZone);
							activityB1[index].setColour(Colours.unselectedZone);
							initializeActivityScreen(Languages.activitiesE[index]);
						} else if(!activityFlags1[index]){
							animA2[index].start();
							this.setColour(Colours.selectedZone);
							activityFlags1[index] = true;
						} else if(activityFlags1[index]){
							animA2[index].stop();
							activityB2[index].setColour(Colours.unselectedZone);
							activityB1[index].setColour(Colours.unselectedZone);
							activityFlags1[index] = false;
						}


						e.setHandled(tappableHandled);
					}
				}

				public void drawZone(){
					super.drawZone();

					sketch.fill(activityBars1[index].getRed(), activityBars1[index].getGreen(), activityBars1[index].getBlue());
					sketch.rect(getX(), getY(), getWidth(), getHeight()/10);
				}


			};

			if(sketch.removeVideoAct && i == 3) {
				activityB1[i].setActive(false);
			}
			
			activityB1[i].setTextColour(Colours.fadedText);
			activityB1[i].setColour(Colours.fadedOutZone);
			activityB1[i].setGestureEnabled("TAP", true, true);
			activityB1[i].setDrawBorder(false);
			sketch.client.addZone(activityB1[i]);

			animA1[index] = PropertySetter.createAnimator(animationTime, activityB1[index], 
					"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

			animA1[index].setRepeatBehavior(RepeatBehavior.REVERSE);
			animA1[index].setRepeatCount(Animator.INFINITE);
		}

		// User 2
		y = sketch.getY() + offset;

		for(int i = 0; i < Sketch.NUM_ACTIVITIES; i++){
			final int index = i;
			x = (int) (sketch.getWidth()/5.8) + index*(int)((sketch.getWidth()-sketch.lineX)/5.0);

			if(sketch.removeVideoAct) {
				if(i == 4) {
					x = (int) (sketch.getWidth()/5.8) + (index - 1)*(int)((sketch.getWidth()-sketch.lineX)/4.0);
				} else {
					x = (int) (sketch.getWidth()/5.8) + index*(int)((sketch.getWidth()-sketch.lineX)/4.0);
				}
			}
			
			activityB2[i] = new TextZone(x, y, actBWidth, actBHeight,
					Colours.pFont, sketch.learner2.activities[index], sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (isTappable() && activityBFlag){
						for(int j = 0; j < Sketch.NUM_ACTIVITIES; j++){
							if(j != index && (activityFlags1[j] == true || activityFlags2[j] == true)){
								animA2[j].stop();
								animA1[j].stop();
								activityB1[j].setColour(Colours.unselectedZone);
								activityB2[j].setColour(Colours.unselectedZone);
								activityFlags2[j] = false;
								activityFlags1[j] = false;
							}
						}
						if(activityFlags1[index]){
							animA2[index].stop();
							activityB2[index].setColour(Colours.unselectedZone);
							activityB1[index].setColour(Colours.unselectedZone);
							initializeActivityScreen(Languages.activitiesE[index]);
						} else if(!activityFlags2[index]){
							animA1[index].start();
							this.setColour(Colours.selectedZone);
							activityFlags2[index] = true;
						} else if(activityFlags2[index]){
							animA1[index].stop();
							activityB2[index].setColour(Colours.unselectedZone);
							activityB1[index].setColour(Colours.unselectedZone);
							activityFlags2[index] = false;
						}

						e.setHandled(tappableHandled);
					}
				}

				public void drawZone(){
					super.drawZone();
					sketch.fill(activityBars2[index].getRed(), activityBars2[index].getGreen(), activityBars2[index].getBlue());

					sketch.rect(getX(), getY(), getWidth(), getHeight()/10);
				}
				
			};

			if(sketch.removeVideoAct && i == 3) {
				activityB2[i].setActive(false);
			}
			
			activityB2[i].rotate((float) (Colours.PI));
			activityB2[i].setTextColour(Colours.fadedText);
			activityB2[i].setColour(Colours.fadedOutZone);
			activityB2[i].setGestureEnabled("TAP", true, true);
			activityB2[i].setDrawBorder(false);
			sketch.client.addZone(activityB2[i]);

			animA2[index] = PropertySetter.createAnimator(animationTime, activityB2[index], 
					"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

			animA2[index].setRepeatBehavior(RepeatBehavior.REVERSE);
			animA2[index].setRepeatCount(Animator.INFINITE);
		}



	}

	/**
	 * Initialize the GUI for the chosen activity
	 * @param activity
	 */
	public void initializeActivityScreen(String activity){

		sketch.mainLogger.log(activity);
		
		lastNodeX = graph.nodes[graph.lastSelectedNode].getX();
		lastNodeY = graph.nodes[graph.lastSelectedNode].getY();
		graph.nodes[graph.lastSelectedNode].setXY(sketch.lineX/2 - graph.nodes[graph.lastSelectedNode].getWidth()/2, sketch.getHeight()/2 - graph.nodes[graph.lastSelectedNode].getHeight()/2);
		//graph.nodes[graph.lastSelectedNode].setXY(buttonX, sketch.getHeight()/2 - graph.nodes[graph.lastSelectedNode].getHeight()/2);

		for(int i = 0; i < Sketch.NUM_ACTIVITIES; i++){
			activityFlags1[i] = false;
			activityFlags2[i] = false;
			activityB1[i].setActive(false);
			activityB2[i].setActive(false);			
		}

		newLang1.setActive(false);
		newLang2.setActive(false);


		chosenActivity = activity;

		edgesFlag = false;
		//leftCenterLineFlag = false;
		graph.deactivateNodes();
		
		// User 1
		switchAct1 = new TextZone(buttonX, buttonYb, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.choAnoAct, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){

					if(!bottomBSelect1 && !bottomBSelect2){
						animSwitchAct[1].start();
						this.setColour(Colours.selectedZone);
						bottomBSelect1 = true;
					} else if (bottomBSelect1 && !bottomBSelect2){
						animSwitchAct[1].stop();
						((TextZone) switchAct2).setColour(Colours.unselectedZone);
						((TextZone) switchAct1).setColour(Colours.unselectedZone);
						bottomBSelect1 = false;
					} else if (bottomBSelect2){
						animSwitchAct[0].stop();
						((TextZone) switchAct2).setColour(Colours.unselectedZone);
						((TextZone) switchAct1).setColour(Colours.unselectedZone);
						bottomBSelect2 = false;
						bottomBSelect1 = false;

						switchActivity(chosenActivity);
					}


					e.setHandled(tappableHandled);

				}
			}
		};

		((TextZone) switchAct1).setTextColour(Colours.zoneText);
		((TextZone) switchAct1).setColour(Colours.unselectedZone);
		switchAct1.setGestureEnabled("TAP", true, true);
		switchAct1.setDrawBorder(false);
		sketch.client.addZone(switchAct1);

		animSwitchAct[0] = PropertySetter.createAnimator(animationTime, switchAct1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animSwitchAct[0].setRepeatBehavior(RepeatBehavior.REVERSE);
		animSwitchAct[0].setRepeatCount(Animator.INFINITE);

		
		// User 2
		switchAct2  = new TextZone(buttonX, buttonYt, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.choAnoAct, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){
					if(!bottomBSelect2 && !bottomBSelect1){
						animSwitchAct[0].start();
						((TextZone) switchAct2).setColour(Colours.selectedZone);
						bottomBSelect2 = true;
					} else if (bottomBSelect2 && !bottomBSelect1){
						animSwitchAct[0].stop();
						((TextZone) switchAct2).setColour(Colours.unselectedZone);
						((TextZone) switchAct1).setColour(Colours.unselectedZone);
						bottomBSelect2 = false;
					} else if (bottomBSelect1){
						animSwitchAct[1].stop();
						((TextZone) switchAct2).setColour(Colours.unselectedZone);
						((TextZone) switchAct1).setColour(Colours.unselectedZone);
						bottomBSelect2 = false;
						bottomBSelect1 = false;

						switchActivity(chosenActivity);
					}

					e.setHandled(tappableHandled);
				}
			}
		};

		switchAct2.rotate((float) (Colours.PI));
		((TextZone) switchAct2).setTextColour(Colours.zoneText);
		((TextZone) switchAct2).setColour(Colours.unselectedZone);
		switchAct2.setGestureEnabled("TAP", true, true);
		switchAct2.setDrawBorder(false);
		sketch.client.addZone(switchAct2);

		animSwitchAct[1] = PropertySetter.createAnimator(animationTime, switchAct2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animSwitchAct[1].setRepeatBehavior(RepeatBehavior.REVERSE);
		animSwitchAct[1].setRepeatCount(Animator.INFINITE);

		//Twitter
		if (activity.equalsIgnoreCase(Languages.activitiesE[0])){
			centerLineFlag = true;

			twitterAct = new TwitterActivity(sketch, this, selectedTopicIndex, lang1, lang2);
			twitterFlag = true;

			//News Headlines
		} else if (activity.equalsIgnoreCase(Languages.activitiesE[1])){
			centerLineFlag = true;

			headlinesAct = new HeadlinesActivity(sketch, this, selectedTopicIndex, lang1, lang2);

			//Pictures
		} else if (activity.equalsIgnoreCase(Languages.activitiesE[2])){

			pictureAct = new PictureActivity(sketch, selectedTopicIndex, lang1, lang2);

			//Videos
		} else if (activity.equalsIgnoreCase(Languages.activitiesE[3])){
			//leftCenterLineFlag = false;
			verticalLineFlag = false;
			youtubeFlag = true;
			videoAct = new VideoActivity(sketch, selectedTopicIndex, lang1, lang2);



			//Picture Game
		} else if (activity.equalsIgnoreCase(Languages.activitiesE[4])){
			centerLineFlag = false;

			pGameAct = new PGame(sketch, selectedTopicIndex);

		}
	}

	/**
	 * Sets the colour of the bar visualization in the middle of each node
	 * @param index
	 * @param randomFlag
	 */
	public void setBarColour(int index, boolean randomFlag){
		
			// Colour indicator
		if(randomFlag){			
			if(graph.randActs[index][0] != 1){
				activityBars1[0] = Colours.twitter;
				activityBars2[0] = Colours.twitter;
			} else {
				activityBars1[0] = Colours.fadedText;
				activityBars2[0] = Colours.fadedText;
			}
			
			if(graph.randActs[index][1] != 1){
				activityBars1[1] = Colours.newsHead;
				activityBars2[1] = Colours.newsHead;
			} else {
				activityBars1[1] = Colours.fadedText;
				activityBars2[1] = Colours.fadedText;
			}
			
			if(graph.randActs[index][2] != 1){
				activityBars1[2] = Colours.pictures;
				activityBars2[2] = Colours.pictures;

			} else {
				activityBars1[2] = Colours.fadedText;
				activityBars2[2] = Colours.fadedText;

			}
			
			if(graph.randActs[index][3] != 1){
				activityBars1[3] = Colours.videos;
				activityBars2[3] = Colours.videos;

			} else {
				activityBars1[3] = Colours.fadedText;
				activityBars2[3] = Colours.fadedText;

			}
			
			if(graph.randActs[index][4] != 1){
				activityBars1[4] = Colours.pGame;
				activityBars2[4] = Colours.pGame;

			} else {
				activityBars1[4] = Colours.fadedText;
				activityBars2[4] = Colours.fadedText;
			}
		} else {
			activityBars1[0] = Colours.twitter;
			activityBars1[1] = Colours.newsHead;
			activityBars1[2] = Colours.pictures;
			activityBars1[3] = Colours.videos;
			activityBars1[4] = Colours.pGame;
			
			activityBars2[0] = Colours.twitter;
			activityBars2[1] = Colours.newsHead;
			activityBars2[2] = Colours.pictures;
			activityBars2[3] = Colours.videos;
			activityBars2[4] = Colours.pGame;
		}
		
		if(sketch.deactivateVideo) {
			activityBars1[3] = Colours.fadedText;
			activityBars2[3] = Colours.fadedText;
		}
		

	}

	/**
	 * Removes all the MainSection zones
	 */
	public void removeAllItems(){
		centerLineFlag = false;
		verticalLineFlag = false;
		edgesFlag = false;

		
		bottomBSelect1 = false;
		bottomBSelect2 = false;
		newLangSelect1 = false;
		newLangSelect2 = false;
		twitterFlag = false;

		for(int i = 0; i < Sketch.NUM_ACTIVITIES; i++){
			activityFlags1[i] = false;
			activityFlags2[i] = false;
			sketch.client.removeZone(activityB1[i]);
			sketch.client.removeZone(activityB2[i]);
		}

		

		sketch.client.removeZone(switchAct1);
		sketch.client.removeZone(switchAct2);
		sketch.client.removeZone(newLang1);
		sketch.client.removeZone(newLang2);
		sketch.client.removeZone(contentPrompt1);
		sketch.client.removeZone(contentPrompt2);
		sketch.client.removeZone(cPromptOverlay1);
		sketch.client.removeZone(cPromptOverlay2);

		graph.removeNodes();
	}

}
