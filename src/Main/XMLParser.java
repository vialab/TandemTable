package Main;

import java.io.IOException;

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
			Document doc = parser.build("./data/tags.xml");
			Element root = doc.getRootElement();
			
			for(int i = 0; i < root.getChildCount(); i++){
				Node node = root.getChild(i);

				if (node instanceof Element) {
					String name = ((Element) node).getLocalName();

					if(name.equalsIgnoreCase("sports")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("religion")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("dance")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("school")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("movies")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("books")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("music")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("food")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("science")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("culture")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("art")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("language")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("history")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("names")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
								}
							}
						}
					} else if(name.equalsIgnoreCase("holidays")){
						int indexEnglish = 0;
						int indexFrench = 0;

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
			Document doc = parser.build("./data/languages.xml");
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
					int indexEnglish = 0;
					int indexFrench = 0;

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
								}
							} else if(element.getLocalName().equalsIgnoreCase("moreQ")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.moreQE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.moreQF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("ranTopics")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.ranTopicsE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.ranTopicsF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("choAnoTop")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.choAnoTopE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.choAnoTopF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("choAnoAct")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.choAnoActE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.choAnoActF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("newLang")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.newLangE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.newLangF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("morePics")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.morePicsE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.morePicsF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("tweetWord")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.tweetWordE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.tweetWordF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("moreNews")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.moreNewsE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.moreNewsF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("back2Heads")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.back2HeadsE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.back2HeadsF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("moreVideos")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.moreVideosE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.moreVideosF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("playAgain")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.playAgainE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.playAgainF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("play")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.playE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.playF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("pause")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.pauseE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.pauseF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("stop")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.stopE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.stopF = element.getValue();
								}
							} else if(element.getLocalName().equalsIgnoreCase("playVideo")){
								if(attribute.equalsIgnoreCase("English")){
									Languages.playVideoE = element.getValue();
								} else if(attribute.equalsIgnoreCase("French")){
									Languages.playVideoF = element.getValue();
								}
							}
						} 

					}
				} else if (name.equalsIgnoreCase("activities")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
					int indexEnglish = 0;
					int indexFrench = 0;
					
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
							}

						}
					}
				} else if (name.equalsIgnoreCase("sportsExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("religionExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("danceExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("schoolExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("moviesExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("booksExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("musicExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("foodExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("scienceExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("cultureExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("artExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("languageExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("historyExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				}  else if (name.equalsIgnoreCase("namesExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				} else if (name.equalsIgnoreCase("holidaysExpansion")){
					int indexEnglish = 0;
					int indexFrench = 0;

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
							}

						}
					}
				}

			}
		}



	}
}
