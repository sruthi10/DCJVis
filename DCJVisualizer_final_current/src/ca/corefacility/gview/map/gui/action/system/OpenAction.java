package ca.corefacility.gview.map.gui.action.system;

import ca.corefacility.gview.map.gui.open.OpenDialog;

/**
 * Open dialog action.
 * 
 * Shows the open (program) dialog.
 * 
 * @author Eric Marinier
 *
 */
public class OpenAction extends SystemAction 
{
	@Override
	public void run() 
	{
		OpenDialog.showOpenDialog();
	}
}
