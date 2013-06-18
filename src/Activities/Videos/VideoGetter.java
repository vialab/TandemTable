package Activities.Videos;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

import processing.core.PConstants;
import processing.core.PImage;
import vialab.simpleMultiTouch.ImageZone;
import vialab.simpleMultiTouch.RectZone;
import vialab.simpleMultiTouch.TextZone;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.Zone;
import vialab.simpleMultiTouch.events.DragEvent;
import vialab.simpleMultiTouch.events.TapEvent;
import Main.Colours;
import Main.MainSketch;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.util.ServiceException;

public class VideoGetter extends Thread {
	VideoActivity vAct;
	MainSketch sketch;

	boolean imgsLoaded1 = false;
	boolean imgsLoaded2 = false;
	boolean stopped = false;
	boolean imgTapped1 = false;
	boolean imgTapped2 = false;

	boolean elementTapped1 = false, elementTapped2 = false;



	ImageZone[] centerImg1, centerImg2, currentImgs1, currentImgs2, playButton1, playButton2;
	TextZone[] centerText1, centerText2;
	RectZone[] background1, background2;

	int sleepTime = 2500, tapped1 = -1, tapped2 = -1;
	int lastImgTap1 = -1, lastImgTap2 = -1, lastElement1 = -1, lastElement2 = -1;
	int videoWidth;
	PImage play, playSelected;

	public VideoGetter(MainSketch sketch, VideoActivity vAct, int startX, int videoWidth){
		this.vAct = vAct;
		this.sketch = sketch;
		this.videoWidth = videoWidth;
		if(play == null){
			play = sketch.loadImage(Colours.PLAY_BUTTON);
			playSelected = sketch.loadImage(Colours.PLAY_BUTTON_SELECTED);
		}
		searchFeedWithKeywords(vAct.topicsScrambled, false, false);

	}

	public void run(){
		if(!vAct.errorFlag){
			createThumbnailsBoth();
		}
	}

	public void createThumbnailsBoth(){
		int numVids = vAct.videoFeed1.getEntries().size();

		/*************
		anim1 = new Animator[numVids];
		anim2 = new Animator[numVids];
		 ***********/

		centerImg1 = new ImageZone[numVids];
		centerImg2 = new ImageZone[numVids];
		centerText1 = new TextZone[numVids];
		centerText2 = new TextZone[numVids];
		currentImgs1 = new ImageZone[numVids];
		currentImgs2 = new ImageZone[numVids];
		playButton1 = new ImageZone[numVids];
		playButton2 = new ImageZone[numVids];
		background1 = new RectZone[numVids];
		background2 = new RectZone[numVids];

		final int space = (sketch.getWidth()-sketch.lineX)/12;
		final int yPos = sketch.getHeight()/2;
		final int imageX = (sketch.getWidth()-sketch.lineX)/5;
		final float tSize = (float) (space/4.8);

		for(int i = 0; i < numVids; i++){

			if(!stopped){

				VideoEntry video = vAct.videoFeed1.getEntries().get(i);
				YouTubeMediaGroup mediaGroup = video.getMediaGroup();
				final PImage img = sketch.loadImage(mediaGroup.getThumbnails().get(0).getUrl());
				final String text =  mediaGroup.getTitle().getPlainTextContent();


				if(!vAct.playerActive){
					createImgZone1(img, space, yPos, imageX, text, i, tSize);
				}
				
				
				if(!vAct.playerActive){
					createImgZone2(img, space, yPos, imageX, text, i, tSize);
				}
				
				if(!vAct.playerActive && sleepTime > 0){
					pullToTop();

					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}

			}
		}

		if(!stopped){
			imgsLoaded1 = true;
			imgsLoaded2 = true;

			vAct.moreVideos1.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			vAct.moreVideos1.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());

			vAct.moreVideos2.setTextColour(Colours.zoneText.getRed(), Colours.zoneText.getGreen(), Colours.zoneText.getBlue());
			vAct.moreVideos2.setColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
		}

	}

	public void removeCenterElements(int i){
		sketch.client.removeZone(centerText1[i]);
		sketch.client.removeZone(centerImg1[i]);
		sketch.client.removeZone(centerText2[i]);
		sketch.client.removeZone(centerImg2[i]);
		sketch.client.removeZone(playButton1[i]);
		sketch.client.removeZone(playButton2[i]);
		sketch.client.removeZone(background1[i]);
		sketch.client.removeZone(background2[i]);


		lastElement1 = -1;
		lastElement2 = -1;
		lastImgTap1 = -1;
		lastImgTap2 = -1;
		imgTapped1 = false;
		imgTapped2 = false;
		elementTapped1 = false;
		elementTapped2 = false;
		tapped1 = -1;
		tapped2 = -1;
	}

	public void createImgZone2(PImage img, final int space, final int yPos, final int imageX, final String text, final int i, final float tSize){
		currentImgs2[i] = new ImageZone(img, sketch.lineX-(sketch.getWidth()-sketch.lineX)/32, yPos-space/2, space/4, space/4){
			public void tapEvent(TapEvent e){

				if(elementTapped2){
					currentImgs2[lastElement2].setShadow(false);
					if(lastElement1 == lastElement2){
						currentImgs1[lastElement1].setShadowColour(Colours.elementShadow);
					} else {
						currentImgs1[lastElement2].setShadow(false);
					}
				}
				this.setShadowColour(Colours.elementShadow);
				this.setShadow(true);



				if(elementTapped2){
					sketch.client.removeZone(centerImg2[tapped2]);
					sketch.client.removeZone(playButton2[tapped2]);
					sketch.client.removeZone(centerText2[tapped2]);
					sketch.client.removeZone(background2[tapped2]);
				}
				tapped2 = i;
				lastElement2 = i;
				elementTapped2 = true;

				background2[i] = new RectZone((float) (sketch.lineX + vAct.polyW), (float) (vAct.polyW - 0.11*vAct.polyW),(float) (sketch.getWidth()-sketch.lineX - 2*(vAct.polyW)), (float) ((sketch.getHeight()/2)-vAct.polyW)){
					public void tapEvent(TapEvent e){
						imgTapped2 = false;
						currentImgs2[i].setShadow(false);
						elementTapped2 = false;
						sketch.client.removeZone(this);
						sketch.client.removeZone(playButton2[i]);
						sketch.client.removeZone(centerText2[i]);
						sketch.client.removeZone(centerImg2[i]);
						tapped2 = -1;

						if(elementTapped1 && lastImgTap2 >= 0){ 
							if(lastImgTap2 == lastElement1){
								currentImgs1[lastImgTap2].setShadowColour(Colours.elementShadow);
							} else {
								currentImgs1[lastImgTap2].setShadow(false);
							}
						}

					}

				};
				background2[i].setDrawBorder(false);
				background2[i].setGestureEnabled("Tap", true);
				sketch.client.addZone(background2[i]);

				centerImg2[i] = new ImageZone(img, 2*imageX, yPos - space/7 - space*2, space*2, space*2);
				centerImg2[i].setDrawBorder(false);
				centerImg2[i].rotate((float) Colours.PI);
				sketch.client.addZone(centerImg2[i]);	

				sketch.textFont(Colours.pFont, tSize);
				sketch.textAlign(PConstants.CENTER, PConstants.CENTER);
				float myHeight = space;
				float textW = sketch.textWidth(text);
				if(space*8 <= textW){
					myHeight = (float) (space*1.7);
				} else if(space*4 <= textW){
					myHeight = (float) (space*1.5);
				}
				centerText2[i] = new TextZone((float) (2*imageX + space*2.5), yPos - space/7 - myHeight, space*5, myHeight, vAct.sketch.radius, Colours.pFont, text, tSize, "CENTER", "CENTER");				
				centerText2[i].setDrawBorder(false);

				centerText2[i].setColour(sketch.getRandomColour());
				centerText2[i].setTextColour(Color.BLACK);
				centerText2[i].rotate((float) Colours.PI);
				centerText2[i].setTextOffsets(space, space/5);
				sketch.client.addZone(centerText2[i]);

				playButton2[i] = new ImageZone(play, (float) (2*imageX + space*5-space/4), yPos - space/7 - myHeight - space/2 - space/4, space/2, space/2){
					public void tapEvent(TapEvent e){

						if(lastImgTap2 != i && imgTapped2){
							imgTapped2 = false;
							currentImgs1[lastImgTap2].setShadow(false);
							sketch.client.removeZone(centerImg2[lastImgTap2]);
							sketch.client.removeZone(centerText2[lastImgTap2]);
							sketch.client.removeZone(playButton2[lastImgTap2]);
						}
						lastImgTap2 = i;

						if(!imgTapped1 && !imgTapped2){
							imgTapped2 = true;

							currentImgs1[i].setShadowColour(Colours.selectedZone);
							currentImgs1[i].setShadow(true);
							playButton2[i].setImage(playSelected);


						} else if (imgTapped1 && !imgTapped2 && tapped1 == i){
							currentImgs2[i].setShadow(false);
							currentImgs1[i].setShadow(false);
							elementTapped1 = false;
							elementTapped2 = false;
							vAct.moreVideos1.setActive(false);
							vAct.moreVideos2.setActive(false);
							if(vAct.bothL){
								vAct.changeL1.setActive(false);
								vAct.changeL2.setActive(false);
							}
							vAct.setVideoControlsVisible();
							vAct.playerActive = true;
							removeCenterElements(i);
							loadVideo(i);

						} else if (imgTapped1 && !imgTapped2 && tapped1 != i){
							imgTapped2 = true;
							currentImgs1[i].setShadowColour(Colours.selectedZone);
							currentImgs1[i].setShadow(true);
							playButton2[i].setImage(playSelected);



						}

					}
				};
				playButton2[i].setGestureEnabled("Tap", true);
				playButton2[i].setDrawBorder(false);
				playButton2[i].rotate((float) Colours.PI);
				sketch.client.addZone(playButton2[i]);


			}

			public void timingEvent(float fraction) {
				// Simple linear interpolation to find current position
				float currentX = 0; 
				float currentY = 0;

				currentX = (getStartx() + (getEndx() - getStartx()) * fraction);
				currentY = (getStarty() + (getEndy() - getStarty()) * fraction);

				this.setXY(currentX, currentY);

			}

			public void dragEvent(DragEvent e){
				float newPosX = getXTimesMatrix() + e.getXDistance();
				float newPosY = getYTimesMatrix() + e.getYDistance();
				if(newPosX > sketch.lineX && newPosX < (sketch.getWidth()-getWidth()) &&
						newPosY < (sketch.getHeight()/2 - getHeight()) && newPosY > 0){
					super.dragEvent(e);
				}
			}

		};
		currentImgs2[i].setGestureEnabled("Tap", true);
		currentImgs2[i].setGestureEnabled("Drag", true);
		currentImgs2[i].setControllable(true);

		int size = sketch.getWidth()/120;
		currentImgs2[i].setShadowX(-size);
		currentImgs2[i].setShadowY(-size);
		currentImgs2[i].setShadowW(size*2);
		currentImgs2[i].setShadowH(size*2);
		currentImgs2[i].setShadowColour(Colours.selectedZone);

		currentImgs2[i].setDrawOnlyImage(false);
		currentImgs2[i].setShadow(false);


		currentImgs2[i].setImgRotateAmount((float) Colours.PI);
		currentImgs2[i].setImgRotated(true);

		sketch.client.addZone(currentImgs2[i]);
	}

	public void createImgZone1(PImage img, final int space, final int yPos, final int imageX, final String text, final int i, final float tSize){
		float y = yPos+space/8;

		currentImgs1[i] = new ImageZone(img, sketch.lineX-(sketch.getWidth()-sketch.lineX)/32, y, space/4, space/4){
			public void tapEvent(TapEvent e){

				if(elementTapped1){
					currentImgs1[lastElement1].setShadow(false);
					if(lastElement1 == lastElement2){
						currentImgs2[lastElement2].setShadowColour(Colours.elementShadow);
					} else {
						currentImgs2[lastElement1].setShadow(false);
					}	
				}

				this.setShadowColour(Colours.elementShadow);
				this.setShadow(true);


				if(elementTapped1){
					sketch.client.removeZone(centerImg1[tapped1]);
					sketch.client.removeZone(centerText1[tapped1]);
					sketch.client.removeZone(playButton1[tapped1]);
					sketch.client.removeZone(background1[tapped1]);

				}
				tapped1 = i;
				lastElement1 = i;
				elementTapped1 = true;

				background1[i] = new RectZone((float) (sketch.lineX + vAct.polyW), (float) (sketch.getHeight()/2+(0.11*vAct.polyW)),(float) (sketch.getWidth()-sketch.lineX - 2*(vAct.polyW)), (float) ((sketch.getHeight()/2)-vAct.polyW)){
					public void tapEvent(TapEvent e){
						imgTapped1 = false;
						currentImgs1[i].setShadow(false);
						elementTapped1 = false;
						sketch.client.removeZone(this);
						sketch.client.removeZone(playButton1[i]);
						sketch.client.removeZone(centerText1[i]);
						sketch.client.removeZone(centerImg1[i]);
						tapped1 = -1;

						if(elementTapped2 && lastImgTap1 >= 0){
							if(lastImgTap1 == lastElement2){
								currentImgs2[lastImgTap1].setShadowColour(Colours.elementShadow);
							} else {
								currentImgs2[lastImgTap1].setShadow(false);
							}
						}

					}

				};
				background1[i].setDrawBorder(false);
				background1[i].setGestureEnabled("Tap", true);
				sketch.client.addZone(background1[i]);

				centerImg1[i] = new ImageZone(img, 2*imageX, yPos+ space/7, space*2, space*2);
				centerImg1[i].setDrawBorder(false);
				sketch.client.addZone(centerImg1[i]);	


				sketch.textFont(Colours.pFont, tSize);
				sketch.textAlign(PConstants.CENTER, PConstants.CENTER);
				float myHeight = space;
				float textW = sketch.textWidth(text);
				if(space*8 <= textW){
					myHeight = (float) (space*1.7);
				} else if(space*4 <= textW){
					myHeight = (float) (space*1.5);
				}

				centerText1[i] = new TextZone((float) (2*imageX + space*2.5), yPos+ space/7, space*5, myHeight, vAct.sketch.radius, Colours.pFont, text, tSize, "CENTER", "CENTER");
				centerText1[i].setDrawBorder(false);
				centerText1[i].setColour(sketch.getRandomColour());
				centerText1[i].setTextColour(Color.BLACK);
				centerText1[i].setTextOffsets(space, space/5);
				sketch.client.addZone(centerText1[i]);

				playButton1[i] = new ImageZone(play, (float) (2*imageX + space*5-space/4), yPos + space/7 + myHeight + space/4, space/2, space/2){

					public void tapEvent(TapEvent e){
						if(lastImgTap1 != i && imgTapped1){
							imgTapped1 = false;
							currentImgs2[lastImgTap1].setShadow(false);
							sketch.client.removeZone(centerImg1[lastImgTap1]);
							sketch.client.removeZone(centerText1[lastImgTap1]);
							sketch.client.removeZone(playButton1[lastImgTap1]);


						}
						lastImgTap1 = i;
						if(!imgTapped1 && !imgTapped2){
							imgTapped1 = true;
							currentImgs2[i].setShadowColour(Colours.selectedZone);
							currentImgs2[i].setShadow(true);
							playButton1[i].setImage(playSelected);


						} else if (imgTapped2 && !imgTapped1 && tapped2 == i){
							currentImgs2[i].setShadow(false);
							currentImgs1[i].setShadow(false);
							elementTapped1 = false;
							elementTapped2 = false;

							vAct.moreVideos1.setActive(false);
							vAct.moreVideos2.setActive(false);
							if(vAct.bothL){

								vAct.changeL1.setActive(false);
								vAct.changeL2.setActive(false);
							}
							vAct.setVideoControlsVisible();
							vAct.playerActive = true;
							removeCenterElements(i);
							loadVideo(i);

						} else if (imgTapped2 && !imgTapped1 && tapped2 != i){
							imgTapped1 = true;
							currentImgs2[i].setShadowColour(Colours.selectedZone);
							currentImgs2[i].setShadow(true);
							playButton1[i].setImage(playSelected);

						}
					}
				};

				playButton1[i].setGestureEnabled("Tap", true);
				playButton1[i].setDrawBorder(false);
				sketch.client.addZone(playButton1[i]);
			}

			public void timingEvent(float fraction) {
				// Simple linear interpolation to find current position
				float currentX = 0; 
				float currentY = 0;

				currentX = (getStartx() + (getEndx() - getStartx()) * fraction);
				currentY = (getStarty() + (getEndy() - getStarty()) * fraction);

				this.setXY(currentX, currentY);
			}

			public void dragEvent(DragEvent e){

				float newPosX = getXTimesMatrix() + e.getXDistance();
				float newPosY = getYTimesMatrix() + e.getYDistance();

				if(newPosX > sketch.lineX && newPosX < (sketch.getWidth()-getWidth()) &&
						newPosY > (sketch.getHeight()/2) && newPosY < (sketch.getHeight()-getHeight())){
					super.dragEvent(e);

				}
			}

		};
		currentImgs1[i].setGestureEnabled("Tap", true);
		currentImgs1[i].setGestureEnabled("Drag", true);
		currentImgs1[i].setControllable(true);

		int size = sketch.getWidth()/120;
		currentImgs1[i].setShadowX(-size);
		currentImgs1[i].setShadowY(-size);
		currentImgs1[i].setShadowW(size*2);
		currentImgs1[i].setShadowH(size*2);
		currentImgs1[i].setShadowColour(Colours.selectedZone);
		currentImgs1[i].setDrawOnlyImage(false);
		currentImgs1[i].setShadow(false);
		sketch.client.addZone(currentImgs1[i]);
	}

	public void searchFeedWithKeywords(String keywords, boolean failedOnce, boolean failedTwice) {
		YouTubeQuery query = null;

		try {
			query = new YouTubeQuery(new URL(VideoActivity.VIDEOS_FEED));

			// order the results by the number of views
			query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE);
			// include restricted content in the search results
			query.setSafeSearch(YouTubeQuery.SafeSearch.MODERATE);

			query.setMaxResults(vAct.MAX_RESULTS);
			query.setFullTextQuery(keywords);

			query.setStartIndex(vAct.startIndex1);
			query.setLanguageRestrict(vAct.currentL);


			vAct.videoFeed1 = sketch.myService.query(query, VideoFeed.class);
			vAct.totalResults = vAct.videoFeed1.getTotalResults();
			if(vAct.totalResults <= 0 && !failedOnce && !failedTwice){
				vAct.startIndex1 = 1;
				searchFeedWithKeywords(keywords, true, false);
				return;
			} else if(vAct.totalResults <= 0 && failedOnce && !failedTwice){
				vAct.startIndex1 = 1;
				searchFeedWithKeywords(vAct.topic, false, true);
				return;
			} else if(vAct.totalResults <= 0 && failedTwice){
				vAct.errorFlag = true;
				return;
			}

			if((vAct.videoFeed1.getTotalResults() < vAct.MAX_RESULTS) && ((vAct.startIndex1 + vAct.videoFeed1.getTotalResults() +1) < 1000)){
				vAct.startIndex1 += vAct.videoFeed1.getTotalResults() + 1;
			} else if ((vAct.startIndex1 + vAct.MAX_RESULTS + 1) < 1000){
				vAct.startIndex1 += vAct.MAX_RESULTS + 1;
			} else {
				vAct.startIndex1 = 1;
			}



		} catch (MalformedURLException e) {
			System.out.println("Search Feed Error 1: MalformedURLException");
			e.printStackTrace();
			vAct.errorFlag = true;
		} catch (IOException e) {
			System.out.println("Search Feed Error 2: IOException");
			e.printStackTrace();
			vAct.errorFlag = true;
		} catch (ServiceException e) {
			System.out.println("Search Feed Error 3: ServiceException");
			e.printStackTrace();
			vAct.errorFlag = true;
		}
		if(vAct.errorFlag){
			String s = "Server Error. Try Again Later";
			sketch.textFont(Colours.pFont, sketch.textSize);
			sketch.textAlign(PConstants.CENTER, PConstants.CENTER);
			float tw = sketch.textWidth(s);
			vAct.errorZone = new TextZone(sketch.lineX + (sketch.getWidth()-sketch.lineX)/2-tw/2, sketch.getHeight()/2,  (float)(tw *1.2), sketch.buttonHeight, sketch.radius, Colours.pFont, s, sketch.textSize, "CENTER", "CENTER");
			vAct.errorZone.setColour(Colours.currentColour);
			vAct.errorZone.setDrawBorder(false);
			sketch.client.addZone(vAct.errorZone);
		}

	}

	public void pullToTop(){
		sketch.client.pullToTop(vAct.coverRect);

		sketch.client.pullToTop(vAct.sketch.layout.graph.nodes[vAct.sketch.layout.graph.lastSelectedNode]);
		sketch.client.pullToTop(vAct.sketch.layout.switchAct1);
		sketch.client.pullToTop(vAct.moreVideos1);
		sketch.client.pullToTop(vAct.sketch.layout.switchAct2);
		sketch.client.pullToTop(vAct.moreVideos2);
		if(vAct.bothL){

			sketch.client.pullToTop(vAct.changeL1);
			sketch.client.pullToTop(vAct.changeL2);
		}



		if(vAct.playerActive){
			vAct.setVideoControlsVisible();
		}


	}


	public void pauseCurrents(){
		ArrayList<Zone> zList = vAct.current1.getZones();

		for(Zone z: zList){
			z.zoneAnimator.pause();
		}

		zList = vAct.current2.getZones();

		for(Zone z: zList){
			z.zoneAnimator.pause();
		}

	}
	public void loadVideo(int index){
		final int height = sketch.getHeight()/2;

		final String href = vAct.videoFeed1.getEntries().get(index).getHtmlLink().getHref();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				vAct.videoPlayer1 = new VideoPlayer(sketch, vAct, sketch.lineX + vAct.videSpaceX/4, height, videoWidth, height, 1);
				vAct.videoPlayer2 = new VideoPlayer(sketch, vAct, sketch.lineX + vAct.videSpaceX/4, 0, videoWidth, height, 2);
				String link = vAct.videoPlayer1.getVideoStream(href);
				vAct.videoPlayer1.init(link);
				vAct.videoPlayer2.init(link);
				vAct.videoPlayer1.startVideo();
				vAct.videoPlayer2.startVideo();
			}
		});

		pauseCurrents();

	}



	public void removeZones() {
		if(currentImgs1 != null){
			for(Zone z: currentImgs1){
				sketch.client.removeZone(z);
			}
		}
		if(currentImgs2 != null){
			for(Zone z: currentImgs2){
				sketch.client.removeZone(z);
			}
		}
		if(elementTapped1){
			sketch.client.removeZone(centerImg1[tapped1]);
			sketch.client.removeZone(centerText1[tapped1]);
			sketch.client.removeZone(playButton1[tapped1]);
			sketch.client.removeZone(background1[tapped1]);
		}

		if(elementTapped2){
			sketch.client.removeZone(centerImg2[tapped2]);
			sketch.client.removeZone(centerText2[tapped2]);
			sketch.client.removeZone(playButton2[tapped2]);
			sketch.client.removeZone(background2[tapped2]);
		}

		imgsLoaded1 = false;
		imgsLoaded2 = false;
		stopped = true;
		imgTapped1 = false;
		imgTapped2 = false;

		elementTapped1 = false;
		elementTapped2 = false;
		tapped1 = -1;
		tapped2 = -1;
		lastImgTap1 = -1;
		lastImgTap2 = -1;
		lastElement1 = -1;
		lastElement2 = -1;
		this.vAct.videoFlag1 = false;
		this.vAct.videoFlag2 = false;




	}


}
