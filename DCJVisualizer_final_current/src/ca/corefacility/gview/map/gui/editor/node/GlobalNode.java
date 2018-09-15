package ca.corefacility.gview.map.gui.editor.node;

import ca.corefacility.gview.map.gui.editor.panel.GlobalPanel;

/**
 * The node class for the global style. Intended to be used within a StyleEditorTree.
 * 
 * @author Eric Marinier
 *
 */
public class GlobalNode extends StyleEditorNode 
{
	private static final long serialVersionUID = 1L;	//requested by java
	private static final String GLOBAL = "Global"; 
	
	private final GlobalPanel globalPanel;	//the related global panel
	
	/**
	 * 
	 * @param globalPanel The related global panel.
	 */
	public GlobalNode(GlobalPanel globalPanel)
	{
		super(globalPanel, GLOBAL);
		
		if(globalPanel == null)
			throw new IllegalArgumentException("GlobalPanel is null");
		
		this.globalPanel = globalPanel;
	}

	@Override
	public GlobalPanel getPanel() 
	{
		if(this.globalPanel == null)
			throw new IllegalArgumentException("GlobalPanel is null");
		
		return this.globalPanel;
	}
}
