package ca.corefacility.gview.utils.geom;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

public class CircularUtils
{
	// used to flip some arcs so that the start/end points are in the correct position
	private final static AffineTransform MIRROR_Y = new AffineTransform(1,0,0,-1,0,0);
	
	// creates an arc by correctly drawing clockwise
	public static Shape createArcClockwise(double radius, double startAngleDeg, double extentAngleDeg, int type)
	{
		// we invert start angle, draw arc, then invert arc so start/end points are correct
		
		Arc2D arc = null;
		
		startAngleDeg = -startAngleDeg;
		
		// the height of the bounding box of the arc
		double height = radius*2;
		
		// width should be same as height
		double width = height;
		
		// upper left corner of bounding box
		double boundsX = -radius;
		double boundsY = -radius;
		
		// defines the arc to draw
		arc = new Arc2DPrecision.Double(boundsX, boundsY, width, height, startAngleDeg,
								extentAngleDeg, type);
		
		return MIRROR_Y.createTransformedShape(arc);
	}
	
	// creates an arc by correctly drawing counter-clockwise
	public static Shape createArcCounterClockwise(double radius, double startAngleDeg, double extentAngleDeg, int type)
	{		
		// the height of the bounding box of the arc
		double height = radius*2;
		
		// width should be same as height
		double width = height;
		
		// upper left corner of bounding box
		double boundsX = -radius;
		double boundsY = -radius; // this shouldn't be negative, but appears to fix y-coord problem
		
		// defines the arc to draw
		return new Arc2DPrecision.Double(boundsX, boundsY, width, height, startAngleDeg,
								extentAngleDeg, type);
	}
}
