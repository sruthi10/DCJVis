package ca.corefacility.gview.layout.plot;

import java.awt.Shape;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.symbol.Location;
import org.biojava.bio.symbol.RangeLocation;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.layout.NoAnnotationException;
import ca.corefacility.gview.layout.RangeLocationComparator;
import ca.corefacility.gview.layout.sequence.SlotPath;

/**
 * Defines a plot whose values are specified in terms of an annotation/data value
 * ex. locus_tag, value
 * @author aaron
 *
 */
public class PlotBuilderAnnotation extends PlotBuilder
{
    private boolean autoscale;

	private Map<FeatureFilterMappable, Double> annotationValuesMap;

	public PlotBuilderAnnotation(Map<FeatureFilterMappable, Double> annotationValuesMap)
	{
		this.autoscale = false;
		this.annotationValuesMap = annotationValuesMap;
	}
	
	protected PlotBuilderAnnotation(PlotBuilderAnnotation other)
	{
	    autoscale = other.autoscale;
	    annotationValuesMap = (Map<FeatureFilterMappable,Double>)((HashMap<FeatureFilterMappable,Double>)other.annotationValuesMap).clone();
	}

	@Override
	public Shape[][] createPlot(GenomeData genomeData, SlotPath path, PlotDrawer plotDrawer)
	{
		SortedMap<RangeLocation, Double> rangeMap; // stores any ranges generated from the passed genome data

		RangeLocation currLocation;
		double currHeight;
		Shape plotShape = null;

		try
		{
			rangeMap = getRangesFor(genomeData);

			if (this.autoscale)
			{
				performAutoScale(rangeMap);
			}

			Set<RangeLocation> rangeSet = rangeMap.keySet();
			Iterator<RangeLocation> rangeIterator = rangeSet.iterator();

			while (rangeIterator.hasNext())
			{
				currLocation = rangeIterator.next();
				currHeight = getScaledHeight(rangeMap.get(currLocation));

				plotDrawer.drawRange(currLocation.getMin(), currLocation.getMax(), currHeight, path);
			}

			plotShape = plotDrawer.finishPlotRange(path)[0][0];
		}
		catch (NoAnnotationException e)
		{
			System.err.println(e);
		}

		return new Shape[][]{{plotShape}};
	}

	private void performAutoScale(SortedMap<RangeLocation, Double> rangeMap)
	{
		double minValue = Double.MAX_VALUE;
		double maxValue = Double.MIN_VALUE;
		double currValue;

		for (RangeLocation range : rangeMap.keySet())
		{
			currValue = rangeMap.get(range);

			if (currValue < minValue)
			{
				minValue = currValue;
			}

			if (currValue > maxValue)
			{
				maxValue = currValue;
			}
		}

		super.scale(minValue, maxValue);
	}

	public SortedMap<RangeLocation, Double> getRangesFor(GenomeData data) throws NoAnnotationException
	{
		SortedMap<RangeLocation, Double> rangeMap = new TreeMap<RangeLocation, Double>(new RangeLocationComparator());

		// iterates through all feature filters, extracts the features, and adds the ranges
		// note: this performs a linear search per each feature filter, possibly slow?
		for (FeatureFilterMappable currFilterMappable : this.annotationValuesMap.keySet())
		{
		    FeatureFilter currFilter = currFilterMappable.getFilter();
			FeatureHolder currHolder = data.getFeatures(currFilter);

			if (currHolder == null || currHolder.countFeatures() == 0)
				throw new NoAnnotationException("no features for annotation " + currFilter);

			Iterator<Feature> featureIterator = currHolder.features();
			while (featureIterator.hasNext())
			{
				Feature currFeature = featureIterator.next();

				Location location = currFeature.getLocation();

				Iterator blockLocationIter = location.blockIterator();
				while (blockLocationIter.hasNext())
				{
					Location block = (Location) blockLocationIter.next();

					RangeLocation range = new RangeLocation(block.getMin(), block.getMax());
					rangeMap.put(range, this.annotationValuesMap.get(currFilterMappable)); // place range into map
				}
			}
		}

		return rangeMap;
	}

	/**
	 * Automatically sets scaling of points so that max point is at top of slot, min is at bottom of slot.
	 */
	@Override
	public void autoScale()
	{
		this.autoscale = true;
	}

	@Override
	public int getNumPoints()
	{
		return this.annotationValuesMap.size();
	}

	@Override
	public void autoScaleCenter()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public double[] getMaxMinValues()
	{
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime
                * result
                + ((annotationValuesMap == null) ? 0 : annotationValuesMap
                        .hashCode());
        result = prime * result + (autoscale ? 1231 : 1237);
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
        PlotBuilderAnnotation other = (PlotBuilderAnnotation) obj;
        if (annotationValuesMap == null)
        {
            if (other.annotationValuesMap != null)
                return false;
        } else if (!annotationValuesMap.equals(other.annotationValuesMap))
            return false;
        if (autoscale != other.autoscale)
            return false;
        return true;
    }
    
    @Override
    public boolean similar(PlotBuilder plotBuilder)
    {
        if (this == plotBuilder)
            return true;
        if (!super.similar(plotBuilder))
            return false;
        if (getClass() != plotBuilder.getClass())
            return false;
        PlotBuilderAnnotation other = (PlotBuilderAnnotation) plotBuilder;
        if (autoscale != other.autoscale)
            return false;
        return true;
    }

    @Override
    public URI getURI()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return new PlotBuilderAnnotation(this);
    }
}
