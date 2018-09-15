package ca.corefacility.gview.map.gui.action.dialog.show;

import ca.corefacility.gview.map.gui.dialog.ScaleDialog;

/**
 * Show scale dialog action class.
 * 
 * @author Eric Marinier.
 *
 */
public class ShowScaleDialogAction extends ShowDialogAction 
{
	private final ScaleDialog dialog;
	
	/**
	 * 
	 * @param dialog The scale dialog to show.
	 */
	public ShowScaleDialogAction(ScaleDialog dialog)
	{
		super(dialog);
		
		this.dialog = dialog;
	}
	
	@Override
	public void run()
	{
		super.run();
		
		this.dialog.showScaleDialog();
	}
}
