package ca.corefacility.gview.map.gui.action.system;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.action.Action;

/**
 * Styem action class.
 * 
 * @author Eric Marinier
 */
public abstract class SystemAction extends Action 
{
	@Override
	public void undo() throws CannotUndoException 
	{
		throw new CannotUndoException();
	}

	@Override
	public void redo() throws CannotRedoException 
	{
		throw new CannotRedoException();
	}
	
	@Override
	public boolean canUndo() 
	{
		return false;
	}
	
	@Override
	public boolean canRedo() 
	{
		return false;
	}
}
