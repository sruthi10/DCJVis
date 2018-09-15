package ca.corefacility.gview.map.gui.editor.panel.propertyMapper;

import ca.corefacility.gview.style.datastyle.mapper.DiscretePropertyMapper;
import ca.corefacility.gview.style.datastyle.mapper.DiscreteStyleMapper;

/**
 * This class represents a discrete property style mapper list item.
 * 
 * @author Eric Marinier
 *
 */
public class DiscreteMapperListItem extends PropertyMapperListItem
{	
	private static final String UNKNOWN_MAPPER = "UNKNOWN PROPERTY MAPPER";
	private static final String UNKNOWN = "UNKNOWN";
	
	/**
	 * Default constructor.
	 */
	public DiscreteMapperListItem()
	{
		super.setName(UNKNOWN_MAPPER);
	}
	
	/**
	 * 
	 * @param propertyMapper The discrete property mapper.
	 * @param styleMapper The associated discrete style mapper.
	 */
	public DiscreteMapperListItem(DiscretePropertyMapper propertyMapper, DiscreteStyleMapper styleMapper)
	{
		super.setName(this.getPropertyMapperName(propertyMapper) + " -> " + this.getStyleMapperName(styleMapper));
	}
	
	/**
	 * Returns the property mapper name. This is only a fall back way of naming; the name should be determined 
	 * by extending the appropriate class and naming itself.
	 * 
	 * @param propertyMapper The property mapper to pull the name from.
	 * @return The name of the property mapper.
	 */
	private String getPropertyMapperName(DiscretePropertyMapper propertyMapper)
	{
		String result = UNKNOWN;

		return result;
	}
	
	/**
	 * Returns the style mapper name. This is only a fall back way of naming; the name should be determined 
	 * by extending the appropriate class and naming itself.
	 * 
	 * @param styleMapper The style mapper to pull the name from.
	 * @return The name of the style mapper.
	 */
	private String getStyleMapperName(DiscreteStyleMapper styleMapper)
	{
		String result = UNKNOWN;

		return result;
	}
}
