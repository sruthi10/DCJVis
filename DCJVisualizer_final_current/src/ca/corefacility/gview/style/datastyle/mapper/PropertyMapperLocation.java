package ca.corefacility.gview.style.datastyle.mapper;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.symbol.Location;

public class PropertyMapperLocation implements ContinuousPropertyMapper
{
	private int min;
	private int max;
	
	public PropertyMapperLocation(int min, int max)
	{
		if (min < 0)
		{
			throw new IllegalArgumentException("min < 0");
		}
		else if (min > max)
		{
			throw new IllegalArgumentException("min > max");
		}
		
		this.min = min;
		this.max = max;
	}

	@Override
	public float propertyValue(Feature f)
	{
		if (f == null)
		{
			throw new IllegalArgumentException("feature is null");
		}
		
		Location l = f.getLocation();
		int locationMin = l.getMin();
		
		float value = INVALID;
		
		if (locationMin < min)
		{
			value = 0.0f;
		}
		else if (locationMin > max)
		{
			value = 1.0f;
		}
		else
		{
			value = (float)(locationMin - min)/(float)(max - min);
		}
			
		return value;
	}

}
