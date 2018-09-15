package ca.corefacility.gview.layout.prototype.segments;


import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.layout.sequence.circular.Polar;
import ca.corefacility.gview.utils.geom.CircularUtils;

/**
 * Stores a segment that is a circular arc.
 * 
 * @author Aaron Petkau
 *
 */
public class CircularArcSegment extends StretchableSegment
{	
	// note:  arc assumed to be circle where both start and end are at same distance from center
	private SequencePoint startPoint; // the point where the arc starts from, assumed to be relative to the center
	private SequencePoint endPoint; // the end of the arc
	
	private Direction direction;  // we store a direction in here to determine how to create the arc
	
	private boolean isFullArc; // boolean determining whether or not this arc should be a full circle or not.
	
	/**
	 * Creates a new CircularArcSegment.
	 * 
	 * @param startPoint  The start of the segment.
	 * @param endPoint  The end of the segment (assumed to be same distance from center as start).
	 * @param direction  The direction of the segment.
	 */
	public CircularArcSegment(SequencePoint startPoint, SequencePoint endPoint, Direction direction, SequencePath.Orientation orientation)
	{
		if (startPoint == null || endPoint == null)
		{
			throw new IllegalArgumentException("passed points are null");
		}
		
		// determines if this should be a full arc or not, done so that if start/end base are initial/final bases,
		//	then we can't accidently draw a small arc segment instead of a full arc
//		if  (((startPoint.getBase() == FullLocation.INITIAL_BASE) && (endPoint.getBase() == FullLocation.FINAL_BASE)) ||
//			 ((startPoint.getBase() == FullLocation.FINAL_BASE) && (endPoint.getBase() == FullLocation.INITIAL_BASE)))
//		 {
//			 isFullArc = true;
//		 }
//		else
		{
			isFullArc = false;
		}
		
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.direction = direction;
		
		this.orientation = orientation;
	}

	protected void performAppend(Path2D path, Backbone backbone)
	{
		double radius;
		double startAngle;
		double extentAngle;
		
		Point2D startPointScaled = backbone.translate(startPoint);
		if (startPointScaled == null) {
			return;
		}
		
		Point2D endPointScaled = backbone.translate(endPoint);
		if (endPointScaled == null) {
			return;
		}
		
		// this is done to account for mirrored y-coords
		startPointScaled.setLocation(startPointScaled.getX(), -startPointScaled.getY());
		endPointScaled.setLocation(endPointScaled.getX(), -endPointScaled.getY());
			
		Polar polarStart = Polar.createPolar(startPointScaled);
		Polar polarEnd = Polar.createPolar(endPointScaled);
		
		radius = polarStart.getRadius(); // assume that polarEnd.getRadius() is the same
		
		if (isFullArc)
		{
			// TODO what about when we want to start arc at different locations?
			startAngle = 90;
			extentAngle = 360;
//			System.out.println("Full Arc, direction=" + direction + ", radius=" + radius);
		}
		else
		{
			startAngle = polarStart.getThetaDegree();
			extentAngle = getAngularExtentDegree(direction, startAngle, polarEnd.getThetaDegree());
		}
//		System.out.println("Arc, direction=" + direction + ", radius=" + radius);

		
		Shape newArc = createArc(direction, radius, startAngle, extentAngle);
//		System.out.println("center=(" + newArc.getBounds2D().getCenterX() + "," + newArc.getBounds2D().getCenterY() + "), width=" + newArc.getBounds2D().getWidth() + ", height=" +newArc.getBounds2D().getHeight());
		
		path.append(newArc, true);
	}
	
	/**
	 * Creates a partial arc using the passed parameters.
	 * 
	 * @param radius  The radius of the arc.
	 * @param direction The direction to draw the arc in.
	 * @param startAngleDeg  The starting angle (in degrees).
	 * @param extentAngleDeg  The extent of the arc (in degrees).
	 * 
	 * @return  An Arc2D defining the arc.
	 */
	private Shape createArc(Direction direction, double radius, double startAngleDeg, double extentAngleDeg)
	{		
		Shape arc = null;
		
		if (direction == Direction.INCREASING)
		{
			arc = CircularUtils.createArcClockwise(radius, startAngleDeg, extentAngleDeg, Arc2D.OPEN);
		}
		else if (direction == Direction.DECREASING)
		{
			arc = CircularUtils.createArcCounterClockwise(radius, startAngleDeg, extentAngleDeg, Arc2D.OPEN);
		}
		else
		{
			throw new IllegalArgumentException("passed direction not INCREASING or DECREASING");
		}
		
		return arc;
	}
	
	/**
	 * Calculates the angular extent between startAngle and endAngle in the direction specified (in degrees).
	 * 
	 * @param direction  The direction in which to obtain the anglular extent.
	 * 
	 * @param startAngle  The starting angle (degrees). [0,360]
	 * @param endAngle  The ending angle (degrees). [0,360]
	 * 
	 * @return  The angular extent in degrees from startAngle to endAngle clockwise.
	 */
	private double getAngularExtentDegree(Direction direction, double startAngle, double endAngle)
	{
		double extent = Math.abs(startAngle - endAngle);
		
		if (direction == Direction.NONE)
		{
			throw new IllegalArgumentException("direction was NONE");
		}
		else
		{
			if ((direction == Direction.INCREASING) && (startAngle <= endAngle)
			  ||(direction == Direction.DECREASING) && (endAngle <= startAngle))
			{
				extent = 360 - extent;
			}
		}
		
		return extent;
	}
}
