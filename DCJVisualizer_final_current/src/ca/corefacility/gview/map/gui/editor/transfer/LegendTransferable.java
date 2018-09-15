package ca.corefacility.gview.map.gui.editor.transfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import ca.corefacility.gview.map.gui.editor.node.LegendItemNode;

/**
 * This class represents the transfer object for legend item style objects.
 * 
 * It uses integers to represent the transfer because the legend objects themselves
 * and internal components are not serializable.
 * 
 * It is intended only to facilitate drag and drop behaviour and not intended to transfer legend
 * items between programs or saved data.
 * 
 * @author Eric Marinier
 *
 */
public class LegendTransferable implements Transferable
{
	public static final DataFlavor legendFlavor = new DataFlavor(LegendItemNode.class, "Legend Flavor");	//the legend flavor
	
	private final LegendTransferableData data;	//the custom transfer data object
	
	/**
	 * 
	 * @param data The custom  legend transferable data that represents the legend transfer.
	 */
	public LegendTransferable(LegendTransferableData data)
	{
		if(data == null)
			throw new IllegalArgumentException("LegendTransferableData is null.");
		
		this.data = data;
	}
	
	/**
	 * 
	 * @return The legend flavor.
	 */
	public static DataFlavor getLegendFlavor()
	{
		return LegendTransferable.legendFlavor;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[]{LegendTransferable.legendFlavor};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		boolean result = false;
		
		if(LegendTransferable.legendFlavor.equals(flavor))
		{
			result = true;
		}
		
		return result;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException
	{
		return this.data;
	}
}
