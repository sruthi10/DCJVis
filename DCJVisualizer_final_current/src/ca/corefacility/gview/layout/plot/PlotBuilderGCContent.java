package ca.corefacility.gview.layout.plot;

import java.awt.Shape;
import java.util.Iterator;

import org.biojava.bio.seq.DNATools;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.layout.sequence.SlotPath;
import ca.corefacility.gview.utils.ProgressHandler;
import ca.corefacility.gview.utils.content.AverageCalculator;
import ca.corefacility.gview.utils.content.ContentCalculator.RangeValue;

public class PlotBuilderGCContent extends PlotBuilderSymbols
{
	public PlotBuilderGCContent()
	{
		super();
	}
	
	public PlotBuilderGCContent(int windowSize, int stepSize)
	{
		super(windowSize,stepSize);
	}
	
	protected PlotBuilderGCContent(PlotBuilderGCContent other)
	{
	    super(other);
	}
	
	@Override
	public Shape[][] createPlot(GenomeData genomeData, SlotPath path, PlotDrawer plotDrawer)
	{
		AverageCalculator calculator;
		
		if (!DNATools.getDNA().equals(genomeData.getSequence().getAlphabet()))
		{
			throw new IllegalArgumentException("alphabet for this sequence data is of type \""
					+ genomeData.getSequence().getAlphabet().getName() + "\" and not of type \"DNA\"");
		}
		
		if (genomeData.getSequenceLength() <= 0)
		{
			throw new IllegalArgumentException("no symbols for sequence data");
		}		

		ProgressHandler.setMessage(ProgressHandler.CALCULATING_GC_CONTENT_STRING + "...");
		
		if (windowSize > 0 && stepSize > 0)
		{
			calculator = new AverageCalculator.GC(genomeData, stepSize, windowSize);
		}
		else
		{
			calculator = new AverageCalculator.GC(genomeData);
		}
		
		double averageGC = calculator.getTotalAverageContent();
		setCenterHeight( averageGC );
		
		Iterator<RangeValue> averageIter = calculator.contentIterator();
		
		while (averageIter.hasNext())
		{
			RangeValue rangeValue = averageIter.next();
			
			addRange( rangeValue.location.getMin(), rangeValue.location.getMax(), rangeValue.value);
		}
		
		autoScaleCenter();
		return super.createPlot( genomeData, path, plotDrawer );
	}
	
	@Override
	public boolean equals(Object o)
	{
	    if (o == null)
	        return false;
	    else if (this == o)
    	    return true;
	    else if (super.equals(o) && o.getClass() == PlotBuilderGCContent.class)
	        return true;
	    return false;
	}
	
   @Override
    public boolean similar(PlotBuilder o)
    {
        if (o == null)
            return false;
        else if (this == o)
            return true;
        else if (super.similar(o) && o.getClass() == PlotBuilderGCContent.class)
            return true;
        return false;
    }
   
   @Override
   protected Object clone() throws CloneNotSupportedException
   {
       return new PlotBuilderGCContent(this);
   }
}
