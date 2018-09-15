package ca.corefacility.gview.style.datastyle.mapper;

import org.biojava.bio.seq.Feature;

public interface DiscretePropertyMapper
{
	public static final int INVALID = -1;
	
	/**
	 * Maps a specific property in a feature to an integer corresponding to the category (starting at 0).
	 * @param f  The feature to map a property to.
	 * @return  An integer representing the category.
	 */
	public int propertyValue(Feature f);
	
	public int getNumberOfCategories();
	public String[] getCategories();
}
