package ca.corefacility.gview.layout.sequence.circular;

import java.awt.Shape;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.prototype.NonFragmentingStretchableShape;
import ca.corefacility.gview.layout.prototype.SequenceOffset;
import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.prototype.SequencePointImp;
import ca.corefacility.gview.layout.prototype.StretchableShape;
import ca.corefacility.gview.layout.prototype.segments.CircularArcSegment;
import ca.corefacility.gview.layout.prototype.segments.CloseSegment;
import ca.corefacility.gview.layout.prototype.segments.FullCircularArcSegment;
import ca.corefacility.gview.layout.prototype.segments.LineSegment;
import ca.corefacility.gview.layout.prototype.segments.MoveSegment;
import ca.corefacility.gview.layout.prototype.segments.SpiralSegment;
import ca.corefacility.gview.layout.sequence.AbstractSequencePath;
import ca.corefacility.gview.layout.sequence.Backbone;

public class SequencePathCircular extends AbstractSequencePath
{
	// used to flip some arcs so that the start/end points are in the correct position
	//private final static AffineTransform MIRROR_Y = new AffineTransform(1,0,0,-1,0,0);
	
	public SequencePathCircular(Backbone backbone)
	{
		super(backbone);
		
		shape = new NonFragmentingStretchableShape(backbone);
	}
	
	public void closePath(Orientation orientation)
	{
		// TODO incorporate closing circular curves
		// 	also handle setting previous point
		shape.appendSegment(new CloseSegment(orientation));
		
		prevSequencePoint = new SequencePointImp();
	}

	public void lineTo(double base, double heightFromBackbone, Direction direction, Orientation orientation)
	{
		SequencePoint movePoint = new SequencePointImp(base, heightFromBackbone);
		
        shape.updateHeight((float)heightFromBackbone);
		shape.appendSegment(new SpiralSegment(prevSequencePoint, movePoint, orientation));
		
		prevSequencePoint = movePoint;
	}
	
	public void lineTo(double base, Direction direction, Orientation orientation)
	{
		// this method more efficiently draws the curve, since we know it should be a circle
		// assumed that (0,0) is the centre of the circular backbone
		
		double heightFromBackbone = prevSequencePoint.getHeightFromBackbone();
		
		SequencePoint startPoint = (SequencePoint)prevSequencePoint.clone();
		SequencePoint endPoint = new SequencePointImp(base, heightFromBackbone);
		
		shape.appendSegment(new CircularArcSegment(startPoint, endPoint, direction, orientation));
		
		prevSequencePoint = endPoint;
	}
	
	public void lineTo(double base, double heightFromBackbone, double lengthFromBase, Direction direction, Orientation orientation)
	{
		SequencePoint startPoint = (SequencePoint)prevSequencePoint.clone();
		SequencePoint endPoint;
		
		endPoint = new SequenceOffset(base, heightFromBackbone, lengthFromBase);
		
        shape.updateHeight((float)heightFromBackbone);
		shape.appendSegment(new CircularArcSegment(startPoint, endPoint, direction, orientation));
		
		prevSequencePoint = endPoint;
	}
	
	@Override
	public Shape createFullBackboneShape(double topHeight, double bottomHeight, Location trueLocation)
	{
		StretchableShape shape =  new NonFragmentingStretchableShape(backbone);

		SequencePoint topPoint = new SequencePointImp(0, topHeight);
		SequencePoint bottomPoint = new SequencePointImp(0, bottomHeight);
		
        shape.updateHeight((float)topHeight);
        shape.updateHeight((float)bottomHeight);
		shape.appendSegment(new MoveSegment(topPoint, Orientation.TOP));
		shape.appendSegment(new FullCircularArcSegment(topPoint, Direction.INCREASING, Orientation.TOP));
		shape.appendSegment(new LineSegment(bottomPoint, Orientation.NONE));
		shape.appendSegment(new FullCircularArcSegment(bottomPoint, Direction.DECREASING, Orientation.BOTTOM));
		shape.appendSegment(new LineSegment(topPoint, Orientation.NONE));
		
		return shape;
	}

	public void clear()
	{
		super.clear();
		shape = new NonFragmentingStretchableShape(backbone);
	}
}
