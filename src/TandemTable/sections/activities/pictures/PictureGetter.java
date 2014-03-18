package TandemTable.sections.activities.pictures;

import gifAnimation.Gif;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import TandemTable.Colours;
import TandemTable.Sketch;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.SearchParameters;

import processing.core.PApplet;
import processing.core.PImage;
import vialab.simpleMultiTouch.zones.ImageZone;
import vialab.simpleMultiTouch.zones.RectZone;

public class PictureGetter extends Thread {
	Sketch sketch;
	PictureActivity picAct;
	
	ImageZone imgZone;

	public PictureGetter(Sketch sketch, PictureActivity picAct){
		this.sketch = sketch;
		this.picAct = picAct;
		
		final Gif ajaxGIF = new Gif(sketch, sketch.loadGIF);
		final PApplet app = sketch;
		ajaxGIF.loop();

		int width = sketch.getWidth()/16;
		picAct.loading = new RectZone((sketch.getWidth() - sketch.lineX)/2 + sketch.lineX - width/2, sketch.getHeight()/2 - width/2, width, width){
			public void drawZone(){
				super.drawZone();
				app.image(ajaxGIF, this.getX(), this.getY(), this.getWidth(), this.getHeight());
			}
		};
		picAct.loading.setDrawBorder(false);
		picAct.loading.setActive(false);
		sketch.client.addZone(picAct.loading);
	}

	public void run(){
		picAct.loading.setActive(true);
		connect(picAct.tags, false, false);
		picAct.loading.setActive(false);
	}
	
	public void connect(String[] keywords, boolean failedOnce, boolean failedTwice){
		try {
			if(!sketch.flickrInit){
				sketch.f = new Flickr(Colours.apiKeyFlickr, Colours.secretFlickr, new REST());
				sketch.requestContext = RequestContext.getRequestContext();
				Flickr.debugRequest = false;
				Flickr.debugStream = false;
				sketch.flickrInit = true;
			}
			if(picAct.canceled){
				picAct.removeImgs();
				return;
			}
			SearchParameters sParams = new SearchParameters();
			sParams.setTags(keywords);
			sParams.setTagMode("any");
			//sParams.setText(picAct.tagsStr);
			sParams.setSafeSearch("2");
			sParams.setSort(SearchParameters.RELEVANCE);

			picAct.list = sketch.f.getPhotosInterface().search(sParams, picAct.MAX_IMAGES, picAct.pageOffset);
			if(picAct.list.isEmpty() && !failedOnce && !failedTwice){
				picAct.pageOffset = 1;
				picAct.currentPicsIndex = 0;
				connect(picAct.tags, true, false);
			} else if (picAct.list.isEmpty() && failedOnce && !failedTwice){
				picAct.pageOffset = 1;
				picAct.currentPicsIndex = 0;
				connect(picAct.topic, false, true);
			} else if (picAct.list.isEmpty() && failedTwice){
				picAct.failed = true;
				picAct.loading.setActive(false);
				System.out.println("Error: Flickr result list is empty.");
			} else {
				for(int i = 0; i < picAct.NUM_IMAGES; i++) {
					if(!picAct.canceled){
						PImage img = loadImage();
						if(img != null){
							picAct.setImgZone(picAct.imgs[i], img);
						}
					}
				}
			}

		} catch (IOException e) {
			picAct.failed = true;
			System.out.println("Error: Flickr IOException");
			e.printStackTrace();
		} catch (SAXException e) {
			picAct.failed = true;
			System.out.println("Error: Flickr SAXException");
			e.printStackTrace();
		} catch (FlickrException e) {
			picAct.failed = true;
			System.out.println("Error: Flickr FlickrException");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			picAct.failed = true;
			System.out.println("Error: Flickr ParserConfigurationException");
			e.printStackTrace();
		}
	}

	

	public PImage loadImage(){
		int i = picAct.currentPicsIndex;
		if(!this.picAct.canceled){
			if(i < picAct.MAX_IMAGES) {
				Photo photo = (Photo) picAct.list.get(i);
				PImage pImage = sketch.loadImage(photo.getSmallUrl());
				
				picAct.currentPicsIndex++;
				return pImage;
			} else {
				picAct.pageOffset++;
				picAct.currentPicsIndex = 0;
				connect(picAct.tags, false, false);
			}
		} else {
			this.picAct.removeImgs();
		}
		
		return null;

	}


	
}

