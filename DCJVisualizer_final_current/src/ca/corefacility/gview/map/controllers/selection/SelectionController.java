package ca.corefacility.gview.map.controllers.selection;

import java.util.List;

import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.controllers.link.Linkable;
import ca.corefacility.gview.map.gui.editor.communication.SelectionEvent;

/**
 * This class is responsible for handling selection interactions.
 * 
 * @author Eric Marinier
 * 
 */
public class SelectionController
{
	private Selectable selectable;

	/**
	 * Sets the selectable object.
	 * 
	 * @param selectable
	 */
	public void setSelectable(Selectable selectable)
	{
		this.selectable = selectable;
	}

	/**
	 * 
	 * @return The selection.
	 */
	public List<SelectionListener> getSelection()
	{
		// Safety:
		if (this.selectable == null)
		{
			return null;
		}
		else
		{
			return this.selectable.getSelection();
		}
	}

	/**
	 * 
	 * @return Whether or not the current selection is consistent.
	 */
	public boolean isSelectionConsistent()
	{
		// Safety:
		if (this.selectable == null)
		{
			return false;
		}

		boolean consistent = true;

		List<SelectionListener> selection = getSelection();

		for (int i = 0; i < selection.size() - 1 && consistent; i++)
		{
			if (selection.get(i).getClass() != selection.get(i + 1).getClass())
			{
				consistent = false;
			}
		}

		return consistent;
	}

	/**
	 * 
	 * @return True: if the selection contains one or no links. False: if the
	 *         selection contains more than one link.
	 */
	public boolean isLinkConsistent()
	{
		// Safety:
		if (this.selectable == null)
		{
			return false;
		}

		boolean consistent = true;
		Link link = null;

		List<SelectionListener> selection = getSelection();

		for (int i = 0; i < selection.size() && consistent; i++)
		{
			Object current = selection.get(i);

			if (current instanceof Linkable)
			{
				if (link == null)
				{
					link = ((Linkable) current).getLink();
				}
				else if (!Link.isEqual(link, ((Linkable) current).getLink()))
				{
					consistent = false;
				}
			}
		}

		return consistent;
	}

	/**
	 * Fires a selection event to all the selected objects.
	 * 
	 * @param event
	 */
	public void fireEvent(SelectionEvent event)
	{
		// Safety:
		if (this.selectable == null)
		{
			return;
		}

		for (SelectionListener listener : getSelection())
		{
			listener.selectionEvent(event);
		}
	}
}
