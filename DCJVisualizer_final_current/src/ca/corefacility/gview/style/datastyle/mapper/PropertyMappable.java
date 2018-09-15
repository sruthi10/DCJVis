package ca.corefacility.gview.style.datastyle.mapper;

/**
 * This allows (data) styles with property mappers to interface.
 * 
 * @author Eric Marinier
 */
public interface PropertyMappable
{
	/**
	 * 
	 * @return The property style mapper.
	 */
	public PropertyStyleMapper getPropertyStyleMapper();
	
	/**
	 * Sets the property style mapper.
	 * 
	 * @param propertyStyleMapper The property style mapper to set.
	 */
	public void setPropertyStyleMapper(PropertyStyleMapper propertyStyleMapper);
}
