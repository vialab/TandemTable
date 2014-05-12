/*
  Simple Multitouch Library
  Copyright 2011
  Erik Paluka, Christopher Collins - University of Ontario Institute of Technology
  Mark Hancock - University of Waterloo

  Parts of this library are based on:
  TUIOZones http://jlyst.com/tz/

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public
  License Version 3 as published by the Free Software Foundation.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */
package vialab.simpleMultiTouch.zones;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import vialab.simpleMultiTouch.TouchClient;

/**
 * This is a rectangular zone which displays an image.<P>
 * 
 * University of Ontario Institute of Technology.
 * Summer Research Assistant with Dr. Christopher Collins (Summer 2011) collaborating with Dr. Mark Hancock.<P>
 * 
 * @author  Erik Paluka 
 * @date  Summer, 2011
 * @version 1.0
 */
public class ImageZone extends RectZone {
	/**The PImage that contains the image passed by the user*/
	public PImage img;
	public PImage[] imgArray;
	boolean imageArray = false;
	boolean drawOnlyImg = true;
	boolean imgRotated = false;
	boolean filter = false;
	float imgRotateAmount = 0;
	int imgIndex = 0;
	int filterType = PConstants.GRAY;

	PImage filteredImg;

	/**
	 * ImageZone constructor, creates a rectangular zone and draws a PImage to it.
	 * 
	 * @param img PImage - The PImage that will be drawn to the zone's coordinates.
	 * @param x int - X-coordinate of the upper left corner of the zone
	 * @param y int - Y-coordinate of the upper left corner of the zone
	 * @param width int - Width of the zone
	 * @param height int - Height of the zone
	 */
	public ImageZone(PImage imgg, float xx, float yy, float width, float height){
		super(xx, yy, width, height);
		this.img = imgg;


	}

	/**
	 * ImageZone constructor, creates a rectangular zone and draws a PImage to it.
	 * Need to set the PImage after construction
	 * 
	 * @param x int - X-coordinate of the upper left corner of the zone
	 * @param y int - Y-coordinate of the upper left corner of the zone
	 * @param width int - Width of the zone
	 * @param height int - Height of the zone
	 */
	public ImageZone(float xx, float yy, float width, float height){
		super(xx, yy, width, height);
		this.img = null;


	}

	/**
	 * ImageZone constructor. Creates a rectangular zone and draws a PImage to it.
	 * The width and height of the zone is set to the PImage's width and height.
	 * 
	 * @param img PImage - The PImage that will be drawn to the zone's coordinates.
	 * @param x int - X-coordinate of the upper left corner of the zone
	 * @param y int - Y-coordinate of the upper left corner of the zone
	 */
	public ImageZone(PImage img, float x, float y){
		this(img, x, y, img.width, img.height);	
	}

	/**
	 * ImageZone constructor, creates a rectangular zone and draws a PImage to it.
	 * 
	 * @param img[] PImage - The PImage[] array that will be drawn to the zone's coordinates.
	 * @param x int - X-coordinate of the upper left corner of the zone
	 * @param y int - Y-coordinate of the upper left corner of the zone
	 * @param width int - Width of the zone
	 * @param height int - Height of the zone
	 */
	public ImageZone(PImage[] img, float x, float y, float width, float height){
		super(x, y, width, height);
		this.imgArray = img;
		imageArray = true;

	}


	public ImageZone(PImage[] img, float x, float y, float width, float height, float r){
		super(x, y, width, height, r);
		this.imgArray = img;
		imageArray = true;

	}

	public void setFilter(boolean flag){
		this.filter = flag;
	}

	public void setFilterType(int pConstant){
		this.filterType = pConstant;

		filteredImg = img.get();
		filteredImg.filter(filterType);
	}

	public void setFilterLvl(int pConstant, int numTimes){
		this.filterType = pConstant;

		filteredImg = img.get();

		//for(int i = 0; i < numTimes; i++){
		filteredImg.filter(filterType, numTimes);
		//}

	}


	/**
	 * Sets the PImage to draw to the ImageZone
	 * 
	 * @param img PImage
	 */
	public void setImage(PImage img){
		this.img = img;
	}

	/**
	 * Gets the PImage that draws to the ImageZone
	 * 
	 * @param img PImage
	 */
	public PImage getImage(){
		return this.img;
	}

	/**
	 * Sets the PImage to draw to the ImageZone
	 * 
	 * @param img PImage
	 */
	public void setImageArray(PImage[] img){
		this.imgArray = img;
	}

	/**
	 * Gets the PImage[]  that draws to the ImageZone
	 * 
	 * @param img PImage[]
	 */
	public PImage[] getImageArray(){
		return this.imgArray;
	}

	/**
	 * Sets if the ImageZone should call super.drawZone() or 
	 * only draw the PImage
	 * 
	 * @param flag - boolean
	 */
	public void setDrawOnlyImage(boolean flag){
		drawOnlyImg = flag;
	}

	public void setImageIndex(int index){
		this.imgIndex = index;
	}

	public int getImageIndex(){
		return this.imgIndex;
	}
	
	public void setImgRotateAmount(float radians){
		this.imgRotateAmount = radians;
	}

	public void setImgRotated(boolean flag){
		this.imgRotated = flag;
	}


	/**
	 * Draws the PImage to the zone's coordinates.
	 */
	public void drawZone(){

		if(!drawOnlyImg){
			super.drawZone();
		}

		if(imageArray && imgIndex >= 0 && imgIndex < imgArray.length){
			PImage img2 = imgArray[imgIndex];
			float x = this.getX() + this.getWidth()/2 - img2.width/2;
			float y = this.getY() + this.getHeight()/2 - img2.height/2;
			float w = img2.width;
			float h = img2.height;

			if(x < this.getX()){
				x = this.getX();
				w = this.getWidth();
			}

			if(y < this.getY()){
				y = this.getY();
				h = this.getHeight();
			}

			if(filter){
				TouchClient.getPApplet().image(filteredImg, x, y, w, h);

			} else {

				TouchClient.getPApplet().image(img2, x, y, w, h);
			}

		} else if(!imageArray){

			if(img != null){
				PImage img2;
				if(filter){
					img2 = filteredImg;
				} else {
					img2 = img;
				}



				if(imgRotated){
					TouchClient.getPApplet().pushMatrix();
					TouchClient.getPApplet().translate(this.getX()+this.getWidth(), this.getY()+this.getHeight());
					TouchClient.getPApplet().rotate(imgRotateAmount);
					TouchClient.getPApplet().image(img2, 0, 0, this.getWidth(), this.getHeight());
					TouchClient.getPApplet().popMatrix();

					//imageFlip(img2, "v");


				} else {
					TouchClient.getPApplet().image(img2, this.getX(), this.getY(), this.getWidth(), this.getHeight());
				}
			}

		}
		
		if(colFilter){
			drawFilter();
		}
	}

	//imageFlip function by nick lally - http://www.openprocessing.org/sketch/22331
	//paste function at the bottom of your sketch, and use like this: imageFlip(imageName,x,y,"mode")
	//modes are "v", "v2", "h", "h2"
	//"v" mirrors vertically, "v2" mirrors top half vertically
	//"h" mirrors horizontally, "h2" mirrors left half horizontally
	void imageFlip(PImage imageName, String mode){
		//"v2" flips the top half of the image across the x-axis
		if(mode == "v2"){
			imageName.loadPixels();
			for(int i = 0; i < imageName.height; i++){
				for(int j = 1; j < imageName.width; j++){
					imageName.pixels[(imageName.height - 1 - i)*(imageName.width) + j] = imageName.pixels[i*(imageName.width) + j];
				}
			}
			imageName.updatePixels();
			//this.img = imageName;
			// image(imageName,xPos,yPos);

			//"v" flips the entire image across the x-axis
		}else if(mode == "v"){
			imageName.loadPixels();
			int tempImage[] = new int[imageName.width*imageName.height];
			for(int i = 0; i < imageName.width*imageName.height; i++){
				tempImage[i] = imageName.pixels[i];
			}

			for(int i = 0; i < imageName.height; i++){
				for(int j = 1; j < imageName.width; j++){
					imageName.pixels[(imageName.height - 1 - i)*(imageName.width) + j] = tempImage[i*(imageName.width) + j];
				}
			}
			imageName.updatePixels();
			//this.img = imageName;
			//image(imageName,xPos,yPos);

			//"h2" flips the left half of the image across the y-axis
		}else if(mode == "h2"){
			imageName.loadPixels();
			for(int i = 0; i < imageName.height; i++){
				for(int j = 1; j < imageName.width; j++){
					imageName.pixels[i*imageName.width + j] = imageName.pixels[(i+1)*(imageName.width) - j];
				}
			}
			imageName.updatePixels();
			// this.img = imageName;
			// image(imageName,xPos,yPos); 

			//"h" flips the entire image across the y-axis
		}else if(mode == "h"){
			imageName.loadPixels();
			int tempImage[] = new int[imageName.width*imageName.height];
			for(int i = 0; i < imageName.width*imageName.height; i++){
				tempImage[i] = imageName.pixels[i];
			}
			for(int i = 0; i < imageName.height; i++){
				for(int j = 1; j < imageName.width; j++){
					imageName.pixels[(i+1)*(imageName.width) - j] = tempImage[i*imageName.width + j];
				}
			}
			imageName.updatePixels();
			//this.img = imageName;
			// image(imageName,xPos,yPos);
		} else {
			PApplet.println("No mirror direction specified!");
			PApplet.println("Use v, v2, h, or h2 for the 4th argument");
		}
	} 

}
