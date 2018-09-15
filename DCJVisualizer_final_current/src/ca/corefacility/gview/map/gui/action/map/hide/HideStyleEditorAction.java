package ca.corefacility.gview.map.gui.action.map.hide;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.action.style.StyleEditorAction;
import ca.corefacility.gview.map.gui.editor.StyleEditorFrame;

/**
 * Hide style editor action class.
 * 
 * @author Eric Marinier
 *
 */
public class HideStyleEditorAction extends StyleEditorAction 
{
	private final StyleEditorFrame editor;
	
	/**
	 * 
	 * @param editor The style editor frame to hide.
	 */
	public HideStyleEditorAction(StyleEditorFrame editor)
	{
		if(editor == null)
			throw new IllegalArgumentException("StyleEditorFrame is null.");
		
		this.editor = editor;
	}

	@Override
	public void undo() throws CannotUndoException 
	{
		this.editor.setVisible(true);
	}

	@Override
	public void redo() throws CannotRedoException 
	{
		this.run();
	}

	@Override
	public void run() 
	{
		this.editor.setVisible(false);
	}

}
