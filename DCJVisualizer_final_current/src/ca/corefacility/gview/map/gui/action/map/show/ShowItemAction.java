package ca.corefacility.gview.map.gui.action.map.show;

import javax.swing.undo.CannotRedoException;

import ca.corefacility.gview.map.gui.action.map.MapAction;

/**
 * Show map item action class.
 * 
 * @author Eric Marinier
 *
 */
public abstract class ShowItemAction extends MapAction 
{
	@Override
	public void redo() throws CannotRedoException 
	{
		this.run();
	}
}
