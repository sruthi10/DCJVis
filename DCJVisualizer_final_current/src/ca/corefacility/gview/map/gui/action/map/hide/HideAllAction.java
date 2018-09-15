package ca.corefacility.gview.map.gui.action.map.hide;


import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.ElementControl;

/**
 * Hide all action class.
 * 
 * Hides all of the items under the element control.
 * 
 * @author Eric Marinier
 *
 */
public class HideAllAction extends HideItemAction 
{
	private final ElementControl control;
	
	/**
	 * 
	 * @param control The control object for the GView map.
	 */
	public HideAllAction(ElementControl control)
	{
		if(control == null)
			throw new IllegalArgumentException("ElementControl is null.");
		
		this.control = control;
	}
	
	@Override
	public void undo() throws CannotUndoException 
	{
		this.control.setAllDisplayed(true);
	}

	@Override
	public void run() 
	{
		this.control.setAllDisplayed(false);
	}

}
