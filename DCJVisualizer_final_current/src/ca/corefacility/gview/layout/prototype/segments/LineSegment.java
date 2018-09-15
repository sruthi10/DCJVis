package ca.corefacility.gview.layout.prototype.segments;


import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;

public class LineSegment extends StretchableSegment
{
	//private int type = PathIterator.SEG_LINETO;
	
	private SequencePoint lineEnd;
	
	/**
	 * Creates a new line segment, the start point is taken to be the last point used when added to a stretchable shape.
	 * @param lineEnd  The end point of the line.
	 */
	public LineSegment(SequencePoint lineEnd, SequencePath.Orientation orientation)
	{
		if (lineEnd == null)
		{
			throw new IllegalArgumentException("lineEnd is null");
		}
		
		this.lineEnd = lineEnd;
		this.orientation = orientation;
	}

	@Override
	public void performAppend(Path2D path, Backbone backbone)
	{
		Point2D lineEndScaled = backbone.translate(lineEnd);
		if (lineEndScaled == null) {
			return;
		}
		
		path.lineTo(lineEndScaled.getX(), lineEndScaled.getY());
	}
}
