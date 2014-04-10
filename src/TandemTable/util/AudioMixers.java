package TandemTable.util;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public class AudioMixers {

	Mixer[] mixers;
	final String INPUT = "Microphone";
	final String BRAND = "C-Media";
	int mixerIndex = 0;
	
	
	public AudioMixers(int numMixers) {
		mixers = new Mixer[numMixers];
		getMicrophones();
	}
	
	private void getMicrophones() {
		
		Mixer.Info[] mixersInfo = AudioSystem.getMixerInfo();
	    
	    for (Mixer.Info mixerInfo : mixersInfo){
	    	//System.out.println("Found Mixer: " + mixerInfo);

	        Mixer m = AudioSystem.getMixer(mixerInfo);
	       // Line.Info[] lines = m.getTargetLineInfo();

	        //for (Line.Info li : lines){
	           // System.out.println("Found target line: " + li);
	            
	            //try {
	            
	            	if(mixerInfo.getName().startsWith(INPUT) && mixerInfo.getName().contains(BRAND) && !m.isOpen() && mixerIndex < mixers.length) {
	            		mixers[mixerIndex++] = m;
		                System.out.println("Found Mixer: " + mixerInfo);
		                //return;
	            		
	            		//m.open();
		                //tLine = (TargetDataLine) m.getLine(li);
		                //tLine.open();
		                //System.out.println("Opened target line: " + li + " from mixer: " + mixerInfo);
	            	}
	            	
	            //} catch (LineUnavailableException e){
	           //     System.out.println("Line unavailable.");
	           // }
	        //}  
	     }
	}
	
	public Mixer[] getMixers() {
		return mixers;
	}
}
