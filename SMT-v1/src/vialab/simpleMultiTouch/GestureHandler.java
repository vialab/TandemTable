/*
 * Simple Multitouch Library Copyright 2011 Erik Paluka, Christopher Collins -
 * University of Ontario Institute of Technology Mark Hancock - University of
 * Waterloo
 * 
 * Parts of this library are based on: TUIOZones http://jlyst.com/tz/
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License Version 3 as published by the
 * Free Software Foundation.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package vialab.simpleMultiTouch;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PVector;
import vialab.simpleMultiTouch.events.DragEvent;
import vialab.simpleMultiTouch.events.HSwipeEvent;
import vialab.simpleMultiTouch.events.PinchEvent;
import vialab.simpleMultiTouch.events.RotateEvent;
import vialab.simpleMultiTouch.events.TapAndHoldEvent;
import vialab.simpleMultiTouch.events.TapEvent;
import vialab.simpleMultiTouch.events.VSwipeEvent;
import vialab.simpleMultiTouch.zones.Zone;
import TUIO.TuioCursor;
import TUIO.TuioPoint;
import TUIO.TuioTime;

/**
 * It handles the TuioCursor events, sees if a gesture has been performed, and
 * if so, it triggers the gesture's action.
 * <P>
 * 
 * University of Ontario Institute of Technology. Summer Research Assistant with
 * Dr. Christopher Collins (Summer 2011) collaborating with Dr. Mark Hancock.
 * <P>
 * 
 * @author Erik Paluka
 * @date Summer, 2011
 * @version 1.0
 */
public class GestureHandler {
	/** Processing PApplet */
	static PApplet parent = TouchClient.pApplet;

	/** The Touch Client zone list */
	static Vector<Zone> zoneList = TouchClient.getZoneList();

	/** The gesture events that the student may override */
	protected Method dragEvent, pinchEvent, rotateEvent, vSwipeEvent, hSwipeEvent, tapEvent,
	tapAndHoldEvent;

	/**
	 * Gesture Handler constructor, gets the gesture and TUIO methods from the
	 * Processing sketch if the student has overridden them.
	 */
	public GestureHandler() {
		dragEvent = SMTUtilities.getPMethod(parent, "dragEvent", new Class[] { DragEvent.class });
		pinchEvent = SMTUtilities.getPMethod(parent, "pinchEvent", new Class[] { PinchEvent.class });
		tapEvent = SMTUtilities.getPMethod(parent, "tapEvent", new Class[] { TapEvent.class });
		tapAndHoldEvent = SMTUtilities.getPMethod(parent, "tapAndHoldEvent",
				new Class[] { TapAndHoldEvent.class });
		rotateEvent = SMTUtilities.getPMethod(parent, "rotateEvent",
				new Class[] { RotateEvent.class });
		hSwipeEvent = SMTUtilities.getPMethod(parent, "hSwipeEvent",
				new Class[] { HSwipeEvent.class });
		vSwipeEvent = SMTUtilities.getPMethod(parent, "vSwipeEvent",
				new Class[] { VSwipeEvent.class });
	}

	/**
	 * Tests if gestures, that require an addTuioCursor event, have been
	 * performed. If yes, it triggers the gesture's action.
	 * 
	 * @param zone
	 *            Zone - The zone on the top-most layer that contains the
	 *            coordinates of the TuioCursor.
	 * @param tcur
	 *            TuioCursor - The TuioCursor that has just moved on the screen.
	 */
	public void detectOnAdd(Zone zone, TuioCursor tcur, int xIn, int yIn) {
		long[] cursor = new long[1];
		cursor[0] = tcur.getSessionID();

		// ////////////////
		// Tap Gesture
		// ////////////////
		if (zone.isTappable() && zone.getTapDown()) {
			detectTouchDown(zone, xIn, yIn, cursor);
		}
	}

	/**
	 * Tests if gestures, that require a removeTuioCursor event, have been
	 * performed. If yes, it triggers the gesture's action.
	 * 
	 * @param zone
	 *            Zone - The zone on the top-most layer that contains the
	 *            coordinates of the TuioCursor.
	 * @param tcur
	 *            TuioCursor - The TuioCursor that has just moved on the screen.
	 */
	public void detectOnRemove(Zone zone, TuioCursor tcur, int xIn, int yIn) {
		long[] cursor = new long[1];
		cursor[0] = tcur.getSessionID();

		// ////////////////
		// Tap Gesture
		// ////////////////
		if (zone.isTappable() && zone.getTapUp()) {
			detectTouchUp(zone, xIn, yIn, cursor);
		}

	}

	/**
	 * Tests if gestures, that require an updateTuioCursor event, have been
	 * performed. If yes, it triggers the gesture's action.
	 * 
	 * @param zone
	 *            Zone - The zone on the top-most layer that contains the
	 *            coordinates of the TuioCursor.
	 * @param tcur
	 *            TuioCursor - The TuioCursor that has just moved on the screen.
	 * @param xIn
	 *            int - The x-coordinate of the TuioCursor.
	 * @param yIn
	 *            int - The y-coordinate of the TuioCursor
	 */
	void detectOnUpdate(Zone zone, TuioCursor tcur, int xIn, int yIn) {
		long[] cursor = new long[1];
		cursor[0] = tcur.getSessionID();

		// /////////////////////////////////////////////////////////////
		// Two-finger gesture -- Rotate and Pinch Gesture (Scalable)
		// ////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////
		////////// Use rst instead
		////////////////////////////////////////////////
		/*if (zone.getCurrentTouches() == 2) {
			zone.setRNTing(false);
			for (TuioCursor tcur2 : zone.getTuioCursorList()) {
				if (tcur2 != null && tcur.getSessionID() != tcur2.getSessionID()) {

					long[] cursors = new long[2];
					cursors[0] = tcur.getSessionID();
					cursors[1] = tcur2.getSessionID();

					int[] iLast = new int[2];
					iLast[0] = tcur2.getPath().lastElement().getScreenX(TouchClient.pApplet.width);
					iLast[1] = tcur2.getPath().lastElement().getScreenY(TouchClient.pApplet.height);

					int midX = (xIn + iLast[0]) / 2;
					int midY = (yIn + iLast[1]) / 2; // Center of pinch and
					// rotate gesture

					zone.contains(midX, midY); // Get midX and midY in the
					// zone's matrix space
					// The values will be in the variables localX and localY

					// Reset the lastLocal values when first starting the
					// gestures.
					if (!zone.getRotating() && !zone.getPinching()) {
						zone.setLastLocalX(zone.getLocalX());
						zone.setLastLocalY(zone.getLocalY());
						zone.setAngle(0);
					}
					// When the centre point between the fingers moves,
					// translate that amount
					if (zone.getCurrentTouches() == 2) {
						fireDragEvent(midX, midY, cursors, zone.getLocalX() - zone.getLastLocalX(),
								zone.getLocalY() - zone.getLastLocalY(), zone);
					}

					// ///////////////////
					// Pinching Gesture
					// //////////////////
					if (zone.isPinchable() || zone.isXYPinchable() || zone.isXPinchable() || zone.isYPinchable()) {
						detectPinch(zone, xIn, yIn, cursors);
					}
					// ////////////////
					// Rotate Gesture
					// ////////////////
					if (zone.isRotatable()) {
						detectRotate(zone, xIn, yIn, cursors);
					}
					return;
				}
			}
		}*/

		rst(zone, tcur);
		// ////////////////
		// RNT Gesture
		// Drag and X/Y Drag Gesture
		// ////////////////
		if (zone.isRNTable()) {
			detectRNT(zone, xIn, yIn, cursor);
		}
		else if (zone.isDraggable() || zone.isXDraggable() || zone.isYDraggable()) {
			detectDrag(zone, xIn, yIn, cursor);
		}

		// ////////////////////////
		// Horizontal Swipe Gesture
		// /////////////////////////
		if (zone.isHSwipeable()) {
			detectHSwipe(zone, xIn, yIn, cursor);
		}
		// ///////////////////////////
		// Vertical Swipe Gesture
		// ///////////////////////////
		if (zone.isVSwipeable()) {
			detectVSwipe(zone, xIn, yIn, cursor);
		}
		// //////////////////////////
		// Tap and Hold Gesture
		// /////////////////////////
		if (zone.isTapAndHoldable()) {
			detectTapAndHold(zone, xIn, yIn, cursor);
		}
	}

	
	public int compare(TuioTime timeA, TuioTime timeB) {
		Long timeALong = new Long(timeA.getSeconds());
		int temp = timeALong.compareTo(timeB.getSeconds());
		// int temp = Long.compare(timeA.getSeconds(), timeB.getSeconds());
		if (temp != 0) {
			return temp;
		}
		timeALong = new Long(timeA.getMicroseconds());
		// return Long.compare(timeA.getMicroseconds(),
		// timeB.getMicroseconds());
		return timeALong.compareTo(timeB.getMicroseconds());
	}
	
	private void rst(Zone zone, TuioCursor tcur) {
		if(zone.getTouchCount() >= 2) {
			TuioCursor touch1 = zone.getTuioCursorList().get(0);
			TuioCursor touch2 = zone.getTuioCursorList().get(1);
			TuioPoint toPoint1 = touch1.getPath().get(touch1.getPath().size()-1);
			TuioPoint toPoint2 = touch2.getPath().get(touch2.getPath().size()-1);
			TuioPoint fromPoint1 = null;
			TuioPoint fromPoint2 = null;
			
			Vector<TuioPoint> path = new Vector<TuioPoint>(touch1.getPath());
			Collections.reverse(path);
			
			for(TuioPoint p: path) {
				if(compare(p.getTuioTime(), zone.lastUpdate) <= 0) {
					fromPoint1 = p;
					break;
				}
			}
			
			path = new Vector<TuioPoint>(touch2.getPath());
			Collections.reverse(path);
			for(TuioPoint p: path) {
				if(compare(p.getTuioTime(), zone.lastUpdate) <= 0) {
					fromPoint2 = p;
					break;
				}
			}
			
			/*if (zone.isDraggable() || zone.isXDraggable() || zone.isYDraggable()) {
				if (zone.isXDraggable() || zone.isDraggable()) {
					zone.getMatrix().translate(toPoint1.getX(), 0);
				}
				if (zone.isYDraggable() || zone.isDraggable()) {
					zone.getMatrix().translate(0, toPoint1.getY());
				}
			}
			else {*/
				zone.getMatrix().translate((zone.getX() + zone.getWidth() / 2), (zone.getY() + zone.getHeight() / 2));
			//}
			
			if(fromPoint1 != null && fromPoint2 != null && (zone.isRotatable() || zone.isPinchable())) {
				PVector fromVec = new PVector(fromPoint2.getX(), fromPoint2.getY());
				fromVec.sub(new PVector(fromPoint1.getX(), fromPoint1.getY()));
				
				PVector toVec = new PVector(toPoint2.getX(), toPoint2.getY());
				toVec.sub(new PVector(toPoint1.getX(), toPoint1.getY()));
				
				float toDist = toVec.mag();
				float fromDist = fromVec.mag();
				
				if (toDist > 0 && fromDist > 0) {
					float angle = PVector.angleBetween(fromVec, toVec);
					PVector cross = PVector.cross(fromVec, toVec, new PVector());
					cross.normalize();
	
					if (!Float.isNaN(angle) && angle != 0 && cross.z != 0 && zone.isRotatable()) {
						zone.getMatrix().rotate(angle, cross.x, cross.y, cross.z);
					}
					
					if (zone.isPinchable()) {
						float ratio = toDist / fromDist;
						zone.getMatrix().scale(ratio);
					}
				}
			}
			
			/*if (zone.isDraggable() || zone.isXDraggable() || zone.isYDraggable()) {
				if (zone.isXDraggable() || zone.isDraggable()) {
					zone.getMatrix().translate(-fromPoint1.getX(), 0);
				}
				if (zone.isYDraggable() || zone.isDraggable()) {
					zone.getMatrix().translate(0, -fromPoint1.getY());
				}
			}
			else {*/
				zone.getMatrix().translate(-(zone.getX() + zone.getWidth() / 2), -(zone.getY() + zone.getHeight() / 2));
			//}

			
			if(touch1.getTuioTime().getMicroseconds() > touch2.getTuioTime().getMicroseconds()) {
				zone.lastUpdate = touch1.getTuioTime();
			} else {
				zone.lastUpdate = touch2.getTuioTime();
			}
		} else {
			zone.lastUpdate = tcur.getTuioTime();
		}
	}
	
	/**
	 * Detects a rotate gesture and fires the corresponding event if it has
	 * occurred.
	 * 
	 * @param zone
	 *            Zone - The zone that the rotate occurred in
	 * @param xIn
	 *            int - The current x-coordinate of the TuioCursor
	 * @param yIn
	 *            int - The current y-coordinate of the TuioCursor
	 * @param cursors
	 *            long[] - An array holding the session IDs of the cursors
	 *            involved in the gesture (just 2 cursors in this case)
	 */
	private void detectRotate(Zone zone, int xIn, int yIn, long[] cursors) {
		TuioCursor tcur = TouchClient.getTuioClient().getTuioCursor(cursors[1]);

		if(tcur != null){
			if (!zone.getRotating()) {
				zone.setAngle(TouchClient.getTuioClient().getTuioCursor(cursors[0]).getAngle(
						TouchClient.getTuioClient().getTuioCursor(cursors[1])));
				zone.setRotating(true);
			}
			float rotAngle = TouchClient.getTuioClient().getTuioCursor(cursors[0]).getAngle(
					TouchClient.getTuioClient().getTuioCursor(cursors[1]))
					- zone.getAngle();

			// When you first start to rotate zone, it rotates 360 degrees for some
			// reason.
			// This gets rid of that bug
			if (Math.abs(rotAngle) > 3) {
				return;
			}

			// If the current rotation angle is less than the last rotation angle
			// for the zone, then
			// rotate the opposite way.
			if (rotAngle < zone.getAngle()) {
				rotAngle = -rotAngle;
			}
			fireRotateEvent(xIn, yIn, cursors, (int) zone.getLocalX(), (int) zone.getLocalY(), rotAngle, zone);
		}
	}

	/**
	 * Detects a pinch gesture and fires the corresponding event if it has
	 * occurred.
	 * 
	 * @param zone
	 *            Zone - The zone that the pinch occurred in
	 * @param xIn
	 *            int - The current x-coordinate of the TuioCursor
	 * @param yIn
	 *            int - The current y-coordinate of the TuioCursor
	 * @param cursors
	 *            long[] - An array holding the session IDs of the cursors
	 *            involved in the gesture (just 2 cursors in this case)
	 */
	private void detectPinch(Zone zone, int xIn, int yIn, long[] cursors) {
		TuioCursor tcur = TouchClient.getTuioClient().getTuioCursor(cursors[1]);

		if(tcur != null){
			int[] iLast = new int[2];
			iLast[0] = tcur.getPath().lastElement().getScreenX(parent.width);
			iLast[1] = tcur.getPath().lastElement().getScreenY(parent.height);

			float sclXDist = (float) Math.sqrt((iLast[0] - xIn) * (iLast[0] - xIn));
			float sclYDist = (float) Math.sqrt((iLast[1] - yIn) * (iLast[1] - yIn));
			float sclDist = (float) Math.sqrt((iLast[0] - xIn) * (iLast[0] - xIn) + (iLast[1] - yIn)
					* (iLast[1] - yIn));

			// For XYPINCH
			if (zone.getCurrentTouches() == 4 && zone.isXYPinchable()) {
				int[] z = new int[8];
				int j = 0;
				cursors = new long[4];
				for (int i = 0; i < 4; i++) {
					cursors[i] = zone.getTuioCursorList().get(i).getSessionID();
					z[j] = zone.getTuioCursorList().get(i).getPath().lastElement()
							.getScreenX(TouchClient.pApplet.width);
					z[j + 1] = zone.getTuioCursorList().get(i).getPath().lastElement()
							.getScreenY(TouchClient.pApplet.height);
					j += 2;
				}
				float dist1x = Math.abs(z[0] - z[2]);
				float dist2x = Math.abs(z[0] - z[4]);
				float dist3x = Math.abs(z[0] - z[6]);
				int x, y;

				if (dist1x >= dist2x && dist1x >= dist3x) {
					x = 2;
				}
				else if (dist2x >= dist1x && dist2x >= dist3x) {
					x = 4;
				}
				else {
					x = 6;
				}

				float dist1y = Math.abs(z[1] - z[3]);
				float dist2y = Math.abs(z[1] - z[5]);
				float dist3y = Math.abs(z[1] - z[7]);

				if (dist1y >= dist2y && dist1y >= dist3y) {
					y = 3;
				}
				else if (dist2y >= dist1y && dist2y >= dist3y) {
					y = 5;
				}
				else {
					y = 7;
				}

				sclXDist = (float) Math.sqrt((z[0] - z[x]) * (z[0] - z[x]));
				sclYDist = (float) Math.sqrt((z[1] - z[y]) * (z[1] - z[y]));
				sclDist = (float) Math.sqrt((z[0] - z[x]) * (z[0] - z[x]) + (z[1] - z[y])
						* (z[1] - z[y]));
				// The values will be in the variables localX and localY
				int midX = (xIn + iLast[0]) / 2;
				int midY = (yIn + iLast[1]) / 2; // Center of pinch and rotate
				// gesture
				zone.contains(midX, midY); // Get midX and midY in the zone's matrix
				// space

				// Reset the lastLocal values when first starting the gestures.
				if (!zone.getXYPinching()) {
					zone.setLastLocalX(zone.getLocalX());
					zone.setLastLocalY(zone.getLocalY());
					zone.setLastSclDist(sclDist);
					zone.setLastSclYDist(sclYDist);
					zone.setLastSclXDist(sclXDist);
					zone.setXYPinching(true);
				}
				// When the centre point between the fingers moves, translate that
				// amount
				fireDragEvent(midX, midY, cursors, zone.getLocalX() - zone.getLastLocalX(), zone.getLocalY()
						- zone.getLastLocalY(), zone);
			}
			if (!zone.getPinching()) {
				zone.setLastSclDist(sclDist);
				zone.setLastSclYDist(sclYDist);
				zone.setLastSclXDist(sclXDist);
				zone.setPinching(true);
			}

			float scl = 1.0f - zone.getScaleSens() + zone.getScaleSens() * (sclDist / zone.getLastSclDist());// apply
			// sensitivity

			float sclX = 1.0f - zone.getScaleSens() + zone.getScaleSens() * (sclXDist / zone.getLastSclXDist());// apply
			// sensitivity

			float sclY = 1.0f - zone.getScaleSens() + zone.getScaleSens() * (sclYDist / zone.getLastSclYDist());// apply
			// sensitivity

			zone.setLastSclDist(sclDist);
			zone.setLastSclYDist(sclYDist);
			zone.setLastSclXDist(sclXDist);

			// limit scale gesture
			if (scl < zone.getScaleLow()) {
				scl = zone.getScaleLow();
			}
			if (scl > zone.getScaleHigh()) {
				scl = zone.getScaleHigh();
			}
			if (sclX < zone.getScaleLow()) {
				sclX = zone.getScaleLow();
			}
			if (sclX > zone.getScaleHigh()) {
				sclX = zone.getScaleHigh();
			}
			if (sclY < zone.getScaleLow()) {
				sclY = zone.getScaleLow();
			}
			if (sclY > zone.getScaleHigh()) {
				sclY = zone.getScaleHigh();
			}
			firePinchEvent(xIn, yIn, cursors, scl, sclX, sclY, (int) zone.getLocalX(), (int) zone.getLocalY(), zone);
		}
	}

	/**
	 * Detects if a horizontal swipe has occurred over a zone and fires the
	 * corresponding event if it has occurred.
	 * 
	 * @param zone
	 *            Zone - The zone that the Horizontal swipe occurred in
	 * @param xIn
	 *            int - The current x-coordinate of the TuioCursor
	 * @param yIn
	 *            int - The current y-coordinate of the TuioCursor
	 * @param cursor
	 *            long[] - An array holding the session IDs of the cursors
	 *            involved in the gesture (just 1 cursor in this case)
	 */
	private void detectHSwipe(Zone zone, int xIn, int yIn, long[] cursor) {
		TuioCursor t = TouchClient.getTuioClient().getTuioCursor(cursor[0]);
		float xSpeed = t.getXSpeed();
		float thresholdDistX = (zone.getWidth() / 2);
		if(zone.getHSwipeDist() != 0){
			thresholdDistX = zone.getHSwipeDist();
		}
		//TODO

		float p1 = t.getPath().get(0).getScreenX(parent.getWidth());
		float p2 = t.getPath().lastElement().getScreenX(parent.getWidth());

		if(zone.getLastHSwipeCursor() != t.getSessionID() && Math.abs(p1 - p2) > thresholdDistX){
			if(xSpeed > 0){
				fireHSwipeEvent(xIn, yIn, cursor, zone, 1);
			} else if (xSpeed < 0){
				fireHSwipeEvent(xIn, yIn, cursor, zone, -1);
			}
		}

	}

	/**
	 * Detects if a vertical swipe has occurred over a zone and fires the
	 * corresponding event if it has occurred.
	 * 
	 * @param zone
	 *            Zone - The zone that the vertical swipe occurred in
	 * @param xIn
	 *            int - The current x-coordinate of the TuioCursor
	 * @param yIn
	 *            int - The current y-coordinate of the TuioCursor
	 * @param cursor
	 *            long[] - An array holding the session IDs of the cursors
	 *            involved in the gesture (just 1 cursor in this case)
	 */
	private void detectVSwipe(Zone zone, int xIn, int yIn, long[] cursor) {
		TuioCursor t = TouchClient.getTuioClient().getTuioCursor(cursor[0]);
		float ySpeed = t.getYSpeed();
		float thresholdDistY = (zone.getHeight() / 2);
		if(zone.getVSwipeDist() != 0){
			thresholdDistY = zone.getVSwipeDist();
		}
		//TODO

		float p1 = t.getPath().get(0).getScreenY(parent.getHeight());
		float p2 = t.getPath().lastElement().getScreenY(parent.getHeight());

		if(zone.getLastVSwipeCursor() != t.getSessionID() && Math.abs(p1 - p2) > thresholdDistY){
			if(ySpeed > 0){
				fireVSwipeEvent(xIn, yIn, cursor, zone, 1);
			} else if (ySpeed < 0){
				fireVSwipeEvent(xIn, yIn, cursor, zone, -1);
			}
		}
	}

	/**
	 * Detects an RNT gesture and fires the corresponding event if it has
	 * occurred.
	 * 
	 * @param zone
	 *            Zone - The zone that RNT occurred in
	 * @param xIn
	 *            int - The current x-coordinate of the TuioCursor
	 * @param yIn
	 *            int - The current y-coordinate of the TuioCursor
	 * @param cursor
	 *            long[] - An array holding the session IDs of the cursors
	 *            involved in the gesture (just 1 cursor in this case)
	 */
	private void detectRNT(Zone zone, int xIn, int yIn, long[] cursor) {
		if (zone.getId(0) == TouchClient.getTuioClient().getTuioCursor(cursor[0]).getSessionID()) {
			float xDist = zone.getLocalX() - zone.getLastLocalX();
			float yDist = zone.getLocalY() - zone.getLastLocalY();
			fireDragEvent(xIn, yIn, cursor, xDist, yDist, zone);
			Point2D positionInZone = new Point2D.Float(zone.getLocalX(), zone.getLocalY());

			if (positionInZone.distance(zone.getX() + zone.getWidth() / 2, zone.getY() + zone.getHeight() / 2) >= zone.getTranslateAreaRadius()) {
				if (!zone.getRNTing()) {
					zone.setLastGlobalX(zone.getLocalX());
					zone.setLastGlobalY(zone.getLocalY());
					zone.setAngle(getAngleABC(new Point.Float(zone.getLocalX(), zone.getLocalY()),
							new Point.Float(zone.getX() + zone.getWidth() / 2, zone.getY() + zone.getHeight() / 2),
							new Point.Float(zone.getLastGlobalX(), zone.getLastGlobalY())));
					zone.setRNTing(true);
				}
				float angle = getAngleABC(new Point.Float(zone.getLocalX(), zone.getLocalY()),
						new Point.Float(zone.getX() + zone.getWidth() / 2, zone.getY() + zone.getHeight() / 2),
						new Point.Float(zone.getLastGlobalX(), zone.getLastGlobalY()));
				fireRotateEvent(xIn, yIn, cursor, (int) zone.getLocalX(), (int) zone.getLocalY(), angle
						- zone.getAngle(), zone);

			}
		}
	}

	/**
	 * Detects a drag gesture and fires the corresponding event if it has
	 * occurred.
	 * 
	 * @param zone
	 *            Zone - The zone that the drag occurred in
	 * @param xIn
	 *            int - The current x-coordinate of the TuioCursor
	 * @param yIn
	 *            int - The current y-coordinate of the TuioCursor
	 * @param cursor
	 *            long[] - An array holding the session IDs of the cursors
	 *            involved in the gesture (just 1 cursor in this case)
	 */
	private void detectDrag(Zone zone, int xIn, int yIn, long[] cursor) {
		if (zone.getId(0) == TouchClient.getTuioClient().getTuioCursor(cursor[0]).getSessionID()) {
			float xDist = zone.getLocalX() - zone.getLastLocalX();
			float yDist = zone.getLocalY() - zone.getLastLocalY();
			fireDragEvent(xIn, yIn, cursor, xDist, yDist, zone);
		}
	}

	/**
	 * Detects a tap(s) gesture and fires the corresponding event if it has
	 * occurred.
	 * 
	 * @param zone
	 *            Zone - The zone that the drag occurred in
	 *
	 * @param cursor
	 *            long[] - An array holding the session IDs of the cursors
	 *            involved in the gesture (just 1 cursor in this case)
	 */
	private void detectTouchDown(Zone zone, int xIn, int yIn, long[] cursor) {


		if (zone.getLastTouchDown() != cursor[0]) {

			
				fireTapEvent(xIn, yIn, cursor, 1, zone);
				zone.setLastTouchDown(cursor[0]);
				

		}
	}

	/**
	 * Detects a tap gesture and fires the corresponding event if it has
	 * occurred.
	 * 
	 * @param zone
	 *            Zone - The zone that the drag occurred in
	 *
	 * @param cursor
	 *            long[] - An array holding the session IDs of the cursors
	 *            involved in the gesture (just 1 cursor in this case)
	 */
	private void detectTouchUp(Zone zone, int xIn, int yIn, long[] cursor) {

		if (zone.getLastTouchUp() != cursor[0] && zone.contains(xIn, yIn)) {

			fireTapEvent(xIn, yIn, cursor, 1, zone);
			
			zone.setLastTouchUp(cursor[0]);


		}
	}

	/**
	 * Detects a tap and hold gesture and fires the corresponding event if it
	 * has occurred.
	 * 
	 * @param zone
	 *            Zone - The zone that the drag occurred in
	 * @param xIn
	 *            int - The current x-coordinate of the TuioCursor
	 * @param yIn
	 *            int - The current y-coordinate of the TuioCursor
	 * @param cursor
	 *            long[] - An array holding the session IDs of the cursors
	 *            involved in the gesture (just 1 cursor in this case)
	 */
	private void detectTapAndHold(Zone zone, int xIn, int yIn, long[] cursor) {
		TuioCursor tcur = TouchClient.getTuioClient().getTuioCursor(cursor[0]);
		Vector<TuioPoint> path = tcur.getPath();
		long time = tcur.getStartTime().getTotalMilliseconds();

		if (TuioTime.getSessionTime().getTotalMilliseconds() - time > zone.getHoldDurationMilli()
				&& (Math.abs(path.firstElement().getX() - path.lastElement().getX()) < 0.005 && Math.abs(path
						.firstElement().getY() - path.lastElement().getY()) < 0.005)) {
			fireTapAndHoldEvent(xIn, yIn, cursor, time, zone);
		}

	}

	/**
	 * Fires a pinch event. First fires the event in the Processing PApplet if
	 * the student has implemented the method. If the event has not been handled
	 * then it goes through the zone list until it has been handled.
	 * 
	 * @param x
	 *            int - The x-coordinate of the event
	 * @param y
	 *            int - The y-coordinate of the event
	 * @param cursors
	 *            long[] - The cursors involved in the event (their session IDs)
	 * @param scl
	 *            float - The scale amount
	 * @param sclX
	 *            float - The X scale amount
	 * @param sclY
	 *            float - The Y scale amount
	 */
	public void firePinchEvent(int x, int y, long[] cursors, float scl, float sclX, float sclY, int xPofScale, int yPofScale, Zone zone) {
		PinchEvent e = new PinchEvent(x, y, cursors, scl, sclX, sclY, xPofScale, yPofScale);
		if (pinchEvent != null) {
			try {
				// fire event on PApplet
				pinchEvent.invoke(parent, e);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (!e.handled()) {
			// fire event on zones
			//for (int i = zoneList.size() - 1; i >= 0; i--) {
			if (/*zoneList.get(i).contains(x, y)
						&& */(zone.isPinchable() || zone.isXYPinchable()
								|| zone.isXPinchable() || zone.isYPinchable())) {
				zone.pinchEvent(e);
			}
			if (e.handled()) {
				return;
			}

			//}
		}
	}

	/**
	 * Fires a drag event. First fires the event in the Processing PApplet if
	 * the student has implemented the method. If the event has not been handled
	 * then it goes through the zone list until it has been handled.
	 * 
	 * @param x
	 *            int - The x-coordinate of the event
	 * @param y
	 *            int - The y-coordinate of the event
	 * @param cursors
	 *            long[] - The cursors involved in the event (their session IDs)
	 * @param xDist
	 *            float - The movement in the x-direction
	 * @param yDist
	 *            float - The movement in the y-direction
	 */
	public void fireDragEvent(int x, int y, long[] cursors, float xDist, float yDist, Zone zone) {
		DragEvent e = new DragEvent(x, y, cursors, xDist, yDist);
		if (dragEvent != null) {
			try {
				// fire event on PApplet
				dragEvent.invoke(parent, e);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (!e.handled()) {
			// fire event on zones
			// for (int i = zoneList.size()-1; i >= 0; i--) {
			if (/*
			 * zone.contains(x, y) &&
			 */(zone.isDraggable() || zone.isXDraggable() || zone.isYDraggable() || zone.isRNTable())) {
				zone.dragEvent(e);
			}
			if (e.handled()) {
				return;
			}

			// }
		}
	}

	/**
	 * Fires a rotate event. First fires the event in the Processing PApplet if
	 * the student has implemented the method. If the event has not been handled
	 * then it goes through the zone list until it has been handled.
	 * 
	 * @param x
	 *            int - The x-coordinate of the event
	 * @param y
	 *            int - The y-coordinate of the event
	 * @param cursors
	 *            long[] - The cursors involved in the event (their session IDs)
	 * @param xPofRotation
	 *            int - the x-coordinate of the point of rotation
	 * @param yPofRotation
	 *            int - the y-coordinate of the point of rotation
	 * @param angle
	 *            float - The angle of rotation
	 */
	public void fireRotateEvent(int x, int y, long[] cursors, int xPofRotation, int yPofRotation,
			float angle, Zone zone) {
		RotateEvent e = new RotateEvent(x, y, cursors, xPofRotation, yPofRotation, angle);
		if (rotateEvent != null) {
			try {
				// fire event on PApplet
				rotateEvent.invoke(parent, e);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (!e.handled()) {
			// fire event on zones
			//for (int i = zoneList.size() - 1; i >= 0; i--) {
			//Zone zone = zoneList.get(i);
			if (/*zone.contains(x, y) &&*/ (zone.isRotatable() || zone.isRNTable())) {
				zone.rotateEvent(e);
			}
			if (e.handled()) {
				return;
			}

			//}
		}
	}

	/**
	 * Fires a horizontal swipe event. First fires the event in the Processing
	 * PApplet if the student has implemented the method. If the event has not
	 * been handled then it goes through the zone list until it has been
	 * handled.
	 * 
	 * @param x
	 *            int - The x-coordinate of the event
	 * @param y
	 *            int - The y-coordinate of the event
	 * @param cursors
	 *            long[] - The cursors involved in the event (their session IDs)
	 * @param zone
	 * 			  Zone - The zone associated with the HSwipeEvent
	 * @param swipeType
	 * 			  int - The type of swipe -> -1 for left swipe, 1 for right swipe
	 */
	public void fireHSwipeEvent(int x, int y, long[] cursors, Zone zone, int swipeType) {
		TuioCursor t = TouchClient.getTuioClient().getTuioCursor(cursors[0]);
		zone.setLastHSwipeCursor(t.getSessionID());

		HSwipeEvent e = new HSwipeEvent(x, y, cursors, swipeType);
		if (hSwipeEvent != null) {
			try {
				// fire event on PApplet
				hSwipeEvent.invoke(parent, e);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (!e.handled()) {
			// fire event on zones
			//for (int i = zoneList.size() - 1; i >= 0; i--) {
			if (/*
			 * zoneList.get(i).contains(x, y) &&
			 */zone.isHSwipeable()) {
				if(TouchClient.debugMode) {
					System.out.println("Firing H Swipe: " + zone.toString());
				}

				zone.hSwipeEvent(e);
			}
			if (e.handled()) {
				return;
			}

			//}
		}
	}

	/**
	 * Fires a vertical swipe event. First fires the event in the Processing
	 * PApplet if the student has implemented the method. If the event has not
	 * been handled then it goes through the zone list until it has been
	 * handled.
	 * 
	 * @param x
	 *            int - The x-coordinate of the event
	 * @param y
	 *            int - The y-coordinate of the event
	 * @param cursors
	 *            long[] - The cursors involved in the event (their session IDs)
	 * @param zone
	 * 			  Zone - The zone associated with the HSwipeEvent
	 * @param swipeType
	 * 			  int - The type of swipe -> -1 for down swipe, 1 for up swipe
	 */
	public void fireVSwipeEvent(int x, int y, long[] cursors, Zone zone, int swipeType) {
		TuioCursor t = TouchClient.getTuioClient().getTuioCursor(cursors[0]);
		zone.setLastVSwipeCursor(t.getSessionID());

		VSwipeEvent e = new VSwipeEvent(x, y, cursors, swipeType);
		if (vSwipeEvent != null) {
			try {
				// fire event on PApplet
				vSwipeEvent.invoke(parent, e);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (!e.handled()) {
			// fire event on zones
			//for (int i = zoneList.size() - 1; i >= 0; i--) {
			if (/*
			 * zoneList.get(i).contains(x, y) &&
			 */zone.isVSwipeable()) {
				if(TouchClient.debugMode) {
					System.out.println("Firing V Swipe: " + zone.toString());
				}

				zone.vSwipeEvent(e);
			}
			if (e.handled()) {
				return;
			}

			//}
		}
	}

	/**
	 * Fires a tap event. First fires the event in the Processing PApplet if the
	 * student has implemented the method. If the event has not been handled
	 * then it goes through the zone list until it has been handled.
	 * 
	 * @param x
	 *            int - The x-coordinate of the event
	 * @param y
	 *            int - The y-coordinate of the event
	 * @param cursors
	 *            long[] - The cursors involved in the event (their session IDs)
	 * @param taps
	 *            int - The number of taps
	 */
	public void fireTapEvent(int x, int y, long[] cursors, int taps, Zone zone) {
		TapEvent e = new TapEvent(x, y, cursors, taps);
		if (tapEvent != null) {
			try {
				// fire event on PApplet
				tapEvent.invoke(parent, e);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if(TouchClient.debugMode) {
			System.out.println("About to Fire tap: " + zone.toString());
		}
		if (!e.handled()) {
			if(TouchClient.debugMode) {
				System.out.println("Firing tap: " + zone.toString());
			}
				zone.tapEvent(e);
				

		}
	}

	/**
	 * Fires a tap and hold event. First fires the event in the Processing
	 * PApplet if the student has implemented the method. If the event has not
	 * been handled then it goes through the zone list until it has been
	 * handled.
	 * 
	 * @param x
	 *            int - The x-coordinate of the event
	 * @param y
	 *            int - The y-coordinate of the event
	 * @param cursors
	 *            long[] - The cursors involved in the event (their session IDs)
	 * @param taps
	 *            int - The number of taps
	 */
	public void fireTapAndHoldEvent(int x, int y, long[] cursors, long time, Zone zone) {
		TapAndHoldEvent e = new TapAndHoldEvent(x, y, cursors, time);
		if (tapAndHoldEvent != null) {
			try {
				// fire event on PApplet
				tapAndHoldEvent.invoke(parent, e);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (!e.handled()) {
			// fire event on zones
			//for (int i = zoneList.size() - 1; i >= 0; i--) {
			if (/*
			 * zoneList.get(i).contains(x, y) &&
			 */zone.isTapAndHoldable()) {
				zone.tapAndHoldEvent(e);
			}
			if (e.handled()) {
				return;
			}

			//}
		}
	}

	/**
	 * Calculates and returns the angle between the three points in Radians.
	 * 
	 * @param a
	 *            Point.Float - First Point
	 * @param b
	 *            Point.Float - Second Point
	 * @param c
	 *            Point.Float - Third Point
	 * @return float The angle between the three points in Radians
	 */
	public static float getAngleABC(Point.Float a, Point.Float b, Point.Float c) {
		Point.Float ab = new Point.Float(b.x - a.x, b.y - a.y);
		Point.Float cb = new Point.Float(b.x - c.x, b.y - c.y);
		float angba = (float) Math.atan2(ab.y, ab.x);
		float angbc = (float) Math.atan2(cb.y, cb.x);
		return angba - angbc;

	}

}
