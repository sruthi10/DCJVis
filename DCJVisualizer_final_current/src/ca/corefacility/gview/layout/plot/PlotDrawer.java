package ca.corefacility.gview.layout.plot;


import java.awt.Shape;

import ca.corefacility.gview.layout.sequence.SlotPath;

/**
 * Defines a way to draw different "styles" of plots.
 * @author aaron
 *
 */
public abstract class PlotDrawer
{
	/**
	 * Draws out the next point on this plot.
	 *  The points passed here should be in order of ascending base value.
	 * @param base  The base to draw this point on.
	 * @param heightInSlot  The height in the slot to draw the point (from -1 to 1).
	 * @param slotPath  The path to draw into.
	 */
	public abstract void drawPoint(int base, double heightInSlot, SlotPath slotPath);

	/**
	 * Draws out a point in a given range.
	 * @param startBase
	 * @param endBase
	 * @param heightInSlot
	 * @param slotPath
	 */
	public abstract void drawRange(int startBase, int endBase, double heightInSlot, SlotPath slotPath);

	/**
	 * Resets this PlotDrawer so we can draw a new set of points.
	 */
	public abstract void reset();

	/**
	 * Adds finishing touches to this plot.
	 * @param slotPath  The SlotPath we are drawing into.
	 * 
	 * @return Shape  The shape object for this plot.
	 */
	public abstract Shape finishPlotPoint(SlotPath slotPath);

	public abstract Shape[][] finishPlotRange(SlotPath slotPath);
	
    public boolean similar(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof PlotDrawer)
            return false;
        return true;
    }
}