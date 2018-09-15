package ca.corefacility.gview.map.gui.action.map.show;

import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.ElementControl;

/**
 * Show legend action class.
 * 
 * @author Eric Marinier
 *
 */
public class ShowLegendAction extends ShowItemAction 
{
	private final ElementControl control;
	
	/**
	 * 
	 * @param control The control object for the GView map.
	 */
	public ShowLegendAction(ElementControl control)
	{
		if(control == null)
			throw new IllegalArgumentException("ElementControl is null.");
		
		this.control = control;
	}

	@Override
	public void undo() throws CannotUndoException 
	{
		this.control.setLegendDisplayed(false);
	}

	@Override
	public void run() 
	{
		this.control.setLegendDisplayed(true);
	}
}
