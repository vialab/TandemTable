/*
 * Modified version of the TUIO processing library - part of the reacTIVision
 * project http://reactivision.sourceforge.net/
 * 
 * Copyright (c) 2005-2009 Martin Kaltenbrunner <mkalten@iua.upf.edu>
 * 
 * This version Copyright (c) 2011 Erik Paluka, Christopher Collins - University
 * of Ontario Institute of Technology Mark Hancock - University of Waterloo
 * contact: christopher.collins@uoit.ca
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

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Vector;

import processing.core.PApplet;
import vialab.mouseToTUIO.MouseToTUIO;
import vialab.simpleMultiTouch.zones.Zone;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioObject;
import TUIO.TuioPoint;

/**
 * The TUIO Processing client.
 * 
 * University of Ontario Institute of Technology. Summer Research Assistant with
 * Dr. Christopher Collins (Summer 2011) collaborating with Dr. Mark Hancock.
 * <P>
 * 
 * @author Erik Paluka
 * @date Summer, 2011
 * @version 1.0
 */
public class TouchClient {
	/** Processing PApplet */
	static PApplet pApplet;
	/** Gesture Handler */
	static GestureHandler handler;

	/** Tuio Client that listens for Tuio Messages via port 3333 UDP */
	private static TuioClient tuioClient;

	/** The main zone list */
	private static Vector<Zone> zoneList = new Vector<Zone>();

	/** Flag for drawing touch points */
	static boolean drawTouchPoints = true;
	/** Flag for drawing touch paths */
	static boolean drawTouchPath = false;
	static HashMap<TuioCursor, int[]> colours = new HashMap<TuioCursor, int[]>();
	static int size;

	/** Flag for applying the zone's matrix to the touch input **/
	static boolean applyZoneMatrix = false;
	
	static boolean debugMode = false;

	public static ZonePicker picker = new ZonePicker();

	/**
	 * Default Constructor. Default port is 3333 for TUIO
	 * 
	 * @param pApplet
	 *            PApplet - The Processing PApplet
	 */
	public TouchClient(PApplet pApplet) {
		this(pApplet, 3333);
	}

	/**
	 * Constructor. Allows you to set the port to connect to.
	 * 
	 * @param pApplet
	 *            PApplet - The Processing PApplet
	 * @param port
	 *            int - The port to connect to.
	 */
	public TouchClient(PApplet pApplet, int port) {
		this(pApplet, port, true, false);
	}

	public TouchClient(PApplet pApplet, boolean emulateTouches) {
		this(pApplet, emulateTouches, false);
	}

	public TouchClient(PApplet pApplet, boolean emulateTouches, boolean fullscreen) {
		this(pApplet, 3333, emulateTouches, fullscreen);
	}

	public TouchClient(PApplet pApplet, int port, boolean emulateTouches, boolean fullscreen) {
		size = pApplet.getHeight()/20;
		
		pApplet.setLayout(new BorderLayout());

		if (emulateTouches) {
			pApplet.add(new MouseToTUIO(pApplet.getWidth(), pApplet.getHeight()));
		}

		pApplet.frame.removeNotify();
		if (fullscreen) {
			pApplet.frame.setUndecorated(true);
			pApplet.frame.setIgnoreRepaint(true);
			pApplet.frame.setExtendedState(Frame.MAXIMIZED_BOTH);

			GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice displayDevice = environment.getDefaultScreenDevice();

			DisplayMode mode = displayDevice.getDisplayMode();
			Rectangle fullScreenRect = new Rectangle(0, 0, mode.getWidth(), mode.getHeight());
			pApplet.frame.setBounds(fullScreenRect);
			pApplet.frame.setVisible(true);

			// the following is exclusive mode
			//	        displayDevice.setFullScreenWindow(pApplet.frame);
			//
			//			Rectangle fullScreenRect = pApplet.frame.getBounds();

			pApplet.frame.setBounds(fullScreenRect);
			pApplet.setBounds((fullScreenRect.width - pApplet.width) / 2,
					(fullScreenRect.height - pApplet.height) / 2, pApplet.width, pApplet.height);
		}
		pApplet.frame.addNotify();
		pApplet.frame.toFront();

		TouchClient.pApplet = pApplet;
		pApplet.registerDispose(this);
		pApplet.registerDraw(this);
		pApplet.registerPre(this);
		handler = new GestureHandler();

		setTuioClient(new TuioClient(port));

		SimpleTuioListener listener = new SimpleTuioListener(handler, picker);
		getTuioClient().addTuioListener(listener);
		//		tuioClient.addTuioListener(this);
		getTuioClient().connect();
	}

	
	/**
	 * Set the debug mode on or off
	 * 
	 * @param debug
	 */
	public void setDebugMode(boolean debug){
		TouchClient.debugMode = debug;
	}
	
	/**
	 * Returns the debug mode status
	 * @return
	 */
	public static boolean getDebugMode(){
		return debugMode;
	}
	/**
	 * Returns the list of zones.
	 * 
	 * @return zoneList
	 */
	public Vector<Zone> getZones() {
		return getZoneList();
	}
	
	/**
	 * 	Sets the size of the touch points that get drawn. (int)
	 * @param size - int - touch points size for drawing
	 */
	public void setDrawTouchPointsSize(int size){
		TouchClient.size = size;
	}
	/**
	 * Sets the flag for drawing touch points in the PApplet. Draws the touch
	 * points if flag is set to true.
	 * 
	 * @param drawTouchPoints
	 *            boolean - flag
	 */
	public void setDrawTouchPoints(boolean drawTouchPoints) {
		TouchClient.drawTouchPoints = drawTouchPoints;
	}
	
	/**
	 * Sets the flag for drawing touch paths in the PApplet. Draws the touch
	 * paths if flag is set to true.
	 * 
	 * @param drawTouchPath
	 *            boolean - flag
	 */
	public void setDrawTouchPath(boolean drawTouchPath){
		TouchClient.drawTouchPath = drawTouchPath;
	}

	/**
	 * Draws the touch points in the PApplet if flag is set to true.
	 */
	public static void drawTouchPoints() {
		Vector<TuioCursor> curs = getTuioClient().getTuioCursors();
		pApplet.strokeWeight(1);
		pApplet.noFill();
		
		if (curs.size() > 0) {
			for (int i = 0; i < curs.size(); i++) {
				
				if(colours.containsKey(curs.get(i))){
					pApplet.fill(colours.get(curs.get(i))[0], colours.get(curs.get(i))[1], colours.get(curs.get(i))[2]);
					pApplet.stroke(colours.get(curs.get(i))[0], colours.get(curs.get(i))[1], colours.get(curs.get(i))[2]);
				}
				pApplet.ellipse(curs.get(i).getScreenX(TouchClient.pApplet.width), curs.get(i)
						.getScreenY(pApplet.height), size, size);
				
				Vector<TuioPoint> path = curs.get(i).getPath();
				if (drawTouchPath && path.size() > 1) {
					for (int j = 1; j < path.size(); j++) {

						pApplet.stroke(255);
						pApplet.line(path.get(j).getScreenX(pApplet.width) - 0.5f, path.get(j)
								.getScreenY(pApplet.height) - 0.5f,
								path.get(j - 1).getScreenX(pApplet.width) - 0.5f, path.get(j - 1)
								.getScreenY(pApplet.height) - 0.5f);
						pApplet.ellipse(path.get(j).getScreenX(pApplet.width), path.get(j)
								.getScreenY(pApplet.height), 5, 5);
						pApplet.stroke(0);
						pApplet.line(path.get(j).getScreenX(pApplet.width) + 0.5f, path.get(j)
								.getScreenY(pApplet.height) + 0.5f,
								path.get(j - 1).getScreenX(pApplet.width) + 0.5f, path.get(j - 1)
								.getScreenY(pApplet.height) + 0.5f);
						pApplet.ellipse(path.get(j).getScreenX(pApplet.width), path.get(j)
								.getScreenY(pApplet.height), 7, 7);
					}
				}
			}
		}
	}
	
	

	/**
	 * Gets the PApplet
	 * @return
	 */
	public static PApplet getPApplet(){
		return pApplet;
	}
	
	/**
	 * Sets the PApplet
	 * @param applet
	 */
	public void setPApplet(PApplet applet){
		pApplet = applet;
	}
	/**
	 * Adds a zone to the zone list. When a student creates a zone, they must
	 * add it to this list.
	 * 
	 * @param zone
	 *            Zone - The zone to add to the list.
	 */
	public void addZone(Zone zone) {
		synchronized(getZoneList()){
			getZoneList().add(zone);
		}
		
		/*synchronized(picker){
			picker.add(zone);
		}*/
	}



	/**
	 * Removes a zone from the zone list.
	 * 
	 * @param zone
	 *            Zone - The zone to remove from the list.
	 */
	public void removeZone(Zone zone) {
		synchronized(getZoneList()){
			getZoneList().remove(zone);
		}
		
		/*synchronized(picker){
			picker.remove(zone);
		}*/
	}

	/**
	 * Pull a zone to the top layer.
	 */
	public void pullToTop(Zone zone){
		synchronized (getZoneList()) {
			int i = getZoneList().indexOf(zone);
			if (i > 0) {
				getZoneList().remove(i);
				getZoneList().add(zone);
			}
		}

		synchronized (picker.getZoneList()) {
			int i = picker.getZoneList().indexOf(zone);
			if (i > 0) {
				picker.getZoneList().remove(i);
				picker.getZoneList().add(zone);
			}
		}
	}
	/**
	 * Push a zone to the bottom layer.
	 */
	public void pushToBottom(Zone zone){

		synchronized (getZoneList()) {
			int i = getZoneList().indexOf(zone);
			if (i > 0) {
				getZoneList().remove(i);
				getZoneList().add(0, zone);
			}
		}


		synchronized (picker.getZoneList()) {
			int i = picker.getZoneList().indexOf(zone);
			if (i > 0) {
				picker.getZoneList().remove(i);
				picker.getZoneList().add(0, zone);
			}
		}
	}


	/**
	 * 	Apply the zone's matrix to the touch input
	 * @param flag - boolean
	 */
	public void applyZonesMatrix(boolean flag){
		TouchClient.applyZoneMatrix = flag;
	}



	/**
	 * Performs the drawing of the zones in order. Zone on top-most layer gets
	 * drawn last. Goes through the list on zones, pushes the current
	 * transformation matrix, applies the zone's matrix, draws the zone, pops
	 * the matrix, and when at the end of the list, it draws the touch points.
	 */
	public synchronized void draw() {
		synchronized (getZoneList()) {
			for (Zone zone : getZoneList()) {
				if (zone != null && zone.isActive()) {
					TouchClient.pApplet.pushMatrix();
					zone.preDraw();
					zone.drawZone();
					zone.postDraw();
					TouchClient.pApplet.popMatrix();
				}
			}
		}

		if (drawTouchPoints) {
			drawTouchPoints();
		}
	}

	/**
	 * Returns a vector containing all the current TuioObjects.
	 * 
	 * @return Vector<TuioObject>
	 */
	public Vector<TuioObject> getTuioObjects() {
		return getTuioClient().getTuioObjects();
	}

	/**
	 * Returns a vector containing all the current Touches(TuioCursors).
	 * 
	 * @return Vector<TuioCursor>
	 */
	public static Touch[] getTouches() {
		Vector<TuioCursor> cursors = getTuioClient().getTuioCursors();
		Touch[] touches = new Touch[cursors.size()];
		int i = 0;
		for (TuioCursor c : cursors) {
			touches[i++] = new Touch(c);
		}
		return touches;
	}

	/**
	 * Returns a the TuioObject associated with the session ID
	 * 
	 * @param s_id
	 *            long - Session ID of the TuioObject
	 * @return TuioObject
	 */
	public TuioObject getTuioObject(long s_id) {
		return getTuioClient().getTuioObject(s_id);
	}

	/**
	 * Returns the Touch(TuioCursor) associated with the session ID
	 * 
	 * @param s_id
	 *            long - Session ID of the Touch(TuioCursor)
	 * @return TuioCursor
	 */
	public static Touch getTouch(long s_id) {
		return new Touch(getTuioClient().getTuioCursor(s_id));
	}

	/**
	 * Returns the number of current Touches (TuioCursors)
	 * 
	 * @return number of current touches
	 */
	public int getTouchCount() {
		return getTuioClient().getTuioCursors().size();
	}

	/**
	 * Manipulates the zone's position if it is throw-able after it has been
	 * released by a finger (cursor) Done before each call to draw. Uses the
	 * zone's x and y friction values.
	 */
	public void pre() {
		// TODO: I really don't think we should be doing anything with zones in TouchClient
		// This seems a lot more like event handling than drawing...
		/*for (int i = 0; i < zoneList.size(); i++) {
			Zone zone = zoneList.get(i);
			if (zone != null && zone.active) {
				if (zone.throwable && zone.getNumIds() > 0 && (zone.vx != 0 || zone.vy != 0)) {
					float vratio = zone.vy / zone.vx;
					if (zone.vx > pApplet.width) {
						zone.vx = pApplet.width;
						zone.vy = (int) (vratio * pApplet.width);
					}// governor
					if (zone.vy > pApplet.height) {
						zone.vy = pApplet.height;
						zone.vx = (int) (pApplet.width / vratio);
					}
					float ax = -zone.frictionX, ay = -zone.frictionY;// deceleration
					if (zone.vy == 0) {
						ay = 0;
					}
					else {
						float ratio = zone.vx / zone.vy;
						ay = -PApplet.sqrt(ax * ax / ((ratio * ratio) + 1)) * PApplet.abs(zone.vy)
								/ zone.vy;
						ax = ratio * ay;
					}

					float t = (pApplet.millis() - zone.releaseTime) / 10000f;
					float movementX = zone.vx * t + 0.5f * ax * PApplet.pow(t, 2);
					float movementY = zone.vy * t + 0.5f * ay * PApplet.pow(t, 2);
					// Get the movement in the zone's matrix space
					// Need to do it when zone has been rotated
					zone.contains(movementX - zone.lastXMovement, movementY - zone.lastYMovement);

					mTest.reset();
					mTest.apply(zone.matrix);
					PVector a = new PVector(zone.x, zone.y);
					PVector tA = new PVector();
					PVector b = new PVector(zone.x + zone.width, zone.y);
					PVector tB = new PVector();
					PVector c = new PVector(zone.x, zone.y + zone.height);
					PVector tC = new PVector();
					PVector d = new PVector(zone.x + zone.width, zone.y + zone.height);
					PVector tD = new PVector();
					mTest.mult(a, tA);
					mTest.mult(b, tB);
					mTest.mult(c, tC);
					mTest.mult(d, tD);

					if (PApplet.abs(ax * t) >= PApplet.abs(zone.vx)
							|| PApplet.abs(ay * t) >= PApplet.abs(zone.vy) || zone.border == true
							&& (tA.x < 0 || tA.x > TouchClient.pApplet.width) || tA.y < 0
							|| tA.y > TouchClient.pApplet.height || tB.x < 0
							|| tB.x > TouchClient.pApplet.width || tB.y < 0
							|| tB.y > TouchClient.pApplet.height || tC.x < 0
							|| tC.x > TouchClient.pApplet.width || tC.y < 0
							|| tC.y > TouchClient.pApplet.height || tD.x < 0
							|| tD.x > TouchClient.pApplet.width || tD.y < 0
							|| tD.y > TouchClient.pApplet.height) {

						// Finish the movement if friction multiplied by time is
						// bigger than the zone's velocity
						// Or if one of the corners of the zone goes off the
						// screen
						ax = zone.vx = 0;
						ay = zone.vy = 0;
						zone.lastXMovement = 0;
						zone.lastYMovement = 0;
						// Change children
						for (Zone z : zone.childList) {
							z.vx = 0;
							z.vy = 0;
							z.lastXMovement = 0;
							z.lastYMovement = 0;
						}
						// Change Group
						for (Zone z : zoneList) {
							if (z != zone && z.group != null
									&& z.group.equalsIgnoreCase(zone.group)) {
								z.vx = 0;
								z.vy = 0;
								z.lastXMovement = 0;
								z.lastYMovement = 0;
							}
						}

					}
					else {
						// Move the zone
						zone.matrix.translate(zone.localX - zone.lastLocalX, zone.localY
								- zone.lastLocalY);
						zone.changed = true;
						zone.lastXMovement = movementX;
						zone.lastYMovement = movementY;

						// Change children
						for (Zone z : zone.childList) {
							z.matrix.translate(zone.localX - zone.lastLocalX, zone.localY
									- zone.lastLocalY);
							z.changed = true;
							z.lastXMovement = movementX;
							z.lastYMovement = movementY;
						}
						// Change Group
						for (Zone z : zoneList) {
							if (z != zone && z.group != null
									&& z.group.equalsIgnoreCase(zone.group)) {
								z.matrix.translate(zone.localX - zone.lastLocalX, zone.localY
										- zone.lastLocalY);
								z.changed = true;
								z.lastXMovement = movementX;
								z.lastYMovement = movementY;
							}
						}
					}

				}
			}

		}*/
	}

	/**
	 * Disconnects the TuioClient when the PApplet is stopped. Shuts down any
	 * threads, disconnect from the net, unload memory, etc.
	 */
	public void dispose() {
		if (getTuioClient().isConnected()) {
			getTuioClient().disconnect();
		}
	}

	/**
	 * Runs a server that sends TUIO events using Windows 7 Touch events
	 * 
	 * @param touch2TuioExePath
	 *            String - the full name (including path) of the exe of
	 *            Touch2Tuio
	 * @see <a href='http://dm.tzi.de/touch2tuio/'>Touch2Tuio</a>
	 */
	public void runWinTouchTuioServer(String touch2TuioExePath) {
		final String tuioServerCommand = touch2TuioExePath + " " + pApplet.frame.getTitle();

		Thread serverThread = new Thread() {

			@Override
			public void run() {
				while (true) {
					try {
						Process tuioServer = Runtime.getRuntime().exec(tuioServerCommand);
						tuioServer.waitFor();
					}
					catch (Exception e) {
						System.err.println("TUIO Server stopped!");
					}
				}
			}
		};
		serverThread.start();
	}

	public static TuioClient getTuioClient() {
		return tuioClient;
	}

	public static void setTuioClient(TuioClient tuioClient) {
		TouchClient.tuioClient = tuioClient;
	}

	public static Vector<Zone> getZoneList() {
		return zoneList;
	}

	public static void setZoneList(Vector<Zone> zoneList) {
		TouchClient.zoneList = zoneList;
	}
}
