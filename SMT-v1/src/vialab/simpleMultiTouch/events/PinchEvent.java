/*
  Modified version of the TUIO processing library - part of the reacTIVision project
  http://reactivision.sourceforge.net/

  Copyright (c) 2005-2009 Martin Kaltenbrunner <mkalten@iua.upf.edu>
 
  This version Copyright (c) 2011 
  Erik Paluka, Christopher Collins - University of Ontario Institute of Technology
  Mark Hancock - University of Waterloo
  contact: christopher.collins@uoit.ca

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
package vialab.simpleMultiTouch.events;
/**
 * Holds the information associated with a pinch event.<P>
 *
 * University of Ontario Institute of Technology.
 * Summer Research Assistant with Dr. Christopher Collins (Summer 2011) collaborating with Dr. Mark Hancock.<P>
 * 
 * @author  Erik Paluka 
 * @date  Summer, 2011
 * @version 1.0
 */
public class PinchEvent extends TuioEvent{
	/**The The scale amount*/
	float scaleAmount;
	/**The The scale amount in the x-direction*/
	float xScaleAmount;	
	/**The The scale amount in the y-direction*/
	float yScaleAmount;
	
	/**The x-coordinate of the point of scale*/
	int xPofScale;
	/**The y-coordinate of the point of scale*/
	int yPofScale;


	/**
	 * Calls the constructor of TuioEvent and passes it the event's coordinates
	 * and the cursors associated with the event. It also saves the scale amount, the scale amount 
	 * in the x-direction, and the scale amount in the y-direction in variables.
	 * 
	 * @param x int - The event's x-coordinate
	 * @param y int - The event's y-coordinate
	 * @param cursors long[] - The cursors associated with the event (their session IDs)
	 * @param scaleAmount float - The scale amount
	 * @param xScaleAmount float - The scale amount in the x-direction
	 * @param yScaleAmount float - The scale amount in the y-direction
	 */
	public PinchEvent(int x, int y, long[] cursors, float scaleAmount, float xScaleAmount, float yScaleAmount, int xPofScale, int yPofScale){
    	super(x, y, cursors);
    	this.scaleAmount = scaleAmount;
    	this.xScaleAmount = xScaleAmount;
    	this.yScaleAmount = yScaleAmount;
    	this.xPofScale = xPofScale;
    	this.yPofScale = yPofScale;
    }
	
	/**
	 * Gets the scale amount.
	 * 
	 * @return scaleAmount float
	 */
	public float getScaleAmount(){
		return scaleAmount;
	}
	
	/**
	 * Gets the scale amount in the x-direction
	 * 
	 * @return xScaleAmount float
	 */
	public float getXScaleAmount(){
		return xScaleAmount;
	}
	
	/**
	 * Gets the scale amount in the y-direction.
	 * 
	 * @return yScaleAmount float
	 */
	public float getYScaleAmount(){
		return yScaleAmount;
	}

	public int getXPofScale() {
		return xPofScale;
	}

	public int getYPofScale() {
		return yPofScale;
	}
}
