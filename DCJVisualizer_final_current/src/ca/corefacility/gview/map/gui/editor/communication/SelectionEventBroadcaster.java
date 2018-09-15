package ca.corefacility.gview.map.gui.editor.communication;

import ca.corefacility.gview.map.controllers.selection.SelectionController;

/**
 * This class represents the object responsible for broadcasting selection
 * events.
 * 
 * @author Eric Marinier
 * 
 */
public class SelectionEventBroadcaster implements GUIEventBroadcaster
{
	private final SelectionController selectionController;

	/**
	 * 
	 * @param selectionController
	 *            The selection controller.
	 */
	public SelectionEventBroadcaster(SelectionController selectionController)
	{
		this.selectionController = selectionController;
	}

	@Override
	public boolean broadcastEvent(GUIEvent event)
	{
		boolean broadcasted = false;

		if (this.selectionController.isSelectionConsistent() && this.selectionController.getSelection().size() > 1)
		{
			this.selectionController.fireEvent(new SelectionEvent(event));
			broadcasted = true;
		}

		return broadcasted;
	}
}
