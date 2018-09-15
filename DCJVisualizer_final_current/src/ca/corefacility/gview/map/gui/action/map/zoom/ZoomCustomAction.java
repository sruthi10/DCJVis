package ca.corefacility.gview.map.gui.action.map.zoom;

import ca.corefacility.gview.layout.sequence.ZoomException;
import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * Zoom map custom action class.
 * 
 * @author Eric Marinier
 *
 */
public class ZoomCustomAction extends ZoomAction 
{
	private final GViewMapManager gViewMapManager;
	private final double value;
	
	/**
	 * 
	 * @param gViewMapManager The GView map Manager object.
	 * @param value The value to zoom to.
	 */
	public ZoomCustomAction(GViewMapManager gViewMapManager, double value)
	{
		super(gViewMapManager, gViewMapManager.getZoomFactor());
		
		this.gViewMapManager = gViewMapManager;
		this.value = value;
	}

	@Override
	public void run()
	{
		try
        {
            this.gViewMapManager.setZoomFactor(value);
        }
		catch (ZoomException e)
        {
			e.printStackTrace();
        } 
	}
}
