package ca.corefacility.gview.map.gui.action.dialog.show;

import ca.corefacility.gview.map.gui.dialog.StyleDialog;

/**
 * Show a style dialog action.
 * 
 * @author Eric Marinier
 *
 */
public class ShowStyleDialogAction extends ShowDialogAction
{
	/**
	 * 
	 * @param dialog The style dialog to show.
	 */
	public ShowStyleDialogAction(StyleDialog dialog)
	{
		super(dialog);

		if(dialog == null)
		{
			throw new IllegalArgumentException("Dialog is null.");
		}
		else
		{
			dialog.updateContentPanel();
		}
	}
}
