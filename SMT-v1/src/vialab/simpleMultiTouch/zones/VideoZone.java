
package vialab.simpleMultiTouch.zones;



public class VideoZone extends RectZone {

	public VideoZone(float x, float y, float width, float height) {
		super(x, y, width, height);
		// TODO
		// Uncomment the rest of the class
		// Cannot find processing.video for some reason, thus I cannot implement this class!
	}
	/*public Movie video;
	public boolean loop = false;
	
	public VideoZone(PApplet applet, String fileName, int fps, float x, float y, float width, float height){
		super(x, y, width, height);
		this.video = new Movie(applet, fileName, fps);
	}
	
	public VideoZone(PApplet applet, URL url, float fps, float x, float y, float width, float height){
		super(x, y, width, height);
		this.video = new Movie(applet, url, fps);
	}
	
	public VideoZone(PApplet applet, String fileName, float x, float y, float width, float height){
		super(x, y, width, height);
		this.video = new Movie(applet, fileName);
	}
	
	public VideoZone(PApplet applet, URL url, float x, float y, float width, float height){
		super(x, y, width, height);
		this.video = new Movie(applet, url);
	}
	
	

	
	public void drawZone(){
		if(video.available()) {
		    video.read();
		}
		TouchClient.parent.image(video, this.getX(), this.getY(), this.width, this.height);	
	}
	
	public void setLoop(boolean loop){
		if(loop){
			video.loop();
		} else {
			video.noLoop();
		}
	}
	
	public void setFrameRate(int fr){
		video.frameRate(fr);
	}

	// Called every time a new frame is available to read
	//public void movieEvent(Movie m) {
	//  m.read();
	//}*/
}
