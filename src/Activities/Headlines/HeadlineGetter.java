package activities.headlines;

import gifAnimation.Gif;
import main.Colours;
import main.MainSketch;

import org.json.JSONException;
import org.json.JSONObject;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import processing.core.PApplet;
import vialab.simpleMultiTouch.RectZone;

public class HeadlineGetter extends Thread {
	MainSketch sketch;
	HeadlinesActivity headAct;
	RectZone loading;
	String replaceRegex = "[^a-zA-Z_0-9_'_Á_á_À _Â_à_Â_â_Ä_ä_Ã_ã_Å_å_Ç_ç_É_é_È_è_Ê_ê_Ë_ë_Í_í_Ì_ì_Î_î_Ï_ï_Ñ_ñ_Ó_ó_Ò_ò_Ô_ô_Ö_ö_Õ_õ_Ú_ú_Ù_ù_Û_û_Ü_ü_Ý_ý_ÿ]";
	String requestURL = "http://api.feedzilla.com/v1/articles/search.json?";

	public HeadlineGetter(MainSketch sketch, HeadlinesActivity headAct){
		this.sketch = sketch;
		this.headAct = headAct;


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
	}

	public void run(){
		loading.setActive(true);

		getResults(this.headAct.topicExpanded1, 1, false);
		getResults(this.headAct.topicExpanded2, 2, false);
		if(!this.headAct.canceled){
			this.headAct.createMiddleTweet();
			this.headAct.createHeadlineZones1();
			this.headAct.createHeadlineZones2();
			this.headAct.setHeadlines(1);
			this.headAct.setHeadlines(2);

		}
		//if(this.headAct.canceled){
		//	this.headAct.removeZones();
		//}
		loading.setActive(false);
	}

	public void getResults(String query, int user, boolean failedOnce){
		if(!this.headAct.canceled){
			String request = requestURL;

			if(user == 1){
				request += "q=" + query + "&order=" + this.headAct.ORDER + "&culture_code=" + this.headAct.culture1 + "&count=" + this.headAct.MAX_HEADLINES;
			} else if (user == 2){
				request += "q=" + query + "&order=" + this.headAct.ORDER + "&culture_code=" + this.headAct.culture2 + "&count=" + this.headAct.MAX_HEADLINES;
			}

			this.headAct.response = sketch.loadStrings(request);

			if (this.headAct.response != null){

				JSONObject feedzilla;
				try {
					feedzilla = new JSONObject(PApplet.join(this.headAct.response, ""));



					if(user == 1){
						this.headAct.results1 = feedzilla.getJSONArray("articles");
						if(this.headAct.results1.length() <= 0 && !failedOnce ){
							getResults(this.headAct.topic1, 1, true);
							return;

						} else if(this.headAct.results1.length() <= 0 && failedOnce){
							this.headAct.errorFlag1 = true;
						}
					} else if (user == 2){
						this.headAct.results2 = feedzilla.getJSONArray("articles");

						if(this.headAct.results2.length() <= 0 && !failedOnce ){
							getResults(this.headAct.topic2, 2, true);
							return;

						} else if(this.headAct.results2.length() <= 0 && failedOnce){
							this.headAct.errorFlag2 = true;
						}
					}
				} catch (JSONException e) {
					if(user == 1){
						this.headAct.errorFlag1 = true;
					} else if (user == 2){
						this.headAct.errorFlag2 = true;
					}
					PApplet.println ("There was an error parsing the JSONObject.");
					e.printStackTrace();
				}


			} else {
				if(user == 1){
					this.headAct.errorFlag1 = true;
				} else if (user == 2){
					this.headAct.errorFlag2 = true;
				}

			}

			if(user == 1 && this.headAct.errorFlag1){
				failedSearch(1);
			} else if(user == 2 && this.headAct.errorFlag2){
				failedSearch(2);
			}
		}
	}

	public void failedSearch(int user){
		for(int i = 0; i < this.headAct.NUM_HEADLINES; i++){
			if(user == 1){
				this.headAct.headlines1[i].setText("Server Error. Try again later.");
				this.headAct.headlines1[i].setGestureEnabled("Tap", false);
				this.headAct.setWidthTitle("Server Error. Try again later.", this.headAct.headlines1[i], 1);
			} else if (user == 2){
				this.headAct.headlines2[i].setText("Server Error. Try again later.");
				this.headAct.headlines2[i].setGestureEnabled("Tap", false);
				this.headAct.setWidthTitle("Server Error. Try again later.", this.headAct.headlines2[i], 2);
			}

		}

		if(user == 1){
			headAct.moreNews1.setGestureEnabled("Tap", false);
		} else if(user == 2){
			headAct.moreNews2.setGestureEnabled("Tap", false);
		}
		//headAct.middleZone.setGestureEnabled("Tap", false);	
	}
	public void translateMiddleWord(){

		if(headAct.middleText.length() > 0 && !headAct.middleText.equalsIgnoreCase(" ")){
			headAct.animMiddleZone.start();
			headAct.middleText = headAct.middleText.trim();
			String translatedText = "";
			try {
				if(!headAct.middleText.startsWith("http:")){
					translatedText = headAct.middleText.replaceAll(replaceRegex, " ");
				}
				
				if(headAct.langTranslate1 == headAct.langTranslate2){
					if(headAct.langTranslate1 == Language.ENGLISH){
						translatedText = Translate.execute(translatedText, headAct.langTranslate1, Language.FRENCH);
					} else {
						translatedText = Translate.execute(translatedText, headAct.langTranslate1, Language.ENGLISH);
					}
				} else if(headAct.lastUser == 1){
					translatedText = Translate.execute(translatedText, headAct.langTranslate1, headAct.langTranslate2);
				} else {
					translatedText = Translate.execute(translatedText, headAct.langTranslate2, headAct.langTranslate1);
				}

				headAct.setMiddleText(translatedText);

			} catch (Exception e) {
				System.out.println("Problem translating \"" + translatedText + "\". Languages: " + headAct.langTranslate1 + " and " + headAct.langTranslate2 + ".");
			}
			headAct.animMiddleZone.stop();
			headAct.middleZone.setColour(Colours.boundingBox);
		}
	}
}

