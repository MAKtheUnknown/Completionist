package edu.virginia.engine.display;

import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Nothing in this class (yet) because there is nothing specific to a Sprite yet that a DisplayObject
 * doesn't already do. Leaving it here for convenience later. you will see!
 * */
public class Sprite extends DisplayObjectContainer 
{

	private Rectangle boundingBox;
	
	
	public Sprite(String id) {
		super(id);
	}

	public Sprite(String id, String imageFileName) {
		super(id, imageFileName);
	}
	
	@Override
	public void update(ArrayList<Integer> pressedKeys) {
		super.update(pressedKeys);
		
	}
	
	public boolean isPointInBoundingBox(int x, int y)
	{
		if(boundingBox.contains(x, y))
		{
			return true;
		}
		
		return false;
	}
}
