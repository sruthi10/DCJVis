package ca.corefacility.gview.map.gui.editor.transfer;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ca.corefacility.gview.map.gui.editor.StyleEditorTree;
import ca.corefacility.gview.map.gui.editor.node.LegendItemNode;
import ca.corefacility.gview.map.gui.editor.node.LegendStyleNode;
import ca.corefacility.gview.map.gui.editor.node.Slotable;
import ca.corefacility.gview.map.gui.editor.node.SlotsNode;
import ca.corefacility.gview.map.gui.editor.node.StyleEditorNode;

/**
 * This class is responsible for handling the drag and drop actions of the style
 * editor tree.
 * 
 * @author Eric Marinier
 * 
 */
public class StyleTransferHandler extends TransferHandler
{
	private static final long serialVersionUID = 1L; // requested by java

	private final StyleEditorTree styleEditorTree; // the related style editor
													// tree

	private final static SlotOrder slotOrder = new SlotOrder();

	/**
	 * 
	 * @param styleEditorTree
	 *            The related style editor tree.
	 */
	public StyleTransferHandler(StyleEditorTree styleEditorTree)
	{
		super();

		if (styleEditorTree == null)
			throw new IllegalArgumentException("StyleEditorTree is null.");

		this.styleEditorTree = styleEditorTree;
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport support)
	{
		if (support == null)
			throw new IllegalArgumentException("TransferSupport is null.");

		boolean result = false;

		TreePath dropPath = ((JTree.DropLocation) support.getDropLocation()).getPath();

		// Anomaly if dropPath is null.
		if (dropPath == null)
		{
			throw new NullPointerException("Drop path is null.");
		}
		// Dropping a slot under a slots node
		else if (dropPath.getLastPathComponent() instanceof SlotsNode)
		{
			result = canImportSlot(support);
		}
		// Dropping a legend item under a legend style node.
		else if (dropPath.getLastPathComponent() instanceof LegendStyleNode)
		{
			result = canImportLegend(support);
		}
		else
		{
			result = false;
		}

		return result;
	}

	/**
	 * Determines whether or not a slot can be imported.
	 * 
	 * @param support
	 *            The transfer handler support.
	 * @return Whether or not the slot can be imported.
	 */
	private boolean canImportSlot(TransferHandler.TransferSupport support)
	{
		if (support == null)
			throw new IllegalArgumentException("TransferSupport is null.");

		boolean result = false;

		Transferable transferable = support.getTransferable();

		if (transferable.isDataFlavorSupported(SlotableTransferable.getSlotFlavor()))
		{
			result = true;
		}
		else
		{
			result = false;
		}

		return result;
	}

	/**
	 * Determines whether or not a legend can be imported.
	 * 
	 * @param support
	 *            The transfer handler support.
	 * @return Whether or not the legend can be imported.
	 */
	private boolean canImportLegend(TransferHandler.TransferSupport support)
	{
		if (support == null)
			throw new IllegalArgumentException("TransferSupport is null.");

		boolean result = false;

		Transferable transferable = support.getTransferable();

		if (transferable.isDataFlavorSupported(LegendTransferable.getLegendFlavor()))
		{
			result = true;
		}
		else
		{
			result = false;
		}

		return result;
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport support)
	{
		if (support == null)
			throw new IllegalArgumentException("TransferSupport is null.");

		boolean result = false;
		Transferable transferable = support.getTransferable();

		// Slot transfer
		if (transferable.isDataFlavorSupported(SlotableTransferable.getSlotFlavor()))
		{
			result = importSlotData(support);
		}
		// Legend transfer
		else if (transferable.isDataFlavorSupported(LegendTransferable.getLegendFlavor()))
		{
			result = importLegendData(support);
		}

		return result;
	}

	/**
	 * Imports the slot data from the transfer handler support.
	 * 
	 * @param support
	 *            The transfer handler support to pull the transfer data from.
	 * @return The result of the transfer.
	 */
	private boolean importSlotData(TransferHandler.TransferSupport support)
	{
		if (support == null)
			throw new IllegalArgumentException("TransferSupport is null.");

		Transferable transferable = support.getTransferable();

		TreePath parentPath = ((javax.swing.JTree.DropLocation) support.getDropLocation()).getPath();
		int dropLocationIndex = ((JTree.DropLocation) support.getDropLocation()).getChildIndex();

		boolean result = false;
		int source, destination;

		Object transferableData;
		StyleEditorNode parentNode;

		try
		{
			// pull the transfer data
			transferableData = transferable.getTransferData(SlotableTransferable.getSlotFlavor());

			// enforce type
			if (transferableData instanceof Integer)
			{
				// pull the source from the transfer data (the actual slot
				// number)
				source = ((Integer) transferableData).intValue();

				// enforce that we're dropping under the appropriate node
				if (parentPath.getLastPathComponent() instanceof SlotsNode)
				{
					// grab the parent (slots node)
					parentNode = (SlotsNode) parentPath.getLastPathComponent();

					// determine the destination slot number from the drop index
					// enforce type
					if (parentNode.getChildAt(dropLocationIndex) instanceof Slotable)
					{
						destination = ((Slotable) (parentNode.getChildAt(dropLocationIndex))).getSlotNumber();
						result = moveSlot(source, destination);
					}
					else
					{
						throw new ClassCastException("Only SlotableNodes may be children of a SlotsNode.");
					}
				}
				else
				{
					throw new ClassCastException("Was expecting parent node to be an instance of SlotsNode.");
				}
			}
			else
			{
				throw new ClassCastException("Was expecting an integer object returned from transferable.");
			}
		}
		catch (UnsupportedFlavorException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Handles the subtleties for moving slot nodes. This is responsible for
	 * converting 'raw' slot locations into 'intuitive' slot locations that make
	 * sense when actually performing the drag and drop.
	 * 
	 * @param source
	 *            The source node location to move.
	 * @param destination
	 *            The destination location to move.
	 * @return The result of the transfer.
	 */
	private boolean moveSlot(int source, int destination)
	{
		boolean result = false;

		int realSource = slotOrder.convertSource(source, destination);
		int realDestination = slotOrder.convertDestination(source, destination);

		if (realSource != realDestination)
		{
			result = this.styleEditorTree.moveSlot(realSource, realDestination);
		}

		return result;
	}

	public static Comparator<Slotable> getSlotsComparator()
	{
		return slotOrder;
	}

	/**
	 * Imports the legend data from the transfer handler support.
	 * 
	 * @param support
	 *            The transfer handler support to pull the transfer data from.
	 * @return The result of the transfer.
	 */
	private boolean importLegendData(TransferHandler.TransferSupport support)
	{
		if (support == null)
			throw new IllegalArgumentException("TransferSupport is null.");

		Transferable transferable = support.getTransferable();

		TreePath parentPath = ((javax.swing.JTree.DropLocation) support.getDropLocation()).getPath();
		int dropLocationIndex = ((JTree.DropLocation) support.getDropLocation()).getChildIndex();

		boolean result = false;
		int sourceLegendStyleIndex, sourceLegendItemStyleIndex;
		int destinationLegendStyleIndex, destinationLegendItemStyleIndex;

		Object transferableData;
		StyleEditorNode parentNode;

		try
		{
			// pull the transfer data
			transferableData = transferable.getTransferData(LegendTransferable.getLegendFlavor());

			// enforce type
			if (transferableData instanceof LegendTransferableData)
			{
				// pull the source from the transfer data
				sourceLegendStyleIndex = ((LegendTransferableData) transferableData).getLegendStyleIndex();
				sourceLegendItemStyleIndex = ((LegendTransferableData) transferableData).getLegendItemStyleIndex();

				// enforce that we're dropping under the appropriate node
				if (parentPath.getLastPathComponent() instanceof LegendStyleNode)
				{
					parentNode = (LegendStyleNode) parentPath.getLastPathComponent();

					if (dropLocationIndex < 0)
					{
						// dropping on the legend style node.. not at an index
						// location underneath
						// just set the index as zero

						destinationLegendStyleIndex = parentNode.getParent().getIndex(parentNode);
						destinationLegendItemStyleIndex = 0;

						result = moveLegend(sourceLegendStyleIndex, sourceLegendItemStyleIndex,
								destinationLegendStyleIndex, destinationLegendItemStyleIndex);
					}
					else
					{
						// determine the destination slot number from the drop
						// index
						// enforce type
						if (parentNode.getChildAt(dropLocationIndex) instanceof LegendItemNode)
						{
							destinationLegendStyleIndex = parentNode.getParent().getIndex(parentNode);
							destinationLegendItemStyleIndex = dropLocationIndex;

							result = moveLegend(sourceLegendStyleIndex, sourceLegendItemStyleIndex,
									destinationLegendStyleIndex, destinationLegendItemStyleIndex);
						}
						else
						{
							throw new ClassCastException(
									"Only LegendItemStyleNodes may be children of a LegendStyleNode.");
						}
					}
				}
				else
				{
					throw new ClassCastException("Was expecting parent node to be an instance of LegendStyleNode.");
				}
			}
			else
			{
				throw new ClassCastException("Was expecting an integer object returned from transferable.");
			}
		}
		catch (UnsupportedFlavorException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Handles the subtleties for moving legend nodes. This is responsible for
	 * converting 'raw' slot locations into 'intuitive' slot locations that make
	 * sense when actually performing the drag and drop.
	 * 
	 * @param sourceLegendStyleIndex
	 *            The source legend style node index.
	 * @param sourceLegendItemStyleIndex
	 *            The source legend item style node index.
	 * @param destinationLegendStyleIndex
	 *            The destination legend style node index.
	 * @param destinationLegendItemStyleIndex
	 *            The destination legend item style node index.
	 * @return The result of the transfer.
	 */
	private boolean moveLegend(int sourceLegendStyleIndex, int sourceLegendItemStyleIndex,
			int destinationLegendStyleIndex, int destinationLegendItemStyleIndex)
	{
		boolean result = false;

		if (sourceLegendStyleIndex == destinationLegendStyleIndex
				&& sourceLegendItemStyleIndex != destinationLegendItemStyleIndex
				&& sourceLegendItemStyleIndex + 1 != destinationLegendItemStyleIndex)
		{
			result = this.styleEditorTree.moveLegendItemStyle(sourceLegendStyleIndex, sourceLegendItemStyleIndex,
					destinationLegendStyleIndex, destinationLegendItemStyleIndex);
		}
		else if (sourceLegendStyleIndex != destinationLegendStyleIndex)
		{
			result = this.styleEditorTree.moveLegendItemStyle(sourceLegendStyleIndex, sourceLegendItemStyleIndex,
					destinationLegendStyleIndex, destinationLegendItemStyleIndex);
		}

		return result;
	}

	/**
	 * @return The type of action performed.
	 */
	public int getSourceActions(JComponent c)
	{
		return MOVE;
	}

	/**
	 * Creates the transferable object.
	 */
	public Transferable createTransferable(JComponent c)
	{
		if (c == null)
			throw new IllegalArgumentException("JComponent is null.");

		Transferable transferable = null;

		StyleEditorTree styleEditorTree;

		Object node;

		// We want to be working with a style editor tree
		if (c instanceof StyleEditorTree && ((StyleEditorTree) c).getSelectionPath() != null)
		{
			styleEditorTree = (StyleEditorTree) c;
			node = styleEditorTree.getSelectionPath().getLastPathComponent();

			// Slotable node
			if (node instanceof Slotable)
			{
				transferable = createSlotTransferable((Slotable) node);
			}
			// legend style node
			else if (node instanceof LegendItemNode)
			{
				transferable = createLegendTransferable((LegendItemNode) node);
			}
			else
			{
				transferable = null;
			}
		}
		else
		{
			transferable = null;
		}

		return transferable;
	}

	/**
	 * Creates a slot transferable object.
	 * 
	 * @param slotableNode
	 *            The slot node to create the transfer from.
	 * @return The newly created transferable object.
	 */
	private Transferable createSlotTransferable(Slotable slotableNode)
	{
		if (slotableNode == null)
			throw new IllegalArgumentException("SlotableNode is null.");

		int slotNumber = slotableNode.getSlotNumber();

		return new SlotableTransferable(slotNumber);
	}

	/**
	 * Creates a legend transferable object.
	 * 
	 * @param legendItemStyleNode
	 *            The legend node to create the transfer from.
	 * @return The newly created transferable object.
	 */
	private Transferable createLegendTransferable(LegendItemNode legendItemStyleNode)
	{
		if (legendItemStyleNode == null)
			throw new IllegalArgumentException("LegendItemStyleNode is null.");

		Transferable transferable = null;

		int legendStyleIndex;
		int legendItemStyleIndex;

		if (legendItemStyleNode.getParent() != null && legendItemStyleNode.getParent().getParent() != null)
		{
			legendStyleIndex = getIndex(legendItemStyleNode.getParent());
			legendItemStyleIndex = getIndex(legendItemStyleNode);

			if (legendStyleIndex >= 0 && legendItemStyleIndex >= 0)
				transferable = new LegendTransferable(
						new LegendTransferableData(legendStyleIndex, legendItemStyleIndex));
		}
		else
		{
			throw new NullPointerException("Both LegendStyleNode and LegendItemStyleNode must have a parent node.");
		}

		return transferable;
	}

	/**
	 * Finds the index of the node under its parent.
	 * 
	 * @param node
	 *            The node to find the index of.
	 * @return The index of the node under its parent or -1 if the node has no
	 *         parent.
	 */
	private int getIndex(TreeNode node)
	{
		int result = -1;

		TreeNode parentNode;

		if (node != null && node.getParent() != null)
		{
			parentNode = node.getParent();

			result = parentNode.getIndex(node);
		}

		return result;
	}

	/**
	 * Finalizes the export.
	 */
	public void exportDone(JComponent c, Transferable t, int action)
	{
		// nothing currently needs to be done
	}

	/**
	 * Groups together code responsible for defining the order of slots in a
	 * tree.
	 */
	private static class SlotOrder implements Comparator<Slotable>
	{
		@Override
		public int compare(Slotable slotNode1, Slotable slotNode2)
		{
			return slotNode2.getSlotNumber() - slotNode1.getSlotNumber();
		}

		/**
		 * Handles the subtleties for moving slot nodes. Converts a 'raw' source
		 * into a more useful source location.
		 * 
		 * @param source
		 *            The source node location to move.
		 * @param destination
		 *            The destination location to move.
		 * @return The intuitive source
		 */
		public int convertSource(int source, int destination)
		{
			return source;
		}

		/**
		 * Handles the subtleties for moving slot nodes. Converts a 'raw'
		 * destination into a more useful destination location.
		 * 
		 * @param source
		 *            The source node location to move.
		 * @param destination
		 *            The destination location to move.
		 * @return The intuitive destination
		 */
		public int convertDestination(int source, int destination)
		{
			int realDestination = destination;

			if (destination >= 0)
			{
				realDestination = destination + 1;
			}

			return realDestination;
		}
	}
}
