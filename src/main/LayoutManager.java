package main;
import java.awt.Color;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.triggers.TimingTrigger;
import org.jdesktop.animation.timing.triggers.TimingTriggerEvent;

import processing.core.PConstants;
import vialab.simpleMultiTouch.events.HSwipeEvent;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.TextZone;
import vialab.simpleMultiTouch.zones.Zone;
import activities.headlines.HeadlinesActivity;
import activities.pGame.PGame;
import activities.pictures.PictureActivity;
import activities.twitter.TwitterActivity;
import activities.videos.VideoActivity;

import com.memetix.mst.translate.Translate;


public class LayoutManager {
	MainSketch sketch;
	public Graph graph;
	TwitterActivity twitterAct;
	HeadlinesActivity headlinesAct;
	PictureActivity pictureAct;
	VideoActivity videoAct;
	PGame pGameAct;
	

	Animator[] animA1, animA2;
	Animator[] animNext = new Animator[2];
	Animator[] animNextTopic = new Animator[2];
	Animator[] animSwitchTopic = new Animator[2];
	Animator[] animSwitchAct = new Animator[2];
	Animator[] animNewLang = new Animator[2];
	Animator[] animQuestion11, animQuestion12;


	public int animationTime = 1400;
	int animIndex1 = 0, animIndex2 = 0;
	TextZone[] questionZone1, questionZone2;
	TextZone[] activityB1;
	TextZone[] activityB2;

	Zone nextB1, nextB2, randTopicsB1, randTopicsB2, newLang1, newLang2;
	public Zone switchAct1, switchAct2;

	boolean leftCenterLineFlag = true;
	boolean centerLineFlag = true;
	boolean verticalLineFlag = true;
	boolean edgesFlag = false;


	boolean activityBFlag = false;
	boolean next1 = false;
	boolean next2 = false;
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
	//int questionIndex1 = 0;


	String chosenActivity = null;
	boolean[] activityFlags1;
	boolean[] activityFlags2;
	Color[] activityBars1, activityBars2;

	float textOffsetY, textOffsetX, lastNodeX, lastNodeY;

	String lang1, lang2;
	//String[] quest1, quest2;

	float[] start1x, start2x, end1x, end2x, start1y, start2y, 
	end1y, end2y, xMaxTranslation1, yMaxTranslation1,
	xMaxTranslation2, yMaxTranslation2, zoneHeight2;

	final int TIME = 4000;
	final int FINAL_TIME = 1000;
	float maxWidthQuestions;

	public LayoutManager(MainSketch sketch, String lang1, String lang2){
		//this.sketch.client = sketch.client;
		this.sketch = sketch;
		//this.sketch = sketch.client.getParent();
		this.lang1 = lang1;
		this.lang2 = lang2;
		maxWidthQuestions = 3*(sketch.getWidth()-sketch.lineX)/4;
		textOffsetY = sketch.getHeight()/20;
		textOffsetX = sketch.getWidth()/30;

		animQuestion11= new Animator[MainSketch.NUM_QUESTIONS];
		animQuestion12 = new Animator[MainSketch.NUM_QUESTIONS];

		animA1 = new Animator[MainSketch.NUM_ACTIVITIES];
		animA2 = new Animator[MainSketch.NUM_ACTIVITIES];
		activityB1 = new TextZone[MainSketch.NUM_ACTIVITIES];
		activityB2 = new TextZone[MainSketch.NUM_ACTIVITIES];
		activityFlags1 = new boolean[MainSketch.NUM_ACTIVITIES];
		activityFlags2 = new boolean[MainSketch.NUM_ACTIVITIES];
		activityBars1 = new Color[MainSketch.NUM_TOPICS];
		activityBars2 = new Color[MainSketch.NUM_TOPICS];



		// Set up translator
		Translate.setClientId(Colours.clientId);
		Translate.setClientSecret(Colours.clientSecret);
		
		//Set Up Languages
		sketch.learner1 = new Languages(lang1);
		sketch.learner2 = new Languages(lang2);


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

		createNextButtons();

		/*if(lang1.equalsIgnoreCase("English")){
			
			quest1 = sketch.introQuestionsEnglish;
		} else if(lang1.equalsIgnoreCase("French")){
			quest1 = sketch.introQuestionsFrench;
		}

		if(lang2.equalsIgnoreCase("English")){
			quest2 = sketch.introQuestionsEnglish;
		} else if(lang2.equalsIgnoreCase("French")){
			quest2 = sketch.introQuestionsFrench;
		}
*/
		questionZones1();
		questionZones2();
	}
	public void questionZones1() {
		String[] quest1 = sketch.learner1.introQuestions;
		questionZone1 = new TextZone[MainSketch.NUM_QUESTIONS];

		int questionTextSize = 55;
		

		start1x = new float[MainSketch.NUM_QUESTIONS];
		end1x = new float[MainSketch.NUM_QUESTIONS];
		start1y = new float[MainSketch.NUM_QUESTIONS];
		end1y = new float[MainSketch.NUM_QUESTIONS];
		xMaxTranslation1 = new float[MainSketch.NUM_QUESTIONS];
		yMaxTranslation1 = new float[MainSketch.NUM_QUESTIONS];
		for(int i = 0; i < MainSketch.NUM_QUESTIONS; i++){
			String s = quest1[i];
			
			sketch.textFont(Colours.pFont, questionTextSize);
			sketch.textAlign(PConstants.LEFT, PConstants.BOTTOM);
			float textWidth = sketch.textWidth(s);
			boolean changeWidthFlag = false;
			if(textWidth > maxWidthQuestions){
				textWidth = maxWidthQuestions;
				changeWidthFlag = true;
			}

			xMaxTranslation1[i] =  ((sketch.getWidth() - sketch.lineX)/2 - textWidth/2);
			yMaxTranslation1[i] = -(sketch.getHeight()/3);
			start1x[i] = sketch.lineX;
			end1x[i] = xMaxTranslation1[i] + sketch.lineX;
			start1y[i] = sketch.getHeight();
			end1y[i] = sketch.getHeight() + yMaxTranslation1[i];

			float zoneHeight = questionTextSize;
			if(changeWidthFlag) {
				zoneHeight *=3;
			}
			final int ii = i;
			questionZone1[i] = new TextZone(sketch.lineX, sketch.getHeight(), (int)(textWidth*1.2), zoneHeight, Colours.pFont, s, questionTextSize,"LEFT", "BOTTOM" ){
				public void hSwipeEvent(HSwipeEvent e){
					if(isHSwipeable()){


						animQuestion11[animIndex1].stop();

						animQuestion11[animIndex1] = new Animator(FINAL_TIME, this);
						start1x[ii] = this.getX();
						end1x[ii] = this.getX() + (sketch.getWidth()-this.getX());
						start1y[ii] = this.getY();
						end1y[ii] = this.getY();
						animQuestion11[animIndex1].setResolution(5);
						animQuestion11[animIndex1].start();


						animIndex1 = animIndex1 + 1;


						if(animIndex1 == MainSketch.NUM_QUESTIONS){
							//animQuestion11[0].stop();
							animQuestion11[0] = new Animator(TIME, questionZone1[0]);
							animQuestion11[0].setResolution(5);
							questionZone1[0].setXY(sketch.lineX, sketch.getHeight());
							start1x[0] = sketch.lineX;
							end1x[0] = xMaxTranslation1[0] + sketch.lineX;
							start1y[0] = sketch.getHeight();
							end1y[0] = sketch.getHeight() + yMaxTranslation1[0];
							TimingTrigger.addTrigger(animQuestion11[animIndex1-1], animQuestion11[0], TimingTriggerEvent.STOP);

							animIndex1 = 0;
							questionZone1[0].setGestureEnabled("HSwipe", true);
							/*questionZone1[1].setXY(sketch.lineX, sketch.getHeight());
							start1x[1] = sketch.lineX;
							end1x[1] = xMaxTranslation+sketch.lineX;
							start1y[1] = sketch.getHeight();
							end1y[1] = sketch.getHeight()+yMaxTranslation;
							TimingTrigger.addTrigger(animQuestion11[0], animQuestion11[1], TimingTriggerEvent.STOP);*/
						} else if(animIndex1 < MainSketch.NUM_QUESTIONS){
							//animQuestion11[animIndex1].stop();
							animQuestion11[animIndex1] = new Animator(TIME, questionZone1[animIndex1]);
							animQuestion11[animIndex1].setResolution(5);

							questionZone1[animIndex1].setXY(sketch.lineX, sketch.getHeight());
							start1x[animIndex1] = sketch.lineX;
							end1x[animIndex1] = xMaxTranslation1[animIndex1] + sketch.lineX;
							start1y[animIndex1] = sketch.getHeight();
							end1y[animIndex1] = sketch.getHeight() + yMaxTranslation1[animIndex1];
							TimingTrigger.addTrigger(animQuestion11[animIndex1-1], animQuestion11[animIndex1], TimingTriggerEvent.STOP);

							questionZone1[animIndex1].setGestureEnabled("HSwipe", true);

						}

						this.setGestureEnabled("HSwipe", false);
					}
					e.setHandled(true);

				}

				public void timingEvent(float fraction) {
					// Simple linear interpolation to find current position
					float currentX = (start1x[ii] + (end1x[ii] - start1x[ii]) * fraction);
					float currentY = (start1y[ii] + (end1y[ii] - start1y[ii]) * fraction);

					setXY(currentX, currentY);
				}
			};
			questionZone1[i].setGestureEnabled("HSwipe", true);
			questionZone1[i].setDrawBorder(false);
			questionZone1[i].setTextColour(Color.BLACK);
			questionZone1[i].setHSwipeDist(sketch.qSwipeThreshold);
			sketch.client.addZone(questionZone1[i]);




		}
		animQuestion11[0] = new Animator(TIME, questionZone1[0]);
		animQuestion11[0].setResolution(5);
		animQuestion11[0].start();	
	}

	public void questionZones2() {
		String[] quest2 = sketch.learner2.introQuestions;
		questionZone2 = new TextZone[MainSketch.NUM_QUESTIONS];
		final int questionTextSize = 55;
		

		start2x = new float[MainSketch.NUM_QUESTIONS];
		end2x = new float[MainSketch.NUM_QUESTIONS];
		start2y = new float[MainSketch.NUM_QUESTIONS];
		end2y = new float[MainSketch.NUM_QUESTIONS];
		xMaxTranslation2 = new float[MainSketch.NUM_QUESTIONS];
		yMaxTranslation2 = new float[MainSketch.NUM_QUESTIONS];
		zoneHeight2 = new float[MainSketch.NUM_QUESTIONS];



		for(int i = 0; i < MainSketch.NUM_QUESTIONS; i++){
			String s = quest2[i];

			sketch.textFont(Colours.pFont, questionTextSize);
			sketch.textAlign(PConstants.LEFT, PConstants.BOTTOM);
			float textWidth = sketch.textWidth(s);
			
			boolean changeWidthFlag = false;
			if(textWidth > maxWidthQuestions){
				textWidth = maxWidthQuestions;
				changeWidthFlag = true;
			}
			xMaxTranslation2[i] = (float) -((sketch.getWidth() - sketch.lineX)/2 - textWidth/1.5);
			yMaxTranslation2[i] = -(sketch.getHeight()/3);

			zoneHeight2[i] = questionTextSize;
			if(changeWidthFlag) {
				zoneHeight2[i] *=3;
			}

			start2x[i] = sketch.lineX;
			end2x[i] = xMaxTranslation2[i] + sketch.lineX;
			start2y[i] = -zoneHeight2[i];
			end2y[i] = -zoneHeight2[i] + yMaxTranslation2[i];

			
			
			final int ii = i;
			questionZone2[i] = new TextZone(sketch.lineX, -zoneHeight2[i], (int)(textWidth*1.2), zoneHeight2[i], Colours.pFont, s, questionTextSize,"LEFT", "BOTTOM" ){
				public void hSwipeEvent(HSwipeEvent e){
					if(isHSwipeable()){


						animQuestion12[animIndex2].stop();



						animQuestion12[animIndex2] = new Animator(FINAL_TIME, this);
						animQuestion12[animIndex2].setResolution(5);
						start2x[ii] = this.getX();
						end2x[ii] = this.getX() - (sketch.getWidth()-this.getX());
						start2y[ii] = this.getY();
						end2y[ii] = this.getY();
						animQuestion12[animIndex2].start();


						animIndex2 = animIndex2 + 1;



						if(animIndex2 == MainSketch.NUM_QUESTIONS){
							//animQuestion12[0].stop();
							animQuestion12[0] = new Animator(TIME, questionZone2[0]);
							animQuestion12[0].setResolution(5);
							questionZone2[0].setXY(sketch.lineX, sketch.getHeight());
							start2x[0] = sketch.lineX;
							end2x[0] = xMaxTranslation2[0] + sketch.lineX;
							start2y[0] = -zoneHeight2[0];
							end2y[0] = -zoneHeight2[0] + yMaxTranslation2[0];
							TimingTrigger.addTrigger(animQuestion12[animIndex2-1], animQuestion12[0], TimingTriggerEvent.STOP);

							animIndex2 = 0;
							questionZone2[0].setGestureEnabled("HSwipe", true);
							/*questionZone2[1].setXY(sketch.lineX, sketch.getHeight());
								start2x[1] = sketch.lineX;
								end2x[1] = xMaxTranslation+sketch.lineX;
								start2y[1] = sketch.getHeight();
								end2y[1] = sketch.getHeight()+yMaxTranslation;
								TimingTrigger.addTrigger(animQuestion12[0], animQuestion12[1], TimingTriggerEvent.STOP);*/
						} else if(animIndex2 < MainSketch.NUM_QUESTIONS){
							//animQuestion12[animIndex2].stop();
							animQuestion12[animIndex2] = new Animator(TIME, questionZone2[animIndex2]);
							animQuestion12[animIndex2].setResolution(5);

							questionZone2[animIndex2].setXY(sketch.lineX, sketch.getHeight());
							start2x[animIndex2] = sketch.lineX;
							end2x[animIndex2] = xMaxTranslation2[animIndex2] + sketch.lineX;
							start2y[animIndex2] = -zoneHeight2[animIndex2];
							end2y[animIndex2] = -zoneHeight2[animIndex2] + yMaxTranslation2[animIndex2];
							TimingTrigger.addTrigger(animQuestion12[animIndex2-1], animQuestion12[animIndex2], TimingTriggerEvent.STOP);

							questionZone2[animIndex2].setGestureEnabled("HSwipe", true);

						}
						this.setGestureEnabled("HSwipe", false);
					}
					e.setHandled(true);

				}

				public void timingEvent(float fraction) {
					// Simple linear interpolation to find current position
					float currentX = (start2x[ii] + (end2x[ii] - start2x[ii]) * fraction);
					float currentY = (start2y[ii] + (end2y[ii] - start2y[ii]) * fraction);

					setXY(currentX, currentY);
				}
			};
			questionZone2[i].setGestureEnabled("HSwipe", true);
			questionZone2[i].setDrawBorder(false);
			questionZone2[i].setTextColour(Color.BLACK);
			questionZone2[i].rotate((float) Colours.PI);
			questionZone2[i].setHSwipeDist(sketch.qSwipeThreshold);
			sketch.client.addZone(questionZone2[i]);

		}
		animQuestion12[0] = new Animator(TIME, questionZone2[0]);
		animQuestion12[0].setResolution(5);
		animQuestion12[0].start();	
	}
	public void drawLayout(){
		if(verticalLineFlag){
			sketch.strokeWeight(5);
			sketch.stroke(Colours.lineColour.getRed(), Colours.lineColour.getGreen(), Colours.lineColour.getBlue());
			sketch.line(sketch.lineX, sketch.getY(), sketch.lineX, sketch.getHeight());
		}

		if(leftCenterLineFlag){
			sketch.strokeWeight(5);
			sketch.stroke(Colours.lineColour.getRed(), Colours.lineColour.getGreen(), Colours.lineColour.getBlue());
			sketch.line(sketch.getX(), sketch.getHeight()/2, sketch.lineX, sketch.getHeight()/2);
		}

		//Half Screen separator
		if(centerLineFlag){
			sketch.strokeWeight(5);
			sketch.stroke(Colours.lineColour.getRed(), Colours.lineColour.getGreen(), Colours.lineColour.getBlue());
			sketch.line(sketch.lineX, sketch.getHeight()/2, sketch.getWidth(), sketch.getHeight()/2);
		}
		//graph edges (lines between nodes)
		if (edgesFlag){
			sketch.strokeWeight(5);
			sketch.stroke(Colours.lineColour.getRed(), Colours.lineColour.getGreen(), Colours.lineColour.getBlue());
			graph.displayEdges();
		}
	}



	public void createNextButtons(){
		//New next1 Button for user 1
		//String s1 = "";
		//String s2 = "";

		/*if(lang1.equalsIgnoreCase("English")){
			s1 = sketch.nextE;
		} else if(lang1.equalsIgnoreCase("French")){
			s1 = sketch.nextF;
		}*/
		nextB1 = new TextZone(buttonX, buttonYb, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.next, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable()){

					if(!next1 && !next2){
						animNext[1].start();
						this.setColour(Colours.selectedZone);
						next1 = true;
					} else if (next1 && !next2){
						animNext[1].stop();
						((TextZone) nextB2).setColour(Colours.unselectedZone);
						((TextZone) nextB1).setColour(Colours.unselectedZone);
						next1 = false;
					} else if (next2){
						animNext[0].stop();
						animNext[1].stop();
						((TextZone) nextB2).setColour(Colours.unselectedZone);
						((TextZone) nextB1).setColour(Colours.unselectedZone);
						next2 = false;
						next1 = false;
						removeQuestions();
						createRandomizeButtons();
						createNewLanguageButtons();
						createGraph();
						createActivityButtons();

					}

					e.setHandled(tappableHandled);

				}
			}
		};

		((TextZone) nextB1).setTextColour(Colours.zoneText);
		((TextZone) nextB1).setColour(Colours.unselectedZone);
		nextB1.setGestureEnabled("TAP", true, true);
		nextB1.setDrawBorder(false);
		sketch.client.addZone(nextB1);

		animNext[0] = PropertySetter.createAnimator(animationTime, nextB1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animNext[0].setRepeatBehavior(RepeatBehavior.REVERSE);
		animNext[0].setRepeatCount(Animator.INFINITE);



		//New next2 Button for user 2

		/*if(lang2.equalsIgnoreCase("English")){
			s1 = sketch.nextE;
		} else if(lang2.equalsIgnoreCase("French")){
			s1 = sketch.nextF;
		}*/
		nextB2  = new TextZone(buttonX, buttonYt, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.next, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable()){

					if(!next2 && !next1){
						animNext[0].start();
						this.setColour(Colours.selectedZone);
						next2 = true;
					} else if (next2 && !next1){
						animNext[0].stop();
						((TextZone) nextB2).setColour(Colours.unselectedZone);
						((TextZone) nextB1).setColour(Colours.unselectedZone);
						next2 = false;
					} else if (next1){
						animNext[1].stop();
						animNext[0].stop();
						((TextZone) nextB2).setColour(Colours.unselectedZone);
						((TextZone) nextB1).setColour(Colours.unselectedZone);
						next2 = false;
						next1 = false;
						removeQuestions();
						createRandomizeButtons();
						createNewLanguageButtons();
						createGraph();
						createActivityButtons();
					}

					e.setHandled(tappableHandled);
				}
			}
		};

		nextB2.rotate((float) (Colours.PI));
		((TextZone) nextB2).setTextColour(Colours.zoneText);
		((TextZone) nextB2).setColour(Colours.unselectedZone);
		nextB2.setGestureEnabled("TAP", true, true);
		nextB2.setDrawBorder(false);
		sketch.client.addZone(nextB2);

		animNext[1] = PropertySetter.createAnimator(animationTime, nextB2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animNext[1].setRepeatBehavior(RepeatBehavior.REVERSE);
		animNext[1].setRepeatCount(Animator.INFINITE);

	}

	public void removeQuestions(){
		sketch.client.removeZone(nextB1);
		sketch.client.removeZone(nextB2);

		for(int i = 0; i < MainSketch.NUM_QUESTIONS; i++){
			sketch.client.removeZone(questionZone1[i]);
			sketch.client.removeZone(questionZone2[i]);
		}
	}

	public void createRandomizeButtons(){		
		//String s = "";
		//New randomize topics Button for user 1

		/*if(lang1.equalsIgnoreCase("English")){
			s = sketch.ranTopicsE;
		} else if(lang1.equalsIgnoreCase("French")){
			s = sketch.ranTopicsF;
		}*/
		randTopicsB1 = new TextZone(buttonX, buttonYb, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.ranTopics, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable()){
					/*if(!bottomBSelect1 && !bottomBSelect2){
						animRand[1].start();
						this.setColour(Colours.selectedZone);
						bottomBSelect1 = true;
					} else if (bottomBSelect1 && !bottomBSelect2){
						animRand[1].stop();
						((TextZone) randTopicsB2).setColour(Colours.unselectedZone);
						((TextZone) randTopicsB1).setColour(Colours.unselectedZone);
						bottomBSelect1 = false;
					} else if (bottomBSelect2){
						animRand[0].stop();
						((TextZone) randTopicsB2).setColour(Colours.unselectedZone);
						((TextZone) randTopicsB1).setColour(Colours.unselectedZone);
						bottomBSelect2 = false;
						bottomBSelect1 = false;
						//TODO
					}*/

					e.setHandled(tappableHandled);

				}
			}
		};

		((TextZone) randTopicsB1).setTextColour(Colours.fadedText);
		((TextZone) randTopicsB1).setColour(Colours.fadedOutZone);
		randTopicsB1.setGestureEnabled("TAP", true, true);
		randTopicsB1.setDrawBorder(false);
		sketch.client.addZone(randTopicsB1);

		/*animRand[0] = PropertySetter.createAnimator(animationTime, randTopicsB1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animRand[0].setRepeatBehavior(RepeatBehavior.REVERSE);
		animRand[0].setRepeatCount(Animator.INFINITE);*/


		//New randomize topics Button for user 2
		/*if(lang2.equalsIgnoreCase("English")){
			s = sketch.ranTopicsE;
		} else if(lang2.equalsIgnoreCase("French")){
			s = sketch.ranTopicsF;
		}*/
		randTopicsB2  = new TextZone(buttonX, buttonYt, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.ranTopics, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable()){


					/*if(!bottomBSelect2 && !bottomBSelect1){
						animRand[0].start();
						((TextZone) randTopicsB2).setColour(Colours.selectedZone);
						bottomBSelect2 = true;
					} else if (bottomBSelect2 && !bottomBSelect1){
						animRand[0].stop();
						((TextZone) randTopicsB2).setColour(Colours.unselectedZone);
						((TextZone) randTopicsB1).setColour(Colours.unselectedZone);
						bottomBSelect2 = false;
					} else if (bottomBSelect1){
						animRand[1].stop();
						((TextZone) randTopicsB2).setColour(Colours.unselectedZone);
						((TextZone) randTopicsB1).setColour(Colours.unselectedZone);
						bottomBSelect2 = false;
						bottomBSelect1 = false;
						//TODO
					}*/
					e.setHandled(tappableHandled);
				}
			}
		};

		randTopicsB2.rotate((float) (Colours.PI));
		((TextZone) randTopicsB2).setTextColour(Colours.fadedText);
		((TextZone) randTopicsB2).setColour(Colours.fadedOutZone);
		randTopicsB2.setGestureEnabled("TAP", true, true);
		randTopicsB2.setDrawBorder(false);
		sketch.client.addZone(randTopicsB2);

		/*animRand[1] = PropertySetter.createAnimator(animationTime, randTopicsB2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animRand[1].setRepeatBehavior(RepeatBehavior.REVERSE);
		animRand[1].setRepeatCount(Animator.INFINITE);*/

	}

	public void createNewLanguageButtons(){
		//String s = "";

		//New language Button for user 1
		/*if(lang1.equalsIgnoreCase("English")){
			s = sketch.newLangE;
		} else if(lang1.equalsIgnoreCase("French")){
			s = sketch.newLangF;
		}*/

		newLang1 = new TextZone(buttonX, buttonYb2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.newLang, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable()){

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

						///////////////////////////////
						//TODO
						//sketch.startScreen.chooseNewLang();
					}

					e.setHandled(tappableHandled);

				}
			}
		};

		((TextZone) newLang1).setTextColour(Colours.fadedText);
		((TextZone) newLang1).setColour(Colours.fadedOutZone);

		//((TextZone) newLang1).setTextColour(Colours.zoneText);
		//((TextZone) newLang1).setColour(Colours.unselectedZone);
		//newLang1.setGestureEnabled("TAP", true, true);
		newLang1.setDrawBorder(false);
		sketch.client.addZone(newLang1);

		/*animNewLang[0] = PropertySetter.createAnimator(animationTime, newLang1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animNewLang[0].setRepeatBehavior(RepeatBehavior.REVERSE);
		animNewLang[0].setRepeatCount(Animator.INFINITE);*/

		//New language Button for user 2
		/*if(lang2.equalsIgnoreCase("English")){
			s = sketch.newLangE;
		} else if(lang2.equalsIgnoreCase("French")){
			s = sketch.newLangF;
		}*/

		newLang2 = new TextZone(buttonX, buttonYt2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.newLang, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable()){

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
						//sketch.startScreen.chooseNewLang();
					}

					e.setHandled(tappableHandled);

				}
			}
		};

		((TextZone) newLang2).setTextColour(Colours.fadedText);
		((TextZone) newLang2).setColour(Colours.fadedOutZone);

		newLang2.rotate((float) (Colours.PI));
		//((TextZone) newLang2).setTextColour(Colours.zoneText);
		//((TextZone) newLang2).setColour(Colours.unselectedZone);
		//newLang2.setGestureEnabled("TAP", true, true);
		newLang2.setDrawBorder(false);
		sketch.client.addZone(newLang2);

		/*animNewLang[1] = PropertySetter.createAnimator(animationTime, newLang2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animNewLang[1].setRepeatBehavior(RepeatBehavior.REVERSE);
		animNewLang[1].setRepeatCount(Animator.INFINITE);*/
	}
	public void createGraph(){
		graph = new Graph(sketch, this, sketch.lineX, lang1, lang2);
		centerLineFlag = false;
		edgesFlag = true;

	}

	public void enableActivityButtons(int index, String topic, boolean randFlag, int randIndex){
		selectedTopicIndex = index;
		//selectedTopic = English.topics[index];
		activityBFlag = true;

		for(int i = 0; i < MainSketch.NUM_ACTIVITIES; i++){


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


			//TODO
			//Delete when web search is implemented
			/*if(i == 4){
				activityB1[i].setGestureEnabled("Tap", false);
				activityB2[i].setGestureEnabled("Tap", false);
				activityB1[i].setTextColour(Colours.fadedText);
				activityB2[i].setTextColour(Colours.fadedText);
				activityB1[i].setColour(Colours.fadedOutZone);
				activityB2[i].setColour(Colours.fadedOutZone);
			}*/

		}

	}

	public void switchActivity(String activity){
		for(int i = 0; i < MainSketch.NUM_ACTIVITIES; i++){
			activityB1[i].setActive(true);
			activityB2[i].setActive(true);
		}

		graph.nodes[graph.lastSelectedNode].setX(lastNodeX);
		graph.nodes[graph.lastSelectedNode].setY(lastNodeY);

		newLang1.setActive(true);
		newLang2.setActive(true);
		leftCenterLineFlag = true;
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
			leftCenterLineFlag = true;
			videoAct.removeZones();

			//Web Search
		} else if (activity.equalsIgnoreCase(Languages.activitiesE[4])){
			pGameAct.removeZones();
		}

		edgesFlag = true;
		centerLineFlag = false;
		graph.activateNodes();


		switchAct1.setActive(false);
		switchAct2.setActive(false);

	}



	public void createActivityButtons(){

		for(int i = 0; i < MainSketch.NUM_TOPICS; i++){
			activityBars1[i] = Colours.fadedText;
			activityBars2[i] = Colours.fadedText;
		}


		//String[] activityString = null;

		/*if(lang1.equalsIgnoreCase("English")){
			activityString = sketch.activitiesE;
		} else if(lang1.equalsIgnoreCase("French")){
			activityString = sketch.activitiesF;
		}*/

		int width = (int)((sketch.getWidth()-sketch.lineX)/5.1);
		int offset = 2;

		int x;
		int y = sketch.getHeight() - actBHeight - offset;

		for(int i = 0; i < MainSketch.NUM_ACTIVITIES; i++){
			final int index = i;

			x = (int) (sketch.getWidth()/5.8) + index*(int)((sketch.getWidth()-sketch.lineX)/5.0);

			activityB1[i] = new TextZone(x, y, width, actBHeight,
					Colours.pFont, sketch.learner1.activities[index], sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (getTappable() && activityBFlag){
						for(int j = 0; j < MainSketch.NUM_ACTIVITIES; j++){
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

		/*if(lang2.equalsIgnoreCase("English")){
			activityString = sketch.activitiesE;
		} else if(lang2.equalsIgnoreCase("French")){
			activityString = sketch.activitiesF;
		}*/

		y = sketch.getY() + offset;

		for(int i = 0; i < MainSketch.NUM_ACTIVITIES; i++){
			final int index = i;
			x = (int) (sketch.getWidth()/5.8) + index*(int)((sketch.getWidth()-sketch.lineX)/5.0);

			activityB2[i] = new TextZone(x, y, width, actBHeight,
					Colours.pFont, sketch.learner2.activities[index], sketch.textSize, "CENTER", "CENTER"){

				public void tapEvent(TapEvent e){
					if (getTappable() && activityBFlag){
						for(int j = 0; j < MainSketch.NUM_ACTIVITIES; j++){
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

	public void initializeActivityScreen(String activity){

		lastNodeX = graph.nodes[graph.lastSelectedNode].getX();
		lastNodeY = graph.nodes[graph.lastSelectedNode].getY();
		graph.nodes[graph.lastSelectedNode].setXY(sketch.lineX/2 - graph.nodes[graph.lastSelectedNode].getWidth()/2, sketch.getHeight()/2 - graph.nodes[graph.lastSelectedNode].getHeight()/2);

		for(int i = 0; i < MainSketch.NUM_ACTIVITIES; i++){
			activityFlags1[i] = false;
			activityFlags2[i] = false;
			activityB1[i].setActive(false);
			activityB2[i].setActive(false);			
		}

		newLang1.setActive(false);
		newLang2.setActive(false);


		chosenActivity = activity;

		edgesFlag = false;
		leftCenterLineFlag = false;
		graph.inactivateNodes();
		//String s ="";

		//New switch activity Button for user 1
		/*if(lang1.equalsIgnoreCase("English")){
			s = sketch.choAnoActE;
		} else if(lang1.equalsIgnoreCase("French")){
			s = sketch.choAnoActF;
		}*/
		switchAct1 = new TextZone(buttonX, buttonYb, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.choAnoAct, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable()){

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

		//New switch activity Button for user 2
		/*if(lang2.equalsIgnoreCase("English")){
			s = sketch.choAnoActE;
		} else if(lang2.equalsIgnoreCase("French")){
			s = sketch.choAnoActF;
		}*/

		switchAct2  = new TextZone(buttonX, buttonYt, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.choAnoAct, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable()){
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
			leftCenterLineFlag = false;
			verticalLineFlag = false;
			youtubeFlag = true;
			videoAct = new VideoActivity(sketch, selectedTopicIndex, lang1, lang2);



			//Picture Game
		} else if (activity.equalsIgnoreCase(Languages.activitiesE[4])){
			centerLineFlag = false;

			pGameAct = new PGame(sketch, selectedTopicIndex, lang1, lang2);

		}
	}

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
		

	}

	public void removeAllItems(){
		centerLineFlag = false;
		verticalLineFlag = false;
		edgesFlag = false;

		next1 = false;
		next2 = false;
		bottomBSelect1 = false;
		bottomBSelect2 = false;
		newLangSelect1 = false;
		newLangSelect2 = false;
		twitterFlag = false;

		for(int i = 0; i < MainSketch.NUM_ACTIVITIES; i++){
			activityFlags1[i] = false;
			activityFlags2[i] = false;
			sketch.client.removeZone(activityB1[i]);
			sketch.client.removeZone(activityB2[i]);
		}

		sketch.client.removeZone(nextB1);
		sketch.client.removeZone(nextB2);

		sketch.client.removeZone(randTopicsB1);
		sketch.client.removeZone(randTopicsB2);
		sketch.client.removeZone(switchAct1);
		sketch.client.removeZone(switchAct2);
		sketch.client.removeZone(newLang1);
		sketch.client.removeZone(newLang2);

		graph.removeNodes();
	}

}
