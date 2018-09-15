package ca.corefacility.gview.map.gui.editor;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.controllers.link.Linkable;
import ca.corefacility.gview.map.gui.editor.icon.OvalTreeIcon;
import ca.corefacility.gview.map.gui.editor.icon.RectangleTreeIcon;
import ca.corefacility.gview.map.gui.editor.icon.TriangleTreeIcon;
import ca.corefacility.gview.map.gui.editor.node.BackboneNode;
import ca.corefacility.gview.map.gui.editor.node.BackboneSlotNode;
import ca.corefacility.gview.map.gui.editor.node.GlobalNode;
import ca.corefacility.gview.map.gui.editor.node.LabelNode;
import ca.corefacility.gview.map.gui.editor.node.LegendItemNode;
import ca.corefacility.gview.map.gui.editor.node.LegendStyleNode;
import ca.corefacility.gview.map.gui.editor.node.PlotNode;
import ca.corefacility.gview.map.gui.editor.node.RulerNode;
import ca.corefacility.gview.map.gui.editor.node.SetNode;
import ca.corefacility.gview.map.gui.editor.node.SlotNode;
import ca.corefacility.gview.map.gui.editor.node.SlotsNode;
import ca.corefacility.gview.map.gui.editor.node.StyleEditorNode;
import ca.corefacility.gview.map.gui.editor.node.TooltipNode;

/**
 * Controls how to render the the tree.
 * 
 * @author Eric Marinier
 * 
 */
public class StyleEditorTreeRenderer extends DefaultTreeCellRenderer
{
	private static final long serialVersionUID = 1L;

	private final StyleEditorTree tree;

	public StyleEditorTreeRenderer(StyleEditorTree tree)
	{
		this.tree = tree;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus)
	{
		Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		Font font = component.getFont();

		boolean highlight = isHighlighted(value);

		// Name:
		if (value instanceof StyleEditorNode)
		{
			this.setText(((StyleEditorNode) value).getNodeName());
		}

		// Font:
		if (highlight)
		{
			font = font.deriveFont(Font.BOLD);
		}
		else
		{
			font = font.deriveFont(Font.PLAIN);
		}

		component.setFont(font);
		setIcon(component, value, expanded, highlight, leaf);

		return component;
	}

	/**
	 * Determines if the node be highlighted.
	 * 
	 * @param node
	 * @return True if the node should be highlighted, false otherwise.
	 */
	private boolean isHighlighted(Object node)
	{
		List<Link> links = getCommonLinks();
		boolean highlight = false;

		// Slot Nodes:
		if (node instanceof SlotNode)
		{
			List<Link> slotLinks = ((SlotNode) node).getLinks();

			highlight = hasMatch(links, slotLinks);
		}
		// Linkable Nodes:
		else if (node instanceof Linkable)
		{
			Link link = ((Linkable) node).getLink();

			if (link != null && links.contains(link))
			{
				highlight = true;
			}
		}

		return highlight;
	}

	/**
	 * 
	 * @param links1
	 * @param links2
	 * @return Whether or not any of the links in one list match any of the
	 *         links in the other.
	 */
	private boolean hasMatch(List<Link> links1, List<Link> links2)
	{
		// Find AT LEAST ONE match:
		for (Link link1 : links1)
		{
			for (Link link2 : links2)
			{
				if (Link.isEqual(link1, link2))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Sets the appropriate icon for the node.
	 * 
	 * @param component
	 * @param value
	 * @param expanded
	 * @param highlight
	 * @param leaf
	 */
	private void setIcon(Component component, Object value, boolean expanded, boolean highlight, boolean leaf)
	{
		// Slots
		if (value instanceof SlotsNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(StyleEditorUtility.SLOTS_IMAGE);
		}
		// Slot
		else if (value instanceof SlotNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(new RectangleTreeIcon(((SlotNode) value).getNodeColor(), highlight));
		}
		// Set
		else if (value instanceof SetNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(new OvalTreeIcon(((SetNode) value).getNodeColor(), highlight));
		}
		// Plot
		else if (value instanceof PlotNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(new TriangleTreeIcon(((PlotNode) value).getNodeColor()));
		}
		// Backbone
		else if (value instanceof BackboneSlotNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(new RectangleTreeIcon(((BackboneSlotNode) value).getNodeColor()));
		}
		// Legend Item
		else if (value instanceof LegendItemNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(new OvalTreeIcon(((LegendItemNode) value).getNodeColor(), highlight));
		}
		// Legend
		else if (value instanceof LegendStyleNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(new RectangleTreeIcon(((LegendStyleNode) value).getNodeColor()));
		}
		// Global
		else if (value instanceof GlobalNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(StyleEditorUtility.GLOBE_IMAGE);
		}
		// Ruler
		else if (value instanceof RulerNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(StyleEditorUtility.RULER_IMAGE);
		}
		// Tooltip
		else if (value instanceof TooltipNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(StyleEditorUtility.TOOLTIP_IMAGE);
		}
		// Backbone
		else if (value instanceof BackboneNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(StyleEditorUtility.BACKBONE_IMAGE);
		}
		// Label Style
		else if (value instanceof LabelNode && component instanceof JLabel)
		{
			((JLabel) component).setIcon(StyleEditorUtility.LABEL_IMAGE);
		}
		// Catch others:
		// not open, non-leaf
		else if (!expanded && !leaf && component instanceof JLabel)
		{
			((JLabel) component).setIcon(StyleEditorUtility.CLOSED_FOLDER);
		}
		// open, non-leaf
		else if (expanded && !leaf && component instanceof JLabel)
		{
			((JLabel) component).setIcon(StyleEditorUtility.OPEN_FOLDER);
		}
		// leaf (and everything else?)
		else if (component instanceof JLabel)
		{
			((JLabel) component).setIcon(StyleEditorUtility.LEAF);
		}
		else
		{
			// Something probably went wrong, don't try to change the rendering.
		}
	}

	/**
	 * 
	 * @return The links of the selection or NULL if no link exists.
	 */
	private List<Link> getCommonLinks()
	{
		List<Link> links = new ArrayList<Link>();
		TreePath[] treePaths = this.tree.getSelectionPaths();

		// Get the links:
		for (int i = 0; treePaths != null && i < treePaths.length; i++)
		{
			Object node = treePaths[i].getLastPathComponent();
			links.addAll(getLinks(node));
		}

		return links;
	}

	/**
	 * 
	 * @param node
	 *            The node to get the link from.
	 * @return The link or NULL if there is a link conflict or no link exists.
	 */
	private List<Link> getLinks(Object node)
	{
		List<Link> links = new ArrayList<Link>();

		// Is it linkable?
		if (node instanceof SlotNode)
		{
			links = ((SlotNode) node).getLinks();
		}
		if (node instanceof Linkable && ((Linkable) node).getLink() != null)
		{
			links.add(((Linkable) node).getLink());
		}

		return links;
	}
}
