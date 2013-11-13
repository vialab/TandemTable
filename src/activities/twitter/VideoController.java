package activities.twitter;

import java.awt.Color;

import javax.swing.SwingUtilities;

import main.ColourEval;
import main.Colours;
import main.MainSketch;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import com.google.gdata.client.youtube.YouTubeService;

import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.RectZone;
import vialab.simpleMultiTouch.zones.TextZone;
import activities.videos.VideoPlayer;

public class VideoController {
	MainSketch sketch;
	public RectZone cBackground;
	RectZone cancelBackground1, cancelBackground2;
	TwitterActivity tAct;
	public TextZone play1, pause1, stop1, play2, pause2, stop2, accept1, accept2; 
	boolean errorFlag = false;
	public boolean playerActive = false;
	boolean videoError = false;
	boolean youtubeInit = false;
	boolean playFlag1 = false, playFlag2 = false, stopFlag1 = false, stopFlag2 = false, pauseFlag1 = false, pauseFlag2 = false;

	
	YouTubeService myService;
	public VideoPlayer videoPlayer1, videoPlayer2;
	int videoControlsX, videSpaceX, videoWidth;
	
	
	int[] hashMap = new int[2];
	
	Animator animPlay1, animPlay2, animPause1, animPause2, animStop1, animStop2;

//	String playStr1, playStr2, pauseStr1, pauseStr2, stopStr1, stopStr2, playVideoStr1, playVideoStr2;
	
	public VideoController(MainSketch sketch, TwitterActivity tAct){
		this.tAct = tAct;
		this.sketch = sketch;
		
		videoWidth = (int) ((sketch.getWidth()-sketch.lineX)/1.5);
		videSpaceX = sketch.getWidth()-sketch.lineX - videoWidth - sketch.buttonWidth;
		videoControlsX = sketch.getWidth() - sketch.buttonWidth - videSpaceX/2;
		
		/*if(tAct.lang1.equalsIgnoreCase("English")){
			playStr1 = sketch.playE;
			pauseStr1 = sketch.pauseE;
			stopStr1 = sketch.stopE;
			playVideoStr1 = sketch.playVideoE;
		} else if(tAct.lang1.equalsIgnoreCase("French")){
			playStr1 = sketch.playF;
			pauseStr1 = sketch.pauseF;
			stopStr1 = sketch.stopF;
			playVideoStr1 = sketch.playVideoF;
		}

		if(tAct.lang2.equalsIgnoreCase("English")){
			playStr2 = sketch.playE;
			pauseStr2 = sketch.pauseE;
			stopStr2 = sketch.stopE;
			playVideoStr2 = sketch.playVideoE;
		} else if(tAct.lang2.equalsIgnoreCase("French")){
			playStr2 = sketch.playF;
			pauseStr2 = sketch.pauseF;
			stopStr2 = sketch.stopF;
			playVideoStr2 = sketch.playVideoF;
		}
		*/
		createWhiteRect();
		createVideoControlButtons1();
		createVideoControlButtons2();
		createAcceptButtons();
		createCancelBackground();
		
		
		
	}
	
	public void createCancelBackground(){
		cancelBackground1 = new RectZone(sketch.lineX, sketch.getHeight()/2, sketch.getWidth()-sketch.lineX, sketch.getHeight()/2){
			public void tapEvent(TapEvent e){
				if(getTappable()){
					accept1.setActive(false);
					this.setActive(false);
				}
			}
		};
		
		
		cancelBackground2 = new RectZone(sketch.lineX, 0, sketch.getWidth()-sketch.lineX, sketch.getHeight()/2){
			public void tapEvent(TapEvent e){
				if(getTappable()){
					accept2.setActive(false);
					this.setActive(false);
				}
			}
		};
		
		cancelBackground1.setDrawBorder(false);
		cancelBackground1.setGestureEnabled("Tap", true);
		cancelBackground1.setActive(false);
		sketch.client.addZone(cancelBackground1);
		
		cancelBackground2.setDrawBorder(false);
		cancelBackground2.setGestureEnabled("Tap", true);
		cancelBackground2.setActive(false);
		sketch.client.addZone(cancelBackground2);
	}
	
	public void createAcceptButtons(){
		float spaceW = (sketch.getWidth()-sketch.lineX);
		float width = 2*(spaceW/3);
		float x = sketch.lineX + spaceW/2 - width/2;
		
		float spaceH = sketch.getHeight()/2;
		float height = 2*(spaceH/3);
		float y = spaceH + spaceH/2 - height/2;
		accept1 = new TextZone(x, y, width, height, sketch.radius, Colours.pFont, sketch.learner1.playVideo, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable()){
					accept1.setActive(false);
					setVideoControlsVisible();
					startVideo(2);
					e.setHandled(tappableHandled);
				}
			}
		};

		accept1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		accept1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		accept1.setGestureEnabled("TAP", true, true);
		accept1.setDrawBorder(false);
		accept1.setActive(false);
		sketch.client.addZone(accept1);
		
		
		y = y - spaceH;
		
		accept2 = new TextZone(x, y, width, height, sketch.radius, Colours.pFont, sketch.learner2.playVideo, sketch.textSize, "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
				if (getTappable()){
					accept2.setActive(false);
					setVideoControlsVisible();
					startVideo(1);
					e.setHandled(tappableHandled);
				}
			}
		};

		accept2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		accept2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		accept2.setGestureEnabled("TAP", true, true);
		accept2.setDrawBorder(false);
		accept2.setActive(false);
		accept2.rotate((float) Colours.PI);
		sketch.client.addZone(accept2);
	}
	
	public void setAgreeButtonVisible(int user){
		if(user == 1){
			sketch.client.pullToTop(cancelBackground1);
			cancelBackground1.setActive(true);
			sketch.client.pullToTop(accept1);
			accept1.setActive(true);
		} else if(user == 2){
			sketch.client.pullToTop(cancelBackground2);
			cancelBackground2.setActive(true);
			sketch.client.pullToTop(accept2);
			accept2.setActive(true);
		}
		sketch.client.pullToTop(tAct.middleTweet);
	}
	
	public void startVideo(int user){
		ContentGetter tempcg = null;
		
		if(user == 1){
			tempcg = tAct.tg.hmap1[hashMap[0]].get(hashMap[1]);
		} else if(user == 2){
			tempcg = tAct.tg.hmap2[hashMap[0]].get(hashMap[1]);
		}
		
		final ContentGetter cg = tempcg;
		if(!youtubeInit){
			myService = new YouTubeService(Colours.appNameTube, Colours.apiKeyTube);

			if(myService == null){
				videoError = true;
				System.out.println("YouTubeService failed to initialize.");
			} else {
				//VideoPlayer.loadVLClibs();
				youtubeInit = true;
			}
		} 
		cg.videoFlag = true;
		
		videoPlayer1 = new VideoPlayer(sketch.lineX + videSpaceX/4, sketch.getHeight()/2, videoWidth, sketch.getHeight()/2, sketch, cg, this, 1);
		
		videoPlayer2 = new VideoPlayer(sketch.lineX + videSpaceX/4, 0, videoWidth, sketch.getHeight()/2, sketch, cg, this, 2);

		if(cg.URL.contains("/embed/")){
			String[] resp = sketch.loadStrings(cg.URL);
			for(String s: resp){
				String pattern = "<link rel=\"canonical\" href=\"";
				if(s.contains(pattern)){
					final String finalS = "http://www.youtube.com" + (s.substring(s.indexOf(pattern) + pattern.length(), s.length()-2)).trim();
					//String href= cg.player.getVideoStream(finalS);
					//cg.loadVideo4TwitterAct(finalS);
					//System.out.println(href);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							String href= videoPlayer1.getVideoStream(finalS);
							videoPlayer1.init(href);
							videoPlayer2.init(href);
							videoPlayer1.startVideocg();
							videoPlayer2.startVideocg();
							pause1.setTextColour(Colours.zoneText);
							pause1.setColour(Colours.unselectedZone); 
							stop1.setTextColour(Colours.zoneText);
							stop1.setColour(Colours.unselectedZone); 
							pause2.setTextColour(Colours.zoneText);
							pause2.setColour(Colours.unselectedZone); 
							stop2.setTextColour(Colours.zoneText);
							stop2.setColour(Colours.unselectedZone); 
						}
					});
					break;
				}
			}
		} else {
			//cg.loadVideo4TwitterAct(cg.URL);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					videoPlayer1.init(cg.URL);
					videoPlayer2.init(cg.URL);
					videoPlayer1.startVideocg();
					videoPlayer2.startVideocg();
					pause1.setTextColour(Colours.zoneText);
					pause1.setColour(Colours.unselectedZone); 
					stop1.setTextColour(Colours.zoneText);
					stop1.setColour(Colours.unselectedZone); 
					pause2.setTextColour(Colours.zoneText);
					pause2.setColour(Colours.unselectedZone); 
					stop2.setTextColour(Colours.zoneText);
					stop2.setColour(Colours.unselectedZone); 
				}
			});

		}
	}
	
	/*public void startVideo2(int ii, int jj){
		final ContentGetter cg = tAct.tg.hmap2[ii].get(jj);
		if(!cg.youtubeInit){
			cg.myService = new YouTubeService(Colours.appNameTube, Colours.apiKeyTube);

			if(cg.myService == null){
				cg.videoError = true;
				System.out.println("YouTubeService failed to initialize.");
			} else {
				VideoPlayer.loadVLClibs();
				cg.youtubeInit = true;
			}
		} 
		cg.videoFlag = true;
		cg.player = new VideoPlayer(sketch.lineX + videSpaceX/4, 0, videoWidth, sketch.getHeight()/2, sketch, cg, 2);

		if(cg.URL.contains("/embed/")){
			String[] resp = sketch.loadStrings(cg.URL);
			for(String s: resp){
				String pattern = "<link rel=\"canonical\" href=\"";
				if(s.contains(pattern)){
					String finalS = "http://www.youtube.com" + (s.substring(s.indexOf(pattern) + pattern.length(), s.length()-2)).trim();
					final String href= cg.player.getVideoStream(finalS);
					//cg.loadVideo4TwitterAct(finalS);
					System.out.println(href);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							cg.player.init(href);
						}
					});
					break;
				}
			}
		} else {
			//cg.loadVideo4TwitterAct(cg.URL);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					cg.player.init(cg.URL);
				}
			});

		}
	}
	*/
	
	public void createWhiteRect(){
		//Right side
		int whiteWidth = sketch.getWidth()-tAct.sketch.lineX;
		int whiteHeight = sketch.getHeight();
		
		cBackground = new RectZone(tAct.sketch.lineX, 0, whiteWidth, whiteHeight);
		cBackground.setColour(Color.WHITE);
		cBackground.setActive(false);
		cBackground.setDrawBorder(false);

		sketch.client.addZone(cBackground);


	}
	
	public void watchItButtons(){
		/*watch1 = new TextZone(, , , , sketch.radius, Colours.pFont, , , "CENTER", "CENTER"){

			public void tapEvent(TapEvent e){
								}
			}
		};

		watch1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
		watch1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		watch1.setGestureEnabled("TAP", true, true);
		watch1.setDrawBorder(false);
		watch1.setActive(false);
		sketch.client.addZone(watch1);
		
		 = PropertySetter.createAnimator(sketch.layout.animationTime, watch1, 
				"colour", new ColourEval(), Colours.unselectedZone, Colours.selectedZone);

		.setRepeatBehavior(RepeatBehavior.REVERSE);
		.setRepeatCount(Animator.INFINITE);*/
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

		pause1.setTextColour(Colours.fadedText);
		pause1.setColour(Colours.fadedOutZone);
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

		stop1.setTextColour(Colours.fadedText);
		stop1.setColour(Colours.fadedOutZone);
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
		play2.setGestureEnabled("TAP", true, true);
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

		pause2.setTextColour(Colours.fadedText);
		pause2.setColour(Colours.fadedOutZone);
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

		stop2.setTextColour(Colours.fadedText);
		stop2.setColour(Colours.fadedOutZone);
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
	
	public void stopVideo(){
		playerActive = false;

		videoPlayer1.remove();
		videoPlayer2.remove();
		
		play1.setActive(false);
		pause1.setActive(false);
		stop1.setActive(false);
		play2.setActive(false);
		pause2.setActive(false);
		stop2.setActive(false);
		cBackground.setActive(false);
		
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
	
	public void removeZones(){
		
		if(playerActive){
			stopVideo();
		}
		sketch.client.removeZone(cBackground);
		sketch.client.removeZone(play1);
		sketch.client.removeZone(pause1);
		sketch.client.removeZone(stop1);
		sketch.client.removeZone(play2);
		sketch.client.removeZone(pause2);
		sketch.client.removeZone(stop2);
		sketch.client.removeZone(accept1);
		sketch.client.removeZone(accept2);
		sketch.client.removeZone(cancelBackground1);
		sketch.client.removeZone(cancelBackground2);

		
	}
}
