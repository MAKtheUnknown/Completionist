package edu.virginia.labtests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import edu.virginia.engine.display.DisplayObjectContainer;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.Sprite;

public class LabThreeDemo extends Game
{
	DisplayObjectContainer view;
	
	Sprite sun;
	DisplayObjectContainer sunMotor;
	Sprite earth;
	DisplayObjectContainer earthRotor;
	Sprite moon;
	DisplayObjectContainer moonRotor;
	Sprite iss;
	DisplayObjectContainer issRotor;
	Sprite mercury;
	Sprite venus;
	Sprite mars;
	
	
	
	
	
	
	
	
	
	public LabThreeDemo(String gameId, int width, int height) {
		super(gameId, width, height);
		
		this.initialize();
	}

	public static void main(String[] args) 
	{
		LabThreeDemo game = new LabThreeDemo("Solar System Simulator", 1600, 900);
		game.start();

	}
	
	public void initialize()
	{

		view = new DisplayObjectContainer("View");
		
		this.getScenePanel().setBackground(Color.BLACK);
		
		sunMotor = new DisplayObjectContainer("Sun Motor");
		sunMotor.setPosition(450, 200);
		
		sun = new Sprite("Sun", "sun.png");
		//sun.setScale(.6, .6);
		sun.setPivotPoint(sun.getUnscaledWidth()/2, sun.getUnscaledHeight()/2);
		sunMotor.addChild(sun);
		
		earthRotor = new DisplayObjectContainer("Earth Rotor");
		earthRotor.setPosition(sunMotor.getPositionX(), sunMotor.getPositionY());
		
		earth = new Sprite("Earth", "earth.png");
		earth.setScale(.125, .125);
		earth.setPivotPoint(earth.getUnscaledWidth()/2, earth.getUnscaledHeight()/2);
		earth.setPosition(420, 0);
		earthRotor.addChild(earth);
		
		moonRotor = new DisplayObjectContainer("Moon Rotor");
		moon = new Sprite("Moon", "moon.png");
		moon.setScale(.125, .125);
		moon.setPivotPoint(moon.getUnscaledWidth()/2, moon.getUnscaledHeight()/2);
		moonRotor.setPosition(0, 0);
		moon.setPosition(420, 0);
		moonRotor.addChild(moon);
		earth.addChild(moonRotor);
		

		view.addChild(sunMotor);
		view.addChild(earthRotor);
		
		this.addChild(view);
	}
	
	
	@Override
	public void draw(Graphics g)
	{
		super.draw(g);
		
		
	}
	
	@Override
	public void update(ArrayList<Integer> pressedKeys)
	{
		super.update(pressedKeys);
		
		/*
		System.out.println("EarthRotor: " + earthRotor.getPositionX() + " " + earthRotor.getPositionY());
		System.out.println("Earth: " + earth.getPositionX() + " " + earth.getPositionY());
		System.out.println("SunMotor: " + sunMotor.getPositionX() + " " + sunMotor.getPositionY());
		System.out.println("Sun: " + sun.getPositionX() + " " + sun.getPositionY());
		*/
		
		earthRotor.rotate(Math.PI/100);
		sun.rotate(Math.PI/1000);
		earth.rotate(Math.PI/64);
		moonRotor.rotate(Math.PI/3000);
		moon.rotate(Math.PI/64);
		//earthRotor.rotate(Math.PI/256);
		//earthRotor.rotate(Math.PI/512.0);
		//earth.rotate(Math.PI/256.0);
		
		for(int i : pressedKeys)
		{
			switch(i)
			{
			case KeyEvent.VK_Q:
				view.setScale(view.getScaleX() + .01, view.getScaleY() + .01);
				break;
			case KeyEvent.VK_W:
				view.setScale(view.getScaleX() - .01, view.getScaleY() - .01);
				break;
			case KeyEvent.VK_LEFT:
				view.setPositionX(view.getPositionX()+10);
				break;
			case KeyEvent.VK_RIGHT:
				view.setPositionX(view.getPositionX()-10);
				break;
			case KeyEvent.VK_UP:
				view.setPositionY(view.getPositionY()+10);
				break;
			case KeyEvent.VK_DOWN:
				view.setPositionY(view.getPositionY()-10);
				break;
			}
			
			
		}
		
		if(this.isClicked())
		{
			System.out.println("Click: " + this.getClickX() + " " + this.getClickY());
		}
		
	}
	
	

}
