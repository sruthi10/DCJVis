package ca.corefacility.gview.layout.plot;


import java.awt.Shape;
import java.util.ArrayList;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.sequence.SlotPath;

/**
 * Draws out a plot with positive values colored above, negitive values colored below center line.
 * @author aaron
 *
 */
public class PlotDrawerCenter extends PlotDrawer
{
	boolean finishedInitialPoint = false;

	boolean initialMoveTo = false; // if we are in state "moveTo" and haven't done a "lineTo"

	boolean positive;

	private int initialBaseChunk;

	private int prevBase;

	private static double centerHeight = 0.0;

	boolean positiveState = true;

	private ArrayList<Shape> negativePaths = new ArrayList<Shape>();
	private ArrayList<Shape> positivePaths = new ArrayList<Shape>();

	@Override
	public void drawRange(int startBase, int endBase, double heightInSlot,
			SlotPath slotPath)
	{
		if( heightInSlot >= centerHeight )
		{
			if( !this.positiveState )
			{
				if( this.initialMoveTo )
				{
					slotPath.closePath();
					this.negativePaths.add( slotPath.getShape() );
					slotPath.clear();
					this.initialMoveTo = false;
				}
			}
			this.positiveState = true;
		}
		else
		{
			if( this.positiveState )
			{
				if( this.initialMoveTo)
				{
					slotPath.closePath();
					this.positivePaths.add( slotPath.getShape() );
					slotPath.clear();
					this.initialMoveTo = false;
				}
			}
			this.positiveState = false;
		}

		slotPath.moveTo(startBase, centerHeight);
		this.initialMoveTo = true;
		slotPath.lineTo(startBase, heightInSlot, Direction.NONE);
		slotPath.lineTo(endBase, startBase <= endBase ? Direction.INCREASING : Direction.DECREASING);
		slotPath.lineTo(endBase, centerHeight, Direction.NONE);
		slotPath.lineTo(startBase, startBase <= endBase ? Direction.DECREASING : Direction.INCREASING);
	}


	@Override
	public void drawPoint(int base, double heightInSlot, SlotPath slotPath)
	{
		if (!this.finishedInitialPoint)
		{
			slotPath.moveTo(base, heightInSlot);

			this.prevBase = base;
			this.initialBaseChunk = base;
			this.finishedInitialPoint = true;
			this.initialMoveTo = true;

			this.positiveState = heightInSlot >= 0;
		}
		else
		{
			// if we are in same state as before (height is positive and we are in positive state)
			if (heightInSlot >= 0 && this.positiveState ||
					heightInSlot <= 0 && !this.positiveState)
			{
				slotPath.lineTo(base, heightInSlot, Direction.INCREASING);

				this.prevBase = base;
				this.initialMoveTo = false;
			}
			// if we switch to a different state, then close off this "chunk"
			else
			{
				closeChunk(slotPath, this.prevBase);

				this.positiveState = heightInSlot > 0;
				this.initialBaseChunk = base;
				this.prevBase = base;
				this.initialMoveTo = true;


				slotPath.moveTo(base, heightInSlot);
			}
		}
	}

	private void closeChunk(SlotPath slotPath, double base)
	{
		if (this.initialMoveTo)
		{
			slotPath.lineTo(this.prevBase, centerHeight, Direction.NONE);
			slotPath.closePath();
		}
		else
		{
			slotPath.lineTo(this.prevBase, centerHeight, Direction.NONE);
			slotPath.lineTo(this.initialBaseChunk, Direction.DECREASING);
			slotPath.closePath();

		}
	}

	@Override
	public Shape finishPlotPoint(SlotPath slotPath)
	{
		closeChunk(slotPath, this.prevBase);

		reset();

		return slotPath.getShape();
	}

	@Override
	public void reset()
	{
		this.finishedInitialPoint = false;
	}


	@Override
	public Shape[][] finishPlotRange(SlotPath slotPath)
	{
		if( this.positiveState )
		{
			//slotPath.closePath();
			this.positivePaths.add( slotPath.getShape() );
		}
		else
		{
			//slotPath.closePath();
			this.negativePaths.add( slotPath.getShape() );
		}
		reset();


		return new Shape[][] { (Shape[]) this.positivePaths.toArray( new Shape[0]), (Shape[]) this.negativePaths.toArray(new Shape[0]) };
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.finishedInitialPoint ? 1231 : 1237);
		result = prime * result + this.initialBaseChunk;
		result = prime * result + (this.initialMoveTo ? 1231 : 1237);
		result = prime * result + (this.positive ? 1231 : 1237);
		result = prime * result + (this.positiveState ? 1231 : 1237);
		result = prime * result + this.prevBase;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlotDrawerCenter other = (PlotDrawerCenter) obj;
		if (this.finishedInitialPoint != other.finishedInitialPoint)
			return false;
		if (this.initialBaseChunk != other.initialBaseChunk)
			return false;
		if (this.initialMoveTo != other.initialMoveTo)
			return false;
		if (this.positive != other.positive)
			return false;
		if (this.positiveState != other.positiveState)
			return false;
		if (this.prevBase != other.prevBase)
			return false;
		return true;
	}

	public void setCenterHeight( double centerHeight )
	{
		PlotDrawerCenter.centerHeight = centerHeight;
	}
	
    @Override
    public boolean similar(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
}
