package ca.corefacility.gview.map.controllers.selection;

import ca.corefacility.gview.map.gui.editor.communication.SelectionEvent;

/**
 * The implementor of this interface will listen for selection events.
 * 
 * @author Eric Marinier
 * 
 */
public interface SelectionListener
{
	public void selectionEvent(SelectionEvent event);
}
