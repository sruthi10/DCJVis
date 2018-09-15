package ca.corefacility.gview.layout.sequence;


import org.biojava.bio.symbol.Location;
import org.biojava.bio.symbol.RangeLocation;

import ca.corefacility.gview.data.GenomeData;

/**
 * Allows conversions from constant locations (such as full) to actual locations.
 * @author Aaron Petkau
 *
 */
public class LocationConverter
{
	private GenomeData genomeData;
	
	public LocationConverter(GenomeData genomeData)
	{
		if (genomeData == null)
		{
			throw new IllegalArgumentException("genomeData is null");
		}
		
		this.genomeData = genomeData;
	}
	
	protected LocationConverter()
	{}
	
	// should I trigger an event on setting genome data?
	public void setGenomeData(GenomeData genomeData)
	{
		if (genomeData != null)
		{
			this.genomeData = genomeData;
		}
	}
	
	public int getSequenceLength()
	{
		return genomeData.getSequenceLength();
	}
	
	/**
	 * Converts a constant location value (such as full) to a real location.
	 * @param location  The location to convert.
	 * @return  The actual location on the current sequence, or the original location if passed location is not one
	 * 		of the constant locations.  Null if passed location is null.
	 */
	public Location convertLocation(Location location)
	{
		if (location == null)
		{
			return null;
		}
		else
		{
		    boolean isEqual = false;
		    
		    // try-catch here since Location.full.equals(CircularLocation) throws an exception, but I want it to be considered false (not equal)
		    try
		    {
		        isEqual = location.equals(Location.full);
            }
            catch (UnsupportedOperationException e)
            {
                isEqual = false;
            }
            
		    if (isEqual)
		    {
		        return new RangeLocation(genomeData.getInitialBase(), genomeData.getSequenceLength() - genomeData.getInitialBase());
		    }
		}
		
		return location;
	}
	
	/**
	 * Determines if the passed base is on the sequence.
	 * @param base  The base pair value to check.
	 * @return  True if the passed base pair value is on the current sequence, false otherwise.
	 */
	public boolean onSequence(int base)
	{
		return (base >= genomeData.getInitialBase() && base <= genomeData.getSequenceLength());
	}
}
