package main;
import java.awt.Color;
import java.util.Random;
import java.util.Vector;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.google.gdata.client.youtube.YouTubeService;

import processing.core.PApplet;
import processing.core.PConstants;
import twitter4j.Twitter;
import vialab.simpleMultiTouch.TouchClient;
import main.login.LoginScreen;

/**
 * Main Class of TandemTable
 * @author Erik Paluka - Copyright 2012-2013
 *
 */
@SuppressWarnings("serial")
public class Sketch extends PApplet {
	public TouchClient client;
	public MainSection mainSection;
	public Languages learner1, learner2;
	
	public YouTubeService myService;
	public Flickr f;
	public REST rest;
	public RequestContext requestContext;
	public Twitter twitter;
	
	public UserLogger logger;
	
	public boolean youTubeInit = false;
	public boolean flickrInit = false;
	public boolean twitterInit = false;
	
	boolean drawMainLayout = false;
	boolean showFrameRate = false;
	

	public int buttonWidth, buttonHeight, radius, yOffset, textSize, shadowOffset, 	lineX,
		qSwipeThreshold, tSwipeThreshold;

	final static int NUM_QUESTIONS = 19;
	final static int NUM_ACTIVITIES = 5;
	final static int NUM_TOPICS = 15;
	final static int NUM_SYN = 10;
	public final int NUM_PAGES = 200;
	public final int TIMEOUT = 10000;
	
	
	public void setup() {
		size(screenWidth, screenHeight, P3D);
		client = new TouchClient(this, true, false);
		client.applyZonesMatrix(true);
		client.setDrawTouchPointsSize(getHeight()/30);
		//client.setDebugMode(true);
		//client.setDrawTouchPoints(false);
		Colours.pFont = loadFont(Colours.font);

		XMLParser parser = new XMLParser();
		parser.parseLanguages();
		parser.parseTags();
		
		yOffset = screenHeight/100;
		textSize = screenWidth/50;
		buttonWidth = screenWidth/6;
		buttonHeight = screenHeight/8;
		radius = screenHeight/25;
		shadowOffset = getHeight()/60;
		lineX = (int) (getWidth()/5.9);
		qSwipeThreshold = getWidth()/15;
		tSwipeThreshold = getHeight()/15;

		LoginScreen startScreen = new LoginScreen();
		startScreen.initialize(client, this);


	}

	public void draw(){
		background(Colours.backgroundColour.getRed(), Colours.backgroundColour.getGreen(), Colours.backgroundColour.getBlue());


		if(drawMainLayout){
			mainSection.drawLayout();
		} else {
			strokeWeight(5);
			stroke(Colours.lineColour.getRed(), Colours.lineColour.getGreen(), Colours.lineColour.getBlue());
			line(getX(), getHeight()/2, getX() + getWidth(), getHeight()/2);
		}

		if(showFrameRate){
			textFont(Colours.pFont, textSize);
			textAlign(PConstants.LEFT, PConstants.BOTTOM);
			fill(0);
			text("FPS:\n" + Integer.toString((int)frameRate), 0, 2*getHeight()/3);
		}



	}

	public static void main(String[] args) {
		PApplet.main(new String[]{"--present", "main.Sketch"});
	}


	public void initializeMainScreen(String lang1, String lang2){
		//TODO
		//LOGGING
		//logger = new UserLogger();
		//logger.logInfo(, "User" + "" + " " + lang1);
		mainSection = new MainSection(this, lang1, lang2);
		drawMainLayout = true;
	}

	public Color getRandomColour(){
		Random rand = new Random();
		float num = rand.nextFloat();
		Color c = null;
		
		/// Colors from www.ColorBrewer.org 
		//  by Cynthia A. Brewer, Geography, Pennsylvania State University.
		if(num < 0.10){
			c = new Color(141, 211, 199);
		} else if (num < 0.20){
			c = new Color(255, 255, 179);
		} else if (num < 0.30){
			c = new Color(190, 186, 218);
		} else if (num < 0.40){
			c = new Color(251, 128, 114);
		} else if (num < 0.50){
			c = new Color(128, 177, 211);
		} else if (num < 0.60){
			c = new Color(253, 180, 98);
		} else if (num < 0.70){
			c = new Color(179, 222, 105);
		} else if (num < 0.80){
			c = new Color(252, 205, 229);
		} else if (num < 0.90){
			c = new Color(204, 235, 197);
		} else {
			c = new Color(255, 237, 111);
		}
		
		return c;
	}
	public String[] scrambleStrings(String[] stringsToScrambled){
		String[] scrambled = new String[stringsToScrambled.length];
		Vector<String> temp1 = new Vector<String>();
		Vector<String> temp2 = new Vector<String>();
		Vector<String> temp3 = new Vector<String>();
		Random rand = new Random();

		for(String s: stringsToScrambled){
			if(rand.nextFloat() <= 0.4){
				temp1.add(s);
			} else {
				temp2.add(s);
			}
		}

		temp3.addAll(temp2);
		temp3.addAll(temp1);
		temp1.clear();
		temp2.clear();
		for(String s: temp3){
			if(rand.nextFloat() <= 0.4){
				temp1.add(s);
			} else {
				temp2.add(s);
			}
		}
		temp3.clear();
		temp3.addAll(temp2);
		temp3.addAll(temp1);
		int index = 0;

		for(String s: temp3){
			scrambled[index] = s;
			index++;
			//System.out.println(s);
		}

		return scrambled;
	}

	

}
