package TandemTable.util;

import java.io.FileInputStream;
import java.io.IOException;

import TandemTable.Languages;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ParsingException;

public class XMLParser {
	

	
	
	public XMLParser(){
		
	}

	public void parseTags(){

		try {

			Builder parser = new Builder();
			FileInputStream fs = new FileInputStream("./data/tags.xml");
			Document doc = parser.build(fs);
			Element root = doc.getRootElement();
			
			for(int i = 0; i < root.getChildCount(); i++){
				Node node = root.getChild(i);

				if (node instanceof Element) {
					String name = ((Element) node).getLocalName();

					if(name.equalsIgnoreCase("sports")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[0][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[0][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[0][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[0][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("religion")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[1][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[1][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[1][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[1][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("dance")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[2][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[2][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[2][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[2][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("school")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[3][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[3][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[3][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[3][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("movies")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[4][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[4][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[4][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[4][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("books")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[5][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[5][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[5][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[5][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("music")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[6][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[6][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[6][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[6][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("food")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[7][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[7][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[7][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[7][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("science")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[8][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[8][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[8][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[8][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("culture")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[9][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[9][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[9][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[9][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("art")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[10][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[10][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[10][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[10][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("language")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[11][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[11][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[11][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[11][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("history")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[12][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[12][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[12][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[12][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("names")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[13][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[13][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[13][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[13][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					} else if(name.equalsIgnoreCase("holidays")){
						int indexEnglish = 0, indexFrench = 0,
								indexPortuguese = 0, indexSpanish = 0;

						for(int j = 0; j < node.getChildCount(); j++){
							if (node.getChild(j) instanceof Element) {
								Element element = (Element) node.getChild(j);
								String attribute = element.getAttribute(0).getValue();

								if(attribute.equalsIgnoreCase("English")){
									Languages.tagsE[14][indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tagsF[14][indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tagsP[14][indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tagsS[14][indexSpanish] = element.getValue();
									indexSpanish++;
								}
							}
						}
					}
				}
			}
										
		} catch (ParsingException e) {
			System.err.println("ParsingException");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException");
			e.printStackTrace();
		}
	}
	
	public void parseLanguages(){

		try {

			Builder parser = new Builder();
			FileInputStream fs = new FileInputStream("./data/languages.xml");
			Document doc = parser.build(fs);
			Element root = doc.getRootElement();
			read(root);
			
			Languages.topicsExpandedE[0] = Languages.sportsE;
			Languages.topicsExpandedE[1] = Languages.religionE;
			Languages.topicsExpandedE[2] = Languages.danceE;
			Languages.topicsExpandedE[3] = Languages.schoolE;
			Languages.topicsExpandedE[4] = Languages.moviesE;
			Languages.topicsExpandedE[5] = Languages.booksE;
			Languages.topicsExpandedE[6] = Languages.musicE;
			Languages.topicsExpandedE[7] = Languages.foodE;
			Languages.topicsExpandedE[8] = Languages.scienceE;
			Languages.topicsExpandedE[9] = Languages.cultureE;
			Languages.topicsExpandedE[10] = Languages.artE;
			Languages.topicsExpandedE[11] = Languages.languageE;
			Languages.topicsExpandedE[12] = Languages.historyE;
			Languages.topicsExpandedE[13] = Languages.namesE;
			Languages.topicsExpandedE[14] = Languages.holidaysE; 
			
			Languages.topicsExpandedF[0] = Languages.sportsF;
			Languages.topicsExpandedF[1] = Languages.religionF;
			Languages.topicsExpandedF[2] = Languages.danceF;
			Languages.topicsExpandedF[3] = Languages.schoolF;
			Languages.topicsExpandedF[4] = Languages.moviesF;
			Languages.topicsExpandedF[5] = Languages.booksF;
			Languages.topicsExpandedF[6] = Languages.musicF;
			Languages.topicsExpandedF[7] = Languages.foodF;
			Languages.topicsExpandedF[8] = Languages.scienceF;
			Languages.topicsExpandedF[9] = Languages.cultureF;
			Languages.topicsExpandedF[10] = Languages.artF;
			Languages.topicsExpandedF[11] = Languages.languageF;
			Languages.topicsExpandedF[12] = Languages.historyF;
			Languages.topicsExpandedF[13] = Languages.namesF;
			Languages.topicsExpandedF[14] = Languages.holidaysF; 
			
			Languages.topicsExpandedP[0] = Languages.sportsP;
			Languages.topicsExpandedP[1] = Languages.religionP;
			Languages.topicsExpandedP[2] = Languages.danceP;
			Languages.topicsExpandedP[3] = Languages.schoolP;
			Languages.topicsExpandedP[4] = Languages.moviesP;
			Languages.topicsExpandedP[5] = Languages.booksP;
			Languages.topicsExpandedP[6] = Languages.musicP;
			Languages.topicsExpandedP[7] = Languages.foodP;
			Languages.topicsExpandedP[8] = Languages.scienceP;
			Languages.topicsExpandedP[9] = Languages.cultureP;
			Languages.topicsExpandedP[10] = Languages.artP;
			Languages.topicsExpandedP[11] = Languages.languageP;
			Languages.topicsExpandedP[12] = Languages.historyP;
			Languages.topicsExpandedP[13] = Languages.namesP;
			Languages.topicsExpandedP[14] = Languages.holidaysP; 
			
			Languages.topicsExpandedS[0] = Languages.sportsS;
			Languages.topicsExpandedS[1] = Languages.religionS;
			Languages.topicsExpandedS[2] = Languages.danceS;
			Languages.topicsExpandedS[3] = Languages.schoolS;
			Languages.topicsExpandedS[4] = Languages.moviesS;
			Languages.topicsExpandedS[5] = Languages.booksS;
			Languages.topicsExpandedS[6] = Languages.musicS;
			Languages.topicsExpandedS[7] = Languages.foodS;
			Languages.topicsExpandedS[8] = Languages.scienceS;
			Languages.topicsExpandedS[9] = Languages.cultureS;
			Languages.topicsExpandedS[10] = Languages.artS;
			Languages.topicsExpandedS[11] = Languages.languageS;
			Languages.topicsExpandedS[12] = Languages.historyS;
			Languages.topicsExpandedS[13] = Languages.namesS;
			Languages.topicsExpandedS[14] = Languages.holidaysS; 

		} catch (ParsingException e) {
			System.err.println("ParsingException");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException");
			e.printStackTrace();
		}
	}

	public void read(Element root){

		for(int i = 0; i < root.getChildCount(); i++){
			Node node = root.getChild(i);

			if (node instanceof Element) {
				String name = ((Element) node).getLocalName();

				if(name.equalsIgnoreCase("questions")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.introQuestionsEnglish[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.introQuestionsFrench[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.introQuestionsPortuguese[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.introQuestionsSpanish[indexSpanish] = element.getValue();
								indexSpanish++;
							}
						}

					}
				} else if (name.equalsIgnoreCase("buttons")){
					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(element.getLocalName().equalsIgnoreCase("next")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.nextE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.nextF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.nextP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.nextS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("moreQ")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.moreQE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.moreQF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.moreQP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.moreQS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("ranTopics")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.ranTopicsE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.ranTopicsF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.ranTopicsP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.ranTopicsS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("choAnoTop")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.choAnoTopE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.choAnoTopF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.choAnoTopP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.choAnoTopS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("choAnoAct")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.choAnoActE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.choAnoActF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.choAnoActP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.choAnoActS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("newLang")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.newLangE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.newLangF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.newLangP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.newLangS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("morePics")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.morePicsE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.morePicsF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.morePicsP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.morePicsS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("tweetWord")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.tweetWordE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tweetWordF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.tweetWordP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.tweetWordS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("moreNews")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.moreNewsE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.moreNewsF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.moreNewsP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.moreNewsS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("back2Heads")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.back2HeadsE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.back2HeadsF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.back2HeadsP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.back2HeadsS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("moreVideos")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.moreVideosE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.moreVideosF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.moreVideosP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.moreVideosS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("playAgain")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.playAgainE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.playAgainF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.playAgainP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.playAgainS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("play")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.playE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.playF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.playP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.playS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("pause")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.pauseE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.pauseF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.pauseP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.pauseS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("stop")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.stopE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.stopF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.stopP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.stopS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("playVideo")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.playVideoE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.playVideoF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.playVideoP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.playVideoS = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("utterVis")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.utterVisE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.utterVisF = element.getValue();
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.utterVisP = element.getValue();
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.utterVisS = element.getValue();
								}
							}
						} 

					}
				} else if (name.equalsIgnoreCase("activities")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							//if(element.getLocalName().equalsIgnoreCase("twitter")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.activitiesE[indexEnglish] = element.getValue();
									indexEnglish++;
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.activitiesF[indexFrench] = element.getValue();
									indexFrench++;
								} else if(attribute.equalsIgnoreCase("Portuguese")){
									Languages.activitiesP[indexPortuguese] = element.getValue();
									indexPortuguese++;
								} else if(attribute.equalsIgnoreCase("Spanish")){
									Languages.activitiesS[indexSpanish] = element.getValue();
									indexSpanish++;
								}
							/*} else if(element.getLocalName().equalsIgnoreCase("news")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.activitiesE[indexEnglish] = element.getValue();
									indexEnglish++;
								}
							} else if(element.getLocalName().equalsIgnoreCase("pictures")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.activitiesE[indexEnglish] = element.getValue();
									indexEnglish++;
								}
							} else if(element.getLocalName().equalsIgnoreCase("videos")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.activitiesE[indexEnglish] = element.getValue();
									indexEnglish++;
								}
							} else if(element.getLocalName().equalsIgnoreCase("game")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.activitiesE[indexEnglish] = element.getValue();
									indexEnglish++;
								}
							}*/
						}
					}


				} else if (name.equalsIgnoreCase("topics")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;
					
					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.topicsE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.topicsF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.topicsP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.topicsS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("sportsExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.sportsE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.sportsF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.sportsP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.sportsS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("religionExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.religionE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.religionF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.religionP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.religionS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("danceExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.danceE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.danceF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.danceP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.danceS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("schoolExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.schoolE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.schoolF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.schoolP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.schoolS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("moviesExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.moviesE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.moviesF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.moviesP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.moviesS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("booksExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.booksE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.booksF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.booksP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.booksS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("musicExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.musicE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.musicF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.musicP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.musicS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("foodExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.foodE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.foodF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.foodP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.foodS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("scienceExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.scienceE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.scienceF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.scienceP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.scienceS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("cultureExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.cultureE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.cultureF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.cultureP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.cultureS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("artExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.artE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.artF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.artP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.artS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("languageExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.languageE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.languageF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.languageP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.languageS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("historyExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.historyE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.historyF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.historyP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.historyS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				}  else if (name.equalsIgnoreCase("namesExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.namesE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.namesF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.namesP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.namesS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				} else if (name.equalsIgnoreCase("holidaysExpansion")){
					int indexEnglish = 0, indexFrench = 0,
							indexPortuguese = 0, indexSpanish = 0;

					for(int j = 0; j < node.getChildCount(); j++){
						if (node.getChild(j) instanceof Element) {
							Element element = (Element) node.getChild(j);
							String attribute = element.getAttribute(0).getValue();

							if(attribute.equalsIgnoreCase("English")){
								Languages.holidaysE[indexEnglish] = element.getValue();
								indexEnglish++;
							} else if(attribute.equalsIgnoreCase("French")){
								Languages.holidaysF[indexFrench] = element.getValue();
								indexFrench++;
							} else if(attribute.equalsIgnoreCase("Portuguese")){
								Languages.holidaysP[indexPortuguese] = element.getValue();
								indexPortuguese++;
							} else if(attribute.equalsIgnoreCase("Spanish")){
								Languages.holidaysS[indexSpanish] = element.getValue();
								indexSpanish++;
							}

						}
					}
				}

			}
		}



	}
}
