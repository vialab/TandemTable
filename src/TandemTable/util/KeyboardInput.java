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
		}
		
		if(input == 't') {
			sketch.talkingPrompt.play();
		}
	}
}
