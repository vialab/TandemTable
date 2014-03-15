package TandemTable;
import java.awt.Color;
import java.util.Random;
import java.util.Vector;

import TandemTable.sections.IntroSection;
import TandemTable.sections.login.LoginScreen;
import TandemTable.sections.mainSection.MainSection;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.google.gdata.client.youtube.YouTubeService;
import com.sun.jna.NativeLibrary;

import processing.core.PApplet;
import processing.core.PConstants;
import twitter4j.Twitter;
import vialab.simpleMultiTouch.TouchClient;

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
	public IntroSection intro;
	public LoginScreen startScreen;
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
	
	// If the learners have already 
	//gone through the intro phase
	public boolean doneIntro = false;
		
	///////////////////////////////////////
	// For study
	//////////////////////////////////////
	
	// Deactivating video activity for study
	public boolean deactivateVideo = false;
	// Removing video activity for study
	public boolean removeVideoAct = true;
	// Activate all activities for all topics
	public boolean activateAllAct = true;
	///////////////////////////////////////

	public int buttonWidth, buttonHeight, radius, yOffset, textSize, shadowOffset, 	lineX,
		qSwipeThreshold, tSwipeThreshold;

	public final static int NUM_QUESTIONS = 19;
	public final static int NUM_ACTIVITIES = 5;
	public final static int NUM_TOPICS = 15;
	public final static int NUM_SYN = 10;
	public final int NUM_PAGES = 200;
	public final int TIMEOUT = 10000;
	
	// Stroke weight of lines
	public int strokeW = 5;
	// Name of the loading GIF picture
	public String loadGIF = "ajaxGIF-blue.gif";
	// Regex for replacement
	public String replaceRegex ="[^a-zA-Z_0-9_'_Á_á_À _Â_à_Â_â_Ä_ä_Ã_ã_Å_å_Ç_ç_É_é_È_è_Ê_ê_Ë_ë_Í_í_Ì_ì_Î_î_Ï_ï_Ñ_ñ_Ó_ó_Ò_ò_Ô_ô_Ö_ö_Õ_õ_Ú_ú_Ù_ù_Û_û_Ü_ü_Ý_ý_ÿ]";
	
	
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
		
		// How much the user has to swipe to activate
		// a swipe event
		qSwipeThreshold = getWidth()/15;
		tSwipeThreshold = getHeight()/15;

		// Needs to be set for each machine
		NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC");
		
		startScreen = new LoginScreen();
		startScreen.initialize(client, this);


	}

	public void draw(){
		background(Colours.backgroundColour.getRed(), Colours.backgroundColour.getGreen(), Colours.backgroundColour.getBlue());


		if(drawMainLayout){
			mainSection.drawLayout();
		} else {
			strokeWeight(strokeW);
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
		PApplet.main(new String[]{"--present", "TandemTable.Sketch"});
	}

	
	public void initializeMainScreen(String lang1, String lang2){
		//TODO
		//LOGGING
		//logger = new UserLogger();
		//logger.logInfo(, "User" + "" + " " + lang1);
		mainSection = new MainSection(this, lang1, lang2);
		
		if(doneIntro) {
			mainSection.createMainScreen();
		} else {
			mainSection.centerLineFlag = true;
			intro = new IntroSection(this, mainSection);
		}
		
		drawMainLayout = true;
	}

	// Returns a predefine colour value
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
	
	// Scramble expanded topic words for activities
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
