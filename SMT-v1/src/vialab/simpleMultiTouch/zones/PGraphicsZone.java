package vialab.simpleMultiTouch.zones;

import processing.core.PGraphics;
import processing.core.PImage;
import vialab.simpleMultiTouch.TouchClient;

public class PGraphicsZone extends RectZone {
	PGraphics g;
	float xOff = 0, yOff = 0;
	
	public PGraphicsZone(float x, float y, float width, float height, PGraphics g) {
		super(x,y,width, height);
		this.g = g;
	}
	
	public PGraphicsZone(float x, float y, float width, float height, float radius, PGraphics g) {
		super(x,y,width, height, radius);
		this.g = g;
	}
	
	
	public void setPGraphics(PGraphics g){
		this.g = g;
	}
	
	public PGraphics getPGraphics(){
		return g;
	}
	
	public void setYOffset(float yOff){
		this.yOff = yOff;
	}
	
	public void setXOffset(float xOff){
		this.xOff = xOff;
	}
	
	public float getXOffset(){
		return this.xOff;
	}
	
	public float getYOffset(){
		return this.yOff;
	}
	
	/**
	 * Student overrides this method
	 */
	public void setUpGraphicsContext(){
	}
	
	public void drawZone(){
		super.drawZone();
		setUpGraphicsContext();
		PImage img = g.get((int)xOff, (int)yOff, (int)width, (int)height);
		TouchClient.getPApplet().image(img, this.getX(), this.getY(), this.width, this.height);
	}
}