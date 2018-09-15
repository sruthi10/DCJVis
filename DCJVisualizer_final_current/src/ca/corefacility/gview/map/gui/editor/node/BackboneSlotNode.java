package ca.corefacility.gview.map.gui.editor.node;

import java.awt.Paint;

import ca.corefacility.gview.map.gui.editor.panel.BackbonePanel;

/**
 * The node used to represent the backbone as it sits in GView's collection of slots.
 * 
 * This should be as minimal as possible, as it is only for representation on the tree
 * and not for manipulation of the backbone itself.
 * 
 * @author Eric Marinier
 *
 */
public class BackboneSlotNode extends StyleEditorNode implements Slotable
{
	private static final long serialVersionUID = 1L;
	
	private static final String BACKBONE = "Backbone Slot";
	private final BackbonePanel backbonePanel;

	/**
	 * 
	 * @param backbonePanel The associate backbone slot panel.
	 */
	public BackboneSlotNode(BackbonePanel backbonePanel)
	{
		super(backbonePanel, BACKBONE);
		
		if(backbonePanel == null)
		{
			throw new IllegalArgumentException("BackboneSlotPanel is null.");
		}
		else
		{
			this.backbonePanel = backbonePanel;
		}
	}
	
	@Override
	/**
	 * @return  The BackbonePanel.
	 */
	public BackbonePanel getPanel()
	{
		if(this.backbonePanel == null)
			throw new NullPointerException("BackbonePanel is null.");
		
		return this.backbonePanel;
	}

	@Override
	public void updateName()
	{
		super.rename(BACKBONE);		
	}

	@Override
	public int getSlotNumber()
	{
		return Slotable.BACKBONE_SLOT_NUMBER;
	}

	public Paint getNodeColor()
	{
		return this.backbonePanel.getBackboneStyleController().getColor();
	}
}
