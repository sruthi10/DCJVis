package ca.corefacility.gview.map.gui.editor;

import ca.corefacility.gview.map.gui.editor.panel.propertyMapper.PropertyMapperListItem;

/**
 * This class represents a unknown mapper list item. This is intended to be a fall back if in the future a new
 * implementation of property style mapper is introduced.
 * 
 * @author Eric Marinier
 *
 */
public class UnknownMapperListItem extends PropertyMapperListItem
{
	private static final String UNKNOWN = "Unknown";
	
	/**
	 * Default constructor.
	 */
	public UnknownMapperListItem()
	{
		super.setName(UNKNOWN);
	}
}
