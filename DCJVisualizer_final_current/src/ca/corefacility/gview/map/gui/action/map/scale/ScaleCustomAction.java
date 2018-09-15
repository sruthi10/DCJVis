package ca.corefacility.gview.map.gui.action.map.scale;

import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * Custom scale map action.
 * 
 * @author Eric
 *
 */
public class ScaleCustomAction extends ScaleAction 
{
	private final GViewMapManager gViewMapManager;
	private final double value;
	
	public ScaleCustomAction(GViewMapManager gViewMapManager, double value)
	{
		super(gViewMapManager, gViewMapManager.getZoomNormalFactor());
		
		this.gViewMapManager = gViewMapManager;
		this.value = value;
	}

	@Override
	public void run() 
	{
		this.gViewMapManager.zoomNormal(value); 
	}
}
