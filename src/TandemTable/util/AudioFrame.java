package TandemTable.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import TandemTable.Sketch;
import TandemTable.util.AudioIn.Utterance;

public class AudioFrame extends JFrame {

	private static final long serialVersionUID = -175788422133617135L;

	Sketch sketch;
	
	// Last index value of drawn PCM data
	// For scrolling the drawing
	int indexScrollPCM = 0;
	// Index and counter for scrolling utterances
	int indexScrollUtter = 0;
	int sliderCounter = 0;
		
	public AudioFrame(Sketch sketch) {
		super("AudioFrame");
		this.sketch = sketch;
		
		setContentPane(new DrawPane());
		setSize(sketch.getWidth(), sketch.getHeight());
		setVisible(true);
	}
	
	class DrawPane extends JPanel {
		private static final long serialVersionUID = -5523137716125969647L;

		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setBackground(Color.black);
			g2.clearRect(0,  0, getWidth(), getHeight());
			draw(g2, sketch.audioIn[0]);
			draw(g2, sketch.audioIn[1]);
		}
		
		public void draw(Graphics2D g, AudioIn audioIn) {

			g.setColor(new Color(255, 255, 255));
			g.setStroke(new BasicStroke(1));
			ArrayList<Float> tempData = new ArrayList<Float>(audioIn.getPCMData());
			
			float yHeight = getHeight()/2;
			float lastX = 0, lastY = yHeight;
			float mult = 2000;
			float index = 0;
			float indexAdd = (float) 0.01;
			
			for(int i = indexScrollPCM; i < tempData.size(); i++) {
				float sample = tempData.get(i);
				
				//System.out.println(sample + " " + index);
				g.drawLine((int) lastX, (int) lastY, (int) index, (int) (yHeight + sample*mult));
				lastX = index;
				lastY = yHeight + sample*mult;
				//sketch.ellipse(index, sketch.height/2 + sample*mult, 0.1f, 0.1f);
				index += indexAdd;
				
				if(index > getWidth()) {
					index = lastX = 0;
					indexScrollPCM = i;
				}
			}
			
			//sketch.textSize(22);
			
			// Draw thresholds
			g.setColor(new Color(255, 0, 0));
			//sketch.stroke(255, 0, 0);
			//sketch.fill(255, 0, 0);
			float heightY = (float) ((getHeight()/2)*0.9);
			float x = 100;
			float length = x + audioIn.utterLengthThresh*indexAdd;
			g.drawLine((int) x, (int) heightY, (int) length, (int) heightY);
			//sketch.text("Min Utterance Length", length, (float) (heightY*0.99));
			
			g.setColor(new Color(255, 22, 225));
			//sketch.stroke(255, 255, 255);
			//sketch.fill(255, 255, 255);
			heightY = (float) ((getHeight()/2)*0.95);
			length = x + audioIn.combineUtterTheshold*indexAdd;
			g.drawLine((int) x, (int) heightY, (int) length, (int) heightY);
			//sketch.text("Combined Utter Length", length, (float) (heightY*0.99));
			
			//sketch.stroke(0, 0, 255);
			g.setColor(new Color(0, 0, 255));
			heightY = getHeight()/2 + audioIn.maxNoiseLvlPos*mult;
			g.drawLine((int) 0, (int) heightY, (int) getWidth(), (int) heightY);
			
			heightY = getHeight()/2 + audioIn.maxNoiseLvlNeg*mult;
			g.drawLine((int) 0, (int) heightY, (int) getWidth(), (int) heightY);
			
			g.setColor(new Color(0, 255, 0));
			heightY = getHeight()/2 + audioIn.maxNoiseLvlPos*mult*audioIn.noiseMult;
			g.drawLine((int) 0, (int) heightY, (int) getWidth(), (int) heightY);
			
			heightY = getHeight()/2 + audioIn.maxNoiseLvlNeg*mult*audioIn.noiseMult;
			g.drawLine((int) 0, (int) heightY, (int) getWidth(), (int) heightY);
			
			
			// Draw captured utterances
			ArrayList<Utterance> tempUtter = new ArrayList<Utterance>(audioIn.utterArray);
			float y = (float) ((getHeight()/2)*0.75);
			
			
			for(int i = indexScrollUtter; i < tempUtter.size(); i++) {
				Utterance utterance = tempUtter.get(i);
				
				if(utterance.getEndIndex()*indexAdd - sliderCounter*getWidth()  > getWidth()) {
					sliderCounter++;
					indexScrollUtter = i;
				}
				
				g.setColor(new Color(utterance.r, utterance.g, utterance.b));
				
				g.drawLine((int) (utterance.getStartIndex()*indexAdd - sliderCounter*getWidth()), (int) y, (int) (utterance.getEndIndex()*indexAdd - sliderCounter*getWidth()), (int) y);
				
				
				
				
				lastX = utterance.getStartIndex()*indexAdd - sliderCounter*getWidth();
				index = lastX;
				float mainY = (float) (getHeight()/1.5);
				lastY = mainY;
				
				ArrayList<Float> tempPCM = new ArrayList<Float>(utterance.getPCM());
				
				for(Float f: tempPCM) {
					g.drawLine((int) lastX, (int) lastY, (int) index, (int) (mainY + f.floatValue()*mult));
					lastX = index;
					lastY = mainY + f.floatValue()*mult;
					index += indexAdd;
					
				}
			}
		}
	}
}
