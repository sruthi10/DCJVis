package ca.corefacility.gview.style.datastyle.mapper;

import ca.corefacility.gview.map.items.FeatureItem;

public class PropertyStyleDiscrete implements PropertyStyleMapper
{
	private DiscretePropertyMapper propertyMapper;
	private DiscreteStyleMapper styleMapper;

	public PropertyStyleDiscrete(DiscretePropertyMapper propertyMapper,
			DiscreteStyleMapper styleMapper)
	{
		if (propertyMapper == null)
		{
			throw new IllegalArgumentException("propertyMapper is null");
		}
		
		if (styleMapper == null)
		{
			throw new IllegalArgumentException("styleMapper is null");
		}
		
		if (propertyMapper.getNumberOfCategories() != styleMapper.getNumberOfCategories())
		{
			throw new IllegalArgumentException("propertyMapper has " + propertyMapper.getNumberOfCategories() +
					" categories and styleMapper has " + styleMapper.getNumberOfCategories() + " categories." +
					"Does not match.");
		}
		
		this.propertyMapper = propertyMapper;
		this.styleMapper = styleMapper;
	}

	@Override
	public void performMappingFor(FeatureItem item)
	{
		if (item == null)
		{
			throw new IllegalArgumentException("item is null");
		}
		
		if (item.getFeature() != null)
		{
			int value = propertyMapper.propertyValue(item.getFeature());
			
			if (value != DiscretePropertyMapper.INVALID && styleMapper.valueInBounds(value))
			{
				styleMapper.mapValueToStyle(value, item);
			}
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((propertyMapper == null) ? 0 : propertyMapper.hashCode());
		result = prime * result
				+ ((styleMapper == null) ? 0 : styleMapper.hashCode());
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
		PropertyStyleDiscrete other = (PropertyStyleDiscrete) obj;
		if (propertyMapper == null)
		{
			if (other.propertyMapper != null)
				return false;
		} else if (!propertyMapper.equals(other.propertyMapper))
			return false;
		if (styleMapper == null)
		{
			if (other.styleMapper != null)
				return false;
		} else if (!styleMapper.equals(other.styleMapper))
			return false;
		return true;
	}
	
	public DiscretePropertyMapper getPropertyMapper()
	{
		return this.propertyMapper;
	}
	
	public DiscreteStyleMapper getStyleMapper()
	{
		return this.styleMapper;
	}
}
