package ca.corefacility.gview.map.gui.action.style;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.editor.StyleEditorTree;

/**
 * New legend action.
 * 
 * Creates a new legend.
 * 
 * @author Eric Marinier
 *
 */
public class NewLegendAction extends StyleEditorAction
{
	private final StyleEditorTree styleEditorTree;
	
	public NewLegendAction(StyleEditorTree styleEditorTree)
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
		this.styleEditorTree.createLegendNode();
	}
}
