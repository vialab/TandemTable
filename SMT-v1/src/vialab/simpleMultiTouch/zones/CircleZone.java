package vialab.simpleMultiTouch.zones;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import processing.core.PConstants;
import processing.core.PVector;
import vialab.simpleMultiTouch.TouchClient;

public class CircleZone extends Zone {
	Shape circle;
	Color fillColor = new Color(50, 200, 70);
	Color sColor = new Color(50, 200, 70);
	public boolean fill = true;
	public boolean stroke = true;
	
	public CircleZone(float x, float y, float width, float height){
		super();
		circle = new Ellipse2D.Float(x, y, width, height);
		createCircleZone(x, y, width, height);
	}
	

	public void createCircleZone(float xIn, float yIn, float wIn, float hIn){
		x = xIn;					// upper left corner x
		y = yIn;					// upper left corner y
		width = wIn;		// width
		height = hIn;	// height
		vx = vy = 0;				// velocities
		setTranslateAreaRadius((float) (Math.sqrt(wIn*wIn + hIn*hIn) / 5)); // Set the translate area radius for RNT
	}
	/**
	 * Tests to see if the x and y coordinates are in the zone. If the zone's
	 * matrix has been changed, reset its inverse matrix. This method is also
	 * used to place the x and y coordinate in the zone's matrix space and saves
	 * it to localX and localY.
	 * 
	 * 
	 * @param x
	 *            float - X-coordinate to test
	 * @param y
	 *            float - Y-coordinate to test
	 * @return boolean True if x and y is in the zone, false otherwise.
	 */
	public boolean contains(float x, float y) {
		if (this.changed) {
			this.getInverse().reset();
			this.getInverse().apply(this.matrix);
			this.getInverse().invert();
			this.changed = false;
		}
		PVector world = new PVector();
		PVector mouse = new PVector(x, y);
		this.getInverse().mult(mouse, world);

		//if ((world.x > this.getX()) && (world.x < this.getX() + this.width)
		//		&& (world.y > this.getY()) && (world.y < this.getY() + this.height)) {
		if(circle.contains(world.x, world.y)){
			this.setLocalX(world.x);
			this.setLocalY(world.y);
			return true;
		}
		return false;

	}
	
	public void setFillColor(int r, int g, int b){
		fillColor = new Color(r, g, b);
	}
	
	public void setFillColor(Color c){
		fillColor = c;
	}
	
	public Color getFillColor(){
		return fillColor;
	}
	public void setStrokeColor(int r, int g, int b){
		sColor = new Color(r, g, b);
	}
	
	public void setStrokeColor(Color c){
		sColor = c;
	}
	
	public void setFill(boolean fill){
		this.fill = fill;
	}
	
	public void setStroke(boolean stroke){
		this.stroke = stroke;
	}
	
	public void drawZone(){
		
		if(stroke){
			TouchClient.getPApplet().strokeWeight(1);
			TouchClient.getPApplet().stroke(sColor.getRed(), sColor.getGreen(), sColor.getBlue());
		} else {
			TouchClient.getPApplet().noStroke();
		}
		
		if(fill){
			TouchClient.getPApplet().fill(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue());
		} else {
			TouchClient.getPApplet().noFill();
		}
		TouchClient.getPApplet().ellipseMode(PConstants.CORNER);
		TouchClient.getPApplet().ellipse(getX(), getY(), getWidth(), getHeight());
		TouchClient.getPApplet().ellipseMode(PConstants.CENTER);

	}


}
