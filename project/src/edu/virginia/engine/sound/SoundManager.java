package edu.virginia.engine.sound;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager 
{

	private static SoundManager singleSoundManager;
	
	private Map<String, Clip> soundFX;
	private Map<String, Clip> music;
	
	private Clip currentMusic = null; 
	
	public SoundManager()
	{
		if(singleSoundManager != null)
		{
			singleSoundManager = this;
		}
		
		soundFX = new HashMap<String, Clip>();
		music = new HashMap<String, Clip>();
	}
	
	public void loadSoundEffect(String id, String filename) throws IOException, LineUnavailableException, UnsupportedAudioFileException
	{
		File soundFile = new File("resources/" + filename);
		AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundFile);
		AudioFormat format = audioInput.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip clip = (Clip)AudioSystem.getLine(info);
		clip.open(audioInput);
		
		soundFX.put(id, clip);
	}
	
	
	public void playSoundEffect(String id)
	{
		Clip clip = soundFX.get(id);
		clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}
	
	public void loadMusic(String id, String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		File soundFile = new File("resources/" + filename);
		AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundFile);
		AudioFormat format = audioInput.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip clip = (Clip)AudioSystem.getLine(info);
		clip.open(audioInput);
		
		music.put(id, clip);
	}
	
	public void playMusic(String id)
	{
		if(currentMusic != null)
		{
			currentMusic.stop();
		}
		
		Clip clip = music.get(id);
		clip.stop();
		clip.setFramePosition(0);
		clip.start();
		
		currentMusic = clip;
	}
}
