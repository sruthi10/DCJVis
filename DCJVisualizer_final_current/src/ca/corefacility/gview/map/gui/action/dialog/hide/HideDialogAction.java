package ca.corefacility.gview.map.gui.action.dialog.hide;

import javax.swing.JDialog;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.action.Action;

/**
 * Hide dialog action class.
 * 
 * @author Eric Marinier
 *
 */
public abstract class HideDialogAction extends Action 
{
	private final JDialog dialog;
	
	/**
	 * 
	 * @param dialog The dialog to hide.
	 */
	HideDialogAction(JDialog dialog)
	{
		if(dialog == null)
			throw new IllegalArgumentException("Dialog is null.");
		
		this.dialog = dialog;
	}
	
	@Override
	public void undo() throws CannotUndoException 
	{
		this.dialog.setVisible(true);
	}

	@Override
	public void redo() throws CannotRedoException 
	{
		this.run();
	}

	@Override
	public void run() 
	{
		this.dialog.setVisible(false);
	}
	
	@Override
	public boolean isSignificant() 
	{
		// TODO Auto-generated method stub
		return false;
	}
}
