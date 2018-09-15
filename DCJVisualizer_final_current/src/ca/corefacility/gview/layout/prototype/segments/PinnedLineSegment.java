package ca.corefacility.gview.layout.prototype.segments;


import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;

public class PinnedLineSegment extends StretchableSegment
{
	private SequencePoint pinnedPoint;
	private double lengthFromPin;
	private double heightFromPin;
	
	public PinnedLineSegment(SequencePoint pinnedPoint, double lengthFromPin, double heightFromPin, SequencePath.Orientation orientation)
	{
		if (pinnedPoint == null)
		{
			throw new IllegalArgumentException("pinnedPoint is null");
		}
		else
		{
			this.pinnedPoint = pinnedPoint;
			this.lengthFromPin = lengthFromPin;
			this.heightFromPin = heightFromPin;
			this.orientation = orientation;
		}
	}

	protected void performAppend(Path2D path, Backbone backbone)
	{
		double base = pinnedPoint.getBase() + backbone.getSpannedBases(lengthFromPin);
		double height = pinnedPoint.getHeightFromBackbone() + heightFromPin;
		
		Point2D lineToPoint = backbone.translate(base, height);
		
		path.lineTo(lineToPoint.getX(), lineToPoint.getY());
	}
}
