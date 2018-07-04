package edu.virginia.engine.display;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * A very basic display object for a java based gaming engine
 * 
 * */
public class DisplayObject {

	/* All DisplayObject have a unique id */
	private String id;

	/* The image that is displayed by this object */
	private BufferedImage displayImage;
	
	private boolean visible = true;

	private double positionX = 0;
	private double positionY = 0;
	
	private double pivotPointX = 0;
	private double pivotPointY = 0;
	
	private double scaleX = 1;
	private double scaleY = 1;
	
	private double rotation = 0;
	
	private double alpha = 1;

	private AlphaComposite alph;
	
	private HitBox hitBox;
	
	private DisplayObjectContainer parent;
	
	private AffineTransform latestTransform;
	
	private boolean drawHitBoxes = true;

	/**
	 * Constructors: can pass in the id OR the id and image's file path and
	 * position OR the id and a buffered image and position
	 */
	public DisplayObject(String id) {
		this.setId(id);
		hitBox = new HitBox(id, new Rectangle());
	}

	public DisplayObject(String id, String fileName) {
		this(id);
		this.setImage(fileName);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}


	/**
	 * Returns the unscaled width and height of this display object
	 * */
	public int getUnscaledWidth() {
		if(displayImage == null) return 0;
		return displayImage.getWidth();
	}

	public int getUnscaledHeight() {
		if(displayImage == null) return 0;
		return displayImage.getHeight();
	}

	public BufferedImage getDisplayImage() {
		return this.displayImage;
	}

	protected void setImage(String imageName) {
		if (imageName == null) {
			return;
		}
		displayImage = readImage(imageName);
		if (displayImage == null) {
			System.err.println("[DisplayObject.setImage] ERROR: " + imageName + " does not exist!");
		}
	}


	/**
	 * Helper function that simply reads an image from the given image name
	 * (looks in resources\\) and returns the bufferedimage for that filename
	 * */
	public BufferedImage readImage(String imageName) {
		BufferedImage image = null;
		try {
			String file = ("resources" + File.separator + imageName);
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:readImage] Could not read image " + imageName);
			e.printStackTrace();
		}
		return image;
	}

	public void setImage(BufferedImage image) {
		if(image == null) return;
		displayImage = image;
	}

	/**
	 * 
	 * Returns true if the hit boxes intersect.
	 * 
	 * @param other display object
	 * @return
	 */
	public boolean collidesWith(DisplayObject o)
	{
		
		if(hitBox != null && o.hitBox != null)
		{
			if(this.hitBox.collidesWith(o.hitBox))
			{
				return true;
			}
				
		}
		
		for(HitBox hbThis : hitBox.subBoxes)
		{
			if(hbThis.collidesWith(o.hitBox))
			{
				return true;
			}
			
			for(HitBox hbO : o.hitBox.subBoxes)
			{
				if(hbThis.collidesWith(hbO))
				{
					return true;
				}
			}
		}
		
		
		
		
		return false;
		
	}
	
	/**
	 * Finds the approximate center of the collision.
	 * 
	 * This is calculated as the center of the intersection of the two bounding rectangles.
	 * 
	 * @param o
	 * @return
	 */
	public Point2D approximateCollisionPoint(DisplayObject o)
	{
		Rectangle2D collisionRect = new Rectangle();
		if(this.collidesWith(o))
		{
			Rectangle2D.intersect(this.getHitBox().getBox().getBounds2D(), o.getHitBox().getBox().getBounds2D(), collisionRect);
			
			Point2D ret = new Point();
			ret.setLocation(collisionRect.getCenterX(), collisionRect.getCenterY());
			return ret;
			
		}
		return null;
	}
	

	/**
	 * Invoked on every frame before drawing. Used to update this display
	 * objects state before the draw occurs. Should be overridden if necessary
	 * to update objects appropriately.
	 * */
	protected void update(ArrayList<Integer> pressedKeys) 
	{
		updateTransform();
		
		hitBox.update(latestTransform);
		
	}

	/**
	 * Draws this image. This should be overloaded if a display object should
	 * draw to the screen differently. This method is automatically invoked on
	 * every frame.
	 * */
	public void draw(Graphics g) {
		
		if (displayImage != null && visible == true) {
			
			/*
			 * Get the graphics and apply this objects transformations
			 * (rotation, etc.)
			 */
			Graphics2D g2d = (Graphics2D) g;
			applyTransformations(g2d);

			/* Actually draw the image, perform the pivot point translation here */
			g2d.drawImage(displayImage, -(int)pivotPointX, -(int)pivotPointY,
					(int) (getUnscaledWidth()),
					(int) (getUnscaledHeight()), null);
			
			if(drawHitBoxes)
			{
				hitBox.draw(g2d);
			}
			
			/*
			 * undo the transformations so this doesn't affect other display
			 * objects
			 */
			reverseTransformations(g2d);
		}
	}

	/**
	 * Applies transformations for this display object to the given graphics
	 * object
	 * */
	protected void applyTransformations(Graphics2D g2d) 
	{
		
		AffineTransform at = new AffineTransform();
		
		/*
		double m02 = positionX;
		double m12 = positionY;
		double m00 = scaleX * Math.cos(rotation);
		double m01 = -scaleX *Math.sin(rotation);
		double m10 = scaleY * Math.sin(rotation);
		double m11 = scaleY * Math.cos(rotation);
		m00 *= scaleX;
		m11 *= scaleY;
		
		AffineTransform at = new AffineTransform(m00, m10, m01, m11, m02, m12);
		*/
		
		at.translate(positionX, positionY);
		at.rotate(rotation);
		at.scale(scaleX, scaleY);
		
		
		g2d.transform(at);
		
		alph = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha);
		g2d.setComposite(alph);
		
		latestTransform = at;
	
	}

	/**
	 * Reverses transformations for this display object to the given graphics
	 * object
	 * */
	protected void reverseTransformations(Graphics2D g2d) 
	{

		AffineTransform at = g2d.getTransform();
		try {
			at = latestTransform.createInverse();
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g2d.transform(at);
		
		alph = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
		g2d.setComposite(alph);
	}
	
	protected void updateTransform()
	{
		AffineTransform at = new AffineTransform();
		

		at.translate(positionX, positionY);
		at.rotate(rotation);
		at.scale(scaleX, scaleY);
		
		latestTransform = at;
	}
	
	public void translate(double x, double y)
	{
		positionX += x;
		positionY += y;
	}

	public void rotate(double theta)
	{
		rotation += theta;
	}
	
	public void adjustScale(double increment)
	{
		scaleX += increment;
		scaleY += increment;
	}
	
	public void adjustAlpha(double change)
	{
		if(alpha+change <= 1.0 && alpha+change >= 0)
			alpha += change;
	}
	
	public void toggleVisible()
	{
		visible = !visible;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getPositionX() {
		return positionX;
	}

	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	public void setPosition(double x, double y)
	{
		setPositionX(x);
		setPositionY(y);
	}
	
	public double getPivotPointX() {
		return pivotPointX;
	}

	public void setPivotPointX(double pivotPointX) {
		this.pivotPointX = pivotPointX;
	}

	public double getPivotPointY() {
		return pivotPointY;
	}

	public void setPivotPointY(double pivotPointY) {
		this.pivotPointY = pivotPointY;
	}

	public void setPivotPoint(double x, double y)
	{
		setPivotPointX(x);
		setPivotPointY(y);
	}
	
	public double getScaleX() {
		return scaleX;
	}

	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}

	public double getScaleY() {
		return scaleY;
	}

	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}
	
	public void setScale(double x, double y)
	{
		setScaleX(x);
		setScaleY(y);
	}
	
	public HitBox getHitBox()
	{
		return hitBox;
	}
	
	public void setHitBox(HitBox hB)
	{
		hitBox = hB;
	}
	
	public void setParent(DisplayObjectContainer p)
	{
		parent = p;
	}

	public DisplayObjectContainer getParent() {
		return parent;
	}
	
	public double getScaledWidth()
	{
		return (getUnscaledWidth() * scaleX);
	}
	
	public double getScaledHeight()
	{
		return (getUnscaledHeight() * scaleY);
	}

	public Point2D getGlobalPosition(double x, double y)
	{
		
		updateTransform();
		
		Point g = null;
		if(parent != null)
		{
			double lX = x + parent.getPositionX();
			double lY = y + parent.getPositionY();
			g = (Point) parent.getGlobalPosition(lX, lY);
			latestTransform.transform(g, g);
		}
		else
		{
			g = new Point((int)x, (int)y);
		}
		return g;
	}

	public AffineTransform getLatestTransform() {
		return latestTransform;
	}
	
	
	public void addSubHitBox(String id, Shape s, AffineTransform at)
	{
		hitBox.addSubBox(new HitBox(id, s, at));
	}
}
