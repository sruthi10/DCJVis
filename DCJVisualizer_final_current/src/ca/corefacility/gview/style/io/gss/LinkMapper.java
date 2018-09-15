package ca.corefacility.gview.style.io.gss;

import java.util.HashMap;

import ca.corefacility.gview.map.controllers.link.Link;

/**
 * This class is responsible for mapping identifiers to links and links to
 * identifiers. These two mappings act independently.
 * 
 * This class was designed to be used in I/O operations.
 * 
 * @author Eric Marinier
 * 
 */
public class LinkMapper
{
	private final HashMap<String, Link> IDToLinkMap = new HashMap<String, Link>();
	private final HashMap<Link, String> LinkToIDMap = new HashMap<Link, String>();

	private int ID = 0;

	/**
	 * This always returns the same link for the same identifier.
	 * 
	 * @param identifier
	 * @return A link that corresponds to the identifier.
	 */
	public Link getLink(String identifier)
	{
		Link link;

		// We already have the link object for the String:
		if (this.IDToLinkMap.containsKey(identifier))
		{
			link = this.IDToLinkMap.get(identifier);
		}
		// We don't already have the link object:
		else
		{
			link = new Link();

			this.IDToLinkMap.put(identifier, link);
		}

		return link;
	}

	/**
	 * This always returns the same identifier for the same link.
	 * 
	 * @param link
	 * @return An identifier that corresponds to the link.
	 */
	public String getIdentifier(Link link)
	{
		String identifier;

		// Identifier already exists:
		if (this.LinkToIDMap.containsKey(link))
		{
			identifier = this.LinkToIDMap.get(link);
		}
		// Identifier doesn't already exist:
		else
		{
			identifier = this.getNextIdentifier();

			this.LinkToIDMap.put(link, identifier);
		}

		return identifier;
	}

	/**
	 * 
	 * @return The next new identifier. Will be unique only for this LinkMapper
	 *         object.
	 */
	private String getNextIdentifier()
	{
		this.ID++;

		return (this.ID + "");
	}
}
