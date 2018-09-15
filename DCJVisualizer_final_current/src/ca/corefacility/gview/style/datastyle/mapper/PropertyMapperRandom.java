package ca.corefacility.gview.style.datastyle.mapper;

import org.biojava.bio.seq.Feature;

public class PropertyMapperRandom implements ContinuousPropertyMapper
{

	@Override
	public float propertyValue(Feature f)
	{
		return (float)Math.random();
	}
}
