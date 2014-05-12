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
 * Holds the information associated with a tap and hold event.<P>
 *
 * University of Ontario Institute of Technology.
 * Summer Research Assistant with Dr. Christopher Collins (Summer 2011) collaborating with Dr. Mark Hancock.<P>
 * 
 * @author  Erik Paluka 
 * @date  Summer, 2011
 * @version 1.0
 */
public class TapAndHoldEvent extends TuioEvent{
	/**Hold long the user has held down a finger (milliseconds)*/
	long time;
	
	/**
	 * Calls the constructor of TuioEvent and passes it the event's coordinates
	 * and the cursors associated with the event. 
	 * 
	 * @param x int - The event's x-coordinate
	 * @param y int - The event's y-coordinate
	 * @param cursor long[] - The cursor associated with the event (their session IDs)
	 */
	public TapAndHoldEvent(int x, int y, long[] cursor, long time) {
		super(x, y, cursor);
		this.time = time;
	}
	
	public long getTime(){
		return this.time;
	}

}
