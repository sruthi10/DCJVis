package ca.corefacility.gview.style.datastyle.mapper;

import org.biojava.bio.seq.Feature;

public interface ContinuousPropertyMapper
{
	public float INVALID = -1.0f;
	
	/**
	 * Used to map some specific property of a Feature to a float value in the range of 0.0 to 1.0
	 * @param f  The feature to extract the property for.
	 * @return  A float value in the range of 0.0 to 1.0, or INVALID if the feature property maps to no value.
	 */
	public float propertyValue(Feature f);
}
