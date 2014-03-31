package TandemTable.sections.activities.headlines;

import gifAnimation.Gif;

import org.json.JSONException;
import org.json.JSONObject;

import TandemTable.Colours;
import TandemTable.Sketch;

import com.memetix.mst.translate.Translate;

import processing.core.PApplet;
import vialab.simpleMultiTouch.zones.RectZone;

public class HeadlineGetter extends Thread {
	Sketch sketch;
	HeadlinesActivity headAct;
	RectZone loading;
	String requestURL = "http://api.feedzilla.com/v1/articles/search.json?";

	public HeadlineGetter(Sketch sketch, HeadlinesActivity headAct){
		this.sketch = sketch;
		this.headAct = headAct;


		final Gif ajaxGIF = new Gif(sketch, sketch.loadGIF);
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
	}

	public void run(){
		loading.setActive(true);

		getResults(headAct.topicExpanded1, 1, false);
		getResults(headAct.topicExpanded2, 2, false);
		
		if(!headAct.canceled){
			headAct.createMiddleZone();
			headAct.activateHeadlines(1);
			headAct.activateHeadlines(2);
			headAct.setHeadlines(1);
			headAct.setHeadlines(2);

		}
		//if(this.headAct.canceled){
		//	this.headAct.removeZones();
		//}
		loading.setActive(false);
	}

	public void getResults(String query, int user, boolean failedOnce){
		if(!headAct.canceled){
			String request = requestURL;

			if(user == 1){
				request += "q=" + query + "&order=" + headAct.ORDER + "&culture_code=" + sketch.learner1.cultureCode + "&count=" + headAct.MAX_HEADLINES;
			} else if (user == 2){
				request += "q=" + query + "&order=" + headAct.ORDER + "&culture_code=" + sketch.learner2.cultureCode + "&count=" + headAct.MAX_HEADLINES;
			}

			headAct.response = sketch.loadStrings(request);
			
			if (headAct.response != null){

				JSONObject feedzilla;
				try {
					feedzilla = new JSONObject(PApplet.join(headAct.response, ""));



					if(user == 1){
						headAct.results1 = feedzilla.getJSONArray("articles");
						
						if(headAct.results1.length() <= 0 && !failedOnce ){
							getResults(headAct.topic1, 1, true);
							return;

						} else if(headAct.results1.length() <= 0 && failedOnce){
							headAct.errorFlag1 = true;
						}
					} else if (user == 2){
						headAct.results2 = feedzilla.getJSONArray("articles");

						if(headAct.results2.length() <= 0 && !failedOnce ){
							getResults(headAct.topic2, 2, true);
							return;

						} else if(headAct.results2.length() <= 0 && failedOnce){
							headAct.errorFlag2 = true;
						}
					}
				} catch (JSONException e) {
					if(user == 1){
						headAct.errorFlag1 = true;
					} else if (user == 2){
						headAct.errorFlag2 = true;
					}
					PApplet.println ("There was an error parsing the JSONObject.");
					e.printStackTrace();
				}


			} else {
				if(user == 1){
					headAct.errorFlag1 = true;
				} else if (user == 2){
					headAct.errorFlag2 = true;
				}

			}

			if(user == 1 && headAct.errorFlag1){
				failedSearch(1);
			} else if(user == 2 && headAct.errorFlag2){
				failedSearch(2);
			}
		}
	}

	public void failedSearch(int user){
		for(int i = 0; i < this.headAct.NUM_HEADLINES; i++){
			if(user == 1){
				headAct.headlines1[i].setText("Server Error. Try again later.");
				headAct.headlines1[i].setGestureEnabled("Tap", false);
				headAct.setWidthTitle("Server Error. Try again later.", this.headAct.headlines1[i], 1);
			} else if (user == 2){
				headAct.headlines2[i].setText("Server Error. Try again later.");
				headAct.headlines2[i].setGestureEnabled("Tap", false);
				headAct.setWidthTitle("Server Error. Try again later.", this.headAct.headlines2[i], 2);
			}

		}

		/*if(user == 1){
			headAct.moreNews1.setGestureEnabled("Tap", false);
		} else if(user == 2){
			headAct.moreNews2.setGestureEnabled("Tap", false);
		}*/
		//headAct.middleZone.setGestureEnabled("Tap", false);	
	}
	public void translateMiddleWord(){

		if(headAct.middleZone.middleText.length() > 0 && !headAct.middleZone.middleText.equalsIgnoreCase(" ")){
			headAct.middleZone.animMiddleZone.start();
			headAct.middleZone.middleText = headAct.middleZone.middleText.trim();
			String translatedText = "";
			try {
				if(!headAct.middleZone.middleText.startsWith("http:")){
					translatedText = headAct.middleZone.middleText.replaceAll(sketch.replaceRegex, " ");
				}
				
			
				if(headAct.langTranslate1 == headAct.langTranslate2){
					/*if(headAct.langTranslate1 == Language.ENGLISH){
						translatedText = Translate.execute(translatedText, headAct.langTranslate1, Language.FRENCH);
					} else {
						translatedText = Translate.execute(translatedText, headAct.langTranslate1, Language.ENGLISH);
					}*/
					
					translatedText = Translate.execute(translatedText, headAct.langTranslate1, sketch.transLanguage);
				} else if(headAct.lastUser == 1){
					translatedText = Translate.execute(translatedText, headAct.langTranslate1, headAct.langTranslate2);
				} else {
					translatedText = Translate.execute(translatedText, headAct.langTranslate2, headAct.langTranslate1);
				}

				headAct.middleZone.setMiddleText(translatedText);

			} catch (Exception e) {
				System.out.println("Problem translating \"" + translatedText + "\". Languages: " + headAct.langTranslate1 + " and " + headAct.langTranslate2 + ".");
			}
			headAct.middleZone.animMiddleZone.stop();
			headAct.middleZone.middleZone.setColour(Colours.boundingBox);
		}
	}
}

