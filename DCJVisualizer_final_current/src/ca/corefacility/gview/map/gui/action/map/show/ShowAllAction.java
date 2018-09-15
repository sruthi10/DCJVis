package ca.corefacility.gview.map.gui.action.map.show;


import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.ElementControl;

/**
 * Show all action.
 * 
 * Shows all the elements under the element control.
 * 
 * @author Eric Marinier
 *
 */
public class ShowAllAction extends ShowItemAction 
{
	private final ElementControl control;
	
	/**
	 * 
	 * @param control The control object for the GView map.
	 */
	public ShowAllAction(ElementControl control)
	{
		if(control == null)
			throw new IllegalArgumentException("ElementControl is null.");
		
		this.control = control;
	}
	
	@Override
	public void undo() throws CannotUndoException 
	{
		this.control.setAllDisplayed(false);
	}

	@Override
	public void run() 
	{
		this.control.setAllDisplayed(true);
	}

}
