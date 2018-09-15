package ca.corefacility.gview.layout.plot;


import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.sequence.SlotPath;

public class PlotDrawerBar extends PlotDrawer
{
	private Stroke barStroke;

	private static final float bottom = -1.0f;

	public PlotDrawerBar()
	{
		this(2.0f);
	}

	public PlotDrawerBar(float barThickness)
	{
		if (barThickness >= 0)
		{
			this.barStroke = new BasicStroke(barThickness);
		}
		else
			throw new IllegalArgumentException("barThickness cannot be negative");
	}

	@Override
	public void drawPoint(int base, double heightInSlot, SlotPath slotPath)
	{
		slotPath.moveTo(base, bottom);
		slotPath.lineTo(base, heightInSlot, Direction.NONE);
	}

	@Override
	public Shape finishPlotPoint(SlotPath slotPath)
	{
		reset();

		return slotPath.createStrokedShape(this.barStroke);
	}

	@Override
	public void reset()
	{
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ (this.barStroke == null ? 0 : this.barStroke.hashCode());
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
		PlotDrawerBar other = (PlotDrawerBar) obj;
		if (this.barStroke == null)
		{
			if (other.barStroke != null)
				return false;
		}
		else if (!this.barStroke.equals(other.barStroke))
			return false;
		return true;
	}

	@Override
	public void drawRange(int startBase, int endBase, double heightInSlot,
			SlotPath slotPath)
	{
		slotPath.moveTo(startBase, bottom);
		slotPath.lineTo(startBase, heightInSlot, Direction.NONE);
		slotPath.lineTo(endBase, startBase <= endBase ? Direction.INCREASING : Direction.DECREASING);
		slotPath.lineTo(endBase, bottom, Direction.NONE);
		slotPath.lineTo(startBase, startBase <= endBase ? Direction.DECREASING : Direction.INCREASING);
	}

	@Override
	public Shape[][] finishPlotRange(SlotPath slotPath)
	{
		reset();

		return new Shape[][] {{slotPath.getShape()}};
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
        PlotDrawerBar other = (PlotDrawerBar) obj;
        if (this.barStroke == null)
        {
            if (other.barStroke != null)
                return false;
        }
        else if (!this.barStroke.equals(other.barStroke))
            return false;
        return true;
    }
}
