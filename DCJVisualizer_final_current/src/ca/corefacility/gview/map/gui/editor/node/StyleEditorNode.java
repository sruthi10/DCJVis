package ca.corefacility.gview.map.gui.editor.node;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ca.corefacility.gview.map.controllers.selection.SelectionListener;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.editor.communication.SelectionEvent;
import ca.corefacility.gview.map.gui.editor.panel.StylePanel;

/**
 * The default node class. Intended to be used within a StyleEditorTree.
 * 
 * @author Eric Marinier
 * 
 */
public abstract class StyleEditorNode extends DefaultMutableTreeNode implements SelectionListener
{
	private static final long serialVersionUID = 1L; // requested by java
	private static int idCounter = 0; // the id counter for the nodes

	private final int id = getMyID(); // the id of the node
	private final StylePanel panel;
	private String name;

	/**
	 * 
	 * @param name
	 *            The displayed name of the node.
	 */
	public StyleEditorNode(StylePanel panel, String name)
	{
		super(name);

		if (name == null)
			name = StyleEditorUtility.DEFAULT_STYLE_NODE_NAME;

		if (panel == null)
			throw new IllegalArgumentException("StylePanel is null.");

		this.name = name;
		this.panel = panel;
	}

	/**
	 * 
	 * @return The ID of the node.
	 */
	private static final int getMyID()
	{
		int ID = idCounter;
		idCounter++;

		return ID;
	}

	/**
	 * 
	 * @return The node's panel. Note: the returned panels may be panels
	 *         extending StylePanel.
	 */
	public StylePanel getPanel()
	{
		if (this.panel == null)
			throw new NullPointerException("StylePanel is null.");

		return this.panel;
	}

	/**
	 * Renames the node. Prompts the user with an input dialog.
	 */
	public void rename()
	{
		// get the object within the node
		Object string = this.getUserObject();

		// should always be a string, given the constructor
		if (string instanceof String)
		{
			string = JOptionPane.showInputDialog("Enter a New Name");

			if (string != null && string instanceof String)
			{
				this.setUserObject(string);
				this.name = (String) string;
			}
		}
	}

	/**
	 * Renames the node to newName.
	 * 
	 * @param newName
	 *            The new name of the node.
	 */
	public void rename(String newName)
	{
		// get the object within the node
		Object object = this.getUserObject();

		if (newName == null)
			newName = StyleEditorUtility.DEFAULT_STYLE_NODE_NAME;

		// should always be a string, given the constructor
		if (object instanceof String)
		{
			object = newName;

			if (object != null && object instanceof String)
			{
				this.setUserObject(object);
				this.name = (String) object;
			}
		}
	}

	/**
	 * Updates the panel within the node.
	 */
	public void update()
	{
		JPanel panel = getPanel();

		if (panel instanceof StylePanel)
			((StylePanel) panel).updatePanel();
	}

	/**
	 * Returns the name of the node.
	 * 
	 * @return The name of the node.
	 */
	public String getNodeName()
	{
		if (this.name == null)
			this.name = StyleEditorUtility.DEFAULT_STYLE_NODE_NAME;

		return this.name;
	}

	@Override
	/**
	 * Forces only StyleEditorNodes to be added as a child.
	 */
	public void add(MutableTreeNode newChild)
	{
		if (newChild instanceof StyleEditorNode)
			super.add(newChild);
		else
			throw new IllegalArgumentException("Child of StyleEditorNode must be a StyleEditorNode");
	}

	@Override
	public StyleEditorNode getParent()
	{
		StyleEditorNode parent = null;

		if (super.getParent() instanceof StyleEditorNode)
		{
			parent = (StyleEditorNode) super.getParent();
		}
		else if (super.getParent() != null)
		{
			throw new ClassCastException("Non-StyleEditorNode parent of a style editor node.");
		}

		return parent;
	}

	/**
	 * 
	 * @return The ID of the node.
	 */
	public int getID()
	{
		return this.id;
	}

	@Override
	public void selectionEvent(SelectionEvent event)
	{
		this.panel.handleGUIEvent(event.getGUIEvent());
	}
}
