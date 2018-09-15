package ca.corefacility.gview.layout.sequence;

import java.awt.Stroke;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.prototype.BackboneShape;
import ca.corefacility.gview.layout.prototype.SequenceOffset;
import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.prototype.SequencePointImp;
import ca.corefacility.gview.layout.prototype.StretchableShape;
import ca.corefacility.gview.layout.prototype.StrokedBackboneShape;
import ca.corefacility.gview.layout.prototype.segments.LineSegment;
import ca.corefacility.gview.layout.prototype.segments.MoveSegment;


public abstract class AbstractSequencePath implements SequencePath
{
	// should these be protected?
	protected StretchableShape shape;

	protected Backbone backbone;

	protected SequencePoint prevSequencePoint;

	/**
	 * Creates a new SequencePath around the passed data.
	 * 
	 * @param backbone The backbone of the sequence.
	 */
	protected AbstractSequencePath(Backbone backbone)
	{
		if (backbone == null)
		{
			throw new IllegalArgumentException("backbone is null");
		}
		else
		{
			this.backbone = backbone;

			prevSequencePoint = new SequencePointImp();
		}
	}

	public BackboneShape getShape()
	{
		return shape;
	}
	
    public void moveTo(double base, double heightFromBackbone)
    {
        moveTo(base,heightFromBackbone,Orientation.NONE);
    }

    public void moveTo(double base, double heightFromBackbone, double lengthFromBase)
    {
        moveTo(base,heightFromBackbone,lengthFromBase,Orientation.NONE);
    }

	public void moveTo(double base, double heightFromBackbone, Orientation orientation)
	{
		SequencePointImp movePoint = new SequencePointImp(base, heightFromBackbone);

        shape.updateHeight((float)heightFromBackbone);
		shape.appendSegment(new MoveSegment(movePoint, orientation));

		prevSequencePoint = movePoint;
	}

	public void moveTo(double base, double heightFromBackbone, double lengthFromBase, Orientation orientation)
	{
		SequencePoint offsetPoint = new SequenceOffset(base, heightFromBackbone, lengthFromBase);

        shape.updateHeight((float)heightFromBackbone);
		shape.appendSegment(new MoveSegment(offsetPoint, orientation));

		prevSequencePoint = offsetPoint;
	}

	public void realLineTo(double base, double heightFromBackbone, double lengthFromBase, Orientation orientation)
	{
		SequenceOffset offsetPoint;

		offsetPoint = new SequenceOffset(base, heightFromBackbone, lengthFromBase);

        shape.updateHeight((float)heightFromBackbone);
		shape.appendSegment(new LineSegment(offsetPoint, orientation));

		prevSequencePoint = offsetPoint;
	}

	public Backbone getBackbone()
	{
		return backbone;
	}

	@Override
	public BackboneShape createStrokedShape(Stroke s)
	{
		return new StrokedBackboneShape(s, shape, backbone);
	}

	public void clear()
	{
		prevSequencePoint = new SequencePointImp();
	}

    @Override
    public void lineTo(double base, double heightFromBackbone,
            Direction direction)
    {
        lineTo(base,heightFromBackbone,direction,Orientation.NONE);
    }

    @Override
    public void realLineTo(double base, double heightFromBackbone,
            double lengthFromBase)
    {
        realLineTo(base,heightFromBackbone,lengthFromBase,Orientation.NONE);
    }

    @Override
    public void lineTo(double base, double heightFromBackbone,
            double lengthFromBase, Direction direction)
    {
        lineTo(base,heightFromBackbone,lengthFromBase,direction,Orientation.NONE);
    }

    @Override
    public void lineTo(double base, Direction direction)
    {
        lineTo(base,direction,Orientation.NONE);
    }

    @Override
    public void closePath()
    {
        closePath(Orientation.NONE);
    }
}
