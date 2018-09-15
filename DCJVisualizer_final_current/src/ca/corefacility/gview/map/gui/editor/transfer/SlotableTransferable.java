package ca.corefacility.gview.map.gui.editor.transfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import ca.corefacility.gview.map.gui.editor.node.Slotable;

/**
 * The slotable transferable object.
 * 
 * This class represents a slot transfer with an internal slot number.
 * 
 * It is intended only to facilitate drag and drop behaviour and not intended to transfer slots
 * between programs or saved data.
 * 
 * @author Eric Marinier
 *
 */
public class SlotableTransferable implements Transferable
{
	public static final DataFlavor slotFlavor = new DataFlavor(Slotable.class, "Slot Flavor");	//the slot flavor
	
	private final int slotNumber;	//the representation of the slot; just the number
	
	public SlotableTransferable(int index)
	{
		this.slotNumber = index;
	}
	
	/**
	 * 
	 * @return The data flavor of the slotable transferable.
	 */
	public static DataFlavor getSlotFlavor()
	{
		return SlotableTransferable.slotFlavor;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[]{SlotableTransferable.slotFlavor};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		boolean result = false;
		
		if(SlotableTransferable.slotFlavor.equals(flavor))
		{
			result = true;
		}
		
		return result;
	}

	@Override
	/**
	 * Will return an integer of the slot number.
	 */
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException
	{
		return this.slotNumber;
	}
}
