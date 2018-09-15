package ca.corefacility.gview.map.gui.action.dialog.show;

import ca.corefacility.gview.map.gui.dialog.MoveDialog;

/**
 * Show move dialog action class.
 * 
 * @author Eric Marinier
 */
public class ShowMoveDialogAction extends ShowDialogAction 
{
	private final MoveDialog dialog;
	
	/**
	 * 
	 * @param dialog The move dialog to show.
	 */
	public ShowMoveDialogAction (MoveDialog dialog)
	{
		super(dialog);
		
		this.dialog = dialog;
	}
	
	@Override
	public void run()
	{
		super.run();
		
		this.dialog.showMoveDialog();
	}
}
