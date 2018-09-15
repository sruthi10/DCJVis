package ca.corefacility.gview.layout.plot;


import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.sequence.SlotPath;

public class PlotDrawerLine extends PlotDrawer
{
	private boolean finishedInitialPoint = false;

	private Stroke stroke;

	public PlotDrawerLine()
	{
		this(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	}

	public PlotDrawerLine(Stroke stroke)
	{
		if (stroke != null)
		{
			this.stroke = stroke;
		}
		else
			throw new IllegalArgumentException("stroke cannot be null");
	}

	@Override
	public void drawPoint(int base, double heightInSlot, SlotPath slotPath)
	{
		if (!this.finishedInitialPoint)
		{
			slotPath.moveTo(base, heightInSlot);

			this.finishedInitialPoint = true;
		}
		else
		{
			slotPath.lineTo(base, heightInSlot, Direction.INCREASING);
		}
	}

	@Override
	public Shape finishPlotPoint(SlotPath slotPath)
	{
		reset();

		return slotPath.createStrokedShape(this.stroke);
	}

	@Override
	public void reset()
	{
		this.finishedInitialPoint = false;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.stroke == null ? 0 : this.stroke.hashCode());
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
		PlotDrawerLine other = (PlotDrawerLine) obj;
		if (this.stroke == null)
		{
			if (other.stroke != null)
				return false;
		}
		else if (!this.stroke.equals(other.stroke))
			return false;
		return true;
	}

	@Override
	public void drawRange(int startBase, int endBase, double heightInSlot,
			SlotPath slotPath)
	{
		drawPoint(startBase, heightInSlot, slotPath);
		drawPoint(endBase, heightInSlot, slotPath);
	}

	@Override
	public Shape[][] finishPlotRange(SlotPath slotPath)
	{
		return new Shape[][] {{finishPlotPoint(slotPath)}};
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
        PlotDrawerLine other = (PlotDrawerLine) obj;
        if (this.stroke == null)
        {
            if (other.stroke != null)
                return false;
        }
        else if (!this.stroke.equals(other.stroke))
            return false;
        return true;
    }
}
