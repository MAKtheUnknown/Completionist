package edu.virginia.engine.physics;

import java.util.List;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;

public abstract class PhysicsSystem implements IEventListener
{

	protected List<PhysicalSprite> items;
	
	public abstract void applyConstantEffects();
	
	public abstract void collisionTracker();
	
	@Override
	public void handleEvent(Event event) 
	{
		
	}

}
