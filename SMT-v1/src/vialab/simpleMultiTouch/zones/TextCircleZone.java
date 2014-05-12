package vialab.simpleMultiTouch.zones;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import vialab.simpleMultiTouch.TouchClient;

public class TextCircleZone extends CircleZone {
	float size;
	String text;
	PFont font;
	PApplet applet = TouchClient.getPApplet();
	int xAlign = -1;
	int yAlign = -1;
	Color tColour;
	
	public TextCircleZone(float x, float y, float width, float height, PFont font, String text, float size, String xAlign, String yAlign) {
		super(x, y, width, height);
		initialize(xAlign, yAlign);
		this.size = size;
		this.text = text;
		this.font = font;
		tColour = new Color(0, 0, 0);
		
			
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
	public void setTextColour(int r, int g, int b){
		tColour = new Color(r, g, b);
	}
	public void drawZone(){
		super.drawZone();
		
		if(text != null){
			if (this.xAlign != -1 && this.yAlign != -1){
				applet.textAlign(this.xAlign, this.yAlign);
			} else if(this.xAlign != -1){
				applet.textAlign(this.xAlign);
			}
			
			applet.fill(tColour.getRed(), tColour.getGreen(), tColour.getBlue());
			applet.textFont(font, size);
			applet.text(text, getX(), getY(), getWidth(), getHeight());
		}
	}

}
