package ca.corefacility.gview.style.io.gss;

import ca.corefacility.gview.map.controllers.link.Link;

/**
 * This class assists in the encoding and decoding of links.
 * 
 * @author Eric Marinier
 * 
 */
public class LinkHandler
{
	/**
	 * Encodes the link.
	 * 
	 * @param link
	 * @param mapper
	 * @return The encoding of the link.
	 */
	public static String encode(Link link, LinkMapper mapper)
	{
		return "\"" + mapper.getIdentifier(link) + "\"";
	}

	/**
	 * Decodes to a link.
	 * 
	 * @param identifier
	 * @param mapper
	 * @return The decoding of the link.
	 */
	public static Link decode(String identifier, LinkMapper mapper)
	{
		return mapper.getLink(identifier);
	}
}
