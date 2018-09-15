package ca.corefacility.gview.map.gui.action.map.scale;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.GViewMapManager;
import ca.corefacility.gview.map.gui.action.map.MapAction;

/**
 * Scale map action.
 * 
 * @author Eric Marinier
 *
 */
public abstract class ScaleAction extends MapAction 
{
	private final GViewMapManager gViewMapManager;
	private final double previous;
	
	public ScaleAction(GViewMapManager gViewMapManager, double previous)
	{
		if(gViewMapManager == null)
			throw new IllegalArgumentException("GViewMapManager is null.");
		
		if(previous < GUIUtility.SCALE_MIN)
			previous = GUIUtility.SCALE_MIN;
		
		if(previous > GUIUtility.SCALE_MAX)
			previous = GUIUtility.SCALE_MAX;
		
		this.gViewMapManager = gViewMapManager;
		this.previous = previous;
	}
	
	@Override
	public void undo() throws CannotUndoException 
	{
		this.gViewMapManager.zoomNormal(this.previous); 
	}
	
	@Override
	public void redo() throws CannotRedoException
	{
		this.run();
	}
}
