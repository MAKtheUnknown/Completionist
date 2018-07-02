package edu.virginia.engine.tweening;

import java.util.function.Function;

public class TweenTransition 
{
	
	Function<Double, Double> transitionFunction;
	
	
	public TweenTransition(Function<Double, Double> func)
	{
		transitionFunction = func;
	}

	
	public double applyTransition(double x)
	{
		return transitionFunction.apply(x);
	}
	
	public static Function<Double, Double> LINEAR = x -> x;
	public static Function<Double, Double> QUADRATIC = x -> Math.pow(x, 2);
	public static Function<Double, Double> CUBIC = x -> Math.pow(x, 3);
	public static Function<Double, Double> EXPONENTIAL = x -> Math.pow(Math.E, x)/Math.E;
	public static Function<Double, Double> LOGISTIC = x -> 1.0/(1+Math.pow(Math.E, -x));
	
}
