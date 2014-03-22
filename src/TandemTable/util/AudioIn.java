package TandemTable.util;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import TandemTable.Sketch;
import be.hogent.tarsos.dsp.AudioDispatcher;
import be.hogent.tarsos.dsp.AudioEvent;
import be.hogent.tarsos.dsp.AudioProcessor;
import be.hogent.tarsos.dsp.SilenceDetector;

public class AudioIn implements AudioProcessor {

	Sketch sketch;
	AudioDispatcher dispatcher;
	Mixer mixer;
	SilenceDetector silenceDetector;
	int threshold;
	public int talkingAmount = 1;
	int user;
	long timeOfLastUtter = 0;
	ArrayList<SlidingWindow> windowArray;
	
	// Size of sliding window in milliseconds
	int windowSize = 500;
	// To use sliding window approach
	boolean slidingWindowFlag = true;
	
	ArrayList<Float> pcmData = null;
	
	// For filtering out noise
	float noiseLevel = 0;
	int noiseLevelIndex = 1;
	int delayNoise = 2000;
	long startTime = 0;
	
	public AudioIn(Mixer mixer, int user, Sketch sketch) {
		this.sketch = sketch;
		this.mixer = mixer;
		this.user = user;
		threshold = -65;//SilenceDetector.DEFAULT_SILENCE_THRESHOLD;
		windowArray = new ArrayList<SlidingWindow>();
		pcmData = new ArrayList<Float>();
	}
	
	
	
	public void setMixer(Mixer mixer) throws LineUnavailableException,
		UnsupportedAudioFileException {
	
		if(dispatcher!= null){
			dispatcher.stop();
		}
		
		float sampleRate = 44100;
		int bufferSize = 512;
		int overlap = 0;
		
		
		final AudioFormat format = new AudioFormat(sampleRate, 16, 1, true,
				true);
		final DataLine.Info dataLineInfo = new DataLine.Info(
				TargetDataLine.class, format);
		TargetDataLine line;
		line = (TargetDataLine) mixer.getLine(dataLineInfo);
		final int numberOfSamples = bufferSize;
		line.open(format, numberOfSamples);
		line.start();
		final AudioInputStream stream = new AudioInputStream(line);
		
		// create a new dispatcher
		dispatcher = new AudioDispatcher(stream, bufferSize,
				overlap);
		
		// add a processor, handle percussion event.
		silenceDetector = new SilenceDetector(threshold, false);
		dispatcher.addAudioProcessor(silenceDetector);
		dispatcher.addAudioProcessor(this);
		
		// run the dispatcher (on a new thread).
		new Thread(dispatcher,"Audio dispatching").start();
	}
	
	public void draw() {
		//sketch.background(0);
		sketch.stroke(255);
		sketch.strokeWeight(1);
		ArrayList<Float> tempData = new ArrayList<Float>(pcmData);
		float lastX = 0, lastY = sketch.height/2;
		float mult = 1000;
		float index = 0;
		
		for(int i = 0; i < tempData.size(); i++) {
			float sample = tempData.get(i);
			
			//System.out.println(sample + " " + index);
			sketch.line(lastX, lastY, index, sketch.height/2 + sample*mult);
			lastX = index;
			lastY = sketch.height/2 + sample*mult;
			//sketch.ellipse(index, sketch.height/2 + sample*mult, 0.1f, 0.1f);
			index += 0.01;
		}
		
	}
	
	public void startSoundCapture() {
		try {
			setMixer(mixer);
		} catch (LineUnavailableException e) {
			System.out.println("Microphone target data line is unavailable.");
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			System.out.println("Unsupported Audio File Exception");
			e.printStackTrace();
		}
	}

	@Override
	public boolean process(AudioEvent audioEvent) {
		float[] pcmAudio = audioEvent.getFloatBuffer();
		
		if(startTime == 0) {
			startTime = System.currentTimeMillis();
		
		} else if(System.currentTimeMillis() - startTime < delayNoise) {
			for(int i = audioEvent.getOverlap(); i < pcmAudio.length; i++) {
				if(pcmAudio[i] < 0 && pcmAudio[i] < noiseLevel/noiseLevelIndex) {
					noiseLevel += pcmAudio[i];
					noiseLevelIndex++;
				}
			}
		} else {

			for(int i = audioEvent.getOverlap(); i < pcmAudio.length; i++) {
				if(pcmAudio[i] < 0 && pcmAudio[i] < noiseLevel/(noiseLevelIndex-1)) {
					pcmData.add(pcmAudio[i]);
				} else {
					pcmData.add(0f);
				}
				
			}
		}
		//System.out.println(noiseLevel/noiseLevelIndex);
		handleSound();
		return true;
	}
	
	public void handleSound() {
		if(slidingWindowFlag) {
			long curTime = System.currentTimeMillis();
			windowArray.add(new SlidingWindow(curTime, silenceDetector.currentSPL()));
			
			ArrayList<SlidingWindow> tempWindow = new ArrayList<SlidingWindow>(windowArray);
			//System.arraycopy(windowArray, 0, tempWindow, 0, windowArray.size());
			float windowValues = 0;
			
			for(SlidingWindow w: tempWindow) {
				if(curTime - w.time > windowSize) {
					windowArray.remove(w);
				} else {
					windowValues += w.dbSPL;
				}
			}
			
			
			
			//windowIndex++;
			//System.out.println(windowValues/windowIndex + " " + (System.currentTimeMillis() - timeOfLastSound > windowSize));
			
			
			if(windowValues/windowArray.size() > threshold) {
				detectedUtterance();
			}
		
		} else if(silenceDetector.currentSPL() > threshold){
			detectedUtterance();
		}
		
	}
	
	public void detectedUtterance() {
		talkingAmount++;
		timeOfLastUtter = System.currentTimeMillis();
		System.out.println("Sound detected for " + user + ": " + (int)(silenceDetector.currentSPL()) + "dB SPL, Talking Amount: " + talkingAmount);
	}
	
	@Override
	public void processingFinished() {
		// TODO Auto-generated method stub
		
	}
	
	class SlidingWindow {
		long time;
		double dbSPL;
		
		public SlidingWindow(long time, double dbSPL) {
			this.time = time;
			this.dbSPL = dbSPL;
		}
	}
	

}