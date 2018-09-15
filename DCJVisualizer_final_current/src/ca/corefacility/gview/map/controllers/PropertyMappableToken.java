package ca.corefacility.gview.map.controllers;

import ca.corefacility.gview.style.datastyle.mapper.PropertyMappable;

/**
* This class is responsible for providing access to it's associated style to ONLY 
* the controllers, which is achieved by using "protected final" method specifiers and 
* by having the controllers and tokens in the same package.
* 
* @author Eric Marinier
*
*/
public final class PropertyMappableToken
{
	private final PropertyMappable propertyMappable;
	
	/**
	 * 
	 * @param propertyMappable The style to wrap the token around.
	 */
	public PropertyMappableToken(PropertyMappable propertyMappable)
	{
		this.propertyMappable = propertyMappable;
	}
	
	/**
	 * 
	 * @return The associated style.
	 */
	protected final PropertyMappable getPropertyMappable()
	{
		return this.propertyMappable;
	}
}
