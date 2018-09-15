package ca.corefacility.gview.map.gui;

import java.net.URI;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.map.controllers.StyleController;
import ca.corefacility.gview.map.gui.editor.StyleNameManager;

/**
 * This class represents an encapsulation of style information, which may be applied to 
 * genome data through the GUIController to create a customized genome visualization.
 * 
 * All modifications to the style should be done through the style's associated StyleController.
 * 
 * @author Eric Marinier
 *
 */
public final class Style
{
	private final StyleController styleController;
	
	private URI uri;
	private String name;
	
	private final boolean XML;
	private final GenomeData genomeData;
	
	/**
	 * 
	 * @param gViewMap The gViewMap associated with the style. Will not be exposed.
	 * @param name The name of the style.
	 * @param uri The URI associated with the style.
	 */
	public Style(GViewMap gViewMap, String name, URI uri)
	{		
		if(name == null)
		{
			this.name = StyleNameManager.getNewStyleName();
		}
		else
		{
			this.name = name;
		}
		
		this.uri = uri;
		this.genomeData = gViewMap.getGenomeData();
		
		this.XML = gViewMap.getGenomeData().isXML();
		
		this.styleController = new StyleController(this, gViewMap);
	}
	
	/**
	 * 
	 * @param gViewMap The gViewMap associated with the style. Will not be exposed.
	 */
	public Style(GViewMap gViewMap)
	{		
		this(gViewMap, null, null);
	}
	
	/**
	 * 
	 * @param gViewMap The gViewMap associated with the style. Will not be exposed.
	 * @param file The URI associated with the style.
	 */
	public Style(GViewMap gViewMap, URI file)
	{
		this(gViewMap, null, file);
	}
	
	/**
	 * 
	 * @param gViewMap The gViewMap associated with the style. Will not be exposed.
	 * @param name The name of the style.
	 */
	public Style(GViewMap gViewMap, String name)
	{
		this(gViewMap, name, null);
	}
	
	/**
	 * 
	 * @return The name of the style.
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Sets the name of the style.
	 * 
	 * @param name 
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * 
	 * @return The StyleController associated with the Style. This can be used to modify
	 * the various styling attributes associated with the style.
	 */
	public StyleController getStyleController()
	{
		return this.styleController;
	}
	
	/**
	 * 
	 * @return The URI associated with the style.
	 */
	public URI getURI()
	{
		return this.uri;
	}
	
	/**
	 * 
	 * @return The genome data associated with the style.
	 */
	public GenomeData getGenomeData()
	{
		return this.genomeData;
	}
	
	@Override
	/**
	 * They should ONLY be equal if they are the same object. Logic depends on this.
	 */
	public final boolean equals(Object other)
	{
		return (this == other);
	}
	
	/**
	 * 
	 * @return Whether or not this style is an XML based style.
	 */
	public boolean isXML()
	{
		return this.XML;
	}
}
