package ca.corefacility.gview.map.gui.editor.node;

import ca.corefacility.gview.map.gui.editor.panel.RootPanel;

/**
 * The root node. Intended to be used within a StyleTree.
 * 
 * @author Eric Marinier
 *
 */
public class RootNode extends StyleEditorNode 
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private final RootPanel rootPanel;
	
	/**
	 * 
	 * @param name The displayed name of the node.
	 * @param panel The related panel.
	 */
	public RootNode(String name, RootPanel panel) 
	{
		super(panel, name);
		
		if(panel == null)
			throw new IllegalArgumentException("RootPanel is null.");

		this.rootPanel = panel;
	}

	@Override
	public RootPanel getPanel() 
	{
		if(this.rootPanel == null)
		{
			throw new IllegalArgumentException("RootPanel is null.");
		}
		
		return this.rootPanel;
	}
}
