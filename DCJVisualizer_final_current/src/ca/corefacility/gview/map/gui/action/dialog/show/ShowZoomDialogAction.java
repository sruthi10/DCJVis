package ca.corefacility.gview.map.gui.action.dialog.show;

import ca.corefacility.gview.map.gui.dialog.ZoomDialog;

/**
 * Show zoom dialog action class.
 * 
 * @author Eric Marinier
 *
 */
public class ShowZoomDialogAction extends ShowDialogAction 
{
	private final ZoomDialog dialog;
	
	/**
	 * 
	 * @param dialog The zoom dialog to show.
	 */
	public ShowZoomDialogAction(ZoomDialog dialog)
	{
		super(dialog);
		
		this.dialog = dialog;
	}
	
	@Override
	public void run()
	{
		super.run();
		
		this.dialog.showZoomDialog();
	}
}
