package ca.corefacility.gview.map.gui.action.map.move;

import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * Move to half action.
 * 
 * Moves the camera to the halfway location on the genome.
 * 
 * @author Eric Marinier
 *
 */
public class MoveHalfAction extends MoveAction 
{
	private final GViewMapManager gViewMapManager;

	public MoveHalfAction(GViewMapManager gViewMapManager)
	{
		super(gViewMapManager);
		
		if(gViewMapManager == null)
			throw new IllegalArgumentException("GViewMapManager is null.");
		
		this.gViewMapManager = gViewMapManager;
	}

	@Override
	public void run() 
	{
		this.gViewMapManager.moveMiddle();
	}
}
