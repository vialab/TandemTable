package vialab.simpleMultiTouch.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import vialab.simpleMultiTouch.Touch;
import vialab.simpleMultiTouch.zones.Zone;
import TUIO.TuioCursor;

public abstract class MultiTouchEvent {
	private ArrayList<Touch> tuioCursors = new ArrayList<Touch>();
	
	private Zone zone;
	
	private boolean handled = false;

	public MultiTouchEvent(Touch... cursors) {
		this(null,cursors);
	}
	
	public MultiTouchEvent(Zone zone, Touch... cursors) {
		this.zone = zone;
		tuioCursors.addAll(Arrays.asList(cursors));
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public boolean isHandled() {
		return handled;
	}

	public void setHandled(boolean handled) {
		this.handled = handled;
	}

	public List<Touch> getTuioCursors() {
		return Collections.unmodifiableList(tuioCursors);
	}

	public TuioCursor getTuioCursor(int index) {
		return tuioCursors.get(index);
	}

	
	///////////////////////
	// abstract methods
	abstract public boolean detect();
}
