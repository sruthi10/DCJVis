package ca.corefacility.gview.utils.content;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.RangeLocation;
import org.biojava.bio.symbol.Symbol;

import ca.corefacility.gview.data.GenomeData;

public class SkewCalculator extends ContentCalculator
{
	/**
	 * Creates a new SkewCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 * @param symbol1  The first symbol to determine the skew for.
	 * @param symbol2  The second symbol to determine the skew for.
	 * @param stepSize  The step size for the skew calculations.
	 * @param windowSize  The window size for the skew calculations.
	 */
	public SkewCalculator(GenomeData genomeData, Symbol symbol1, Symbol symbol2, int stepSize, int windowSize)
	{
		super(genomeData, stepSize, windowSize);
		
		if (symbol1 == null || symbol2 == null)
		{
			throw new IllegalArgumentException("passed symbols are null");
		}
		else if (symbol1.equals(symbol2))
		{
			throw new IllegalArgumentException("passed symbols are equal");
		}
		
		setMatchSymbols(new Symbol[]{symbol1, symbol2});
	}

	/**
	 * Creates a new SkewCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 * @param symbol1  The first symbol to determine the skew for.
	 * @param symbol2  The second symbol to determine the skew for.
	 */
	public SkewCalculator(GenomeData genomeData, Symbol symbol1, Symbol symbol2)
	{
		super(genomeData);
		
		if (symbol1 == null || symbol2 == null)
		{
			throw new IllegalArgumentException("passed symbols are null");
		}
		else if (symbol1.equals(symbol2))
		{
			throw new IllegalArgumentException("passed symbols are equal");
		}
		
		setMatchSymbols(new Symbol[]{symbol1, symbol2});
	}
	
	/**
	 * Creates a new SkewCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 * @param circular  Whether or not the genomeData represents a circular genome.
	 * @param symbol1  The first symbol to determine the skew for.
	 * @param symbol2  The second symbol to determine the skew for.
	 * @param stepSize  The step size for the skew calculations.
	 * @param windowSize  The window size for the skew calculations.
	 */
	public SkewCalculator(GenomeData genomeData, boolean circular, Symbol symbol1, Symbol symbol2, int stepSize, int windowSize)
	{
		super(genomeData, circular, stepSize, windowSize);
		
		if (symbol1 == null || symbol2 == null)
		{
			throw new IllegalArgumentException("passed symbols are null");
		}
		else if (symbol1.equals(symbol2))
		{
			throw new IllegalArgumentException("passed symbols are equal");
		}
		
		setMatchSymbols(new Symbol[]{symbol1, symbol2});
	}

	/**
	 * Creates a new SkewCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 * @param circular  Whether or not the genomeData represents a circular genome.
	 * @param symbol1  The first symbol to determine the skew for.
	 * @param symbol2  The second symbol to determine the skew for.
	 */
	public SkewCalculator(GenomeData genomeData, boolean circular, Symbol symbol1, Symbol symbol2)
	{
		super(genomeData, circular);
		
		if (symbol1 == null || symbol2 == null)
		{
			throw new IllegalArgumentException("passed symbols are null");
		}
		else if (symbol1.equals(symbol2))
		{
			throw new IllegalArgumentException("passed symbols are equal");
		}
		
		setMatchSymbols(new Symbol[]{symbol1, symbol2});
	}
	
	/**
	 * Class which sets up SkewCalculator to perform gc skew calculations.
	 * @author aaron
	 *
	 */
	public static class GC extends SkewCalculator
	{
		/**
		 * Creates a new GC skew calculator around the passed genomic data, with the passed properties.
		 * @param genomeData  The data to calculate averages for.
		 * @param stepSize  The step size for the skew calculations.
		 * @param windowSize  The window size for the skew calculations.
		 */
		public GC(GenomeData genomeData, int stepSize, int windowSize)
		{
			super(genomeData, DNATools.g(), DNATools.c(), stepSize, windowSize);
		}

		/**
		 * Creates a new GC skew calculator around the passed genomic data, with the passed properties.
		 * @param genomeData  The data to calculate averages for.
		 */
		public GC(GenomeData genomeData)
		{
			super(genomeData, DNATools.g(), DNATools.c());
		}
	}

	@Override
	public Iterator<RangeValue> contentIterator()
	{
		return new SymbolsSkewIterator(getMatchSymbols());
	}

	private class SymbolsSkewIterator extends ContentIterator implements Iterator<RangeValue>
	{
		public SymbolsSkewIterator(Symbol[] matchSymbols)
		{
			super(matchSymbols);
			
			if (matchSymbols == null || matchSymbols.length != 2)
			{
				throw new IllegalArgumentException("matchSymbols must have a length of exactly 2");
			}
		}

		@Override
		public RangeValue next()
		{
			if (!hasNext())
			{
				throw new NoSuchElementException("no more symbol-content averages");
			}
			
			int[] symbolCounts = new int[matchSymbols.length];
			fillCountsForCurrentWindow(symbolCounts);
			
			RangeValue rangeValue = new RangeValue();

			if (symbolCounts[0] == 0 && symbolCounts[1] == 0)
			{
				rangeValue.value = 0.0;
			}
			else
			{
				rangeValue.value = ((double)symbolCounts[0]-symbolCounts[1])/(symbolCounts[0]+symbolCounts[1]);
			}
			rangeValue.location = new RangeLocation(currentStartBase(), currentStopBase());
			
			nextStep();
			
			return rangeValue;
		}	
	}

	/**
	 * Calculates average skew for the whole sequence.
	 * @return  The average skew for the whole sequence.
	 */
	public double getTotalAverageContent()
	{
		int symbol0Count = 0;
		int symbol1Count = 0;
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
				
				if (matchSymbols[0].equals(currSymbol))
				{
					symbol0Count++;
				}
				else if (matchSymbols[1].equals(currSymbol))
				{
					symbol1Count++;
				}
			}
			
			if (symbol0Count + symbol1Count == 0)
			{
				return 0.0;
			}
			else
			{
				return (double)(symbol0Count - symbol1Count)/(double)(symbol0Count + symbol1Count);
			}
			
		}
	}
}
