package ca.corefacility.gview.map.gui.action.map.hide;

import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.ElementControl;

/**
 * Hide legend action class.
 * 
 * @author Eric Marinier
 *
 */
public class HideLegendAction extends HideItemAction 
{
	private final ElementControl control;
	
	/**
	 * 
	 * @param control The control object for the GView map.
	 */
	public HideLegendAction(ElementControl control)
	{
		if(control == null)
			throw new IllegalArgumentException("ElementControl is null.");
		
		this.control = control;
	}

	@Override
	public void undo() throws CannotUndoException 
	{
		this.control.setLegendDisplayed(true);
	}

	@Override
	public void run() 
	{
		this.control.setLegendDisplayed(false);
	}

}
