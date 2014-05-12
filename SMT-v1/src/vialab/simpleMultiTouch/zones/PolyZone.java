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

import processing.core.PConstants;
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

public class PolyZone extends Zone{
	boolean rounded = false;
	boolean fill = true;
	boolean stroke = false;
	boolean shadow = false;
	Color colour, strokeColour, shadowColour;
	float strokeWeight = 1;
	boolean curved;
	

	
	/**
	 * PolyZone constructor
	 **/
	public PolyZone(float[] coordinates, float width , float height, boolean curved){
		super();
		this.setPoly(true);
		createPolyZone(coordinates, width, height);
		this.curved = curved;
	}




	public void createPolyZone(float[] coordinates, float wIn, float hIn){
		setCoordinates(coordinates);
		colour = new Color(0, 0, 0);
		shadowColour = new Color(155, 155, 155);
		strokeColour = new Color(0, 0, 0);
		x = coordinates[0];					// upper left corner x
		y = coordinates[0];					// upper left corner y
		width = wIn;		// width
		height = hIn;	// height
		vx = vy = 0;				// velocities
		setTranslateAreaRadius((float) (Math.sqrt(wIn*wIn + hIn*hIn) / 5)); // Set the translate area radius for RNT
	}

	

	/*public void setShadow(boolean flag){
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
	}*/
	public void setColour(int r, int g, int b){
		fill = true;
		colour = new Color(r, g, b);
	}

	public void setColour(int r, int g, int b, int a){
		fill = true;
		colour = new Color(r, g, b, a);
	}

	public void setColour(Color c){
		fill = true;
		colour = c;
	}


	public Color getColour(){
		return colour;
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

	/*public void setShadowX(int xShadow){
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
	}*/

	public void drawZone(){
		super.drawZone();

		/*if(shadow){
			TouchClient.getPApplet().stroke(shadowColour.getRed(), shadowColour.getGreen(), shadowColour.getBlue(), shadowColour.getAlpha());
			TouchClient.getPApplet().strokeWeight(1);
			applet.fill(shadowColour.getRed(), shadowColour.getGreen(), shadowColour.getBlue());
			if(rounded){
				//roundedRect(x + xShadow, y + yShadow, width + wShadow, height + hShadow, r);
			} else {
				//applet.rect(x + xShadow, y + yShadow, width + wShadow, height + hShadow);
			}
		}*/


		if(stroke){
			TouchClient.getPApplet().stroke(strokeColour.getRed(), strokeColour.getGreen(), strokeColour.getBlue(), strokeColour.getAlpha());
			TouchClient.getPApplet().strokeWeight(strokeWeight);

		} else {
			TouchClient.getPApplet().noStroke();
		}



		if (fill){
			TouchClient.getPApplet().fill(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha());
		}
		TouchClient.getPApplet().beginShape();
		if(this.curved){
			TouchClient.getPApplet().curveVertex(coordinates[0], coordinates[1]);
		}
		
		for(int i = 0; i < coordinates.length; i = i + 2){

			if(this.curved){
				TouchClient.getPApplet().curveVertex(coordinates[i], coordinates[i+1]);
			} else {
				TouchClient.getPApplet().vertex(coordinates[i], coordinates[i+1]);
			}

		}
		if(this.curved){
			TouchClient.getPApplet().curveVertex(coordinates[0], coordinates[1]);
		}

		TouchClient.getPApplet().endShape(PConstants.CLOSE);




	}




}
