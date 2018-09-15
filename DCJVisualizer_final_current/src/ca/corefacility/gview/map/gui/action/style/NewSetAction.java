package ca.corefacility.gview.map.gui.action.style;

import javax.swing.JOptionPane;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.gui.editor.SlotSelectionDialog;
import ca.corefacility.gview.map.gui.editor.StyleEditorTree;
import ca.corefacility.gview.map.gui.editor.node.SetNode;
import ca.corefacility.gview.map.gui.editor.node.SlotNode;

/**
 * New style action.
 * 
 * Creates a new style.
 * 
 * @author Eric Marinier
 *
 */
public class NewSetAction extends StyleEditorAction
{
	private final StyleEditorTree styleEditorTree;
	
	public NewSetAction(StyleEditorTree styleEditorTree)
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
		
		if(selectedNode instanceof SlotNode)
		{
			this.styleEditorTree.createSetNode((SlotNode)selectedNode);
		}
		else if(selectedNode instanceof SetNode)
		{
			this.styleEditorTree.createSetNode((SetNode)selectedNode);
		}
		else
		{
			JOptionPane.showMessageDialog(this.styleEditorTree.getTopLevelAncestor(), "Please select a slot or set node.");
			
			SlotSelectionDialog dialog = new SlotSelectionDialog(this.styleEditorTree.getLowerSlot(), this.styleEditorTree.getUpperSlot());
			
			dialog.setLocationRelativeTo(this.styleEditorTree.getTopLevelAncestor());
			dialog.setVisible(true);	//This will block the current thread until the dialog is hidden.

			Integer slotNumber = dialog.getSlotNumber();
			
			if (slotNumber != null)
			{
				this.styleEditorTree.createSetNode(slotNumber);
			}
		}
	}
}
