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
 * Holds the information associated with a rotate event.<P>
 *
 * University of Ontario Institute of Technology.
 * Summer Research Assistant with Dr. Christopher Collins (Summer 2011) collaborating with Dr. Mark Hancock.<P>
 * 
 * @author  Erik Paluka 
 * @date  Summer, 2011
 * @version 1.0
 */
public class RotateEvent extends TuioEvent{
	/**The x-coordinate of the point of rotation*/
	int xPofRotation;
	/**The y-coordinate of the point of rotation*/
	int yPofRotation;
	/**The angle of rotation*/
	float angle;
	
	/** 
	 * Calls the constructor of TuioEvent and passes it the event's coordinates
	 * and the cursors associated with the event. It also saves the point of rotation, and
	 * the rotation angle in variables.
	 * 
	 * @param x int - The x-coordinate of the event
	 * @param y int - The y-coordinate of the event
	 * @param cursors long[] - The cursors involved in the event (their session IDs)
	 * @param xPofRotation int - The x-coordinate of the point of rotation
	 * @param yPofRotation int - The y-coordinate of the point of rotation
	 * @param angle float - The angle of rotation
	 */
	public RotateEvent(int x, int y, long[] cursors, int xPofRotation, int yPofRotation, float angle){
    	super(x, y, cursors);
    	this.xPofRotation = xPofRotation;
    	this.yPofRotation = yPofRotation;
    	this.angle = angle;
    }
	
	/**
	 * Gets the x-coordinate from the point of rotation
	 * 
	 * @return xPofRotation int - X-coordinate of rotation
	 */
	public int getXPofRotation(){
		return xPofRotation;
	}
	
	/**
	 * Gets the y-coordinate from the point of rotation
	 * 
	 * @return yPofRotation int - Y-coordinate of rotation
	 */
	public int getYPofRotation(){
		return yPofRotation;
	}

	/**
	 * Gets the angle of rotation.
	 * 
	 * @return angle - float
	 */
	public float getAngle(){
		return angle;
	}
}
