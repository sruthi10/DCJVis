package ca.corefacility.gview.map.gui.action.map.move;

import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * Move to first quarter action.
 * 
 * Moves the camera to the first quarter location of the genome.
 * 
 * @author Eric Marinier
 *
 */
public class MoveFirstQuarterAction extends MoveAction 
{
	private final GViewMapManager gViewMapManager;

	public MoveFirstQuarterAction(GViewMapManager gViewMapManager)
	{
		super(gViewMapManager);
		
		if(gViewMapManager == null)
			throw new IllegalArgumentException("GViewMapManager is null.");
		
		this.gViewMapManager = gViewMapManager;
	}

	@Override
	public void run() 
	{
		this.gViewMapManager.moveFirstQuarter();
	}

}
