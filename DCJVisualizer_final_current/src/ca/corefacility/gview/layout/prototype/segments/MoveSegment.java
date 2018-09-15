package ca.corefacility.gview.layout.prototype.segments;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath.Orientation;

public class MoveSegment extends StretchableSegment
{
	private SequencePoint movePoint;
	
	/**
	 * Creates a new move segment with the passed point.
	 * @param movePoint  The point this segment describes.
	 * @param orientation 
	 */
	public MoveSegment(SequencePoint movePoint, Orientation orientation)
	{
		if (movePoint == null)
		{
			throw new IllegalArgumentException("movePoint is null");
		}
		
		this.movePoint = movePoint;
		this.orientation = orientation;
	}

	@Override
	protected void performAppend(Path2D path, Backbone backbone)
	{
		Point2D movePoint2D = backbone.translate(movePoint);
		if (movePoint2D == null) {
			return;
		}
		
		path.moveTo(movePoint2D.getX(), movePoint2D.getY());
	}
}
