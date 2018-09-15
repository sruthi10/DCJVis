package ca.corefacility.gview.map.gui.action.style;

import javax.swing.JOptionPane;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.editor.StyleEditorTree;
import ca.corefacility.gview.map.gui.editor.node.SetNode;

/**
 * Refine style action.
 * 
 * Allows for an "AND" operation on the current set and a new set.
 * 
 * @author Eric Marinier
 *
 */
public class RefineSetAction extends StyleEditorAction
{
	private final StyleEditorTree styleEditorTree;
	
	public RefineSetAction(StyleEditorTree styleEditorTree)
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
		Object selectedNode = this.styleEditorTree.getLastSelectedPathComponent();
		
		if(selectedNode instanceof SetNode)
		{
			this.styleEditorTree.refineSetNode((SetNode)selectedNode);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please select a slot or set node.");
		}
	}
}
