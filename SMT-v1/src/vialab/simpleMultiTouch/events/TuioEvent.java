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
 * Holds the information associated with each type of event.
 * This is the base event class which all the other events extend.<P>
 *
 * University of Ontario Institute of Technology.
 * Summer Research Assistant with Dr. Christopher Collins (Summer 2011) collaborating with Dr. Mark Hancock.<P>
 * 
 * @author  Erik Paluka 
 * @date  Summer, 2011
 * @version 1.0
 */
public abstract class TuioEvent {
		/**An array of the cursors associated with this event (contains their session IDs).*/
	    long cursors[];	
	    /**x-coordinate of the event*/
	    int x; 		
	    /**y-coordinate of the event*/
	    int y;			
	    /**A flag indicating if the event has been handled*/
	    boolean handled = false;
	    
	    /**
	     * Base constructor for all events. Saves the x and y coordinates of the event,
	     * and the cursors associated with this event (their session IDs).
	     * 
	     * @param x int - X-coordinate of the event.
	     * @param y int - Y-coordinate of the event.
	     * @param cursors long[] - The cursors associated with this event (their session IDs).
	     */
	    public TuioEvent(int x, int y, long[] cursors){
	    	this.x = x;
	    	this.y = y;
	    	this.cursors = cursors;
	    }
	    
	    /**
	     * Gets the boolean value of the flag handled.
	     * Handled is set to true if the event has been handled.
	     * 
	     * @return handled boolean
	     */
	    public boolean handled() {
	    	return handled;
	    }
	    
	    /**
	     * Sets the boolean value of handled.
	     * 
	     * @param handled boolean
	     */
	    public void setHandled(boolean handled) {
	    	this.handled = handled;
	    }
	    
	    /**
	     * Gets the x-coordinate of the event.
	     * 
	     * @return x int - X-coordinate of the event.
	     */
	    public int getX() {
	    	return x;
	    }
	    
	    /**
	     * Gets the y-coordinate of the event.
	     * 
	     * @return y int - Y-coordinate of the event.
	     */
	    public int getY() {
	    	return y;
	    }
	    
	    /**
	     * Gets the cursors associated with this event (their session IDs)
	     * 
	     * @return cursors long[] - Array of session IDs
	     */
	    public long[] getCursors(){
	    	return cursors;
	    }
	    
}

