package ca.corefacility.gview.map.gui.action.map.show;

import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.ElementControl;

/**
 * Show ruler action class.
 * 
 * @author Eric Marinier
 *
 */
public class ShowRulerAction extends ShowItemAction 
{
	private final ElementControl control;
	
	/**
	 * 
	 * @param control The control object for the GView map.
	 */
	public ShowRulerAction(ElementControl control)
	{
		if(control == null)
			throw new IllegalArgumentException("ElementControl is null.");
		
		this.control = control;
	}

	@Override
	public void undo() throws CannotUndoException 
	{
		this.control.setRulerDisplayed(false);
	}

	@Override
	public void run() 
	{
		this.control.setRulerDisplayed(true);
	}

}
