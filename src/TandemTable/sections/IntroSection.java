package TandemTable.sections;

import java.awt.Color;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.triggers.TimingTrigger;
import org.jdesktop.animation.timing.triggers.TimingTriggerEvent;

import TandemTable.ColourEval;
import TandemTable.Colours;
import TandemTable.Sketch;
import TandemTable.sections.mainSection.MainSection;
import processing.core.PConstants;
import vialab.simpleMultiTouch.events.HSwipeEvent;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.TextZone;

/**
 * In the IntroSectoin, users are prompted with questions
 * to make them get to know each other, and how they can
 * structure their learning session.
 *
 */
public class IntroSection {

	Sketch sketch;
	MainSection mainSection;
	TextZone[] questionZone1, questionZone2;
	public TextZone nextB1, nextB2;

	Animator[] animQuestion11, animQuestion12;
	Animator[] animNext = new Animator[2];
	
	float[] start1x, start2x, end1x, end2x, start1y, start2y, 
	end1y, end2y, xMaxTranslation1, yMaxTranslation1,
	xMaxTranslation2, yMaxTranslation2, zoneHeight2;
	
	float maxWidthQuestions;
	
	int animIndex1 = 0, animIndex2 = 0;


	final int TIME = 4000, FINAL_TIME = 1000;

	public boolean next1 = false, next2 = false;
	
	/**
	 * Creates the next buttons and the questions textZones
	 * @param sketch
	 * @param mainSection
	 */
	public IntroSection(Sketch sketch, MainSection mainSection){
		this.sketch = sketch;
		this.mainSection = mainSection;
		animQuestion11= new Animator[Sketch.NUM_QUESTIONS];
		animQuestion12 = new Animator[Sketch.NUM_QUESTIONS];
		maxWidthQuestions = 3*(sketch.getWidth()-sketch.lineX)/4;
		
		createNextButtons();
		createQuestionZones1();
		createQuestionZones2();
		sketch.doneIntro = true;
	}
	
	/**
	 * Creates the 'Next' buttons to go from the introSection
	 * to the mainSection of TandemTable
	 */
	public void createNextButtons(){
		//New next1 Button for user 1
		//String s1 = "";
		//String s2 = "";

		/*if(lang1.equalsIgnoreCase("English")){
			s1 = sketch.nextE;
		} else if(lang1.equalsIgnoreCase("French")){
			s1 = sketch.nextF;
		}*/
		
		nextB1 = new TextZone(mainSection.buttonX, mainSection.buttonYb, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.next, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){

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
						mainSection.createSwitchLanguageButtons();
						mainSection.createGraph();
						mainSection.createActivityButtons();

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

		animNext[0] = PropertySetter.createAnimator(mainSection.animationTime, nextB1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animNext[0].setRepeatBehavior(RepeatBehavior.REVERSE);
		animNext[0].setRepeatCount(Animator.INFINITE);



		//New next2 Button for user 2

		/*if(lang2.equalsIgnoreCase("English")){
			s1 = sketch.nextE;
		} else if(lang2.equalsIgnoreCase("French")){
			s1 = sketch.nextF;
		}*/
		nextB2  = new TextZone(mainSection.buttonX, mainSection.buttonYt, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.next, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (isTappable()){

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
						mainSection.createSwitchLanguageButtons();
						mainSection.createGraph();
						mainSection.createActivityButtons();
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

		animNext[1] = PropertySetter.createAnimator(mainSection.animationTime, nextB2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animNext[1].setRepeatBehavior(RepeatBehavior.REVERSE);
		animNext[1].setRepeatCount(Animator.INFINITE);

	}
	
	/**
	 * Create the TextZones used to display the user 1 prompts (questions)
	 */
	public void createQuestionZones1() {
		String[] quest1 = sketch.learner1.introQuestions;
		questionZone1 = new TextZone[Sketch.NUM_QUESTIONS];

		int questionTextSize = 55;
		

		start1x = new float[Sketch.NUM_QUESTIONS];
		end1x = new float[Sketch.NUM_QUESTIONS];
		start1y = new float[Sketch.NUM_QUESTIONS];
		end1y = new float[Sketch.NUM_QUESTIONS];
		xMaxTranslation1 = new float[Sketch.NUM_QUESTIONS];
		yMaxTranslation1 = new float[Sketch.NUM_QUESTIONS];
		
		for(int i = 0; i < Sketch.NUM_QUESTIONS; i++){
			String s =  quest1[i];
			
			sketch.textFont(Colours.pFont, questionTextSize);
			sketch.textAlign(PConstants.LEFT, PConstants.BOTTOM);
			float textWidth = sketch.textWidth(s);
			//System.out.println(textWidth);
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
				zoneHeight *= 3;
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


						if(animIndex1 == Sketch.NUM_QUESTIONS){
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
						} else if(animIndex1 < Sketch.NUM_QUESTIONS){
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

	/**
	 * Create the TextZones used to display the user 2 prompts (questions)
	 */
	public void createQuestionZones2() {
		String[] quest2 = sketch.learner2.introQuestions;
		questionZone2 = new TextZone[Sketch.NUM_QUESTIONS];
		final int questionTextSize = 55;
		

		start2x = new float[Sketch.NUM_QUESTIONS];
		end2x = new float[Sketch.NUM_QUESTIONS];
		start2y = new float[Sketch.NUM_QUESTIONS];
		end2y = new float[Sketch.NUM_QUESTIONS];
		xMaxTranslation2 = new float[Sketch.NUM_QUESTIONS];
		yMaxTranslation2 = new float[Sketch.NUM_QUESTIONS];
		zoneHeight2 = new float[Sketch.NUM_QUESTIONS];



		for(int i = 0; i < Sketch.NUM_QUESTIONS; i++){
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



						if(animIndex2 == Sketch.NUM_QUESTIONS){
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
						} else if(animIndex2 < Sketch.NUM_QUESTIONS){
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
	
	/**
	 * Remove the user prompts TextZones
	 */
	public void removeQuestions(){
		sketch.client.removeZone(nextB1);
		sketch.client.removeZone(nextB2);

		for(int i = 0; i < Sketch.NUM_QUESTIONS; i++){
			sketch.client.removeZone(questionZone1[i]);
			sketch.client.removeZone(questionZone2[i]);
		}
	}
}
