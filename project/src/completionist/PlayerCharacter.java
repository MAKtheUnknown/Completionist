package completionist;

import java.io.IOException;

import edu.virginia.engine.physics.PhysicalSprite;

public class PlayerCharacter extends PhysicalSprite
{
	
	private double health;
	private double maxHealth;
	
	public PlayerCharacter(String id, String spriteSheetName, String sheetDescriptionFile, double mass)
			throws IOException 
	{
		super(id, spriteSheetName, sheetDescriptionFile, mass);
		
	}

	
	public void addHealth(double dHealth)
	{
		health += dHealth;
	}
	
	public void subtractHealth(double dHealth)
	{
		health -= dHealth;
	}
	
	public double getHealth() 
	{
		return health;
	}

	public void setHealth(double health) 
	{
		this.health = health;
	}

	public double getMaxHealth() 
	{
		return maxHealth;
	}

	public void setMaxHealth(double maxHealth) 
	{
		this.maxHealth = maxHealth;
	}
}
