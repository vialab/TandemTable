package TandemTable.sections;

import TandemTable.Colours;
import TandemTable.Sketch;
import TandemTable.sections.activities.headlines.HeadlinesActivity;
import TandemTable.sections.activities.twitter.ContentGetter;
import processing.core.PConstants;
import processing.core.PGraphics;
import vialab.simpleMultiTouch.events.DragEvent;
import vialab.simpleMultiTouch.zones.PGraphicsZone;

public class ArticleContainer extends PGraphicsZone {
	float widthBody;
	String regex = "[ \t\n\f\r]+";
	int textYSpacing;
	int textSize;
	HeadlinesActivity headAct;
	ContentGetter twitCG;
	boolean headline = false, twit = false;
	int xMargin, yMargin;
	
	int graphicsHeight;
	Sketch sketch;
	int user;
	
	public ArticleContainer(Sketch sketch, float x, float y, float width, float height, float radius, PGraphics pg, 
			int textYSpacing, int textSize, int xMargin, int yMargin, int graphicsHeight) {
		super(x, y, width, height, radius, pg);
		this.sketch = sketch;
		widthBody = width;
		this.textYSpacing = textYSpacing;
		this.textSize = textSize;
		this.xMargin = xMargin;
		this.yMargin = yMargin;
		this.graphicsHeight =  graphicsHeight;
	}
	
	public void setHeadActivity(HeadlinesActivity headAct, int user) {
		this.headAct = headAct;
		headline = true;
		twit = false;
		this.user = user;
	}
	
	public void setTwitActivity(ContentGetter twitCG, int user) {
		this.twitCG = twitCG;
		headline = false;
		twit = true;
		this.user = user;
	}
	
	public void drawBackBuffer(PGraphics backBuffer, String t, int xAlign, int yAlign, float y, float x, float width){
		String[] str = t.split(regex);
		int index = -1;
		// Width of line of text
		float xw = x;
		//int wordIndex1 = 1;
		String titleLine = "";

		for(int k = 0; k < str.length; k++){
			if(str[k].equalsIgnoreCase("\n") || (xw + backBuffer.textWidth(str[k]) + backBuffer.textWidth(' ')) > width){
				index = k;
				break;
			}
			titleLine += str[k] + " ";
			xw += backBuffer.textWidth(str[k]) + backBuffer.textWidth(' ');
		}

		if(xAlign == PConstants.CENTER){
			if(index != -1){
				//x = 0;//x = widthBody/2 - findWidth(str, 0, index+1)/2 + backBuffer.textWidth(' ')/2 * (str.length - 2);
				x = widthBody/2 - backBuffer.textWidth(titleLine)/2; 
			} else {
				x = widthBody/2 - backBuffer.textWidth(t)/2;
			}
		} /*else if (xAlign == PConstants.RIGHT){
			if(index != -1){
				//x = widthBody/2 + findWidth(str, 0, index+1)/2;
			} else {
				x = widthBody/2 + backBuffer.textWidth(t)/2;
			}
		}*/

		if(yAlign == PConstants.CENTER && index != -1){
			y += backBuffer.textAscent()/2;//textYSpacing;//textSize  + backBuffer.textAscent() / 2;
		} else if(yAlign == PConstants.TOP){
			y += 0;//;textSize  + backBuffer.textAscent();
		}

		for(int k = 0; k < str.length; k++){
			if(str[k].equalsIgnoreCase("\n") || (x + backBuffer.textWidth(str[k]) + backBuffer.textWidth(' ')) > width){
				if(yAlign == PConstants.CENTER){
					y += textYSpacing;//textSize  + backBuffer.textAscent() / 2;
				} else if(yAlign == PConstants.TOP){
					y += textSize  + backBuffer.textAscent();
				}


				if(xAlign == PConstants.CENTER){
					float tw = findWidth(str, k, str.length, backBuffer);
					x = widthBody/2 - tw/2;
				
				/*} else if (xAlign == PConstants.RIGHT){
					float tw = findWidth(str, k, str.length, backBuffer);
					x = widthBody/2 + tw/2;*/
				} else {
					x = xMargin;
				}
			} 

			if(headline) {
				if(user == 1) {
					backBuffer.fill(headAct.red1, headAct.green1, headAct.blue1);
					backBuffer.rect(x, yMargin + y, backBuffer.textWidth(str[k]), textSize);
					headAct.wordMap1.put(backBuffer.color(headAct.red1, headAct.green1, headAct.blue1), str[k]);
					headAct.wordX1.put(backBuffer.color(headAct.red1, headAct.green1, headAct.blue1), new Float[]{x, yMargin + y, backBuffer.textWidth(str[k]), (float) textSize});
		
					x += backBuffer.textWidth(str[k]) + backBuffer.textWidth(' ');
		
					if(headAct.red1 < 255){
						headAct.red1++;
					} else if(headAct.green1 < 255){
						headAct.green1++;
					} else if(headAct.blue1 < 255){
						headAct.blue1++;
					}
				} else if(user == 2){
					backBuffer.fill(headAct.red2, headAct.green2, headAct.blue2);
					backBuffer.rect(x, yMargin + y, sketch.textWidth(str[k]), textSize);
					headAct.wordMap2.put(sketch.color(headAct.red2, headAct.green2, headAct.blue2), str[k]);
					headAct.wordX2.put(sketch.color(headAct.red2, headAct.green2, headAct.blue2), new Float[]{x, yMargin + y, sketch.textWidth(str[k]), (float) textSize});

					x += sketch.textWidth(str[k]) + sketch.textWidth(' ');

					if(headAct.red2 < 255){
						headAct.red2++;
					} else if(headAct.green2 < 255){
						headAct.green2++;
					} else if(headAct.blue2 < 255){
						headAct.blue2++;
					}
				}
			} else if(twit) {
				backBuffer.fill(twitCG.red, twitCG.green, twitCG.blue);
				backBuffer.rect(x, yMargin + y, backBuffer.textWidth(str[k]), textSize);

				twitCG.wordMap1.put(backBuffer.color(twitCG.red, twitCG.green, twitCG.blue), str[k]);
				twitCG.wordX1.put(backBuffer.color(twitCG.red, twitCG.green, twitCG.blue), new Float[]{x, yMargin + y, backBuffer.textWidth(str[k]), (float) textSize});

				x += backBuffer.textWidth(str[k]) + backBuffer.textWidth(' ');

				if(twitCG.red < 255){
					twitCG.red++;
				} else if(twitCG.green < 255){
					twitCG.green++;
				} else if(twitCG.blue < 255){
					twitCG.blue++;
				}
				
			}
		}

	}
	
	public void setBody(PGraphics pg, String t, float x, float y, int w, int h){
		String[] str = t.split(regex);
		float xx = x;

		for(int k = 0; k < str.length; k++){
			if((xx + pg.textWidth(str[k])+pg.textWidth(' ') > w)){
				y += textSize + pg.textAscent();
				xx = x;
			}
			pg.text(str[k], xx, y);
			xx += pg.textWidth(str[k])+pg.textWidth(' ');

		}
	}
	
	// Draw scrollbar and triangles
	public void postDraw(){
		if(graphicsHeight > height) {
			float triWidth = sketch.getWidth()/100;
			float xw = this.getX()+ this.getWidth() - triWidth/2;

			///////////////////////////////////////////////////
			//Draw bottom triangle
			float h = this.getY()+ this.getHeight() - triWidth/2;
			
			if(getYOffset() + 10 <= graphicsHeight - height){
				sketch.fill(Colours.scrollTriColor.getRed(), Colours.scrollTriColor.getGreen(),Colours.scrollTriColor.getBlue());
			} else {
				sketch.fill(Colours.scrollTriFaded.getRed(), Colours.scrollTriFaded.getGreen(),Colours.scrollTriFaded.getBlue());
			}
			
			sketch.triangle(xw-triWidth, h-triWidth, xw-triWidth/2, h, xw, h-triWidth);	
			//////////////////////////////////////////////////
			
			/////////////////////////////////////////////////
			// Draw bottom triangle
			h = this.getY() + triWidth/2;
			
			if(getYOffset() - 10 >= 0){
				sketch.fill(Colours.scrollTriColor.getRed(), Colours.scrollTriColor.getGreen(),Colours.scrollTriColor.getBlue());
			} else {
				sketch.fill(Colours.scrollTriFaded.getRed(), Colours.scrollTriFaded.getGreen(),Colours.scrollTriFaded.getBlue());
			}
			
			sketch.triangle(xw-triWidth, h + triWidth, xw-triWidth/2, h, xw, h + triWidth);
			////////////////////////////////////////////////////
			
			// Draw scroll bar
			
			sketch.fill(Colours.scrollBar.getRed(), Colours.scrollBar.getGreen(),Colours.scrollBar.getBlue());
			sketch.rect(xw - triWidth, (float) (getY() + triWidth*1.5), triWidth, getHeight() - triWidth*3);
		}
	}

	// Scrolling the news article
	public void dragEvent(DragEvent e){
		if(isDraggable()){
			// -1 for iPad scrolling
			int direction = -1;
			
			int dist = (int) (direction * e.getYDistance()/10 + this.getYOffset());

			if(dist <= graphicsHeight - height && dist >= 0){
				this.setYOffset(dist);
			}
			e.setHandled(true);
		}
	}
	public float findWidth(String[] str, int start, int end, PGraphics pg){
		float tw = 0;
		String wordNSpace = "";
		for(int q = start; q < end; q++){
			wordNSpace = str[q] + " ";
			tw += pg.textWidth(wordNSpace);
		}
		return tw;
	}
}
