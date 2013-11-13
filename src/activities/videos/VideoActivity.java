package activities.videos;


import java.awt.Color;
import java.util.ArrayList;

import main.ColourEval;
import main.Colours;
import main.Languages;
import main.MainSketch;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.CurrentZone;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.TextZone;
import vialab.simpleMultiTouch.zones.Zone;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoFeed;

public class VideoActivity {
	MainSketch sketch;
	VideoPlayer videoPlayer1, videoPlayer2;
	
	int videoControlsX, videSpaceX, videoWidth, startIndex1 = 1, topicIndex, totalResults = 0;
	boolean errorFlag = false, playerActive = false, videoFlag1 = false, videoFlag2 = false;
	boolean mvFlag1 = false, mvFlag2 = false, playFlag1 = false, playFlag2 = false, stopFlag1 = false, stopFlag2 = false, pauseFlag1 = false, pauseFlag2 = false;
	boolean cLang1 = false, cLang2 = false;
	TextZone play1, pause1, stop1, play2, pause2, stop2, errorZone; 
	VideoActivity vAct;
	CurrentZone current1, current2;
	VideoGetter vGetter;

	TextZone moreVideos1, moreVideos2, changeL1, changeL2; 
	RectZone coverRect, cBackground;

	VideoFeed videoFeed1 = null;

	final int MAX_RESULTS = 8;
	final int TIME_CURRENT = 25000;
	final static String VIDEOS_FEED = "http://gdata.youtube.com/feeds/api/videos";
	float polyW;
	
	Animator animMV1, animMV2, animPlay1, animPlay2, animPause1, animPause2, animStop1, animStop2, animCL1, animCL2;

	String lang1, lang2;//, playStr1, playStr2, pauseStr1, pauseStr2, stopStr1, stopStr2;
	String topicsScrambled, topic, currentL;
	
	boolean bothL = false;

	public VideoActivity(MainSketch sketch, int topicIndex, String lang1, String lang2){
		this.sketch = sketch;
		this.topicIndex = topicIndex;
		this.polyW = (sketch.getWidth()-sketch.lineX)/8;
		this.lang1 = lang1;
		this.lang2 = lang2;
		
		
		/*if(lang1.equalsIgnoreCase("English")){
			playStr1 = sketch.playE;
			pauseStr1 = sketch.pauseE;
			stopStr1 = sketch.stopE;
		} else if(lang1.equalsIgnoreCase("French")){
			playStr1 = sketch.playF;
			pauseStr1 = sketch.pauseF;
			stopStr1 = sketch.stopF;
		}

		if(lang2.equalsIgnoreCase("English")){
			playStr2 = sketch.playE;
			pauseStr2 = sketch.pauseE;
			stopStr2 = sketch.stopE;
		} else if(lang2.equalsIgnoreCase("French")){
			playStr2 = sketch.playF;
			pauseStr2 = sketch.pauseF;
			stopStr2 = sketch.stopF;
		}*/
		
		if((lang1.equalsIgnoreCase("English") && lang2.equalsIgnoreCase("French"))
				|| (lang2.equalsIgnoreCase("English") && lang1.equalsIgnoreCase("French"))){
			bothL = true;	
			currentL = "en";
		} 
		String[] scrambled = null;
		
		if(lang1.equalsIgnoreCase("English") || lang2.equalsIgnoreCase("English")){
			scrambled = sketch.scrambleStrings(Languages.topicsExpandedE[topicIndex]);
			topic = Languages.topicsE[topicIndex];
		} else if(lang1.equalsIgnoreCase("French") || lang2.equalsIgnoreCase("French")){
			scrambled = sketch.scrambleStrings(Languages.topicsExpandedF[topicIndex]);
			topic = Languages.topicsF[topicIndex];

		}
		topicsScrambled = "";
		
		for(String s: scrambled){
			topicsScrambled += s + " || ";
		}
	
		topicsScrambled = topicsScrambled.substring(0, topicsScrambled.length() - 4);
		vAct = this;
		
		if(!sketch.youTubeInit){
			sketch.myService = new YouTubeService(Colours.appNameTube, Colours.apiKeyTube);
			sketch.myService.setReadTimeout(sketch.TIMEOUT);
			sketch.myService.setConnectTimeout(sketch.TIMEOUT);
			
			sketch.youTubeInit = true;
		}


		if(sketch.myService == null){
			errorFlag = true;
			System.out.println("YouTubeService failed to initialize.");
		} else {
			createCurrent1();
			createCurrent2();
			createLeftWhiteRect();
			createMoreVidoes();
			if(bothL){
				createChangeLang();
			}

			sketch.client.pullToTop(sketch.layout.graph.nodes[sketch.layout.graph.lastSelectedNode]);
			sketch.client.pullToTop(sketch.layout.switchAct1);
			sketch.client.pullToTop(sketch.layout.switchAct2);
			videoWidth = (int) ((sketch.getWidth()-sketch.lineX)/1.5);
			videSpaceX = sketch.getWidth()-sketch.lineX - videoWidth - sketch.buttonWidth;
			this.videoControlsX = sketch.getWidth() - sketch.buttonWidth - videSpaceX/2;

			createRightWhiteRect();
			createVideoControlButtons1();
			createVideoControlButtons2();
			

			vGetter = new VideoGetter(sketch, this, sketch.lineX, videoWidth);
			vGetter.start();
			
			//if(errorFlag == false){
			//VideoPlayer.loadVLClibs();
			//}
		}
	}
	
	public void createChangeLang(){
		
		changeL1 = new TextZone(sketch.layout.buttonX, sketch.layout.buttonYb3, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.newLang, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable() && !errorFlag){
					if(cLang2){
						cLang2 = false;
						animCL1.stop();

						changeL1.setTextColour(Colours.zoneText);
						changeL1.setColour(Colours.unselectedZone);
						changeL2.setTextColour(Colours.zoneText);
						changeL2.setColour(Colours.unselectedZone);
						changeLang();
					} else if(cLang1){
						cLang1 = false;
						animCL2.stop();
						changeL2.setColour(Colours.unselectedZone);

					} else {
						cLang1 = true;
						animCL2.start();
						changeL1.setColour(Colours.selectedZone);

					}

					e.setHandled(tappableHandled);

				}
			}
		};

		changeL1.setTextColour(Colours.zoneText);
		changeL1.setColour(Colours.unselectedZone);
		changeL1.setGestureEnabled("TAP", true, true);
		changeL1.setDrawBorder(false);
		sketch.client.addZone(changeL1);
		
		animCL1 = PropertySetter.createAnimator(sketch.layout.animationTime, changeL1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animCL1.setRepeatBehavior(RepeatBehavior.REVERSE);
		animCL1.setRepeatCount(Animator.INFINITE);
		
		
		
		
		changeL2 = new TextZone(sketch.layout.buttonX, sketch.layout.buttonYt3, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.newLang, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable() && !errorFlag){
					if(cLang1){
						cLang1 = false;
						animCL2.stop();

						changeL1.setTextColour(Colours.zoneText);
						changeL1.setColour(Colours.unselectedZone);
						changeL2.setTextColour(Colours.zoneText);
						changeL2.setColour(Colours.unselectedZone);
						changeLang();
					} else if(cLang2){
						cLang2 = false;
						animCL1.stop();
						changeL1.setColour(Colours.unselectedZone);

					} else {
						cLang2 = true;
						animCL1.start();
						changeL2.setColour(Colours.selectedZone);

					}

					e.setHandled(tappableHandled);

				}
			}
		};

		changeL2.setTextColour(Colours.zoneText);
		changeL2.setColour(Colours.unselectedZone);
		
		changeL2.setGestureEnabled("TAP", true, true);
		changeL2.setDrawBorder(false);
		changeL2.rotate((float) Colours.PI);

		sketch.client.addZone(changeL2);
		
		animCL2 = PropertySetter.createAnimator(sketch.layout.animationTime, changeL2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animCL2.setRepeatBehavior(RepeatBehavior.REVERSE);
		animCL2.setRepeatCount(Animator.INFINITE);
		
		
	}
	
	public void changeLang(){
		if(currentL.equalsIgnoreCase("fr")){
			String[] scrambled = sketch.scrambleStrings(Languages.topicsExpandedE[topicIndex]);
			topic = Languages.topicsE[topicIndex];
			
			topicsScrambled = "";
			
			for(String s: scrambled){
				topicsScrambled += s + " || ";
			}
		
			topicsScrambled = topicsScrambled.substring(0, topicsScrambled.length() - 4);
			currentL = "en";
		} else if(currentL.equalsIgnoreCase("en")){
			String[] scrambled = sketch.scrambleStrings(Languages.topicsExpandedF[topicIndex]);
			topic = Languages.topicsF[topicIndex];
			
			topicsScrambled = "";
			
			for(String s: scrambled){
				topicsScrambled += s + " || ";
			}
		
			topicsScrambled = topicsScrambled.substring(0, topicsScrambled.length() - 4);
			currentL = "fr";
		} 
		
		vGetter.removeZones();
		vGetter = new VideoGetter(sketch, vAct, sketch.lineX, videoWidth);
		vGetter.start();
	}

	public void createLeftWhiteRect(){
		//Left side
		coverRect = new RectZone(sketch.lineX-polyW, 0, polyW, sketch.getHeight());
		coverRect.setColour(Color.WHITE);
		coverRect.setDrawBorder(false);
		sketch.client.addZone(coverRect);


	}

	public void createRightWhiteRect(){
		//Right side
		int whiteWidth = sketch.getWidth()-sketch.lineX;
		cBackground = new RectZone(sketch.lineX, 0, whiteWidth, sketch.getHeight());
		cBackground.setColour(Color.WHITE);
		cBackground.setActive(false);
		cBackground.setDrawBorder(false);

		sketch.client.addZone(cBackground);

	}

	public void createMoreVidoes(){
		//String s = "";
		
		/*if(lang1.equalsIgnoreCase("English")){
			s = sketch.moreVideosE;
		} else if(lang1.equalsIgnoreCase("French")){
			s = sketch.moreVideosF;
		}*/
		
		moreVideos1 = new TextZone(sketch.layout.buttonX, sketch.layout.buttonYb2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.moreVideos, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable() && !errorFlag && vGetter.imgsLoaded1){
					if(mvFlag2){
						mvFlag2 = false;
						animMV1.stop();
						moreVideos1.setColour(Colours.unselectedZone);

						vGetter.removeZones();
						moreVideos1.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
						moreVideos1.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
						moreVideos2.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
						moreVideos2.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
						vGetter = new VideoGetter(sketch, vAct, sketch.lineX, videoWidth);
						vGetter.start();
					} else if(mvFlag1){
						mvFlag1 = false;
						animMV2.stop();
						moreVideos2.setColour(Colours.unselectedZone);

					} else {
						mvFlag1 = true;
						animMV2.start();
						moreVideos1.setColour(Colours.selectedZone);

					}

					e.setHandled(tappableHandled);

				}
			}
		};

		moreVideos1.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
		moreVideos1.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
		moreVideos1.setGestureEnabled("TAP", true, true);
		moreVideos1.setDrawBorder(false);
		sketch.client.addZone(moreVideos1);
		
		animMV1 = PropertySetter.createAnimator(sketch.layout.animationTime, moreVideos1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animMV1.setRepeatBehavior(RepeatBehavior.REVERSE);
		animMV1.setRepeatCount(Animator.INFINITE);

		/*if(lang2.equalsIgnoreCase("English")){
			s = sketch.moreVideosE;
		} else if(lang2.equalsIgnoreCase("French")){
			s = sketch.moreVideosF;
		}*/
		
		moreVideos2 = new TextZone(sketch.layout.buttonX, sketch.layout.buttonYt2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.moreVideos, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable() && !errorFlag && vGetter.imgsLoaded2){
					
					if(mvFlag1){
						mvFlag1 = false;
						animMV2.stop();
						moreVideos2.setColour(Colours.unselectedZone);

						vGetter.removeZones();
						moreVideos1.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
						moreVideos1.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
						moreVideos2.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
						moreVideos2.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
						
						vGetter = new VideoGetter(sketch, vAct, sketch.lineX, videoWidth);
						vGetter.start();
					} else if(mvFlag2){
						mvFlag2 = false;
						animMV1.stop();
						moreVideos1.setColour(Colours.unselectedZone);
					} else {
						mvFlag2 = true;
						animMV1.start();
						moreVideos2.setColour(Colours.selectedZone);

					}

					e.setHandled(tappableHandled);

				}
			}
		};

		moreVideos2.setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
		moreVideos2.setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
		moreVideos2.setGestureEnabled("TAP", true, true);
		moreVideos2.setDrawBorder(false);
		moreVideos2.rotate((float) Colours.PI);
		sketch.client.addZone(moreVideos2);
		
		animMV2 = PropertySetter.createAnimator(sketch.layout.animationTime, moreVideos2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animMV2.setRepeatBehavior(RepeatBehavior.REVERSE);
		animMV2.setRepeatCount(Animator.INFINITE);
	}
	


	public void createCurrent1(){
		float polyH2 = (float) (sketch.getHeight()-polyW*1.1);
		float polyW2 = sketch.getWidth()-polyW;
		float polyW3 = (float) (sketch.getWidth()-(polyW*1.5));


		float controlP1y = sketch.getHeight()-polyW/2;
		float controlP1x = sketch.getWidth()-polyW/2;

		final float halfHeight = sketch.getHeight()/2 + sketch.getHeight()/50;
		final float halfHeight2 = sketch.getHeight()/2 + sketch.getHeight()/10;
		final float halfHeight3 = sketch.getHeight()/2 + sketch.getHeight()/35;

		float[] coordinates = {sketch.lineX-polyW, halfHeight3, sketch.lineX + polyW, halfHeight3, (float) (sketch.lineX + polyW*1.5), polyH2, polyW3, polyH2, polyW2, halfHeight, sketch.getWidth() +  polyW, halfHeight2, sketch.getWidth(), sketch.getHeight(), sketch.lineX, sketch.getHeight()};

		float halfHPlus = halfHeight + polyW/2;
		float[] controlPoints = {sketch.lineX - polyW/4, halfHPlus, sketch.lineX + polyW/2, halfHPlus, sketch.lineX + polyW/2, controlP1y, controlP1x, controlP1y, controlP1x, halfHPlus, sketch.getWidth() + polyW, halfHeight2};

		current1 = new CurrentZone(coordinates, true, controlPoints, polyW/2, polyW/2, TIME_CURRENT){
			public void endOfFlow(Zone zone){
				zone.resetMatrix();
				zone.setX(sketch.lineX - polyW/4);
				zone.setY(halfHeight +zone.getHeight()/2);

			}
		};
		current1.setColour(Colours.currentColour);
		sketch.client.addZone(current1);





	}

	public void createCurrent2(){
		float polyH2 = (float) (polyW*1.1);
		float polyW2 = sketch.getWidth()-polyW;
		float polyW3 = (float) (sketch.getWidth()-(polyW*1.5));


		float controlP1y = polyW/2;
		float controlP1x = sketch.getWidth()-polyW/2;

		final float halfHeight = sketch.getHeight()/2 - sketch.getHeight()/50;
		final float halfHeight2 = sketch.getHeight()/2 - sketch.getHeight()/10;
		final float halfHeight3 = sketch.getHeight()/2 - sketch.getHeight()/35;

		float[] coordinates = {sketch.lineX-polyW, halfHeight3, sketch.lineX + polyW, halfHeight3, (float) (sketch.lineX + polyW*1.5), polyH2, polyW3, polyH2, polyW2, halfHeight, sketch.getWidth() +  polyW, halfHeight2, sketch.getWidth(), 0, sketch.lineX, 0};

		float halfHPlus = halfHeight - polyW/2;
		float[] controlPoints = {sketch.lineX - polyW/4, halfHPlus, sketch.lineX + polyW/2, halfHPlus, sketch.lineX + polyW/2, controlP1y, controlP1x, controlP1y, controlP1x, halfHPlus, sketch.getWidth() + polyW, halfHeight2};

		current2 = new CurrentZone(coordinates, true, controlPoints, polyW/2, polyW/2, TIME_CURRENT){
			public void endOfFlow(Zone zone){
				zone.resetMatrix();
				zone.setX(sketch.lineX - polyW/4);
				zone.setY(halfHeight - polyW/2-zone.getHeight()/2);

			}
		};
		current2.setColour(Colours.currentColour);
		sketch.client.addZone(current2);

	}


	public void createVideoControlButtons1(){

		play1 = new TextZone(videoControlsX, sketch.layout.buttonYb, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.play, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable() && !errorFlag && playerActive && videoPlayer1.mediaPlayerComponent.getMediaPlayer().isPlayable() && !videoPlayer1.mediaPlayerComponent.getMediaPlayer().isPlaying()){
					
					if(playFlag2){
						animPlay1.stop();
						playFlag2 = false;
						play1.setTextColour(Colours.fadedText);
						play1.setColour(Colours.fadedOutZone);
						play2.setTextColour(Colours.fadedText);
						play2.setColour(Colours.fadedOutZone);
						play1.setGestureEnabled("TAP", false);
						play2.setGestureEnabled("TAP", false);
						
						stop1.setTextColour(Colours.zoneText);
						stop1.setColour(Colours.unselectedZone);
						pause1.setTextColour(Colours.zoneText);
						pause1.setColour(Colours.unselectedZone);
						
						stop2.setTextColour(Colours.zoneText);
						stop2.setColour(Colours.unselectedZone);
						pause2.setTextColour(Colours.zoneText);
						pause2.setColour(Colours.unselectedZone);
						
						stop1.setGestureEnabled("TAP", true);
						stop2.setGestureEnabled("TAP", true);
						pause1.setGestureEnabled("TAP", true);
						pause2.setGestureEnabled("TAP", true);
						

						videoPlayer1.mediaPlayerComponent.getMediaPlayer().play();
						videoPlayer2.mediaPlayerComponent.getMediaPlayer().play();
					} else if (playFlag1){
						animPlay2.stop();
						playFlag1 = false;
						play1.setColour(Colours.unselectedZone);
						play2.setColour(Colours.unselectedZone);

					} else {
						animPlay2.start();
						this.setColour(Colours.selectedZone);
						playFlag1 = true;
					}

					e.setHandled(tappableHandled);
				}
			}
		};

		play1.setTextColour(Colours.fadedText);
		play1.setColour(Colours.fadedOutZone);
		play1.setDrawBorder(false);
		play1.setActive(false);
		sketch.client.addZone(play1);
		
		animPlay1 = PropertySetter.createAnimator(sketch.layout.animationTime, play1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animPlay1.setRepeatBehavior(RepeatBehavior.REVERSE);
		animPlay1.setRepeatCount(Animator.INFINITE);

		pause1 = new TextZone(videoControlsX, sketch.layout.buttonYb2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.pause, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable() && !errorFlag && playerActive && videoPlayer1.mediaPlayerComponent.getMediaPlayer().isPlaying()){
					

					if(pauseFlag2){
						animPause1.stop();
						pauseFlag2 = false;
						pause1.setTextColour(Colours.fadedText);
						pause1.setColour(Colours.fadedOutZone);
						pause2.setTextColour(Colours.fadedText);
						pause2.setColour(Colours.fadedOutZone);
						
						pause1.setGestureEnabled("TAP", false);
						pause2.setGestureEnabled("TAP", false);
						
						play1.setTextColour(Colours.zoneText);
						play1.setColour(Colours.unselectedZone);
						play2.setTextColour(Colours.zoneText);
						play2.setColour(Colours.unselectedZone);
						
						play1.setGestureEnabled("TAP", true);
						play2.setGestureEnabled("TAP", true);

						videoPlayer1.mediaPlayerComponent.getMediaPlayer().pause();
						videoPlayer2.mediaPlayerComponent.getMediaPlayer().pause();
					} else if (pauseFlag1){
						animPause2.stop();
						pauseFlag1 = false;
						pause1.setColour(Colours.unselectedZone);
						pause2.setColour(Colours.unselectedZone);

					} else {
						animPause2.start();
						this.setColour(Colours.selectedZone);
						pauseFlag1 = true;
					}
					e.setHandled(tappableHandled);
				}
			}
		};

		pause1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		pause1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		pause1.setGestureEnabled("TAP", true, true);
		pause1.setDrawBorder(false);
		pause1.setActive(false);
		sketch.client.addZone(pause1);
		
		animPause1 = PropertySetter.createAnimator(sketch.layout.animationTime, pause1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animPause1.setRepeatBehavior(RepeatBehavior.REVERSE);
		animPause1.setRepeatCount(Animator.INFINITE);

		stop1 = new TextZone(videoControlsX, sketch.layout.buttonYb3, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner1.stop, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable() && !errorFlag && playerActive){
					
					if(stopFlag2){
						animStop1.stop();
						stopFlag2 = false;
						

						stopVideo();

					} else if (stopFlag1){
						animStop2.stop();
						stopFlag1 = false;
						stop1.setColour(Colours.unselectedZone);
						stop2.setColour(Colours.unselectedZone);

					} else {
						animStop2.start();
						this.setColour(Colours.selectedZone);
						stopFlag1 = true;
					}

					e.setHandled(tappableHandled);
				}
			}
		};

		stop1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		stop1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		stop1.setGestureEnabled("TAP", true, true);
		stop1.setDrawBorder(false);
		stop1.setActive(false);
		sketch.client.addZone(stop1);
		
		animStop1 = PropertySetter.createAnimator(sketch.layout.animationTime, stop1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animStop1.setRepeatBehavior(RepeatBehavior.REVERSE);
		animStop1.setRepeatCount(Animator.INFINITE);


	}

	public void createVideoControlButtons2(){

		play2 = new TextZone(videoControlsX, sketch.layout.buttonYt, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.play, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable() && !errorFlag && playerActive && videoPlayer2.mediaPlayerComponent.getMediaPlayer().isPlayable() && !videoPlayer2.mediaPlayerComponent.getMediaPlayer().isPlaying()){
					if(playFlag1){
						animPlay2.stop();
						playFlag1 = false;
						play1.setTextColour(Colours.fadedText);
						play1.setColour(Colours.fadedOutZone);
						play2.setTextColour(Colours.fadedText);
						play2.setColour(Colours.fadedOutZone);
						play1.setGestureEnabled("TAP", false);
						play2.setGestureEnabled("TAP", false);
						
						stop1.setTextColour(Colours.zoneText);
						stop1.setColour(Colours.unselectedZone);
						pause1.setTextColour(Colours.zoneText);
						pause1.setColour(Colours.unselectedZone);
						
						stop2.setTextColour(Colours.zoneText);
						stop2.setColour(Colours.unselectedZone);
						pause2.setTextColour(Colours.zoneText);
						pause2.setColour(Colours.unselectedZone);
						
						stop1.setGestureEnabled("TAP", true);
						stop2.setGestureEnabled("TAP", true);
						pause1.setGestureEnabled("TAP", true);
						pause2.setGestureEnabled("TAP", true);
						

						videoPlayer1.mediaPlayerComponent.getMediaPlayer().play();
						videoPlayer2.mediaPlayerComponent.getMediaPlayer().play();
					} else if (playFlag2){
						animPlay1.stop();
						playFlag2 = false;
						play1.setColour(Colours.unselectedZone);
						play2.setColour(Colours.unselectedZone);

					} else {
						animPlay1.start();
						this.setColour(Colours.selectedZone);
						playFlag2 = true;
					}

					e.setHandled(tappableHandled);
				}
			}
		};

		play2.setTextColour(Colours.fadedText);
		play2.setColour(Colours.fadedOutZone);
		play2.setDrawBorder(false);
		play2.setActive(false);
		play2.rotate((float) Colours.PI);
		sketch.client.addZone(play2);
		
		animPlay2 = PropertySetter.createAnimator(sketch.layout.animationTime, play2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animPlay2.setRepeatBehavior(RepeatBehavior.REVERSE);
		animPlay2.setRepeatCount(Animator.INFINITE);

		pause2 = new TextZone(videoControlsX, sketch.layout.buttonYt2, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.pause, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable() && !errorFlag && playerActive && videoPlayer2.mediaPlayerComponent.getMediaPlayer().isPlaying()){
					if(pauseFlag1){
						animPause2.stop();
						pauseFlag1 = false;
						pause1.setTextColour(Colours.fadedText);
						pause1.setColour(Colours.fadedOutZone);
						pause2.setTextColour(Colours.fadedText);
						pause2.setColour(Colours.fadedOutZone);
						
						pause1.setGestureEnabled("TAP", false);
						pause2.setGestureEnabled("TAP", false);
						
						play1.setTextColour(Colours.zoneText);
						play1.setColour(Colours.unselectedZone);
						play2.setTextColour(Colours.zoneText);
						play2.setColour(Colours.unselectedZone);
						
						play1.setGestureEnabled("TAP", true);
						play2.setGestureEnabled("TAP", true);

						videoPlayer1.mediaPlayerComponent.getMediaPlayer().pause();
						videoPlayer2.mediaPlayerComponent.getMediaPlayer().pause();
					} else if (pauseFlag2){
						animPause1.stop();
						pauseFlag2 = false;
						pause1.setColour(Colours.unselectedZone);
						pause2.setColour(Colours.unselectedZone);


					} else {
						animPause1.start();
						this.setColour(Colours.selectedZone);
						pauseFlag2 = true;
					}

					e.setHandled(tappableHandled);
				}
			}
		};

		pause2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		pause2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		pause2.setGestureEnabled("TAP", true, true);
		pause2.setDrawBorder(false);
		pause2.setActive(false);
		pause2.rotate((float) Colours.PI);
		sketch.client.addZone(pause2);
		
		animPause2 = PropertySetter.createAnimator(sketch.layout.animationTime, pause2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animPause2.setRepeatBehavior(RepeatBehavior.REVERSE);
		animPause2.setRepeatCount(Animator.INFINITE);

		stop2 = new TextZone(videoControlsX, sketch.layout.buttonYt3, sketch.buttonWidth, sketch.buttonHeight, sketch.radius, Colours.pFont, sketch.learner2.stop, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable() && !errorFlag && playerActive){
					if(stopFlag1){
						animStop2.stop();
						stopFlag1 = false;
						
						stopVideo();

					} else if (stopFlag2){
						animStop1.stop();
						stopFlag2 = false;
						stop1.setColour(Colours.unselectedZone);
						stop2.setColour(Colours.unselectedZone);
					} else {
						animStop1.start();
						this.setColour(Colours.selectedZone);
						stopFlag2 = true;
					}

					e.setHandled(tappableHandled);
				}
			}
		};

		stop2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		stop2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		stop2.setGestureEnabled("TAP", true, true);
		stop2.setDrawBorder(false);
		stop2.setActive(false);
		stop2.rotate((float) Colours.PI);
		sketch.client.addZone(stop2);
		
		animStop2 = PropertySetter.createAnimator(sketch.layout.animationTime, stop2, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		animStop2.setRepeatBehavior(RepeatBehavior.REVERSE);
		animStop2.setRepeatCount(Animator.INFINITE);



	}

	public void setVideoControlsVisible(){
		sketch.client.pullToTop(cBackground);
		cBackground.setActive(true);

		sketch.client.pullToTop(play1);
		sketch.client.pullToTop(pause1);
		sketch.client.pullToTop(stop1);


		sketch.client.pullToTop(play2);
		sketch.client.pullToTop(pause2);
		sketch.client.pullToTop(stop2);

		play1.setActive(true);
		pause1.setActive(true);
		stop1.setActive(true);

		play2.setActive(true);
		pause2.setActive(true);
		stop2.setActive(true);

	}

	/*
	public void loadVideo(int index){
		final String href = videoFeed1.getEntries().get(index).getHtmlLink().getHref();
		final VideoActivity videoActivity = this;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				videoPlayer = new VideoPlayer(sketch, videoActivity, sketch.lineX, sketch.getHeight()/2, 1);
				String link = videoPlayer.getVideoStream(href);
				videoPlayer.init(link);
			}
		});
	}*/

	public void stopVideo(){
		playerActive = false;

		videoPlayer1.remove();
		videoPlayer2.remove();
		resumeCurrents();

		play1.setActive(false);
		pause1.setActive(false);
		stop1.setActive(false);
		play2.setActive(false);
		pause2.setActive(false);
		stop2.setActive(false);
		cBackground.setActive(false);
		moreVideos1.setActive(true);
		moreVideos2.setActive(true);
		if(bothL){
			changeL1.setActive(true);
			changeL2.setActive(true);
		}
		
		pause1.setTextColour(Colours.zoneText);
		pause1.setColour(Colours.unselectedZone);
		pause2.setTextColour(Colours.zoneText);
		pause2.setColour(Colours.unselectedZone);
		
		pause1.setGestureEnabled("TAP", true);
		pause2.setGestureEnabled("TAP", true);
		
		stop1.setTextColour(Colours.zoneText);
		stop1.setColour(Colours.unselectedZone);
		stop2.setTextColour(Colours.zoneText);
		stop2.setColour(Colours.unselectedZone);
		
		stop1.setGestureEnabled("TAP", true);
		stop2.setGestureEnabled("TAP", true);
		
		play1.setTextColour(Colours.fadedText);
		play1.setColour(Colours.fadedOutZone);
		play2.setTextColour(Colours.fadedText);
		play2.setColour(Colours.fadedOutZone);
		
		play1.setGestureEnabled("TAP", false);
		play2.setGestureEnabled("TAP", false);
		
	}
	public void resumeCurrents(){
		ArrayList<Zone> zList = vAct.current1.getZones();

		for(Zone z: zList){
			z.zoneAnimator.resume();
		}
		zList = vAct.current2.getZones();

		for(Zone z: zList){
			z.zoneAnimator.resume();
		}
	}

	public void removeZones(){
		vGetter.stopped = true;
		sketch.client.removeZone(coverRect);
		sketch.client.removeZone(cBackground);

		vGetter.removeZones();
		
		if(bothL){
			sketch.client.removeZone(changeL1);
			sketch.client.removeZone(changeL2);
		}

		sketch.client.removeZone(moreVideos1);
		sketch.client.removeZone(play1);
		sketch.client.removeZone(pause1);
		sketch.client.removeZone(stop1);
		sketch.client.removeZone(current1);

		sketch.client.removeZone(moreVideos2);
		sketch.client.removeZone(play2);
		sketch.client.removeZone(pause2);
		sketch.client.removeZone(stop2);
		sketch.client.removeZone(current2);

		if(playerActive){
			playerActive = false;

			videoPlayer1.remove();
			videoPlayer2.remove();
		}
		
		if(errorFlag){
			sketch.client.removeZone(errorZone);
		}

	}
}
