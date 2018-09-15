package ca.corefacility.gview.map.gui.action.map;

import javax.swing.JFrame;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;


/**
 * The full screen action class.
 * 
 * Performs the a full screen on the frame.
 * 
 * @author ericm
 *
 */
public class FullScreenAction extends MapAction
{
	private final JFrame frame;
	
	/**
	 * 
	 * @param frame The frame to make full screen.
	 */
	public FullScreenAction(JFrame frame)
	{
		if(frame == null)
			throw new IllegalArgumentException("Frame is null.");
		
		this.frame = frame;
	}

	@Override
	public void undo() throws CannotUndoException 
	{
		this.frame.setExtendedState(JFrame.NORMAL);		
	}

	@Override
	public void redo() throws CannotRedoException 
	{
		this.run();		
	}

	@Override
	public void run() 
	{
		this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

}
