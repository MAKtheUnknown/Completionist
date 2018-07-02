package edu.virginia.engine.tweening;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.events.EventDispatcher;

public class Tween extends EventDispatcher
{

	private DisplayObject changedObject;
	private List<TweenParam> parameters = new ArrayList<TweenParam>();
	private boolean active = true;
	
	public Tween(DisplayObject o)
	{
		super("Tween");
		this.changedObject = o;
	}
	
	
	
	public void animate(TweenableParam fieldToAnimate, double startVal, double endVal, double time, TweenTransition transition)
	{
		TweenParam tp = new TweenParam(fieldToAnimate, startVal, endVal, time, transition);
		parameters.add(tp);
		Date d = new Date();
		tp.setStartTime(((double)d.getTime())/1000);
	}
	
	public void update()
	{
		if(active)
		{
		for(TweenParam p : parameters)
		{
			double timeSinceStart = ((double)(new Date()).getTime())/1000 - p.getStartTime();
			double portionComplete = timeSinceStart/p.getTweenTime();
			double tweenPortionComplete = p.getTransition().applyTransition(portionComplete);
			
			if(portionComplete >= 1)
			{
				tweenPortionComplete = 1;
				this.dispatchEvent(new TweenEvent(TweenEvent.END_EVENT, this));
			}
			else
			{
			
			double param = (p.getEndValue()-p.getStartValue()) * tweenPortionComplete + p.getStartValue();
			
			switch(p.getParam())
			{
			case X:
				changedObject.setPositionX(param);
				break;
			case Y:
				changedObject.setPositionY(param);
				break;
			case SCALE_X:
				changedObject.setScaleX(param);
				break;
			case SCALE_Y:
				changedObject.setScaleY(param);
				break;
			case ROTATION:
				changedObject.setRotation(param);
				break;
			case ALPHA:
				changedObject.setAlpha(param);
				break;
			}
			}
		}
		}
	}
	
	public boolean isComplete()
	{
		for(TweenParam p : parameters)
		{
			if(((double)(new Date()).getTime())/1000 - p.getStartTime() < p.getTweenTime())
			{
				return false;
			}
		}
		return true;
	}
	
	public void deactivate()
	{
		active = false;
	}
	
}
