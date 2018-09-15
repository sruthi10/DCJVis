package ca.corefacility.gview.layout.sequence;


import java.awt.Shape;
import java.awt.Stroke;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.prototype.BackboneShape;

public class SlotPathImp implements SlotPath
{
	private SequencePath sequencePath;
	private SlotTranslator slotTranslator;
	private int slot;

	private Backbone backbone;

	/**
	 * Creates a new SlotPath around the passed data.
	 * 
	 * @param sequencePath  The SequencePath used to perform the underlying translations on the sequence.
	 * @param backbone  The backbone used to perform the underlying translations of points.
	 * @param slotTranslator  A SlotTranslator used to translate to different heights given slot numbers.
	 * @param slot  The slot about which we will be creating paths.
	 */
	// TODO see if I even need backbone?
	public SlotPathImp(SequencePath sequencePath, Backbone backbone, SlotTranslator slotTranslator, int slot)
	{
		if (sequencePath == null || slotTranslator == null || backbone == null)
			throw new IllegalArgumentException("sequencePath or slotTranslator are null");
		else
		{
			this.slot = slot;
			this.slotTranslator = slotTranslator;
			this.sequencePath = sequencePath;

			this.backbone = backbone;

			sequencePath.clear();
		}
	}

	public void clear()
	{
		this.sequencePath.clear();
	}

	public void closePath()
	{
		this.sequencePath.closePath();
	}
	
	public void closePath(SequencePath.Orientation orientation)
    {
        this.sequencePath.closePath(orientation);
    }

	public Backbone getBackbone()
	{
		return this.backbone;
	}

	public BackboneShape getShape()
	{
		return this.sequencePath.getShape();
	}

	public void lineTo(double base, double heightInSlot, Direction direction, SequencePath.Orientation orientation)
	{
		double heightFromBackbone = this.slotTranslator.getHeightFromBackbone(this.slot, heightInSlot);

		this.sequencePath.lineTo(base, heightFromBackbone, direction, orientation);
	}

	public void lineTo(double base, double heightInSlot, double lengthFromBase,
			Direction direction, SequencePath.Orientation orientation)
	{
		double heightFromBackbone = this.slotTranslator.getHeightFromBackbone(this.slot, heightInSlot);

		this.sequencePath.lineTo(base, heightFromBackbone, lengthFromBase, direction, orientation);
	}

	public void lineTo(double base, Direction direction, SequencePath.Orientation orientation)
	{
		this.sequencePath.lineTo(base, direction, orientation);
	}

	public void moveTo(double base, double heightInSlot, SequencePath.Orientation orientation)
	{
		double heightFromBackbone = this.slotTranslator.getHeightFromBackbone(this.slot, heightInSlot);

		this.sequencePath.moveTo(base, heightFromBackbone, orientation);
	}

	public void moveTo(double base, double heightInSlot, double lengthFromBase, SequencePath.Orientation orientation)
	{
		double heightFromBackbone = this.slotTranslator.getHeightFromBackbone(this.slot, heightInSlot);

		this.sequencePath.moveTo(base, heightFromBackbone, lengthFromBase, orientation);
	}
	
    public void moveTo(double base, double heightInSlot)
    {
        moveTo(base,heightInSlot,SequencePath.Orientation.NONE);
    }

    public void moveTo(double base, double heightInSlot, double lengthFromBase)
    {
        moveTo(base,heightInSlot,SequencePath.Orientation.NONE);
    }

	public void realLineTo(double base, double heightInSlot,
			double lengthFromBase, SequencePath.Orientation orientation)
	{
		double heightFromBackbone = this.slotTranslator.getHeightFromBackbone(this.slot, heightInSlot);

		this.sequencePath.realLineTo(base, heightFromBackbone, lengthFromBase, orientation);
	}

	@Override
	public Shape createStrokedShape(Stroke stroke)
	{
		return this.sequencePath.createStrokedShape(stroke);
	}

	@Override
	public Shape createFullBackboneShape(double top, double bottom, Location trueLocation)
	{
		double absoluteTop = this.slotTranslator.getHeightFromBackbone(this.slot, top);
		double absoluteBottom = this.slotTranslator.getHeightFromBackbone(this.slot, bottom);
		
		return this.sequencePath.createFullBackboneShape(absoluteTop, absoluteBottom, trueLocation);
	}

    @Override
    public void lineTo(double base, double heightInSlot, Direction direction)
    {
        lineTo(base,heightInSlot,direction,SequencePath.Orientation.NONE);
    }

    @Override
    public void realLineTo(double base, double heightInSlot,
            double lengthFromBase)
    {
        realLineTo(base,heightInSlot,lengthFromBase,SequencePath.Orientation.NONE);
    }

    @Override
    public void lineTo(double base, double heightInSlot, double lengthFromBase,
            Direction direction)
    {
        lineTo(base,heightInSlot,lengthFromBase,direction,SequencePath.Orientation.NONE);
    }

    @Override
    public void lineTo(double base, Direction direction)
    {
        lineTo(base,direction,SequencePath.Orientation.NONE);
    }
}
