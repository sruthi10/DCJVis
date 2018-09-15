package ca.corefacility.gview.map.gui.editor.node;

import ca.corefacility.gview.map.gui.editor.panel.LegendsPanel;

/**
 * The node class for the legend. 
 * 
 * Intended to be used within a StyleEditorTree.
 * 
 * @author Eric Marinier
 *
 */
public class LegendNode extends StyleEditorNode 
{
	private static final long serialVersionUID = 1L;	//requested by java
	private static final String LEGEND = "Legends";
	
	private final LegendsPanel legendPanel;

	/**
	 * 
	 * @param panel The related LegendPanel.
	 */
	public LegendNode(LegendsPanel panel) 
	{
		super(panel, LEGEND);
		
		if(panel == null)
			throw new IllegalArgumentException("LegendPanel is null");
		
		this.legendPanel = panel;
	}

	@Override
	public LegendsPanel getPanel() 
	{
		if(this.legendPanel == null)
			throw new IllegalArgumentException("LegendPanel is null");
		
		return this.legendPanel;
	}
}
