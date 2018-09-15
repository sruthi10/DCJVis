package ca.corefacility.gview.map.gui.action.map.scale;

import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * Scale map out action.
 * 
 * @author Eric Marinier
 *
 */
public class ScaleOutAction extends ScaleAction 
{
	private final GViewMapManager gViewMapManager;
	
	public ScaleOutAction(GViewMapManager gViewMapManager)
	{
		super(gViewMapManager, gViewMapManager.getZoomNormalFactor());
		
		this.gViewMapManager = gViewMapManager;
	}
	
	public void run()
	{
		double scaleFactor;
		
		scaleFactor = this.gViewMapManager.getZoomNormalFactor();
		scaleFactor *= 0.9;
		this.gViewMapManager.zoomNormal(scaleFactor);
	}
}
