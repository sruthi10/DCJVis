package ca.corefacility.gview.data.readers.cgview;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Used to store/tie together a name and attributes of some element in the file.
 *
 * @author Aaron Petkau
 *
 */
class ElementDetails
{
	public String name;
	public Attributes attributes;

	public ElementDetails(String name, Attributes atts)
	{
		this.name = name;
		this.attributes = new AttributesImpl(atts);
	}
}
