package edu.virginia.engine.tweening;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;

public class TweenEvent extends Event
{
	private Tween tween;
	
	public TweenEvent(String id, Tween observed)
	{
		super(id, observed);
		tween = observed;
		
	}
	
	public Tween getTween()
	{
		return tween;
	}
	
	public final static String END_EVENT = "Tween End";
	
}
