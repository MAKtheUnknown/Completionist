package edu.virginia.engine.tweening;

public class TweenParam 
{

	private TweenableParam parameter;
	
	private double startValue;
	private double endValue;
	
	private double startTime;
	private double totalTime;
	
	private TweenTransition transition;
	
	public TweenParam(TweenableParam paramToTween, double startVal, double endVal, double time, TweenTransition trans)
	{
		this.parameter = paramToTween;
		this.startValue = startVal;
		this.endValue = endVal;
		this.totalTime = time;
		this.transition = trans;
		
	}
	
	public TweenableParam getParam()
	{
		return parameter;
	}
	
	public double getStartValue()
	{
		return startValue;
	}
	
	public double getEndValue()
	{
		return endValue;
	}
	
	public double getTweenTime()
	{
		return totalTime;
	}
	
	public void setStartTime(double t)
	{
		this.startTime = t;
	}
	
	public double getStartTime()
	{
		return this.startTime;
	}
	
	public TweenTransition getTransition()
	{
		return transition;
	}
	
}
