package TandemTable.util;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioOut  {
	Clip clip = null;
	AudioInputStream audioInStream;
	String audioFile;
	
	public AudioOut(String audioFile) {
		this.audioFile = audioFile;
	}
	
	public void play() {
		if(clip == null) {
			try {
				clip = AudioSystem.getClip();
				audioInStream = AudioSystem.getAudioInputStream(new File(audioFile));
				clip.open(audioInStream);
			} catch (Exception e) {
				System.out.println("Problem with loading audio file: " + audioFile);
				e.printStackTrace();
			}
		}
		
		clip.setMicrosecondPosition(0);
		clip.start();
		
	}
	
	
}
