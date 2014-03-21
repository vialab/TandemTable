package TandemTable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;

public class Audio {

	Sketch sketch;
	
	public Audio(Sketch sketch) {
		this.sketch = sketch;
		
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
	    List<Line.Info> availableLines = new ArrayList<Line.Info>();
	    
	    for (Mixer.Info mixerInfo : mixers){
	        
	        	
		    	System.out.println("Found Mixer: " + mixerInfo);
	
		        Mixer m = AudioSystem.getMixer(mixerInfo);
	
		        Line.Info[] lines = m.getTargetLineInfo();
	
		        for (Line.Info li : lines){
		            System.out.println("Found target line: " + li);
		            
		            try {
		            
		            	if(mixerInfo.toString().startsWith("Microphone")) {
			                m.open();
			                TargetDataLine tLine = (TargetDataLine) m.getLine(li);
			                tLine.open();
			                
			                ByteArrayOutputStream out  = new ByteArrayOutputStream();
			                int numBytesRead;
			                byte[] data = new byte[tLine.getBufferSize() / 5];
	
			                // Begin audio capture.
			                tLine.start();
			                int index = 1;
			                // Here, stopped is a global boolean set by another thread.
			                while (index < 1) {
			                   // Read the next chunk of data from the TargetDataLine.
			                   numBytesRead =  tLine.read(data, 0, data.length);
			                   // Save this chunk of data.
			                   out.write(data, 0, numBytesRead);
			                   index++;
			                }     
			                
			                System.out.println(out.toString());
		            	}
		            	
		                availableLines.add(li);                  
		            } catch (LineUnavailableException e){
		                System.out.println("Line unavailable.");
		            }
		        }  
	        }
		  //if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
		    //    System.out.println("A");
		    //}
	
		    System.out.println("Available lines: " + availableLines);
	    }
	    
	    
		
	}
	
	


