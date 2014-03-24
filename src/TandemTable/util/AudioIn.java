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
	int noiseLevelIndex = 0;
	int delayNoise = 3000;
	long startTime = 0;
	
	// Maximum noise level detected
	float maxNoiseLvl = 0;
	// List of successful utterances
	ArrayList<Utterance> utterArray;
	// Current utterance
	Utterance curUtter = null;
	// If the utterance has started
	boolean startedUtter = false;
	// Pseudo time threshold for combining two groups of sound
	// Some words have more than one group of sound
	int combineUtterTheshold = 10;
	// Pseudo time for utterances
	int utterTime = 0;
	// If the two groups of sound should be combined
	boolean combineSounds = false;
	// Pseudo time length of utterance threshold
	int utterLengthThresh = 100;
	// If last sound was added to the utterArray
	boolean utterAdded = false;

	
	
	public AudioIn(Mixer mixer, int user, Sketch sketch) {
		this.sketch = sketch;
		this.mixer = mixer;
		this.user = user;
		threshold = -65;//SilenceDetector.DEFAULT_SILENCE_THRESHOLD;
		windowArray = new ArrayList<SlidingWindow>();
		pcmData = new ArrayList<Float>();
		utterArray = new ArrayList<Utterance>();
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
		float mult = 2000;
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
		
		// Draw captured utterances
		ArrayList<Utterance> tempUtter = new ArrayList<Utterance>(utterArray);
		float y = (float) ((sketch.height/2)*0.75);
		mult = 2000;
		int colour = 140;
		boolean switchFlag = false;
		
		for(int i = 0; i < tempUtter.size(); i++) {
			Utterance utterance = tempUtter.get(i);
			
			sketch.stroke(utterance.r, utterance.g, utterance.b);
			
			sketch.line(utterance.startTime/100, y, utterance.endTime/100, y);
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
		int overlap = audioEvent.getOverlap();
		
		if(startTime == 0) {
			startTime = System.currentTimeMillis();
		
		} else if(System.currentTimeMillis() - startTime < delayNoise) {
			for(int i = overlap; i < pcmAudio.length; i++) {
				if(pcmAudio[i] < 0) { 
					noiseLevel += pcmAudio[i];
					noiseLevelIndex++;
					
					if(pcmAudio[i] < maxNoiseLvl) {
						maxNoiseLvl = pcmAudio[i];
					}
				}
			}
		} else {
			
			for(int i = overlap; i < pcmAudio.length; i++) {
				float value = pcmAudio[i];
				
				if(value < 0 ) {
					utterTime++;
					
					if(value < maxNoiseLvl) {
						
						if(!startedUtter) {
							startedUtter = true;
							//long timeNow = System.currentTimeMillis();
							
							if(curUtter == null || utterArray.size() == 0 || utterTime - curUtter.getEndTime() >= combineUtterTheshold) {
								curUtter = new Utterance(utterTime);
								utterAdded = false;
							} else {
								System.out.println(utterTime - curUtter.getEndTime() + " " + combineUtterTheshold);
								combineSounds = true;
							}
						}
						
						if(combineSounds && utterAdded) {
							utterArray.get(utterArray.size() - 1).addFloat(value);
						} else {
							curUtter.addFloat(value);
						}
						
						pcmData.add(value);
					} else {
						
						if(startedUtter) {							
							if(combineSounds) {
								utterArray.get(utterArray.size() - 1).setEndTime(utterTime);
								System.out.println("Combined sounds - Utterance number " + utterArray.size() + " ended at " + utterTime); //System.currentTimeMillis());
							
							} else {
								curUtter.setEndTime(utterTime);
								
								if(curUtter.getEndTime() - curUtter.getStartTime() > utterLengthThresh) {
									utterArray.add(curUtter);
									utterAdded = true;
									System.out.println("Utterance number " + utterArray.size() + " ended at " + utterTime); //System.currentTimeMillis());
								} else {
									utterAdded = false;
								}
							}
						}
						
						startedUtter = false;
						combineSounds = false;
						pcmData.add(0f);
					}
				}
				
			}
			
			//detectUtterance(overlap);
		}
		
		//handleSoundDetection();
		return true;
	}
	
	public void detectUtterance(int overlap) {
		//long curTime = System.currentTimeMillis();
		//utterArray.add(new SlidingWindow(curTime, ));
	}
	
	public void handleSoundDetection() {
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
	
	public int getUtterNumber() {
		return utterArray.size();
	}
	
	class SlidingWindow {
		long time;
		double dbSPL;
		
		public SlidingWindow(long time, double dbSPL) {
			this.time = time;
			this.dbSPL = dbSPL;
		}
	}
	
	class Utterance {
		int startTime = -1;
		int endTime = -1;
		ArrayList<Float> pcmData;
		int r, g, b;
		
		public Utterance(int startTime) {
			this.startTime = startTime;
			pcmData = new ArrayList<Float>();
			r = (int) sketch.random(0, 256);
			g = (int) sketch.random(0, 256);
			b = (int) sketch.random(0, 256);
			
		}
		
		public void addFloat(float value) {
			pcmData.add(value);
		}
		
		public void setEndTime(int endTime) {
			this.endTime = endTime;
		}
		
		public int getEndTime() {
			return endTime;
		}
		
		public int getStartTime() {
			return startTime;
		}
	}
	

}