package ca.corefacility.gview.map.gui.editor.node;

import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;

import ca.corefacility.gview.map.gui.editor.panel.SlotsPanel;

/**
 * The node class for the slots.
 * 
 * Intended to be used within a StyleEditorTree.
 * 
 * @author Eric Marinier
 *
 */
public class SlotsNode extends StyleEditorNode
{
	private static final long serialVersionUID = 1L;	//requested by java
	private static final String SLOTS = "Slots";
	
	private final SlotsPanel slotsPanel;
	
	/**
	 * 
	 * @param slotsPanel The related SlotsPanel.
	 */
	public SlotsNode(SlotsPanel slotsPanel)
	{
		super(slotsPanel, SLOTS);
		
		if(slotsPanel == null)
			throw new IllegalArgumentException("SlotsPanel is null.");
		
		this.slotsPanel = slotsPanel;
	}
	
	@Override
	public SlotsPanel getPanel()
	{
		if(this.slotsPanel == null)
			throw new IllegalArgumentException("SlotsPanel is null.");
		
		return this.slotsPanel;
	}

	@Override
	/**
	 * Forces only Slotable nodes to be added.
	 */
	public void add(MutableTreeNode newChild)
	{
		if(newChild instanceof Slotable)
			super.add(newChild);
		else
			throw new IllegalArgumentException("Only Slotable nodes may be added to a SlotsNode.");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	/**
	 * Checks that only Slotable nodes are children. Then returns super's.
	 */
	 public Enumeration<Slotable> children()
	{
		Enumeration<Object> enumeration = super.children();
		
		Object currentNode;
		
		while(enumeration.hasMoreElements())
		{
			currentNode = enumeration.nextElement();
			
			if(!(currentNode instanceof Slotable))
			{					
				throw new IllegalArgumentException("Non-Slotable node child of SlotsNode");
			}
		}
		
		return super.children();
	}
	
	/**
	 * 
	 * @return The backbone node.
	 */
	public BackboneSlotNode getBackboneNode()
	{
		Enumeration<Slotable> nodes = this.children();
		BackboneSlotNode backboneNode = null;
		
		MutableTreeNode current;
		
		while(nodes.hasMoreElements() && backboneNode == null)
		{
			current = nodes.nextElement();
			
			if(current instanceof BackboneSlotNode)
			{
				backboneNode = (BackboneSlotNode)current;
			}
		}
		
		if(backboneNode == null)
		{
			throw new IllegalArgumentException("BackboneNode is null!");
		}
		
		return backboneNode;
	}
}
