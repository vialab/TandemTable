package vialab.simpleMultiTouch.zones;

import org.jdesktop.animation.timing.TimingTargetAdapter;


///Use with the timing framework
public abstract class ZoneAnimator extends TimingTargetAdapter {



	boolean controlled = false, controllable = false;

	//for translating the zone
	public float animPosX = 0;
	public float animPosY = 0;
	//  for changing the zones dimensions
	protected float animWidth = 0;
	protected float animHeight = 0;

	private float startx = 0;

	private float endx = 0;

	private float starty = 0;

	private float endy = 0;


	Zone timingZone;
	
	public ZoneAnimator(){	}

	public boolean isControllable() {
		return controllable;
	}

	public void setControllable(boolean controllable) {
		this.controllable = controllable;
	}

	public float getStartx() {
		return startx;
	}

	public void setStartx(float startx) {
		this.startx = startx;
	}

	public float getEndx() {
		return endx;
	}

	public void setEndx(float endx) {
		this.endx = endx;
	}

	public float getEndy() {
		return endy;
	}

	public void setEndy(float endy) {
		this.endy = endy;
	}

	public float getStarty() {
		return starty;
	}

	public void setStarty(float starty) {
		this.starty = starty;
	}

	public void timingEvent(float fraction) {
		// Simple linear interpolation to find current position
		float currentX = 0; 
		float currentY = 0;

			currentX = (getStartx() + (getEndx() - getStartx()) * fraction);
			currentY = (getStarty() + (getEndy() - getStarty()) * fraction);
		
		timingZone.setXY(currentX, currentY);
	}

	// Used with the timing framework for animation - for translating the zone
	public float getAnimPosX(){
		return this.animPosX;
	}

	public float getAnimPosY(){
		return this.animPosY;
	}


	public void setAnimPosX(float x){
		float num = x-getAnimPosX();
		timingZone.setX(timingZone.getX() + num);
		this.animPosX = x;

	}

	public void setAnimPosY(float y){
		float num = y-getAnimPosY();
		timingZone.setY(timingZone.getY() + num);
		this.animPosY = y;

	}

	// Used with the timing framework for animation - for changing the zones dimensions
	public float getAnimWidth(){
		return this.animWidth;
	}

	public float getAnimHeight(){
		return this.animHeight;
	}


	public void setAnimWidth(float w){
		float num = w-getAnimWidth();
		timingZone.setWidth(timingZone.getWidth() + num);
		this.animWidth = w;

	}

	public void setAnimHeight(float h){
		float num = h-getAnimHeight();
		timingZone.setHeight(timingZone.getHeight() + num);
		this.animHeight = h;

	}
}
