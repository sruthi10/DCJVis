package ca.corefacility.gview.layout.sequence.linear;

import java.awt.Shape;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.prototype.NonFragmentingStretchableShape;
import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.prototype.SequencePointImp;
import ca.corefacility.gview.layout.prototype.StretchableShape;
import ca.corefacility.gview.layout.prototype.segments.CloseSegment;
import ca.corefacility.gview.layout.prototype.segments.LineSegment;
import ca.corefacility.gview.layout.prototype.segments.MoveSegment;
import ca.corefacility.gview.layout.sequence.AbstractSequencePath;
import ca.corefacility.gview.layout.sequence.Backbone;

public class SequencePathLinear extends AbstractSequencePath
{
	/**
	 * Creates a new SequencePathLinear around the passed data.
	 * 
	 * @param backbone  The backbone of the sequence.
	 */
	public SequencePathLinear(Backbone backbone)
	{
		super(backbone);
		
		shape = new NonFragmentingStretchableShape(backbone);
	}

	public void closePath(Orientation orientation)
	{
		shape.appendSegment(new CloseSegment(orientation));
		
		prevSequencePoint = new SequencePointImp();
	}

	public void lineTo(double base, double heightFromBackbone, Direction direction, Orientation orientation) // ignores direction for now
	{
		performLineTo(base, heightFromBackbone, direction, orientation);
	}

	public void lineTo(double base, Direction direction, Orientation orientation)
	{
		performLineTo(base, prevSequencePoint.getHeightFromBackbone(), direction, orientation);
	}
	
	// performs the lineTo, used so that we can call this method with  the previous heightFromBackbone (in prevSequencePoint).
	private void performLineTo(double base, double heightFromBackbone, Direction direction, Orientation orientation)
	{
		SequencePoint movePoint = new SequencePointImp(base, heightFromBackbone);
		
        shape.updateHeight((float)heightFromBackbone);
		shape.appendSegment(new LineSegment(movePoint, orientation));
		
		prevSequencePoint = movePoint;
	}
	
	public void lineTo(double base, double heightFromBackbone, double lengthFromBase, Direction direction, Orientation orientation)
	{
		realLineTo(base, heightFromBackbone, lengthFromBase, orientation);
	}
	
	public void clear()
	{
		super.clear();
		shape = new NonFragmentingStretchableShape(backbone);
		
		prevSequencePoint = new SequencePointImp();
	}

	@Override
	public Shape createFullBackboneShape(double topHeight, double bottomHeight, Location trueLocation)
	{
		StretchableShape shape = new NonFragmentingStretchableShape(backbone);
		
        shape.updateHeight((float)topHeight);
        shape.updateHeight((float)bottomHeight);
		SequencePoint currPoint = new SequencePointImp(0, topHeight);
		shape.appendSegment(new MoveSegment(currPoint, Orientation.TOP));
		currPoint = new SequencePointImp(trueLocation.getMax(), topHeight);
		shape.appendSegment(new LineSegment(currPoint, Orientation.TOP));
		currPoint = new SequencePointImp(trueLocation.getMax(), bottomHeight);
		shape.appendSegment(new LineSegment(currPoint, Orientation.NONE));
		currPoint = new SequencePointImp(0, bottomHeight);
		shape.appendSegment(new LineSegment(currPoint, Orientation.BOTTOM));
		currPoint = new SequencePointImp(0, topHeight);
		shape.appendSegment(new LineSegment(currPoint, Orientation.NONE));
		
		return shape;
	}
}
