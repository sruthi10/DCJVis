package ca.corefacility.gview.layout.plot;


import java.awt.Shape;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.sequence.SlotPath;

public class PlotDrawerSolid extends PlotDrawer
{
	boolean finishedInitialPoint = false;

	private int initialBase;

	private int prevBase;

	private final static double bottomHeight = -1;

	@Override
	public void drawPoint(int base, double heightInSlot, SlotPath slotPath)
	{
		if (!this.finishedInitialPoint)
		{
			slotPath.moveTo(base, heightInSlot);

			this.prevBase = base;
			this.initialBase = base;
			this.finishedInitialPoint = true;
		}
		else
		{
			slotPath.lineTo(base, heightInSlot, Direction.INCREASING);

			this.prevBase = base;
		}
	}

	@Override
	public Shape finishPlotPoint(SlotPath slotPath)
	{
		if (this.finishedInitialPoint)
		{
			slotPath.lineTo(this.prevBase, bottomHeight, Direction.NONE);
			slotPath.lineTo(this.initialBase, Direction.DECREASING);
			slotPath.closePath();
		}

		reset();

		return slotPath.getShape();
	}

	@Override
	public void reset()
	{
		this.finishedInitialPoint = false;
	}

	@Override
	public void drawRange(int startBase, int endBase, double heightInSlot,
			SlotPath slotPath)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Shape[][] finishPlotRange(SlotPath slotPath)
	{
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (finishedInitialPoint ? 1231 : 1237);
        result = prime * result + initialBase;
        result = prime * result + prevBase;
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
        PlotDrawerSolid other = (PlotDrawerSolid) obj;
        if (finishedInitialPoint != other.finishedInitialPoint)
            return false;
        if (initialBase != other.initialBase)
            return false;
        if (prevBase != other.prevBase)
            return false;
        return true;
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
