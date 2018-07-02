package edu.virginia.engine.events;

public class Event 
{

	private String eventType;
	
	private IEventDispatcher source;

	/**
	 * Create a new Event.
	 * 
	 * @param type of event
	 * @param source of event
	 */
	public Event(String type, IEventDispatcher source)
	{
		this.eventType = type;
		this.source = source;
	}
	
	public Event()
	{
		eventType = "Event";
		source = null;
	}
	
	public String getEventType() 
	{
		return eventType;
	}

	public void setEventType(String eventType) 
	{
		this.eventType = eventType;
	}

	public IEventDispatcher getSource() {
		return source;
	}

	public void setSource(IEventDispatcher source) {
		this.source = source;
	}
	
	
	
}
