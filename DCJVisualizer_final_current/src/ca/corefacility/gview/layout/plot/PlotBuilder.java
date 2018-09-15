package ca.corefacility.gview.layout.plot;

import java.awt.Shape;
import java.net.URI;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.layout.sequence.SlotPath;

public abstract class PlotBuilder
{
	protected double minValue;
	protected double maxValue;
	
	protected PlotBuilder(PlotBuilder other)
	{
	    if (other != null)
	    {
	        minValue = other.minValue;
	        maxValue = other.maxValue;
	    }
	}
	
	public PlotBuilder()
	{}

	/**
	 * Builds the plot defined by this builder within the passed path
	 * @param genomeData  The GenomeData we are using.
	 * @param path  The path to build the plot on
	 * @param plotDrawer  The PlotDrawer defining how to draw the plot.
	 * @return  A Shape defining the plot we created.
	 */
	public abstract Shape[][] createPlot(GenomeData genomeData, SlotPath path, PlotDrawer plotDrawer);

	/**
	 * Returns the heightInSlot corresponding to the passed height, scaled to be between [-1, 1]
	 * @param height  The height to scale.
	 */
	public double getScaledHeight(double height)
	{
		double totalDiff = this.maxValue - this.minValue;
		
		if (totalDiff == 0)
		{
			return 0;
		}

		// cut off value if it exceeds bounds
		if (height > this.maxValue)
		{
			height = this.maxValue;
		}
		else if (height < this.minValue)
		{
			height = this.minValue;
		}

		// scale value between 0 and totalDiff
		height = height - this.minValue;

		// scale value between 0 and 2
		height = 2*height/ totalDiff;

		// scale value between -1 and 1
		height = height - 1;

		return height;
	}

	/**
	 * Automatically sets scaling of points so that max point is at top of slot, min is at bottom of slot.
	 */
	public abstract void autoScale();

	/**
	 * Automatically sets scaling of points so that the largest value on either side of the center point is the highest point above and below the line
	 */
	public abstract void autoScaleCenter();


	/**
	 * Scales the construction of this plot to be between (min, max).
	 * @param min
	 * @param max
	 */
	public void scale(double min, double max)
	{
		this.minValue = min;
		this.maxValue = max;
	}

	@Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(maxValue);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minValue);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        PlotBuilder other = (PlotBuilder) obj;
        if (Double.doubleToLongBits(maxValue) != Double
                .doubleToLongBits(other.maxValue))
            return false;
        if (Double.doubleToLongBits(minValue) != Double
                .doubleToLongBits(other.minValue))
            return false;
        return true;
    }

    public abstract int getNumPoints();

	public abstract double[] getMaxMinValues();
	
	public abstract URI getURI();

	public boolean similar(PlotBuilder plotBuilder)
	{
        if (this == plotBuilder)
            return true;
        if (plotBuilder == null)
            return false;
        if (getClass() != plotBuilder.getClass())
            return false;
        return true;
	}
}