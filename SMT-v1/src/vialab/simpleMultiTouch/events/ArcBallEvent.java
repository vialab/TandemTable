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

import java.awt.geom.Line2D;
/**
 * UNIMPLEMENTED
 * Holds the information associated with an arc ball event.<P>
 *
 * University of Ontario Institute of Technology.
 * Summer Research Assistant with Dr. Christopher Collins (Summer 2011) collaborating with Dr. Mark Hancock.<P>
 * 
 * @author  Erik Paluka 
 * @date  Summer, 2011
 * @version 1.0
 */
//TODO Implement ARC BALL or other gestures to manipulate 3D objects
public class ArcBallEvent extends TuioEvent {
	/**The axis of rotation*/
	Line2D axis;
	/**The angle of rotation*/
	float angle;
	
	/**
	 * Calls the constructor of TuioEvent and passes it the event's coordinates
	 * and the cursors associated with the event. It also saves axis of rotation
	 * and the angle of rotation in two seperate variables.
	 * 
	 * @param x int - The event's x-coordinate
	 * @param y int - The event's y-coordinate
	 * @param cursors long[] - The cursors associated with the event (their session IDs)
	 * @param axis Line2D - The axis of rotation
	 * @param angle float - The angle of rotation
	 */
	public ArcBallEvent(int x, int y, long cursors[], Line2D axis, float angle){
    	super(x, y, cursors);
    	this.axis = axis;
    	this.angle = angle;
    	
    }
	
	/**
	 * Gets the angle of rotation. 
	 *
	 * @return angle float
	 */
	public float getAngle(){
		return angle;
	}
	
	/**
	 * Gets the axis of rotation
	 * 
	 * @return axis Line2D
	 */
	public Line2D getAxis(){
		return axis;
	}
}
