package completionist;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.DisplayObjectContainer;
import edu.virginia.engine.display.HitBox;
import edu.virginia.engine.events.Event;

public class SinistarLevel extends Level
{

	private DisplayObjectContainer space = new DisplayObjectContainer("Space");
	private double direction = 0;
	
	private AnimatedSprite sinistar;
	
	private MouseListener hbTest;
	
	public SinistarLevel(String id) 
	{
		super(id);
	}

	@Override
	public void init() 
	{
		
		player.setPosition(200, 200);
		
		try 
		{
			sinistar = new AnimatedSprite("Sinistar", "Spritesheets/ProtoBoss.png", "Spritesheets/ProtoBoss.spritetext");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		sinistar.setAnimation("ProtoBoss");
		space.addChild(sinistar);
		LevelManager.getLevelManager().addChild(space);
		
		
		
		
		hbTest = new MouseListener()
				{
					DisplayObject testedObject = player;
			
					@Override
					public void mouseClicked(MouseEvent arg0) 
					{
						System.out.println("Click: " + arg0.getX() + " " + arg0.getY());
						if(sinistar.getHitBox().collidesWith(new HitBox("", new Rectangle(arg0.getX(), arg0.getY(), 1, 1))))
						{
							System.out.println("Hit");
						}
					}
					@Override
					public void mouseEntered(MouseEvent arg0) {	}
					@Override
					public void mouseExited(MouseEvent arg0) {	}
					@Override
					public void mousePressed(MouseEvent arg0) {				}
					@Override
					public void mouseReleased(MouseEvent arg0) {					}
			
				};
				
		LevelManager.getLevelManager().getScenePanel().addMouseListener(hbTest);
		
		player.setScale(2, 2);
		
	}

	@Override
	public void controls(ArrayList<Integer> pressedKeys) 
	{
		double vecX = 0;
		double vecY = 0;
		
		for(Integer key : pressedKeys)
		{
			switch(key)
			{
			case KeyEvent.VK_UP:
			case KeyEvent.VK_NUMPAD8:
				vecY -= 1;
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_NUMPAD4:
				vecX -= 1;
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_NUMPAD6:
				vecX += 1;
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_NUMPAD5:
				vecY += 1;
				break;
				
			}
		}
		
		
		if(vecX == 0)
		{
			if(vecY < 0)
			{
				direction = 0;
			}
			else if(vecY > 0)
			{
				direction = Math.PI;
			}
		}
		else if (vecY == 0)
		{
			if(vecX > 0)
			{
				direction = Math.PI/2;
			}
			else if (vecX < 0)
			{
				direction = Math.PI/2 + Math.PI;
			}
		}
		else
		{
			direction = -Math.atan(vecY/vecX);
			if(vecY > 0){direction += Math.PI;}
		}
		
		player.setRotation(direction);
		
		
	}

	@Override
	public void update() 
	{
		player.translate(ROCKET_FORCE * Math.sin(player.getRotation()), -ROCKET_FORCE * Math.cos(player.getRotation()));
		
		
		double directionToPlayer;
		if(player.getPositionX() > sinistar.getPositionX())
		{
			directionToPlayer = Math.atan((player.getPositionY() - sinistar.getPositionY()) / (player.getPositionX() - sinistar.getPositionX()));
		}
		else
		{
			directionToPlayer = Math.PI + Math.atan((sinistar.getPositionY()-player.getPositionY()) / (sinistar.getPositionX()-player.getPositionX()));
		}
		
		sinistar.translate(SINI_SPEED * Math.cos(directionToPlayer), SINI_SPEED * Math.sin(directionToPlayer));
		
		if(player.collidesWith(sinistar))
		{
			this.dispatchEvent(new Event("BOSS_DEFEATED", this));
			System.out.println("asd");
		}
	}
	
	@Override
	public void tearDown()
	{
		
	}
	
	private final double ROCKET_FORCE = 2;
	private final double SINI_SPEED = 2;

}
