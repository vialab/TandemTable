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
import processing.core.PFont;
import processing.core.PImage;
import vialab.simpleMultiTouch.Touch;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.events.TapEvent;


public class DrawZone extends RectZone{
	static PApplet applet = TouchClient.getPApplet();
	TouchClient client;
	Vector<int[]> points;
	Vector<Vector<int[]>> points2;
	PImage saveArea;
	String saveDir = "";
	boolean saving = false;
	boolean saved = false;
	boolean curve;
	PFont font;
	
	int[] brushColour = {0, 0, 0};
	int brushWidth = 25;
	int brushHeight = 25;
	int filesCount;
	Zone colourZone, brushZone1, brushZone2, brushZone3, saveZone, undoZone, popUp, curveZone;

	
	/**
	 * DrawZone constructor, creates a rectangular zone which the user may draw into.
	 * 
	 * @param x int - X-coordinate of the upper left corner of the zone
	 * @param y int - Y-coordinate of the upper left corner of the zone
	 * @param width int - Width of the zone
	 * @param height int - Height of the zone
	 * @param colourPicker
	 * @param brushPicker
	 * @param save
	 * @param undo
	 */
	public DrawZone(int x, int y, int width, int height, PFont font, boolean colourPicker, boolean brushPicker, boolean save, boolean undo, TouchClient client){
		super(x, y, width, height);
		points = new Vector<int[]>();
		points2 = new Vector<Vector<int[]>>();
		this.client = client;
		this.font = font;
		
		createColourPicker();
		createBrushSizePicker();
		createSave();
		createUndo();
		createCurvePicker();
		
		setSaveZone(save);
		setUndoZone(undo);
		setColourPickerZone(colourPicker);
		setBrushPickerZone(brushPicker);

	}
	
	public void setCurvePickerZone(boolean flag){
		curveZone.setActive(false);
	}
	
	public void setColourPickerZone(boolean flag){
		colourZone.setActive(flag);
	}
	
	public void setSaveZone(boolean flag){
		saveZone.setActive(flag);
		popUp.setActive(flag);
	}
	
	public void setUndoZone(boolean flag){
		undoZone.setActive(flag);
	}
	
	public void setBrushPickerZone(boolean flag){
		brushZone1.setActive(flag);
		brushZone2.setActive(flag);
		brushZone3.setActive(flag);
	}

	public void setSaveDir(String dir){
		saveDir = dir;
	}
		
	public void drawZone(){
		if(!saving){
			
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
						applet.strokeWeight((n[5] + n[6])/2);
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
			
			int[] n = {tcur.getScreenX(applet.width), tcur.getScreenY(applet.height), brushColour[0], brushColour[1], brushColour[2], brushWidth, brushHeight};
			if(!curve){
				points.add(n);
			} else {
				Vector<int[]> p2 = new Vector<int[]>();
				p2.add(n);
				points2.add(p2);
			}
		} else {
			saving = false;
		}
		return true;
	}


	public boolean updateTouch(Touch tcur) {
		if(!saving && contains(tcur.x, tcur.y)){
			int[] n = {tcur.getScreenX(applet.width), tcur.getScreenY(applet.height), brushColour[0], brushColour[1], brushColour[2], brushWidth, brushHeight};
			if(!curve){
				points.add(n);
			} else {
				points2.get(points2.size()-1).add(n);
			}
		}
		return true;
	}
	
	/**
	 *  Creates a save button
	 */
	public void createSave(){
		popUp = new TextZone(getX(), getY(), getWidth(), getHeight(), font, "Saving... Click to continue!", getWidth()/10, "CENTER", "CENTER"){			
			public void drawZone(){
				if(saving){
					applet.fill(230, 55, 99);
					applet.rect(getX(), getY(), getWidth(), getHeight());
					super.drawZone();
				}
			}
		};
		
		saveZone = new TextZone(getX(), getY()-getHeight()/5, getWidth()/4, getHeight()/5, font, "Save", 16, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){
				if (isTappable()){

					//saveArea = applet.get(getX(), getY()+getHeight(), getWidth()*4, getHeight()*5);
					saveArea = getScreen((int)getX(), (int)(getY()+getHeight()), (int)getWidth()*4, (int)getHeight()*5);
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
					saveArea.save(path);
					saving = true;
					saved = true;
					e.setHandled(tappableHandled);
				}
			}
		};
		saveZone.setGestureEnabled("TAP", true, true);
		popUp.setDrawBorder(false);

		client.addZone(saveZone);
		client.addZone(popUp);
	}
	
	/**
	 * Creates a button to select to draw a curve or paint brush
	 */
	public void createCurvePicker(){
		curveZone = new TextZone(getX()+getWidth()/4, getY()-getHeight()/5, getWidth()/4, getHeight()/5, font, "Curve", 16, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){
				if (isTappable()){
					if(!curve){
						curve = true;
						this.setText("Paint Brush");
					} else {
						curve = false;
						this.setText("Curve");
					}
					e.setHandled(tappableHandled);
				}
			}
		};
		curveZone.setGestureEnabled("TAP", true, true);

		client.addZone(curveZone);
	}

	/**
	 *  Creates an undo button
	 */
	public void createUndo(){
		
		undoZone = new TextZone(getX()+getWidth()-getWidth()/4, getY()-getHeight()/5, getWidth()/4, getHeight()/5, font, "Erase", 16, "CENTER", "CENTER"){
			public void tapEvent(TapEvent e){
				if (isTappable()){
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
					e.setHandled(tappableHandled);
				}
			}
		};
		undoZone.setGestureEnabled("TAP", true, true);
		
		client.addZone(undoZone);
	}
	
	private void createColourPicker(){
		colourZone = new RectZone(x-width/2, y, width/2, height){
			public void drawZone(){
				//Row 1
				
				applet.fill(0, 0, 0);
				applet.stroke(0, 0, 0);
				applet.rect(getX(), getY(), getWidth()/5, getHeight()/4);
				
				applet.fill(127, 127, 127);
				applet.stroke(127, 127, 127);
				applet.rect(getX()+ getWidth()/5, getY(), getWidth()/5, getHeight()/4);
				
				applet.fill(136, 0, 21);
				applet.stroke(136, 0, 21);
				applet.rect(getX()+ 2*getWidth()/5, getY(), getWidth()/5, getHeight()/4);
				
				applet.fill(237, 28, 36);
				applet.stroke(237, 28, 36);
				applet.rect(getX()+ 3*getWidth()/5, getY(), getWidth()/5, getHeight()/4);
				
				applet.fill(255, 127, 39);
				applet.stroke(255, 127, 39);
				applet.rect(getX()+ 4*getWidth()/5, getY(), getWidth()/5, getHeight()/4);
				
				//Row 2
				
				applet.fill(255, 255, 255);
				applet.stroke(255, 255, 255);
				applet.rect(getX(), getY() + getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(195, 195, 195);
				applet.stroke(195, 195, 195);
				applet.rect(getX()+ getWidth()/5, getY() + getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(185, 122, 87);
				applet.stroke(185, 122, 87);
				applet.rect(getX()+ 2*getWidth()/5, getY() + getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(255, 174, 201);
				applet.stroke(255, 174, 201);
				applet.rect(getX()+ 3*getWidth()/5, getY() + getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(255, 201, 14);
				applet.stroke(255, 201, 14);
				applet.rect(getX()+ 4*getWidth()/5, getY() + getHeight()/4, getWidth()/5, getHeight()/4);
				
				//Row 3
				
				applet.fill(255, 242, 0);
				applet.stroke(255, 242, 0);
				applet.rect(getX(), getY() + 2*getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(34, 177, 76);
				applet.stroke(34, 177, 76);
				applet.rect(getX()+ getWidth()/5, getY() + 2*getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(0, 162, 232);
				applet.stroke(0, 162, 232);
				applet.rect(getX()+ 2*getWidth()/5, getY() + 2*getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(63, 72, 204);
				applet.stroke(63, 72, 204);
				applet.rect(getX()+ 3*getWidth()/5, getY() + 2*getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(163, 73, 164);
				applet.stroke(163, 73, 164);
				applet.rect(getX()+ 4*getWidth()/5, getY() + 2*getHeight()/4, getWidth()/5, getHeight()/4);
				
				//Row 4
				
				applet.fill(239, 228, 176);
				applet.stroke(239, 228, 176);
				applet.rect(getX(), getY() + 3*getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(181, 230, 29);
				applet.stroke(181, 230, 29);
				applet.rect(getX()+ getWidth()/5, getY() + 3*getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(153, 217, 234);
				applet.stroke(153, 217, 234);
				applet.rect(getX()+ 2*getWidth()/5, getY() + 3*getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(112, 146, 190);
				applet.stroke(112, 146, 190);
				applet.rect(getX()+ 3*getWidth()/5, getY() + 3*getHeight()/4, getWidth()/5, getHeight()/4);
				
				applet.fill(200, 191, 231);
				applet.stroke(200, 191, 231);
				applet.rect(getX()+ 4*getWidth()/5, getY() + 3*getHeight()/4, getWidth()/5, getHeight()/4);
			}
			
			public void tapEvent(TapEvent e){
				if (isTappable()){
					//Row one
					//black
					if(e.getX() < (colourZone.getX() + colourZone.getWidth()/5)
							&& e.getY() < (colourZone.getY() + colourZone.getHeight()/4)){
						brushColour = new int[]{0, 0, 0};

						//dark grey
					} else if(e.getX() > (colourZone.getX() + colourZone.getWidth()/5)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*2)
							&& e.getY() < (colourZone.getY() + colourZone.getHeight()/4)){
						brushColour = new int[]{127, 127, 127};

						//maroon
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*2)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*3)
							&& e.getY() < (colourZone.getY() + colourZone.getHeight()/4)){
						brushColour = new int[]{136, 0, 21};

						//red
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*3)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*4)
							&& e.getY() < (colourZone.getY() + colourZone.getHeight()/4)){
						brushColour = new int[]{237, 28, 36};

						//orange
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*4)
							&& e.getX() < (colourZone.getX() + colourZone.getWidth())
							&& e.getY() < (colourZone.getY() + colourZone.getHeight()/4)){
						brushColour = new int[]{255, 127, 39};
						
						//Row two
						//white
					} else if(e.getX() < (colourZone.getX() + colourZone.getWidth()/5)
							&& e.getY() > (colourZone.getY() + colourZone.getHeight()/4)
							&& e.getY() < (colourZone.getY() + (colourZone.getHeight()/4)*2)){
						brushColour = new int[]{255, 255, 255};

						//light grey
					} else if(e.getX() > (colourZone.getX() + colourZone.getWidth()/5)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*2)
							&& e.getY() > (colourZone.getY() + colourZone.getHeight()/4)
							&& e.getY() < (colourZone.getY() + (colourZone.getHeight()/4)*2)){
						brushColour = new int[]{195, 195, 195};

						//brown
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*2)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*3)
							&& e.getY() > (colourZone.getY() + colourZone.getHeight()/4)
							&& e.getY() < (colourZone.getY() + (colourZone.getHeight()/4)*2)){
						brushColour = new int[]{185, 122, 87};

						//pink
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*3)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*4)
							&& e.getY() > (colourZone.getY() + colourZone.getHeight()/4)
							&& e.getY() < (colourZone.getY() + (colourZone.getHeight()/4)*2)){
						brushColour = new int[]{255, 174, 201};

						//light orange
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*4)
							&& e.getX() < (colourZone.getX() + colourZone.getWidth())
							&& e.getY() > (colourZone.getY() + colourZone.getHeight()/4)
							&& e.getY() < (colourZone.getY() + (colourZone.getHeight()/4)*2)){
						brushColour = new int[]{255, 201, 14};
						
						//Row three
						//yellow
					} else if(e.getX() < (colourZone.getX() + colourZone.getWidth()/5)
							&& e.getY() > (colourZone.getY() + (colourZone.getHeight()/4)*2)
							&& e.getY() < (colourZone.getY() + (colourZone.getHeight()/4)*3)){
						brushColour = new int[]{255, 242, 0};

						//dark green
					} else if(e.getX() > (colourZone.getX() + colourZone.getWidth()/5)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*2)
							&& e.getY() > (colourZone.getY() + (colourZone.getHeight()/4)*2)
							&& e.getY() < (colourZone.getY() + (colourZone.getHeight()/4)*3)){
						brushColour = new int[]{34, 177, 76};

						//blue
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*2)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*3)
							&& e.getY() > (colourZone.getY() + (colourZone.getHeight()/4)*2)
							&& e.getY() < (colourZone.getY() + (colourZone.getHeight()/4)*3)){
						brushColour = new int[]{0, 162, 232};

						//dark blue
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*3)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*4)
							&& e.getY() > (colourZone.getY() + (colourZone.getHeight()/4)*2)
							&& e.getY() < (colourZone.getY() + (colourZone.getHeight()/4)*3)){
						brushColour = new int[]{63, 72, 204};

						//purple
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*4)
							&& e.getX() < (colourZone.getX() + colourZone.getWidth())
							&& e.getY() > (colourZone.getY() + (colourZone.getHeight()/4)*2)
							&& e.getY() < (colourZone.getY() + (colourZone.getHeight()/4)*3)){
						brushColour = new int[]{163, 73, 164};

						//Row four
						//cream
					} else if(e.getX() < (colourZone.getX() + colourZone.getWidth()/5)
							&& e.getY() > (colourZone.getY() + (colourZone.getHeight()/4)*3)
							&& e.getY() < (colourZone.getY() + colourZone.getHeight())){
						brushColour = new int[]{239, 228, 176};

						//light green
					} else if(e.getX() > (colourZone.getX() + colourZone.getWidth()/5)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*2)
							&& e.getY() > (colourZone.getY() + (colourZone.getHeight()/4)*3)
							&& e.getY() < (colourZone.getY() + colourZone.getHeight())){
						brushColour = new int[]{181, 230, 29};

						//light blue
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*2)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*3)
							&& e.getY() > (colourZone.getY() + (colourZone.getHeight()/4)*3)
							&& e.getY() < (colourZone.getY() + colourZone.getHeight())){
						brushColour = new int[]{153, 217, 234};

						//purple blue
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*3)
							&& e.getX() < (colourZone.getX() + (colourZone.getWidth()/5)*4)
							&& e.getY() > (colourZone.getY() + (colourZone.getHeight()/4)*3)
							&& e.getY() < (colourZone.getY() + colourZone.getHeight())){
						brushColour = new int[]{112, 146, 190};

						//light purple
					} else if(e.getX() > (colourZone.getX() + (colourZone.getWidth()/5)*4)
							&& e.getX() < (colourZone.getX() + colourZone.getWidth())
							&& e.getY() > (colourZone.getY() + (colourZone.getHeight()/4)*3)
							&& e.getY() < (colourZone.getY() + colourZone.getHeight())){
						brushColour = new int[]{200, 191, 231};

					}
					e.setHandled(tappableHandled);
				}
			}
		};
		colourZone.setGestureEnabled("Tap", true, true);
		colourZone.setDrawBorder(false);
		client.addZone(colourZone);
	}
	
	private void createBrushSizePicker(){
		brushZone1 = new SliderZone(getX() + getWidth(), getY() + 3*getHeight()/10, getWidth()/10, getHeight() - 3*getHeight()/10, 1, 30, brushWidth){
			public boolean addTouch(Touch t){
				super.addTouch(t);
				brushWidth = getValue();
				return true;
				
			}
			
			public boolean updateTouch(Touch t){
				super.updateTouch(t);
				brushWidth = getValue();
				return true;
				
			}
		};
		brushZone2 = new SliderZone(getX() + getWidth() + getWidth()/10, getY() + 3*getHeight()/10, getWidth()/10, getHeight() - 3*getHeight()/10, 1, 30, brushHeight){
			public boolean addTouch(Touch t){
				super.addTouch(t);
				brushHeight = getValue();
				return true;
				
			}
			
			public boolean updateTouch(Touch t){
				super.updateTouch(t);
				brushHeight = getValue();
				return true;
				
			}
		};
		
		//Displays the brush stroke
		brushZone3 = new RectZone(getX() + getWidth(), getY(), 2*getWidth()/10, 3*getHeight()/10){
			public void drawZone(){
				applet.fill(brushColour[0], brushColour[1], brushColour[2]);
				applet.stroke(brushColour[0], brushColour[1], brushColour[2]);
				applet.ellipse(getX() + getWidth()/2, getY() + getHeight()/2, brushWidth, brushHeight);
			}
		};
		brushZone1.setDrawBorder(false);
		client.addZone(brushZone1);
		brushZone2.setDrawBorder(false);
		client.addZone(brushZone2);
		brushZone3.setDrawBorder(false);
		client.addZone(brushZone3);
	}
	
	public boolean getSavedStatus(){
		return saved;
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
	
	public Zone getBrushZone1(){
		return brushZone1;
	}
	
	public Zone getBrushZone2(){
		return brushZone2;
	}
	
	public Zone getBrushZone3(){
		return brushZone3;
	}
	
	public Zone getSaveZone(){
		return saveZone;
	}

	public Zone getUndoZone(){
		return undoZone;
	}
	
	public Zone getPopUpZone(){
		return popUp;
	}
	
	public Zone getColourZone(){
		return colourZone;
	}
	
	public Zone getCurveZone(){
		return curveZone;
	}
	
	public Zone[] getZones(){
		Zone[] z = {curveZone, colourZone, popUp, undoZone, saveZone, brushZone3, brushZone2, brushZone1};
		return z;
		
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
		  Rectangle bounds = new Rectangle(x, y, width, height);
		  BufferedImage desktop = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		  try {
		    desktop = new Robot(gs[0]).createScreenCapture(bounds);
		  }
		  catch(AWTException e) {
		    System.err.println("Screen capture failed.");
		  }

		  return (new PImage(desktop));
	}
}

