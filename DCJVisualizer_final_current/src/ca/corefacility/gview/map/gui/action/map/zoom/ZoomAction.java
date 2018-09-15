package ca.corefacility.gview.map.gui.action.map.zoom;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.layout.sequence.ZoomException;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.GViewMapManager;
import ca.corefacility.gview.map.gui.action.map.MapAction;

/**
 * Zoom action class.
 * 
 * @author Eric Marinier
 *
 */
public abstract class ZoomAction extends MapAction 
{
	private final GViewMapManager gViewMapManager;
	private final double previous;
	
	public ZoomAction(GViewMapManager gViewMapManager, double previous)
	{
		if(gViewMapManager == null)
			throw new IllegalArgumentException("GViewMapManager is null.");
		
		if(previous < GUIUtility.ZOOM_MIN)
			previous = GUIUtility.ZOOM_MIN;
		
		if(previous > GUIUtility.ZOOM_MAX)
			previous = GUIUtility.ZOOM_MAX;
		
		this.gViewMapManager = gViewMapManager;
		this.previous = previous;
	}
	
	@Override
	public void undo() throws CannotUndoException 
	{
		try
        {
            this.gViewMapManager.setZoomFactor(this.previous);
        }
		catch (ZoomException e)
        {
            e.printStackTrace();
        } 
	}
	
	@Override
	public void redo() throws CannotRedoException 
	{
		this.run();
	}
}
