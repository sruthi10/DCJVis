package ca.corefacility.gview.map.gui.action.map;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * The Fit Image to Map / Fit Map to Screen action.
 * 
 * Scales and centers the map / image so that it takes up the entire window.
 * 
 * @author Eric Marinier
 *
 */
public class FITMAction extends MapAction 
{
	private final GViewMapManager gViewMapManager;
	private final double previousScale;
	private final double previousPosition;
	
	public FITMAction(GViewMapManager gViewMapManager)
	{
		if(gViewMapManager == null)
			throw new IllegalArgumentException("GViewMapManager is null.");
		
		this.gViewMapManager = gViewMapManager;
		this.previousPosition = gViewMapManager.getCenterBaseValue();
		this.previousScale = gViewMapManager.getZoomNormalFactor();
	}
	
	@Override
	public void undo() throws CannotUndoException 
	{
		this.gViewMapManager.setCenter((int)Math.round(this.previousPosition));
		this.gViewMapManager.zoomNormal(this.previousScale);
	}

	@Override
	public void redo() throws CannotRedoException 
	{
		this.run();
	}

	@Override
	public void run() 
	{
		this.gViewMapManager.scaleMapToScreen();
	}

}
