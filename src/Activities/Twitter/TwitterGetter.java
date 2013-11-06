package Activities.Twitter;

import gifAnimation.Gif;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import vialab.simpleMultiTouch.RectZone;
import vialab.simpleMultiTouch.TextZone;
import vialab.simpleMultiTouch.events.TapEvent;
import Main.Colours;
import Main.MainSketch;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class TwitterGetter extends Thread {
	TwitterActivity twitterAct;
	MainSketch sketch;
	
	List<Status> tweets1, tweets2;
	RectZone loading;
	

	String replaceRegex = "[^a-zA-Z_0-9_'_�_�_� _�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�_�]";
	String regex = "[ \t\n\f\r]+";


	HashMap<Integer, ContentGetter>[] hmap1, hmap2;
	ContentGetter currentGetter1 = null, currentGetter2 = null;

	public TwitterGetter(MainSketch sketch, TwitterActivity twitterAct){

		this.twitterAct = twitterAct;
		this.sketch = sketch;

		final Gif ajaxGIF = new Gif(sketch, "ajaxGIF.gif");
		final PApplet app = sketch;
		ajaxGIF.loop();

		int width = sketch.getWidth()/16;
		loading = new RectZone((sketch.getWidth() - sketch.lineX)/2 + sketch.lineX - width/2, sketch.getHeight()/2 - width/2, width, width){
			public void drawZone(){
				super.drawZone();
				app.image(ajaxGIF, this.getX(), this.getY(), this.getWidth(), this.getHeight());
			}
		};
		loading.setDrawBorder(false);
		loading.setActive(false);
		sketch.client.addZone(loading);

		hmap1 = new HashMap[twitterAct.NUM_TWEETS];
		hmap2 = new HashMap[twitterAct.NUM_TWEETS];



	}

	public void run(){
		loading.setActive(true);
		if(!sketch.twitterInit){
			connect();
			sketch.twitterInit = true;
		}
		if(!this.twitterAct.canceled){
			setUpLayout();
		}
		if(!this.twitterAct.canceled){
			this.twitterAct.createMiddleTweet();
		}
		loading.setActive(false);

	}


	public void connect(){

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(Colours.consumerKeyTwitter);
		cb.setOAuthConsumerSecret(Colours.consumerSecretTwitter);
		cb.setOAuthAccessToken(Colours.tokenTwitter);
		cb.setOAuthAccessTokenSecret(Colours.tokenSecretTwitter);
		cb.setHttpConnectionTimeout(sketch.TIMEOUT);
		cb.setHttpReadTimeout(sketch.TIMEOUT);
		cb.setHttpStreamingReadTimeout(sketch.TIMEOUT);
		TwitterFactory tf = new TwitterFactory(cb.build());
		sketch.twitter = tf.getInstance();
	}

	public void setUpLayout(){
		if(!twitterAct.canceled){
			getTweets(1, twitterAct.topicExpanded1, false, false);
		}
		if(!twitterAct.canceled){
			getTweets(2, twitterAct.topicExpanded2, false, false);
		}
		if(!twitterAct.canceled){
			twitterAct.createBackground4Swipe();
			twitterAct.createTweetBackgrounds();
			createTweetZones(1);
			createTweetZones(2);
		}
		if(twitterAct.canceled){
			//twitterAct.removeZones();
		}
	}


	public void getTweets(int user, String topic, boolean failedOnce, boolean failedTwice){
		if(!twitterAct.canceled){
			try {
				Query query;
				QueryResult result;
				if(user == 1){
					twitterAct.tweetIndex1 = 0;
					query = new Query(topic);
					query.setCount(twitterAct.MAX_TWEETS);
					//query.setResultType(Query.POPULAR);
					query.setLang(twitterAct.langQuery1);
					//****////query.setPage(twitterAct.pageOffset1);
					result = sketch.twitter.search(query);
	
					tweets1 = result.getTweets();
					if(tweets1.size() <= 0 && failedTwice){
						twitterAct.errorFlag = true;
					} else if(tweets1.size() <= 0 && failedOnce && !failedTwice){
						twitterAct.pageOffset1 = 1;
						getTweets(1, twitterAct.topic1, false, true);
					} else if(tweets1.size() <= 0 && !failedOnce && !failedTwice){
						twitterAct.pageOffset1 = 1;
						getTweets(1, topic, true, false);
					}
	
				} else if (user == 2){
					twitterAct.tweetIndex2 = 0;
					query = new Query(topic);
					query.setCount(twitterAct.MAX_TWEETS);
					//query.setResultType(Query.POPULAR);
					query.setLang(twitterAct.langQuery2);
					//****////query.setPage(twitterAct.pageOffset2);
					result = sketch.twitter.search(query);
					tweets2 = result.getTweets();
	
					if(tweets2.size() <= 0 && failedTwice){
						twitterAct.errorFlag = true;
					} else if(tweets2.size() <= 0 && failedOnce && !failedTwice){
						twitterAct.pageOffset2 = 1;
						getTweets(2, twitterAct.topic2, false, true);
					} else if(tweets2.size() <= 0 && !failedOnce && !failedTwice){
						twitterAct.pageOffset2 = 1;
						getTweets(2, topic, true, false);
					}
	
				}
			
				
			} catch (TwitterException te) {
				System.out.println("Failed to search tweets: \"" + te.getMessage() + "\" for user " + user);
				twitterAct.errorFlag = true;
			}
		}
	}

	public void queryWord(String word, int user){

		if(user == 1){
			twitterAct.animTweet1.start();
			twitterAct.pageOffset1 = 1;
			getTweets(1, word, false, false);
			if (!twitterAct.errorFlag){
				twitterAct.removeTweets(1);
				/*if(twitterAct.tweetIndex1 >= twitterAct.MAX_TWEETS - twitterAct.NUM_TWEETS - (twitterAct.MAX_TWEETS % twitterAct.NUM_TWEETS)){
						twitterAct.tweetIndex1 = 0; 
					} else {
						twitterAct.tweetIndex1 += twitterAct.NUM_TWEETS;
					}*/

				createTweetZones(1);
			}
		} else {
			twitterAct.animTweet2.start();
			twitterAct.pageOffset2 = 1;
			getTweets(2, word, false, false);
			if (!twitterAct.errorFlag){
				twitterAct.removeTweets(2);
				/*if(twitterAct.tweetIndex2 >= twitterAct.MAX_TWEETS - twitterAct.NUM_TWEETS - (twitterAct.MAX_TWEETS % twitterAct.NUM_TWEETS)){
						twitterAct.tweetIndex2 = 0; 
					} else {
						twitterAct.tweetIndex2 += twitterAct.NUM_TWEETS;
					}*/

				createTweetZones(2);
			}
		}

		if(user == 1){
			twitterAct.animTweet1.stop();
			twitterAct.tweetWord1.setColour(Colours.unselectedZone);
		} else if (user == 2){
			twitterAct.animTweet2.stop();
			twitterAct.tweetWord2.setColour(Colours.unselectedZone);

		}

	}


	public void translateMiddleWord(){

		if(twitterAct.middleText.length() > 0 && !twitterAct.middleText.equalsIgnoreCase(" ")){
			twitterAct.animMiddleTweet.start();
			twitterAct.middleText = twitterAct.middleText.trim();
			String translatedText = "";
			try {
				if(!twitterAct.middleText.startsWith("http:") && !twitterAct.middleText.startsWith("https:")){
					translatedText = twitterAct.middleText.replaceAll(replaceRegex, " ");
				}
				if(twitterAct.langTranslate1 == twitterAct.langTranslate2){
					if(twitterAct.langTranslate1 == Language.ENGLISH){
						translatedText = Translate.execute(translatedText, twitterAct.langTranslate1, Language.FRENCH);
					} else {
						translatedText = Translate.execute(translatedText, twitterAct.langTranslate1, Language.ENGLISH);
					}
				} else if(twitterAct.lastUser == 1){
					translatedText = Translate.execute(translatedText, twitterAct.langTranslate1, twitterAct.langTranslate2);
				} else {
					translatedText = Translate.execute(translatedText, twitterAct.langTranslate2, twitterAct.langTranslate1);
				}

				twitterAct.setMiddleTweet(translatedText);

			} catch (Exception e) {
				System.out.println("Problem translating \"" + translatedText + "\". Languages: " + twitterAct.langTranslate1 + " and " + twitterAct.langTranslate2 + ".");
			}
			twitterAct.animMiddleTweet.stop();
			twitterAct.middleTweet.setColour(Colours.boundingBox);
		}
	}



	public void createTweetZones(int user){
		String text = "";
		int yLineSpace = 5;

		for(int i = 0; i < twitterAct.NUM_TWEETS; i++){

			if(!twitterAct.errorFlag ){
				if(user == 1){
					if((i + twitterAct.tweetIndex1) < tweets1.size()){
						text = "@" + tweets1.get(i + twitterAct.tweetIndex1).getUser().getName() + ":" + "\n" + tweets1.get(i + twitterAct.tweetIndex1).getText();
					} else {
						if(twitterAct.pageOffset1 < sketch.NUM_PAGES){
							twitterAct.pageOffset1++;
						} else {
							twitterAct.pageOffset1 = 1;
						}
						getTweets(1, twitterAct.topicExpanded1, false, false);
					}
				} else if (user == 2){	
					if((i + twitterAct.tweetIndex2) < tweets2.size()){
						text = "@" + tweets2.get(i + twitterAct.tweetIndex2).getUser().getName() + ":" + "\n" + tweets2.get(i + twitterAct.tweetIndex2).getText();
					} else {
						if(twitterAct.pageOffset2 < sketch.NUM_PAGES){
							twitterAct.pageOffset2++;
						} else {
							twitterAct.pageOffset2 = 1;
						}
						getTweets(2, twitterAct.topicExpanded2, false, false);
					}
				}
			} else {
				text = "Twitter Server Error. Try again later.";
			}

			String[] s = text.split(regex);


			if(user == 1){
				twitterAct.tweetZones1[i] = new TextZone[s.length];
				twitterAct.contentVisual1[i] = new Color[s.length];
				twitterAct.tweetZones1[i] = TextZone.createWordZones(twitterAct.x, (int)((sketch.getHeight()/2 + (i + 1.13)*twitterAct.textYOffset) + twitterAct.tweetTextSize/2), text, Colours.pFont, twitterAct.tweetTextSize, twitterAct.width, regex, yLineSpace, false);
			} else if (user == 2){
				twitterAct.tweetZones2[i] = new TextZone[s.length];
				twitterAct.contentVisual2[i] = new Color[s.length];
				twitterAct.tweetZones2[i] = TextZone.createWordZones(twitterAct.x + twitterAct.width, (int)((sketch.getHeight()/2 - (i + 1.13)*twitterAct.textYOffset) - twitterAct.tweetTextSize/2), text, Colours.pFont, twitterAct.tweetTextSize, twitterAct.width, regex, yLineSpace, true);
			}

			if(user == 1){

				hmap1[i] = new HashMap<Integer, ContentGetter>();

				for(int j = 0; j < twitterAct.tweetZones1[i].length; j++){
					String ss = twitterAct.tweetZones1[i][j].getText();


					if(ss.startsWith("#") || ss.startsWith("@")){
						final String ss2 = ss;
						twitterAct.tweetZones1[i][j] = new TextZone(twitterAct.tweetZones1[i][j].getX(), twitterAct.tweetZones1[i][j].getY(), twitterAct.tweetZones1[i][j].getWidth(), twitterAct.tweetZones1[i][j].getHeight(), Colours.pFont, twitterAct.tweetZones1[i][j].getText(), (float)twitterAct.tweetTextSize, "LEFT", "BOTTOM"){
							public void tapEvent(TapEvent e){
								if(tappable){

									twitterAct.started = true;
									queryWord(ss2, 1);
									e.setHandled(true);
								}
							}
						};
					} else if (ss.contains("http")){
						if(!ss.startsWith("http:")|| !ss.startsWith("https:")){
							ss = ss.replaceAll("[\'()!]", "");

						}
						if(ss.startsWith("http:")|| ss.startsWith("https:")){
							ContentGetter cg2 = new ContentGetter(sketch, 1, this, i, j);
							String str = ss.trim().replace("\"", "");
							str.replace("!", "");
							str.replace("'", "");

							cg2.setURL(str);

							cg2.start();
							hmap1[i].put(j, cg2);

							final int ii = i;
							final int jj = j;

							twitterAct.tweetZones1[i][j] = new TextZone(twitterAct.tweetZones1[i][j].getX(), twitterAct.tweetZones1[i][j].getY(), twitterAct.tweetZones1[i][j].getWidth(), twitterAct.tweetZones1[i][j].getHeight(), Colours.pFont, twitterAct.tweetZones1[i][j].getText(), (float)twitterAct.tweetTextSize, "LEFT", "BOTTOM"){
								public void tapEvent(TapEvent e){
									if(tappable){
										currentGetter1 = hmap1[ii].get(jj);
										hmap1[ii].get(jj).activated = true;
										sketch.client.pullToTop(twitterAct.middleTweet);
										hmap1[ii].get(jj).background.setActive(true);
										if(currentGetter1.videoFlag){
											twitterAct.videoController.hashMap[0] = ii;
											twitterAct.videoController.hashMap[1] = jj;
											twitterAct.videoController.setAgreeButtonVisible(2);
										} else {
											if(hmap1[ii].get(jj).img1 != null){
												if(hmap1[ii].get(jj).img1.getImageIndex() >= hmap1[ii].get(jj).img1.getImageArray().length-1){
													hmap1[ii].get(jj).img1.setImageIndex(0);

												}
												hmap1[ii].get(jj).img1.setActive(true);
												hmap1[ii].get(jj).imgFlag = true;
											}
											if(hmap1[ii].get(jj).tZone1 != null){
												hmap1[ii].get(jj).tZone1.setActive(true);
												hmap1[ii].get(jj).textFlag = true;
											}
										}
										e.setHandled(true);
									}
								}
								
								public void drawZone(){
									super.drawZone();
									if(twitterAct.contentVisual1[ii][jj] != null){
										
									
										twitterAct.sketch.textSize(sketch.textSize/2);
										twitterAct.sketch.fill(twitterAct.contentVisual1[ii][jj].getRed(), twitterAct.contentVisual1[ii][jj].getGreen(),twitterAct.contentVisual1[ii][jj].getBlue());
										String s = "";
										if(twitterAct.contentVisual1[ii][jj] == Colours.videoIndicator){
											s = "V";
										} else if(twitterAct.contentVisual1[ii][jj] == Colours.imageIndicator){
											s = "I";
										} else if(twitterAct.contentVisual1[ii][jj] == Colours.textIndicator){
											s = "T";
										}

										
										twitterAct.sketch.text(s, this.getX() + this.getWidth() - twitterAct.sketch.textWidth(s), this.getY() + this.getHeight()- sketch.textSize/4);

									}
								}


							};
						}
					} else {
						final String ss2 = ss;
						twitterAct.tweetZones1[i][j] = new TextZone(twitterAct.tweetZones1[i][j].getX(), twitterAct.tweetZones1[i][j].getY(), twitterAct.tweetZones1[i][j].getWidth(), twitterAct.tweetZones1[i][j].getHeight(), Colours.pFont, twitterAct.tweetZones1[i][j].getText(), (float)twitterAct.tweetTextSize, "LEFT", "BOTTOM"){
							public void tapEvent(TapEvent e){
								if(tappable){
									twitterAct.lastUser = 1;
									twitterAct.tweetWord1.setGestureEnabled("Tap", true);
									twitterAct.tweetWord1.setColour(Colours.unselectedZone);
									twitterAct.tweetWord1.setTextColour(Colours.zoneText);
									twitterAct.tweetWord2.setGestureEnabled("Tap", false);
									twitterAct.tweetWord2.setColour(Colours.fadedOutZone);
									twitterAct.tweetWord2.setTextColour(Colours.fadedText);
									twitterAct.selectedWord = ss2;
									if(twitterAct.started && twitterAct.lastHighlightZone != null){
										twitterAct.lastHighlightZone.setHighlightText(false);
									} else {
										twitterAct.started = true;
									}
									twitterAct.lastHighlightZone = this;
									this.setHighlightText(true);
									twitterAct.setMiddleTweet(ss2);
									e.setHandled(true);
								}
							}
						};
					}
					twitterAct.tweetZones1[i][j].setGestureEnabled("Tap", true);
					//tweetZones1[i][j].setGestureEnabled("Drag", true);
					twitterAct.tweetZones1[i][j].setDrawBorder(false);
					twitterAct.tweetZones1[i][j].setTextBoundByRect(false);
					twitterAct.tweetZones1[i][j].setHighlightTextColour(Colours.highlightTextColour);
					//tweetZones1[i][j].setTextOffsets(0, tweetZones1[i][j].getHeight()/2);
					twitterAct.client.addZone(twitterAct.tweetZones1[i][j]);

					if(ss.startsWith("#") || ss.startsWith("@")){
						twitterAct.tweetZones1[i][j].setTextColour(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue());
					}
					if(ss.startsWith("http:") || ss.startsWith("https:")){
						twitterAct.tweetZones1[i][j].setTextColour(Colours.fadedText);
					}
					//if ((ss.startsWith("http:") || ss.startsWith("https:")) && !hmap[i].get(j).content){
					//	twitterAct.tweetZones1[i][j].setTextColour(Colours.fadedText);
					//}
				}
			} else if(user == 2){

				hmap2[i] = new HashMap<Integer, ContentGetter>();

				for(int j = 0; j < twitterAct.tweetZones2[i].length; j++){
					String ss = twitterAct.tweetZones2[i][j].getText();


					if(ss.startsWith("#") || ss.startsWith("@")){
						final String ss2 = ss;
						twitterAct.tweetZones2[i][j] = new TextZone(twitterAct.tweetZones2[i][j].getX(), twitterAct.tweetZones2[i][j].getY(), twitterAct.tweetZones2[i][j].getWidth(), twitterAct.tweetZones2[i][j].getHeight(), Colours.pFont, twitterAct.tweetZones2[i][j].getText(), (float)twitterAct.tweetTextSize, "LEFT", "BOTTOM"){
							public void tapEvent(TapEvent e){
								if(tappable){

									twitterAct.started = true;
									queryWord(ss2, 2);
									e.setHandled(true);
								}
							}
						};
					} else if (ss.contains("http")){
						if(!ss.startsWith("http:")|| !ss.startsWith("https:")){
							ss = ss.replaceAll("[\'()!]", "");

						}
						if(ss.startsWith("http:")|| ss.startsWith("https:")){
							ContentGetter cg2 = new ContentGetter(sketch, 2, this, i, j);
							String str = ss.trim().replace("\"", "");
							str.replace("!", "");
							str.replace("'", "");

							cg2.setURL(str);

							cg2.start();
							hmap2[i].put(j, cg2);

							final int ii = i;
							final int jj = j;

							twitterAct.tweetZones2[i][j] = new TextZone(twitterAct.tweetZones2[i][j].getX(), twitterAct.tweetZones2[i][j].getY(), twitterAct.tweetZones2[i][j].getWidth(), twitterAct.tweetZones2[i][j].getHeight(), Colours.pFont, twitterAct.tweetZones2[i][j].getText(), (float)twitterAct.tweetTextSize, "LEFT", "BOTTOM"){
								public void tapEvent(TapEvent e){
									if(tappable){
										currentGetter2 = hmap2[ii].get(jj);
										hmap2[ii].get(jj).activated = true;
										sketch.client.pullToTop(twitterAct.middleTweet);
										hmap2[ii].get(jj).background.setActive(true);
										if(currentGetter2.videoFlag){
											twitterAct.videoController.hashMap[0] = ii;
											twitterAct.videoController.hashMap[1] = jj;
											twitterAct.videoController.setAgreeButtonVisible(1);

										} else {
											if(hmap2[ii].get(jj).img1 != null){
												if(hmap2[ii].get(jj).img1.getImageIndex() >= hmap2[ii].get(jj).img1.getImageArray().length-1){
													hmap2[ii].get(jj).img1.setImageIndex(0);

												}
												hmap2[ii].get(jj).img1.setActive(true);
												hmap2[ii].get(jj).imgFlag = true;
											}
											if(hmap2[ii].get(jj).tZone1 != null){
												hmap2[ii].get(jj).tZone1.setActive(true);
												hmap2[ii].get(jj).textFlag = true;
											}
										}
										e.setHandled(true);
									}
								}
								
								public void drawZone(){
									super.drawZone();
									if(twitterAct.contentVisual2[ii][jj] != null){
										
									
										twitterAct.sketch.textSize(sketch.textSize/2);
										twitterAct.sketch.fill(twitterAct.contentVisual2[ii][jj].getRed(), twitterAct.contentVisual2[ii][jj].getGreen(),twitterAct.contentVisual2[ii][jj].getBlue());

										String s = "";
										if(twitterAct.contentVisual2[ii][jj] == Colours.videoIndicator){
											s = "V";
										} else if(twitterAct.contentVisual2[ii][jj] == Colours.imageIndicator){
											s = "I";
										} else if(twitterAct.contentVisual2[ii][jj] == Colours.textIndicator){
											s = "T";
										}

										twitterAct.sketch.text(s, this.getX() + this.getWidth() - twitterAct.sketch.textWidth(s), this.getY() + this.getHeight()- sketch.textSize/4);

									}
								}

							};
						}
					} else {
						final String ss2 = ss;
						twitterAct.tweetZones2[i][j] = new TextZone(twitterAct.tweetZones2[i][j].getX(), twitterAct.tweetZones2[i][j].getY(), twitterAct.tweetZones2[i][j].getWidth(), twitterAct.tweetZones2[i][j].getHeight(), Colours.pFont, twitterAct.tweetZones2[i][j].getText(), (float)twitterAct.tweetTextSize, "LEFT", "BOTTOM"){
							public void tapEvent(TapEvent e){
								if(tappable){
									twitterAct.lastUser = 2;
									twitterAct.tweetWord2.setGestureEnabled("Tap", true);
									twitterAct.tweetWord2.setColour(Colours.unselectedZone);
									twitterAct.tweetWord2.setTextColour(Colours.zoneText);
									twitterAct.tweetWord1.setGestureEnabled("Tap", false);
									twitterAct.tweetWord1.setColour(Colours.fadedOutZone);
									twitterAct.tweetWord1.setTextColour(Colours.fadedText);
									twitterAct.selectedWord = ss2;
									if(twitterAct.started && twitterAct.lastHighlightZone != null){
										twitterAct.lastHighlightZone.setHighlightText(false);
									} else {
										twitterAct.started = true;
									}
									twitterAct.lastHighlightZone = this;
									this.setHighlightText(true);
									twitterAct.setMiddleTweet(ss2);
									e.setHandled(true);
								}
							}
						};
					}
					twitterAct.tweetZones2[i][j].setGestureEnabled("Tap", true);
					//tweetZones2[i][j].setGestureEnabled("Drag", true);
					twitterAct.tweetZones2[i][j].setDrawBorder(false);
					twitterAct.tweetZones2[i][j].rotate((float) Colours.PI);
					twitterAct.tweetZones2[i][j].setTextBoundByRect(false);
					twitterAct.tweetZones2[i][j].setHighlightTextColour(Colours.highlightTextColour);
					//tweetZones2[i][j].setTextOffsets(0, tweetZones2[i][j].getHeight()/2);
					twitterAct.client.addZone(twitterAct.tweetZones2[i][j]);

					if(ss.startsWith("#") || ss.startsWith("@")){
						twitterAct.tweetZones2[i][j].setTextColour(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue());
					}
					if(ss.startsWith("http:") || ss.startsWith("https:")){
						twitterAct.tweetZones2[i][j].setTextColour(Colours.fadedText);
					}
					//if ((ss.startsWith("http:") || ss.startsWith("https:")) && !hmap[i].get(j).content){
					//	twitterAct.tweetZones2[i][j].setTextColour(Colours.fadedText);
					//}
				}
			}

		}

		if(currentGetter1 != null){
			if(currentGetter1.imgFlag){
				sketch.client.pullToTop(currentGetter1.img1);
			}

			if(currentGetter1.textFlag){
				sketch.client.pullToTop(currentGetter1.tZone1);
			}
		}

		if(currentGetter2 != null){
			if(currentGetter2.imgFlag){
				sketch.client.pullToTop(currentGetter2.img1);
			}

			if(currentGetter2.textFlag){
				sketch.client.pullToTop(currentGetter2.tZone1);
			}
		}
	}

}