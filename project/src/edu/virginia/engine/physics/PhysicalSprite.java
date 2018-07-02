package edu.virginia.engine.physics;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.events.EventDispatcher;

public class PhysicalSprite extends AnimatedSprite
{
	
	public PhysicalSprite(String id, String spriteSheetName, String sheetDescriptionFile, double mass) throws IOException 
	{
		super(id, spriteSheetName, sheetDescriptionFile);
		
		this.mass = mass;
	}

	private Point2D velocity = new Point2D.Double(0,0);
	private Point2D acceleration = new Point2D.Double(0,0);
	double mass = 1;
	
	List<Point2D> forces = new ArrayList<Point2D>();
	
	@Override
	public void update(ArrayList<Integer> pressedKeys)
	{
		super.update(pressedKeys);
		
		double forceX = 0;
		double forceY = 0;
		for(Point2D force : forces)
		{
			forceX += force.getX();
			forceY += force.getY();
		}
		
		acceleration.setLocation(forceX/mass, forceY/mass);
		
		velocity.setLocation(velocity.getX() + acceleration.getX(), velocity.getY() + acceleration.getY());
		
		super.setPositionX(super.getPositionX() + velocity.getX());
		super.setPositionY(super.getPositionY() + velocity.getY());
		
		
		forces.clear();
	}

	
	public void applyForce(Point2D f)
	{
		forces.add(f);
	}
	
	
	public Point2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Point2D velocity) {
		this.velocity = velocity;
	}
	
	public void setVelocityX(double vX)
	{
		this.velocity.setLocation(vX, velocity.getY());
	}
	
	public void setVelocityY(double vY)
	{
		this.velocity.setLocation(velocity.getX(), vY);
	}


	public Point2D getAcceleration() {
		return acceleration;
	}


	public void setAcceleration(Point2D acceleration) {
		this.acceleration = acceleration;
	}


	public double getMass() {
		return mass;
	}


	public void setMass(double mass) {
		this.mass = mass;
	}


	public List<Point2D> getForces() {
		return forces;
	}


	public void setForces(List<Point2D> forces) {
		this.forces = forces;
	}
	
	

}
