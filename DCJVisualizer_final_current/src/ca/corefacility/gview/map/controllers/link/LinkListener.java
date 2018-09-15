package ca.corefacility.gview.map.controllers.link;

import ca.corefacility.gview.map.gui.editor.communication.LinkEvent;


/**
 * This interface is appropriate for classes who wish to listen for link events.
 * 
 * @author Eric Marinier
 *
 */
public interface LinkListener
{
	public void linkEvent(LinkEvent event);
}
