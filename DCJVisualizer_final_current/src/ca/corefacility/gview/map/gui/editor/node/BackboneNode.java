package ca.corefacility.gview.map.gui.editor.node;

import java.awt.Paint;

import ca.corefacility.gview.map.gui.editor.panel.BackbonePanel;

/**
 * The node class for the backbone panel.
 * 
 * Intended to be used within a StyleEditorTree.
 * 
 * @author Eric Marinier
 */
public class BackboneNode extends StyleEditorNode 
{
	private static final long serialVersionUID = 1L;	//requested by java
	private static final String BACKBONE = "Backbone";
	
	private final BackbonePanel backbonePanel;	//the related backbone panel
	
	/**
	 * 
	 * @param backbonePanel The related backbone panel.
	 */
	public BackboneNode(BackbonePanel backbonePanel) 
	{
		super(backbonePanel, BACKBONE);
		
		if(backbonePanel == null)
			throw new IllegalArgumentException("BackbonePanel is null.");

		this.backbonePanel = backbonePanel;
	}
	
	@Override
	/**
	 * Returns the backbone panel.
	 */
	public BackbonePanel getPanel() 
	{
		if(this.backbonePanel == null)
			throw new IllegalArgumentException("BackbonePanel is null.");
		
		return this.backbonePanel;
	}

	public Paint getNodeColor()
	{
		return this.backbonePanel.getBackboneStyleController().getColor();
	}
}
