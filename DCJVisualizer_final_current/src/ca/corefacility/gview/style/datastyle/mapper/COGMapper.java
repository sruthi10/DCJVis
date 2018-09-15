package ca.corefacility.gview.style.datastyle.mapper;

import java.util.Arrays;

import org.biojava.bio.seq.Feature;

public class COGMapper implements DiscretePropertyMapper
{
	private final AnnotationMapper[] mappers;
	
	public COGMapper(String[] cogCategories)
	{		
		mappers = new AnnotationMapper[2];
		mappers[0] = new AnnotationMapper("COG", cogCategories);
		mappers[1] = new AnnotationMapper("COG_letter", cogCategories);
	}

	@Override
	public int propertyValue(Feature f)
	{
		for (AnnotationMapper m : mappers)
		{
			int value = m.propertyValue(f);
			
			if (value != DiscretePropertyMapper.INVALID)
			{
				return value;
			}
		}
		
		return DiscretePropertyMapper.INVALID;
	}

	@Override
	public int getNumberOfCategories()
	{
		return mappers[0].getNumberOfCategories();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(mappers);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		COGMapper other = (COGMapper) obj;
		if (!Arrays.equals(mappers, other.mappers))
			return false;
		return true;
	}
	
	public String[] getCategories()
	{
		return mappers[0].getCategories();
	}
}
