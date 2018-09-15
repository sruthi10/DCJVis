package ca.corefacility.gview.map.gui.action.map.scale;

import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * Scale map in action.
 * 
 * @author Eric Marinier
 *
 */
public class ScaleInAction extends ScaleAction
{
	private final GViewMapManager gViewMapManager;
	
	public ScaleInAction(GViewMapManager gViewMapManager)
	{
		super(gViewMapManager, gViewMapManager.getZoomNormalFactor());
		
		this.gViewMapManager = gViewMapManager;
	}
	
	public void run()
	{
		double scaleFactor;
		
		scaleFactor = this.gViewMapManager.getZoomNormalFactor();
		scaleFactor *= 1.1;
		this.gViewMapManager.zoomNormal(scaleFactor);
	}
}
