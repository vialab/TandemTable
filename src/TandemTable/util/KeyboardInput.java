package TandemTable.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import TandemTable.Sketch;

public class KeyboardInput extends KeyAdapter {

	Sketch sketch;
	
	public KeyboardInput(Sketch sketch) {
		this.sketch = sketch;
	}
	
	public void keyPressed(KeyEvent e) {
		
		char input = e.getKeyChar();
		//System.out.println("Key Pressed: " + input);
		
		if(input == ' ') {
			sketch.languagePrompt.play();
			sketch.mainLogger.log("Played language prompt");
			System.out.println("Played language prompt");
		}
		else if(input == 't') {
			sketch.talkingPrompt.play();
			long timeNow = System.currentTimeMillis();
			sketch.audioIn[0].timeOfLastUtter = timeNow;
			sketch.audioIn[1].timeOfLastUtter = timeNow;
			
			sketch.mainLogger.log("Played talking prompt");
			System.out.println("Played talking prompt");
		}
		else if(input == 's') {
			//sketch.logger1.close();
			//sketch.logger2.close();
			//System.out.println("Closed logging files");
			
			
			//sketch.audioIn[0].saveAudio();
			//sketch.audioIn[1].saveAudio();
		}
		else if(input == '1') {
			sketch.audioIn[0].maxNoiseLvlNeg++;
			sketch.audioIn[0].maxNoiseLvlPos--;
		}
		else if(input == '2') {
			sketch.audioIn[0].maxNoiseLvlNeg--;
			sketch.audioIn[0].maxNoiseLvlPos++;
		}
		else if(input == '3') {
			sketch.audioIn[1].maxNoiseLvlNeg++;
			sketch.audioIn[1].maxNoiseLvlPos--;
		}
		else if(input == '4') {
			sketch.audioIn[1].maxNoiseLvlNeg--;
			sketch.audioIn[1].maxNoiseLvlPos++;
		}
		else if(input == '5') {
			sketch.audioIn[0].noiseMult += 0.1;
			sketch.audioIn[1].noiseMult += 0.1;
		}
		else if(input == '6') {
			sketch.audioIn[0].noiseMult -= 0.1;
			sketch.audioIn[1].noiseMult -= 0.1;
		}
	}
}
