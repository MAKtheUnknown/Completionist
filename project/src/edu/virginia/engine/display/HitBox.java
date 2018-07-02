package edu.virginia.engine.display;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.EventDispatcher;
import edu.virginia.engine.events.IEventDispatcher;

public class HitBox extends EventDispatcher
{
	private Shape initBox;
	
	private Shape box;
	private AffineTransform offset;
	
	List<HitBox> subBoxes = new ArrayList<HitBox>();
	
	public HitBox(String id, Shape box)
	{
		super(id);
		
		this.initBox = box;
		this.box = box;
		
		offset = new AffineTransform();
	}
	
	public HitBox(String id, Shape box, AffineTransform offset)
	{
		this(id, box);
		
		this.offset = offset;
	}
	
	
	public boolean collidesWith(HitBox other)
	{
		Area area = new Area(box);
		Area otherArea = new Area(other.box);
		area.intersect(otherArea);
		
		if(!area.isEmpty())
		{
			return true;
		}
		
		for(int i = 0; i < subBoxes.size(); i++)
		{
			HitBox sub = subBoxes.get(i);
			AffineTransform at = sub.offset;
			Shape s = at.createTransformedShape(sub.getBox());
			HitBox h = new HitBox("", s);
			if(h.collidesWith(other))
			{
				return true;
			}
		}
		
		return false;
		
	}
	
	public Area getIntersection(HitBox other)
	{
		Area area = new Area(box);
		Area otherArea = new Area(other.box);
		area.intersect(otherArea);
		
		return area;
	}

	public Shape getBox() {
		return box;
	}

	public void setBox(Shape box) {
		this.box = box;
	}
	
	public void addSubBox(HitBox sub)
	{
		subBoxes.add(sub);
	}
	
	public void update(AffineTransform parentPosition)
	{
		this.box = parentPosition.createTransformedShape(initBox);
		this.box = offset.createTransformedShape(box);
		
		for(HitBox child : subBoxes)
		{
			child.update(parentPosition);
		}
	}
	
	public void draw(Graphics2D g2d)
	{
		//Shape transformedShape = this.offset.createTransformedShape(box);
		//g2d.draw(transformedShape);
		
		g2d.draw(box);
		for(HitBox b : subBoxes)
		{
			b.draw(g2d);
		}
	}
	
	public HitBox getChild(String id)
	{
		return (HitBox)subBoxes.stream().filter(c -> c.getId().equals(id)).toArray()[0];
		
	}
	
	public class CollisionEvent extends Event
	{
		
		private DisplayObject object1;
		private DisplayObject object2;
		
		private Area collisionArea;
		
		public CollisionEvent(IEventDispatcher ed, DisplayObject object1, DisplayObject object2)
		{
			super("Collision", ed);
			
			this.object1 = object1;
			this.object2 = object2;
			
		}
		
	}
	
}
