package ca.corefacility.gview.map.gui.editor.node;

import ca.corefacility.gview.map.gui.editor.panel.TooltipPanel;

/**
 * The node class for the tool tip styles. Intended to be used within a StyleEditorTree.
 * 
 * @author Eric Marinier
 *
 */
public class TooltipNode extends StyleEditorNode 
{
	private static final long serialVersionUID = 1L;	//requested by java
	private static final String TOOLTIP = "Tooltip";
	
	private final TooltipPanel tooltipPanel;

	/**
	 * 
	 * @param tooltipPanel The related panel.
	 */
	public TooltipNode(TooltipPanel tooltipPanel) 
	{
		super(tooltipPanel, TOOLTIP);
		
		if(tooltipPanel == null)
			throw new IllegalArgumentException("TooltipPanel is null.");
		
		this.tooltipPanel = tooltipPanel;
	}

	@Override
	public TooltipPanel getPanel() 
	{
		if(this.tooltipPanel == null)
			throw new IllegalArgumentException("TooltipPanel is null.");
		
		return this.tooltipPanel;
	}
}
