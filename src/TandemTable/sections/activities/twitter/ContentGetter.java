package TandemTable.sections.activities.twitter;

import gifAnimation.Gif;

import java.awt.Color;
import java.util.HashMap;
import java.util.Vector;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import TandemTable.Colours;
import TandemTable.Sketch;
import TandemTable.util.ArticleContainer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.ImageZone;
import vialab.simpleMultiTouch.zones.RectZone;

public class ContentGetter extends Thread{
	
	public RectZone loading;
	RectZone background;
	
	// Image from news article
	ImageZone articleImg;
	// News article container for text
	ArticleContainer article;
	
	
	Sketch sketch;
	TwitterGetter tg;
	ContentGetter cg;
	Animator animZoneX, animZoneY, animZoneWidth, animZoneHeight,  animtZone1;
	
	boolean imgFlag = false, textFlag = false, activated = false, wordTapped = false, content = false;
	public boolean videoFlag = false;

	String regex = "[ \t\n\f\r]+";
	String URL = null;
	String url;

	public HashMap<Integer, String> wordMap1 = new HashMap<Integer, String>();
	public HashMap<Integer, Float[]> wordX1 = new HashMap<Integer, Float[]>();

	private volatile Thread threadFlag;
	
	final int CHAR_LIMIT = 1000;
	final int TEXT_COUNT_MIN = 2; 
	final int IMAGE_W_MIN = 50;
	final int IMAGE_H_MIN = 50;
	
	// Colour picking values
	public int red = 1, green = 1, blue = 1;
	// Margins for news article text
	int xMargin,  yMargin;
	
	int sizeTime = 300;
	int colourPicker1, textSize, height, textYOffset, widthOffset, widthBody, textYSpacing, user, tweet, wordInTweet;



	public ContentGetter(final Sketch sketch, int user, final TwitterGetter tg, int tweet, int wordInTweet){
		this.sketch = sketch;
		this.user = user;
		this.threadFlag = this;
		this.tg = tg;
		//IMAGE_W_MIN = 0;//(sketch.getWidth()-lineX)/20;
		//IMAGE_H_MIN = 0;//(sketch.getHeight()/2)/15;;
		cg = this;
		this.tweet = tweet;
		this.wordInTweet = wordInTweet;
		
		xMargin = sketch.getWidth()/60;
		yMargin = sketch.getWidth()/60;
		
		textYOffset = sketch.getHeight()/15;
		widthOffset = (int) (0.20*(sketch.getWidth()-sketch.lineX));
		widthBody = (int) (sketch.getWidth()- sketch.lineX - 2*widthOffset);
		textSize = 	sketch.getHeight()/50;
		textYSpacing = (int) (textSize*1.2);
		

		final Gif ajaxGIF = new Gif(sketch, sketch.loadGIF);
		final PApplet app = sketch;
		ajaxGIF.loop();

		int width = sketch.getWidth()/20;
		if(user == 1){
			loading = new RectZone(sketch.getWidth() - width, sketch.getHeight() - width, width, width){
				public void drawZone(){
					super.drawZone();
					app.image(ajaxGIF, this.getX(), this.getY(), this.getWidth(), this.getHeight());
				}
			};
			
			background = new RectZone(sketch.lineX, sketch.getHeight()/2, sketch.getWidth()-sketch.lineX, sketch.getHeight()/2){
				public void tapEvent(TapEvent e){
					if(isTappable()){
						deactivateZones();
						tg.twitterAct.fadeTweetWordButton(1);
						tg.contGetter1 = cg;
					}
				}
			};
			
		} else if (user == 2){
			loading = new RectZone(sketch.getWidth() - width, 0, width, width){
				public void drawZone(){
					super.drawZone();
					app.image(ajaxGIF, this.getX(), this.getY(), this.getWidth(), this.getHeight());
				}
			};
			
			background = new RectZone(sketch.lineX, 0, sketch.getWidth()-sketch.lineX, sketch.getHeight()/2){
				public void tapEvent(TapEvent e){
					if(isTappable()){
						deactivateZones();
						tg.twitterAct.fadeTweetWordButton(2);
						tg.contGetter2 = cg;
					}
				}
			};
		}
		loading.setDrawBorder(false);
		loading.setActive(false);
		sketch.client.addZone(loading);
		
		background.setDrawBorder(false);
		background.setGestureEnabled("Tap", true);
		background.setActive(false);
		sketch.client.addZone(background);


	}

	public void setURL(String url){
		this.url = url;
	}
	public void run(){
		loading.setActive(true);
		connect();
		loading.setActive(false);

	}
	
	public void connect(){
		String request = Colours.diffbotEndpoint + "token=" + Colours.diffbotToken;
		//if(user == 1){
		request += "&url=" + url;
		//} else if (user == 2){
		//	request += "q=" + this.headAct.topic2 + "&order=" + this.headAct.ORDER + "&culture_code=" + this.headAct.culture2 + "&count=" + this.headAct.MAX_HEADLINES;
		//}
		String[] response = sketch.loadStrings(request);
		if(threadFlag == this){
			if (response != null){

				if(user == 1){
					user1(response);
				} else if(user == 2){
					user2(response);
				}


			}

			if(activated){
				if(imgFlag){
					articleImg.setActive(true);
				}
				if(textFlag){
					article.setActive(true);
				}
			}

			if(content){
				if(user == 1){
					if(!((sketch.deactivateVideo || sketch.removeVideoAct) && videoFlag)) {
						tg.twitterAct.tweetZones1[tweet][wordInTweet].setTextColour(Color.BLUE);
					}
					
					if(videoFlag) {// && !(sketch.deactivateVideo || sketch.removeVideoAct)){
						tg.twitterAct.contentVisual1[tweet][wordInTweet] = Colours.videoIndicator; 
					} else if(imgFlag){
						tg.twitterAct.contentVisual1[tweet][wordInTweet] = Colours.imageIndicator;
					} else if (textFlag){
						tg.twitterAct.contentVisual1[tweet][wordInTweet] = Colours.textIndicator;
					}
				} else if(user == 2){
					if(!((sketch.deactivateVideo || sketch.removeVideoAct) && videoFlag)) {
						tg.twitterAct.tweetZones2[tweet][wordInTweet].setTextColour(Color.BLUE);
					}
					
					if(videoFlag) {// && !(sketch.deactivateVideo || sketch.removeVideoAct)){
						tg.twitterAct.contentVisual2[tweet][wordInTweet] = Colours.videoIndicator; 
					} else if(imgFlag){
						tg.twitterAct.contentVisual2[tweet][wordInTweet] = Colours.imageIndicator;
					} else if (textFlag){
						tg.twitterAct.contentVisual2[tweet][wordInTweet] = Colours.textIndicator;
					}
				}

			} //else {
			//tg.twitterAct.tweetZones1[x][c].setTextColour(Colours.fadedText);
			//}
		}
	}


	public void cancel(){
		threadFlag = null;
	}

	public void removeZones(){
		activated = false;
		loading.setActive(false);
		sketch.client.removeZone(loading);
		sketch.client.removeZone(background);
		if(articleImg != null){
			imgFlag = false;
			sketch.client.removeZone(articleImg);
		}
		if(article != null){
			textFlag = false;
			sketch.client.removeZone(article);
		}
	}
	
	public void deactivateZones(){
		activated = false;
		loading.setActive(false);
		background.setActive(false);
		if(articleImg != null){
			articleImg.setActive(false);
		}
		if(article != null){
			article.setActive(false);
		}
		
		wordTapped = false;

	}

	public void user1(String[] response){
		JSONObject diffbot;
		try {
			diffbot = new JSONObject(PApplet.join(response, ""));
			Vector<PImage> imgVec = null;
			final int wSpace = (int) (0.20*(sketch.getWidth()-sketch.lineX));//(sketch.getWidth()-lineX)/4;
			final int w = (sketch.getWidth()-sketch.lineX) - wSpace * 2;


			final int hSpace = (int) (5.5*sketch.getHeight()/40 + sketch.shadowOffset)/2;//sketch.getHeight()/10;
			int h = (int) (sketch.getHeight()/2 - hSpace*2);


			if(diffbot.has("media") && threadFlag == this){
				JSONArray results1 = diffbot.getJSONArray("media");
				imgVec = new Vector<PImage>();
				for(int i = 0; i < results1.length(); i++){
					JSONObject o = results1.getJSONObject(i);
					//System.out.println(o);
					if(o.get("type").equals("video")){
						URL =  (String) o.get("link");
						videoFlag = true;
						content = true;


					}
					if(o.get("type").equals("image")){ //o.has("primary") &&

						//System.out.println(o);
						String s = (String) o.get("link");
						if(s.charAt(s.length()-4) != '.' && s.charAt(s.length()-5) != '.'){
							s += ".png";
						}
						PImage pimg = sketch.loadImage(s);

						if(pimg != null && pimg.width != -1 && pimg.height != -1){
							//articleImg = new ImageZone(sketch.loadImage((String) o.get("link")), (int) (lineX + Math.random() * (sketch.getWidth()-lineX)/2), sketch.getHeight()/2, sketch.getHeight()/5, sketch.getHeight()/5);
							//client.addZone(imgZone);
							if(pimg.width > IMAGE_W_MIN && pimg.height > IMAGE_H_MIN){
								imgVec.add(pimg);
								imgFlag = true;
								content = true;
							}

						}
					}
				}
				if(imgFlag){
					PImage[] pimg = new PImage[imgVec.size()];
					imgVec.copyInto(pimg);




					articleImg = new ImageZone(pimg, sketch.lineX + wSpace, sketch.getHeight()/2 + hSpace, w, h, sketch.radius){

						public void tapEvent(TapEvent e){
							if (isTappable()){

								if(getImageIndex()+1 < getImageArray().length-1){
									int index = getImageIndex() + 1;
									setAnimators(index);
									
									if(user == 1){
										tg.contGetter1 = cg;
									} else if(user == 2){
										tg.contGetter2 = cg;
									}

								} else {
									this.setActive(false);
									imgFlag = false;
									setImageIndex(0);
									
									if(user == 1){
										tg.contGetter1 = cg;
									} else if(user == 2){
										tg.contGetter2 = cg;
									}


									if(textFlag){
										article.animPosY = 0;
										float tDiff = 0;

										tDiff = (sketch.getHeight()/2 + hSpace) -  article.getY() + 2*sketch.shadowOffset;

										if(tDiff != 0){
											animtZone1 = PropertySetter.createAnimator(sizeTime, article, "animPosY", 0f, tDiff);
											animtZone1.start();
										}
									}

								}
								e.setHandled(true);
							}
						}

						public void setAnimators(int index){
							animPosX = 0;
							animPosY = 0;
							animWidth = 0;
							animHeight = 0;
							if(textFlag){
								article.animPosY = 0;
							}

							float xDiff = (this.getX() + this.getWidth()/2 - articleImg.getImageArray()[index].width/2 - articleImg.getX());
							float yDiff = (this.getY() + this.getHeight()/2 - articleImg.getImageArray()[index].height/2 - articleImg.getY());
							float wDiff = (articleImg.getImageArray()[index].width - articleImg.getWidth());
							float hDiff = (articleImg.getImageArray()[index].height - articleImg.getHeight());

							float tDiff = 0;
							if(textFlag){
								//int tDiff = articleImg.getY() + (articleImg.getY() + articleImg.getHeight()/2 - articleImg.getImageArray()[index].height/2 - articleImg.getY()) + articleImg.getImageArray()[index].height - tZone1.getY();


								if(yDiff != 0 && yDiff + articleImg.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
									if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
										tDiff = articleImg.getY() + yDiff + articleImg.getHeight() + hDiff -  article.getY() + 2*sketch.shadowOffset;
									} else {
										tDiff = articleImg.getY() + yDiff + articleImg.getHeight() - article.getY() + 2*sketch.shadowOffset;

									}
								} else {
									if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
										tDiff = articleImg.getY() + articleImg.getHeight() + hDiff - article.getY()+ 2*sketch.shadowOffset;

									} else {
										tDiff = articleImg.getY() + articleImg.getHeight() -  article.getY() + 2*sketch.shadowOffset;

									}
								}
							}
							if((animZoneX == null || !animZoneX.isRunning()) && (animZoneY == null || !animZoneY.isRunning()) && (animZoneWidth == null || !animZoneWidth.isRunning()) && (animZoneHeight == null || !animZoneHeight.isRunning()) && (animtZone1 == null || !animtZone1.isRunning())){
								if(xDiff != 0){
									animZoneX = PropertySetter.createAnimator(sizeTime, articleImg, "animPosX", 0f, xDiff);
									animZoneX.start();
								}
								if(yDiff != 0 && yDiff + articleImg.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
									animZoneY = PropertySetter.createAnimator(sizeTime, articleImg, "animPosY", 0f, yDiff);
									animZoneY.start();
								}
								if(wDiff != 0){
									animZoneWidth = PropertySetter.createAnimator(sizeTime, articleImg, "animWidth", 0f, wDiff);
									animZoneWidth.start();
								}
								if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
									animZoneHeight = PropertySetter.createAnimator(sizeTime, articleImg, "animHeight", 0f, hDiff);
									animZoneHeight.start();
								}
								if(textFlag){
									if(tDiff != 0){
										animtZone1 = PropertySetter.createAnimator(sizeTime, article, "animPosY", 0f, tDiff);
										animtZone1.start();
									}
								}

								setImageIndex(index);
							}


						}

					};
					articleImg.setShadowColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
					articleImg.setShadowX(-sketch.shadowOffset);
					articleImg.setShadowY(-sketch.shadowOffset);
					articleImg.setShadowW(2*sketch.shadowOffset);
					articleImg.setShadowH(2*sketch.shadowOffset);
					//articleImg.setColour(Colours.boundingBox.getRed(), Colours.boundingBox.getGreen(), Colours.boundingBox.getBlue());
					articleImg.setShadow(true);
					articleImg.setStroke(false);
					articleImg.setDrawBorder(false);
					articleImg.setDrawOnlyImage(false);
					articleImg.setGestureEnabled("Tap", true);
					articleImg.setActive(false);
					sketch.client.addZone(articleImg);

					int index = 0;
					float xDiff = (articleImg.getX() + articleImg.getWidth()/2 - articleImg.getImageArray()[index].width/2 - articleImg.getX());
					float yDiff = (articleImg.getY() + articleImg.getHeight()/2 - articleImg.getImageArray()[index].height/2 - articleImg.getY());
					float wDiff = (articleImg.getImageArray()[index].width - articleImg.getWidth());
					float hDiff = (articleImg.getImageArray()[index].height - articleImg.getHeight());

					if(xDiff != 0){
						animZoneX = PropertySetter.createAnimator(sizeTime, articleImg, "animPosX", 0f, xDiff);
						animZoneX.start();
					}
					if(yDiff != 0 && yDiff + articleImg.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
						animZoneY = PropertySetter.createAnimator(sizeTime, articleImg, "animPosY", 0f, yDiff);
						animZoneY.start();
					}
					if(wDiff != 0){
						animZoneWidth = PropertySetter.createAnimator(sizeTime, articleImg, "animWidth", 0f, wDiff);
						animZoneWidth.start();
					}
					if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
						animZoneHeight = PropertySetter.createAnimator(sizeTime, articleImg, "animHeight", 0f, hDiff);
						animZoneHeight.start();
					}






				}
			}
			
			if(diffbot.has("title") && threadFlag == this){
				String title = (String) diffbot.get("title");
				String body = "";
				if(diffbot.has("text")){
					body = (String) diffbot.get("text");

				}
				title = title.trim();
				body = body.trim();
				if(body.length() > CHAR_LIMIT){
					body = body.substring(0, CHAR_LIMIT);
				}
				if(body.length() > TEXT_COUNT_MIN){
					content = true;
					textFlag = true;
					createUIElement1(title, body);

				
					if(imgVec != null && imgVec.size() > 0){
						int index = 0;
						float yDiff = (articleImg.getY() + articleImg.getHeight()/2 - articleImg.getImageArray()[index].height/2 - articleImg.getY());
						float hDiff = (articleImg.getImageArray()[index].height - articleImg.getHeight());
						float tDiff = 0;

						if(yDiff != 0 && yDiff + articleImg.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
							if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
								tDiff = articleImg.getY() + yDiff + articleImg.getHeight() + hDiff -  article.getY() + 2*sketch.shadowOffset;
							} else {
								tDiff = articleImg.getY() + yDiff + articleImg.getHeight() -  article.getY() +2*sketch.shadowOffset;

							}
						} else {
							if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 + sketch.shadowOffset)/2){
								tDiff = articleImg.getY() + articleImg.getHeight() + hDiff -  article.getY() + 2*sketch.shadowOffset;

							} else {
								tDiff = articleImg.getY() + articleImg.getHeight() -  article.getY() + 2*sketch.shadowOffset;

							}
						}
						if(tDiff != 0){
							animtZone1 = PropertySetter.createAnimator(sizeTime, article, "animPosY", 0f, tDiff);
							animtZone1.start();
						}
					}

				}
			}

		} catch (JSONException e) {
			PApplet.println ("There was an error parsing the JSONObject.");
			e.printStackTrace();
		}
	}

	public void user2(String[] response){
		JSONObject diffbot;
		try {
			diffbot = new JSONObject(PApplet.join(response, ""));
			Vector<PImage> imgVec = null;
			final int wSpace = (int) (0.20*(sketch.getWidth()-sketch.lineX));//(sketch.getWidth()-lineX)/4;
			final int w = (sketch.getWidth()-sketch.lineX) - wSpace * 2;


			final int hSpace = (int) (5.5*sketch.getHeight()/40 + sketch.shadowOffset)/2;//sketch.getHeight()/10;
			final int h = (int) (sketch.getHeight()/2 - hSpace*2);


			//System.out.println(diffbot);


			if(diffbot.has("media") && threadFlag == this){
				JSONArray results1 = diffbot.getJSONArray("media");
				imgVec = new Vector<PImage>();
				for(int i = 0; i < results1.length(); i++){
					JSONObject o = results1.getJSONObject(i);
					//System.out.println(o);
					if(o.get("type").equals("video")){
						URL =  (String) o.get("link");
						videoFlag = true;
						content = true;


					}
					if(o.get("type").equals("image")){ //o.has("primary") &&

						//System.out.println(o);
						String s = (String) o.get("link");
						if(s.charAt(s.length()-4) != '.' && s.charAt(s.length()-5) != '.'){
							s += ".png";
						}
						PImage pimg = sketch.loadImage(s);

						if(pimg != null && pimg.width != -1 && pimg.height != -1){
							//articleImg = new ImageZone(sketch.loadImage((String) o.get("link")), (int) (lineX + Math.random() * (sketch.getWidth()-lineX)/2), sketch.getHeight()/2, sketch.getHeight()/5, sketch.getHeight()/5);
							//sketch.client.addZone(imgZone);
							if(pimg.width > IMAGE_W_MIN && pimg.height > IMAGE_H_MIN){
								imgVec.add(pimg);
								imgFlag = true;
								content = true;
							}

						}
					}
				}
				if(imgFlag){
					PImage[] pimg = new PImage[imgVec.size()];
					imgVec.copyInto(pimg);


					articleImg = new ImageZone(pimg, sketch.lineX + wSpace, - hSpace, w, h+ 8*sketch.shadowOffset, sketch.radius){

						public void tapEvent(TapEvent e){
							if (isTappable()){

								if(getImageIndex()+1 < getImageArray().length-1){
									int index = getImageIndex() + 1;
									setAnimators(index);
									tg.contGetter2 = cg;


								} else {
									this.setActive(false);
									imgFlag = false;
									//tZone1.setActive(false);
									setImageIndex(0);
									tg.contGetter2 = cg;

									//sketch.client.removeZone(tZone1);
									//sketch.client.removeZone(this);
									//int index = 0;
									//setAnimators(index);	

									if(textFlag){
										article.animPosY = 0;
										float tDiff = 0;

										//TODO
										// -  tZone1.getY() + 2*sketch.shadowOffset; ?????
										tDiff = - hSpace  -  article.getY() + 4*sketch.shadowOffset;

										//tDiff = (sketch.getHeight()/2 - hSpace - h) -  (tZone1.getY() + 2*sketch.shadowOffset) ;

										if(tDiff != 0){
											animtZone1 = PropertySetter.createAnimator(sizeTime, article, "animPosY", 0f, tDiff);
											animtZone1.start();
										}
									}

								}
								e.setHandled(true);
							}
						}

						public void setAnimators(int index){
							animPosX = 0;
							animPosY = 0;
							animWidth = 0;
							animHeight = 0;
							if(textFlag){
								article.animPosY = 0;
							}

							float xDiff = (this.getX() + this.getWidth()/2 - articleImg.getImageArray()[index].width/2 - articleImg.getX());
							float yDiff = (this.getY() + this.getHeight()/2 - articleImg.getImageArray()[index].height/2 - articleImg.getY());
							float wDiff = (articleImg.getImageArray()[index].width - articleImg.getWidth());
							float hDiff = (articleImg.getImageArray()[index].height - articleImg.getHeight());

							float tDiff = 0;
							if(textFlag){
								//int tDiff = articleImg.getY() + (articleImg.getY() + articleImg.getHeight()/2 - articleImg.getImageArray()[index].height/2 - articleImg.getY()) + articleImg.getImageArray()[index].height - tZone1.getY();


								if(yDiff != 0 && yDiff + articleImg.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
									if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
										tDiff = articleImg.getY() + yDiff + articleImg.getHeight() + hDiff -  article.getY() + 2*sketch.shadowOffset;
									} else {
										tDiff = articleImg.getY() + yDiff + articleImg.getHeight() - article.getY() + 2*sketch.shadowOffset;

									}
								} else {
									if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
										tDiff = articleImg.getY() + articleImg.getHeight() + hDiff - article.getY()+ 2*sketch.shadowOffset;

									} else {
										tDiff = articleImg.getY() + articleImg.getHeight() -  article.getY() + 2*sketch.shadowOffset;

									}
								}
							}
							if((animZoneX == null || !animZoneX.isRunning()) && (animZoneY == null || !animZoneY.isRunning()) && (animZoneWidth == null || !animZoneWidth.isRunning()) && (animZoneHeight == null || !animZoneHeight.isRunning()) && (animtZone1 == null || !animtZone1.isRunning())){
								if(xDiff != 0){
									animZoneX = PropertySetter.createAnimator(sizeTime, articleImg, "animPosX", 0f, xDiff);
									animZoneX.start();
								}
								if(yDiff != 0 && yDiff + articleImg.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
									animZoneY = PropertySetter.createAnimator(sizeTime, articleImg, "animPosY", 0f, yDiff);
									animZoneY.start();
								}
								if(wDiff != 0){
									animZoneWidth = PropertySetter.createAnimator(sizeTime, articleImg, "animWidth", 0f, wDiff);
									animZoneWidth.start();
								}
								if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
									animZoneHeight = PropertySetter.createAnimator(sizeTime, articleImg, "animHeight", 0f, hDiff);
									animZoneHeight.start();
								}
								if(textFlag){
									if(tDiff != 0){
										animtZone1 = PropertySetter.createAnimator(sizeTime, article, "animPosY", 0f, tDiff);
										animtZone1.start();
									}
								}

								setImageIndex(index);
							}


						}

					};
					articleImg.setShadowColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
					articleImg.setShadowX(-sketch.shadowOffset);
					articleImg.setShadowY(-sketch.shadowOffset);
					articleImg.setShadowW(2*sketch.shadowOffset);
					articleImg.setShadowH(2*sketch.shadowOffset);
					//articleImg.setColour(Colours.boundingBox.getRed(), Colours.boundingBox.getGreen(), Colours.boundingBox.getBlue());
					articleImg.setShadow(true);
					articleImg.setStroke(false);
					articleImg.setDrawBorder(false);
					articleImg.setDrawOnlyImage(false);
					articleImg.setGestureEnabled("Tap", true);
					articleImg.setActive(false);
					articleImg.rotate((float) Colours.PI);
					sketch.client.addZone(articleImg);

					int index = 0;
					float xDiff = (articleImg.getX() + articleImg.getWidth()/2 - articleImg.getImageArray()[index].width/2 - articleImg.getX());
					float yDiff = (articleImg.getY() + articleImg.getHeight()/2 - articleImg.getImageArray()[index].height/2 - articleImg.getY());
					float wDiff = (articleImg.getImageArray()[index].width - articleImg.getWidth());
					float hDiff = (articleImg.getImageArray()[index].height - articleImg.getHeight());

					if(xDiff != 0){
						animZoneX = PropertySetter.createAnimator(sizeTime, articleImg, "animPosX", 0f, xDiff);
						animZoneX.start();
					}
					if(yDiff != 0 && yDiff + articleImg.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
						animZoneY = PropertySetter.createAnimator(sizeTime, articleImg, "animPosY", 0f, yDiff);
						animZoneY.start();
					}
					if(wDiff != 0){
						animZoneWidth = PropertySetter.createAnimator(sizeTime, articleImg, "animWidth", 0f, wDiff);
						animZoneWidth.start();
					}
					if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
						animZoneHeight = PropertySetter.createAnimator(sizeTime, articleImg, "animHeight", 0f, hDiff);
						animZoneHeight.start();
					}






				}
			}
			if(diffbot.has("title") && threadFlag == this){
				String title = (String) diffbot.get("title");
				String body = "";
				if(diffbot.has("text")){
					body = (String) diffbot.get("text");

				}
				title = title.trim();
				body = body.trim();
				if(body.length() > CHAR_LIMIT){
					body = body.substring(0, CHAR_LIMIT);
				}
				if(body.length() > TEXT_COUNT_MIN){
					content = true;
					textFlag = true;
					
					createUIElement2(title, body);

					//float textSize = sketch.getHeight()/100;
					if(imgVec != null && imgVec.size() > 0){

						int index = 0;
						float yDiff = (articleImg.getY() - articleImg.getImageArray()[index].height/2);
						float hDiff = (articleImg.getImageArray()[index].height - articleImg.getHeight());
						float tDiff = 0;

						if(yDiff != 0 && yDiff + articleImg.getY() > tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
							if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
								tDiff = articleImg.getY() + yDiff + articleImg.getHeight() + hDiff -  article.getY() + 2*sketch.shadowOffset;
							} else {
								tDiff = articleImg.getY() + yDiff + articleImg.getHeight() -  article.getY() +2*sketch.shadowOffset;

							}
						} else {
							if(hDiff != 0 && hDiff + articleImg.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 + sketch.shadowOffset)/2){
								tDiff = articleImg.getY() + articleImg.getHeight() + hDiff -  article.getY() + 2*sketch.shadowOffset;

							} else {
								tDiff = articleImg.getY() + articleImg.getHeight() -  article.getY() + 2*sketch.shadowOffset;

							}
						}
						if(tDiff != 0){
							animtZone1 = PropertySetter.createAnimator(sizeTime, article, "animPosY", 0f, tDiff);
							animtZone1.start();
						}
					} 
				}
			}
		} catch (JSONException e) {
			PApplet.println ("There was an error parsing the JSONObject.");
			e.printStackTrace();
		}
	}
	
	public void createUIElement1(final String title, String bodyText){
		final String body = bodyText + "...";
		

		final int bodyStartY = (int) (sketch.getHeight()/2 + 1.75*textYOffset);
		final int bodyHeight = sketch.getHeight() - bodyStartY;
		final int contentHeight = sketch.calculateTextHeight(body, widthBody - xMargin*2, textSize, PConstants.LEFT, PConstants.TOP); 

		int graphicsHeightTemp = yMargin + 2*textYSpacing + contentHeight; 
		int temp= 0;

		if(graphicsHeightTemp  < bodyHeight){
			temp = bodyHeight;
		} else {
			temp = graphicsHeightTemp;
		}

		final int graphicsHeight = temp;

		PGraphics pg = sketch.createGraphics(widthBody, graphicsHeight, PConstants.P3D);
		final PGraphics backBuffer = sketch.createGraphics(widthBody, graphicsHeight, PConstants.P3D);

		article = new ArticleContainer(sketch, sketch.lineX + widthOffset, bodyStartY, widthBody, bodyHeight, sketch.radius, pg, 
				textYSpacing, textSize, xMargin, yMargin, graphicsHeight){
			
			public void setUpGraphicsContext(){
				PGraphics pg = this.getPGraphics();
				pg.beginDraw();
				pg.background(Colours.bodyTextBackground);
				drawHighlight(pg);
				pg.textFont(Colours.pFont, textSize); 
				pg.textAlign(PConstants.CENTER, PConstants.CENTER);
				pg.fill(Colours.newsTextColor.getRed(), Colours.newsTextColor.getGreen(),Colours.newsTextColor.getBlue());
				
				float tw = pg.textWidth(title);
				float offset = 0;
				
				if(tw >= widthBody - xMargin*2){
					pg.text(title, xMargin, yMargin, widthBody - xMargin*2, textSize*3);
					offset = 2*textYSpacing;
				} else {
					pg.text(title, xMargin, yMargin, widthBody - xMargin*2, textSize);
				}
				pg.textAlign(PConstants.LEFT, PConstants.TOP);
				setBody(pg, body, xMargin, yMargin + 2*textYSpacing + offset, widthBody - xMargin, contentHeight*2);

				pg.endDraw();

				backBuffer.beginDraw();
				backBuffer.background(0);
				backBuffer.textFont(Colours.pFont, textSize); 
				backBuffer.textAlign(PConstants.CENTER, PConstants.CENTER);
				drawBackBuffer(backBuffer, title, PConstants.CENTER, PConstants.CENTER, 0, xMargin, widthBody - xMargin);
				drawBackBuffer(backBuffer, body, PConstants.LEFT, PConstants.TOP, 2*textYSpacing + offset, xMargin, widthBody - xMargin);
				backBuffer.endDraw();
				red = 1;
				green = 1;
				blue = 1;

			}

			public void drawHighlight(PGraphics pg){

				if(wordTapped){
					if(wordMap1.containsKey(colourPicker1)){
						pg.noStroke();

						pg.fill(Colours.highlightTextColour.getRed(), Colours.highlightTextColour.getGreen(),Colours.highlightTextColour.getBlue());
						pg.rect(wordX1.get(colourPicker1)[0], wordX1.get(colourPicker1)[1], wordX1.get(colourPicker1)[2], wordX1.get(colourPicker1)[3]);
					}
				} else {
					tg.twitterAct.fadeTweetWordButton(1);
				}

			}

			
			public void tapEvent(TapEvent e){
				colourPicker1 = sketch.color(backBuffer.get((int)(e.getX()-getX()),(int)( e.getY()-getY() + getYOffset())));

				if(wordMap1.containsKey(sketch.color(backBuffer.get((int)(e.getX()-getX()), (int)(e.getY()-getY() + getYOffset()))))){
					String ss = wordMap1.get(sketch.color(backBuffer.get((int)(e.getX()-getX()), (int)(e.getY()-getY() + getYOffset()))));
					tg.twitterAct.middleZone.setMiddleText(ss);
					tg.twitterAct.selectedWord = ss;
					if(!tg.twitterAct.middleZone.started){
						tg.twitterAct.middleZone.started = true;
					}
					tg.twitterAct.lastUser = 1;
					
					//TODO
					//tg.twitterAct.started = true;
					wordTapped = true;
					
					if(tg.contGetter2 != null) {
						tg.contGetter2.wordTapped = false;
					}
					tg.twitterAct.tweetWord1.setGestureEnabled("Tap", true);
					tg.twitterAct.tweetWord1.setColour(Colours.unselectedZone);
					tg.twitterAct.tweetWord1.setTextColour(Colours.zoneText);


				}

				tg.contGetter1 = cg;

			}

		};

		article.setTwitActivity(this, 1);
		article.setDrawBorder(false);
		article.setGestureEnabled("Drag", true);
		article.setGestureEnabled("Tap", true);
		article.setShadow(true);
		article.setShadowColour(Colours.shadow.getRed(), Colours.shadow.getGreen(), Colours.shadow.getBlue());
		article.setStroke(false);
		article.setShadowX(-sketch.shadowOffset);
		article.setShadowY(-sketch.shadowOffset);
		article.setShadowW(2*sketch.shadowOffset);
		article.setShadowH(2*sketch.shadowOffset);
		article.setActive(false);

		sketch.client.addZone(article);



	}

	public void createUIElement2(final String title, String bodyText){
		final String body = bodyText + "...";
		
		
		final int bodyHeight = (int) (sketch.getHeight()/2 - 1.75*textYOffset);
		final int contentHeight = sketch.calculateTextHeight(body, widthBody - xMargin*2, textSize, PConstants.LEFT, PConstants.TOP); 

		int graphicsHeightTemp = yMargin + 2*textYSpacing + contentHeight; 
		int temp= 0;

		if(graphicsHeightTemp  < bodyHeight){
			temp = bodyHeight;
		} else {
			temp = graphicsHeightTemp;
		}

		final int graphicsHeight = temp;

		PGraphics pg = sketch.createGraphics(widthBody, graphicsHeight, PConstants.P3D);
		final PGraphics backBuffer = sketch.createGraphics(widthBody, graphicsHeight, PConstants.P3D);

		article = new ArticleContainer(sketch, sketch.lineX + widthOffset, 0, widthBody, bodyHeight, sketch.radius, pg, 
				textYSpacing, textSize, xMargin, yMargin, graphicsHeight){
			
			public void setUpGraphicsContext(){
				PGraphics pg = this.getPGraphics();
				pg.beginDraw();
				pg.background(Colours.bodyTextBackground);
				drawHighlight(pg);
				pg.textFont(Colours.pFont, textSize); 
				pg.textAlign(PConstants.CENTER, PConstants.CENTER);
				pg.fill(Colours.textColour.getRed(), Colours.textColour.getGreen(),Colours.textColour.getBlue());
				
				float tw = pg.textWidth(title);
				float offset = 0;
				if(tw >= widthBody - xMargin*2){
					pg.text(title, xMargin, yMargin, widthBody - xMargin*2, textSize*3);
					offset = 2*textYSpacing;
				} else {
					pg.text(title, xMargin, yMargin, widthBody - xMargin*2, textSize);
				}
				pg.textAlign(PConstants.LEFT, PConstants.TOP);
				setBody(pg, body, xMargin, yMargin + 2*textYSpacing + offset, widthBody - xMargin, contentHeight*2);

				pg.endDraw();

				backBuffer.beginDraw();
				backBuffer.background(0);
				backBuffer.textFont(Colours.pFont, textSize); 
				backBuffer.textAlign(PConstants.CENTER, PConstants.CENTER);
				drawBackBuffer(backBuffer, title, PConstants.CENTER, PConstants.CENTER, 0, xMargin, widthBody - xMargin);
				drawBackBuffer(backBuffer, body, PConstants.LEFT, PConstants.TOP, 2*textYSpacing + offset, xMargin, widthBody - xMargin);
				backBuffer.endDraw();
				red = 1;
				green = 1;
				blue = 1;

			}

			public void drawHighlight(PGraphics pg){

				if(wordTapped){
					if(wordMap1.containsKey(colourPicker1)){
						pg.noStroke();

						pg.fill(Colours.highlightTextColour.getRed(), Colours.highlightTextColour.getGreen(),Colours.highlightTextColour.getBlue());
						pg.rect(wordX1.get(colourPicker1)[0], wordX1.get(colourPicker1)[1], wordX1.get(colourPicker1)[2], wordX1.get(colourPicker1)[3]);
					}
				} else {
					tg.twitterAct.fadeTweetWordButton(2);
				}

			}

			public void tapEvent(TapEvent e){
				float newX =  ((e.getX() * this.matrix.m00 + e.getY() * this.matrix.m01 + 0 * this.matrix.m02 + this.matrix.m03))-x;
				float newY =  ((e.getX() * this.matrix.m10 + e.getY() * this.matrix.m11 + 0 * this.matrix.m12 + this.matrix.m13))-y;

				colourPicker1 = sketch.color(backBuffer.get((int)newX, (int)(newY + getYOffset())));

				if(wordMap1.containsKey(colourPicker1)){
					String ss = wordMap1.get(colourPicker1);
					tg.twitterAct.middleZone.setMiddleText(ss);
					tg.twitterAct.selectedWord = ss;
					if(!tg.twitterAct.middleZone.started){
						tg.twitterAct.middleZone.started = true;
					}
					tg.twitterAct.lastUser = 2;

					//TODO
					//tg.twitterAct.started = true;
					wordTapped = true;
					
					if(tg.contGetter1 != null) {
						tg.contGetter1.wordTapped = false;
					}
					tg.twitterAct.tweetWord2.setGestureEnabled("Tap", true);
					tg.twitterAct.tweetWord2.setColour(Colours.unselectedZone);
					tg.twitterAct.tweetWord2.setTextColour(Colours.zoneText);


				}

				tg.contGetter2 = cg;
			}


		};

		article.setTwitActivity(this, 2);
		article.setDrawBorder(false);
		article.setGestureEnabled("Drag", true);
		article.setGestureEnabled("Tap", true);
		article.setShadow(true);
		article.setShadowColour(Colours.shadow.getRed(), Colours.shadow.getGreen(), Colours.shadow.getBlue());
		article.setStroke(false);
		article.setShadowX(-sketch.shadowOffset);
		article.setShadowY(-sketch.shadowOffset);
		article.setShadowW(2*sketch.shadowOffset);
		article.setShadowH(2*sketch.shadowOffset);
		article.setActive(false);

		article.rotate((float) Colours.PI);

		sketch.client.addZone(article);



	}

	/*public void loadVideo4TwitterAct(final String href, final VideoPlayer player){
		final ContentGetter cg = this;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				cg.player = player;
				cg.player.init(href);
			}
		});
	}*/

}
