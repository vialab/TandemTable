package main;

public class Languages {
	public static String[] introQuestionsEnglish = new String[Sketch.NUM_QUESTIONS];
	public static String[] activitiesE = new String[Sketch.NUM_ACTIVITIES];
	public static String[] topicsE = new String[Sketch.NUM_TOPICS];
	public static String[][] topicsExpandedE = new String[Sketch.NUM_TOPICS][Sketch.NUM_SYN];
	
	public static String[] introQuestionsFrench = new String[Sketch.NUM_QUESTIONS];
	public static String[] activitiesF = new String[Sketch.NUM_ACTIVITIES];
	public static String[] topicsF = new String[Sketch.NUM_TOPICS];
	public static String[][] topicsExpandedF = new String[Sketch.NUM_TOPICS][Sketch.NUM_SYN];
	
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

	public static String nextE, moreQE, ranTopicsE, choAnoTopE, choAnoActE, newLangE, morePicsE, tweetWordE,
	moreNewsE, back2HeadsE, moreVideosE, playAgainE, playE, pauseE, stopE, playVideoE;
	
	public static String nextF, moreQF, ranTopicsF, choAnoTopF, choAnoActF, newLangF, morePicsF, tweetWordF,
	moreNewsF, back2HeadsF, moreVideosF, playAgainF, playF, pauseF, stopF, playVideoF;
	
	public static String[][] tagsE = new String[15][18];
	public static String[][] tagsF = new String[15][18];
	
	/////////////////////////////////////////////////
	
	String lang;
	
	public String[] introQuestions = new String[Sketch.NUM_QUESTIONS];
	public String[] activities = new String[Sketch.NUM_ACTIVITIES];
	public String[] topics = new String[Sketch.NUM_TOPICS];
	public String[][] topicsExpanded = new String[Sketch.NUM_TOPICS][Sketch.NUM_SYN];
	
	
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
	moreNews, back2Heads, moreVideos, playAgain, play, pause, stop, playVideo;
	
	public String[][] tags = new String[15][18];
	
	public Languages(String lang){
		this.lang = lang;
		
		if(lang.equalsIgnoreCase("English")){
			setEnglish();
		} else if(lang.equalsIgnoreCase("French")){
			setFrench();
		}
	}
	
	public void setEnglish(){
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
		
		tags = tagsE;
	}
	
	public void setFrench(){
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
		
		tags = tagsF;
	}
}
