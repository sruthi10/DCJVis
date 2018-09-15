package ca.corefacility.gview.map.gui.action.map;

import javax.swing.JFrame;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;


/**
 * Resize screen action.
 * 
 * @author Eric Marinier
 *
 */
public class ResizeScreenAction extends MapAction
{
	private final JFrame frame;
	
	/**
	 * 
	 * @param frame The frame to resize.
	 */
	public ResizeScreenAction(JFrame frame)
	{
		if(frame == null)
			throw new IllegalArgumentException("Frame is null.");
		
		this.frame = frame;
	}

	@Override
	public void undo() throws CannotUndoException 
	{
		this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	@Override
	public void redo() throws CannotRedoException 
	{
		this.run();		
	}

	@Override
	public void run() 
	{
		this.frame.setExtendedState(JFrame.NORMAL);		
	}

}
