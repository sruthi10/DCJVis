package ca.corefacility.gview.map.gui.action.map.hide;

import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.ElementControl;

/**
 * Hide labels action class.
 * 
 * @author Eric Marinier
 *
 */
public class HideLabelsAction extends HideItemAction 
{
	private final ElementControl control;
	
	/**
	 * 
	 * @param control The control object for the GView map.
	 */
	public HideLabelsAction(ElementControl control)
	{
		if(control == null)
			throw new IllegalArgumentException("ElementControl is null.");
		
		this.control = control;
	}
	
	@Override
	public void undo() throws CannotUndoException 
	{
		this.control.setLabelsDisplayed(true);
	}

	@Override
	public void run() 
	{
		this.control.setLabelsDisplayed(false);
	}

}
