package ca.corefacility.gview.map.gui.action.style;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.editor.StyleEditorTree;

/**
 * New slot action.
 * 
 * Creates a new slot.
 * 
 * @author Eric Marinier
 *
 */
public class NewSlotAction extends StyleEditorAction
{
	private final StyleEditorTree styleEditorTree;
	
	public NewSlotAction(StyleEditorTree styleEditorTree)
	{
		if(styleEditorTree == null)
			throw new IllegalArgumentException("StyleEditorTree is null.");
		
		this.styleEditorTree = styleEditorTree;
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
	public void run()
	{
		this.styleEditorTree.createSlotNode();
	}
}
