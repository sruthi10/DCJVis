package ca.corefacility.gview.map.gui.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.tree.TreePath;

import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.controllers.link.Linkable;
import ca.corefacility.gview.map.gui.editor.StyleEditorFrame;
import ca.corefacility.gview.map.gui.editor.StyleEditorTree;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.editor.node.LabelNode;
import ca.corefacility.gview.map.gui.editor.node.LegendItemNode;
import ca.corefacility.gview.map.gui.editor.node.LegendStyleNode;
import ca.corefacility.gview.map.gui.editor.node.PlotNode;
import ca.corefacility.gview.map.gui.editor.node.SetNode;
import ca.corefacility.gview.map.gui.editor.node.SlotNode;

/**
 * The selection menu class.
 * 
 * This selection menu is intended to exist on a StyleEditorFrame menu.
 * 
 * @author Eric Marinier
 * 
 */
public class SelectionMenu extends JMenu implements ActionListener, MenuListener
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final String SELECT_ALL_TEXT = "Select All ...";
	private static final String SELECT_WITHIN_TEXT = "Select Within ...";

	private static final String SELECT_ALL_SETS = "Select All Sets";
	private static final String SELECT_ALL_SETS_TEXT = "Sets";
	private static final String SELECT_ALL_SLOTS = "Select All Slots";
	private static final String SELECT_ALL_SLOTS_TEXT = "Slots";
	private static final String SELECT_ALL_PLOTS = "Select All Plots";
	private static final String SELECT_ALL_PLOTS_TEXT = "Plots";
	private static final String SELECT_ALL_LEGENDS = "Select All Legends";
	private static final String SELECT_ALL_LEGENDS_TEXT = "Legend Boxes";
	private static final String SELECT_ALL_LEGEND_ITEMS = "Select All Legend Items";
	private static final String SELECT_ALL_LEGEND_ITEMS_TEXT = "Legend Texts";
	private static final String SELECT_ALL_LABELS = "Select All Labels";
	private static final String SELECT_ALL_LABELS_TEXT = "Labels";

	private static final String SELECT_WITHIN_SETS = "Select Within Sets";
	private static final String SELECT_WITHIN_SETS_TEXT = "Sets";
	private static final String SELECT_WITHIN_SLOTS = "Select Within Slots";
	private static final String SELECT_WITHIN_SLOTS_TEXT = "Slots";
	private static final String SELECT_WITHIN_PLOTS = "Select Within Plots";
	private static final String SELECT_WITHIN_PLOTS_TEXT = "Plots";
	private static final String SELECT_WITHIN_LEGENDS = "Select Within Legend Boxes";
	private static final String SELECT_WITHIN_LEGENDS_TEXT = "Legend Boxes";
	private static final String SELECT_WITHIN_LEGEND_ITEMS = "Select Within Legend Items";
	private static final String SELECT_WITHIN_LEGEND_ITEMS_TEXT = "Legend Items";
	private static final String SELECT_WITHIN_LABELS = "Select Within Labels";
	private static final String SELECT_WITHIN_LABELS_TEXT = "Labels";

	private static final String SELECT_LINK = "Select Link";
	private static final String SELECT_LINK_TEXT = "Select Linked Sets";

	private final JMenu selectAll;
	private final JMenu selectWithin;
	private final JMenuItem selectLink;

	private final JMenuItem selectAllSets;
	private final JMenuItem selectAllSlots;
	private final JMenuItem selectAllPlots;
	private final JMenuItem selectAllLegends;
	private final JMenuItem selectAllLegendItems;
	private final JMenuItem selectAllLabels;

	private final JMenuItem selectWithinSets;
	private final JMenuItem selectWithinSlots;
	private final JMenuItem selectWithinPlots;
	private final JMenuItem selectWithinLegends;
	private final JMenuItem selectWithinLegendItems;
	private final JMenuItem selectWithinLabels;

	private final StyleEditorFrame frame;

	public SelectionMenu(StyleEditorFrame frame)
	{
		super(StyleEditorUtility.SELECTION_TEXT);

		this.frame = frame;

		this.selectAll = new JMenu(SELECT_ALL_TEXT);

		this.selectAllSets = new JMenuItem(SELECT_ALL_SETS_TEXT);
		this.selectAllSets.setActionCommand(SELECT_ALL_SETS);
		this.selectAllSets.addActionListener(this);

		this.selectAllSlots = new JMenuItem(SELECT_ALL_SLOTS_TEXT);
		this.selectAllSlots.setActionCommand(SELECT_ALL_SLOTS);
		this.selectAllSlots.addActionListener(this);

		this.selectAllPlots = new JMenuItem(SELECT_ALL_PLOTS_TEXT);
		this.selectAllPlots.setActionCommand(SELECT_ALL_PLOTS);
		this.selectAllPlots.addActionListener(this);

		this.selectAllLegends = new JMenuItem(SELECT_ALL_LEGENDS_TEXT);
		this.selectAllLegends.setActionCommand(SELECT_ALL_LEGENDS);
		this.selectAllLegends.addActionListener(this);

		this.selectAllLegendItems = new JMenuItem(SELECT_ALL_LEGEND_ITEMS_TEXT);
		this.selectAllLegendItems.setActionCommand(SELECT_ALL_LEGEND_ITEMS);
		this.selectAllLegendItems.addActionListener(this);

		this.selectAllLabels = new JMenuItem(SELECT_ALL_LABELS_TEXT);
		this.selectAllLabels.setActionCommand(SELECT_ALL_LABELS);
		this.selectAllLabels.addActionListener(this);

		this.selectWithin = new JMenu(SELECT_WITHIN_TEXT);

		this.selectWithinSets = new JMenuItem(SELECT_WITHIN_SETS_TEXT);
		this.selectWithinSets.setActionCommand(SELECT_WITHIN_SETS);
		this.selectWithinSets.addActionListener(this);

		this.selectWithinSlots = new JMenuItem(SELECT_WITHIN_SLOTS_TEXT);
		this.selectWithinSlots.setActionCommand(SELECT_WITHIN_SLOTS);
		this.selectWithinSlots.addActionListener(this);

		this.selectWithinPlots = new JMenuItem(SELECT_WITHIN_PLOTS_TEXT);
		this.selectWithinPlots.setActionCommand(SELECT_WITHIN_PLOTS);
		this.selectWithinPlots.addActionListener(this);

		this.selectWithinLegends = new JMenuItem(SELECT_WITHIN_LEGENDS_TEXT);
		this.selectWithinLegends.setActionCommand(SELECT_WITHIN_LEGENDS);
		this.selectWithinLegends.addActionListener(this);

		this.selectWithinLegendItems = new JMenuItem(SELECT_WITHIN_LEGEND_ITEMS_TEXT);
		this.selectWithinLegendItems.setActionCommand(SELECT_WITHIN_LEGEND_ITEMS);
		this.selectWithinLegendItems.addActionListener(this);

		this.selectWithinLabels = new JMenuItem(SELECT_WITHIN_LABELS_TEXT);
		this.selectWithinLabels.setActionCommand(SELECT_WITHIN_LABELS);
		this.selectWithinLabels.addActionListener(this);

		this.selectLink = new JMenuItem(SELECT_LINK_TEXT);
		this.selectLink.setActionCommand(SELECT_LINK);
		this.selectLink.addActionListener(this);

		// Add items:
		this.add(this.selectAll);
		this.selectAll.add(this.selectAllLabels);
		this.selectAll.add(this.selectAllLegends);
		this.selectAll.add(this.selectAllLegendItems);
		this.selectAll.add(this.selectAllPlots);
		this.selectAll.add(this.selectAllSets);
		this.selectAll.add(this.selectAllSlots);

		this.add(this.selectWithin);
		this.selectWithin.add(this.selectWithinLabels);
		this.selectWithin.add(this.selectWithinLegends);
		this.selectWithin.add(this.selectWithinLegendItems);
		this.selectWithin.add(this.selectWithinPlots);
		this.selectWithin.add(this.selectWithinSets);
		this.selectWithin.add(this.selectWithinSlots);

		this.addSeparator();

		this.add(this.selectLink);

		// Menu Listener:
		this.addMenuListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		StyleEditorTree tree = this.frame.getCurrentStyleTree();

		// Select all sets:
		if (this.selectAllSets.equals(e.getSource()))
		{
			tree.selectAllNodes(SetNode.class);
		}
		// Select all slots:
		else if (this.selectAllSlots.equals(e.getSource()))
		{
			tree.selectAllNodes(SlotNode.class);
		}
		// Select all plots:
		else if (this.selectAllPlots.equals(e.getSource()))
		{
			tree.selectAllNodes(PlotNode.class);
		}
		// Select all legends:
		else if (this.selectAllLegends.equals(e.getSource()))
		{
			tree.selectAllNodes(LegendStyleNode.class);
		}
		// Select all legend items:
		else if (this.selectAllLegendItems.equals(e.getSource()))
		{
			tree.selectAllNodes(LegendItemNode.class);
		}
		// Select all legend items:
		else if (this.selectAllLabels.equals(e.getSource()))
		{
			tree.selectAllNodes(LabelNode.class);
		}
		// Select within sets:
		else if (this.selectWithinSets.equals(e.getSource()))
		{
			tree.selectNodesWithinSelection(SetNode.class);
		}
		// Select within slots:
		else if (this.selectWithinSlots.equals(e.getSource()))
		{
			tree.selectNodesWithinSelection(SlotNode.class);
		}
		// Select within plots:
		else if (this.selectWithinPlots.equals(e.getSource()))
		{
			tree.selectNodesWithinSelection(PlotNode.class);
		}
		// Select within legends:
		else if (this.selectWithinLegends.equals(e.getSource()))
		{
			tree.selectNodesWithinSelection(LegendStyleNode.class);
		}
		// Select within legend items:
		else if (this.selectWithinLegendItems.equals(e.getSource()))
		{
			tree.selectNodesWithinSelection(LegendItemNode.class);
		}
		// Select within labels:
		else if (this.selectWithinLabels.equals(e.getSource()))
		{
			tree.selectNodesWithinSelection(LabelNode.class);
		}
		// Select link:
		else if (this.selectLink.equals(e.getSource()))
		{
			ArrayList<Link> links = new ArrayList<Link>();
			TreePath[] treePaths = tree.getSelectionPaths();

			// Find all selected linked objects:
			for (int i = 0; treePaths != null && i < treePaths.length; i++)
			{
				Object node = treePaths[i].getLastPathComponent();

				// Linkable?
				if (node instanceof Linkable && ((Linkable) node).getLink() != null)
				{
					Link link = ((Linkable) node).getLink();

					if (!links.contains(link))
					{
						links.add(link);
					}
				}
			}

			tree.selectNodesWithLink(links, SetNode.class);
		}
	}

	@Override
	public void menuSelected(MenuEvent e)
	{
		this.update();
	}

	@Override
	public void menuDeselected(MenuEvent e)
	{
	}

	@Override
	public void menuCanceled(MenuEvent e)
	{
	}

	/**
	 * Updates the menu.
	 */
	private void update()
	{
		if (StyleEditorTree.hasLink(this.frame.getCurrentStyleTree().getSelectionPaths()))
		{
			this.selectLink.setEnabled(true);
		}
		else
		{
			this.selectLink.setEnabled(false);
		}
	}
}
