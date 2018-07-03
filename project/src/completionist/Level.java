package completionist;

import java.awt.event.KeyListener;
import java.util.ArrayList;

import edu.virginia.engine.display.DisplayObjectContainer;
import edu.virginia.engine.events.EventDispatcher;

/**
 * 
 * An abstract class for a Level.
 * Extend it into your own level.
 * init() is called when the level starts.
 * controls() will allow you to handle pressed key events.
 * update() for everything else.
 * Add display objects to the view.
 * When the boss is defeated, dispatch an event with the title "BOSS_DEFEATED"
 * 
 * @author Michael
 *
 */
public abstract class Level extends EventDispatcher
{
	

	public PlayerCharacter player;
	
	public DisplayObjectContainer view;
	
	public Level(String id) 
	{
		super(id);
	}
	
	public abstract void init();
	
	public abstract void controls(ArrayList<Integer> pressedKeys);
	
	public abstract void update();

	public PlayerCharacter getPlayer() {
		return player;
	}

	public void setPlayer(PlayerCharacter player) {
		this.player = player;
	}

	public DisplayObjectContainer getView() {
		return view;
	}

	public void setView(DisplayObjectContainer view) {
		this.view = view;
	}
	
	
}
