package ca.corefacility.gview.map.gui.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ca.corefacility.gview.map.controllers.LegendItemStyleToken;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.controllers.link.LinkController;
import ca.corefacility.gview.map.controllers.link.Linkable;
import ca.corefacility.gview.map.gui.action.style.NewPlotAction;
import ca.corefacility.gview.map.gui.action.style.NewSetAction;
import ca.corefacility.gview.map.gui.action.style.NewSlotAction;
import ca.corefacility.gview.map.gui.action.style.RefineSetAction;
import ca.corefacility.gview.map.gui.editor.node.FeatureContainerNode;
import ca.corefacility.gview.map.gui.editor.node.LegendItemNode;
import ca.corefacility.gview.map.gui.editor.node.LegendNode;
import ca.corefacility.gview.map.gui.editor.node.LegendStyleNode;
import ca.corefacility.gview.map.gui.editor.node.PlotNode;
import ca.corefacility.gview.map.gui.editor.node.PropertyMapperNode;
import ca.corefacility.gview.map.gui.editor.node.RootNode;
import ca.corefacility.gview.map.gui.editor.node.SetNode;
import ca.corefacility.gview.map.gui.editor.node.SlotNode;
import ca.corefacility.gview.map.gui.editor.node.SlotsNode;
import ca.corefacility.gview.map.gui.editor.node.StyleEditorNode;

/**
 * The right click menu for the StyleTree.
 * 
 * @author Eric Marinier
 * 
 */
public class TreeRightClickMenu extends JPopupMenu implements ActionListener
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final String RENAME_TEXT = "Rename";
	private static final String RENAME = "Rename Node";

	private static final String NEW_SLOT = "New Slot";
	private static final String NEW_SLOT_TEXT = "New Slot";
	private static final String NEW_SET = "New Set";
	private static final String NEW_SET_TEXT = "New Set";
	private static final String NEW_SUBSET_TEXT = "New Subset";
	private static final String NEW_PLOT = "New Plot";
	private static final String NEW_PLOT_TEXT = "New Plot";
	private static final String NEW_LEGEND = "New Legend Box";
	private static final String NEW_LEGEND_TEXT = "New Legend Box";
	private static final String NEW_LEGEND_ITEM = "New Legend Text";
	private static final String NEW_LEGEND_ITEM_TEXT = "New Legend Text";
	private static final String NEW_PROPERTY_MAPPER = "New Property Mapper";
	private static final String NEW_PROPERTY_MAPPER_TEXT = "New Property Mapper";

	private static final String REFINE = "Refine";
	private static final String REFINE_TEXT = "Refine Set";

	private static final String REMOVE_SLOT = "Remove Slot";
	private static final String REMOVE_PLOT = "Remove Plot";
	private static final String REMOVE_SET = "Remove Set";
	private static final String REMOVE_SLOT_TEXT = "Remove Slot";
	private static final String REMOVE_PLOT_TEXT = "Remove Plot";
	private static final String REMOVE_SET_TEXT = "Remove Set";
	private static final String REMOVE_LEGEND = "Remove Legend Box";
	private static final String REMOVE_LEGEND_TEXT = "Remove Legend Box";
	private static final String REMOVE_LEGEND_ITEM = "Remove Legend Text";
	private static final String REMOVE_LEGEND_ITEM_TEXT = "Remove Legend Text";
	private static final String REMOVE_PROPERTY_MAPPER = "Remove Property Mapper";
	private static final String REMOVE_PROPERTY_MAPPER_TEXT = "Remove Property Mapper";

	private static final String LINK = "Link";
	private static final String LINK_TEXT = "Create Legend Link";
	private static final String REMOVE_LINK_TEXT = "Remove Legend Link";
	private static final String REMOVE_LINK_ALL = "Remove Link From All";
	private static final String REMOVE_LINK_ALL_TEXT = "Remove Legend Links";
	private static final String DESTROY_LINK_TEXT = "Destroy Link";

	private final JMenuItem rename;

	private final JMenuItem newSlot;
	private final JMenuItem newSet;
	private final JMenuItem newSubset;
	private final JMenuItem newPlot;
	private final JMenuItem newLegend;
	private final JMenuItem newLegendItem;
	private final JMenuItem newPropertyMapper;

	private final JMenuItem refine;

	private final JMenuItem removeSlot;
	private final JMenuItem removePlot;
	private final JMenuItem removeSet;
	private final JMenuItem removeLegend;
	private final JMenuItem removeLegendItem;
	private final JMenuItem removePropertyMapper;

	private final JMenuItem link;
	private final JMenuItem removeLink;
	private final JMenuItem removeLinkAll;
	private final JMenuItem destroyLink;

	private final StyleEditorFrame styleEditorFrame;

	public TreeRightClickMenu(StyleEditorFrame styleEditorFrame, StyleEditorTree styleEditorTree)
	{
		super();

		if (styleEditorFrame == null)
			throw new IllegalArgumentException("StyleEditorFrame is null");

		this.styleEditorFrame = styleEditorFrame;

		this.rename = new JMenuItem(RENAME_TEXT);
		this.rename.setActionCommand(RENAME);
		this.rename.addActionListener(this);

		this.newSlot = new JMenuItem(NEW_SLOT_TEXT);
		this.newSlot.setActionCommand(NEW_SLOT);
		this.newSlot.addActionListener(this);

		this.newSet = new JMenuItem(NEW_SET_TEXT);
		this.newSet.setActionCommand(NEW_SET);
		this.newSet.addActionListener(this);

		this.newSubset = new JMenuItem(NEW_SUBSET_TEXT);
		this.newSubset.setActionCommand(NEW_SET); // This should use the same
													// action command as new
													// set.. the difference in
													// name is for clarity!
		this.newSubset.addActionListener(this);

		this.newPlot = new JMenuItem(NEW_PLOT_TEXT);
		this.newPlot.setActionCommand(NEW_PLOT);
		this.newPlot.addActionListener(this);

		this.newLegend = new JMenuItem(NEW_LEGEND_TEXT);
		this.newLegend.setActionCommand(NEW_LEGEND);
		this.newLegend.addActionListener(this);

		this.newLegendItem = new JMenuItem(NEW_LEGEND_ITEM_TEXT);
		this.newLegendItem.setActionCommand(NEW_LEGEND_ITEM);
		this.newLegendItem.addActionListener(this);

		this.newPropertyMapper = new JMenuItem(NEW_PROPERTY_MAPPER_TEXT);
		this.newPropertyMapper.setActionCommand(NEW_PROPERTY_MAPPER);
		this.newPropertyMapper.addActionListener(this);

		this.removeSlot = new JMenuItem(REMOVE_SLOT_TEXT);
		this.removeSlot.setActionCommand(REMOVE_SLOT);
		this.removeSlot.addActionListener(this);

		this.removePlot = new JMenuItem(REMOVE_PLOT_TEXT);
		this.removePlot.setActionCommand(REMOVE_PLOT);
		this.removePlot.addActionListener(this);

		this.removeSet = new JMenuItem(REMOVE_SET_TEXT);
		this.removeSet.setActionCommand(REMOVE_SET);
		this.removeSet.addActionListener(this);

		this.removeLegend = new JMenuItem(REMOVE_LEGEND_TEXT);
		this.removeLegend.setActionCommand(REMOVE_LEGEND);
		this.removeLegend.addActionListener(this);

		this.removeLegendItem = new JMenuItem(REMOVE_LEGEND_ITEM_TEXT);
		this.removeLegendItem.setActionCommand(REMOVE_LEGEND_ITEM);
		this.removeLegendItem.addActionListener(this);

		this.removePropertyMapper = new JMenuItem(REMOVE_PROPERTY_MAPPER_TEXT);
		this.removePropertyMapper.setActionCommand(REMOVE_PROPERTY_MAPPER);
		this.removePropertyMapper.addActionListener(this);

		this.refine = new JMenuItem(REFINE_TEXT);
		this.refine.setActionCommand(REFINE);
		this.refine.addActionListener(this);

		this.link = new JMenuItem(LINK_TEXT);
		this.link.setActionCommand(LINK);
		this.link.addActionListener(this);

		this.removeLink = new JMenuItem(REMOVE_LINK_TEXT);
		this.removeLink.setActionCommand(REMOVE_LINK_ALL);
		this.removeLink.addActionListener(this);

		this.removeLinkAll = new JMenuItem(REMOVE_LINK_ALL_TEXT);
		this.removeLinkAll.setActionCommand(REMOVE_LINK_ALL);
		this.removeLinkAll.addActionListener(this);

		this.destroyLink = new JMenuItem(DESTROY_LINK_TEXT);
		this.destroyLink.setActionCommand(REMOVE_LINK_ALL);
		this.destroyLink.addActionListener(this);
	}

	/**
	 * Shows the right click menu.
	 * 
	 * @param x
	 *            The x location to show at.
	 * @param y
	 *            The y location to show at.
	 * @param node
	 *            The node that was right clicked.
	 */
	public void show(int x, int y, Object node)
	{
		if (node instanceof StyleEditorNode)
		{
			this.setMenuItems((StyleEditorNode) node);
			showMenu(x, y);
		}
	}

	/**
	 * Show the menu when multiple items are selected.
	 * 
	 * @param x
	 * @param y
	 * @param treePaths
	 */
	public void showMultipleSelected(int x, int y, TreePath[] treePaths)
	{
		if (treePaths != null && treePaths.length > 0)
		{
			this.setMultiSelectionMenuItems(treePaths);
			showMenu(x, y);
		}
	}

	/**
	 * Show the menu.
	 * 
	 * @param x
	 * @param y
	 */
	private void showMenu(int x, int y)
	{
		if (this.getSubElements().length > 0)
		{
			super.show(this.styleEditorFrame.getCurrentStyleTree(), x, y);
		}
	}

	/**
	 * Set the menu items when multiple items are selected.
	 * 
	 * @param treePaths
	 */
	private void setMultiSelectionMenuItems(TreePath[] treePaths)
	{
		this.removeAll();

		if (LinkController.isLinkable(treePaths))
		{
			this.add(this.link);
		}

		if (StyleEditorTree.hasLink(treePaths))
		{
			if (this.getComponentCount() > 0)
			{
				this.addSeparator();
			}

			this.add(this.removeLinkAll);
		}
	}

	/**
	 * Shows only the appropriate menu items for the node.
	 * 
	 * @param node
	 *            The (right) clicked node.
	 */
	private void setMenuItems(StyleEditorNode node)
	{
		TreePath[] treePaths = this.styleEditorFrame.getCurrentStyleTree().getSelectionPaths();

		this.removeAll();

		// Root node
		if (node instanceof RootNode)
		{

		}

		// Slots (the collection of slots)
		if (node instanceof SlotsNode)
		{
			this.add(this.newSlot);
		}

		// Slot
		if (node instanceof SlotNode)
		{
			// if the slot node DOESNT contain a PlotNode or SetNode child
			// that is, you can add a new set or a new plot to a slot that is
			// empty
			if (!((SlotNode) node).containsPlotNode() && !((SlotNode) node).containsSetNode())
			{
				this.add(this.newSet);
				this.add(this.newPlot);

				this.addSeparator();

				this.add(this.removeSlot);
			}
			// if the slot node DOESNT contain a PlotNode
			// that is, you can add a set to a slot node that doesn't contain a
			// plot node
			else if (!((SlotNode) node).containsPlotNode())
			{
				this.add(this.newSet);

				this.addSeparator();

				this.add(this.removeSlot);
			}
			else
			{
				this.add(this.removeSlot);
			}
		}

		// Set
		if (node instanceof SetNode)
		{
			this.add(this.refine);

			this.addSeparator();

			this.add(this.newSubset);

			// if the set node does NOT contain a property mapper already:
			// that is, you can add a property mapper to a set that doesnt
			// contain a property mapper already
			if (!((SetNode) node).containsPropertyMapper())
			{
				this.add(this.newPropertyMapper);
			}

			this.addSeparator();

			this.add(this.removeSet);

			// Linkable
			if (StyleEditorTree.hasLink(treePaths))
			{
				this.addSeparator();

				this.add(this.removeLink);
			}
		}

		// Plot
		if (node instanceof PlotNode)
		{
			this.add(this.rename);

			this.addSeparator();

			this.add(this.removePlot);

		}

		// Legend (collection node for legends)
		if (node instanceof LegendNode)
		{
			this.add(this.newLegend);
		}

		// Legend Style (the legend itself)
		if (node instanceof LegendStyleNode)
		{
			this.add(this.newLegendItem);

			this.addSeparator();

			this.add(this.removeLegend);
		}

		// Legend Item Style (the sub items under legend style)
		if (node instanceof LegendItemNode)
		{
			this.add(this.removeLegendItem);

			// Linkable
			if (StyleEditorTree.hasLink(treePaths))
			{
				this.addSeparator();

				this.add(this.destroyLink);
			}
		}

		// Property Mapper
		if (node instanceof PropertyMapperNode)
		{
			this.add(this.removePropertyMapper);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object selectedNode = this.styleEditorFrame.getCurrentStyleTree().getLastSelectedPathComponent();
		TreePath[] treePaths = this.styleEditorFrame.getCurrentStyleTree().getSelectionPaths();

		// RENAME
		if (e.getActionCommand().equals(RENAME))
		{
			if (selectedNode instanceof StyleEditorNode)
			{
				((StyleEditorNode) selectedNode).rename();
				this.styleEditorFrame.getCurrentStyleTree().getTreeModel().nodeChanged((TreeNode) selectedNode);
			}
		}
		// NEW SLOT
		else if (e.getActionCommand().equals(NEW_SLOT))
		{
			if (selectedNode instanceof SlotsNode)
			{
				(new NewSlotAction(this.styleEditorFrame.getCurrentStyleTree())).run();
			}
		}
		// NEW SET
		else if (e.getActionCommand().equals(NEW_SET))
		{
			(new NewSetAction(this.styleEditorFrame.getCurrentStyleTree())).run();
		}
		// REFINE SET
		else if (e.getActionCommand().equals(REFINE))
		{
			(new RefineSetAction(this.styleEditorFrame.getCurrentStyleTree())).run();
		}
		// NEW PLOT
		else if (e.getActionCommand().equals(NEW_PLOT))
		{
			(new NewPlotAction(this.styleEditorFrame.getCurrentStyleTree())).run();
		}
		// NEW LEGEND
		else if (e.getActionCommand().equals(NEW_LEGEND))
		{
			this.styleEditorFrame.getCurrentStyleTree().createLegendNode();
		}
		// NEW LEGEND ITEM
		else if (e.getActionCommand().equals(NEW_LEGEND_ITEM))
		{
			if (selectedNode instanceof LegendStyleNode)
			{
				this.styleEditorFrame.getCurrentStyleTree().createLegendItemNode((LegendStyleNode) selectedNode);
			}
		}
		// NEW PROPERTY MAPPER
		else if (e.getActionCommand().equals(NEW_PROPERTY_MAPPER))
		{
			if (selectedNode instanceof FeatureContainerNode)
			{
				if (!((FeatureContainerNode) selectedNode).containsPropertyMapper())
					this.styleEditorFrame.getCurrentStyleTree().createPropertyMapper(
							(FeatureContainerNode) selectedNode);
			}
		}
		// REMOVE SLOT
		else if (e.getActionCommand().equals(REMOVE_SLOT))
		{
			if (selectedNode instanceof SlotNode)
			{
				this.styleEditorFrame.getCurrentStyleTree().removeSlotNode((SlotNode) selectedNode);
			}
		}
		// REMOVE SET
		else if (e.getActionCommand().equals(REMOVE_SET))
		{
			if (selectedNode instanceof SetNode)
			{
				this.styleEditorFrame.getCurrentStyleTree().removeSetNode((SetNode) selectedNode);
			}
		}
		// REMOVE PLOT
		else if (e.getActionCommand().equals(REMOVE_PLOT))
		{
			if (selectedNode instanceof PlotNode)
			{
				this.styleEditorFrame.getCurrentStyleTree().removePlotNode((PlotNode) selectedNode);
			}
		}
		// REMOVE LEGEND
		else if (e.getActionCommand().equals(REMOVE_LEGEND))
		{
			if (selectedNode instanceof LegendStyleNode)
			{
				this.styleEditorFrame.getCurrentStyleTree().removeLegend((LegendStyleNode) selectedNode);
			}
		}
		// REMOVE LEGEND ITEM
		else if (e.getActionCommand().equals(REMOVE_LEGEND_ITEM))
		{
			if (selectedNode instanceof LegendItemNode)
			{
				this.styleEditorFrame.getCurrentStyleTree().removeLegendItem((LegendItemNode) selectedNode);
			}
		}
		// REMOVE PROPERTY MAPPER
		else if (e.getActionCommand().equals(REMOVE_PROPERTY_MAPPER))
		{
			if (selectedNode instanceof PropertyMapperNode)
			{
				this.styleEditorFrame.getCurrentStyleTree().removePropertyMapper((PropertyMapperNode) selectedNode);
			}
		}
		// LINK
		else if (e.getActionCommand().equals(LINK))
		{
			link();
		}
		// REMOVE LINK ALL
		else if (e.getActionCommand().equals(REMOVE_LINK_ALL))
		{
			// Set the link object:
			for (int i = 0; i < treePaths.length; i++)
			{
				Object node = treePaths[i].getLastPathComponent();

				// Is it linkable?
				if (node instanceof Linkable && node instanceof StyleEditorNode)
				{
					LinkController.removeLink((StyleEditorNode) node, this.styleEditorFrame.getCurrentStyleTree());
				}
			}

			this.styleEditorFrame.getCurrentStyleTree().updateTreeRendering();
		}

		this.styleEditorFrame.getCurrentStyleTree().repaint(); // Force repaint.
	}

	/**
	 * Links all of the currently selected nodes.
	 */
	private void link()
	{
		TreePath[] treePaths = this.styleEditorFrame.getCurrentStyleTree().getSelectionPaths();
		LegendItemStyleToken legend = getLegend(treePaths);

		if (legend != null)
		{
			Link link = new Link(legend);

			// Set the link object:
			for (int i = 0; i < treePaths.length; i++)
			{
				Object node = treePaths[i].getLastPathComponent();

				// Is it linkable?
				if (node instanceof Linkable && node instanceof StyleEditorNode)
				{
					LinkController.removeLink((StyleEditorNode) node, this.styleEditorFrame.getCurrentStyleTree());
					((Linkable) node).setLink(link);
				}
			}

			this.styleEditorFrame.getCurrentStyleTree().updateTreeRendering();
		}
	}

	/**
	 * 
	 * @param treePaths
	 * @return The legend item in the selection.
	 */
	private LegendItemStyleToken getLegend(TreePath[] treePaths)
	{
		LegendItemStyleToken legend = null;

		// Set the link object:
		for (int i = 0; i < treePaths.length && legend == null; i++)
		{
			Object node = treePaths[i].getLastPathComponent();

			// Is it linkable?
			if (node instanceof LegendItemNode)
			{
				legend = ((LegendItemNode) node).getPanel().getLegendItemSyle();
			}
		}

		return legend;
	}
}
