package ca.corefacility.gview.style.datastyle.mapper;

import ca.corefacility.gview.map.items.FeatureItem;

public class OpacityFeatureStyleMapper implements ContinuousStyleMapper
{
	private float lower;
	private float upper;
	
	public OpacityFeatureStyleMapper()
	{
		this(0.0f, 1.0f);
	}
	
	public OpacityFeatureStyleMapper(float lower, float upper)
	{
		if (lower > upper)
		{
			throw new IllegalArgumentException("lower > upper");
		}
		else if (lower < 0.0 || upper > 1.0)
		{
			throw new IllegalArgumentException("values are out of range of 0.0 to 1.0");
		}
		
		this.lower = lower;
		this.upper = upper;
	}

	@Override
	public void mapValueToStyle(float value, FeatureItem featureItem)
	{
		if (value < 0.0 || value > 1.0)
		{
			throw new IllegalArgumentException("value is out of range of [0.0,1.0]");
		}
		
		if (featureItem == null)
		{
			throw new IllegalArgumentException("featureItem is null");
		}
		
		float opacity;
		
		if (value <= lower)
		{
			opacity = 0.0f;
		}
		else if (value >= upper)
		{
			opacity = 1.0f;
		}
		else
		{
			opacity = (value - lower) / (upper - lower);
		}
		
		featureItem.setTransparency(opacity);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(lower);
		result = prime * result + Float.floatToIntBits(upper);
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
		OpacityFeatureStyleMapper other = (OpacityFeatureStyleMapper) obj;
		if (Float.floatToIntBits(lower) != Float.floatToIntBits(other.lower))
			return false;
		if (Float.floatToIntBits(upper) != Float.floatToIntBits(other.upper))
			return false;
		return true;
	}
}
