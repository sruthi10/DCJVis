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

public class FullCircularArcSegment extends StretchableSegment
{	
	private SequencePoint seqPoint;
	private Direction direction;

	public FullCircularArcSegment(SequencePoint seqPoint, Direction direction, SequencePath.Orientation orientation)
	{
		this.seqPoint = seqPoint;
		this.direction = direction;
		this.orientation = orientation;
	}

	@Override
	protected void performAppend(Path2D path, Backbone backbone)
	{
		Point2D seqPointScaled = backbone.translate(seqPoint);
		if (seqPointScaled == null) {
			return;
		}
		
		// this is done to account for mirrored y-coords
		seqPointScaled.setLocation(seqPointScaled.getX(), -seqPointScaled.getY());
		
		Polar polarPoint = Polar.createPolar(seqPointScaled);
		
		float radius = (float)polarPoint.getRadius();
		
		Shape arc = null;
		
		if (direction == Direction.INCREASING)
		{
			arc = CircularUtils.createArcClockwise(radius, 90, 360, Arc2D.OPEN);
		}
		else if (direction == Direction.DECREASING)
		{
			arc = CircularUtils.createArcCounterClockwise(radius, 90, 360, Arc2D.OPEN);
		}
		
		path.append(arc, true);
	}
}
