package ca.corefacility.gview.layout.prototype.segments;


import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;

public class QuadSegment extends StretchableSegment
{
	private SequencePoint controlPoint;
	private SequencePoint endPoint;
	
	public QuadSegment(SequencePoint controlPoint, SequencePoint endPoint, SequencePath.Orientation orientation)
	{
		if (controlPoint == null || endPoint == null)
		{
			throw new IllegalArgumentException("passed points are null");
		}
		else
		{
			this.controlPoint = controlPoint;
			this.endPoint = endPoint;
			this.orientation = orientation;
		}
	}

	@Override
	public void performAppend(Path2D path, Backbone backbone)
	{
		Point2D quadControl = backbone.translate(controlPoint);
		if (quadControl == null) {
			return;
		}
		
		Point2D quadEnd = backbone.translate(endPoint);
		if (quadEnd == null) {
			return;
		}
		
		path.quadTo(quadControl.getX(), quadControl.getY(), quadEnd.getX(), quadEnd.getY());
	}

}
