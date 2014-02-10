package TandemTable;
import java.awt.Color;

import processing.core.PFont;



public class Colours {
	// For twitter!!
	public static final String consumerKeyTwitter = "WWlZXooRI0aPV66hEp18yQ";
	public static final String consumerSecretTwitter = "XnSRDtJ2tQMSo54Wx4iSiO2bTd90IoqbBxUbXtonw";
	public static final String tokenTwitter = "436221506-Ou4542xd9boPlf0HHs4Ibqn5K0qclsbdXEAm1fdK";
	public static final String tokenSecretTwitter ="WMMmFUKQTLwnpRALv5sPydSmhUzc2tki5EiJJkdY2sw";
	public static Color boundingBox = new Color(236, 250, 254);//(200, 240, 253);//(78, 163, 233);
	public static Color shadow = new Color(188, 188, 188);
	public static Color tweetText = new Color(0,0,0);
	public static Color videoIndicator = new Color(188, 1, 188);
	public static Color imageIndicator = new Color(188, 188, 2);
	public static Color textIndicator = new Color(3, 188, 188);
	// For diffbot
	public static final String diffbotToken = "32c6d7dcc7e1f722f0ca7184cadf5b13";
	public static final String diffbotEndpoint = "http://www.diffbot.com/api/article?";
	
	// For Bing Translator Web Service
	public static final String clientSecret = "GLEJGnL6x5dxJPtvOiDjE5YlK48+UGFKNQkM9NXCWV0=";
	public static final String clientId = "EP";
	
	// For flickr!!
	public static final String apiKeyFlickr = "60be7b8070e765b1f7ca00165e10cd84";
	public static final String secretFlickr = "bc2b8cc23599d52b";
	
	// For New York Times API!!
	public static final String apiKeyNYT = "be779e860522d0b7b007f27c82978c24:9:65789053";
	public static final String baseURLNYT = "http://api.nytimes.com/svc/search/v1/article";
	
	// For YouTube API
	public static final String apiKeyTube = "AI39si65RBMm12llmp81wJ64-WUFhwYWvJfr1YuONwlTXgIQqDQRtjZsK--tbVtG4Wd8L2uK7rQNztfOLTHHUMiYg38zAdO3VA";
	public static final String appNameTube = "TanLangLearn";
	
	
	public static String font = "Dialog.bold-225.vlw";//"CooperBlack-48.vlw";//"Consolas-Bold-48.vlw";//Aharoni-Bold-48.vlw";
	public static PFont pFont;
	public static final double PI = 3.14159265358979;

	//From MainSketch.java
	public static Color backgroundColour = new Color(255, 255, 255);//Color(179, 255, 174);

	//Disabled ImageZone colour in profile picker
	public static Color disabledZone = new Color(200, 200, 200, 255);
	//Title Screen Colours
	//Colours of the circles used in the Title Screen
	public static Color[] circTitle = {new Color(30, 210, 10, 255), new Color(130, 210, 90, 255)};
	//Colour of "TandemTable"
	public static Color titleColor = new Color(135, 158, 208, 255);
	
	//From Main.login
	public static Color zoneText = new Color(255, 255, 255);
	public static Color unselectedZone = new Color(79, 129, 189);
	public static Color selectedZone = new Color(189, 84, 79);
	public static Color fadedOutZone = new Color(191, 191, 191);//(185, 205, 229);//(166, 206, 227);//(220, 230, 242);
	public static int zoneBorder = 103; //56. 93, 138

	// For News Headlines
	public static Color newsTitleShadow = new Color(127,194,245);
	public static Color highlightTextColour = new Color(233, 255, 31);
	public static Color scrollTriColor = new Color(255, 0, 0);
	public static int bodyTextBackground = 240;
	
	//From LayoutManager.java
	public static Color textColour = new Color(79, 129, 189);
	public static Color lineColour = new Color(149, 179, 215);
	public static Color fadedText = new Color(171, 171, 171);

	//From VideoActivity.java
	public static Color currentColour = new Color(240,248,255);
	public static Color elementShadow = new Color(72, 181, 17);
	public static final String PLAY_BUTTON = "./play.png";
	public static final String PLAY_BUTTON_SELECTED = "./play-red.png"; 
	
	
	//From Graph.java
	public static Color unselectedNode = new Color(79, 129, 189);
	public static Color selectedNode = new Color(135, 140, 100);
	public static Color visitedNode = new Color(185, 205, 229);
	public static Color currentNode = new Color(77, 175, 74);
	public static Color edgeColour = new Color(79, 129, 189);
	public static Color nodeText = new Color(255, 255, 255);
	
	// Activity bar
	public static Color twitter = new Color(102, 194, 165);//(251, 180, 174);
	public static Color newsHead = new Color(252, 141, 98);//(179, 205, 227);
	public static Color pictures = new Color(141, 160, 203);//(204, 235, 197);
	public static Color videos = new Color(231, 138, 195);//(222, 203, 228);
	public static Color pGame = new Color(166, 216, 84);//(254, 217, 166);

	
}
