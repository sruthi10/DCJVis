package ca.corefacility.gview.map.gui.action.map.move;

import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * Move to third quarter action.
 * 
 * Moves the camera to the third quarter of the genome.
 * 
 * @author Eric Marinier
 *
 */
public class MoveThirdQuarterAction extends MoveAction 
{
	private final GViewMapManager gViewMapManager;

	public MoveThirdQuarterAction(GViewMapManager gViewMapManager)
	{
		super(gViewMapManager);
		
		if(gViewMapManager == null)
			throw new IllegalArgumentException("GViewMapManager is null.");
		
		this.gViewMapManager = gViewMapManager;
	}

	@Override
	public void run() 
	{
		this.gViewMapManager.moveThirdQuarter();
	}
}
