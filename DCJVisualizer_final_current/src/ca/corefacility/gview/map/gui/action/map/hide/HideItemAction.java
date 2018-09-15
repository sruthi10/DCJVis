package ca.corefacility.gview.map.gui.action.map.hide;

import javax.swing.undo.CannotRedoException;

import ca.corefacility.gview.map.gui.action.map.MapAction;

/**
 * Hide item action class.
 * 
 * @author Eric Marinier
 *
 */
public abstract class HideItemAction extends MapAction 
{
	@Override
	public void redo() throws CannotRedoException 
	{
       run();
	}
}
