package ca.corefacility.gview.style.datastyle.mapper;

import java.util.Arrays;

import org.biojava.bio.Annotation;
import org.biojava.bio.seq.Feature;

public class AnnotationMapper implements DiscretePropertyMapper
{
	private String annotationKey;
	private String[] annotationValues;
	
	public AnnotationMapper(String key, String[] values)
	{
		if (key == null)
		{
			throw new IllegalArgumentException("key is null");
		}
		
		if (values == null)
		{
			throw new IllegalArgumentException("values are null");
		}
		
		for (String value : values)
		{
			if (value == null)
			{
				throw new IllegalArgumentException("one element in values is null");
			}
		}
		
		this.annotationKey = key;
		this.annotationValues = values;
	}

	@Override
	public int propertyValue(Feature f)
	{
		if (f == null)
		{
			throw new IllegalArgumentException("feature f is null");
		}
		
		int category = INVALID;
		
		Annotation a = f.getAnnotation();
		
		if (a != null && a.containsProperty(annotationKey))
		{
			Object valueObj = a.getProperty(annotationKey);
			
			if (valueObj instanceof String)
			{
				String value = (String)valueObj;
				
				for (int currCategory = 0; currCategory < annotationValues.length &&
					category == INVALID; currCategory++)
				{
					String currValue = annotationValues[currCategory];
					if (currValue != null && currValue.equals(value))
					{
						category = currCategory;
					}					
				}
			}
		}
		
		return category;
	}
	
	@Override
	public int getNumberOfCategories()
	{
		return annotationValues.length;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((annotationKey == null) ? 0 : annotationKey.hashCode());
		result = prime * result + Arrays.hashCode(annotationValues);
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
		AnnotationMapper other = (AnnotationMapper) obj;
		if (annotationKey == null)
		{
			if (other.annotationKey != null)
				return false;
		} else if (!annotationKey.equals(other.annotationKey))
			return false;
		if (!Arrays.equals(annotationValues, other.annotationValues))
			return false;
		return true;
	}
	
	public String[] getCategories()
	{
		return this.annotationValues;
	}
}
