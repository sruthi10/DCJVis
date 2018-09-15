package ca.corefacility.gview.layout.prototype.segments;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.layout.sequence.circular.BackboneCircular;

/**
 * A spiral segment. Draws appropriates curves around the circular backbone.
 * Intended to be used with a circular backbone. 
 * Usable with a linear backbone, but will be very wasteful when drawing a straight line.
 * 
 * @author Eric Marinier
 *
 */
public class SpiralSegment extends StretchableSegment
{
	private SequencePoint startingPoint;
	private SequencePoint endingPoint;
	
	public SpiralSegment(SequencePoint startingPoint, SequencePoint endingPoint, SequencePath.Orientation orientation)
	{
		this.startingPoint = startingPoint;
		this.endingPoint = endingPoint;
		this.orientation = orientation;
	}

	@Override
	protected void performAppend(Path2D path, Backbone backbone)
	{
		BackboneCircular backboneCircular = (BackboneCircular)backbone;
		
		int numSubSegments;	//the number of line segments to break the sprial segment into.
		
		double deltaBase = (endingPoint.getBase() - startingPoint.getBase());
		double deltaHeight = (endingPoint.getHeightFromBackbone() - startingPoint.getHeightFromBackbone());
		double slope =  deltaHeight / deltaBase;
		
		double x, y;
		double b = startingPoint.getHeightFromBackbone() - slope * startingPoint.getBase();
		
		double arcRadius = (backboneCircular.getRadiusFromHeight(startingPoint.getHeightFromBackbone()) + backboneCircular.getRadiusFromHeight(startingPoint.getHeightFromBackbone())) / 2;
		double arcAngle = backboneCircular.getAngle(deltaBase);		
		double arcLength = arcRadius * arcAngle;		
		
		Point2D point = new Point2D.Double();
		
		if(deltaBase != 0)
		{
			//Determines the number of sub segments to use.
			if(calculateNumSubSegments(arcLength) >= (int)Math.ceil(arcAngle * 32 / Math.PI))
				numSubSegments = calculateNumSubSegments(arcLength);
			else		
				numSubSegments = (int)Math.ceil(arcAngle * 32 / Math.PI);
			
			for(double i = startingPoint.getBase(); i <= endingPoint.getBase(); i += Math.abs(deltaBase) / numSubSegments)
			{
				x = i;
				y = slope * x + b;
				
				point = backbone.translate(x, y);
				
				path.lineTo(point.getX(), point.getY());
			}
		}
		else
		//Just draw a line (point, basically); no need to break into sub segments.
		{
			x = endingPoint.getBase();
			y = endingPoint.getHeightFromBackbone();
			
			point = backbone.translate(x, y);
			
			path.lineTo(point.getX(), point.getY());
		}
	}

	/**
	 * Calculates the number of sub segments to use given the arc length.
	 * 
	 * @param arcLength The length of the arc to break into sub segments.
	 * @return The number of sub segments to break the arc into.
	 */
	private int calculateNumSubSegments(double arcLength)
	{
		final double DIVISOR = 2;
		
		return (int)Math.ceil(arcLength / DIVISOR);
	}

}
