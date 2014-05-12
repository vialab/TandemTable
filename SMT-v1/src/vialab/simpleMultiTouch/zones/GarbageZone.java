package vialab.simpleMultiTouch.zones;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import vialab.simpleMultiTouch.TouchClient;

public class GarbageZone extends RectZone{
	TouchClient client;
	PApplet applet;
	boolean image = false;
	PImage img;

	public GarbageZone(TouchClient client, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.client = client;

	}

	public GarbageZone(TouchClient client, float x, float y, float width, float height, PImage img) {
		super(x, y, width, height);
		this.client = client;
		this.applet = TouchClient.getPApplet();
		this.img = img;
		image = true;

	}

	public void drawZone(){
		super.drawZone();
		if(image){
			applet.image(img, getX(), getY(), getWidth(), getHeight());
		}
		synchronized(zoneList){
			for(int i = 0; i < zoneList.size(); i++){
				Zone zone = zoneList.get(i);
				if(zone != null && zone.active){
					if(zone != this){			
						if (zone.changed) {
							zone.getInverse().reset();
							zone.getInverse().apply(zone.matrix);
							zone.getInverse().invert();
							zone.changed = false;
						}
						PVector world = new PVector();
						PVector mouse = new PVector(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
						zone.getInverse().mult(mouse, world);

						if ((world.x > zone.getX()) && (world.x < zone.getX() + zone.width)
								&& (world.y > zone.getY()) && (world.y < zone.getY() + zone.height)) {
							
							zone.setActive(false);
							gbTrigger(zone);

						}

					}
				}
			}
		}

	}

	public void gbTrigger(Zone z){

	}
}
