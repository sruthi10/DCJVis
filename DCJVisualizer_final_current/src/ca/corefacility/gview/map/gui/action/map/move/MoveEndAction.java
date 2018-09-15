package ca.corefacility.gview.map.gui.action.map.move;

import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * Move end action.
 * 
 * Moves the camera to the end of the genome.
 * 
 * @author Eric Marinier
 *
 */
public class MoveEndAction extends MoveAction 
{
	private final GViewMapManager gViewMapManager;

	public MoveEndAction (GViewMapManager gViewMapManager)
	{		
		super(gViewMapManager);
		
		if(gViewMapManager == null)
			throw new IllegalArgumentException("GViewMapManager is null.");
		
		this.gViewMapManager = gViewMapManager;
	}
	
	@Override
	public void run() 
	{
		this.gViewMapManager.moveEnd();
	}
}
