package ca.corefacility.gview.layout.plot;

import java.awt.Shape;
import java.net.URI;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.layout.sequence.SlotPath;

public class PlotBuilderPoints extends PlotBuilder
{
	private SortedMap<Integer, Double> points;
	
	private URI uri;

	public PlotBuilderPoints(SortedMap<Integer,Double> points, URI uri)
	{
		this.points = points;

		this.minValue = -1;
		this.maxValue = 1;
	}

	@Override
	public void autoScale()
	{
		double minValue = Double.MAX_VALUE;
		double maxValue = Double.MIN_VALUE;
		double currValue;

		for (int base : this.points.keySet())
		{
			currValue = this.points.get(base);

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
		int currBase;
		double currHeight;
		Shape plotShape = null;

		Set<Integer> baseSet = this.points.keySet();
		Iterator<Integer> baseIterator = baseSet.iterator();

		while (baseIterator.hasNext())
		{
			currBase = baseIterator.next();
			currHeight = getScaledHeight(this.points.get(currBase));

			plotDrawer.drawPoint(currBase, currHeight, path);
		}

		plotShape = plotDrawer.finishPlotPoint(path);

		return new Shape[][]{{plotShape}};
	}

	@Override
	public int getNumPoints()
	{
		// TODO Auto-generated method stub
		return this.points.size();
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
	
	public URI getURI()
	{
		return uri;
	}
	
	public void setURI(URI uri)
	{
		this.uri = uri;
	}
}
