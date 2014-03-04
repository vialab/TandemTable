package TandemTable.sections.mainSection;

import java.util.Random;
import java.util.Vector;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import TandemTable.ColourEval;
import TandemTable.Colours;
import TandemTable.Sketch;
import processing.core.PConstants;
import vialab.simpleMultiTouch.Touch;
import vialab.simpleMultiTouch.TouchClient;
import vialab.simpleMultiTouch.zones.CircleZone;


public class Graph {
	TouchClient client;
	Sketch sketch;
	MainSection layoutManager;
	final int NUM_NODES = 15;
	boolean[] nodeFlag = new boolean[NUM_NODES];
	boolean[] visitedNodes = new boolean[NUM_NODES];
	public CircleZone[] nodes = new CircleZone[NUM_NODES];
	Animator[] animator = new Animator[NUM_NODES];

	Vector<int[]> nodePos = new Vector<int[]>();
	Vector<int[]> nodeConnect = new Vector<int[]>();
	Random randGen;

	String[] topics1, topics2;
	String lang1, lang2;

	int startX, spaceX;
	int rows = 3;
	int columns = 5;
	int sizeNode;
	public int lastSelectedNode = -1;

	// For randomly picking nodes and deactivating certain activities for those nodes
	int[] randNodes;
	int[][] randActs;


	public Graph(Sketch sketch, MainSection layoutManager, int lineX, String lang1, String lang2) {
		this.client = sketch.client;
		this.sketch = sketch;
		this.layoutManager = layoutManager;
		this.lang1 = lang1;
		this.lang2 = lang2;
		startX = lineX;
		spaceX = sketch.getWidth() - lineX;
		sizeNode = sketch.getWidth()/10;
		randGen = new Random(System.currentTimeMillis());


		randomlyPickNodes(randGen);
		createNodes();
	}

	public void randomlyPickNodes(Random randGen){

		int randNumNodes = randGen.nextInt(4) + 4;


		randNodes = randNotEqual(randNumNodes, NUM_NODES);
		int[][] randActs1 = new int[randNumNodes][];


		for(int i = 0; i < randNumNodes; i++){
			int numAct = randGen.nextInt(2) + 1;
			int[] acts = randNotEqual(numAct, Sketch.NUM_ACTIVITIES);
			randActs1[i] = acts;
		}

		randActs = new int[randNumNodes][Sketch.NUM_ACTIVITIES];

		for(int i = 0; i < randNumNodes; i++){

			for(int j = 0; j < randActs1[i].length; j++){

				if(randActs1[i][j] == 0){
					randActs[i][0] = 1;
				} else if(randActs1[i][j] == 1){
					randActs[i][1] = 1;
				} else if(randActs1[i][j] == 2){
					randActs[i][2] = 1;
				} else if(randActs1[i][j] == 3){
					randActs[i][3] = 1;
				} else if(randActs1[i][j] == 4){
					randActs[i][4] = 1;
				}
			}

		}

		
	}

	//Creates maxI random numbers from 0 to maxRand that do not equal each other
	public int[] randNotEqual(int maxI, int maxRand){
		int[] array = new int[maxI];
		
		for(int i = 0; i < maxI; i ++){
			array[i] = randGen.nextInt(maxRand);
			if(i == 0){
				continue;
			} else {
				boolean flag = false;
				for(int j = 0; j < i; j++){
					if(array[i] == array[j]){
						flag = true;
						break;
					}
				}
				if (flag == true){
					i = i - 2;
				}
			}
		}
		return array;

	}

	public void createNodes(){

		int gridSizeX = spaceX/5;
		int gridSizeY = (sketch.getHeight() - 2*layoutManager.actBHeight)/3;
		int sizeNode = sketch.getWidth()/10;
		int textSize = sketch.screenWidth/55;
		int offSet = sketch.getHeight()/220;


		Random randomG = new Random();
		int x = 0;
		int y = 0;

		//if(lang1.equalsIgnoreCase("English")){
			topics1 = sketch.learner1.topics;//layoutManager.sketch.topicsE;
		//} else if(lang1.equalsIgnoreCase("French")){
		//	topics1 = layoutManager.sketch.topicsF;
		//}

		//if(lang2.equalsIgnoreCase("English")){
			topics2 = sketch.learner2.topics;//layoutManager.sketch.topicsE;
		//} else if(lang2.equalsIgnoreCase("French")){
		//	topics2 = layoutManager.sketch.topicsF;
		//}

		for(int j = 0; j < rows; j++){
			for(int h = 0; h < columns; h++){

				x = randomG.nextInt(gridSizeX-sizeNode) + startX + gridSizeX*(h);
				if (j == 0){
					y = randomG.nextInt(gridSizeY-sizeNode - sizeNode/10) + layoutManager.actBHeight + offSet + sizeNode/10;
				} else if (j == rows-1){
					y = randomG.nextInt(gridSizeY-sizeNode - sizeNode/10) + gridSizeY*(j) + layoutManager.actBHeight + offSet;
				} else {
					y = randomG.nextInt(gridSizeY-sizeNode) + gridSizeY*(j)+ layoutManager.actBHeight + offSet;
				}
				nodePos.add(new int[]{x + sizeNode/2, y + sizeNode/2});

				final int k = j*columns + h;
				final int tSize = textSize;

				//********************************** for randomly deactivating activites
				boolean randFlag1 = false;
				int index1 = -1;
				for(int i = 0; i < randNodes.length; i++){
					if(k == randNodes[i]){
						randFlag1 = true;
						index1 = i;
					}
				}
				final int index = index1;
				final boolean randFlag = randFlag1;
				
				nodes[k] = new CircleZone(x, y, sizeNode, sizeNode){

					public boolean addTouch(Touch t){
						
						layoutManager.setBarColour(index, randFlag);
						

						for(int i = 0; i < NUM_NODES; i++){
							if(i != k && nodeFlag[i] == true){
								animator[i].stop();
								
								if(visitedNodes[i]){
									nodes[i].setFillColor(Colours.visitedNode.getRed(), Colours.visitedNode.getGreen(), Colours.visitedNode.getBlue());	
								} else {
									nodes[i].setFillColor(Colours.unselectedNode.getRed(), Colours.unselectedNode.getGreen(), Colours.unselectedNode.getBlue());
								}
								nodeFlag[i] = false;
							}
						}
						if(!nodeFlag[k] && k != lastSelectedNode){
							animator[k].start();

							layoutManager.activityBFlag = false;
							for(int i = 0; i < Sketch.NUM_ACTIVITIES; i++){
								if(layoutManager.activityFlags1[i] == true){
									layoutManager.animA2[i].stop();
									layoutManager.activityFlags1[i] = false;
								}
								if(layoutManager.activityFlags2[i] == true){
									layoutManager.animA1[i].stop();
									layoutManager.activityFlags2[i] = false;
								}
								layoutManager.activityB1[i].setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
								layoutManager.activityB2[i].setColour(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
								layoutManager.activityB1[i].setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
								layoutManager.activityB2[i].setTextColour(Colours.fadedText.getRed(), Colours.fadedText.getGreen(), Colours.fadedText.getBlue());
							}
							
							nodeFlag[k] = true;
						} else if (this.getNumIds() >= 2 ){
							if (lastSelectedNode != -1){
								nodes[lastSelectedNode].setFillColor(Colours.visitedNode.getRed(), Colours.visitedNode.getGreen(), Colours.visitedNode.getBlue());	
							}
							nodeFlag[k] = false;
							lastSelectedNode = k;
							animator[k].stop();
							nodes[k].setFillColor(Colours.currentNode.getRed(), Colours.currentNode.getGreen(), Colours.currentNode.getBlue());
							visitedNodes[k] = true;
							
							layoutManager.enableActivityButtons(k, topics1[k], randFlag, index);
						} 
						else if(k == lastSelectedNode){
							
							layoutManager.enableActivityButtons(k, topics1[k], randFlag, index);
							
						}
						
						return true;

					}

					

					public void drawZone(){
						super.drawZone();
						//drawRing(getX() + getWidth()/2, getY()+getHeight()/2, sNode/2, (int)(sNode/1.8), 32);


						sketch.textAlign(PConstants.CENTER, PConstants.CENTER);
						sketch.fill(Colours.nodeText.getRed(), Colours.nodeText.getGreen(), Colours.nodeText.getBlue());
						sketch.textFont(Colours.pFont, tSize);
						sketch.text(topics1[k], getX(), getY()+getHeight()/2, getWidth(), getHeight()/3);

						//Bar across middle
						float actIndWidth = getWidth()/7;
						float yTop = getY()+getHeight()/2 - (getHeight()/17)/2;
						float yBot = getHeight()/17;

						if(!randFlag){
							sketch.fill(Colours.twitter.getRed(), Colours.twitter.getGreen(), Colours.twitter.getBlue());
							sketch.rect(getX()+actIndWidth, yTop, actIndWidth, yBot);

							sketch.fill(Colours.newsHead.getRed(), Colours.newsHead.getGreen(), Colours.newsHead.getBlue());
							sketch.rect(getX()+actIndWidth*2, yTop, actIndWidth, yBot);

							sketch.fill(Colours.pictures.getRed(), Colours.pictures.getGreen(), Colours.pictures.getBlue());
							sketch.rect(getX()+actIndWidth*3, yTop, actIndWidth, yBot);

							sketch.fill(Colours.videos.getRed(), Colours.videos.getGreen(), Colours.videos.getBlue());
							sketch.rect(getX()+actIndWidth*4, yTop, actIndWidth, yBot);

							sketch.fill(Colours.pGame.getRed(), Colours.pGame.getGreen(), Colours.pGame.getBlue());
							sketch.rect(getX()+actIndWidth*5, yTop, actIndWidth, yBot);
						} else {
							if(randActs[index][0] != 1){
								sketch.fill(Colours.twitter.getRed(), Colours.twitter.getGreen(), Colours.twitter.getBlue());
								
							} else {
								sketch.fill(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
								
							}
							sketch.rect(getX()+actIndWidth, yTop, actIndWidth, yBot);
							//sketch.noStroke();

							if(randActs[index][1] != 1){
								sketch.fill(Colours.newsHead.getRed(), Colours.newsHead.getGreen(), Colours.newsHead.getBlue());
								
							} else {
								sketch.fill(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
							}
							sketch.rect(getX()+actIndWidth*2, yTop, actIndWidth, yBot);


							if(randActs[index][2] != 1){
								sketch.fill(Colours.pictures.getRed(), Colours.pictures.getGreen(), Colours.pictures.getBlue());
								
							} else {
								sketch.fill(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
							}	
							sketch.rect(getX()+actIndWidth*3, yTop, actIndWidth, yBot);


							if(randActs[index][3] != 1){
								sketch.fill(Colours.videos.getRed(), Colours.videos.getGreen(), Colours.videos.getBlue());
								
							} else {
								sketch.fill(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
							}	
							sketch.rect(getX()+actIndWidth*4, yTop, actIndWidth, yBot);
						
							if(randActs[index][4] != 1){
								sketch.fill(Colours.pGame.getRed(), Colours.pGame.getGreen(), Colours.pGame.getBlue());
								
							} else {
								sketch.fill(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
							}
							sketch.rect(getX()+actIndWidth*5, yTop, actIndWidth, yBot);

						}
						
						if(sketch.deactivateVideo) {
							sketch.fill(Colours.fadedOutZone.getRed(), Colours.fadedOutZone.getGreen(), Colours.fadedOutZone.getBlue());
							sketch.rect(getX()+actIndWidth*4, yTop, actIndWidth, yBot);
						}

						sketch.pushMatrix();
						sketch.translate(getX() + getWidth(), getY() + getHeight()/2);
						sketch.rotate((float) Colours.PI);
						sketch.fill(Colours.nodeText.getRed(), Colours.nodeText.getGreen(), Colours.nodeText.getBlue());
						sketch.text(topics2[k], 0, 0, getWidth(), getHeight()/3);
						sketch.popMatrix();
					}
				};
				nodes[k].setFillColor(Colours.unselectedNode.getRed(), Colours.unselectedNode.getGreen(), Colours.unselectedNode.getBlue());
				nodes[k].setStroke(false);
				nodes[k].setDrawBorder(false);


				client.addZone(nodes[k]);

				animator[k] = PropertySetter.createAnimator(1200, nodes[k], 
						"colour", new ColourEval(), Colours.unselectedNode, Colours.selectedNode);

				animator[k].setRepeatBehavior(RepeatBehavior.REVERSE);
				animator[k].setRepeatCount(Animator.INFINITE);

			}
		}


		for(int j = 0; j < rows; j++){
			for(int h = 0; h < columns; h++){
				int k = j*columns + h;
				float rand = randomG.nextFloat();

				if(rand > 0.60){
					if(h < columns-1){
						nodeConnect.add(new int[]{nodePos.get(k)[0], nodePos.get(k)[1], nodePos.get(k+1)[0], nodePos.get(k+1)[1]});	
					} else {
						nodeConnect.add(new int[]{nodePos.get(k)[0], nodePos.get(k)[1], nodePos.get(k-1)[0], nodePos.get(k-1)[1]});	
					}
				} else if(rand > 0.30){
					if(j < rows-1){
						int n = (j+1)*columns + h;
						nodeConnect.add(new int[]{nodePos.get(k)[0], nodePos.get(k)[1], nodePos.get(n)[0], nodePos.get(n)[1]});	
					} else {
						int n = (j-1)*columns + h;
						nodeConnect.add(new int[]{nodePos.get(k)[0], nodePos.get(k)[1], nodePos.get(n)[0], nodePos.get(n)[1]});	
					}
				} else {
					if(h < columns-1){
						if(j < rows-1){
							int n = (j+1)*columns + h+1;
							nodeConnect.add(new int[]{nodePos.get(k)[0], nodePos.get(k)[1], nodePos.get(n)[0], nodePos.get(n)[1]});	
						} else {
							int n = (j-1)*columns + h+1;
							nodeConnect.add(new int[]{nodePos.get(k)[0], nodePos.get(k)[1], nodePos.get(n)[0], nodePos.get(n)[1]});

						}
					} else {
						if(j < rows-1){
							int n = (j+1)*columns + h-1;
							nodeConnect.add(new int[]{nodePos.get(k)[0], nodePos.get(k)[1], nodePos.get(n)[0], nodePos.get(n)[1]});	
						} else {
							int n = (j-1)*columns + h-1;
							nodeConnect.add(new int[]{nodePos.get(k)[0], nodePos.get(k)[1], nodePos.get(n)[0], nodePos.get(n)[1]});

						}


					}
				}

			}
		}


	}

	public void displayEdges() {
		for(int[] i : nodeConnect){
			sketch.strokeWeight(3);
			sketch.stroke(Colours.edgeColour.getRed(), Colours.edgeColour.getGreen(), Colours.edgeColour.getBlue());
			sketch.line(i[0], i[1], i[2], i[3]);
		}
	}


	public void activateNodes(){
		for(CircleZone cZone: nodes){
			cZone.setActive(true);
		}
	}

	public void deactivateNodes(){
		//for(CircleZone cZone: nodes){
		for(int i = 0; i < NUM_NODES; i++){
			if(i != lastSelectedNode){
				nodes[i].setActive(false);
			}
		}
	}

	public void removeNodes(){
		for(int i = 0; i < NUM_NODES; i++){
			if(i != lastSelectedNode){
				client.removeZone(nodes[i]);
			}
		}
	}
	
	/*void drawRing(int x, int y, float w1, float w2, int segments){
		//Taken from http://processing.org/discourse/yabb2/YaBB.pl?num=1128001871 on Feb. 8, 2012
		// w1 == outer radius, w2 == inner radius.
		float deltaA=(float) ((1.0/(float)segments)*PConstants.TWO_PI);
		sketch.beginShape(PConstants.QUADS);
		for(int i=0;i<segments;i++){

			if(i < segments/5){
				sketch.fill(Colours.twitter.getRed(), Colours.twitter.getGreen(), Colours.twitter.getBlue());
			} else if (i < 2*segments/5){
				sketch.fill(Colours.newsHead.getRed(), Colours.newsHead.getGreen(), Colours.newsHead.getBlue());
			} else if (i < 3*segments/5){
				sketch.fill(Colours.pictures.getRed(), Colours.pictures.getGreen(), Colours.pictures.getBlue());
			} else if (i < 4*segments/5){
				sketch.fill(Colours.videos.getRed(), Colours.videos.getGreen(), Colours.videos.getBlue());
			} else if (i < 5*segments/5){
				sketch.fill(Colours.webSearch.getRed(), Colours.webSearch.getGreen(), Colours.webSearch.getBlue());
			}
			sketch.vertex(x+w1*PApplet.cos(i*deltaA),y+w1*PApplet.sin(i*deltaA));
			sketch.vertex(x+w2*PApplet.cos(i*deltaA),y+w2*PApplet.sin(i*deltaA));
			sketch.vertex(x+w2*PApplet.cos((i+1)*deltaA),y+w2*PApplet.sin((i+1)*deltaA));
			sketch.vertex(x+w1*PApplet.cos((i+1)*deltaA),y+w1*PApplet.sin((i+1)*deltaA));
		}
		sketch.endShape();
	} */

}
