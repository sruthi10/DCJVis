package ca.corefacility.gview.map.gui.action.dialog.show;

import javax.swing.JDialog;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.action.Action;

/**
 * Show dialog action.
 * 
 * @author Eric Marinier
 */
public abstract class ShowDialogAction extends Action 
{
	private final JDialog dialog;
	
	/**
	 * 
	 * @param dialog The dialog to show.
	 */
	public ShowDialogAction(JDialog dialog)
	{
		if(dialog == null)
			throw new IllegalArgumentException("Dialog is null.");
		
		this.dialog = dialog;
	}
	
	@Override
	public void undo() throws CannotUndoException 
	{
		this.dialog.setVisible(false);
	}

	@Override
	public void redo() throws CannotRedoException 
	{
		this.run();
	}

	@Override
	public void run() 
	{
		if(this.dialog.getOwner() != null)
		{
			this.dialog.setLocationRelativeTo(this.dialog.getOwner());
		}
		
		this.dialog.setVisible(true);
	}
	
	@Override
	public boolean isSignificant() 
	{
		return false;
	}
}
