/*
  Simple Multitouch Library
  Copyright 2011
  Erik Paluka, Christopher Collins - University of Ontario Institute of Technology
  Mark Hancock - University of Waterloo

  Parts of this library are based on:
  TUIOZones http://jlyst.com/tz/

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public
  License Version 3 as published by the Free Software Foundation.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */
package vialab.simpleMultiTouch.zones;

import java.awt.Color;

import vialab.simpleMultiTouch.TouchClient;

/** 
 * This is the basic rectangular zone class.<P>
 * 
 * University of Ontario Institute of Technology.
 * Summer Research Assistant with Dr. Christopher Collins (Summer 2011) collaborating with Dr. Mark Hancock.<P>
 * 
 * @author  Erik Paluka 
 * @date  Summer, 2011
 * @version 1.0
 */

public class RectZone extends Zone{
	boolean rounded = false;
	boolean fill = false;
	boolean stroke = false;
	boolean shadow = false;
	Color strokeColour, shadowColour;
	float r;
	float strokeWeight = 1;
	
	float prevX = 0;
	float prevY = 0;
	float xShadow = 5;
	float yShadow = 5;
	float wShadow = 0;
	float hShadow = 0;
	
	/** Sets if the zone should have RectZone of a 
	 * certain colour drawn overtop of its contents.
	 */
	boolean colFilter = false;
	Color filterColor = new Color(110, 110, 110, 200);
	
	
	
	/**
	 * RectZone constructor, creates a rectangular zone by calling "createRectZone()"
	 * 
	 * @param x int - X-coordinate of the upper left corner of the zone
	 * @param y int - Y-coordinate of the upper left corner of the zone
	 * @param width int - Width of the zone
	 * @param height int - Height of the zone
	 */
	public RectZone(float x, float y, float width, float height){
		super();
		createRectZone(x, y, width, height);
	}

	public RectZone(float x, float y, float width, float height, float r){
		super();
		rounded = true;
		this.r = r;
		createRectZone(x, y, width, height);
	}

	/**
	 * Sets the x, y, width, height, and translate area radius (for RNT gesture) of the new rectangular zone.
	 * 
	 * @param xIn int - X-coordinate of the upper left corner of the zone
	 * @param yIn int - Y-coordinate of the upper left corner of the zone
	 * @param wIn int - Width of the zone
	 * @param hIn int - Height of the zone
	 */
	public void createRectZone(float xIn, float yIn, float wIn, float hIn){
		shadowColour = new Color(155, 155, 155);
		strokeColour = new Color(0, 0, 0);
		x = xIn;					// upper left corner x
		y = yIn;					// upper left corner y
		width = wIn;		// width
		height = hIn;	// height
		vx = vy = 0;				// velocities
		setTranslateAreaRadius((float) (Math.sqrt(wIn*wIn + hIn*hIn) / 5)); // Set the translate area radius for RNT
	}
	

	/**
	 * Sets the filter color
	 * Sets if the zone should have RectZone of a 
	 * certain colour drawn overtop of its contents.
	 *
	 * @param c
	 */
	public void setFilterColor(Color c){
		filterColor = c;
	}
	
	/** 
	 * Gets the filter color
	 */
	public Color getFilterColor(){
		return filterColor;
	}


	
	/**
	 * Set the filter color of the overlay
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void setFilterColor(int r, int g, int b, int a){
		filterColor = new Color(r, g, b, a);
	}
	
	/**
	 * Sets the filter color
	 * @param flag
	 */
	public void setFilterColFlag(boolean flag){
		colFilter = flag;
	}
	
	/**
	 * Get the filter color flag
	 * @return
	 */
	public boolean getFilterColFlag(){
		return colFilter;
	}

	public void setShadow(boolean flag){
		shadow = flag;
	}
	public void setShadowColour(int r, int g, int b){
		shadowColour = new Color(r, g, b);
	}
	
	public void setShadowColour(int r, int g, int b, int a){
		shadowColour = new Color(r, g, b, a);
	}


	public void setShadowColour(Color c){
		shadowColour = c;
	}


	public Color getShadowColour(){
		return shadowColour;
	}
	public void setColour(int r, int g, int b){
		fill = true;
		super.setColour(new Color(r, g, b));
	}
	
	public void setColour(int r, int g, int b, int a){
		fill = true;
		super.setColour(new Color(r, g, b, a));
	}
	
	

	public void setColour(Color c){
		fill = true;
		super.setColour(c);
	}


	public void setFill(boolean fill){
		this.fill = fill;
	}

	public void setStroke(boolean stroke){
		this.stroke = stroke;
	}

	public void setStrokeColour(int r, int g, int b){
		this.strokeColour = new Color(r, g, b);
	}
	
	public void setStrokeColour(int r, int g, int b, int a){
		this.strokeColour = new Color(r, g, b, a);
	}

	public void setStrokeWeight(float strokeWeight){
		this.strokeWeight = strokeWeight;
	}

	public void setShadowX(int xShadow){
		this.xShadow = xShadow;
	}

	public void setShadowY(int yShadow){
		this.yShadow = yShadow;
	}
	
	public void setShadowW(int wShadow){
		this.wShadow = wShadow;
	}

	public void setShadowH(int hShadow){
		this.hShadow = hShadow;
	}

	public void drawZone(){
		super.drawZone();
		
		if(shadow){
			TouchClient.getPApplet().stroke(shadowColour.getRed(), shadowColour.getGreen(), shadowColour.getBlue(), shadowColour.getAlpha());
			TouchClient.getPApplet().strokeWeight(1);
			applet.fill(shadowColour.getRed(), shadowColour.getGreen(), shadowColour.getBlue(), shadowColour.getAlpha());
			if(rounded){
				roundedRect(x + xShadow, y + yShadow, width + wShadow, height + hShadow, r);
			} else {
				applet.rect(x + xShadow, y + yShadow, width + wShadow, height + hShadow);
			}
		}


		if(stroke){
			TouchClient.getPApplet().stroke(strokeColour.getRed(), strokeColour.getGreen(), strokeColour.getBlue(), strokeColour.getAlpha());
			TouchClient.getPApplet().strokeWeight(strokeWeight);

		} else {
			TouchClient.getPApplet().noStroke();
		}



		if(rounded){

			/*TouchClient.getPApplet().fill(colour.getRed(), colour.getGreen(), colour.getBlue());
			TouchClient.getPApplet().noStroke();
			TouchClient.getPApplet().rectMode(PConstants.CORNER);
			TouchClient.getPApplet().ellipseMode(PConstants.CENTER);
			TouchClient.getPApplet().rect(x2, y2, width2, height2);
			TouchClient.getPApplet().arc(x2, y2, r, r, PApplet.radians((float) 180.0), PApplet.radians((float) 270.0));
			TouchClient.getPApplet().arc(ax, y2, r,r, PApplet.radians((float) 270.0), PApplet.radians((float) 360.0));
			TouchClient.getPApplet().arc(x2, ay, r,r, PApplet.radians((float) 90.0), PApplet.radians((float) 180.0));
			TouchClient.getPApplet().arc(ax, ay, r,r, PApplet.radians((float) 0.0), PApplet.radians((float) 90.0));
			TouchClient.getPApplet().rect(x2, y2-hr, width2, hr);
			TouchClient.getPApplet().rect(x2-hr, y2, hr, height2);
			TouchClient.getPApplet().rect(x2, y2+height2, width2, hr);
			TouchClient.getPApplet().rect(x2+width2,y2,hr, height2);*/
			if(fill){
				TouchClient.getPApplet().fill(getColour().getRed(), getColour().getGreen(), getColour().getBlue(), getColour().getAlpha());
			}
			roundedRect(x, y, width, height, r);

		} else if (fill){
			TouchClient.getPApplet().noStroke();
			TouchClient.getPApplet().fill(getColour().getRed(), getColour().getGreen(), getColour().getBlue(), getColour().getAlpha());
			TouchClient.getPApplet().rect(getX(), getY(), getWidth(), getHeight());

		}

		//TODO
		// GRADIENTs!!!!
		//setGradient(getX(),getY(), getWidth(), getHeight()/2, new Color(0,0,0), new Color(22, 22, 22), "Y_AXIS");
		//setGradient(getX(), getY() + getHeight()/2, getWidth(), getHeight(), new Color(22,22,22), new Color(44, 44, 44), "Y_AXIS");



		if(colFilter){
			drawFilter();
		}
	}

	/**
	 * Draws the rectangular colour filter
	 */
	public void drawFilter(){
		TouchClient.getPApplet().fill(filterColor.getRed(), filterColor.getGreen(), filterColor.getBlue(), filterColor.getAlpha());
		TouchClient.getPApplet().noStroke();
		TouchClient.getPApplet().rect(this.getX(),  this.getY(), this.getWidth(),  this.getHeight());
	}

	//////////////////////////////////////////////
	///// From WebSite - http://quasipartikel.at/2010/01/07/quadratic-bezier-curves-for-processingjs/
	///// Last Accessed on May 22, 2012
	///// Quadratic bezier curves for Processing.js
	public void quadraticBezierVertex(float f, float g, float h, float i) {
		prevX = (float) f;
		prevY = (float) g;
		float cp1x = (float) (prevX + 2.0/3.0*(f - prevX));
		float cp1y = (float) (prevY + 2.0/3.0*(g - prevY));
		float cp2x = (float) (cp1x + (h - prevX)/3.0);
		float cp2y = (float) (cp1y + (i - prevY)/3.0);


		// finally call cubic Bezier curve function
		TouchClient.getPApplet().bezierVertex(cp1x, cp1y, cp2x, cp2y, h, i);

	}


	//////////////////////////////////////////////
	///// From WebSite - http://quasipartikel.at/2010/01/07/quadratic-bezier-curves-for-processingjs/
	///// Last Accessed on May 22, 2012
	///// Quadratic bezier curves for Processing.js
	public void roundedRect(float f, float g, float h, float i, float r2) {  

		TouchClient.getPApplet().beginShape();
		TouchClient.getPApplet().vertex(f+r2, g);
		TouchClient.getPApplet().vertex(f+h-r2, g);
		quadraticBezierVertex(f+h, g, f+h, g+r2);
		TouchClient.getPApplet().vertex(f+h, g+i-r2);
		quadraticBezierVertex(f+h, g+i, f+h-r2, g+i);
		TouchClient.getPApplet().vertex(f+r2, g+i);
		quadraticBezierVertex(f, g+i, f, g+i-r2);
		TouchClient.getPApplet().vertex(f, g+r2);
		quadraticBezierVertex(f, g, f+r2, g);
		TouchClient.getPApplet().endShape();
	}



}
