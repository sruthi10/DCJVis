package ca.corefacility.gview.utils.content;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.biojava.bio.symbol.RangeLocation;
import org.biojava.bio.symbol.Symbol;

import ca.corefacility.gview.data.GenomeData;

public class AverageCalculator extends ContentCalculator
{		
	/**
	 * Creates a new AverageCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 * @param symbols  A set of symbols this AverageCalculator will search through for matches.
	 * @param stepSize  The step size for the average calculations.
	 * @param windowSize  The window size for the average calculations.
	 */
	public AverageCalculator(GenomeData genomeData, Set<Symbol> symbols, int stepSize, int windowSize)
	{
		super(genomeData, stepSize, windowSize);
		setMatchSymbols(symbols);
	}

	/**
	 * Creates a new AverageCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 * @param symbols  A set of symbols this AverageCalculator will search through for matches.
	 */
	public AverageCalculator(GenomeData genomeData, Set<Symbol> symbols)
	{
		super(genomeData);
		setMatchSymbols(symbols);
	}
	
	/**
	 * Creates a new AverageCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 * @param circular  Whether or not the genomeData represents a circular genome.
	 * @param symbols  A set of symbols this AverageCalculator will search through for matches.
	 * @param stepSize  The step size for the average calculations.
	 * @param windowSize  The window size for the average calculations.
	 */
	public AverageCalculator(GenomeData genomeData, boolean circular, Set<Symbol> symbols, int stepSize, int windowSize)
	{
		super(genomeData, circular, stepSize, windowSize);
		setMatchSymbols(symbols);
	}

	/**
	 * Creates a new AverageCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 * @param circular  Whether or not the genomeData represents a circular genome.
	 * @param symbols  A set of symbols this AverageCalculator will search through for matches.
	 */
	public AverageCalculator(GenomeData genomeData, boolean circular, Set<Symbol> symbols)
	{
		super(genomeData, circular);
		setMatchSymbols(symbols);
	}
	
	/**
	 * Sets up AverageCalculator to perform GC content calculations.
	 * @author aaron
	 *
	 */
	public static class GC extends AverageCalculator
	{
		/** Creates a new GC calculator around the passed genomic data, with the passed properties.
		 * @param genomeData  The data to calculate averages for.
		 * @param circular  Whether or not the genomeData represents a circular genome. 
		 * @param stepSize  The step size for the average calculations.
		 * @param windowSize  The window size for the average calculations.
		 */
		public GC(GenomeData genomeData, boolean circular, int stepSize, int windowSize)
		{
			super(genomeData, circular, gc, stepSize, windowSize);
		}

		/** Creates a new GC calculator around the passed genomic data, with the passed properties.
		 * @param genomeData  The data to calculate averages for.
	     * @param circular  Whether or not the genomeData represents a circular genome. 
		 */
		public GC(GenomeData genomeData, boolean circular)
		{
			super(genomeData, circular, gc);
		}

		/** Creates a new GC calculator around the passed genomic data, with the passed properties.
		 * @param genomeData  The data to calculate averages for.
		 * @param stepSize  The step size for the average calculations.
		 * @param windowSize  The window size for the average calculations.
		 */
		public GC(GenomeData genomeData,
				int stepSize, int windowSize)
		{
			super(genomeData, gc, stepSize, windowSize);
		}

		/** Creates a new GC calculator around the passed genomic data, with the passed properties.
		 * @param genomeData  The data to calculate averages for.
		 */
		public GC(GenomeData genomeData)
		{
			super(genomeData, gc);
		}
	}
	
	/**
	 * Calculates the average symbol content from the registered symbols.
	 * @return  The average symbol content as a proportion of the length.
	 */
	public double getTotalAverageContent()
	{
		int count = 0;
		int length = seq.length();
		
		if (length == 0)
		{
			return 0.0;
		}
		else
		{
			for (int i = 1; i <= length; i++) // symbols start at 1
			{
				Symbol currSymbol = seq.symbolAt(i);
				
				if (containsSymbol(currSymbol))
				{
					count++;
				}
			}
			
			return (double)count/(double)length;
		}
	}
	
	private class SymbolsAverageIterator extends ContentIterator implements Iterator<RangeValue>
	{					
		public SymbolsAverageIterator(Symbol[] matchSymbols)
		{
			super(matchSymbols);			
		}
		
		private int totalCounts(int[] symbolCounts)
		{
			int total = 0;
			for (int i = 0; i < symbolCounts.length; i++)
			{
				total += symbolCounts[i];
			}
			return total;
		}

		@Override
		public RangeValue next()
		{
			if (!hasNext())
			{
				throw new NoSuchElementException("no more symbol-content averages");
			}

			int[] symbolCounts = new int[matchSymbols.length];
			int baseLength = fillCountsForCurrentWindow(symbolCounts);
			int totalSymbolsCount = totalCounts(symbolCounts);
			
			RangeValue rangeAverage = new RangeValue();

			rangeAverage.value = (double)totalSymbolsCount/baseLength;
			rangeAverage.location = new RangeLocation(currentStartBase(), currentStopBase());
			
			nextStep();
			
			return rangeAverage;
		}
	}

	@Override
	public Iterator<RangeValue> contentIterator()
	{
		return new SymbolsAverageIterator(getMatchSymbols());
	}
}
