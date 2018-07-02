package edu.virginia.engine.display;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

/**
 * 
 * This class allows animated sprites to be created from spritesheets.
 * 
 * @author Michael A. Klaczynski
 * 
 *
 */
public class AnimatedSprite extends Sprite
{
	
	BufferedImage spriteSheet;
	File infoFile;
	List<String> sheetDescriptionLines = new ArrayList<String>();
	String sheetDescription = "";
	
	BufferedImage[] allFrames;
	
	List<BufferedImage> allFramesTempList = new ArrayList<BufferedImage>();
	
	boolean playing = true;

	Map<String, AnimationIndecies> animations = new HashMap<>();
	
	AnimationIndecies currentAnimation;
	int currentFrameIndex;
	BufferedImage currentFrame;
	double timer = 0;
	
	/**
	 * 
	 * This method creates the sprite using a spritesheet and a text file describing the spritesheet.
	 * 
	 * In the text file, to add a frame, type:
	 * FRAME
	 * x
	 * y
	 * width
	 * height
	 * ADD
	 * 
	 * Replace x, y, width and height with their actual values.
	 * 
	 * To specify an animation, type:
	 * ANIMATION
	 * animationName
	 * startIndex
	 * stopIndex
	 * rate
	 * repeat	<-- Type true or false here
	 * ADD
	 * 
	 * @param id
	 * @param spriteSheetName for image file
	 * @param sheetDescriptionFile for text file
	 * @throws IOException
	 */
	public AnimatedSprite(String id, String spriteSheetName, String sheetDescriptionFile) throws IOException 
	{
		super(id);
		
		if(spriteSheetName != null)
		{
		spriteSheet = this.readImage(spriteSheetName);
		
		infoFile = new File("resources/" + sheetDescriptionFile);
		
		BufferedReader br = new BufferedReader(new FileReader(infoFile));
		
		br.lines().filter(s -> !s.isEmpty()).forEach(s -> sheetDescriptionLines.add(s));
		
		CreationStage stage = CreationStage.HEADER;
		SetVarStage var = SetVarStage.NONE;
		
		int newFrameX = 0;
		int newFrameY = 0;
		int newFrameWidth = 0;
		int newFrameHeight = 0;
		
		String newAnimationName = "";
		int newAnimationStart = 0;
		int newAnimationStop = 0;
		double newAnimationRate = 0;
		boolean newAnimationRepeats = false;
		
		for(String s : sheetDescriptionLines)
		{
			switch(var)
			{
			case X:
				newFrameX = Integer.parseInt(s);
				var = SetVarStage.Y;
				break;
			case Y:
				newFrameY = Integer.parseInt(s);
				var = SetVarStage.WIDTH;
				break;
			case WIDTH:
				newFrameWidth = Integer.parseInt(s);
				var = SetVarStage.HEIGHT;
				break;
			case HEIGHT:
				newFrameHeight = Integer.parseInt(s);
				var = SetVarStage.NONE;
				break;
			case NAME:
				newAnimationName = s;
				var = SetVarStage.START;
				break;
			case START:
				newAnimationStart = Integer.parseInt(s);
				var = SetVarStage.STOP;
				break;
			case STOP:
				newAnimationStop = Integer.parseInt(s);
				var = SetVarStage.RATE;
				break;
			case RATE:
				newAnimationRate = Double.parseDouble(s);
				var = SetVarStage.REPEATS;
				break;
			case REPEATS:
				newAnimationRepeats = Boolean.parseBoolean(s);
				var = SetVarStage.NONE;
				break;
			}
			
			switch(s)
			{
			case "FRAME":
				stage = CreationStage.FRAME;
				var = SetVarStage.X;
				break;
			case "ANIMATION":
				stage = CreationStage.ANIMATION;
				var = SetVarStage.NAME;
				break;
			case "ADD":
				switch(stage)
				{
				case FRAME:
					addFrame(newFrameX, newFrameY, newFrameWidth, newFrameHeight);
					break;
				case ANIMATION:
					addAnimation(newAnimationName, newAnimationStart, newAnimationStop, newAnimationRate, newAnimationRepeats);
					break;
				}
			}
			
		}
		
		allFrames = new BufferedImage[allFramesTempList.size()];
		for(int i = 0; i < allFramesTempList.size(); i++)
		{
			allFrames[i] = allFramesTempList.get(i);
		}
		
		currentFrame = allFrames[0];
		}
	}
	
	/**
	 * 
	 * Set animation using the name specified in the text file.
	 * 
	 * @param animationName
	 */
	public void setAnimation(String animationName)
	{
		currentAnimation = animations.get(animationName);
		currentFrameIndex = currentAnimation.startIndex;
		currentFrame = allFrames[currentAnimation.startIndex];
	}
	
	public void pauseAnimation(boolean pause)
	{
		playing = !pause;
	}
	
	public void setAnimationSpeed(double frequency)
	{
		this.currentAnimation.rate = frequency;
	}
	
	public void turnLeft(boolean left)
	{
		if(left == true)
		{
			this.setScaleX(-Math.abs(this.getScaleX()));
		}
		else
		{
			this.setScaleX(Math.abs(this.getScaleX()));
		}
	}
	
	@Override
	public void update(ArrayList<Integer> pressedKeys) 
	{
		super.update(pressedKeys);
		
		timer += 1.0/66.0;
		
		if(playing)
		{
			double m = currentAnimation.rate;
			double i = timer % m;
			if(i < timer)
			{
				incrementAnimation();
			}
		}
		
		super.setImage(currentFrame);
		
	}
	
	private void addFrame(int x, int y, int width, int height)
	{
		allFramesTempList.add(spriteSheet.getSubimage(x, y, width, height));
	}
	
	private void addAnimation(String name, int start, int stop, double rate, boolean repeats)
	{
		animations.put(name, new AnimationIndecies(start, stop, rate, repeats));
	}
	
	
	private void incrementAnimation()
	{
		currentFrameIndex++;
		if(currentFrameIndex > currentAnimation.endIndex)
		{
			if(currentAnimation.repeat)
			{
				currentFrameIndex = currentAnimation.startIndex;
			}
			else
			{
				currentFrameIndex--;
			}
		}
		
		currentFrame = allFrames[currentFrameIndex];
	}
	
	
	/**
	 * The start and stop indecies in the allFrames array for a certain animation.
	 *
	 */
	private class AnimationIndecies
	{
		int startIndex;
		int endIndex;
		double rate;
		boolean repeat;
		
		AnimationIndecies(int start, int stop, double rate, boolean rep)
		{
			this.startIndex = start;
			this.endIndex = stop;
			this.rate = rate;
			this.repeat = rep;
		}
	}
	
	enum CreationStage
	{
		HEADER,
		FRAME,
		ANIMATION,
		ADD
	}
	
	enum SetVarStage
	{
		NONE,
		X,
		Y,
		WIDTH,
		HEIGHT,
		NAME,
		START,
		STOP,
		RATE,
		REPEATS
	}
	

}
