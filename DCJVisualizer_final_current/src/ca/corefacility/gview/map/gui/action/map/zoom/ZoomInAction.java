package ca.corefacility.gview.map.gui.action.map.zoom;

import ca.corefacility.gview.layout.sequence.ZoomException;
import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * Zoom map in action.
 * 
 * @author Eric Marinier
 *
 */
public class ZoomInAction extends ZoomAction
{
	private final GViewMapManager gViewMapManager;
	
	public ZoomInAction(GViewMapManager gViewMapManager)
	{
		super(gViewMapManager, gViewMapManager.getZoomFactor());
		
		this.gViewMapManager = gViewMapManager;
	}
	
	@Override
	public void run() 
	{
		double zoomFactor;
		
		zoomFactor = this.gViewMapManager.getZoomFactor();
		zoomFactor *= 1.1;
		try
        {
		    this.gViewMapManager.setZoomFactor(zoomFactor);
        }
		catch (ZoomException e)
        {
            
        }
	}
}
