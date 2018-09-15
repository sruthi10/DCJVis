package ca.corefacility.gview.layout.plot;


import java.awt.Shape;
import java.net.URI;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.biojava.bio.symbol.RangeLocation;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.layout.RangeLocationComparator;
import ca.corefacility.gview.layout.sequence.SlotPath;

public class PlotBuilderRange extends PlotBuilder
{
    private SortedMap<RangeLocation, Double> ranges;
	
	private URI uri;

	private double centerHeight;

	private static final int	MIN	= 0;
	private static final int	MAX	= 1;

	public PlotBuilderRange()
	{
	    this.ranges = new TreeMap<RangeLocation, Double>(new RangeLocationComparator());
	    uri = null;
	}
	
	public PlotBuilderRange(SortedMap<RangeLocation, Double> ranges, URI uri)
	{
		this.ranges = ranges;
		this.centerHeight = 0;
		this.uri = uri;
		this.minValue = -1;
		this.maxValue = 1;
	}
	
	protected PlotBuilderRange(PlotBuilderRange other)
	{
	    super(other);
	    ranges = (TreeMap<RangeLocation,Double>)((TreeMap<RangeLocation,Double>)other.ranges).clone();
	    centerHeight = other.centerHeight;
	    this.uri = other.uri;
	}

	public void setCenterHeight( double height )
	{
		this.centerHeight = height;
	}

	/**
	 * Automatically sets scaling of points so that max point is at top of slot, min is at bottom of slot.
	 */
	@Override
	public void autoScale()
	{
		double minValue = Double.MAX_VALUE;
		double maxValue = -Double.MAX_VALUE;
		double currValue;

		for (RangeLocation range : this.ranges.keySet())
		{
			currValue = this.ranges.get(range);

			if (currValue < minValue)
			{
				minValue = currValue;
			}

			if (currValue > maxValue)
			{
				maxValue = currValue;
			}
		}

		scale(minValue, maxValue);
	}

	@Override
	public Shape[][] createPlot(GenomeData genomeData, SlotPath path, PlotDrawer plotDrawer)
	{
		RangeLocation currLocation;
		double currHeight;

		Set<RangeLocation> rangeSet = this.ranges.keySet();
		Iterator<RangeLocation> rangeIterator = rangeSet.iterator();

		while (rangeIterator.hasNext())
		{
			currLocation = rangeIterator.next();
			currHeight = getScaledHeight(this.ranges.get(currLocation));

			plotDrawer.drawRange(currLocation.getMin(), currLocation.getMax(), currHeight, path);
		}

		return plotDrawer.finishPlotRange(path);
	}

	@Override
	public int getNumPoints()
	{
		return this.ranges.size();
	}

	@Override
	public void autoScaleCenter()
	{
		double[] minMax = getMaxMinValues();

		double distance = minMax[MAX] - minMax[MIN];

		double low = centerHeight - minMax[MIN];
		double high = minMax[MAX] - centerHeight;

		if( high >= low )
		{
			scale( minMax[MAX] - high * 2, minMax[MAX] );
		}
		else
		{
			scale( minMax[MIN], minMax[MIN] + low * 2 );
		}
	}

    /**
     * Adds a range to this plot.
     * @param startBase
     * @param endBase
     * @param height
     */
    protected void addRange(int startBase, int endBase, double height)
    {
        this.ranges.put(new RangeLocation(startBase, endBase), height);
    }

	/**
	 * @return Minimum and Maximum values in the plot.  Minimum value is in the returned array index 0.  Maximum value is in the returned array index 1.
	 */
	@Override
	public double[] getMaxMinValues()
	{
		double[] maxMin = new double[2];
		maxMin[MIN] = Double.MAX_VALUE;
		maxMin[MAX] = -Double.MAX_VALUE;
		double currValue;

		for (RangeLocation range : this.ranges.keySet())
		{
			currValue = this.ranges.get(range);

			if (currValue < maxMin[MIN])
			{
				maxMin[MIN] = currValue;
			}

			if (currValue > maxMin[MAX])
			{
				maxMin[MAX] = currValue;
			}
		}

		return maxMin;
	}
	
	public URI getURI()
	{
		return uri;
	}

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(centerHeight);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((ranges == null) ? 0 : ranges.hashCode());
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlotBuilderRange other = (PlotBuilderRange) obj;
        if (Double.doubleToLongBits(centerHeight) != Double
                .doubleToLongBits(other.centerHeight))
            return false;
        if (ranges == null)
        {
            if (other.ranges != null)
                return false;
        } else if (!ranges.equals(other.ranges))
            return false;
        if (uri == null)
        {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
            return false;
        return true;
    }
    
    @Override
    public boolean similar(PlotBuilder obj)
    {
        if (this == obj)
            return true;
        if (!super.similar(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlotBuilderRange other = (PlotBuilderRange) obj;
        if (uri == null)
        {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
            return false;
        return true;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return new PlotBuilderRange(this);
    }
}
