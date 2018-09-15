package ca.corefacility.gview.map.gui.action.map;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * The export action class.
 * 
 * Performs an export of the GViewMap to an image.
 * 
 * @author Eric Marinier
 *
 */
public class ExportAction extends MapAction 
{
	private final GViewMapManager gViewMapManager;
	
	public ExportAction(GViewMapManager gViewMapManager)
	{
		if(gViewMapManager == null)
			throw new IllegalArgumentException("GViewMapManager is null.");
		
		this.gViewMapManager = gViewMapManager;
	}
	
	@Override
	public void undo() throws CannotUndoException 
	{
		throw new CannotUndoException();
	}

	@Override
	public void redo() throws CannotRedoException 
	{
		throw new CannotRedoException();
	}

	@Override
	public void run() 
	{
		this.gViewMapManager.exportImage();
	}
	
	@Override
	public boolean canUndo() 
	{
		return false;
	}
	
	@Override
	public boolean canRedo() 
	{
		return false;
	}
	
	@Override
	public boolean isSignificant() 
	{
		return true;
	}
}
