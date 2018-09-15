package ca.corefacility.gview.map.gui.editor.node;

import ca.corefacility.gview.map.gui.editor.panel.RulerPanel;

/**
 * The node class for the ruler style. 
 * 
 * Intended to be used within a StyleEditorTree.
 * 
 * @author Eric Marinier
 *
 */
public class RulerNode extends StyleEditorNode 
{
	private static final long serialVersionUID = 1L;	//requested by java
	private static final String RULER = "Ruler";
	
	private final RulerPanel rulerPanel;	//the related ruler planel

	/**
	 * 
	 * @param rulerPanel The related panel.
	 */
	public RulerNode(RulerPanel rulerPanel) 
	{
		super(rulerPanel, RULER);
		
		if(rulerPanel == null)
			throw new IllegalArgumentException("RulerPanel is null");

		this.rulerPanel = rulerPanel;
	}

	@Override
	public RulerPanel getPanel() 
	{
		if(this.rulerPanel == null)
			throw new IllegalArgumentException("RulerPanel is null");
		
		return this.rulerPanel;
	}
}
