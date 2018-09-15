package ca.corefacility.gview.map.gui.action.map.move;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.GViewMapManager;
import ca.corefacility.gview.map.gui.action.map.MapAction;

/**
 * Move action class.
 * 
 * @author Eric Marinier
 *
 */
public abstract class MoveAction extends MapAction 
{
	private final GViewMapManager gViewMapManager;
	private final double previousPosition;
	
	public MoveAction(GViewMapManager gViewMapManager)
	{
		if(gViewMapManager == null)
			throw new IllegalArgumentException("GViewMapManager is null.");
		
		this.gViewMapManager = gViewMapManager;
		this.previousPosition = gViewMapManager.getCenterBaseValue();
	}
	
	@Override
	public void undo() throws CannotUndoException 
	{
		this.gViewMapManager.setCenter((int)Math.round(this.previousPosition));
	}
	
	@Override
	public void redo() throws CannotRedoException 
	{
		this.run();
	}
}
