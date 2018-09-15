package ca.corefacility.gview.map.gui.editor.panel.propertyMapper;

/**
 * This class represents a generic property style mapper list item.
 * 
 * @author Eric Marinier
 *
 */
public abstract class PropertyMapperListItem
{
	private static final String UNKNOWN_MAPPER = "UNKNOWN PROPERTY MAPPER";
	
	private String name;	//the name to be displayed by renderers, rather than the .toString()
	
	/**
	 * Default constructor.
	 */
	public PropertyMapperListItem()
	{
		this.name = UNKNOWN_MAPPER;
	}
	
	/**
	 * Sets the name of the list item. This is the name that is expected to be displayed by renderers.
	 * Not .toString().
	 * 
	 * @param name The name to set the list item to.
	 */
	public void setName(String name)
	{
		if(name != null)
		{
			this.name = name;
		}
	}
	
	/**
	 * Returns the list item name.
	 * 
	 * @return The list item name.
	 */
	public String getName()
	{
		if(name == null)
			throw new  NullPointerException("Name is null.");
		
		return this.name;
	}
}
