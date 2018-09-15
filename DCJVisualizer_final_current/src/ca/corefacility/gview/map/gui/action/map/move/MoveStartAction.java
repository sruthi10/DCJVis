package ca.corefacility.gview.map.gui.action.map.move;

import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * Move to start action.
 * 
 * Moves the camera to the start of the genome.
 * 
 * @author Eric Marinier
 *
 */
public class MoveStartAction extends MoveAction 
{
	private final GViewMapManager gViewMapManager;

	public MoveStartAction(GViewMapManager gViewMapManager)
	{
		super(gViewMapManager);
		
		if(gViewMapManager == null)
			throw new IllegalArgumentException("GViewMap is null.");
		
		this.gViewMapManager = gViewMapManager;
	}

	@Override
	public void run() 
	{
		this.gViewMapManager.moveStart();
	}

}
