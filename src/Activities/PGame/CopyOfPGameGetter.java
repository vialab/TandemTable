package Activities.PGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import processing.core.PApplet;
import processing.core.PImage;
import vialab.simpleMultiTouch.TouchClient;
import Main.Colours;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.SearchParameters;
import com.aetrion.flickr.tags.Tag;

public class CopyOfPGameGetter extends Thread {
	TouchClient client;
	PApplet applet;
	Flickr f;
	REST rest;
	RequestContext requestContext;
	PhotoList list;
	boolean failed = false;

	int lineX, pageOffset;
	PGame pGame;
	PImage[] pImg;

	public CopyOfPGameGetter(TouchClient client, PApplet applet, PGame pGame, int lineX){
		this.client = client;
		this.applet = applet;
		this.lineX = lineX;
		this.pGame = pGame;
		
		
	}
	
	public void connect(){
		try {
			f = new Flickr(Colours.apiKeyFlickr, Colours.secretFlickr, new REST());
		} catch (ParserConfigurationException e) {
			failed = true;
			e.printStackTrace();
		}

		requestContext = RequestContext.getRequestContext();
		Flickr.debugRequest = false;
		Flickr.debugStream = false;

	}

	public void run(){
		connect();
		pageOffset = 1;
		search();

	}
	
	public void search(){
		pGame.imgTags = "";
		getImages();
		loadImages();
		pGame.createTagZones();
	}



	public void getImages(){
		try {
			

			SearchParameters sParams = new SearchParameters();
			sParams.setTags(pGame.tags);
			//sParams.setText(pGame.tagsStr);
			Set<String> s = new LinkedHashSet<String>();
			s.add("tags");
			sParams.setExtras(s);
			sParams.setSafeSearch("2");
			sParams.setSort(SearchParameters.RELEVANCE);

			list = f.getPhotosInterface().search(sParams, pGame.MAX_IMAGES, pageOffset);
			pageOffset++;



		} catch (IOException e) {
			failed = true;
			e.printStackTrace();
		} catch (SAXException e) {
			failed = true;
			e.printStackTrace();
		} catch (FlickrException e) {
			failed = true;
			e.printStackTrace();
		} 


	}

	public void loadImages(){
		
		pImg = new PImage[pGame.NUM_IMAGES];
		
		for(int j = 0; j < pGame.ROWS; ++j){
			for(int i = 0; i < pGame.COLUMNS; ++i){
				if(!failed){
					final int index = j*pGame.COLUMNS + i;
					Photo photo = (Photo) list.get(index);

					/////////////////////////////////////////////////
					// Printing out Flickr photo information: title, description, and tags

					System.out.println("Title: " + photo.getTitle());
					/*try {
					System.out.println("Description: " + f.getPhotosInterface().getInfo(photo.getId(), null).getDescription());
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (SAXException e1) {
					e1.printStackTrace();
				} catch (FlickrException e1) {
					e1.printStackTrace();
				}*/
					System.out.print("Tags:");

					for(Object tag: photo.getTags()){
						System.out.print(" "+ ((Tag)tag).getValue());
					}

					System.out.println("\n");
					/////////////////////////////////////////////////

					String tag = " ";
					if(!photo.getTags().isEmpty()){
						tag = ((Tag) ((ArrayList) photo.getTags()).get(0)).getValue();
					}
					pGame.imgTags +=  tag + " &-&";
					//pGame.tagsArray[index] = tag + " &-&";


					pImg[index] = pGame.sketch.loadImage(photo.getSmallUrl());
					
					pGame.imgs[index].setImage(pImg[index]);
		
				}
			}
		}
	}




}

