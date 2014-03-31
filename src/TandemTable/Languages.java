package TandemTable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Languages {
	// Variables for storing words extracted by XML parser
	
	// English
	public static String[] introQuestionsEnglish = new String[Sketch.NUM_QUESTIONS];
	public static String[] activitiesE = new String[Sketch.NUM_ACTIVITIES];
	public static String[] topicsE = new String[Sketch.NUM_TOPICS];
	public static String[][] topicsExpandedE = new String[Sketch.NUM_TOPICS][Sketch.NUM_SYN];
	
	// French
	public static String[] introQuestionsFrench = new String[Sketch.NUM_QUESTIONS];
	public static String[] activitiesF = new String[Sketch.NUM_ACTIVITIES];
	public static String[] topicsF = new String[Sketch.NUM_TOPICS];
	public static String[][] topicsExpandedF = new String[Sketch.NUM_TOPICS][Sketch.NUM_SYN];
	
	// Portuguese
	public static String[] introQuestionsPortuguese = new String[Sketch.NUM_QUESTIONS];
	public static String[] activitiesP = new String[Sketch.NUM_ACTIVITIES];
	public static String[] topicsP = new String[Sketch.NUM_TOPICS];
	public static String[][] topicsExpandedP = new String[Sketch.NUM_TOPICS][Sketch.NUM_SYN];
	
	// Spanish
	public static String[] introQuestionsSpanish = new String[Sketch.NUM_QUESTIONS];
	public static String[] activitiesS = new String[Sketch.NUM_ACTIVITIES];
	public static String[] topicsS = new String[Sketch.NUM_TOPICS];
	public static String[][] topicsExpandedS = new String[Sketch.NUM_TOPICS][Sketch.NUM_SYN];
	
	// English
	public static String[] tweetPromptsE = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] newsPromptsE = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] photosPromptsE = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] pGamePromptsE = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] videoPromptsE = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] topicPromptsE = new String[Sketch.NUM_CONTENT_PROMPTS];
	
	// French
	public static String[] tweetPromptsF = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] newsPromptsF = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] photosPromptsF = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] pGamePromptsF = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] videoPromptsF = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] topicPromptsF = new String[Sketch.NUM_CONTENT_PROMPTS];
	
	// Portuguese
	public static String[] tweetPromptsP = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] newsPromptsP = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] photosPromptsP = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] pGamePromptsP = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] videoPromptsP = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] topicPromptsP = new String[Sketch.NUM_CONTENT_PROMPTS];
	
	// Spanish
	public static String[] tweetPromptsS = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] newsPromptsS = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] photosPromptsS = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] pGamePromptsS = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] videoPromptsS = new String[Sketch.NUM_CONTENT_PROMPTS];
	public static String[] topicPromptsS = new String[Sketch.NUM_CONTENT_PROMPTS];
	
	// English
	public static String[] sportsE = new String[Sketch.NUM_SYN];
	public static String[] religionE = new String[Sketch.NUM_SYN];
	public static String[] danceE = new String[Sketch.NUM_SYN];
	public static String[] schoolE = new String[Sketch.NUM_SYN];
	public static String[] moviesE = new String[Sketch.NUM_SYN];
	public static String[] booksE = new String[Sketch.NUM_SYN];
	public static String[] musicE = new String[Sketch.NUM_SYN];
	public static String[] foodE = new String[Sketch.NUM_SYN];
	public static String[] scienceE = new String[Sketch.NUM_SYN];
	public static String[] cultureE = new String[Sketch.NUM_SYN];
	public static String[] artE = new String[Sketch.NUM_SYN];
	public static String[] languageE = new String[Sketch.NUM_SYN];
	public static String[] historyE = new String[Sketch.NUM_SYN];
	public static String[] namesE = new String[Sketch.NUM_SYN];
	public static String[] holidaysE = new String[Sketch.NUM_SYN];
	
	// French
	public static String[] sportsF = new String[Sketch.NUM_SYN];
	public static String[] religionF = new String[Sketch.NUM_SYN];
	public static String[] danceF = new String[Sketch.NUM_SYN];
	public static String[] schoolF = new String[Sketch.NUM_SYN];
	public static String[] moviesF = new String[Sketch.NUM_SYN];
	public static String[] booksF = new String[Sketch.NUM_SYN];
	public static String[] musicF = new String[Sketch.NUM_SYN];
	public static String[] foodF = new String[Sketch.NUM_SYN];
	public static String[] scienceF = new String[Sketch.NUM_SYN];
	public static String[] cultureF = new String[Sketch.NUM_SYN];
	public static String[] artF = new String[Sketch.NUM_SYN];
	public static String[] languageF = new String[Sketch.NUM_SYN];
	public static String[] historyF = new String[Sketch.NUM_SYN];
	public static String[] namesF = new String[Sketch.NUM_SYN];
	public static String[] holidaysF = new String[Sketch.NUM_SYN];
	
	// Portuguese
	public static String[] sportsP = new String[Sketch.NUM_SYN];
	public static String[] religionP = new String[Sketch.NUM_SYN];
	public static String[] danceP = new String[Sketch.NUM_SYN];
	public static String[] schoolP = new String[Sketch.NUM_SYN];
	public static String[] moviesP = new String[Sketch.NUM_SYN];
	public static String[] booksP = new String[Sketch.NUM_SYN];
	public static String[] musicP = new String[Sketch.NUM_SYN];
	public static String[] foodP = new String[Sketch.NUM_SYN];
	public static String[] scienceP = new String[Sketch.NUM_SYN];
	public static String[] cultureP = new String[Sketch.NUM_SYN];
	public static String[] artP = new String[Sketch.NUM_SYN];
	public static String[] languageP = new String[Sketch.NUM_SYN];
	public static String[] historyP = new String[Sketch.NUM_SYN];
	public static String[] namesP = new String[Sketch.NUM_SYN];
	public static String[] holidaysP = new String[Sketch.NUM_SYN];
	
	// Spanish
	public static String[] sportsS = new String[Sketch.NUM_SYN];
	public static String[] religionS = new String[Sketch.NUM_SYN];
	public static String[] danceS = new String[Sketch.NUM_SYN];
	public static String[] schoolS = new String[Sketch.NUM_SYN];
	public static String[] moviesS = new String[Sketch.NUM_SYN];
	public static String[] booksS = new String[Sketch.NUM_SYN];
	public static String[] musicS = new String[Sketch.NUM_SYN];
	public static String[] foodS = new String[Sketch.NUM_SYN];
	public static String[] scienceS = new String[Sketch.NUM_SYN];
	public static String[] cultureS = new String[Sketch.NUM_SYN];
	public static String[] artS = new String[Sketch.NUM_SYN];
	public static String[] languageS = new String[Sketch.NUM_SYN];
	public static String[] historyS = new String[Sketch.NUM_SYN];
	public static String[] namesS = new String[Sketch.NUM_SYN];
	public static String[] holidaysS = new String[Sketch.NUM_SYN];

	// English
	public static String nextE, moreQE, ranTopicsE, choAnoTopE, choAnoActE, newLangE, morePicsE, tweetWordE,
	moreNewsE, back2HeadsE, moreVideosE, playAgainE, playE, pauseE, stopE, playVideoE, utterVisE;
	
	// French
	public static String nextF, moreQF, ranTopicsF, choAnoTopF, choAnoActF, newLangF, morePicsF, tweetWordF,
	moreNewsF, back2HeadsF, moreVideosF, playAgainF, playF, pauseF, stopF, playVideoF, utterVisF;
	
	// Portuguese
	public static String nextP, moreQP, ranTopicsP, choAnoTopP, choAnoActP, newLangP, morePicsP, tweetWordP,
	moreNewsP, back2HeadsP, moreVideosP, playAgainP, playP, pauseP, stopP, playVideoP, utterVisP;
	
	// Spanish
	public static String nextS, moreQS, ranTopicsS, choAnoTopS, choAnoActS, newLangS, morePicsS, tweetWordS,
	moreNewsS, back2HeadsS, moreVideosS, playAgainS, playS, pauseS, stopS, playVideoS, utterVisS;
	
	// English
	public static String[][] tagsE = new String[15][18];
	// French
	public static String[][] tagsF = new String[15][18];
	// Portuguese
	public static String[][] tagsP = new String[15][18];
	// Spanish
	public static String[][] tagsS = new String[15][18];
	
	/////////////////////////////////////////////////
	
	String lang;
	
	public String[] introQuestions = new String[Sketch.NUM_QUESTIONS];
	public String[] activities = new String[Sketch.NUM_ACTIVITIES];
	public String[] topics = new String[Sketch.NUM_TOPICS];
	public String[][] topicsExpanded = new String[Sketch.NUM_TOPICS][Sketch.NUM_SYN];
	
	public String[] tweetPrompts = new String[Sketch.NUM_CONTENT_PROMPTS];
	public String[] newsPrompts = new String[Sketch.NUM_CONTENT_PROMPTS];
	public String[] photosPrompts = new String[Sketch.NUM_CONTENT_PROMPTS];
	public String[] pGamePrompts = new String[Sketch.NUM_CONTENT_PROMPTS];
	public String[] videoPrompts = new String[Sketch.NUM_CONTENT_PROMPTS];
	public String[] topicPrompts = new String[Sketch.NUM_CONTENT_PROMPTS];
	
	
	public String[] sports = new String[Sketch.NUM_SYN];
	public String[] religion = new String[Sketch.NUM_SYN];
	public String[] dance = new String[Sketch.NUM_SYN];
	public String[] school = new String[Sketch.NUM_SYN];
	public String[] movies = new String[Sketch.NUM_SYN];
	public String[] books = new String[Sketch.NUM_SYN];
	public String[] music = new String[Sketch.NUM_SYN];
	public String[] food = new String[Sketch.NUM_SYN];
	public String[] science = new String[Sketch.NUM_SYN];
	public String[] culture = new String[Sketch.NUM_SYN];
	public String[] art = new String[Sketch.NUM_SYN];
	public String[] language = new String[Sketch.NUM_SYN];
	public String[] history = new String[Sketch.NUM_SYN];
	public String[] names = new String[Sketch.NUM_SYN];
	public String[] holidays = new String[Sketch.NUM_SYN];
	
	public String next, moreQ, ranTopics, choAnoTop, choAnoAct, newLang, morePics, tweetWord,
	moreNews, back2Heads, moreVideos, playAgain, play, pause, stop, playVideo, utterVis;
	
	// For filtering web service data based on language and culture
	public String countryCode, cultureCode;
	
	public String[][] tags = new String[Sketch.NUM_TOPICS][Sketch.NUM_TAGS];
	
	// For writing languages to file
	static FileWriter fw;
	static BufferedWriter bw;
	static boolean fr = false;
	static boolean pt = false;
	static boolean es = false;
	
	public Languages(String lang){
		this.lang = lang;
		
		if(lang.equalsIgnoreCase("English")){
			setEnglish();
		} else if(lang.equalsIgnoreCase("French")){
			setFrench();
		} else if(lang.equalsIgnoreCase("Portuguese")){
			setPortuguese();
		} else if(lang.equalsIgnoreCase("Spanish")){
			setSpanish();
		}
	}
	
	public static void writeOut(String language) {				
		try {
			fw = new FileWriter("toTranslate" + language + ".txt");
		
		
			bw = new BufferedWriter(fw);
			
			if(language.equalsIgnoreCase("French")){
				fr = true;
				pt = false;
				es = false;
			} else if(language.equalsIgnoreCase("Portuguese")){
				fr = false;
				pt = true;
				es = false;
			} else if(language.equalsIgnoreCase("Spanish")){
				fr = false;
				pt = false;
				es = true;
			} else {
				System.out.println("Incorrect language submitted for language write out.");
				throw new RuntimeException("Incorrect language submitted for language write out.");
			}
			
			for(int i = 0; i < Sketch.NUM_QUESTIONS; i ++) {
				writeBlock(introQuestionsEnglish[i], introQuestionsFrench[i], introQuestionsPortuguese[i], introQuestionsSpanish[i]);
			}
			
			for(int i = 0; i < Sketch.NUM_ACTIVITIES; i ++) {
				writeBlock(activitiesE[i], activitiesF[i], activitiesP[i], activitiesS[i]);
			}
			
			for(int i = 0; i < Sketch.NUM_TOPICS; i ++) {
				writeBlock(topicsE[i], topicsF[i], topicsP[i], topicsS[i]);
				
				for(int j = 0; j < Sketch.NUM_SYN; j ++) {
					writeBlock(topicsExpandedE[i][j], topicsExpandedF[i][j], topicsExpandedP[i][j], topicsExpandedS[i][j]);
				}
			}
			
			for(int i = 0; i < Sketch.NUM_CONTENT_PROMPTS; i ++) {
				writeBlock(newsPromptsE[i], newsPromptsF[i], newsPromptsP[i], newsPromptsS[i]);	
			}
			
			for(int i = 0; i < Sketch.NUM_CONTENT_PROMPTS; i ++) {
				writeBlock(photosPromptsE[i], photosPromptsF[i], photosPromptsP[i], photosPromptsS[i]);	
			}
			
			for(int i = 0; i < Sketch.NUM_CONTENT_PROMPTS; i ++) {
				writeBlock(pGamePromptsE[i], pGamePromptsF[i], pGamePromptsP[i], pGamePromptsS[i]);	
			}
			
			for(int i = 0; i < Sketch.NUM_CONTENT_PROMPTS; i ++) {
				writeBlock(videoPromptsE[i], videoPromptsF[i], videoPromptsP[i], videoPromptsS[i]);
			}
			
			for(int i = 0; i < Sketch.NUM_CONTENT_PROMPTS; i ++) {
				writeBlock(topicPromptsE[i], topicPromptsF[i], topicPromptsP[i], topicPromptsS[i]);
			}
			
			for(int i = 0; i < Sketch.NUM_CONTENT_PROMPTS; i ++) {
				writeBlock(tweetPromptsE[i], tweetPromptsF[i], tweetPromptsP[i], tweetPromptsS[i]);
			}
			
			writeBlock(nextE, nextF, nextP, nextS);
			writeBlock(moreQE, moreQF, moreQP, moreQS);
			writeBlock(ranTopicsE, ranTopicsF, ranTopicsP, ranTopicsS);
			writeBlock(choAnoTopE, choAnoTopF, choAnoTopP, choAnoTopS);
			writeBlock(choAnoActE, choAnoActF, choAnoActP, choAnoActS);
			writeBlock(newLangE, newLangF, newLangP, newLangS);
			writeBlock(morePicsE, morePicsF, morePicsP, morePicsS);
			writeBlock(tweetWordE, tweetWordF, tweetWordP, tweetWordS);
			writeBlock(moreNewsE, moreNewsF, moreNewsP, moreNewsS);
			writeBlock(back2HeadsE, back2HeadsF, back2HeadsP, back2HeadsS);
			writeBlock(moreVideosE, moreVideosF, moreVideosP, moreVideosS);
			writeBlock(playAgainE, playAgainF, playAgainP, playAgainS);
			writeBlock(playE, playF, playP, playS);
			writeBlock(pauseE, pauseF, pauseP, pauseS);
			writeBlock(stopE, stopF, stopP, stopS);
			writeBlock(playVideoE, playVideoF, playVideoP, playVideoS);
			writeBlock(utterVisE, utterVisF, utterVisP, utterVisS);
			
			
			bw.close();
			fw.close();
			System.out.println("Finished writing language out to file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

		
	}
	
	private static void writeBlock(String enS, String frS, String ptS, String esS) {
		try {
			bw.write(enS);
			bw.write("\n");
			
			if(fr) {
				bw.write(frS);
			} else if(pt) {
				bw.write(ptS);
			} else if(es) {
				bw.write(esS);
			}
			
			bw.write("\n");
			bw.write("\n");	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setEnglish(){
		introQuestions = introQuestionsEnglish;
		activities = activitiesE;
		topics = topicsE;
		topicsExpanded = topicsExpandedE;
		
		sports = sportsE;
		religion = religionE;
		dance = danceE;
		school = schoolE;
		movies = moviesE;
		books = booksE;
		music = musicE;
		food = foodE;
		science = scienceE;
		culture = cultureE;
		art = artE;
		language = languageE;
		history = historyE;
		names = namesE;
		holidays = holidaysE;
		
		next = nextE;
		moreQ = moreQE;
		ranTopics = ranTopicsE;
		choAnoTop = choAnoTopE;
		choAnoAct = choAnoActE;
		newLang = newLangE;
		morePics = morePicsE;
		tweetWord = tweetWordE;
		
		moreNews = moreNewsE;
		back2Heads = back2HeadsE;
		moreVideos = moreVideosE;
		playAgain = playAgainE;
		play = playE;
		pause = pauseE;
		stop = stopE;
		playVideo = playVideoE;
		utterVis = utterVisE;
		
		tweetPrompts = tweetPromptsE;
		newsPrompts = newsPromptsE;
		photosPrompts = photosPromptsE;
		pGamePrompts = pGamePromptsE;
		videoPrompts = videoPromptsE;
		topicPrompts = topicPromptsE;
		
		tags = tagsE;
		
		countryCode = "en";
		cultureCode = "en_all";
	}
	
	private void setFrench(){
		introQuestions = introQuestionsFrench;
		activities = activitiesF;
		topics = topicsF;
		topicsExpanded = topicsExpandedF;
		
		sports = sportsF;
		religion = religionF;
		dance = danceF;
		school = schoolF;
		movies = moviesF;
		books = booksF;
		music = musicF;
		food = foodF;
		science = scienceF;
		culture = cultureF;
		art = artF;
		language = languageF;
		history = historyF;
		names = namesF;
		holidays = holidaysF;
		
		next = nextF;
		moreQ = moreQF;
		ranTopics = ranTopicsF;
		choAnoTop = choAnoTopF;
		choAnoAct = choAnoActF;
		newLang = newLangF;
		morePics = morePicsF;
		tweetWord = tweetWordF;
		
		moreNews = moreNewsF;
		back2Heads = back2HeadsF;
		moreVideos = moreVideosF;
		playAgain = playAgainF;
		play = playF;
		pause = pauseF;
		stop = stopF;
		playVideo = playVideoF;
		utterVis = utterVisF;
		
		tweetPrompts = tweetPromptsF;
		newsPrompts = newsPromptsF;
		photosPrompts = photosPromptsF;
		pGamePrompts = pGamePromptsF;
		videoPrompts = videoPromptsF;
		topicPrompts = topicPromptsF;
		
		tags = tagsF;
		
		countryCode = "fr";
		cultureCode = "fr";
	}
	
	private void setPortuguese(){
		introQuestions = introQuestionsPortuguese;
		activities = activitiesP;
		topics = topicsP;
		topicsExpanded = topicsExpandedP;
		
		sports = sportsP;
		religion = religionP;
		dance = danceP;
		school = schoolP;
		movies = moviesP;
		books = booksP;
		music = musicP;
		food = foodP;
		science = scienceP;
		culture = cultureP;
		art = artP;
		language = languageP;
		history = historyP;
		names = namesP;
		holidays = holidaysP;
		
		next = nextP;
		moreQ = moreQP;
		ranTopics = ranTopicsP;
		choAnoTop = choAnoTopP;
		choAnoAct = choAnoActP;
		newLang = newLangP;
		morePics = morePicsP;
		tweetWord = tweetWordP;
		
		moreNews = moreNewsP;
		back2Heads = back2HeadsP;
		moreVideos = moreVideosP;
		playAgain = playAgainP;
		play = playP;
		pause = pauseP;
		stop = stopP;
		playVideo = playVideoP;
		utterVis = utterVisP;
		
		tweetPrompts = tweetPromptsP;
		newsPrompts = newsPromptsP;
		photosPrompts = photosPromptsP;
		pGamePrompts = pGamePromptsP;
		videoPrompts = videoPromptsP;
		topicPrompts = topicPromptsP;
		
		tags = tagsP;
		
		countryCode = "pt";
		
		// Brazil
		cultureCode = "pt-BR_br";
	}
	
	private void setSpanish(){
		introQuestions = introQuestionsSpanish;
		activities = activitiesS;
		topics = topicsS;
		topicsExpanded = topicsExpandedS;
		
		sports = sportsS;
		religion = religionS;
		dance = danceS;
		school = schoolS;
		movies = moviesS;
		books = booksS;
		music = musicS;
		food = foodS;
		science = scienceS;
		culture = cultureS;
		art = artS;
		language = languageS;
		history = historyS;
		names = namesS;
		holidays = holidaysS;
		
		next = nextS;
		moreQ = moreQS;
		ranTopics = ranTopicsS;
		choAnoTop = choAnoTopS;
		choAnoAct = choAnoActS;
		newLang = newLangS;
		morePics = morePicsS;
		tweetWord = tweetWordS;
		
		moreNews = moreNewsS;
		back2Heads = back2HeadsS;
		moreVideos = moreVideosS;
		playAgain = playAgainS;
		play = playS;
		pause = pauseS;
		stop = stopS;
		playVideo = playVideoS;
		utterVis = utterVisS;
		
		tweetPrompts = tweetPromptsS;
		newsPrompts = newsPromptsS;
		photosPrompts = photosPromptsS;
		pGamePrompts = pGamePromptsS;
		videoPrompts = videoPromptsS;
		topicPrompts = topicPromptsS;
		
		tags = tagsS;
		
		countryCode = "es";
		cultureCode = "es";
	}
}
