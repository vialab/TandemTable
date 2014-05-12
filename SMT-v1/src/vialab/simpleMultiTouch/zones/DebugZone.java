package vialab.simpleMultiTouch.zones;

import processing.core.PFont;

public class DebugZone extends TextZone {
	
	public DebugZone(float x, float y, float width, float height, PFont font, float size){
		super(x, y, width, height, font, "", size, "LEFT", "TOP");
		this.setGestureEnabled("Drag", true);
	}
	
	
	public void drawZone(){
		String s = "";
		int index = 1;
		for(Zone z: zoneList){
			s += index + z.toString() + "\n";
			index++;
		}
		setText(s);
		super.drawZone();		
	}

}
