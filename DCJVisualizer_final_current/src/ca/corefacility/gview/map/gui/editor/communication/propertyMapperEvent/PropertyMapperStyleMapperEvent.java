package ca.corefacility.gview.map.gui.editor.communication.propertyMapperEvent;

import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleMapper;

/**
 * An action for modifying the property mapper.
 * 
 * @author Eric Marinier
 * 
 */
public class PropertyMapperStyleMapperEvent extends PropertyMapperEvent
{
	private final PropertyStyleMapper mapper;

	/**
	 * 
	 * @param mapper
	 *            The property mapper.
	 */
	public PropertyMapperStyleMapperEvent(PropertyStyleMapper mapper)
	{
		this.mapper = mapper;
	}

	@Override
	public PropertyStyleMapper getData()
	{
		return this.mapper;
	}
}
