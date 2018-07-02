package edu.virginia.labtests;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.HitBox;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.EventDispatcher;
import edu.virginia.engine.events.IEventDispatcher;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.physics.PhysicalSprite;
import edu.virginia.engine.sound.SoundManager;
import edu.virginia.engine.tweening.Tween;
import edu.virginia.engine.tweening.TweenEvent;
import edu.virginia.engine.tweening.TweenTransition;
import edu.virginia.engine.tweening.TweenableParam;

public class LabFiveDemo extends Game
{

	PhysicalSprite stickMan;
	Coin coin;
	QuestManager questManager;
	List<Sprite> platforms;
	SoundManager soundManager = new SoundManager();
	
	boolean questIsComplete = false;
	
	boolean jumping = false;
	
	double plat12X = 5;
	
	public LabFiveDemo(String gameId, int width, int height) {
		super(gameId, width, height);
		
		initialize();
	}

	public static void main(String[] args) 
	{
		LabFiveDemo demo = new LabFiveDemo("Coin Collector Quest", 1800, 900);
		
		demo.start();

	}
	
	public void initialize()
	{
		
		this.getScenePanel().setBackground(Color.WHITE);
		
		try 
		{
			stickMan = new PhysicalSprite("Stickman", "stickman.png", "stickman.spritetext", 1);		
			coin = new Coin("Coin", "coin.png", "coin.spritetext");
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
		this.addChild(stickMan);
		this.addChild(coin);
		stickMan.setAnimation("Standing");
		//stickMan.setScale(2, 2);
		stickMan.setHitBox(new HitBox("main", new Rectangle(0,0, 20, 30)));
		stickMan.setPositionY(600);
		AffineTransform footTransform = new AffineTransform();
		footTransform.translate(0, 120);
		stickMan.addSubHitBox("Feet", 
				new Rectangle(0, 0, 15, 4), 
				footTransform);
		stickMan.addSubHitBox("SubFeet", new Rectangle(0, 5, 30, 10), footTransform);
		
		coin.setAnimation("Spin");
		coin.setScale(4, 4);
		coin.setPivotPoint(5, 5);
		
		
		questManager = new QuestManager();
		coin.eventDispatcher.addEventListener(questManager, PickedUpEvent.EVENT_NAME);
		
		platforms = new ArrayList<Sprite>();
		for(int i = 0; i < 24; i++)
		{
			Sprite p = new Sprite("Platform", "Platform1.png");
			platforms.add(p);
			p.setScale(4, 4);
			p.setHitBox(new HitBox("Platform", new Rectangle(0, 0, 25, 5)));
			this.addChild(p);
		}
		platforms.get(0).setPosition(100, 100);
		platforms.get(1).setPosition(40, 320);
		platforms.get(2).setPosition(120, 360);
		platforms.get(3).setPosition(240, 320);
		platforms.get(4).setPosition(100, 720);
		platforms.get(5).setPosition(120, 580);
		platforms.get(6).setPosition(700, 320);
		platforms.get(7).setPosition(540, 320);
		platforms.get(8).setPosition(400, 320);
		platforms.get(9).setPosition(500, 420);
		platforms.get(10).setPosition(300, 800);
		platforms.get(11).setPosition(300, 620);
		platforms.get(12).setPosition(0, 500);
		platforms.get(14).setPosition(800, 400);
		platforms.get(16).setPosition(700, 500);
		platforms.get(16).setPosition(600, 500);
		platforms.get(17).setPosition(600, 700);
		platforms.get(18).setPosition(500, 900);
		platforms.get(19).setPosition(500, 500);
		platforms.get(20).setPosition(600, 600);
		platforms.get(21).setPosition(500, 700);
		platforms.get(22).setPosition(400, 800);
		
		Sprite floor = new Sprite("Floor", "LongPlatform.png");
		floor.setPosition(0, 800);
		floor.setScale(4, 4);
		floor.setHitBox(new HitBox("Platform", new Rectangle(0,0, 1600, 300)));
		platforms.add(floor);
		this.addChild(floor);

		Sprite moreFloor = new Sprite("Floor", "LongPlatform.png");
		moreFloor.setPosition(800, 800);
		moreFloor.setScale(4, 4);
		this.addChild(moreFloor);
		
		try 
		{
			soundManager.loadMusic("Win Theme", "MHWGO-opening.wav");
			soundManager.loadMusic("Main", "DWDTD.wav");
			soundManager.loadSoundEffect("Jump Sound", "jump.wav");
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
		{
			e.printStackTrace();
		}
		
		//soundManager.playMusic("Main");
		
		Tween stickManInit = new Tween(stickMan);
		stickManInit.animate(TweenableParam.X, 1600, 16, 2, new TweenTransition(TweenTransition.LINEAR));
		stickManInit.animate(TweenableParam.Y, 0, 720, 2, new TweenTransition(TweenTransition.LINEAR));
		stickManInit.animate(TweenableParam.SCALE_X, 8, 4, 2, new TweenTransition(TweenTransition.LINEAR));
		stickManInit.animate(TweenableParam.SCALE_Y, 8, 4, 2, new TweenTransition(TweenTransition.LINEAR));
		stickManInit.animate(TweenableParam.ALPHA, 0, 1, 2, new TweenTransition(TweenTransition.LINEAR));
		this.getTweenJuggler().add(stickManInit);
		
		stickManInit.addEventListener(new IEventListener(){
			@Override
			public void handleEvent(Event event) {
				stickMan.setVelocityY(0);
			}}, TweenEvent.END_EVENT);
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
				stickMan.translate(-4, 0);
				stickMan.setAnimation("Running");
				//stickMan.turnLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				stickMan.translate(4, 0);
				stickMan.setAnimation("Running");
				stickMan.turnLeft(false);
				break;
			case KeyEvent.VK_UP:
				if(jumping == false)
				{
					stickMan.setVelocityY(-8);
					jumping = true;
					soundManager.playSoundEffect("Jump Sound");
				}
				break;
			case KeyEvent.VK_DOWN:
				stickMan.translate(0, 15);
				break;
			}
		}
		if(!pressedKeys.contains(KeyEvent.VK_LEFT) && 
				!pressedKeys.contains(KeyEvent.VK_RIGHT))
		{
			stickMan.setAnimation("Standing");
		}
		
		if(stickMan.getHitBox() != null)
		{
			if(stickMan.getHitBox().getBox().intersects(0,0,16000,1))
			{
				stickMan.setVelocityY(0);
			}
			if(stickMan.getHitBox().getBox().intersects(0,0,1,16000))
			{
				//stickMan.translate(20, 0);
			}
			if(stickMan.getHitBox().getBox().intersects(3600,0,1,16000))
			{
				//stickMan.translate(-20, 0);
			}
			if(stickMan.getHitBox().getBox().intersects(0, 0,16000,1))
			{	
				stickMan.setVelocityY(0);
			}
		}
		
		platforms.get(12).translate(plat12X, 0);
		if(platforms.get(12).getHitBox().getBox().intersects(1800,0,1,16000))
		{
			platforms.get(12).translate(-10, 0);
			plat12X = -plat12X;
		}
		else if (platforms.get(12).getHitBox().getBox().intersects(0,0,1,16000))
		{
			platforms.get(12).translate(10, 0);
			plat12X = -plat12X;
		}

		boolean applyGravity = true;
		
		for(Sprite platform : platforms)
		{
			if(stickMan.getHitBox().getChild("Feet").collidesWith(platform.getHitBox()))
			{
				double y = stickMan.getHitBox().getIntersection(platform.getHitBox()).getBounds2D().getHeight();
				//stickMan.translate(0, -5);
				//stickMan.setVelocityY(0);
				//stickMan.applyForce(new Point2D.Double(0, -5.8/66));
				jumping = false;
			}
			
			if(stickMan.getHitBox().getChild("SubFeet").collidesWith(platform.getHitBox()))
			{
				jumping = false;
				applyGravity = false;
			}
			
			/*
			else if(stickMan.getHitBox().collidesWith(platform.getHitBox()))
			{
				stickMan.getVelocity().setLocation(-stickMan.getVelocity().getX(),
						-stickMan.getVelocity().getY());
			}*/
		}
		
		if(applyGravity)
		{
			stickMan.applyForce(new Point2D.Double(0, 15.0/66));
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
			
			soundManager.playMusic("Win Theme");
			
			Tween t = new Tween(coin);
			t.animate(TweenableParam.X, coin.getPositionX(), 800.0, 4.0, new TweenTransition(TweenTransition.QUADRATIC));
			t.animate(TweenableParam.Y, coin.getPositionY(), 400.0, 4.0, new TweenTransition(TweenTransition.LINEAR));
			t.animate(TweenableParam.SCALE_X, coin.getScaleX(), 8.0, 4.0, new TweenTransition(TweenTransition.EXPONENTIAL));
			t.animate(TweenableParam.SCALE_Y, coin.getScaleY(), 8.0, 4.0, new TweenTransition(TweenTransition.EXPONENTIAL));
			getTweenJuggler().add(t);
			t.addEventListener(new IEventListener(){

				@Override
				public void handleEvent(Event event) 
				{
					Tween fade = new Tween(coin);
					fade.animate(TweenableParam.ALPHA, 1.0, 0.0, 3.0, new TweenTransition(TweenTransition.LINEAR));
					//fade.animate(TweenableParam.ROTATION, 0, 100, 3.0, new TweenTransition(TweenTransition.EXPONENTIAL));
					getTweenJuggler().add(fade);
				}}, TweenEvent.END_EVENT);
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
	
	private class Coin extends AnimatedSprite
	{

		EventDispatcher eventDispatcher = new EventDispatcher("Coin");
		
		public Coin(String id, String spriteSheetName, String sheetDescriptionFile) throws IOException 
		{
		 	super(id, spriteSheetName, sheetDescriptionFile);
			
		 	Random rand = new Random();
			int coinX = 1000;
			int coinY = 200;
			setPosition(coinX, coinY);
			
			this.setHitBox(new HitBox("Coin", new Ellipse2D.Double(0, 0, 5, 5)));
		}
		
		@Override
		public void update(ArrayList<Integer> pressedKeys)
		{
			super.update(pressedKeys);
			
			
			if(this.collidesWith(stickMan))
			{
				eventDispatcher.dispatchEvent(new PickedUpEvent(eventDispatcher));
				
				//coin.setVisible(false);
				
				
			}
			
			
			
		}
		
	}

}
