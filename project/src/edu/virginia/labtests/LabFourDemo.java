package edu.virginia.labtests;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.EventDispatcher;
import edu.virginia.engine.events.IEventDispatcher;
import edu.virginia.engine.events.IEventListener;

public class LabFourDemo extends Game
{

	AnimatedSprite stickMan;
	Coin coin;
	QuestManager questManager;
	boolean questIsComplete = false;
	
	public LabFourDemo(String gameId, int width, int height) {
		super(gameId, width, height);
		
		initialize();
	}

	public static void main(String[] args) 
	{
		LabFourDemo demo = new LabFourDemo("Coin Collector Quest", 1800, 900);
		
		demo.start();

	}
	
	public void initialize()
	{
		
		this.getScenePanel().setBackground(Color.WHITE);
		
		try 
		{
			stickMan = new AnimatedSprite("Stickman", "stickman.png", "stickman.spritetext");		
			coin = new Coin("Coin", "coin.png", "coin.spritetext");
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		this.addChild(stickMan);
		this.addChild(coin);
		stickMan.setAnimation("Standing");
		stickMan.setScale(2, 2);
		coin.setAnimation("Spin");
		coin.setScale(2, 2);
		coin.setPivotPoint(5, 5);
		
		
		questManager = new QuestManager();
		coin.addEventListener(questManager, PickedUpEvent.EVENT_NAME);;
		
	}
	
	@Override
	public void update(ArrayList<Integer> pressedKeys)
	{
		super.update(pressedKeys);
		
		for(Integer key : pressedKeys)
		{
			switch(key)
			{
			case KeyEvent.VK_LEFT:
				stickMan.translate(-10, 0);
				stickMan.setAnimation("Running");
				//stickMan.turnLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				stickMan.translate(10, 0);
				stickMan.setAnimation("Running");
				stickMan.turnLeft(false);
				break;
			case KeyEvent.VK_UP:
				stickMan.translate(0, -5);
				break;
			case KeyEvent.VK_DOWN:
				stickMan.translate(0, 5);
				break;
			}
		}
		if(!pressedKeys.contains(KeyEvent.VK_LEFT) && 
				!pressedKeys.contains(KeyEvent.VK_RIGHT))
		{
			stickMan.setAnimation("Standing");
		}
	}
	
	@Override 
	public void draw(Graphics g)
	{
		super.draw(g);
		Graphics2D g2d = (Graphics2D) g;FontRenderContext frc;
		Font normalFont = new Font("Courier", Font.BOLD, 48);
		frc = g2d.getFontRenderContext();
		TextLayout completeLabel = new TextLayout("Quest is complete!", normalFont, frc);
		
		if(questIsComplete)
		{
			completeLabel.draw(g2d, 100, 48);
		}
	}
	
	private class QuestManager implements IEventListener
	{

		@Override
		public void handleEvent(Event event) 
		{
			questIsComplete = true;
			
			System.out.println("Quest is complete!");
			
			
		}
			
	}
	
	private class PickedUpEvent extends Event
	{
		
		public PickedUpEvent(IEventDispatcher ied)
		{
			super(EVENT_NAME, ied);
		}
		
		public static final String EVENT_NAME = "Pick Up";
	}
	
	private class Coin extends EventDispatcher
	{

		public Coin(String id, String spriteSheetName, String sheetDescriptionFile) throws IOException 
		{
		 	super(id, spriteSheetName, sheetDescriptionFile);
			
		 	Random rand = new Random();
			int coinX = rand.nextInt(500);
			int coinY = rand.nextInt(300);
			setPosition(coinX, coinY);
		}
		
		@Override
		public void update(ArrayList<Integer> pressedKeys)
		{
			super.update(pressedKeys);
			
			
			if(this.collidesWith(stickMan))
			{
				dispatchEvent(new PickedUpEvent(this));
				
				coin.setVisible(false);
			}
			
			if(stickMan.getHitBox() != null)
			{
			if(stickMan.getHitBox().intersects(0,0,16000,1))
			{
				stickMan.translate(0, 20);
			}
			if(stickMan.getHitBox().intersects(0,0,1,16000))
			{
				stickMan.translate(20, 0);
			}
			if(stickMan.getHitBox().intersects(1800,0,1,16000))
			{
				stickMan.translate(-20, 0);
			}
			if(stickMan.getHitBox().intersects(0,900,16000,1))
			{
				stickMan.translate(0, -20);
			}
			}
			
		}
		
	}

}
