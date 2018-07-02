package edu.virginia.engine.display;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class DisplayObjectContainer extends DisplayObject
{

	List<DisplayObject> children = new ArrayList<>();
	
	
	public DisplayObjectContainer(String id) {
		super(id);
	}
	
	public DisplayObjectContainer(String id, String imageFileName)
	{
		super(id, imageFileName);
	}
	
	/**
	 * Add a new child.
	 * This will become this child's parent.
	 * @param c
	 */
	public void addChild(DisplayObject c)
	{
		c.setParent(this);
		//this.updateTransform();
		//c.updateTransform();
		//Point2D g = c.getGlobalPosition(c.getPositionX(), c.getPositionY());
		//Point l = globalToLocal(g.getX(), g.getY());
		//c.setPositionX(l.getX());
		//c.setPositionY(l.getY());
		children.add(c);
	}
	
	/**
	 * Abandon your child. 
	 * @param c
	 */
	public void removeChild(DisplayObject c)
	{
		c.setParent(null);
		children.remove(c);
	}
	
	/**
	 * Abandon all of your children.
	 */
	public void removeAllChildren()
	{
		children.clear();
	}
	
	@Override
	public void draw(Graphics g)
	{
		super.draw(g);
		
		Graphics2D g2d = (Graphics2D) g;
		super.applyTransformations(g2d);
		
		try
		{
		for(DisplayObject child : children)
		{
			if(child != null)
			{
				child.draw(g2d);
			}
		}
		}
		catch(ConcurrentModificationException e)
		{
			
		}
		
		
		super.reverseTransformations(g2d);
		
		
	}
	
	@Override
	protected void update(ArrayList<Integer> pressedKeys) 
	{
		super.update(pressedKeys);
		
		for(DisplayObject o : children)
		{
			o.update(pressedKeys);
		}
		
	}
	
	public boolean contains(DisplayObject d)
	{
		for(DisplayObject o : children)
		{
			if(d.equals(o))
			{
				return true;
			}
			else if(o instanceof DisplayObjectContainer)
			{
				DisplayObjectContainer doco = (DisplayObjectContainer) o;
				if(doco.contains(d))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Get child by id.
	 * 
	 * @param id
	 * @return
	 */
	public DisplayObject getChild(String id)
	{
		for(DisplayObject c : children)
		{
			if(c.getId().equals(id))
			{
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Get child by index (probably the order added).
	 * 
	 * @param index
	 * @return
	 */
	public DisplayObject getChild(int index)
	{
		return children.get(index);
	}
	
	public Point globalToLocal(double x, double y)
	{
		Point l = null;
		super.updateTransform();
		
		if(this.getParent() != null)
		{
			l = this.getParent().globalToLocal(x-this.getParent().getPositionX(),y-this.getParent().getPositionY());
			this.getLatestTransform().transform(l, l);
		}
		else
		{
			l = new Point((int)x,(int)y);
		}
		
		return l;
		
	}
	
}
