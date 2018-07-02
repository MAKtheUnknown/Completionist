package edu.virginia.labtests;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;

import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.Sprite;

/**
 * Example game that utilizes our engine. We can create a simple prototype game with just a couple lines of code
 * although, for now, it won't be a very fun game :)
 * */
public class LabOneGame extends Game{

	/* Create a sprite object for our game. We'll use mario */
	Sprite mario = new Sprite("Mario", "Mario.png");
	
	FontRenderContext frc;
	Font normalFont = new Font("Courier", Font.BOLD, 14);
	TextLayout scoreLabel;
	TextLayout timeLabel;
	TextLayout winnerLabel;
	
	double xPos = 0;
	double yPos = 0;
	
	int health = 100;
	double timer = 30;
	String winner = "";
	
	private boolean initialToggle = true;
	
	/**
	 * Constructor. See constructor in Game.java for details on the parameters given
	 * */
	public LabOneGame() {
		super("Lab One Test Game", 500, 300);
	}
	
	/**
	 * Engine will automatically call this update method once per frame and pass to us
	 * the set of keys (as strings) that are currently being pressed down
	 * */
	@Override
	public void update(ArrayList<Integer> pressedKeys){
		super.update(pressedKeys);
		
		/* Make sure mario is not null. Sometimes Swing can auto cause an extra frame to go before everything is initialized */
		if(mario != null) mario.update(pressedKeys);
		
		boolean toggleVizThisCycle = false;
		
		for(int i : pressedKeys)
		{
			switch(i)
			{
			case KeyEvent.VK_UP:
				mario.translate(0, -UP_SPEED);
				break;
			case KeyEvent.VK_DOWN:
				mario.translate(0, DOWN_SPEED);
				break;
			case KeyEvent.VK_LEFT:
				mario.translate(-LEFT_SPEED, 0);
				break;
			case KeyEvent.VK_RIGHT:
				mario.translate(RIGHT_SPEED, 0);;
				break;
			case KeyEvent.VK_Q:
				mario.setRotation(mario.getRotation()-0.2);
				break;
			case KeyEvent.VK_E:
				mario.setRotation(mario.getRotation()+0.2);
				break;
			case KeyEvent.VK_A:
				mario.adjustScale(.0006);
				break;
			case KeyEvent.VK_S:
				if(mario.isVisible())
					mario.adjustScale(-.0006);
				break;
			case KeyEvent.VK_I:
				mario.setPivotPointY(mario.getPivotPointY() - 2);
				break;
			case KeyEvent.VK_K:
				mario.setPivotPointY(mario.getPivotPointY() + 2);
				break;
			case KeyEvent.VK_J:
				mario.setPivotPointX(mario.getPivotPointX() - 2);
				break;
			case KeyEvent.VK_L:
				mario.setPivotPointX(mario.getPivotPointX() + 2);
				break;
			case KeyEvent.VK_Z:
				mario.adjustAlpha(.1);
				break;
			case KeyEvent.VK_X:
				mario.adjustAlpha(-.1);
				break;
			case KeyEvent.VK_V:
				toggleVizThisCycle = true;
				if(initialToggle == true)
				{
					mario.toggleVisible();
					initialToggle = false;
				}
				break;
			default:
				break;
			}
			
			
		}
		
		if(toggleVizThisCycle == false)
		{
			initialToggle = true;
		}
		
		if(this.isClicked())
		{
			
			int x = this.getClickX();
			int y = this.getClickY();
			
			
			if(mario.getHitBox().contains(x, y)
					//x > mario.getPositionX() && x < mario.getUnscaledWidth() + mario.getPositionX() &&
					//y > mario.getPositionY() && y < mario.getUnscaledHeight() + mario.getPositionY()
					)
			{
				health--;
			}
			
			this.unclick();
		}
		
		if(mario.isVisible())
		{
			if(timer > 0)
			{
				timer -= (1.0/66.0);
			}
			else if(timer <= 0)
			{
				if(health > 0)
				{
					winner = "Player 1 wins!";
				}
			}
			if(health <= 0)
			{
				winner = "Player 2 wins!";
			}
		}
		
		//Boundaries
		//TODO: This currently only applies to the top-left corner of Mario.
		if(mario.getHitBox() != null)
		{
		if(mario.getHitBox().intersects(0,0,1000,1))
		{
			mario.translate(0, 20);
		}
		if(mario.getHitBox().intersects(0,0,1,1000))
		{
			mario.translate(20, 0);
		}
		if(mario.getHitBox().intersects(this.getScenePanel().getWidth(),0,1,1000))
		{
			mario.translate(-20, 0);
		}
		if(mario.getHitBox().intersects(0,this.getScenePanel().getHeight(),1000,1))
		{
			mario.translate(0, -20);
		}
		}
	}
	
	/**
	 * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
	 * the screen, we need to make sure to override this method and call mario's draw method.
	 * */
	@Override
	public void draw(Graphics g){
		super.draw(g);
		Graphics2D g2d = (Graphics2D) g;
		frc = g2d.getFontRenderContext();
		if(normalFont != null)
		{
			scoreLabel = new TextLayout("score: " + health, normalFont, frc);
			timeLabel = new TextLayout("time: " + (int)timer, normalFont, frc);
			winnerLabel = new TextLayout(winner + " ", normalFont, frc);
			if(scoreLabel!=null)
			{
				scoreLabel.draw(g2d,5,10);
			}
			if(timeLabel != null)
			{
				timeLabel.draw(g2d, 250, 10);
			}
			if(winnerLabel!=null)
			{
				winnerLabel.draw(g2d, 150, 200);
			}
		}
			
		//move canvas to mario's position
		g.translate((int)xPos, (int)yPos);
		
		/* Same, just check for null in case a frame gets thrown in before Mario is initialized */
		if(mario != null) mario.draw(g);
		//move canvas back
		g.translate(-(int)xPos, -(int)yPos);
	}

	/**
	 * Quick main class that simply creates an instance of our game and starts the timer
	 * that calls update() and draw() every frame
	 * */
	public static void main(String[] args) {
		LabOneGame game = new LabOneGame();
		game.start();

	}
	
	public final double DOWN_SPEED = 2;
	public final double UP_SPEED = 2;
	public final double LEFT_SPEED = 3;
	public final double RIGHT_SPEED = 3;
}
