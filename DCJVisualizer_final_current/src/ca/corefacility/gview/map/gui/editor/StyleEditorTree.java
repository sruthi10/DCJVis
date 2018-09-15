package ca.corefacility.gview.map.gui.editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.biojava.bio.seq.FeatureFilter;

import ca.corefacility.gview.map.controllers.FeatureHolderStyleToken;
import ca.corefacility.gview.map.controllers.LegendItemStyleToken;
import ca.corefacility.gview.map.controllers.LegendStyleController;
import ca.corefacility.gview.map.controllers.LegendStyleToken;
import ca.corefacility.gview.map.controllers.PlotStyleController;
import ca.corefacility.gview.map.controllers.PlotStyleToken;
import ca.corefacility.gview.map.controllers.PropertyMapperController;
import ca.corefacility.gview.map.controllers.SetStyleController;
import ca.corefacility.gview.map.controllers.SlotStyleController;
import ca.corefacility.gview.map.controllers.SlotStyleToken;
import ca.corefacility.gview.map.controllers.StyleController;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.controllers.link.Linkable;
import ca.corefacility.gview.map.controllers.selection.Selectable;
import ca.corefacility.gview.map.controllers.selection.SelectionListener;
import ca.corefacility.gview.map.gui.Style;
import ca.corefacility.gview.map.gui.editor.featureFilter.FeatureFilterChooserDialog;
import ca.corefacility.gview.map.gui.editor.node.BackboneSlotNode;
import ca.corefacility.gview.map.gui.editor.node.FeatureContainerNode;
import ca.corefacility.gview.map.gui.editor.node.GlobalNode;
import ca.corefacility.gview.map.gui.editor.node.LegendItemNode;
import ca.corefacility.gview.map.gui.editor.node.LegendNode;
import ca.corefacility.gview.map.gui.editor.node.LegendStyleNode;
import ca.corefacility.gview.map.gui.editor.node.NodeBuilder;
import ca.corefacility.gview.map.gui.editor.node.PlotNode;
import ca.corefacility.gview.map.gui.editor.node.PropertyMapperNode;
import ca.corefacility.gview.map.gui.editor.node.RootNode;
import ca.corefacility.gview.map.gui.editor.node.SetNode;
import ca.corefacility.gview.map.gui.editor.node.SlotNode;
import ca.corefacility.gview.map.gui.editor.node.Slotable;
import ca.corefacility.gview.map.gui.editor.node.SlotsNode;
import ca.corefacility.gview.map.gui.editor.node.StyleEditorNode;
import ca.corefacility.gview.map.gui.editor.panel.RootPanel;
import ca.corefacility.gview.map.gui.editor.panel.StylePanel;
import ca.corefacility.gview.map.gui.editor.transfer.StyleTransferHandler;

/**
 * The style editor tree used in the StyleEditorFrame.
 * 
 * @author Eric Marinier
 * 
 */
public class StyleEditorTree extends JTree implements MouseListener, Comparable<StyleEditorTree>, Selectable
{
	private static final long serialVersionUID = 1L; // requested by java

	private final StyleEditorNode root;

	private final DefaultTreeModel treeModel; // tree model used with the JTree
	private final TreeRightClickMenu rightClickMenu; // the trees right click
														// menu

	private final StyleEditorFrame styleEditorFrame;
	private final Style style;

	private final StyleController styleController;

	private final NodeBuilder nodeBuilder;

	private GlobalNode globalNode;
	private LegendNode legendNode;
	private SlotsNode slotsNode;

	public StyleEditorTree(StyleEditorFrame styleEditorFrame, Style style)
	{
		this.styleEditorFrame = styleEditorFrame;
		this.style = style;

		this.styleController = style.getStyleController();
		this.styleController.getSelectionController().setSelectable(this);

		this.root = new RootNode("Style", new RootPanel());

		// Needs to be before buildNodes() is called.
		this.nodeBuilder = new NodeBuilder(this.styleController, this);

		// Tree properties
		this.setDragEnabled(true);
		this.setDropMode(DropMode.ON_OR_INSERT);
		this.setTransferHandler(new StyleTransferHandler(this));
		this.setScrollsOnExpand(true);

		// Tree rendering (display)
		this.treeModel = new DefaultTreeModel(this.root);
		this.setModel(this.treeModel);
		this.setCellRenderer(new StyleEditorTreeRenderer(this));

		// Allow multiple items to be selected.
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

		// create the nodes
		buildNodes();

		// Expanding the rows makes it look nicer on startup.
		this.expandPath(getPath(this.slotsNode));

		this.addTreeSelectionListener(styleEditorFrame);

		this.rightClickMenu = new TreeRightClickMenu(this.styleEditorFrame, this);
		this.addMouseListener(this);

		this.setRootVisible(false);
		this.setShowsRootHandles(true);
	}

	/**
	 * Builds the tree nodes.
	 */
	private void buildNodes()
	{
		this.globalNode = this.nodeBuilder.createGlobalNode();
		this.slotsNode = this.nodeBuilder.createSlotNodes();
		sortSlotableNodes();

		BackboneSlotNode backboneNode = this.slotsNode.getBackboneNode();
		this.nodeBuilder.createBackboneNode(this.globalNode, backboneNode.getPanel());

		this.legendNode = this.nodeBuilder.createLegendNode(this.globalNode);

		this.nodeBuilder.createRulerNode(this.globalNode);
		this.nodeBuilder.createTooltipNode(this.globalNode);
	}

	/**
	 * Updates all of the nodes in the tree, which will forward the message to
	 * their panels.
	 */
	public void updateTree()
	{
		Enumeration<StyleEditorNode> nodes = this.breadthFirstEnumeration();
		StyleEditorNode current;

		while (nodes.hasMoreElements())
		{
			current = nodes.nextElement();
			current.update();
			this.treeModel.nodeChanged(current);
		}
	}

	/**
	 * Updates the visual rendering of the tree nodes. This causes the tree
	 * renderer to update the appearance.
	 * 
	 * Note: This update may not happen immediately. It will likely be scheduled
	 * on the EDT.
	 */
	public void updateTreeRendering()
	{
		Enumeration<StyleEditorNode> nodes = this.breadthFirstEnumeration();
		StyleEditorNode current;

		while (nodes.hasMoreElements())
		{
			current = nodes.nextElement();
			if (current instanceof Slotable)
			{
				((Slotable) current).updateName();
			}

			this.treeModel.nodeChanged(current);
		}

		this.repaint();
	}

	/**
	 * 
	 * @return The root node of the tree.
	 */
	public StyleEditorNode getRoot()
	{
		if (this.root == null)
			throw new NullPointerException("StyleTree's root is null.");

		return this.root;
	}

	/**
	 * Checks to make sure that all children of the tree and style editor nodes
	 * and returns a breadth first enumeration of the nodes.
	 */
	@SuppressWarnings("unchecked")
	public Enumeration<StyleEditorNode> breadthFirstEnumeration()
	{
		Enumeration<Object> objectEnum = this.root.breadthFirstEnumeration();

		while (objectEnum.hasMoreElements())
		{
			if (!(objectEnum.nextElement() instanceof StyleEditorNode))
			{
				throw new ClassCastException("Non-StyleEditorNode a child of StyleEditorTree.");
			}
		}

		return this.root.breadthFirstEnumeration();
	}

	/**
	 * Adds the slotable node to the root node.
	 * 
	 * @param slotableNode
	 *            The slotable node to add.
	 */
	private void addSlotableNode(Slotable slotableNode)
	{
		if (slotableNode == null)
			throw new IllegalArgumentException("SlotableNode is null.");

		this.treeModel.insertNodeInto(slotableNode, this.slotsNode, this.slotsNode.getChildCount());
		updateSlotableNodes();
	}

	/**
	 * Adds the set node to the passed parent node.
	 * 
	 * @param setNode
	 *            The set node to add.
	 * @param parentNode
	 *            The parent node to add the set node to.
	 */
	private void addSetNode(SetNode setNode, FeatureContainerNode parentNode)
	{
		if (setNode == null)
			throw new IllegalArgumentException("SetNode is null.");

		if (parentNode == null)
			throw new IllegalArgumentException("FeatureContainerNode is null.");

		this.treeModel.insertNodeInto(setNode, parentNode, parentNode.getChildCount());

		if (parentNode instanceof SlotNode)
		{
			((SlotNode) parentNode).updateName();
		}
	}

	/**
	 * Adds the plot node to the passed parent node.
	 * 
	 * @param plotNode
	 *            The plot node to add.
	 * @param parentNode
	 *            The parent node to add the plot node to.
	 */
	private void addPlotNode(PlotNode plotNode, SlotNode parentNode)
	{
		if (plotNode == null)
			throw new IllegalArgumentException("PlotNode is null.");

		if (parentNode == null)
			throw new IllegalArgumentException("StyleEditorNode is null.");

		this.treeModel.insertNodeInto(plotNode, parentNode, parentNode.getChildCount());

		if (parentNode instanceof SlotNode)
		{
			((SlotNode) parentNode).updateName();
		}
	}

	/**
	 * Creates a new, default and empty slot node and adds it to the root node.
	 */
	public void createSlotNode()
	{
		SlotStyleController slotStyleController = this.styleController.getSlotStyleController();
		SlotStyleToken slotStyle;

		SlotNode slotNode;

		Integer tempNumber;
		int slotNumber;

		int lowerBounds = slotStyleController.getLowerSlotNumber() - 1;
		int upperBounds = slotStyleController.getUpperSlotNumber() + 1;

		SlotSelectionDialog dialog = new SlotSelectionDialog(lowerBounds, upperBounds);

		dialog.setLocationRelativeTo(this.styleEditorFrame);
		dialog.setVisible(true); // This will block the current thread until the
									// dialog is hidden.

		tempNumber = dialog.getSlotNumber();

		if (tempNumber != null)
		{
			slotNumber = tempNumber.intValue();

			if (slotStyleController.validNewSlotNumber(slotNumber))
			{
				slotStyle = slotStyleController.createSlot(slotNumber);
				slotNode = this.nodeBuilder.createSlotNode(slotStyle);

				addSlotableNode(slotNode);
				this.setSelectionPath(getPath(slotNode));
			}
		}
	}

	/**
	 * 
	 * @param slotNumber
	 *            The number of the slot node to return.
	 * 
	 * @return The specified slot node.
	 */
	private SlotNode getSlotNode(Integer slotNumber)
	{
		Enumeration<Slotable> slots = this.slotsNode.children();
		SlotNode result = null;

		while (slots.hasMoreElements())
		{
			Slotable slot = slots.nextElement();

			if (slot.getSlotNumber() == slotNumber && slot instanceof SlotNode)
			{
				result = (SlotNode) slot;
			}
		}

		return result;
	}

	public void createSetNode(Integer slotNumber)
	{
		this.createSetNode(this.getSlotNode(slotNumber));
	}

	/**
	 * Creates a set node under the passed parent node.
	 * 
	 * @param parentNode
	 *            The node to add the set node to.
	 */
	public void createSetNode(FeatureContainerNode parentNode)
	{
		if (parentNode == null)
			throw new IllegalArgumentException("StyleEditorNode is null.");

		if (parentNode.canAddSetNodeAsChild())
		{
			(new FeatureFilterChooserDialog(this, this.styleController.getGenomeDataController(), parentNode,
					FeatureFilterChooserDialog.DIALOG_TYPE.CREATE_SET)).showDialog(this.styleEditorFrame);
		}
		else
		{
			JOptionPane.showMessageDialog(this.styleEditorFrame, "Cannot add set node.");
		}
	}

	/**
	 * Refines the passed set node.
	 * 
	 * @param setNode
	 *            The set node to refine.
	 */
	public void refineSetNode(SetNode setNode)
	{
		if (setNode == null)
			throw new IllegalArgumentException("SetNode is null.");

		(new FeatureFilterChooserDialog(this, this.styleController.getGenomeDataController(), setNode,
				FeatureFilterChooserDialog.DIALOG_TYPE.REFINE_SET)).showDialog(this.styleEditorFrame);

	}

	/**
	 * Creates a set node from the passed feature filter and adds it under the
	 * passed parent node.
	 * 
	 * @param parentNode
	 *            The parent node of the new set node.
	 * @param featureFilter
	 *            The feature filter to use in the new set node.
	 */
	public void createSetNodeFromFeatureFilter(FeatureContainerNode parentNode, FeatureFilter featureFilter)
	{
		if (parentNode == null)
			throw new IllegalArgumentException("StyleEditorNode is null.");

		if (featureFilter == null)
			throw new IllegalArgumentException("FeatureFilter is null.");

		SetStyleController setStyleController = this.styleController.getSetStyleController();

		FeatureHolderStyleToken setStyle;
		SetNode setNode;

		if (parentNode.canAddSetNodeAsChild())
		{
			if (parentNode instanceof SlotNode)
			{
				setStyle = setStyleController.createSet(((SlotNode) parentNode).getSlotStyle(), featureFilter);
			}
			else if (parentNode instanceof SetNode)
			{
				setStyle = setStyleController.createSet(((SetNode) parentNode).getSetStyle(), featureFilter);
			}
			else
			{
				throw new IllegalArgumentException("Unrecognized parent node class");
			}

			setNode = this.nodeBuilder.createSetNode(setStyle);

			addSetNode(setNode, parentNode);
			this.setSelectionPath(getPath(setNode));
		}
	}

	/**
	 * Creates a plot node, if possible, at the passed slot number.
	 * 
	 * @param slotNumber
	 *            The number of the slot to create a plot inside.
	 */
	public void createPlotNode(Integer slotNumber)
	{
		this.createPlotNode(this.getSlotNode(slotNumber));
	}

	/**
	 * Creates a new default plot node and adds it to the passed slot node.
	 * 
	 * @param slotNode
	 *            The slot node to add the plot node to.
	 */
	public void createPlotNode(SlotNode slotNode)
	{
		if (slotNode == null)
			throw new IllegalArgumentException("SlotNode is null.");

		SlotStyleController slotStyleController = this.styleController.getSlotStyleController();

		PlotStyleToken plotStyle;
		PlotNode plotNode;

		SlotStyleToken slotStyle;

		// if the slot node contains no plot node AND no set node(s) as a child
		// already
		if (!slotNode.containsPlotNode() && !slotNode.containsSetNode())
		{
			slotStyle = slotNode.getSlotStyle();
			plotStyle = slotStyleController.createPlot(slotStyle);

			plotNode = this.nodeBuilder.createPlotNode(plotStyle);

			plotNode.update();

			addPlotNode(plotNode, slotNode);
			this.setSelectionPath(getPath(plotNode));
		}
		// there is a plot node already as a child of the slot node
		else
		{
			JOptionPane.showMessageDialog(this.styleEditorFrame,
					"The slot node already contains a plot node or set nodes.");
		}
	}

	/**
	 * Collapses all the tree's nodes recursively.
	 */
	public void collapse()
	{
		collapse(this.slotsNode);
		collapse(this.globalNode);

		this.setSelectionPath(null);
	}

	/**
	 * Recursively collapses the node and its children.
	 * 
	 * @param node
	 *            The node to collapse.
	 */
	private void collapse(TreeNode node)
	{
		for (int i = 0; i < node.getChildCount(); i++)
		{
			collapse(node.getChildAt(i));
		}

		collapsePath(getPath(node));
	}

	/**
	 * Selects all the nodes in the tree that are of the passed class type.
	 * 
	 * @param type
	 *            The class of nodes to select.
	 */
	public void selectAllNodes(Class<? extends StyleEditorNode> type)
	{
		Enumeration<StyleEditorNode> nodes = this.breadthFirstEnumeration();
		StyleEditorNode node;

		ArrayList<TreePath> paths = new ArrayList<TreePath>();
		TreePath[] arrayPaths;

		while (nodes.hasMoreElements())
		{
			node = nodes.nextElement();

			if (node.getClass().equals(type))
			{
				paths.add(getPath(node));
			}
		}

		arrayPaths = new TreePath[paths.size()];
		this.setSelectionPaths(paths.toArray(arrayPaths));
	}

	/**
	 * Selects all of the nodes within the subtrees of the selection of the
	 * specified type.
	 * 
	 * @param type
	 *            The type of node to select.
	 */
	public void selectNodesWithinSelection(Class<? extends StyleEditorNode> type)
	{
		TreePath[] selectionPaths = this.getSelectionPaths();
		ArrayList<StyleEditorNode> nodes = new ArrayList<StyleEditorNode>();

		// Get all the selected nodes:
		for (TreePath selectedPath : selectionPaths)
		{
			Object current = selectedPath.getLastPathComponent();

			if (current instanceof StyleEditorNode)
			{
				nodes.add((StyleEditorNode) current);
			}
		}

		ArrayList<TreePath> paths = new ArrayList<TreePath>();
		TreePath[] arrayPaths;

		for (StyleEditorNode node : nodes)
		{
			paths.addAll(getNodesWithinSelection(type, node));
		}

		arrayPaths = new TreePath[paths.size()];
		this.setSelectionPaths(paths.toArray(arrayPaths));
	}

	/**
	 * Selects all of the nodes within the subtree of the node of the specified
	 * type.
	 * 
	 * @param type
	 *            The type of node to select.
	 */
	private ArrayList<TreePath> getNodesWithinSelection(Class<? extends StyleEditorNode> type, StyleEditorNode topNode)
	{
		@SuppressWarnings("rawtypes")
		Enumeration nodes = topNode.breadthFirstEnumeration();

		ArrayList<TreePath> paths = new ArrayList<TreePath>();

		while (nodes.hasMoreElements())
		{
			Object node = nodes.nextElement();

			if (node instanceof StyleEditorNode)
			{
				if (node.getClass().equals(type))
				{
					paths.add(getPath((StyleEditorNode) node));
				}
			}
		}

		return paths;
	}

	/**
	 * Selects all of the nodes within the tree that have the specified
	 * criteria.
	 * 
	 * @param links
	 *            The link selection criteria.
	 * @param type
	 *            The type of node to select.
	 */
	public void selectNodesWithLink(ArrayList<Link> links, Class<? extends StyleEditorNode> type)
	{
		Enumeration<StyleEditorNode> nodes = this.breadthFirstEnumeration();
		ArrayList<TreePath> paths = new ArrayList<TreePath>();
		TreePath[] arrayPaths;

		// Iterate over all the nodes:
		while (nodes.hasMoreElements())
		{
			StyleEditorNode node = nodes.nextElement();

			// Linkable and matches?
			if (node.getClass().equals(type) && node instanceof Linkable && links.contains(((Linkable) node).getLink()))
			{
				paths.add(this.getPath(node));
			}
		}

		arrayPaths = new TreePath[paths.size()];
		this.setSelectionPaths(paths.toArray(arrayPaths));
	}

	/**
	 * 
	 * @param path
	 * @return The nodes that are currently expanded beneath the passed path.
	 */
	public ArrayList<TreeNode> getExpandedDescendantNodes(TreePath path)
	{
		// The paths of expanded children:
		Enumeration<TreePath> expandedPaths = this.getExpandedDescendants(path);
		TreePath current;

		// The nodes of the expanded children (because the paths will change):
		ArrayList<TreeNode> expandedNodes = new ArrayList<TreeNode>();
		Object child;

		// Get the expanded nodes:
		while (expandedPaths != null && expandedPaths.hasMoreElements())
		{
			current = expandedPaths.nextElement();

			child = current.getLastPathComponent();

			if (child instanceof TreeNode)
			{
				expandedNodes.add((TreeNode) child);
			}
		}

		return expandedNodes;
	}

	/**
	 * Sorts the slot nodes in the tree according to their slot number.
	 * 
	 * Should only really be called by updateSlotableNodes().
	 */
	private void sortSlotableNodes()
	{
		// The nodes of the expanded children (because the paths will change):
		ArrayList<TreeNode> expandedNodes = getExpandedDescendantNodes(getPath(this.slotsNode));

		Enumeration<Slotable> nodes = this.slotsNode.children();
		Slotable currentNode;

		ArrayList<Slotable> slotNodeList = new ArrayList<Slotable>();

		// remove slot nodes from tree; insert them (sorted) into array list
		while (nodes.hasMoreElements())
		{
			currentNode = nodes.nextElement();
			slotNodeList.add(currentNode);
		}

		// remove
		for (int i = 0; i < slotNodeList.size(); i++)
			this.treeModel.removeNodeFromParent(slotNodeList.get(i));

		// sort
		Slotable[] slotNodeArray = new Slotable[slotNodeList.size()];
		Arrays.sort(slotNodeList.toArray(slotNodeArray), StyleTransferHandler.getSlotsComparator());

		// re-add the nodes
		for (int i = 0; i < slotNodeArray.length; i++)
		{
			this.treeModel.insertNodeInto(slotNodeArray[i], this.slotsNode, this.slotsNode.getChildCount());
		}

		// Expand nodes:
		// The slots node itself:
		this.expandPath(getPath(this.slotsNode));

		// Expand nodes that were expanded underneath:
		for (TreeNode node : expandedNodes)
		{
			this.expandPath(getPath(node));
		}
	}

	/**
	 * Removes the slot node from the tree.
	 * 
	 * @param slotNode
	 *            The slot node to remove.
	 */
	public void removeSlotNode(SlotNode slotNode)
	{
		if (slotNode == null)
			throw new IllegalArgumentException("SlotNode is null.");

		SlotStyleToken slotStyle = slotNode.getSlotStyle();

		SlotStyleController slotStyleController = this.styleController.getSlotStyleController();
		slotStyleController.removeSlot(slotStyle);

		updateSlotableNodes();

		selectNodesParent(slotNode);
		this.nodeBuilder.removeNode(slotNode);
	}

	/**
	 * Removes the plot node from the tree.
	 * 
	 * @param plotNode
	 *            The plot node to remove.
	 */
	public void removePlotNode(PlotNode plotNode)
	{
		if (plotNode == null)
			throw new IllegalArgumentException("PlotNode is null.");

		PlotStyleController plotStyleController = this.styleController.getPlotStyleController();

		SlotNode slotNode = plotNode.getParent();
		SlotStyleToken slotStyle = slotNode.getSlotStyle();

		PlotStyleToken plotStyle = plotNode.getPlotStyle();

		plotStyleController.removePlot(slotStyle, plotStyle);
		selectNodesParent(plotNode);
		this.nodeBuilder.removeNode(plotNode);
	}

	/**
	 * Removes the set node from the tree.
	 * 
	 * @param setNode
	 *            The set node to remove.
	 */
	public void removeSetNode(SetNode setNode)
	{
		if (setNode == null)
			throw new IllegalArgumentException("SetNode is null.");

		FeatureHolderStyleToken removedStyle = setNode.getSetStyle();
		FeatureContainerNode parentNode = setNode.getParent();

		if (parentNode instanceof SetNode)
		{
			FeatureHolderStyleToken setStyle = ((SetNode) parentNode).getSetStyle();
			this.styleController.getSetStyleController().removeSet(setStyle, removedStyle);
		}
		else if (parentNode instanceof SlotNode)
		{
			SlotStyleToken slotStyle = ((SlotNode) parentNode).getSlotStyle();
			this.styleController.getSlotStyleController().removeSet(slotStyle, removedStyle);
		}

		selectNodesParent(setNode);
		this.nodeBuilder.removeNode(setNode);
		this.updateTreeRendering();
	}

	/**
	 * Creates a new default legend node and adds it to the tree under the
	 * legend node. Adds the new legend style to the GView map.
	 */
	public void createLegendNode()
	{
		LegendStyleController legendStyleController = this.styleController.getLegendStyleController();

		LegendStyleToken legendStyle = legendStyleController.createLegend();
		LegendStyleNode legendStyleNode = this.nodeBuilder.createLegendStyleNode(this.legendNode, legendStyle);

		if (this.legendNode == null)
		{
			throw new NullPointerException("LegendNode is null.");
		}

		this.treeModel.nodesWereInserted(this.legendNode, new int[] { this.legendNode.getIndex(legendStyleNode) });
		this.setSelectionPath(getPath(legendStyleNode));
	}

	/**
	 * Creates a default legend item style node and adds it under the passed
	 * parent node. Adds the legend item style to the GView map.
	 * 
	 * @param parentNode
	 *            The parent node of the new legend item style node.
	 */
	public void createLegendItemNode(LegendStyleNode parentNode)
	{

		if (parentNode == null)
		{
			throw new IllegalArgumentException("Parent node is null");
		}
		else
		{
			LegendStyleController legendStyleController = this.styleController.getLegendStyleController();

			LegendStyleToken legendStyle = parentNode.getLegendStyle();
			LegendItemStyleToken legendItemStyle = legendStyleController.createLegendItem(legendStyle);

			LegendItemNode legendItemStyleNode = this.nodeBuilder.createLegendItemStyleNode(parentNode, legendStyle,
					legendItemStyle);

			this.treeModel.nodesWereInserted(parentNode, new int[] { parentNode.getIndex(legendItemStyleNode) });
			this.setSelectionPath(getPath(legendItemStyleNode));
		}
	}

	/**
	 * Removes the legend style node from the tree. Removes the legend style
	 * from the GView map.
	 * 
	 * @param legendStyleNode
	 *            The node to remove.
	 */
	public void removeLegend(LegendStyleNode legendStyleNode)
	{
		if (legendStyleNode == null)
		{
			throw new IllegalArgumentException("LegendStyleNode is null.");
		}
		else
		{
			LegendStyleController legendStyleController = this.styleController.getLegendStyleController();
			LegendStyleToken legendStyle = legendStyleNode.getLegendStyle();

			// remove legend
			if (legendStyleController.removeLegend(legendStyle))
			{
				// successful remove
				selectNodesParent(legendStyleNode);
				this.nodeBuilder.removeNode(legendStyleNode);
			}
		}
	}

	public void removeLegendItem(LegendItemNode legendItemStyleNode)
	{
		if (legendItemStyleNode == null)
		{
			throw new IllegalArgumentException("LegendStyleNode is null.");
		}
		else
		{
			LegendStyleController legendStyleController = this.styleController.getLegendStyleController();

			LegendStyleNode parentNode = legendItemStyleNode.getParent();
			LegendStyleToken legendStyle = parentNode.getLegendStyle();

			LegendItemStyleToken legendItemStyle = legendItemStyleNode.getLegendItemSyle();

			// remove
			if (legendStyleController.removeLegendItem(legendStyle, legendItemStyle))
			{
				// successful remove
				selectNodesParent(legendItemStyleNode);
				this.nodeBuilder.removeNode(legendItemStyleNode);
			}
		}
	}

	/**
	 * Returns the tree model.
	 * 
	 * @return The tree model.
	 */
	public DefaultTreeModel getTreeModel()
	{
		if (this.treeModel == null)
			throw new NullPointerException("TreeModel is null.");

		return this.treeModel;
	}

	/**
	 * Returns the name of the tree.
	 * 
	 * @return The name of the tree.
	 */
	public String getStyleName()
	{
		return this.style.getName();
	}

	/**
	 * Sets the name of the style tree. This will not update any graphical
	 * components that rely on the style tree's name.
	 * 
	 * @param name
	 *            The name to set.
	 */
	public void setTreeName(String name)
	{
		this.style.setName(name);
	}

	/**
	 * Moves the source slot to the destination slot. The source slot will
	 * replace the destination slot's position and the destination slot and all
	 * slots farther away from the backbone will be pushed away one slot. The
	 * gap that was created from moving the source slot will be collapsed as
	 * appropriate.
	 * 
	 * @param source
	 *            The slot number of the source slot.
	 * @param destination
	 *            The slot number of the destination slot.
	 * @return The result of the move.
	 */
	public boolean moveSlot(int source, int destination)
	{
		boolean result = false;

		SlotStyleController slotStyleController = this.styleController.getSlotStyleController();

		// backbone source
		if (source == SlotStyleController.BACKBONE_SLOT_NUMBER && slotStyleController.validNewSlotNumber(destination)
				&& source != destination)
		{
			slotStyleController.moveBackbone(destination);
			updateSlotableNodes();

			result = true;
		}
		// non-backbone source
		else if (slotStyleController.validNewSlotNumber(source) && slotStyleController.validNewSlotNumber(destination)
				&& source != destination)
		{
			slotStyleController.moveSlot(source, destination);
			updateSlotableNodes();

			result = true;
		}

		return result;
	}

	/**
	 * Updates all slotable nodes under slots node in the tree and sorts them so
	 * they remain in order.
	 */
	private void updateSlotableNodes()
	{
		Enumeration<Slotable> slotsNodeChildren = this.slotsNode.children();
		Slotable tempSlotableNode;

		// update slot node names
		while (slotsNodeChildren.hasMoreElements())
		{
			tempSlotableNode = slotsNodeChildren.nextElement();
			tempSlotableNode.updateName();
		}

		// sort nodes
		sortSlotableNodes();
	}

	// Returns a TreePath containing the specified node.
	public TreePath getPath(TreeNode node)
	{
		List<TreeNode> list = new ArrayList<TreeNode>();

		// Add all nodes to list
		while (node != null)
		{
			list.add(node);
			node = node.getParent();
		}
		Collections.reverse(list);

		// Convert array of nodes to TreePath
		return new TreePath(list.toArray());
	}

	/**
	 * Moves the location of the source legend item style to the destination.
	 * 
	 * @param sourceLegendStyleIndex
	 *            The source legend style index of the node to move.
	 * @param sourceLegendItemStyleIndex
	 *            The source legend item style index of the node to move.
	 * @param destinationLegendStyleIndex
	 *            The destination legend style index of the node to move.
	 * @param destinationLegendItemStyleIndex
	 *            The destination legend item style index of the node to move.
	 * @return The result of the transfer.
	 */
	public boolean moveLegendItemStyle(int sourceLegendStyleIndex, int sourceLegendItemStyleIndex,
			int destinationLegendStyleIndex, int destinationLegendItemStyleIndex)
	{
		boolean result = false;

		LegendStyleController legendStyleController = this.styleController.getLegendStyleController();

		LegendStyleNode sourceLegendStyleNode;
		LegendStyleNode destinationLegendStyleNode;
		LegendItemNode sourceLegendItemStyleNode;

		// Check for valid indices
		if (sourceLegendStyleIndex < this.legendNode.getChildCount()
				&& destinationLegendStyleIndex < this.legendNode.getChildCount() && sourceLegendStyleIndex >= 0
				&& destinationLegendStyleIndex >= 0)
		{
			// grab the parent nodes
			sourceLegendStyleNode = (LegendStyleNode) this.legendNode.getChildAt(sourceLegendStyleIndex);
			destinationLegendStyleNode = (LegendStyleNode) this.legendNode.getChildAt(destinationLegendStyleIndex);

			// null check and index check
			if (sourceLegendStyleNode != null && destinationLegendStyleNode != null
					&& sourceLegendItemStyleIndex < sourceLegendStyleNode.getChildCount()
					&& destinationLegendItemStyleIndex <= destinationLegendStyleNode.getChildCount()
					// <= (less than or equal) so that we can add to an empty
					// node
					&& sourceLegendItemStyleIndex >= 0 && destinationLegendItemStyleIndex >= 0)
			{
				// grab the actual node to move
				sourceLegendItemStyleNode = (LegendItemNode) sourceLegendStyleNode
						.getChildAt(sourceLegendItemStyleIndex);

				legendStyleController.moveLegendItem(sourceLegendStyleIndex, sourceLegendItemStyleIndex,
						destinationLegendStyleIndex, destinationLegendItemStyleIndex);

				// move the nodes themselves
				this.treeModel.removeNodeFromParent(sourceLegendItemStyleNode);

				if (sourceLegendStyleIndex == destinationLegendStyleIndex
						&& sourceLegendItemStyleIndex <= destinationLegendItemStyleIndex)
				{
					// adjust the destination index because of the remove (one
					// less item now)
					// but ONLY if the source is less than or equal to the
					// destination
					this.treeModel.insertNodeInto(sourceLegendItemStyleNode, destinationLegendStyleNode,
							destinationLegendItemStyleIndex - 1);
				}
				else
				{
					// normal
					this.treeModel.insertNodeInto(sourceLegendItemStyleNode, destinationLegendStyleNode,
							destinationLegendItemStyleIndex);
				}

				result = true;
			}
		}

		return result;
	}

	/**
	 * Removes the property mapper node from its parent node and from its slot
	 * style or feature holder style.
	 * 
	 * @param propertyMapperNode
	 *            The node to remove.
	 */
	public void removePropertyMapper(PropertyMapperNode propertyMapperNode)
	{
		if (propertyMapperNode == null)
		{
			throw new IllegalArgumentException("PropertyMapperNode is null.");
		}

		FeatureContainerNode parentNode = propertyMapperNode.getParent();
		PropertyMapperController propertyMapperController = this.styleController.getPropertyMapperController();

		if (parentNode instanceof SlotNode)
		{
			propertyMapperController.removePropertyStyleMapper(((SlotNode) parentNode).getSlotStyle());
		}
		else if (parentNode instanceof SetNode)
		{
			propertyMapperController.removePropertyStyleMapper(((SetNode) parentNode).getSetStyle());
		}

		selectNodesParent(propertyMapperNode);
		this.nodeBuilder.removeNode(propertyMapperNode);
	}

	/**
	 * Sets the trees selection to the passed node's parent.
	 * 
	 * In the unlikely circumstance that the node's parent is null (which is
	 * shouldn't be), the root node is selected instance.
	 * 
	 * @param node
	 *            The node whose parent should be selected.
	 */
	private void selectNodesParent(TreeNode node)
	{
		// Change selection to the node's parent
		if (node.getParent() != null)
			this.setSelectionPath(this.getPath(node.getParent()));
		else if (this.root != null)
			this.setSelectionPath(this.getPath(this.root));
	}

	/**
	 * Saves the current style.
	 */
	public void saveCurrentStyle()
	{
		Enumeration<StyleEditorNode> nodes = this.breadthFirstEnumeration();

		while (nodes.hasMoreElements())
		{
			JPanel currPanel = nodes.nextElement().getPanel();

			if (currPanel instanceof StylePanel)
			{
				((StylePanel) currPanel).save();
			}
		}
	}

	/**
	 * 
	 * @return The style associated with the tree.
	 */
	public Style getStyle()
	{
		return this.style;
	}

	/**
	 * Selects the root of the tree.
	 */
	public void selectRoot()
	{
		this.setSelectionPath(new TreePath(this.root));
	}

	/**
	 * Creates a property mapper.
	 * 
	 * @param node
	 */
	public void createPropertyMapper(FeatureContainerNode node)
	{
		PropertyMapperNode propertyMapperNode;

		// Create a property mapper underneath this node!
		if (node instanceof SetNode)
		{
			FeatureHolderStyleToken setToken = ((SetNode) node).getSetStyle();
			propertyMapperNode = this.nodeBuilder.createPropertyMapperNode(node, setToken);

			this.treeModel.nodesWereInserted(node, new int[] { node.getIndex(propertyMapperNode) });
			this.setSelectionPath(getPath(propertyMapperNode));
		}
		else if (node instanceof SlotNode)
		{
			SlotStyleToken slotToken = ((SlotNode) node).getSlotStyle();
			propertyMapperNode = this.nodeBuilder.createPropertyMapperNode(node, slotToken);

			this.treeModel.nodesWereInserted(node, new int[] { node.getIndex(propertyMapperNode) });
			this.setSelectionPath(getPath(propertyMapperNode));
		}
	}

	/**
	 * Refines a given set node with a feature filter by performing a logical
	 * AND operation with the current filter of the passed set node.
	 * 
	 * @param setNode
	 * @param featureFilter
	 */
	public void refineSetNodeFromFeatureFilter(SetNode setNode, FeatureFilter featureFilter)
	{
		FeatureHolderStyleToken style = setNode.getSetStyle();

		this.styleController.getSetStyleController().refine(style, featureFilter);
		setNode.rename(this.styleController.getSetStyleController().generateDefaultName(style));

		this.getTreeModel().nodeChanged((TreeNode) setNode);
	}

	/**
	 * Finds the set node that shares the target style in the tree, if it
	 * exists.
	 * 
	 * @param target
	 *            The set style to search for.
	 * @return The set node corresponding to the style or NULL if not found.
	 */
	public SetNode findSetNode(FeatureHolderStyleToken target)
	{
		Enumeration<Slotable> slots = this.slotsNode.children();
		Slotable slotable;

		SetNode result = null;

		while (target != null && slots.hasMoreElements() && result == null)
		{
			slotable = slots.nextElement();

			if (slotable instanceof SlotNode)
			{
				result = searchSlotNodeForSet((SlotNode) slotable, target);
			}
		}

		return result;
	}

	/**
	 * Searches the slot node for the target set style.
	 * 
	 * @param slotNode
	 *            The slot slot node search.
	 * @param target
	 *            The set style to search for.
	 * @return The set node corresponding to the style or NULL if not found.
	 */
	private SetNode searchSlotNodeForSet(SlotNode slotNode, FeatureHolderStyleToken target)
	{
		@SuppressWarnings("rawtypes")
		Enumeration nodes = slotNode.children();
		Object node;

		SetNode result = null;

		while (target != null && nodes.hasMoreElements() && result == null)
		{
			node = nodes.nextElement();

			if (node instanceof SetNode)
			{
				result = searchSetNodeForSet((SetNode) node, target);
			}
		}

		return result;
	}

	/**
	 * Searches the set node recursively for the target set style.
	 * 
	 * @param setNode
	 *            The set node to search.
	 * @param target
	 *            The set style to search for.
	 * @return The set node corresponding to the style or NULL if not found.
	 */
	private SetNode searchSetNodeForSet(SetNode setNode, FeatureHolderStyleToken target)
	{
		@SuppressWarnings("rawtypes")
		Enumeration nodes = setNode.children();
		Object node;

		SetNode result = null;

		// Have we found the node?
		// Yes:
		if (setNode.getSetStyle().hasSameStyleReference(target))
		{
			return setNode;
		}
		// No:
		// Recurse
		else
		{
			while (nodes.hasMoreElements() && result == null)
			{
				node = nodes.nextElement();

				if (node instanceof SetNode)
				{
					result = searchSetNodeForSet((SetNode) node, target);
				}
			}
		}

		return result;
	}

	/**
	 * Finds the legend item node that shares the target style in the tree, if
	 * it exists.
	 * 
	 * @param target
	 *            The legend item style to search for.
	 * @return The legend item node corresponding to the style or NULL if not
	 *         found.
	 */
	public LegendItemNode findLegendNode(LegendItemStyleToken target)
	{
		@SuppressWarnings("rawtypes")
		Enumeration legends = this.legendNode.children();
		Object node;

		LegendItemNode result = null;

		while (legends.hasMoreElements() && result == null)
		{
			node = legends.nextElement();

			if (node instanceof LegendStyleNode)
			{
				result = searchLegendStyleNodeForLegendItem((LegendStyleNode) node, target);
			}
		}

		return result;
	}

	/**
	 * Searches the legend style node for the target style.
	 * 
	 * @param legendStyleNode
	 *            The legend style node to search.
	 * @param target
	 *            The legend item style to search for.
	 * @return The legend item node corresponding to the style or NULL if not
	 *         found.
	 */
	private LegendItemNode searchLegendStyleNodeForLegendItem(LegendStyleNode legendStyleNode,
			LegendItemStyleToken target)
	{
		@SuppressWarnings("rawtypes")
		Enumeration nodes = legendStyleNode.children();
		Object node;

		LegendItemNode result = null;

		while (nodes.hasMoreElements() && result == null)
		{
			node = nodes.nextElement();

			if (node instanceof LegendItemNode
					&& ((LegendItemNode) node).getLegendItemSyle().sameStyleReference(target))
			{
				result = (LegendItemNode) node;
			}
		}

		return result;
	}

	/**
	 * 
	 * @return The lower slot number.
	 */
	public Integer getLowerSlot()
	{
		return this.styleController.getSlotStyleController().getLowerSlotNumber();
	}

	/**
	 * 
	 * @return The upper slot number.
	 */
	public Integer getUpperSlot()
	{
		return this.styleController.getSlotStyleController().getUpperSlotNumber();
	}

	/**
	 * 
	 * @param treePaths
	 * @return Whether or not the selection of tree paths has a linkable object
	 *         that has a link.
	 */
	public static boolean hasLink(TreePath[] treePaths)
	{
		boolean hasLink = false;

		// Is the selection linkable?
		for (int i = 0; i < treePaths.length && !hasLink; i++)
		{
			Object node = treePaths[i].getLastPathComponent();

			// Is it linkable?
			if (node instanceof SlotNode)
			{
				List<Link> links = ((SlotNode) node).getLinks();

				if (links.size() > 0)
				{
					hasLink = true;
				}
			}
			if (node instanceof Linkable && ((Linkable) node).getLink() != null)
			{
				hasLink = true;
			}
		}

		return hasLink;
	}

	// ---------- Listeners ----------//

	@Override
	public void mousePressed(MouseEvent e)
	{
		maybeShowPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		maybeShowPopup(e);
	}

	/**
	 * Decides whether or not to show the popup menu.
	 * 
	 * @param e
	 */
	private void maybeShowPopup(MouseEvent e)
	{
		TreePath selectedPath;

		if (e.isPopupTrigger())
		{
			selectedPath = this.getPathForLocation(e.getX(), e.getY());

			// Did we actually click on a node?
			if (selectedPath != null)
			{
				// Handling how a user would expect the path selection to work:
				if (e.isControlDown())
				{
					this.addSelectionPath(selectedPath);
				}
				else
				{
					if (!this.isPathSelected(selectedPath))
					{
						this.setSelectionPath(selectedPath);
					}
				}

				if (this.getSelectionCount() == 1)
				{
					this.rightClickMenu.show(e.getX(), e.getY(), selectedPath.getLastPathComponent());
				}
				else
				{
					this.rightClickMenu.showMultipleSelected(e.getX(), e.getY(), this.getSelectionPaths());
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public int compareTo(StyleEditorTree other)
	{
		String myName = this.getStyleName();
		String otherName = other.getStyleName();

		return myName.compareToIgnoreCase(otherName);
	}

	/**
	 * 
	 * @return The last selected path component node or NULL. This makes no
	 *         guarantees about which node is returned if multiple nodes are
	 *         selected.
	 */
	public StyleEditorNode getSelectedNode()
	{
		DefaultMutableTreeNode temp = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		StyleEditorNode currentNode = null;

		if (temp instanceof StyleEditorNode)
		{
			currentNode = (StyleEditorNode) temp;
		}

		return currentNode;
	}

	/**
	 * Attempts to "delete" the tree by removing all of its nodes appropriately.
	 * 
	 * This is important because it will remove pointers to nodes that might
	 * have been established during node creation.
	 */
	public void delete()
	{
		Enumeration<StyleEditorNode> nodes = this.breadthFirstEnumeration();
		StyleEditorNode node;

		while (nodes.hasMoreElements())
		{
			node = nodes.nextElement();

			if (node.getParent() != null)
			{
				this.nodeBuilder.removeNode(node);
			}
		}
	}

	@Override
	public List<SelectionListener> getSelection()
	{
		TreePath[] treePaths = this.getSelectionPaths();
		List<SelectionListener> selection = new ArrayList<SelectionListener>();

		Object node;

		if (treePaths != null)
		{
			for (TreePath treePath : treePaths)
			{
				node = treePath.getLastPathComponent();

				if (node instanceof SelectionListener)
				{
					selection.add((SelectionListener) node);
				}
			}
		}

		return selection;
	}
}
