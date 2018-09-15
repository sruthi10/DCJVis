package ca.corefacility.gview.map.gui.editor.node;

import javax.swing.tree.MutableTreeNode;

/**
 * This interface is intended to allow tree nodes that represent 'styles' 
 * that can be placed in slots to interface appropriately.
 * 
 * @author Eric Marinier
 *
 */
public interface Slotable extends MutableTreeNode
{	
	public static final String BACKBONE = "Backbone";
	public static final int BACKBONE_SLOT_NUMBER = 0;
	
	/**
	 * Updates the name of the slotable node by getting its internal slot number.
	 */
	public void updateName();
	
	/**
	 * Returns the slot number.
	 * @return The slot number.
	 */
	public int getSlotNumber();
}
