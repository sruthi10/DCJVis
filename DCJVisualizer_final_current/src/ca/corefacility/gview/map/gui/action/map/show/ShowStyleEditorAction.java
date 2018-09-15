package ca.corefacility.gview.map.gui.action.map.show;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.action.style.StyleEditorAction;
import ca.corefacility.gview.map.gui.editor.StyleEditorFrame;

/**
 * Show style editor action class.
 * 
 * @author Eric Marinier
 *
 */
public class ShowStyleEditorAction extends StyleEditorAction 
{
	private StyleEditorFrame editor;
	
	/**
	 * 
	 * @param editor The style editor frame to show.
	 */
	public ShowStyleEditorAction(StyleEditorFrame editor)
	{
		if(editor == null)
			throw new IllegalArgumentException("StyleEditor is null.");
		
		this.editor = editor;
	}

	@Override
	public void undo() throws CannotUndoException 
	{
		this.editor.setVisible(false);
	}

	@Override
	public void redo() throws CannotRedoException 
	{
		this.run();
	}

	@Override
	public void run() 
	{
		this.editor.setVisible(true);
	}

}
