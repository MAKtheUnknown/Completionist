package completionist;

import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.virginia.engine.display.DisplayObjectContainer;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;

public class LevelManager extends Game
{

	private static LevelManager levelManager;
	
	private List<Level> levels = new ArrayList<Level>();
	private Level currentLevel;
	
	private PlayerCharacter player;
	
	private DisplayObjectContainer view;
	
	private IEventListener bossBeatenCheck = new IEventListener()
			{
				@Override
				public void handleEvent(Event event) 
				{
					LevelManager.getLevelManager().nextLevel();
				}	
			}; 
	
	public LevelManager(String gameId, int width, int height) 
	{
		super(gameId, width, height);
		
		
	}
	
	@Override
	public void start()
	{
		super.start();
		
		levelManager = this;
		
		try 
		{
			player = new PlayerCharacter("Player", "Spritesheets/ProtoPlayer.png", "Spritesheets/ProtoPlayer.spritetext", 1.0);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		player.setPivotPoint(14, 18);
		
		Level sinistar = new SinistarLevel("IAMSINISTAR");
		currentLevel = sinistar;
		currentLevel.setPlayer(player);
		currentLevel.init();
		//currentLevel.addEventListener(bossBeatenCheck, "BOSS_DEFEATED");
		levels.add(sinistar);
		player.setAnimation("ProtoPlayer");
		this.addChild(player);
	}
	
	@Override
	public void update(ArrayList<Integer> pressedKeys)
	{
		super.update(pressedKeys);
		
		if(currentLevel != null)
		{
			currentLevel.controls(pressedKeys);
			currentLevel.update();
		}
	}
	
	@Override
	public void draw(Graphics g)
	{
		super.draw(g);
	}
	
	public void nextLevel()
	{
		currentLevel.tearDown();
		levels.remove(currentLevel);
		
		Random randPaul = new Random();
		currentLevel = levels.get(randPaul.nextInt(levels.size()));
		currentLevel.init();
		currentLevel.addEventListener(bossBeatenCheck, "BOSS_DEFEATED");
		currentLevel.setView(view);
		
	}
	
	public static LevelManager getLevelManager()
	{
		return levelManager;
	}
	
	
	
}
