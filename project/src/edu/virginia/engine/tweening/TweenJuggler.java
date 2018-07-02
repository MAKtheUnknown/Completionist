package edu.virginia.engine.tweening;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;

public class TweenJuggler implements IEventListener
{
	
	private static TweenJuggler juggler;
	private List<Tween> tweens;
	
	private boolean nexting = false;
	
	public TweenJuggler()
	{
		if(juggler == null)
		{
			juggler = this;
			tweens = new ArrayList<Tween>();
		}
	}
	
	public synchronized void add(Tween t)
	{
		tweens.add(t);
		t.addEventListener(juggler, TweenEvent.END_EVENT);
	}
	
	public synchronized void nextFrame()
	{
		nexting = true;
		try
		{
		for(Tween t : tweens)
		{
			t.update();
		}
		}
		catch(ConcurrentModificationException e){}
		nexting = false;
	}

	@Override
	public synchronized void handleEvent(Event event) 
	{
		if(event instanceof TweenEvent)
		{
			TweenEvent e = (TweenEvent) event;
			if(event.getEventType() == TweenEvent.END_EVENT)
			{
				e.getTween().deactivate();
			}
		}
	}

}
