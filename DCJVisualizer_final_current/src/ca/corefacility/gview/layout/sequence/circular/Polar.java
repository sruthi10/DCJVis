package ca.corefacility.gview.layout.sequence.circular;

import java.awt.geom.Point2D;

/**
 * This class encapsulates polar coordinates.  Should place this someplace else, but am only
 * 	using it here for now.
 * 
 * @author Aaron Petkau
 */
public class Polar
{
	private double radius;
	private double theta;
	
	/**
	 * Creates a polar point from the passed cartesian point.
	 * 
	 * @param point  The point to convert.
	 * 
	 * @return A new Polar point, or null if passed point was null.
	 */
	public static Polar createPolar(Point2D point)
	{
		Polar polarPoint = null;
		
		if (point != null)
		{
			polarPoint = new Polar();
			polarPoint.setFromCartesian(point.getX(), point.getY());
		}
		else
		{
			System.err.println("Passed point was null.");
		}
		
		return polarPoint;
	}
	
	/**
	 * Creates a polar point from the passed cartesian coords.
	 * 
	 * @param x  The x-coordinate.
	 * @param y  The y-coordinate.
	 * 
	 * @return A new Polar point corresponding to the passed (x,y) coords.
	 */
	public static Polar createPolar(double x, double y)
	{
		Polar polarPoint = new Polar();
		polarPoint.setFromCartesian(x,y);
		
		return polarPoint;
	}
	
	/**
	 * Converts the passed (x,y) coords into polar coords, and stores them in the new polar object.
	 */
	public Polar(double radius, double theta)
	{
		this.radius = radius;
		this.theta = theta;
	}
	
	/**
	 * Creates a Polar point with r=0, t=0.
	 */
	public Polar()
	{
		this.radius = 0;
		this.theta = 0;
	}
	
	public Point2D toPoint2D()
	{
		return new Point2D.Double(radius*Math.cos(theta), radius*Math.sin(theta));
	}
	
	private void setFromCartesian(double x, double y)
	{
		radius = Math.sqrt(x*x + y*y);
		
		if (radius == 0) // can't divide only when radius is exactly 0
		{
			theta = 0; // set default angle if our point was (0,0)
		}
		else
		{
			theta = Math.acos(x/radius);
		}
		
		// change sign
		if (y < 0)
		{
			theta *= -1;
		}
		
		theta = thetaToStandardRange(theta);
	}

	/**
	 * @return  The angle (in radians);
	 */
	public double getTheta()
	{
		return theta;
	}
	
	/**
	 * @return	The angle (in degrees).
	 */
	public double getThetaDegree()
	{
		return Math.toDegrees(theta);
	}

	/**
	 * @return	The radius.
	 */
	public double getRadius()
	{
		return radius;
	}
	
	/**
	 * Converts the passed angle to the standard [0, 2*PI) range
	 * @param theta  The angle to convert
	 * 
	 * @return The converted angle.
	 */
	public static double thetaToStandardRange(double theta)
	{
		double newTheta = theta;
		double TWO_PI = 2*Math.PI;
		
		while (newTheta >= TWO_PI)
		{
			newTheta -= TWO_PI;
		}
		
		while (newTheta < 0)
		{
			newTheta += TWO_PI;
		}
		
		return newTheta;
	}
	
	public String toString()
	{
		return "(" + radius + ", " + theta + ")";
	}
}
