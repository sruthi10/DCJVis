package ca.corefacility.gview.layout.prototype.segments;


import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;

public class CubicSegment extends StretchableSegment
{
	private SequencePoint controlPoint1;
	private SequencePoint controlPoint2;
	private SequencePoint endPoint;
	
	/**
	 * Creates a new CubicSegment which can be stretched.
	 * 	The first control point is taken to be the last used point of the stretchable shape this will be appended to.
	 * 
	 * @param controlPoint1  The first control point of the cubic segment.
	 * @param controlPoint2  The second control point of the cubic segment.
	 * @param endPoint  The ending point of the cubic segment.
	 */
	public CubicSegment(SequencePoint controlPoint1, SequencePoint controlPoint2, SequencePoint endPoint, SequencePath.Orientation orientation)
	{
		if (controlPoint1 == null || controlPoint2 == null || endPoint == null)
		{
			throw new IllegalArgumentException("passed points are null");
		}
		else
		{
			this.controlPoint1 = controlPoint1;
			this.controlPoint2 = controlPoint2;
			this.endPoint = endPoint;
			this.orientation = orientation;
		}
	}
	
	@Override
	public void performAppend(Path2D path, Backbone backbone)
	{
		Point2D cubicControl1 = backbone.translate(controlPoint1);
		if (cubicControl1 == null) {
			return;
		}
		
		Point2D cubicControl2 = backbone.translate(controlPoint2);
		if (cubicControl2 == null) {
			return;
		}
		
		Point2D cubicEnd = backbone.translate(endPoint);
		if (cubicEnd == null) {
			return;
		}
		
		path.curveTo(cubicControl1.getX(), cubicControl1.getY(), cubicControl2.getX(), cubicControl2.getY(),
					 cubicEnd.getX(), cubicEnd.getY());
	}
}
