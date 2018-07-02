package edu.virginia.engine.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.events.Event;

public class EventDispatcher implements IEventDispatcher
{

	private String id;

	private Map<String, ArrayList<IEventListener>> listeners = new HashMap<String, ArrayList<IEventListener>>();
	
	public EventDispatcher(String id) 
	{
		this.id = id;
	}
	
	@Override
	public void addEventListener(IEventListener listener, String eventType) 
	{
		if(listeners.containsKey(eventType) == false)
		{
			listeners.put(eventType, new ArrayList<IEventListener>());
		}
		
		listeners.get(eventType).add(listener);
		
	}

	@Override
	public void removeEventListener(IEventListener listener, String eventType) 
	{
		if(listeners.containsKey(eventType))
		{
			listeners.get(eventType).remove(listener);
		}
		
		
	}

	@Override
	public void dispatchEvent(Event event) 
	{
		String type = event.getEventType();
		
		ArrayList<IEventListener> affectedListeners = listeners.get(type);
		
		if(affectedListeners != null)
		{
		for(IEventListener l : affectedListeners)
		{
			l.handleEvent(event);
		}
		}
		
	}

	@Override
	public boolean hasEventListener(IEventListener listener, String eventType) 
	{
		if(listeners.containsKey(eventType))
		{
			if(listeners.get(eventType).contains(listener))
			{
				return true;
			}
		}
		
		return false;
		
	}

	public String getId() 
	{
		return id;
	}
	
	
	
}
