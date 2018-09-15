package ca.corefacility.gview.map.gui.editor.communication;

import ca.corefacility.gview.map.controllers.link.Link;

/**
 * This class is responsible for facilitating notifications of linked objects.
 * 
 * This facilitation is useful for allowing GUI components to remain
 * synchronized and consistent.
 * 
 * @author Eric Marinier
 * 
 */
public class LinkEvent extends CommunicationEvent
{
	private final Link link;

	public LinkEvent(Link link, GUIEvent event)
	{
		super(event);

		this.link = link;
	}

	/**
	 * 
	 * @return The link.
	 */
	public Link getLink()
	{
		return this.link;
	}
}
