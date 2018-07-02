package edu.virginia.engine.util;

import java.awt.geom.Point2D;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;;

public class CollisionEvent extends Event
{
	
	private DisplayObject otherObject;
	private Point2D collisionPoint;

	public CollisionEvent(IEventDispatcher dispatcher, DisplayObject otherObject, Point2D collisionPoint)
	{
		super("Collision", dispatcher);
		
		this.otherObject = otherObject;
		this.collisionPoint = collisionPoint;
	}

	public DisplayObject getOtherObject() 
	{
		return otherObject;
	}

	public void setOtherObject(DisplayObject otherObject) 
	{
		this.otherObject = otherObject;
	}

	public Point2D getCollisionPoint() 
	{
		return collisionPoint;
	}

	public void setCollisionPoint(Point2D collisionPoint) 
	{
		this.collisionPoint = collisionPoint;
	}
	
	
	
}
