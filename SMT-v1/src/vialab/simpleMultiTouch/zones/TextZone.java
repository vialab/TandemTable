package vialab.simpleMultiTouch.zones;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import vialab.simpleMultiTouch.TouchClient;

public class TextZone extends RectZone{
	float size;
	String text;
	PFont font;
	PApplet applet = TouchClient.getPApplet();
	int xAlign = -1;
	int yAlign = -1;
	public float yOffset = 0;
	public float xOffset = 0;
	Color tColour;
	boolean rounded = false;
	boolean withinZone = true;
	boolean highlighted = false;
	Color highlightColour = new Color(233, 255, 31, 255);

	/**
	 * 
	 * @param x - x
	 * @param y
	 * @param width
	 * @param height
	 * @param font
	 * @param text
	 * @param size
	 * @param xAlign - Horizontal alignment, either LEFT, CENTER, or RIGHT
	 * @param yAlign - Vertical alignment, either TOP, BOTTOM, CENTER, or BASELINE
	 */
	public TextZone(float x, float y, float width, float height, PFont font, String text, float size, String xAlign, String yAlign) {
		super(x, y, width, height);
		initialize(xAlign, yAlign);
		this.size = size;
		this.text = text;
		this.font = font;
		tColour = new Color(0, 0, 0);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param font
	 * @param r
	 * @param text
	 * @param size
	 * @param xAlign - Horizontal alignment, either LEFT, CENTER, or RIGHT
	 * @param yAlign - Vertical alignment, either TOP, BOTTOM, CENTER, or BASELINE
	 */
	public TextZone(float x, float y, float width, float height, float r, PFont font, String text, float size, String xAlign, String yAlign) {
		super(x, y, width, height, r);
		initialize(xAlign, yAlign);
		this.size = size;
		this.text = text;
		this.font = font;
		tColour = new Color(0, 0, 0);
		rounded = true;

	}

	public void setTextOffsets(float xOffset, float yOffset){
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void initialize(String xAlign, String yAlign){
		if(xAlign.equalsIgnoreCase("LEFT")){
			this.xAlign = PConstants.LEFT;
		} else if(xAlign.equalsIgnoreCase("CENTER")){
			this.xAlign = PConstants.CENTER;
		} else if(xAlign.equalsIgnoreCase("RIGHT")){
			this.xAlign = PConstants.RIGHT;
		} else {
			System.err.println("TextZone was given the wrong horizontal alignment string - " + xAlign);
			System.err.println("Possible string values are 'LEFT', 'CENTER', or 'RIGHT'.");
		}

		if(yAlign.equalsIgnoreCase("TOP")){
			this.yAlign = PConstants.TOP;
		} else if(yAlign.equalsIgnoreCase("BOTTOM")){
			this.yAlign = PConstants.BOTTOM;
		} else if(yAlign.equalsIgnoreCase("CENTER")){
			this.yAlign = PConstants.CENTER;
		} else if(yAlign.equalsIgnoreCase("BASELINE")){
			this.yAlign = PConstants.BASELINE;
		} else {
			System.err.println("TextZone was given the wrong vertical alignment string - " + yAlign);
			System.err.println("Possible string values are 'TOP', 'BOTTOM', 'CENTER', or 'BASELINE'.");
		}	
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return this.text;
	}

	public int getXAlign(){
		return xAlign;
	}
	public int getYAlign(){
		return yAlign;
	}
	public void setTextColour(int r, int g, int b, int a){
		tColour = new Color(r, g, b, a);
	}
	
	public void setTextColour(int r, int g, int b){
		tColour = new Color(r, g, b);
	}	

	public void setTextColour(Color c){
		tColour = c;
	}

	public void setTextBoundByRect(boolean flag){
		this.withinZone = flag;
	}

	public PFont getPFont(){
		return font;
	}

	public float getTextSize(){
		return size;
	}



	public Color getTextColour(){
		return tColour;
	}



	public void setHighlightText(boolean flag){
		highlighted = flag;
	}

	public void setHighlightTextColour(Color c){
		highlightColour = c;
	}

	public void setHighlightTextColour(int r, int g, int b){
		highlightColour = new Color(r, g, b);
	}
	public void setHighlightTextColour(int r, int g, int b, int a){
		highlightColour = new Color(r, g, b, a);
	}
	public void drawZone(){
		super.drawZone();

		if(highlighted){
			applet.noStroke();
			applet.fill(highlightColour.getRed(), highlightColour.getGreen(), highlightColour.getBlue());
			applet.rect(getX() + xOffset, getY() + yOffset, getWidth() - (2*xOffset), getHeight() - (2*yOffset));
		}

		if (this.xAlign != -1 && this.yAlign != -1){
			applet.textAlign(this.xAlign, this.yAlign);
		} else if(this.xAlign != -1){
			applet.textAlign(this.xAlign);
		}
		applet.fill(tColour.getRed(), tColour.getGreen(), tColour.getBlue(), tColour.getAlpha());
		
		if(text != null){
			if(font != null){
				applet.textFont(font, size);
			}


			if(withinZone){
				applet.text(text, getX() + xOffset, getY() + yOffset, getWidth() - (2*xOffset), getHeight() - (2*yOffset));
			} else {
				applet.text(text, getX() + xOffset, getY() + getHeight() + yOffset);
			}
		}
	}

	public static TextZone[] createWordZones(float x, float y, String text, PFont pfont, float textSize, float totalWidth, String regex, float yLineSpace, boolean upsideDown){

		float leftSpacing = TouchClient.getPApplet().getWidth()/120;
		float topSpacing = TouchClient.getPApplet().getHeight()/90;
		float xx = 0;
		float yy = 0;
		String[] s = text.split(regex);
		float xOff = 0, yOff = 0;

		TextZone[] zones = new TextZone[s.length];
		TouchClient.getPApplet().textFont(pfont, textSize);
		TouchClient.getPApplet().textAlign(PConstants.LEFT, PConstants.BOTTOM);
		float spaceWidth = TouchClient.getPApplet().textWidth(' ');

		for(int j = 0; j < s.length; j++){
			char[] c = s[j].toCharArray();
			float textWidth = TouchClient.getPApplet().textWidth(c, 0, c.length);
			float zoneWidth = (textWidth + spaceWidth);

			if(xOff + textWidth + leftSpacing > totalWidth){
				yOff += textSize + yLineSpace;
				xOff = 0;
			}

			if(upsideDown){
				xx = -leftSpacing - xOff - zoneWidth;
				yy = (- textSize - topSpacing - yOff);
			} else {
				xx = leftSpacing + xOff;
				yy = topSpacing + yOff;

			}

			zones[j] = new TextZone(x + xx, y + yy, zoneWidth, textSize, pfont, s[j], textSize, "LEFT", "BOTTOM");

			xOff += zoneWidth;
		}
		return zones;


	}
	
	public static TextZone[] createWordZones(float x, float y, String text, PFont pfont, float textSize, float totalWidth, String regex, float yLineSpace, boolean upsideDown, float xSpace){

		float leftSpacing = TouchClient.getPApplet().getWidth()/120;
		float topSpacing = TouchClient.getPApplet().getHeight()/90;
		float xx = 0;
		float yy = 0;
		String[] s = text.split(regex);
		float xOff = 0, yOff = 0;

		TextZone[] zones = new TextZone[s.length];
		TouchClient.getPApplet().textFont(pfont, textSize);
		TouchClient.getPApplet().textAlign(PConstants.LEFT, PConstants.BOTTOM);
		float spaceWidth = TouchClient.getPApplet().textWidth(' ');

		for(int j = 0; j < s.length; j++){
			char[] c = s[j].toCharArray();
			float textWidth = TouchClient.getPApplet().textWidth(c, 0, c.length);
			float zoneWidth = (textWidth + spaceWidth);

			if(xOff + textWidth + leftSpacing + xSpace*j > totalWidth){
				yOff += textSize + yLineSpace;
				xOff = 0;
			}

			if(upsideDown){
				xx = -leftSpacing - xOff - zoneWidth - xSpace*j;
				yy = (- textSize - topSpacing - yOff);
			} else {
				xx = leftSpacing + xOff + xSpace*j;
				yy = topSpacing + yOff;

			}

			zones[j] = new TextZone(x + xx, y + yy, zoneWidth, textSize, pfont, s[j], textSize, "LEFT", "BOTTOM");

			xOff += zoneWidth;
		}
		return zones;


	}


}
