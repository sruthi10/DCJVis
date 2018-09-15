package ca.corefacility.gview.map.gui.editor.communication;

import ca.corefacility.gview.map.controllers.link.LinkController;
import ca.corefacility.gview.map.controllers.link.Linkable;

/**
 * This class represents the object responsible for broadcasting link events.
 * 
 * @author Eric Marinier
 * 
 */
public class LinkEventBroadcaster implements GUIEventBroadcaster
{
	private final LinkController linkController;
	private final Linkable linkable;

	public LinkEventBroadcaster(LinkController linkController, Linkable linkable)
	{
		this.linkController = linkController;
		this.linkable = linkable;
	}

	@Override
	public boolean broadcastEvent(GUIEvent event)
	{
		boolean broadcasted = false;

		if (this.linkController.isLinkableEvent(event.getClass()) && this.linkable.getLink() != null)
		{
			this.linkController.fireEvent(new LinkEvent(this.linkable.getLink(), event));
			broadcasted = true;
		}

		return broadcasted;
	}
}
