package TandemTable.util;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	
	JLabel minNoiseNeg1, maxNoiseNeg1, minNoisePos1, maxNoisePos1,
		minNoiseNeg2, maxNoiseNeg2, minNoisePos2, maxNoisePos2;
	JTextField minTNeg1, maxTNeg1, minTPos1, maxTPos1, 
		minTNeg2, maxTNeg2, minTPos2, maxTPos2;
		
	public AudioFrame(Sketch sketch) {
		super("AudioFrame");
		this.sketch = sketch;
		
		setContentPane(new DrawPane());
		setLayout(new BorderLayout());
		setSize(sketch.getWidth(), sketch.getHeight());
		
		
		minNoiseNeg1 = new JLabel("Learner 1 - Min Noise Neg:");
		maxNoiseNeg1 = new JLabel("Learner 1 - Max Noise Neg");
		minNoisePos1 = new JLabel("Learner 1 - Min Noise Pos:");
		maxNoisePos1 = new JLabel("Learner 1 - Max Noise Pos");
		
		minTNeg1 = new JTextField("0");
		minTNeg1.setEditable(false);
		maxTNeg1 = new JTextField("0");
		minTPos1 = new JTextField("0");
		maxTPos1 = new JTextField("0");
		
		//minNoiseNeg1.setForeground(Color.white);
		//maxNoiseNeg1.setForeground(Color.white);
		//minNoisePos1.setForeground(Color.white);
		//maxNoisePos1.setForeground(Color.white);
		JPanel pl1 = new JPanel();
		pl1.setLayout(new GridLayout(1, 8, 10, 10));
		pl1.add(minNoiseNeg1);
		pl1.add(minTNeg1);
		pl1.add(maxNoiseNeg1);
		pl1.add(maxTNeg1);
		pl1.add(minNoisePos1);
		pl1.add(minTPos1);
		pl1.add(maxNoisePos1);
		pl1.add(maxTPos1);
		add(pl1, BorderLayout.PAGE_START);
		
		
		minNoiseNeg2 = new JLabel("Learner 2 - Min Noise Neg:");
		maxNoiseNeg2 = new JLabel("Learner 2 - Max Noise Neg");
		minNoisePos2 = new JLabel("Learner 2 - Min Noise Pos:");
		maxNoisePos2 = new JLabel("Learner 2 - Max Noise Pos");
		
		minTNeg2 = new JTextField("0");
		maxTNeg2 = new JTextField("0");
		minTPos2 = new JTextField("0");
		maxTPos2 = new JTextField("0");
		
		//minNoiseNeg2.setForeground(Color.white);
		//maxNoiseNeg2.setForeground(Color.white);
		//minNoisePos2.setForeground(Color.white);
		//maxNoisePos2.setForeground(Color.white);
		JPanel pl2 = new JPanel();
		pl2.setLayout(new GridLayout(1, 8, 10, 10));
		pl2.add(minNoiseNeg2);
		pl2.add(minTNeg2);
		pl2.add(maxNoiseNeg2);
		pl2.add(maxTNeg2);
		pl2.add(minNoisePos2);
		pl2.add(minTPos2);
		pl2.add(maxNoisePos2);
		pl2.add(maxTPos2);
		add(pl2, BorderLayout.PAGE_END);
		
		
		
		setVisible(true);
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());
	}
	
	class DrawPane extends JPanel {
		private static final long serialVersionUID = -5523137716125969647L;

		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setBackground(Color.black);
			g2.clearRect(0,  0, getWidth(), getHeight());
			draw(g2, sketch.audioIn[0], 0);
			draw(g2, sketch.audioIn[1], 1);
			
			minTNeg1.setText(String.valueOf(sketch.audioIn[0].maxNoiseLvlNeg));
			minTNeg2.setText(String.valueOf(sketch.audioIn[1].maxNoiseLvlNeg));
			maxTNeg1.setText(String.valueOf(sketch.audioIn[0].maxNoiseLvlNeg*sketch.audioIn[0].noiseMult));
			maxTNeg2.setText(String.valueOf(sketch.audioIn[1].maxNoiseLvlNeg*sketch.audioIn[1].noiseMult));
			
			minTPos1.setText(String.valueOf(sketch.audioIn[0].maxNoiseLvlPos));
			minTPos2.setText(String.valueOf(sketch.audioIn[1].maxNoiseLvlPos));
			maxTPos1.setText(String.valueOf(sketch.audioIn[0].maxNoiseLvlPos*sketch.audioIn[0].noiseMult));
			maxTPos2.setText(String.valueOf(sketch.audioIn[1].maxNoiseLvlPos*sketch.audioIn[1].noiseMult));
		}
		
		public void draw(Graphics2D g, AudioIn audioIn, int user) {
			////////////////////////////////////////////
			// Draw PCM Data
			////////////////////////////////////////////
			g.setColor(new Color(255, 255, 255));
			g.setStroke(new BasicStroke(1));
			ArrayList<Float> tempData = new ArrayList<Float>(audioIn.getPCMData());
			
			float yHeight = getHeight()/4 + (getHeight()/2)*user;
			float lastX = 0, lastY = yHeight;
			float mult = 1000;
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
			/////////////////////////////////////////////
			
			//sketch.textSize(22);
			
			///////////////////////////////////////////////////
			// Draw Utterance Length and End Length Thresholds
			//////////////////////////////////////////////////
			g.setColor(new Color(255, 0, 0));
			//sketch.stroke(255, 0, 0);
			//sketch.fill(255, 0, 0);
			float heightY = (float) (yHeight*0.9);
			float x = 100;
			float length = x + audioIn.utterLengthThreshPaper*indexAdd;
			g.drawLine((int) x, (int) heightY, (int) length, (int) heightY);
			//sketch.text("Min Utterance Length", length, (float) (heightY*0.99));
			
			g.setColor(new Color(255, 22, 225));
			//sketch.stroke(255, 255, 255);
			//sketch.fill(255, 255, 255);
			heightY = (float) (yHeight*0.95);
			length = x + audioIn.endThesholdPaper*indexAdd;
			g.drawLine((int) x, (int) heightY, (int) length, (int) heightY);
			//sketch.text("Combined Utter Length", length, (float) (heightY*0.99));
			/////////////////////////////////////////////////
			
			//sketch.stroke(0, 0, 255);
			
			///////////////////////////////////////////////////
			// Draw Positive and Negative Noise Thresholds
			//////////////////////////////////////////////////
			g.setColor(new Color(0, 0, 255));
			heightY = yHeight + audioIn.maxNoiseLvlPos*mult;
			g.drawLine((int) 0, (int) heightY, (int) getWidth(), (int) heightY);
			
			heightY = yHeight + audioIn.maxNoiseLvlNeg*mult;
			g.drawLine((int) 0, (int) heightY, (int) getWidth(), (int) heightY);
			
			g.setColor(new Color(0, 255, 0));
			heightY = yHeight + audioIn.maxNoiseLvlPos*mult*audioIn.noiseMult;
			g.drawLine((int) 0, (int) heightY, (int) getWidth(), (int) heightY);
			
			heightY = yHeight + audioIn.maxNoiseLvlNeg*mult*audioIn.noiseMult;
			g.drawLine((int) 0, (int) heightY, (int) getWidth(), (int) heightY);
			////////////////////////////////////////////
			
			////////////////////////////////////////////
			// Draw captured utterances
			////////////////////////////////////////////
			ArrayList<Utterance> tempUtter = new ArrayList<Utterance>(audioIn.utterArray);
			float y = (float) (yHeight*0.75);
			
			
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
				float mainY = yHeight;
				lastY = mainY;
				
				ArrayList<Float> tempPCM = new ArrayList<Float>(utterance.getPCM());
				
				for(Float f: tempPCM) {
					g.drawLine((int) lastX, (int) lastY, (int) index, (int) (mainY + f.floatValue()*mult));
					lastX = index;
					lastY = mainY + f.floatValue()*mult;
					index += indexAdd;
					
				}
			}
			//////////////////////////////////////////////
		}
	}
	
	private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {

            } else if (e.getID() == KeyEvent.KEY_RELEASED) {

            } else if (e.getID() == KeyEvent.KEY_TYPED) {
            	char input = e.getKeyChar();
            	float chngAmnt = 0.001f;
            	
            	if(input == '1') {
        			sketch.audioIn[0].maxNoiseLvlNeg += chngAmnt;
        			sketch.audioIn[0].maxNoiseLvlPos -= chngAmnt;
        		}
        		else if(input == '2') {
        			sketch.audioIn[0].maxNoiseLvlNeg -= chngAmnt;
        			sketch.audioIn[0].maxNoiseLvlPos += chngAmnt;
        		}
        		else if(input == '3') {
        			sketch.audioIn[1].maxNoiseLvlNeg += chngAmnt;
        			sketch.audioIn[1].maxNoiseLvlPos -= chngAmnt;
        		}
        		else if(input == '4') {
        			sketch.audioIn[1].maxNoiseLvlNeg -= chngAmnt;
        			sketch.audioIn[1].maxNoiseLvlPos += chngAmnt;
        		}
        		else if(input == '5') {
        			sketch.audioIn[0].noiseMult += chngAmnt;
        			sketch.audioIn[1].noiseMult += chngAmnt;
        		}
        		else if(input == '6') {
        			sketch.audioIn[0].noiseMult -= chngAmnt;
        			sketch.audioIn[1].noiseMult -= chngAmnt;
        		}
            }
            return false;
        }
    }
}
