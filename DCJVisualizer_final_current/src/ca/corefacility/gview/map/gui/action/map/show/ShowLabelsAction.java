package ca.corefacility.gview.map.gui.action.map.show;

import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.ElementControl;

/**
 * Show labels action class.
 * 
 * @author Eric Marinier
 *
 */
public class ShowLabelsAction extends ShowItemAction 
{
	private final ElementControl control;
	
	public ShowLabelsAction(ElementControl control)
	{
		if(control == null)
			throw new IllegalArgumentException("ElementControl is null.");
		
		this.control = control;
	}
	
	@Override
	public void undo() throws CannotUndoException 
	{
		this.control.setLabelsDisplayed(false);
	}

	@Override
	public void run() 
	{
		this.control.setLabelsDisplayed(true);
	}

}
