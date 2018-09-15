package ca.corefacility.gview.map.gui.editor.panel.propertyMapper;

import ca.corefacility.gview.style.datastyle.mapper.ContinuousPropertyMapper;
import ca.corefacility.gview.style.datastyle.mapper.ContinuousStyleMapper;

/**
 * This class represents a continuous property style mapper list item.
 * 
 * @author Eric Marinier
 *
 */
public class ContinuousMapperListItem extends PropertyMapperListItem
{
	private static final String UNKNOWN_MAPPER = "UNKNOWN PROPERTY MAPPER";
	private static final String UNKNOWN = "UNKNOWN";	
	
	/**
	 * Default constructor.
	 */
	public ContinuousMapperListItem()
	{
		super.setName(UNKNOWN_MAPPER);
	}
	
	/**
	 * 
	 * @param propertyMapper The continuous property mapper.
	 * @param styleMapper The associated continuous style mapper.
	 */
	public ContinuousMapperListItem(ContinuousPropertyMapper propertyMapper, ContinuousStyleMapper styleMapper)
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
	private String getPropertyMapperName(ContinuousPropertyMapper propertyMapper)
	{
		String result = UNKNOWN;
		
		if(propertyMapper != null)
		{
			result = propertyMapper.toString();
		}

		return result;
	}
	
	/**
	 * Returns the style mapper name. This is only a fall back way of naming; the name should be determined 
	 * by extending the appropriate class and naming itself.
	 * 
	 * @param styleMapper The style mapper to pull the name from.
	 * @return The name of the style mapper.
	 */
	private String getStyleMapperName(ContinuousStyleMapper styleMapper)
	{
		String result = UNKNOWN;
		
		if(styleMapper != null)
		{
			result = styleMapper.toString();
		}

		return result;
	}
}
