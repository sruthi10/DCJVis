package ca.corefacility.gview.style.datastyle.mapper;

import ca.corefacility.gview.map.items.FeatureItem;

public class PropertyStyleContinuous implements PropertyStyleMapper
{
    private ContinuousPropertyMapper propertyMapper;
    private ContinuousStyleMapper styleMapper;
    
	public PropertyStyleContinuous(ContinuousPropertyMapper propertyMapper,
			ContinuousStyleMapper styleMapper)
	{
		super();
		this.propertyMapper = propertyMapper;
		this.styleMapper = styleMapper;
	}
	
	/* (non-Javadoc)
	 * @see ca.corefacility.gview.style.datastyle.mapper.PropertyStyle#performMappingFor(ca.corefacility.gview.map.items.FeatureItem)
	 */
	@Override
	public void performMappingFor(FeatureItem item)
	{
		float value = 0.0f;
		
		if (item == null)
		{
			throw new IllegalArgumentException("item is null");
		}
		
		if (item.getFeature() == null)
		{
			throw new IllegalArgumentException("item.getFeature() is null");
		}
		
		value = propertyMapper.propertyValue(item.getFeature());
		
		if (value != ContinuousPropertyMapper.INVALID)
		{
			styleMapper.mapValueToStyle(value, item);
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
		PropertyStyleContinuous other = (PropertyStyleContinuous) obj;
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
	
	public ContinuousPropertyMapper getPropertyMapper()
	{
		return this.propertyMapper;
	}
	
	public ContinuousStyleMapper getStyleMapper()
	{
		return this.styleMapper;
	}
}
