package ca.corefacility.gview.map.gui.action.map.zoom;

import ca.corefacility.gview.layout.sequence.ZoomException;
import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * Zoom map out action class.
 * 
 * @author Eric Marinier
 *
 */
public class ZoomOutAction extends ZoomAction
{
	private final GViewMapManager gViewMapManager;
	
	public ZoomOutAction(GViewMapManager gViewMapManager)
	{
		super(gViewMapManager, gViewMapManager.getZoomFactor());
		
		this.gViewMapManager = gViewMapManager;
	}
	
	@Override
	public void run()
	{
		double zoomFactor;
		
		zoomFactor = this.gViewMapManager.getZoomFactor();
		zoomFactor *= 0.9;
		try
        {
            this.gViewMapManager.setZoomFactor(zoomFactor);
        }
		catch (ZoomException e)
        {
            
        }
	}
}
