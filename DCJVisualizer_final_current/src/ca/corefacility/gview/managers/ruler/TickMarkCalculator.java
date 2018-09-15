package ca.corefacility.gview.managers.ruler;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

// handles calculation of tick mark positions/shapes
public class TickMarkCalculator
{		
	private int sequenceLength;
	private double backboneLength; // the backbone length perceived based upon zoom level
	
	private float majorLengthProportion = 1.0f; // length of major mark as a proportion of slot length
	private float minorLengthProportion = 0.3f; // the length proportion of minor tick marks to major tick marks
	
	private int majorMarkNumber = 5; // defines that every "majorMarkNumber"th mark is a major tick mark
	
	private double desiredLengthPerMark = 50; // length on screen desired per mark
	private float tickDensity;
	
	private double approxNumberOfMarks;
	private double approxBasesPerMark;
	private int properBasesPerMark;
	private int chosenUnitsIndex;
	
	// used to match a certain number of bases with corresponding units
	private static final int[] BASE_UNITS = {1, 1000, 1000000, 1000000000};
	private static final String[] UNIT = {"bp", "kbp", "mbp", "gbp"};
		
	private static final DecimalFormat LABEL_FORMATTER = new DecimalFormat("0.#");
	
	/**
	 * Creates a new tick mark calculator, used to calculate positions/lengths of tick marks.
	 * 
	 * @param sequenceLength  The length of the sequence.
	 * @param tickDensity  The density of the tick marks to create (from 0.0 to 1.0).
	 */
	public TickMarkCalculator(int sequenceLength, float tickDensity, double desiredLengthPerMark,
			float majorLengthProportion, float minorLengthProportion)
	{
		this.sequenceLength = sequenceLength;
		
		if (tickDensity < 0.0)
		{
			this.tickDensity = 0.0f;
		}
		else if (tickDensity > 1.0)
		{
			this.tickDensity = 1.0f;
		}
		else
		{
			this.tickDensity = tickDensity;
		}
		
		this.desiredLengthPerMark = desiredLengthPerMark;
		
		this.majorLengthProportion = majorLengthProportion;
		this.minorLengthProportion = minorLengthProportion;
		
		changeBackboneLength(1.0);
	}
	
	public float getMajorLengthProportion()
	{
		return majorLengthProportion;
	}

	public void setMajorLengthProportion(float majorLengthProportion)
	{
		this.majorLengthProportion = majorLengthProportion;
	}

	public float getMinorLengthProportion()
	{
		return minorLengthProportion;
	}

	public void setMinorLengthProportion(float minorLengthProportion)
	{
		this.minorLengthProportion = minorLengthProportion;
	}

	public float getTickDensity()
	{
		return tickDensity;
	}

	public void setTickDensity(float tickDensity)
	{
		this.tickDensity = tickDensity;
		this.changeBackboneLength(this.backboneLength);
	}

	/**
	 * Changes the backbone length we use to the passed value.
	 * @param backboneLength  The current perceived length of the backbone.
	 */
	public void changeBackboneLength(double backboneLength)
	{
		if (backboneLength <= 0)
		{
			throw new IllegalArgumentException("backboneLength is not positive");
		}
		
		this.backboneLength = backboneLength;
		
		approxNumberOfMarks = (backboneLength/desiredLengthPerMark)*tickDensity;
		
		// if we have fractional approximate number of marks, then we shouldn't have any tick marks
		if (approxNumberOfMarks >= 1)
		{
			approxBasesPerMark = (double)sequenceLength/approxNumberOfMarks;
			properBasesPerMark = getProperBasesPerMark(approxBasesPerMark);
			
			chosenUnitsIndex = selectBaseUnits(properBasesPerMark*majorMarkNumber);
		}
		else
		{
			approxBasesPerMark = 0;
			properBasesPerMark = 0;
			chosenUnitsIndex = 0;
		}
	}
	
	/**
	 * Creates tick mark objects for each tick mark to create in between the two passed bases.
	 *   Only draws marks that lie within [initialBase, finalBase)
	 * 
	 * @param initialBase  The initial starting base in the range.
	 * @param finalBase  The final base in the range.
	 * 
	 * @return A list of TickMark objects defining the tick marks to build around the sequence.
	 */
	public List<TickMark> createMarks(double initialBase, double finalBase) // will section ever overlap 0 base?
	{
		// calculate these from section id
		double initialBaseRange = initialBase;
		double finalBaseRange = finalBase;
		
		List<TickMark> markList = new LinkedList<TickMark>();
		
		if (properBasesPerMark > 0)
		{
			// determine closest location of tick mark within [initial,final]
			int currentTick = closestMarkIn(initialBaseRange, finalBaseRange);
			int currentBase = currentTick*properBasesPerMark;
		
			while (currentBase < finalBaseRange)
			{
				String label = (LABEL_FORMATTER.format((double)currentBase/BASE_UNITS[chosenUnitsIndex])) + " " + UNIT[chosenUnitsIndex];
				
				if ((currentTick%majorMarkNumber) == 0) // major tick
				{
					markList.add(new TickMark(currentBase, majorLengthProportion, label, TickMark.Type.LONG));
				}
				else // minor tick
				{
					markList.add(new TickMark(currentBase, minorLengthProportion, label, TickMark.Type.SHORT));
				}
			
				currentBase += properBasesPerMark;
				currentTick++;
			}
		}
		
		return markList;
	}
	
	/**
	 * Determine the closest mark within the passed range of bases.
	 * @param initalBase
	 * @param finalBase
	 * @return  The closest tick mark number within the range.
	 */
	private int closestMarkIn(double initialBase, double finalBase)
	{
		return (int)Math.ceil(initialBase / properBasesPerMark);
	}
	
	// obtains a "proper" (evenly rounded) bases per tick mark
	// TODO problem with code in that doesn't work if approxBases is to large, define a largest
	//  value to compare to sometime
	private int getProperBasesPerMark(double approxBasesPerMark)
	{
		// defines the proper base units tick marks should represent
		final int[] PROPER_BASE_INTERVAL = {1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000,
											10000, 20000, 50000, 100000, 200000, 500000, 1000000,
											2000000, 5000000, 10000000, 20000000, 50000000, 100000000,
											200000000, 500000000, 1000000000};
		
		int properBasesPerMark = -1;
		
		assert (approxBasesPerMark >= 0);
		if (approxBasesPerMark >= 0)
		{
			// sets properBasesPerMark to closest value in PROPER_BASE_INTERVAL larger than approxBases
			for (int i = 0; (i < PROPER_BASE_INTERVAL.length) && (properBasesPerMark == -1); i++)
			{
				if (approxBasesPerMark <= PROPER_BASE_INTERVAL[i])
				{
					properBasesPerMark = PROPER_BASE_INTERVAL[i];
				}
			}
		}
		
		return properBasesPerMark;
	}
	
	
	/**
	 * Selects the proper units to use for tick mark text.
	 * 
	 * @param basesPerMajorMark  The bases per every major tick mark, used to select proper units.
	 * 
	 * @return  An index into BASE_UNITS and UNITS for the proper units.
	 */
	private int selectBaseUnits(int basesPerMajorMark)
	{
		int chosenIndex = -1;
		
		if (basesPerMajorMark < 0)
		{
			throw new IllegalArgumentException("basesPerMajorMark=" + basesPerMajorMark + " is negative");
		}
		
		// count down
		for (int i = (UNIT.length-1); (i >= 0) && (chosenIndex == -1); i--)
		{
			if (basesPerMajorMark >= BASE_UNITS[i])
			{
				chosenIndex = i;
			}
		}
		
		return chosenIndex;
	}
	
	/**
	 * Determines the longest (in terms of characters) tick mark label possible for the given sequence length.
	 * @param sequenceLength  The sequence length to check against.
	 * @return  The longest tick mark label possible.
	 */
	public static String getLongestTickLabel(int sequenceLength)
	{
		return sequenceLength + " mbp";
	}
}
