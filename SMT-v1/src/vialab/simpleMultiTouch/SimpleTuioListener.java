package vialab.simpleMultiTouch;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import processing.core.PApplet;
import processing.core.PVector;
import vialab.simpleMultiTouch.zones.Zone;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;

class SimpleTuioListener implements TuioListener {

	private PApplet applet;

	private Method addTouch, removeTouch, updateTouch;

	private Method addObject, removeObject, updateObject;

	private Method refresh;

	private ZonePicker picker;

	private GestureHandler handler;


	public SimpleTuioListener() {
		this(new GestureHandler(), new ZonePicker());
	}

	public SimpleTuioListener(GestureHandler handler) {
		this(handler, new ZonePicker());
	}

	public SimpleTuioListener(GestureHandler handler, ZonePicker picker) {
		super();
		this.picker = picker;
		this.handler = handler;

		this.applet = TouchClient.pApplet;
		retrieveMethods(applet);
	}

	@Override
	public void addTuioCursor(TuioCursor tcur) {
		int c1 = 0 + (int)(Math.random() * ((266 - 0) + 1));
		int c2 = 0 + (int)(Math.random() * ((266 - 0) + 1));
		int c3 = 0 + (int)(Math.random() * ((266 - 0) + 1));
		int[] c = {c1, c2, c3};
		
		TouchClient.colours.put(tcur, c);
		
		if(TouchClient.debugMode){
			System.out.println("addTuioCursor Session ID: " + tcur.getSessionID() + " x: " + tcur.getScreenX(applet.width) + " y: " + tcur.getScreenY(applet.height));
		}
		
		
		Touch touch = new Touch(tcur);
		boolean handled = false;
		if (addTouch != null) {
			try {
				// Invoke the parents addTuioCursor method
				handled = ((Boolean) addTouch.invoke(applet, new Object[] { touch }))
						.booleanValue();
			}
			catch (IllegalAccessException e) {}
			catch (IllegalArgumentException e) {}
			catch (InvocationTargetException e) {}
		}

		if (!handled) {
			int x = tcur.getScreenX(applet.width);
			int y = tcur.getScreenY(applet.height);
			
			Zone zone = picker.pick(tcur.getSessionID(), x, y, touch, true);

			if (zone != null && zone.isActive()) {
				if(TouchClient.debugMode){
					zone.setColour(new Color(0, 0, 0));
					System.out.println("addTuioCursor zone: " + zone.toString());
				}

				if (zone.getNumIds() == 0) {
					//if (zone.changed) {
						zone.getInverse().reset();
						zone.getInverse().apply(zone.getMatrix());
						zone.getInverse().invert();
						zone.setChanged(false);
					//}
					PVector world = new PVector();
					PVector mouse = new PVector(x, y);
					zone.getInverse().mult(mouse, world);

					
					zone.setLocalX(world.x);
					zone.setLocalY(world.y);
					
					
					zone.setLastLocalX(world.x);
					zone.setLastLocalY(world.y);
				}
				zone.addId(tcur.getSessionID());

				zone.getTouchList().add(touch);
				zone.getTuioCursorList().add(tcur);
				zone.setCurrentTouches(zone.getCurrentTouches() + 1);
				handler.detectOnAdd(zone, tcur, x, y);

				//********************************************* Used for applying the zone's matrix to the touch points

				if(TouchClient.applyZoneMatrix){
					float newX = x * zone.getMatrix().m00 + y * zone.getMatrix().m01 + 0 * zone.getMatrix().m02 + zone.getMatrix().m03;
					float newY = x * zone.getMatrix().m10 + y * zone.getMatrix().m11 + 0 * zone.getMatrix().m12 + zone.getMatrix().m13;

					TuioCursor t = new TuioCursor(tcur);
					t.update(newX/applet.width, newY/applet.height);
					touch = new Touch(t);
				}
				//**********************************************************************************************


				// Stop processing if touch handled
				if (zone.addTouch(touch)) {
					return;
				}
			}
		}

	}

	@Override
	public void updateTuioCursor(TuioCursor tcur) {
		
		if(TouchClient.debugMode){
			System.out.println("updateTuioCursor Session ID: " + tcur.getSessionID() + " x: " + tcur.getScreenX(applet.width) + " y: " + tcur.getScreenY(applet.height));
		}
		
		boolean handled = false;
		Touch touch = new Touch(tcur);
		if (updateTouch != null) {
			try {
				// Invoke the parents updateTuioCursor method
				handled = ((Boolean) updateTouch.invoke(applet, new Object[] { new Touch(tcur) }))
						.booleanValue();
			}
			catch (IllegalAccessException e) {}
			catch (IllegalArgumentException e) {}
			catch (InvocationTargetException e) {}
		}
		if (!handled) {
			int y = tcur.getScreenY(applet.height);
			int x = tcur.getScreenX(applet.width);

			Zone zone = picker.pick(tcur.getSessionID(), x, y, touch, false);
	
			if (zone != null && zone.isActive()) {
				if(TouchClient.debugMode){
					zone.setColour(new Color(111, 111, 111));
					System.out.println("updateTuioCursor zone: " + zone.toString());
				}

				if (zone.getId(0) == tcur.getSessionID()) {
					//if (zone.changed) {
						zone.getInverse().reset();
						zone.getInverse().apply(zone.getMatrix());
						zone.getInverse().invert();
						zone.setChanged(false);
					//}
					PVector world = new PVector();
					PVector mouse = new PVector(x, y);
					zone.getInverse().mult(mouse, world);
	
					
					zone.setLocalX(world.x);
					zone.setLocalY(world.y);
				}

				// This is to enable proper propagation of touches
				/*if (!zone.touchList.contains(touch) && zone.currentTouches == 0
						&& zone.getNumIds() == 0) {
					zone.addId(tcur.getSessionID());
					zone.touchList.add(touch);
					zone.tuioCursorList.add(tcur);
					zone.currentTouches++;
					zone.lastLocalX = zone.localX;
					zone.lastLocalY = zone.localY;
				}*/
				handler.detectOnUpdate(zone, tcur, x, y);

				//********************************************* Used for applying the zone's matrix to the touch points
				if(TouchClient.applyZoneMatrix){

					float newX = x * zone.getMatrix().m00 + y * zone.getMatrix().m01 + 0 * zone.getMatrix().m02 + zone.getMatrix().m03;
					float newY = x * zone.getMatrix().m10 + y * zone.getMatrix().m11 + 0 * zone.getMatrix().m12 + zone.getMatrix().m13;

					TuioCursor t = new TuioCursor(tcur);
					t.update(newX/applet.width, newY/applet.height);
					touch = new Touch(t);
				}
				//**********************************************************************************************


				// Add touch for the purpose of propagation
				// Stop processing if touch handled
				if (zone.updateTouch(touch)) {
					return;
				}
			}
		}

	}

	@Override
	public void removeTuioCursor(TuioCursor tcur) {
		TouchClient.colours.remove(tcur);
		if(TouchClient.debugMode){
			System.out.println("removeTuioCursor Session ID: " + tcur.getSessionID() + " x: " + tcur.getScreenX(applet.width) + " y: " + tcur.getScreenY(applet.height));
		}
		
		Touch touch = new Touch(tcur);
		boolean handled = false;
		if (removeTouch != null) {
			try {
				// Invoke the parents removeTuioCursor method
				handled = ((Boolean) removeTouch.invoke(applet, new Object[] { touch }))
						.booleanValue();
			}
			catch (IllegalAccessException e) {}
			catch (IllegalArgumentException e) {}
			catch (InvocationTargetException e) {}
		}
		if (!handled) {
			int x = tcur.getScreenX(applet.width);
			int y = tcur.getScreenY(applet.height);

			Zone zone = picker.pick(tcur.getSessionID(), x, y, touch, false);
			picker.removeMapping(tcur.getSessionID());

			if (zone != null && zone.isActive()) {
				if(TouchClient.debugMode){
					zone.setColour(new Color(0, 255, 0));
					System.out.println("removeTuioCursor zone: " + zone.toString());
				}

				// Detect gestures on remove
				handler.detectOnRemove(zone, tcur, x, y);
				// Reset 4 finger xyPinch distances
				if (zone.getCurrentTouches() == 4) {
					zone.setLastSclDist(1);
					zone.setLastSclXDist(1);
					zone.setLastSclYDist(1);
					zone.setXYPinching(false);
				}
				/*if (zone.getId(0) == tcur.getSessionID()){// || zone.getNumIds() == 1) {
					zone.reset();
					//picker.removeMapping(zone);
				} else {*/
					if (zone.getId(1) == tcur.getSessionID()) {		
						zone.setPinching(false);
						zone.setRotating(false);
						// Reset lastLocalX and lastLocalY for dragging
						// It jumps otherwise
						//TuioCursor cursor = TouchClient.getTouch(zone.getId(0));
						///zone.contains(cursor.getScreenX(applet.width), cursor.getScreenY(applet.height));
						//zone.lastLocalX = zone.localX;
						//zone.lastLocalY = zone.localY;
					} 
						zone.removeId(tcur.getSessionID());
						zone.getTouchList().remove(touch);
						zone.getTuioCursorList().remove(tcur);
						zone.setCurrentTouches(zone.getCurrentTouches() - 1);
					
				//}
				//picker.removeMapping(tcur.getSessionID());
				
				zone.setToggle(!zone.getToggle());

				//********************************************* Used for applying the zone's matrix to the touch points
				if(TouchClient.applyZoneMatrix){

					float newX = x * zone.getMatrix().m00 + y * zone.getMatrix().m01 + 0 * zone.getMatrix().m02 + zone.getMatrix().m03;
					float newY = x * zone.getMatrix().m10 + y * zone.getMatrix().m11 + 0 * zone.getMatrix().m12 + zone.getMatrix().m13;

					TuioCursor t = new TuioCursor(tcur);
					t.update(newX/applet.width, newY/applet.height);
					touch = new Touch(t);
				}
				//**********************************************************************************************

				// Stop processing if touch handled
				if(zone.removeTouch(touch)){
					return;
				}

			}
		}
		

	}

	@Override
	public void addTuioObject(TuioObject tobj) {
		if (addObject != null) {
			try {
				addObject.invoke(applet, new Object[] { tobj });
			}
			catch (IllegalAccessException e) {}
			catch (IllegalArgumentException e) {}
			catch (InvocationTargetException e) {}
		}
	}

	@Override
	public void updateTuioObject(TuioObject tobj) {
		if (updateObject != null) {
			try {
				updateObject.invoke(applet, new Object[] { tobj });
			}
			catch (IllegalAccessException e) {}
			catch (IllegalArgumentException e) {}
			catch (InvocationTargetException e) {}
		}
	}

	@Override
	public void removeTuioObject(TuioObject tobj) {
		if (removeObject != null) {
			try {
				removeObject.invoke(applet, new Object[] { tobj });
			}
			catch (IllegalAccessException e) {}
			catch (IllegalArgumentException e) {}
			catch (InvocationTargetException e) {}
		}
	}

	@Override
	public void refresh(TuioTime bundleTime) {
		if (refresh != null) {
			try {
				refresh.invoke(applet, new Object[] { bundleTime });
			}
			catch (IllegalAccessException e) {}
			catch (IllegalArgumentException e) {}
			catch (InvocationTargetException e) {}
		}
	}

	private void retrieveMethods(PApplet parent) {
		addTouch = SMTUtilities.getPMethod(parent, "addTouch", new Class[] { Touch.class });
		removeTouch = SMTUtilities.getPMethod(parent, "removeTouch", new Class[] { Touch.class });
		updateTouch = SMTUtilities.getPMethod(parent, "updateTouch", new Class[] { Touch.class });

		addObject = SMTUtilities.getPMethod(parent, "addObject", new Class[] {TuioObject.class});
		removeObject = SMTUtilities.getPMethod(parent, "removeObject", new Class[] {TuioObject.class});
		updateObject = SMTUtilities.getPMethod(parent, "updateObject", new Class[] {TuioObject.class});

		refresh = SMTUtilities.getPMethod(parent, "refresh", new Class[] { TuioTime.class });
	}
}
