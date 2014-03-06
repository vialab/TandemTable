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
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import vialab.simpleMultiTouch.events.DragEvent;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.zones.ImageZone;
import vialab.simpleMultiTouch.zones.PGraphicsZone;
import vialab.simpleMultiTouch.zones.RectZone;

public class ContentGetter extends Thread{
	
	public RectZone loading;
	RectZone background;
	ImageZone img1;
	PGraphicsZone tZone1;
	Sketch sketch;
	TwitterGetter tg;
	ContentGetter cg;
	Animator animZoneX, animZoneY, animZoneWidth, animZoneHeight,  animtZone1;
	
	boolean imgFlag = false, textFlag = false, activated = false, wordTapped1 = false, content = false;
	public boolean videoFlag = false;

	String regex = "[ \t\n\f\r]+";
	String URL = null;
	String url;

	HashMap<Integer, String> wordMap1 = new HashMap<Integer, String>();
	HashMap<Integer, Float[]> wordX1 = new HashMap<Integer, Float[]>();

	private volatile Thread threadFlag;
	
	final int CHAR_LIMIT = 1000;
	final int TEXT_COUNT_MIN = 2; 
	final int IMAGE_W_MIN = 50;
	final int IMAGE_H_MIN = 50;
	
	int red1 = 1;
	int green1 = 1;
	int blue1 = 1;
	int sizeTime = 300;
	int colourPicker1, textSize, height, textYOffset, widthOffset, widthBody, textYSpacing, user, x, c;



	public ContentGetter(final Sketch sketch, int user, final TwitterGetter tg, int i, int j){
		
		this.sketch = sketch;
		this.user = user;
		this.threadFlag = this;
		this.tg = tg;
		//IMAGE_W_MIN = 0;//(sketch.getWidth()-lineX)/20;
		//IMAGE_H_MIN = 0;//(sketch.getHeight()/2)/15;;
		cg = this;
		x = i;
		c = j;

		final Gif ajaxGIF = new Gif(sketch, "ajaxGIF.gif");
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
						tg.currentGetter1 = cg;
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
						tg.currentGetter2 = cg;
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
					this.img1.setActive(true);
				}
				if(textFlag){
					this.tZone1.setActive(true);
				}
			}

			if(content){
				if(user == 1){
					if(!((sketch.deactivateVideo || sketch.removeVideoAct) && videoFlag)) {
						tg.twitterAct.tweetZones1[x][c].setTextColour(Color.BLUE);
					}
					
					if(videoFlag) {// && !(sketch.deactivateVideo || sketch.removeVideoAct)){
						tg.twitterAct.contentVisual1[x][c] = Colours.videoIndicator; 
					} else if(imgFlag){
						tg.twitterAct.contentVisual1[x][c] = Colours.imageIndicator;
					} else if (textFlag){
						tg.twitterAct.contentVisual1[x][c] = Colours.textIndicator;
					}
				} else if(user == 2){
					if(!((sketch.deactivateVideo || sketch.removeVideoAct) && videoFlag)) {
						tg.twitterAct.tweetZones2[x][c].setTextColour(Color.BLUE);
					}
					
					if(videoFlag) {// && !(sketch.deactivateVideo || sketch.removeVideoAct)){
						tg.twitterAct.contentVisual2[x][c] = Colours.videoIndicator; 
					} else if(imgFlag){
						tg.twitterAct.contentVisual2[x][c] = Colours.imageIndicator;
					} else if (textFlag){
						tg.twitterAct.contentVisual2[x][c] = Colours.textIndicator;
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
		if(img1 != null){
			imgFlag = false;
			sketch.client.removeZone(img1);
		}
		if(tZone1 != null){
			textFlag = false;
			sketch.client.removeZone(tZone1);
		}
	}
	
	public void deactivateZones(){
		activated = false;
		loading.setActive(false);
		background.setActive(false);
		if(img1 != null){
			img1.setActive(false);
		}
		if(tZone1 != null){
			tZone1.setActive(false);
		}

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


			//System.out.println(diffbot);


			//if(user == 1){
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
							//img1 = new ImageZone(sketch.loadImage((String) o.get("link")), (int) (lineX + Math.random() * (sketch.getWidth()-lineX)/2), sketch.getHeight()/2, sketch.getHeight()/5, sketch.getHeight()/5);
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




					img1 = new ImageZone(pimg, sketch.lineX + wSpace, sketch.getHeight()/2 + hSpace, w, h, sketch.radius){

						public void tapEvent(TapEvent e){
							if (isTappable()){

								if(getImageIndex()+1 < getImageArray().length-1){
									int index = getImageIndex() + 1;
									setAnimators(index);
									if(user == 1){
										tg.currentGetter1 = cg;
									} else if(user == 2){
										tg.currentGetter2 = cg;
									}

								} else {
									this.setActive(false);
									imgFlag = false;
									setImageIndex(0);
									if(user == 1){
										tg.currentGetter1 = cg;
									} else if(user == 2){
										tg.currentGetter2 = cg;
									}


									if(textFlag){
										tZone1.animPosY = 0;
										float tDiff = 0;

										tDiff = (sketch.getHeight()/2 + hSpace) -  tZone1.getY() + 2*sketch.shadowOffset;

										if(tDiff != 0){
											animtZone1 = PropertySetter.createAnimator(sizeTime, tZone1, "animPosY", 0f, tDiff);
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
								tZone1.animPosY = 0;
							}

							float xDiff = (this.getX() + this.getWidth()/2 - img1.getImageArray()[index].width/2 - img1.getX());
							float yDiff = (this.getY() + this.getHeight()/2 - img1.getImageArray()[index].height/2 - img1.getY());
							float wDiff = (img1.getImageArray()[index].width - img1.getWidth());
							float hDiff = (img1.getImageArray()[index].height - img1.getHeight());

							float tDiff = 0;
							if(textFlag){
								//int tDiff = img1.getY() + (img1.getY() + img1.getHeight()/2 - img1.getImageArray()[index].height/2 - img1.getY()) + img1.getImageArray()[index].height - tZone1.getY();


								if(yDiff != 0 && yDiff + img1.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
									if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
										tDiff = img1.getY() + yDiff + img1.getHeight() + hDiff -  tZone1.getY() + 2*sketch.shadowOffset;
									} else {
										tDiff = img1.getY() + yDiff + img1.getHeight() - tZone1.getY() + 2*sketch.shadowOffset;

									}
								} else {
									if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
										tDiff = img1.getY() + img1.getHeight() + hDiff - tZone1.getY()+ 2*sketch.shadowOffset;

									} else {
										tDiff = img1.getY() + img1.getHeight() -  tZone1.getY() + 2*sketch.shadowOffset;

									}
								}
							}
							if((animZoneX == null || !animZoneX.isRunning()) && (animZoneY == null || !animZoneY.isRunning()) && (animZoneWidth == null || !animZoneWidth.isRunning()) && (animZoneHeight == null || !animZoneHeight.isRunning()) && (animtZone1 == null || !animtZone1.isRunning())){
								if(xDiff != 0){
									animZoneX = PropertySetter.createAnimator(sizeTime, img1, "animPosX", 0f, xDiff);
									animZoneX.start();
								}
								if(yDiff != 0 && yDiff + img1.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
									animZoneY = PropertySetter.createAnimator(sizeTime, img1, "animPosY", 0f, yDiff);
									animZoneY.start();
								}
								if(wDiff != 0){
									animZoneWidth = PropertySetter.createAnimator(sizeTime, img1, "animWidth", 0f, wDiff);
									animZoneWidth.start();
								}
								if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
									animZoneHeight = PropertySetter.createAnimator(sizeTime, img1, "animHeight", 0f, hDiff);
									animZoneHeight.start();
								}
								if(textFlag){
									if(tDiff != 0){
										animtZone1 = PropertySetter.createAnimator(sizeTime, tZone1, "animPosY", 0f, tDiff);
										animtZone1.start();
									}
								}

								setImageIndex(index);
							}


						}

					};
					img1.setShadowColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
					img1.setShadowX(-sketch.shadowOffset);
					img1.setShadowY(-sketch.shadowOffset);
					img1.setShadowW(2*sketch.shadowOffset);
					img1.setShadowH(2*sketch.shadowOffset);
					//img1.setColour(Colours.boundingBox.getRed(), Colours.boundingBox.getGreen(), Colours.boundingBox.getBlue());
					img1.setShadow(true);
					img1.setStroke(false);
					img1.setDrawBorder(false);
					img1.setDrawOnlyImage(false);
					img1.setGestureEnabled("Tap", true);
					img1.setActive(false);
					sketch.client.addZone(img1);

					int index = 0;
					float xDiff = (img1.getX() + img1.getWidth()/2 - img1.getImageArray()[index].width/2 - img1.getX());
					float yDiff = (img1.getY() + img1.getHeight()/2 - img1.getImageArray()[index].height/2 - img1.getY());
					float wDiff = (img1.getImageArray()[index].width - img1.getWidth());
					float hDiff = (img1.getImageArray()[index].height - img1.getHeight());

					if(xDiff != 0){
						animZoneX = PropertySetter.createAnimator(sizeTime, img1, "animPosX", 0f, xDiff);
						animZoneX.start();
					}
					if(yDiff != 0 && yDiff + img1.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
						animZoneY = PropertySetter.createAnimator(sizeTime, img1, "animPosY", 0f, yDiff);
						animZoneY.start();
					}
					if(wDiff != 0){
						animZoneWidth = PropertySetter.createAnimator(sizeTime, img1, "animWidth", 0f, wDiff);
						animZoneWidth.start();
					}
					if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
						animZoneHeight = PropertySetter.createAnimator(sizeTime, img1, "animHeight", 0f, hDiff);
						animZoneHeight.start();
					}






				}
				//System.out.println(results1);
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
					if(imgVec != null && imgVec.size() > 0){

						createUIElement1(title, body);
						int index = 0;
						float yDiff = (img1.getY() + img1.getHeight()/2 - img1.getImageArray()[index].height/2 - img1.getY());
						float hDiff = (img1.getImageArray()[index].height - img1.getHeight());
						float tDiff = 0;

						if(yDiff != 0 && yDiff + img1.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
							if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
								tDiff = img1.getY() + yDiff + img1.getHeight() + hDiff -  tZone1.getY() + 2*sketch.shadowOffset;
							} else {
								tDiff = img1.getY() + yDiff + img1.getHeight() -  tZone1.getY() +2*sketch.shadowOffset;

							}
						} else {
							if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 + sketch.shadowOffset)/2){
								tDiff = img1.getY() + img1.getHeight() + hDiff -  tZone1.getY() + 2*sketch.shadowOffset;

							} else {
								tDiff = img1.getY() + img1.getHeight() -  tZone1.getY() + 2*sketch.shadowOffset;

							}
						}
						if(tDiff != 0){
							animtZone1 = PropertySetter.createAnimator(sizeTime, tZone1, "animPosY", 0f, tDiff);
							animtZone1.start();
						}
					} else {
						createUIElement1(title, body);

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
							//img1 = new ImageZone(sketch.loadImage((String) o.get("link")), (int) (lineX + Math.random() * (sketch.getWidth()-lineX)/2), sketch.getHeight()/2, sketch.getHeight()/5, sketch.getHeight()/5);
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


					img1 = new ImageZone(pimg, sketch.lineX + wSpace, - hSpace, w, h+ 8*sketch.shadowOffset, sketch.radius){

						public void tapEvent(TapEvent e){
							if (isTappable()){

								if(getImageIndex()+1 < getImageArray().length-1){
									int index = getImageIndex() + 1;
									setAnimators(index);
									tg.currentGetter2 = cg;


								} else {
									this.setActive(false);
									imgFlag = false;
									//tZone1.setActive(false);
									setImageIndex(0);
									tg.currentGetter2 = cg;

									//sketch.client.removeZone(tZone1);
									//sketch.client.removeZone(this);
									//int index = 0;
									//setAnimators(index);	

									if(textFlag){
										tZone1.animPosY = 0;
										float tDiff = 0;

										//TODO
										// -  tZone1.getY() + 2*sketch.shadowOffset; ?????
										tDiff = - hSpace  -  tZone1.getY() + 4*sketch.shadowOffset;

										//tDiff = (sketch.getHeight()/2 - hSpace - h) -  (tZone1.getY() + 2*sketch.shadowOffset) ;

										if(tDiff != 0){
											animtZone1 = PropertySetter.createAnimator(sizeTime, tZone1, "animPosY", 0f, tDiff);
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
								tZone1.animPosY = 0;
							}

							float xDiff = (this.getX() + this.getWidth()/2 - img1.getImageArray()[index].width/2 - img1.getX());
							float yDiff = (this.getY() + this.getHeight()/2 - img1.getImageArray()[index].height/2 - img1.getY());
							float wDiff = (img1.getImageArray()[index].width - img1.getWidth());
							float hDiff = (img1.getImageArray()[index].height - img1.getHeight());

							float tDiff = 0;
							if(textFlag){
								//int tDiff = img1.getY() + (img1.getY() + img1.getHeight()/2 - img1.getImageArray()[index].height/2 - img1.getY()) + img1.getImageArray()[index].height - tZone1.getY();


								if(yDiff != 0 && yDiff + img1.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
									if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
										tDiff = img1.getY() + yDiff + img1.getHeight() + hDiff -  tZone1.getY() + 2*sketch.shadowOffset;
									} else {
										tDiff = img1.getY() + yDiff + img1.getHeight() - tZone1.getY() + 2*sketch.shadowOffset;

									}
								} else {
									if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
										tDiff = img1.getY() + img1.getHeight() + hDiff - tZone1.getY()+ 2*sketch.shadowOffset;

									} else {
										tDiff = img1.getY() + img1.getHeight() -  tZone1.getY() + 2*sketch.shadowOffset;

									}
								}
							}
							if((animZoneX == null || !animZoneX.isRunning()) && (animZoneY == null || !animZoneY.isRunning()) && (animZoneWidth == null || !animZoneWidth.isRunning()) && (animZoneHeight == null || !animZoneHeight.isRunning()) && (animtZone1 == null || !animtZone1.isRunning())){
								if(xDiff != 0){
									animZoneX = PropertySetter.createAnimator(sizeTime, img1, "animPosX", 0f, xDiff);
									animZoneX.start();
								}
								if(yDiff != 0 && yDiff + img1.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
									animZoneY = PropertySetter.createAnimator(sizeTime, img1, "animPosY", 0f, yDiff);
									animZoneY.start();
								}
								if(wDiff != 0){
									animZoneWidth = PropertySetter.createAnimator(sizeTime, img1, "animWidth", 0f, wDiff);
									animZoneWidth.start();
								}
								if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
									animZoneHeight = PropertySetter.createAnimator(sizeTime, img1, "animHeight", 0f, hDiff);
									animZoneHeight.start();
								}
								if(textFlag){
									if(tDiff != 0){
										animtZone1 = PropertySetter.createAnimator(sizeTime, tZone1, "animPosY", 0f, tDiff);
										animtZone1.start();
									}
								}

								setImageIndex(index);
							}


						}

					};
					img1.setShadowColour(Colours.unselectedZone.getRed(), Colours.unselectedZone.getGreen(), Colours.unselectedZone.getBlue());
					img1.setShadowX(-sketch.shadowOffset);
					img1.setShadowY(-sketch.shadowOffset);
					img1.setShadowW(2*sketch.shadowOffset);
					img1.setShadowH(2*sketch.shadowOffset);
					//img1.setColour(Colours.boundingBox.getRed(), Colours.boundingBox.getGreen(), Colours.boundingBox.getBlue());
					img1.setShadow(true);
					img1.setStroke(false);
					img1.setDrawBorder(false);
					img1.setDrawOnlyImage(false);
					img1.setGestureEnabled("Tap", true);
					img1.setActive(false);
					img1.rotate((float) Colours.PI);
					sketch.client.addZone(img1);

					int index = 0;
					float xDiff = (img1.getX() + img1.getWidth()/2 - img1.getImageArray()[index].width/2 - img1.getX());
					float yDiff = (img1.getY() + img1.getHeight()/2 - img1.getImageArray()[index].height/2 - img1.getY());
					float wDiff = (img1.getImageArray()[index].width - img1.getWidth());
					float hDiff = (img1.getImageArray()[index].height - img1.getHeight());

					if(xDiff != 0){
						animZoneX = PropertySetter.createAnimator(sizeTime, img1, "animPosX", 0f, xDiff);
						animZoneX.start();
					}
					if(yDiff != 0 && yDiff + img1.getY() > sketch.getHeight()/2 + tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
						animZoneY = PropertySetter.createAnimator(sizeTime, img1, "animPosY", 0f, yDiff);
						animZoneY.start();
					}
					if(wDiff != 0){
						animZoneWidth = PropertySetter.createAnimator(sizeTime, img1, "animWidth", 0f, wDiff);
						animZoneWidth.start();
					}
					if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
						animZoneHeight = PropertySetter.createAnimator(sizeTime, img1, "animHeight", 0f, hDiff);
						animZoneHeight.start();
					}






				}
				//System.out.println(results1);
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
					//float textSize = sketch.getHeight()/100;
					if(imgVec != null && imgVec.size() > 0){

						createUIElement2(title, body);
						int index = 0;
						float yDiff = (img1.getY() - img1.getImageArray()[index].height/2);
						float hDiff = (img1.getImageArray()[index].height - img1.getHeight());
						float tDiff = 0;

						if(yDiff != 0 && yDiff + img1.getY() > tg.twitterAct.tweetTextSize/2 + sketch.shadowOffset){
							if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 - sketch.shadowOffset)/2){
								tDiff = img1.getY() + yDiff + img1.getHeight() + hDiff -  tZone1.getY() + 2*sketch.shadowOffset;
							} else {
								tDiff = img1.getY() + yDiff + img1.getHeight() -  tZone1.getY() +2*sketch.shadowOffset;

							}
						} else {
							if(hDiff != 0 && hDiff + img1.getHeight() < (sketch.getHeight()/2 - tg.twitterAct.tweetTextSize /2 + sketch.shadowOffset)/2){
								tDiff = img1.getY() + img1.getHeight() + hDiff -  tZone1.getY() + 2*sketch.shadowOffset;

							} else {
								tDiff = img1.getY() + img1.getHeight() -  tZone1.getY() + 2*sketch.shadowOffset;

							}
						}
						if(tDiff != 0){
							animtZone1 = PropertySetter.createAnimator(sizeTime, tZone1, "animPosY", 0f, tDiff);
							animtZone1.start();
						}
					} else {
						createUIElement2(title, body);

					}

				}
			}
		} catch (JSONException e) {
			PApplet.println ("There was an error parsing the JSONObject.");
			e.printStackTrace();
		}
	}
	
	public void createUIElement1(final String title, final String body){
		textYOffset = sketch.getHeight()/15;
		widthOffset = (int) (0.20*(sketch.getWidth()-sketch.lineX));
		widthBody = (int) (sketch.getWidth()- sketch.lineX - 2*widthOffset);
		textSize = 	sketch.getHeight()/50;
		textYSpacing = (int) (textSize*1.2);

		final int bodyStartY = (int) (sketch.getHeight()/2 + 1.75*textYOffset);
		final int bodyHeight = sketch.getHeight() - bodyStartY;
		final int xMargin = sketch.getWidth()/60;
		final int yMargin = sketch.getWidth()/60;
		final int contentHeight = calculateTextHeight(body, widthBody, textSize, PConstants.LEFT, PConstants.TOP); 

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

		tZone1 = new PGraphicsZone(sketch.lineX + widthOffset, bodyStartY, widthBody, bodyHeight, sketch.radius, pg){
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
				if(tw > widthBody){
					pg.text(title, 0, yMargin, widthBody, textSize*3);
					offset = 2*textYSpacing;
				} else {
					pg.text(title, 0, yMargin, widthBody, textSize);
				}
				pg.textAlign(PConstants.LEFT, PConstants.TOP);
				setBody(pg, body, xMargin, 0 + yMargin + 2*textYSpacing + offset, widthBody, contentHeight*2);

				pg.endDraw();

				backBuffer.beginDraw();
				backBuffer.background(0);
				backBuffer.textFont(Colours.pFont, textSize); 
				backBuffer.textAlign(PConstants.CENTER, PConstants.CENTER);
				drawBackBuffer(backBuffer, title, PConstants.CENTER, PConstants.CENTER, 0, 0);
				drawBackBuffer(backBuffer, body, PConstants.LEFT, PConstants.TOP, 2*textYSpacing + offset, xMargin);
				backBuffer.endDraw();
				red1 = 1;
				green1 = 1;
				blue1 = 1;

			}

			public void drawHighlight(PGraphics pg){

				if(wordTapped1){
					if(wordMap1.containsKey(colourPicker1)){
						pg.noStroke();

						pg.fill(Colours.highlightTextColour.getRed(), Colours.highlightTextColour.getGreen(),Colours.highlightTextColour.getBlue());
						pg.rect(wordX1.get(colourPicker1)[0], wordX1.get(colourPicker1)[1], wordX1.get(colourPicker1)[2], wordX1.get(colourPicker1)[3]);
					}
				} else {
					tg.twitterAct.tweetWord1.setGestureEnabled("Tap", false);
					tg.twitterAct.tweetWord1.setColour(Colours.fadedOutZone);
					tg.twitterAct.tweetWord1.setTextColour(Colours.fadedText);
				}

			}

			public void drawBackBuffer(PGraphics backBuffer, String t, int xAlign, int yAlign, float y, float x){
				String[] str = t.split(regex);
				int index = -1;
				float xx = x;
				//int wordIndex1 = 1;

				for(int k = 0; k < str.length; k++){
					if(str[k].equalsIgnoreCase("\n") || (xx + backBuffer.textWidth(str[k]) +backBuffer.textWidth(' ')) > widthBody){
						index = k;
						break;
					}
					xx += backBuffer.textWidth(str[k]) + backBuffer.textWidth(' ');
				}

				if(xAlign == PConstants.CENTER){
					if(index != -1){
						x = xMargin/2;//x = widthBody/2 - findWidth(str, 0, index+1)/2 + backBuffer.textWidth(' ')/2 * (str.length - 2);
					} else {
						x = widthBody/2 - backBuffer.textWidth(t)/2;
					}
				} else if (xAlign == PConstants.RIGHT){
					if(index != -1){
						//x = widthBody/2 + findWidth(str, 0, index+1)/2;
					} else {
						x = widthBody/2 + backBuffer.textWidth(t)/2;
					}
				}

				if(yAlign == PConstants.CENTER && index != -1){
					y += backBuffer.textAscent()/2;//textYSpacing;//textSize  + backBuffer.textAscent() / 2;
				} else if(yAlign == PConstants.TOP){
					y += 0;//;textSize  + backBuffer.textAscent();
				}

				for(int k = 0; k < str.length; k++){
					if(str[k].equalsIgnoreCase("\n") || (x + backBuffer.textWidth(str[k]) +backBuffer.textWidth(' ')) > widthBody){
						if(yAlign == PConstants.CENTER){
							y += textYSpacing;//textSize  + backBuffer.textAscent() / 2;
						} else if(yAlign == PConstants.TOP){
							y += textSize  + backBuffer.textAscent();
						}


						if(xAlign == PConstants.CENTER){
							float tw = findWidth(str, k, str.length, backBuffer);
							x = widthBody/2 - tw/2;
						} else if (xAlign == PConstants.RIGHT){
							float tw = findWidth(str, k, str.length, backBuffer);
							x = widthBody/2 + tw/2;
						} else {
							x = xMargin;
						}
					} 

					backBuffer.fill(red1, green1, blue1);
					backBuffer.rect(x, yMargin + y, backBuffer.textWidth(str[k]), textSize);

					wordMap1.put(backBuffer.color(red1, green1, blue1), str[k]);
					wordX1.put(backBuffer.color(red1, green1, blue1), new Float[]{x, yMargin + y, backBuffer.textWidth(str[k]), (float) textSize});

					x += backBuffer.textWidth(str[k]) + backBuffer.textWidth(' ');

					if(red1 < 255){
						red1++;
					} else if(green1 < 255){
						green1++;
					} else if(blue1 < 255){
						blue1++;
					}
				}

			}

			public void setBody(PGraphics pg, String t, float x, float y, int w, int h){
				String[] str = t.split(regex);
				float xx = x;
				//pg.textFont(Colours.pFont, textSize);
				//pg.fill(Colours.textColour.getRed(), Colours.textColour.getGreen(),Colours.textColour.getBlue());

				for(int k = 0; k < str.length; k++){
					if((xx + pg.textWidth(str[k]) + pg.textWidth(' ') > w)){
						y += textSize + pg.textAscent();
						xx = x;
					}
					//pg.fill(Colours.textColour.getRed(), Colours.textColour.getGreen(),Colours.textColour.getBlue());
					pg.text(str[k], xx, y);

					xx += pg.textWidth(str[k])+pg.textWidth(' ');



				}
			}
			
			public float findWidth(String[] str, int start, int end, PGraphics pg){
				float tw = 0;
				for(int q = start; q < end; q++){
					tw += pg.textWidth(str[q]);
				}
				return tw;
			}

			public void postDraw(){
				if(getYOffset() + 10 <= graphicsHeight-bodyHeight){
					float triWidth = sketch.getWidth()/100;
					float height = this.getY()+ this.getHeight() - triWidth/2;
					float width = this.getX()+ this.getWidth() - triWidth/2;
					sketch.fill(Colours.scrollTriColor.getRed(), Colours.scrollTriColor.getGreen(),Colours.scrollTriColor.getBlue());
					sketch.triangle(width-triWidth, height-triWidth, width-triWidth/2, height, width, height-triWidth);
				}

				if(getYOffset() - 10 >= 0){
					float triWidth = sketch.getWidth()/100;
					float height = this.getY() + triWidth/2;
					float width = this.getX()+ this.getWidth() - triWidth/2;
					sketch.fill(Colours.scrollTriColor.getRed(), Colours.scrollTriColor.getGreen(),Colours.scrollTriColor.getBlue());
					sketch.triangle(width-triWidth, height + triWidth, width-triWidth/2, height, width, height + triWidth);
				}

			}

			public void dragEvent(DragEvent e){
				if(isDraggable()){
					float dist = (e.getYDistance()/10 + this.getYOffset());

					if(dist <= graphicsHeight-bodyHeight && dist >= 0){
						this.setYOffset(dist);
					}
					e.setHandled(true);
				}
			}

			public void tapEvent(TapEvent e){
				colourPicker1 = sketch.color(backBuffer.get((int)(e.getX()-getX()),(int)( e.getY()-getY() + getYOffset())));

				if(wordMap1.containsKey(sketch.color(backBuffer.get((int)(e.getX()-getX()), (int)(e.getY()-getY() + getYOffset()))))){
					String ss = wordMap1.get(sketch.color(backBuffer.get((int)(e.getX()-getX()), (int)(e.getY()-getY() + getYOffset()))));
					tg.twitterAct.setMiddleTweet(ss);
					tg.twitterAct.selectedWord = ss;
					if(!tg.twitterAct.started){
						tg.twitterAct.started = true;
					}
					tg.twitterAct.lastUser = 1;
					//TODO
					//TODO
					//TODO
					//tg.twitterAct.started = true;
					wordTapped1 = true;
					//TODO
					tg.twitterAct.tweetWord1.setGestureEnabled("Tap", true);
					tg.twitterAct.tweetWord1.setColour(Colours.unselectedZone);
					tg.twitterAct.tweetWord1.setTextColour(Colours.zoneText);


				}

				tg.currentGetter1 = cg;

			}

		};


		tZone1.setDrawBorder(false);
		tZone1.setGestureEnabled("Drag", true);
		tZone1.setGestureEnabled("Tap", true);
		tZone1.setShadow(true);
		tZone1.setShadowColour(Colours.shadow.getRed(), Colours.shadow.getGreen(), Colours.shadow.getBlue());
		tZone1.setStroke(false);
		tZone1.setShadowX(-sketch.shadowOffset);
		tZone1.setShadowY(-sketch.shadowOffset);
		tZone1.setShadowW(2*sketch.shadowOffset);
		tZone1.setShadowH(2*sketch.shadowOffset);
		tZone1.setActive(false);

		sketch.client.addZone(tZone1);



	}

	public void createUIElement2(final String title, final String body){
		textYOffset = sketch.getHeight()/15;
		widthOffset = (int) (0.20*(sketch.getWidth()-sketch.lineX));
		widthBody = (int) (sketch.getWidth()- sketch.lineX - 2*widthOffset);
		textSize = 	sketch.getHeight()/50;
		textYSpacing = (int) (textSize*1.2);

		final int bodyHeight = (int) (sketch.getHeight()/2 - 1.75*textYOffset);
		final int xMargin = sketch.getWidth()/60;
		final int yMargin = sketch.getWidth()/60;
		final int contentHeight = calculateTextHeight(body, widthBody, textSize, PConstants.LEFT, PConstants.TOP); 

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

		tZone1 = new PGraphicsZone(sketch.lineX + widthOffset, 0, widthBody, bodyHeight, sketch.radius, pg){
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
				if(tw > widthBody){
					pg.text(title, 0, yMargin, widthBody, textSize*3);
					offset = 2*textYSpacing;
				} else {
					pg.text(title, 0, yMargin, widthBody, textSize);
				}
				pg.textAlign(PConstants.LEFT, PConstants.TOP);
				setBody(pg, body, xMargin, 0 + yMargin + 2*textYSpacing + offset, widthBody, contentHeight*2);

				pg.endDraw();

				backBuffer.beginDraw();
				backBuffer.background(0);
				backBuffer.textFont(Colours.pFont, textSize); 
				backBuffer.textAlign(PConstants.CENTER, PConstants.CENTER);
				drawBackBuffer(backBuffer, title, PConstants.CENTER, PConstants.CENTER, 0, 0);
				drawBackBuffer(backBuffer, body, PConstants.LEFT, PConstants.TOP, 2*textYSpacing + offset, xMargin);
				backBuffer.endDraw();
				red1 = 1;
				green1 = 1;
				blue1 = 1;

			}

			public void drawHighlight(PGraphics pg){

				if(wordTapped1){
					if(wordMap1.containsKey(colourPicker1)){
						pg.noStroke();

						pg.fill(Colours.highlightTextColour.getRed(), Colours.highlightTextColour.getGreen(),Colours.highlightTextColour.getBlue());
						pg.rect(wordX1.get(colourPicker1)[0], wordX1.get(colourPicker1)[1], wordX1.get(colourPicker1)[2], wordX1.get(colourPicker1)[3]);
					}
				} else {
					tg.twitterAct.tweetWord2.setGestureEnabled("Tap", false);
					tg.twitterAct.tweetWord2.setColour(Colours.fadedOutZone);
					tg.twitterAct.tweetWord2.setTextColour(Colours.fadedText);
				}

			}

			public void drawBackBuffer(PGraphics backBuffer, String t, int xAlign, int yAlign, float y, float x){
				String[] str = t.split(regex);
				int index = -1;
				float xx = x;
				//int wordIndex1 = 1;

				for(int k = 0; k < str.length; k++){
					if(str[k].equalsIgnoreCase("\n") || (xx + backBuffer.textWidth(str[k]) +backBuffer.textWidth(' ')) > widthBody){
						index = k;
						break;
					}
					xx += backBuffer.textWidth(str[k]) + backBuffer.textWidth(' ');
				}

				if(xAlign == PConstants.CENTER){
					if(index != -1){
						x = xMargin/2;//x = widthBody/2 - findWidth(str, 0, index+1)/2 + backBuffer.textWidth(' ')/2 * (str.length - 2);
					} else {
						x = widthBody/2 - backBuffer.textWidth(t)/2;
					}
				} else if (xAlign == PConstants.RIGHT){
					if(index != -1){
						//x = widthBody/2 + findWidth(str, 0, index+1)/2;
					} else {
						x = widthBody/2 + backBuffer.textWidth(t)/2;
					}
				}

				if(yAlign == PConstants.CENTER && index != -1){
					y += backBuffer.textAscent()/2;//textYSpacing;//textSize  + backBuffer.textAscent() / 2;
				} else if(yAlign == PConstants.TOP){
					y += 0;//;textSize  + backBuffer.textAscent();
				}

				for(int k = 0; k < str.length; k++){
					if(str[k].equalsIgnoreCase("\n") || (x + backBuffer.textWidth(str[k]) +backBuffer.textWidth(' ')) > widthBody){
						if(yAlign == PConstants.CENTER){
							y += textYSpacing;//textSize  + backBuffer.textAscent() / 2;
						} else if(yAlign == PConstants.TOP){
							y += textSize  + backBuffer.textAscent();
						}


						if(xAlign == PConstants.CENTER){
							float tw = findWidth(str, k, str.length, backBuffer);
							x = widthBody/2 - tw/2;
						} else if (xAlign == PConstants.RIGHT){
							float tw = findWidth(str, k, str.length, backBuffer);
							x = widthBody/2 + tw/2;
						} else {
							x = xMargin;
						}
					} 

					backBuffer.fill(red1, green1, blue1);
					backBuffer.rect(x, yMargin + y, backBuffer.textWidth(str[k]), textSize);

					wordMap1.put(backBuffer.color(red1, green1, blue1), str[k]);
					wordX1.put(backBuffer.color(red1, green1, blue1), new Float[]{x, yMargin + y, backBuffer.textWidth(str[k]), (float) textSize});

					x += backBuffer.textWidth(str[k]) + backBuffer.textWidth(' ');

					if(red1 < 255){
						red1++;
					} else if(green1 < 255){
						green1++;
					} else if(blue1 < 255){
						blue1++;
					}
				}

			}

			public void setBody(PGraphics pg, String t, float x, float y, int w, int h){
				String[] str = t.split(regex);
				float xx = x;
				//pg.textFont(Colours.pFont, textSize);
				//pg.fill(Colours.textColour.getRed(), Colours.textColour.getGreen(),Colours.textColour.getBlue());

				for(int k = 0; k < str.length; k++){
					if((xx + pg.textWidth(str[k]) + pg.textWidth(' ') > w)){
						y += textSize + pg.textAscent();
						xx = x;
					}
					//pg.fill(Colours.textColour.getRed(), Colours.textColour.getGreen(),Colours.textColour.getBlue());
					pg.text(str[k], xx, y);

					xx += pg.textWidth(str[k])+pg.textWidth(' ');



				}
			}
			
			public float findWidth(String[] str, int start, int end, PGraphics pg){
				float tw = 0;
				for(int q = start; q < end; q++){
					tw += pg.textWidth(str[q]);
				}
				return tw;
			}

			public void postDraw(){
				if(getYOffset() + 10 <= graphicsHeight-bodyHeight){
					float triWidth = sketch.getWidth()/100;
					float height = this.getY()+ this.getHeight() - triWidth/2;
					float width = this.getX()+ this.getWidth() - triWidth/2;
					sketch.fill(Colours.scrollTriColor.getRed(), Colours.scrollTriColor.getGreen(),Colours.scrollTriColor.getBlue());
					sketch.triangle(width-triWidth, height-triWidth, width-triWidth/2, height, width, height-triWidth);
				}

				if(getYOffset() - 10 >= 0){
					float triWidth = sketch.getWidth()/100;
					float height = this.getY() + triWidth/2;
					float width = this.getX()+ this.getWidth() - triWidth/2;
					sketch.fill(Colours.scrollTriColor.getRed(), Colours.scrollTriColor.getGreen(),Colours.scrollTriColor.getBlue());
					sketch.triangle(width-triWidth, height + triWidth, width-triWidth/2, height, width, height + triWidth);
				}

			}

			public void dragEvent(DragEvent e){
				if(isDraggable()){
					float dist = (e.getYDistance()/10 + this.getYOffset());

					if(dist <= graphicsHeight-bodyHeight && dist >= 0){
						this.setYOffset(dist);
					}
					e.setHandled(true);
				}
			}

			public void tapEvent(TapEvent e){
				float newX =  ((e.getX() * this.matrix.m00 + e.getY() * this.matrix.m01 + 0 * this.matrix.m02 + this.matrix.m03))-x;
				float newY =  ((e.getX() * this.matrix.m10 + e.getY() * this.matrix.m11 + 0 * this.matrix.m12 + this.matrix.m13))-y;

				colourPicker1 = sketch.color(backBuffer.get((int)newX, (int)(newY + getYOffset())));

				if(wordMap1.containsKey(colourPicker1)){
					String ss = wordMap1.get(colourPicker1);
					tg.twitterAct.setMiddleTweet(ss);
					tg.twitterAct.selectedWord = ss;
					if(!tg.twitterAct.started){
						tg.twitterAct.started = true;
					}
					tg.twitterAct.lastUser = 2;
					//TODO
					//TODO
					//TODO
					//tg.twitterAct.started = true;
					wordTapped1 = true;
					//TODO
					tg.twitterAct.tweetWord2.setGestureEnabled("Tap", true);
					tg.twitterAct.tweetWord2.setColour(Colours.unselectedZone);
					tg.twitterAct.tweetWord2.setTextColour(Colours.zoneText);


				}

				tg.currentGetter2 = cg;
			}


		};


		tZone1.setDrawBorder(false);
		tZone1.setGestureEnabled("Drag", true);
		tZone1.setGestureEnabled("Tap", true);
		tZone1.setShadow(true);
		tZone1.setShadowColour(Colours.shadow.getRed(), Colours.shadow.getGreen(), Colours.shadow.getBlue());
		tZone1.setStroke(false);
		tZone1.setShadowX(-sketch.shadowOffset);
		tZone1.setShadowY(-sketch.shadowOffset);
		tZone1.setShadowW(2*sketch.shadowOffset);
		tZone1.setShadowH(2*sketch.shadowOffset);
		tZone1.setActive(false);

		tZone1.rotate((float) Colours.PI);

		sketch.client.addZone(tZone1);



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

	///////////////////////////////////////////////////////////////////////
	////
	//// From user "steven" on processing.org
	//// http://processing.org/discourse/beta/num_1195937999.html
	////
	///////////////////////////////////////////////////////////////////////
	public int calculateTextHeight(String string, int specificWidth, int textSize, int xAlign, int yAlign) {
		String[] wordsArray;
		String tempString = "";
		int numLines = 0;
		float textHeight;

		wordsArray = string.split(" ");
		sketch.textFont(Colours.pFont, textSize);
		sketch.textAlign(xAlign, yAlign);
		for (int i=0; i < wordsArray.length; i++) {
			if (sketch.textWidth(tempString + wordsArray[i]) < specificWidth) {
				tempString += wordsArray[i] + " ";
			}
			else {
				tempString = wordsArray[i] + " ";
				numLines++;
			}
		}

		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '\n') {
				numLines++;
			}
		}

		numLines++; //adds the last line
		float textLeading = sketch.textDescent() + sketch.textDescent() * 1.275f;

		textHeight = numLines * textSize + numLines * textLeading;//(sketch.textDescent() + sketch.textAscent() + lineSpacing);
		return(PApplet.round(textHeight));
	} 

}
