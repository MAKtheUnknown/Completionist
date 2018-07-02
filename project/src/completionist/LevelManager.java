package completionist;

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
		
		levelManager = this;
	}
	
	public void update(ArrayList<Integer> pressedKeys)
	{
		currentLevel.controls(pressedKeys);
		currentLevel.update();
	}
	
	public void nextLevel()
	{
		levels.remove(currentLevel);
		
		Random randPaul = new Random();
		currentLevel = levels.get(randPaul.nextInt(levels.size()));
		currentLevel.addEventListener(bossBeatenCheck, "BOSS_DEFEATED");
		currentLevel.setView(view);
		
	}
	
	public static LevelManager getLevelManager()
	{
		return levelManager;
	}
	
	
	
}
