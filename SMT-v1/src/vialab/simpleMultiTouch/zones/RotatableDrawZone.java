package vialab.simpleMultiTouch.zones;
import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import vialab.simpleMultiTouch.Touch;
import vialab.simpleMultiTouch.TouchClient;


public class RotatableDrawZone extends RectZone{
	PApplet applet;
	TouchClient client;
	Vector<int[]> points;
	Vector<Vector<int[]>> points2;
	PImage captureArea;
	/** Directory where the saved image will be placed */
	String saveDir = "";
	/** Image file has been saved */
	boolean saved = false;
	/** Image file is in the process of being saved (TODO: Does not reset) */
	boolean saving = false;
	/** Image has been captured */
	boolean captured = false;
	boolean curve = true;
	boolean flipped = false;

	PFont font;
	float rWidth = (this.getWidth()/4)/5;
	float rHeight = this.getHeight()/4;
	int[] tColour;
	float textSize;
	String text = "Brush";

	int[] brushColour = {0, 0, 0};
	int brushWidth = 10;
	int brushHeight = 10;
	int filesCount;

	
	/** Automatically save the image to the system */
	boolean autoSave = false;

	/**
	 * RotatableDrawZone constructor, creates a rectangular zone which the user may draw into.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param font
	 * @param colour
	 * @param textSize
	 * @param client
	 */
	public RotatableDrawZone(float x, float y, float width, float height, PFont font, int colour, float textSize, TouchClient client){
		super(x, y, width, height);
		points = new Vector<int[]>();
		points2 = new Vector<Vector<int[]>>();
		this.client = client;
		applet = TouchClient.getPApplet();
		this.font = font;
		tColour = new int[]{colour, colour, colour};
		this.textSize = textSize;

	}

	public void setBrushSize(int width, int height){
		brushWidth = width;
		brushHeight = height;
	}

	private void createPalette(){
		//Row 1

		applet.fill(0, 0, 0);
		applet.stroke(0, 0, 0);
		applet.rect(getX(), getY(), rWidth, rHeight);

		applet.fill(127, 127, 127);
		applet.stroke(127, 127, 127);
		applet.rect(getX()+ rWidth, getY(), rWidth, rHeight);

		applet.fill(136, 0, 21);
		applet.stroke(136, 0, 21);
		applet.rect(getX()+ 2*rWidth, getY(), rWidth, rHeight);

		applet.fill(237, 28, 36);
		applet.stroke(237, 28, 36);
		applet.rect(getX()+ 3*rWidth, getY(), rWidth, rHeight);

		applet.fill(255, 127, 39);
		applet.stroke(255, 127, 39);
		applet.rect(getX()+ 4*rWidth, getY(), rWidth, rHeight);

		//Row 2

		applet.fill(255, 255, 255);
		applet.stroke(255, 255, 255);
		applet.rect(getX(), getY() + rHeight, rWidth, rHeight);

		applet.fill(195, 195, 195);
		applet.stroke(195, 195, 195);
		applet.rect(getX()+ rWidth, getY() + rHeight, rWidth, rHeight);

		applet.fill(185, 122, 87);
		applet.stroke(185, 122, 87);
		applet.rect(getX()+ 2*rWidth, getY() + rHeight, rWidth, rHeight);

		applet.fill(255, 174, 201);
		applet.stroke(255, 174, 201);
		applet.rect(getX()+ 3*rWidth, getY() + rHeight, rWidth, rHeight);

		applet.fill(255, 201, 14);
		applet.stroke(255, 201, 14);
		applet.rect(getX()+ 4*rWidth, getY() + rHeight, rWidth, rHeight);

		//Row 3

		applet.fill(255, 242, 0);
		applet.stroke(255, 242, 0);
		applet.rect(getX(), getY() + 2*rHeight, rWidth, rHeight);

		applet.fill(34, 177, 76);
		applet.stroke(34, 177, 76);
		applet.rect(getX()+ rWidth, getY() + 2*rHeight, rWidth, rHeight);

		applet.fill(0, 162, 232);
		applet.stroke(0, 162, 232);
		applet.rect(getX()+ 2*rWidth, getY() + 2*rHeight, rWidth, rHeight);

		applet.fill(63, 72, 204);
		applet.stroke(63, 72, 204);
		applet.rect(getX()+ 3*rWidth, getY() + 2*rHeight, rWidth, rHeight);

		applet.fill(163, 73, 164);
		applet.stroke(163, 73, 164);
		applet.rect(getX()+ 4*rWidth, getY() + 2*rHeight, rWidth, rHeight);

		//Row 4

		applet.fill(239, 228, 176);
		applet.stroke(239, 228, 176);
		applet.rect(getX(), getY() + 3*rHeight, rWidth, rHeight);

		applet.fill(181, 230, 29);
		applet.stroke(181, 230, 29);
		applet.rect(getX()+ rWidth, getY() + 3*rHeight, rWidth, rHeight);

		applet.fill(153, 217, 234);
		applet.stroke(153, 217, 234);
		applet.rect(getX()+ 2*rWidth, getY() + 3*rHeight, rWidth, rHeight);

		applet.fill(112, 146, 190);
		applet.stroke(112, 146, 190);
		applet.rect(getX()+ 3*rWidth, getY() + 3*rHeight, rWidth, rHeight);

		applet.fill(200, 191, 231);
		applet.stroke(200, 191, 231);
		applet.rect(getX()+ 4*rWidth, getY() + 3*rHeight, rWidth, rHeight);
	}

	private void createSave(){
		applet.noFill();
		applet.stroke(0);
		applet.rect(getX() + this.getWidth()/4, getY(), this.getWidth()/4, rHeight);
		applet.textAlign(PConstants.CENTER, PConstants.CENTER);

		applet.fill(tColour[0], tColour[1], tColour[2]);
		applet.textFont(font, textSize);
		applet.text("Save", getX() + rWidth*5, getY(), this.getWidth()/4, rHeight);
	}

	private void createUndo(){
		applet.noFill();
		applet.stroke(0);
		applet.rect(getX() + 2*this.getWidth()/4, getY(), this.getWidth()/4, rHeight);
		applet.textAlign(PConstants.CENTER, PConstants.CENTER);

		applet.fill(tColour[0], tColour[1], tColour[2]);
		applet.textFont(font, textSize);
		applet.text("Undo", getX() + 2*this.getWidth()/4, getY(), this.getWidth()/4, rHeight);
	}

	private void createCurve(){
		applet.noFill();
		applet.stroke(0);
		applet.rect(getX() + 3*this.getWidth()/4, getY(), this.getWidth()/4, rHeight);
		applet.textAlign(PConstants.CENTER, PConstants.CENTER);

		applet.fill(tColour[0], tColour[1], tColour[2]);
		applet.textFont(font, textSize);
		applet.text(text, getX() + 3*this.getWidth()/4, getY(), this.getWidth()/4, rHeight);
	}

	public void drawZone(){

		if(!saving){
			createPalette();
			createSave();
			createUndo();
			createCurve();

			synchronized(points){
				for(int[] n : points){
					applet.fill(n[2], n[3], n[4]);
					applet.stroke(n[2], n[3], n[4]);
					applet.ellipse(n[0], n[1], n[5], n[6]);
				}
			}

			synchronized(points2){
				applet.noFill();

				for(Vector<int[]> v : points2){
					applet.beginShape();
					for(int[] n: v){
						applet.strokeWeight(10);
						applet.stroke(n[2], n[3], n[4]);
						applet.curveVertex(n[0], n[1]);
					}
					applet.endShape();
				}
			}
		}
	}

	public boolean addTouch(Touch tcur) {
		if(!saving){

			if(tcur.x > getX() + getWidth()/4 + brushWidth/2
					&& tcur.x < getX() + getWidth() - brushWidth/2
					&& tcur.y > getY() + rHeight + brushHeight/2
					&& tcur.y < getY() + getHeight() -brushHeight/2
					){
				int[] n = {tcur.getScreenX(applet.width), tcur.getScreenY(applet.height), brushColour[0], brushColour[1], brushColour[2], brushWidth, brushHeight};
				if(!curve){
					points.add(n);
				} else {
					Vector<int[]> p2 = new Vector<int[]>();
					p2.add(n);
					points2.add(p2);
				}
			} else if (tcur.x > getX() + getWidth()/4 && tcur.y < getY() + rHeight){
				if(tcur.x < getX() + 2*this.getWidth()/4){
					capture();
				} else if (tcur.x < getX() + 3*this.getWidth()/4){
					undo();
				} else {
					curve();
				}
			} else {
				touchPalette(tcur);
			}
		} else {
			saving = false;
		}
		return true;
	}


	public boolean updateTouch(Touch tcur) {
		if(!saving && contains(tcur.x, tcur.y)){
			if(tcur.x > getX() + getWidth()/4 + brushWidth/2
					&& tcur.x < getX() + getWidth() - brushWidth/2
					&& tcur.y > getY() + rHeight + brushHeight/2
					&& tcur.y < getY() + getHeight() -brushHeight/2
					){

				int[] n = {tcur.getScreenX(applet.width), tcur.getScreenY(applet.height), brushColour[0], brushColour[1], brushColour[2], brushWidth, brushHeight};
				if(!curve){
					points.add(n);
				} else {
					if(!points2.isEmpty()){
						points2.get(points2.size()-1).add(n);
					}
				}
			}
		}
		return true;
	}

	public void setSaveDir(String dir){
		saveDir = dir;
	}

	//The save area needs to change if the RotatabledrawZone has been flipped
	public void setFlipped(boolean b){
		flipped = b;
	}

	private void capture(){
		//saveArea = applet.get(getX(), getY()+getHeight(), getWidth()*4, getHeight()*5);
		if (flipped){
			this.rotate(PConstants.PI);
			//saveArea = getScreen(getX()+2 , getY()+2, getWidth()-getWidth()/4, getHeight()-rHeight);
		} 
		
		captureArea = getScreen((int)(getX() + getWidth()/4), (int)(getY() + rHeight), (int)(getWidth()-getWidth()/4), (int)(getHeight()-rHeight));
		captured = true;

		if (flipped){
			this.rotate(PConstants.PI);
		}

		if(autoSave){
			save();
		}
	}

	
	/**
	 * Sets if the image should be saved to the save
	 * location ('saveDir') automatically
	 * @param autoSaveFlag
	 */
	public void setAutoSave(boolean autoSaveFlag){
		autoSave = autoSaveFlag;
	}
	
	/**
	 * Gets the autoSave flag.
	 * It sets if the image should be saved to the save
	 * location ('saveDir') automatically
	 * @return
	 */
	public boolean getAutoSave(){
		return autoSave;
	}
	
	public void save(){
		boolean flag = false;
		int i = 0;
		String path;

		do {
			path = applet.dataPath(saveDir + "user" + i + ".png");
			File file = new File(path);
			if(file.exists()){
				i++;
			} else {
				flag = true;
			}
		} while(!flag);
		filesCount = i;
		captureArea.save(path);
		saving = true;
		saved = true;
	}
	private void undo(){
		if(!curve){
			if(!points.isEmpty()){
				//remove the last point
				points.remove(points.size()-1);
			}
		} else {
			if(!points2.isEmpty()){
				//remove the last point
				points2.remove(points2.size()-1);
			}
		}
	}

	private void curve(){
		if(!curve){
			curve = true;
			text ="Brush";
			//oldBrushWidth = brushWidth;
			//brushWidth = 10;
			//oldBrushHeight = brushHeight;
			//brushHeight = 10;
		} else {
			curve = false;
			text = "Curve";
			//brushWidth = oldBrushWidth;
			//brushHeight = oldBrushHeight;
		}
	}
	private void touchPalette(Touch tcur){
		//Row one
		//black
		if(tcur.x < (getX() + rWidth)
				&& tcur.y < (getY() + rHeight)){
			brushColour = new int[]{0, 0, 0};

			//dark grey
		} else if(tcur.x > (getX()+ rWidth)
				&& tcur.x < (getX()+ rWidth*2)
				&& tcur.y < (getY() + rHeight)){
			brushColour = new int[]{127, 127, 127};

			//maroon
		} else if(tcur.x > (getX()+ 2*rWidth)
				&& tcur.x < (getX()+ 3*rWidth)
				&& tcur.y < (getY() + rHeight)){
			brushColour = new int[]{136, 0, 21};

			//red
		} else if(tcur.x > (getX()+ 3*rWidth)
				&& tcur.x < (getX()+ 4*rWidth)
				&& tcur.y < (getY() + rHeight)){
			brushColour = new int[]{237, 28, 36};

			//orange
		} else if(tcur.x > (getX()+ 4*rWidth)
				&& tcur.x < (getX()+ 5*rWidth)
				&& tcur.y < (getY() + rHeight)){
			brushColour = new int[]{255, 127, 39};

			//Row two
			//white
		} else if(tcur.x < (getX() + rWidth)
				&& tcur.y > (getY() + rHeight)
				&& tcur.y < (getY() + 2*rHeight)){
			brushColour = new int[]{255, 255, 255};

			//light grey
		} else if(tcur.x > (getX() + rWidth)
				&& tcur.x < (getX() + 2*rWidth)
				&& tcur.y > (getY() + rHeight)
				&& tcur.y < (getY() + 2*rHeight)){
			brushColour = new int[]{195, 195, 195};

			//brown
		} else if(tcur.x > (getX()+ 2*rWidth)
				&& tcur.x < (getX()+ 3*rWidth)
				&& tcur.y > (getY() + rHeight)
				&& tcur.y < (getY() + 2*rHeight)){
			brushColour = new int[]{185, 122, 87};

			//pink
		} else if(tcur.x > (getX()+ 3*rWidth)
				&& tcur.x < (getX()+ 4*rWidth)
				&& tcur.y > (getY() + rHeight)
				&& tcur.y < (getY() + 2*rHeight)){
			brushColour = new int[]{255, 174, 201};

			//light orange
		} else if(tcur.x > (getX()+ 4*rWidth)
				&& tcur.x < (getX()+ 5*rWidth)
				&& tcur.y > (getY() + rHeight)
				&& tcur.y < (getY() + 2*rHeight)){
			brushColour = new int[]{255, 201, 14};

			//Row three
			//yellow
		} else if(tcur.x < (getX() + rWidth)
				&& tcur.y > (getY() + 2*rHeight)
				&& tcur.y < (getY() + 3*rHeight)){
			brushColour = new int[]{255, 242, 0};

			//dark green
		} else if(tcur.x > (getX() + rWidth)
				&& tcur.x < (getX() + 2*rWidth)
				&& tcur.y > (getY() + 2*rHeight)
				&& tcur.y < (getY() + 3*rHeight)){
			brushColour = new int[]{34, 177, 76};

			//blue
		} else if(tcur.x > (getX()+ 2*rWidth)
				&& tcur.x < (getX()+ 3*rWidth)
				&& tcur.y > (getY() + 2*rHeight)
				&& tcur.y < (getY() + 3*rHeight)){
			brushColour = new int[]{0, 162, 232};

			//dark blue
		} else if(tcur.x > (getX()+ 3*rWidth)
				&& tcur.x < (getX()+ 4*rWidth)
				&& tcur.y > (getY() + 2*rHeight)
				&& tcur.y < (getY() + 3*rHeight)){
			brushColour = new int[]{63, 72, 204};

			//purple
		} else if(tcur.x > (getX()+ 4*rWidth)
				&& tcur.x < (getX()+ 5*rWidth)
				&& tcur.y > (getY() + 2*rHeight)
				&& tcur.y < (getY() + 3*rHeight)){
			brushColour = new int[]{163, 73, 164};

			//Row four
			//cream
		} else if(tcur.x < (getX() + rWidth)
				&& tcur.y > (getY() + 3*rHeight)
				&& tcur.y < (getY() + 4*rHeight)){
			brushColour = new int[]{239, 228, 176};

			//light green
		} else if(tcur.x > (getX() + rWidth)
				&& tcur.x < (getX() + 2*rWidth)
				&& tcur.y > (getY() + 3*rHeight)
				&& tcur.y < (getY() + 4*rHeight)){
			brushColour = new int[]{181, 230, 29};

			//light blue
		} else if(tcur.x > (getX()+ 2*rWidth)
				&& tcur.x < (getX()+ 3*rWidth)
				&& tcur.y > (getY() + 3*rHeight)
				&& tcur.y < (getY() + 4*rHeight)){
			brushColour = new int[]{153, 217, 234};

			//purple blue
		} else if(tcur.x > (getX()+ 3*rWidth)
				&& tcur.x < (getX()+ 4*rWidth)
				&& tcur.y > (getY() + 3*rHeight)
				&& tcur.y < (getY() + 4*rHeight)){
			brushColour = new int[]{112, 146, 190};

			//light purple
		} else if(tcur.x > (getX()+ 4*rWidth)
				&& tcur.x < (getX()+ 5*rWidth)
				&& tcur.y > (getY() + 3*rHeight)
				&& tcur.y < (getY() + 4*rHeight)){
			brushColour = new int[]{200, 191, 231};

		}
	}

	/**
	 * Returns true if image has been saved
	 * @return
	 */
	public boolean getSavedStatus(){
		return saved;
	}

	/**
	 * Captured area encapsulated in a PImage
	 * @return
	 */
	public PImage getImage(){
		return captureArea;
	}
	
	/**
	 * Return true if the image has been captured
	 * and is in the PImage
	 * @return
	 */
	public boolean getCapturedStatus(){
		return captured;
	}
	/**
	 * Returns the number of files in the save directory
	 * **Warning**: Only works if the save zone has been tapped,
	 * 				otherwise it returns null.
	 * 
	 * @return filesCount - the number of files in save directory
	 */
	public int getFilesCount(){
		return filesCount;
	}
	/**
	 * Screen Capture method taken from http://forum.processing.org/topic/get-screencapture
	 * @param x
	 * @param y
	 * @param height
	 * @param width
	 * @return
	 */
	public PImage getScreen(int x, int y, int width, int height) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		//DisplayMode mode = gs[0].getDisplayMode();
		//Width and height are subtracted by 3 since Zone draws a border on the inside of the zone
		Rectangle bounds = new Rectangle(x, y, width-3, height-3);
		BufferedImage desktop = new BufferedImage(width-3, height-3, BufferedImage.TYPE_INT_RGB);

		try {
			desktop = new Robot(gs[0]).createScreenCapture(bounds);
		}
		catch(AWTException e) {
			System.err.println("Screen capture failed.");
		}

		return (new PImage(desktop));
	}
}

