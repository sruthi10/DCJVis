package ca.corefacility.gview.utils.content;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.symbol.RangeLocation;
import org.biojava.bio.symbol.Symbol;

import ca.corefacility.gview.data.GenomeData;

/**
 * Calculates the content frequencies of symbols within the sequence.
 * @author aaron
 *
 */
public abstract class ContentCalculator
{
	protected static final Set<Symbol> gc = new HashSet<Symbol>();
	
	static
	{
		gc.add(DNATools.g());
		gc.add(DNATools.c());
	}
	
	protected GenomeData genomeData;
	protected Sequence seq;
		
	protected int windowSize;
	protected int stepSize;
	
	protected Symbol[] matchSymbols;
	
	private boolean circular;
		
	/**
	 * Creates a new ContentCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 * @param circular  Whether or not the genomeData represents a circular genome.
	 * @param stepSize  The step size for the average calculations.
	 * @param windowSize  The window size for the average calculations.
	 */
	public ContentCalculator(GenomeData genomeData, boolean circular, int stepSize, int windowSize)
	{
		if (genomeData == null)
		{
			throw new IllegalArgumentException("Error: genomeData is null");
		}
		
		if (genomeData.getSequence() == null)
		{
			throw new IllegalArgumentException("Error: genomeData contains no sequence");
		}
		
		if (windowSize < 0)
		{
			throw new IllegalArgumentException("window size cannot be negative");
		}
		
		this.genomeData = genomeData;
		this.seq = genomeData.getSequence();
		
		this.matchSymbols = new Symbol[0];
		
		this.windowSize = windowSize;
		this.stepSize = stepSize;
		
		this.circular = circular;
	}
	
	/**
	 * Creates a new ContentCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 * @param stepSize  The step size for the average calculations.
	 * @param windowSize  The window size for the average calculations.
	 */
	public ContentCalculator(GenomeData genomeData, int stepSize, int windowSize)
	{
		if (genomeData == null)
		{
			throw new IllegalArgumentException("Error: genomeData is null");
		}
		
		if (genomeData.getSequence() == null)
		{
			throw new IllegalArgumentException("Error: genomeData contains no sequence");
		}
		
		if (windowSize < 0)
		{
			throw new IllegalArgumentException("window size cannot be negative");
		}
		
		this.genomeData = genomeData;
		this.seq = genomeData.getSequence();
		
		this.matchSymbols = new Symbol[0];
		
		this.windowSize = windowSize;
		this.stepSize = stepSize;
		
		this.circular = genomeData.isCircular();
	}
	
	/**
	 * Creates a new ContentCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 */
	public ContentCalculator(GenomeData genomeData)
	{
		this(genomeData, 1, 0);
		
		windowSize = estimateWindow(genomeData);
		stepSize = estimateStep(genomeData);
	}
	
	/**
	 * Creates a new ContentCalculator around the passed genomic data, with the passed properties.
	 * @param genomeData  The data to calculate averages for.
	 * @param circular  Whether or not the genomeData represents a circular genome.
	 */
	public ContentCalculator(GenomeData genomeData, boolean circular)
	{
		this(genomeData, circular, 1, 0);
		
		windowSize = estimateWindow(genomeData);
		stepSize = estimateStep(genomeData);
	}
	
	protected void setMatchSymbols(Set<Symbol> symbols)
	{
		if (symbols == null)
		{
			throw new IllegalArgumentException("symbols is null");
		}
		
		this.matchSymbols = symbols.toArray(this.matchSymbols);
	}
	
	protected void setMatchSymbols(Symbol[] symbols)
	{
		if (symbols == null)
		{
			throw new IllegalArgumentException("symbols is null");
		}
		
		this.matchSymbols = symbols;
	}
	
	protected Symbol[] getMatchSymbols()
	{
		return matchSymbols;
	}
	
	protected boolean containsSymbol(Symbol s)
	{
		for (Symbol check : matchSymbols)
		{
			if (check.equals(s))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private int estimateStep(GenomeData genomeData)
	{
		int seqLength = genomeData.getSequenceLength();
		int stepSize;
		
		if (seqLength < 1000)
		{
			stepSize = 1;
		}
		else if (seqLength < 10000)
		{
			stepSize = 1;
		}
		else if (seqLength < 100000)
		{
			stepSize = 1;
		}
		else if (seqLength < 1000000)
		{
			stepSize = 10;
		}
		else if (seqLength < 10000000)
		{
			stepSize = 100;
		}
		else
		{
			stepSize = 100;
		}
		
		return stepSize;
	}
	
	private int estimateWindow(GenomeData genomeData)
	{
		int seqLength = genomeData.getSequenceLength();
		int windowSize;
		
		if (seqLength < 1000)
		{
			windowSize = 10;
		}
		else if (seqLength < 10000)
		{
			windowSize = 50;
		}
		else if (seqLength < 100000)
		{
			windowSize = 500;
		}
		else if (seqLength < 1000000)
		{
			windowSize = 1000;
		}
		else if (seqLength < 10000000)
		{
			windowSize = 10000;
		}
		else
		{
			windowSize = 10000;
		}
		
		return windowSize;
	}
	
	/**
	 * Creates an iterator to iterate over the content and do the appropriate calculations.
	 * @return  A RangeValue which contains a range location on the sequence, and the value for this range.
	 */
	public abstract Iterator<RangeValue> contentIterator();
	
	/**
	 * Class to tie together the location on the sequence and a value.
	 * @author aaron
	 *
	 */
	public class RangeValue
	{
		/**
		 * The location on the sequence.
		 */
		public RangeLocation location;
		
		/**
		 * The value for this location.
		 */
		public double value;
	}
	
	protected abstract class ContentIterator implements Iterator<RangeValue>
	{
		protected Symbol[] matchSymbols;
		
		private int upstreamLength;
		private int downstreamLength;
		
		private int currentStep;
		private int currentStartBase;
		private int currentStopBase;

		
		public ContentIterator(Symbol[] matchSymbols)
		{
			this.matchSymbols = matchSymbols;
			
			currentStep = 0;
			
			// done so upstreamLength + downstreamLength = windowSize
			upstreamLength = (int)(windowSize/2.0);
			downstreamLength = windowSize - upstreamLength;
			
			if (isCircular())
			{
				currentStartBase = 1;
				currentStopBase = currentStartBase + stepSize;
			}
			else
			{
				currentStartBase = 1 + upstreamLength;
				currentStopBase = currentStartBase + stepSize;
			}
		}
		
		protected boolean containsSymbol(Symbol s)
		{
			for (Symbol check : matchSymbols)
			{
				if (check.equals(s))
				{
					return true;
				}
			}
			
			return false;
		}
		
		// symbolCounts should have same length as matchSymbols
		// will not zero out symbolCounts beforehand
		private void fillMatchCountIn(int start, int stop, int[] symbolCounts)
		{
			if (start < 1)
			{
				throw new IllegalArgumentException("start < 1");
			}
			else if (start > stop)
			{
				throw new IllegalArgumentException("start > stop");
			}
			else if (symbolCounts == null || symbolCounts.length != matchSymbols.length)
			{
				throw new IllegalArgumentException("invalid symbolCounts");
			}
			
			for (int i = start; i < stop; i++)
			{
				Symbol currSymbol = seq.symbolAt(i);
				
				for (int j = 0; j < matchSymbols.length; j++)
				{
					if (matchSymbols[j].equals(currSymbol))
					{
						symbolCounts[j]++;
						break;
					}
				}
			}
		}
		
		private void zeroArray(int[] array)
		{
			for (int i = 0; i < array.length; i++)
			{
				array[i] = 0;
			}
		}
		
		/**
		 * Fills in symbol counts for the current window.  Returns the window size
		 * @param symbolCounts  The array to fill in with symbol counts.
		 * @return  The window size.
		 */
		protected int fillCountsForCurrentWindow(int[] symbolCounts)
		{
			int start = currentStartBase() - upstreamLength();
			int stop = currentStopBase() + downstreamLength();
			
			int baseLength = stop-start;
			
			zeroArray(symbolCounts);
			
			if (start < 1)
			{
				if (isCircular())
				{
					int realStart = seq.length() + start;
					int realStop = seq.length() + 1;
					
					fillMatchCountIn(realStart, realStop, symbolCounts);
					
					realStart = 1;
					if (stop > seq.length())
					{
						realStop = stop - seq.length();
					}
					else
					{
						realStop = stop;						
					}			
					
					fillMatchCountIn(realStart, realStop, symbolCounts);
				}
				else
				{
					int realStart = 1;
					int realStop;
					
					if (stop > seq.length()+1)
					{
						realStop = seq.length()+1;
					}
					else
					{
						realStop = stop;
					}
					
					fillMatchCountIn(realStart, realStop, symbolCounts);
					
					baseLength = realStop - realStart;
				}
			}
			else
			{
				if (isCircular())
				{
					if (stop > seq.length())
					{
						int realStart = start;
						int realStop = seq.length() + 1;
						
						fillMatchCountIn(realStart, realStop, symbolCounts);

						realStart = 1;
						realStop = stop - seq.length();
						
						fillMatchCountIn(realStart, realStop, symbolCounts);
					}
					else
					{
						fillMatchCountIn(start, stop, symbolCounts);
					}
				}
				else
				{
					int realStart = start;
					int realStop;
					
					if (stop > seq.length()+1)
					{
						realStop = seq.length()+1;
					}
					else
					{
						realStop = stop;
					}
					
					fillMatchCountIn(realStart, realStop, symbolCounts);
						
					baseLength = realStop - realStart;
				}
			}
			
			return baseLength;
		}
		
		protected void nextStep()
		{
			currentStartBase += stepSize;
			currentStopBase += stepSize;
			
			currentStep++;
		}
		
		protected int upstreamLength()
		{
			return upstreamLength;
		}
		
		protected int downstreamLength()
		{
			return downstreamLength;
		}
		
		protected int currentStartBase()
		{
			return currentStartBase;
		}
		
		protected int currentStopBase()
		{
			return currentStopBase;
		}
		
		protected boolean isCircular()
		{
			return circular;
		}

		@Override
		public boolean hasNext()
		{
			if (isCircular())
			{
				return currentStopBase <= (seq.length()+1);
			}
			else
			{
				return (currentStopBase + downstreamLength()) <= (seq.length() + 1);
			}
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("remove unsupported for ContentIterator");
		}
	}
}
