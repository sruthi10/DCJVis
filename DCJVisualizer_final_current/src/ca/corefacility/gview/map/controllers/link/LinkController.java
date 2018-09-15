package ca.corefacility.gview.map.controllers.link;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.tree.TreePath;

import ca.corefacility.gview.map.gui.editor.StyleEditorTree;
import ca.corefacility.gview.map.gui.editor.communication.LinkEvent;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendItemSwatchColorEvent;
import ca.corefacility.gview.map.gui.editor.communication.setEvent.SetColorEvent;
import ca.corefacility.gview.map.gui.editor.node.LegendItemNode;
import ca.corefacility.gview.map.gui.editor.node.SetNode;
import ca.corefacility.gview.map.gui.editor.node.SlotNode;
import ca.corefacility.gview.map.gui.editor.node.StyleEditorNode;

/**
 * This class is responsible for handling link interactions.
 * 
 * @author Eric Marinier
 * 
 */
public class LinkController
{
	private final ArrayList<LinkListener> listeners = new ArrayList<LinkListener>();
	private static final HashMap<Class<?>, Boolean> linkableEventMap = new HashMap<Class<?>, Boolean>();

	static
	{
		linkableEventMap.put(SetColorEvent.class, true);
		linkableEventMap.put(LegendItemSwatchColorEvent.class, true);
	}

	/**
	 * Adds a link listener.
	 * 
	 * @param listener
	 */
	public void addLinkListener(LinkListener listener)
	{
		this.listeners.add(listener);
	}

	/**
	 * Removes the link listener, if possible.
	 * 
	 * @param listener
	 */
	public void removeLinkListener(LinkListener listener)
	{
		this.listeners.remove(listener);
	}

	/**
	 * Fires the link event to all link listeners.
	 * 
	 * @param event
	 */
	public void fireEvent(LinkEvent event)
	{
		for (LinkListener listener : this.listeners)
		{
			listener.linkEvent(event);
		}
	}

	/**
	 * 
	 * @param event
	 * @return Whether or not the event is defined as a linkable event.
	 */
	public boolean isLinkableEvent(Class<?> event)
	{
		Boolean result = linkableEventMap.get(event);

		if (result == null)
		{
			return false;
		}
		else
		{
			return result.booleanValue();
		}
	}

	/**
	 * Destroys the link by enumerating over the entire tree and setting all
	 * link references that match the passed link to NULL.
	 * 
	 * @param link
	 *            The link to destroy.
	 * @param tree
	 *            The tree whose nodes will be iterated.
	 */
	public static void destroyLink(Link link, StyleEditorTree tree)
	{
		Enumeration<StyleEditorNode> nodes = tree.breadthFirstEnumeration();
		StyleEditorNode node;

		// Iterate over all the nodes in the tree:
		while (nodes.hasMoreElements())
		{
			node = nodes.nextElement();

			// Check to see if the link exists:
			if (node instanceof Linkable && Link.isEqual(((Linkable) node).getLink(), link))
			{
				// Remove the link:
				((Linkable) node).setLink(null);
			}
		}
	}

	/**
	 * Determines whether or not the selection is linkable.
	 * 
	 * Criteria:
	 * 
	 * Only 1 LegendItemNode
	 * 
	 * At least 1 SetNode
	 * 
	 * @param treePaths
	 * @return Whether or the nodes selection in the paths are linkable.
	 */
	public static boolean isLinkable(TreePath[] treePaths)
	{
		int numSetNodes = 0;
		int numLegendItemNodes = 0;

		// Is the selection linkable?
		for (int i = 0; i < treePaths.length; i++)
		{
			Object node = treePaths[i].getLastPathComponent();

			// Is it linkable?
			if (node instanceof Linkable)
			{
				if (node instanceof SetNode)
				{
					numSetNodes++;
				}
				else if (node instanceof LegendItemNode)
				{
					numLegendItemNodes++;
				}
				else if (node instanceof SlotNode)
				{
					numSetNodes += countChildren((SlotNode) node, SetNode.class);
				}
			}
			else
			{
				return false;
			}
		}

		// Check criteria:
		return checkLinkCriteria(numSetNodes, numLegendItemNodes);
	}

	/**
	 * 
	 * @param node
	 * @param classType
	 * @return The number of children that have the given class type.
	 */
	private static int countChildren(StyleEditorNode node, Class<?> classType)
	{
		int count = 0;

		for (int i = 0; i < node.getChildCount(); i++)
		{
			if (node.getChildAt(0).getClass().equals(classType))
			{
				count++;
			}
		}

		return count;
	}

	/**
	 * Determines whether or not the criteria for a link is still satisfied.
	 * 
	 * @param link
	 *            The link to verify.
	 * @param tree
	 *            The tree to search.
	 * @return Whether or not the link criteria is met.
	 */
	private static boolean isCriteriaSatisfied(Link link, StyleEditorTree tree)
	{
		int numSetNodes = 0;
		int numLegendItemNodes = 0;

		Enumeration<StyleEditorNode> nodes = tree.breadthFirstEnumeration();
		StyleEditorNode node;

		// Iterate over all the nodes in the tree:
		while (nodes.hasMoreElements())
		{
			node = nodes.nextElement();

			// Check to see if the link exists:
			if (node instanceof Linkable && Link.isEqual(((Linkable) node).getLink(), link))
			{
				if (node instanceof SetNode)
				{
					numSetNodes++;
				}
				else if (node instanceof LegendItemNode)
				{
					numLegendItemNodes++;
				}
			}
		}

		// Check criteria:
		return checkLinkCriteria(numSetNodes, numLegendItemNodes);
	}

	/**
	 * 
	 * @param numSetNodes
	 *            The number of set nodes that contain the link.
	 * @param numLegendItemNodes
	 *            The number of legend item nodes that contain the link.
	 * 
	 * @return Whether or not the link criteria is satisfied numerically.
	 */
	private static boolean checkLinkCriteria(int numSetNodes, int numLegendItemNodes)
	{
		boolean linkable = true;

		// Check criteria:
		if (!(numLegendItemNodes == 1 && numSetNodes > 0))
		{
			linkable = false;
		}

		return linkable;
	}

	/**
	 * Attempts to remove the link from the passed node and updates the other
	 * nodes in the tree if the criteria for maintaining the link is not met.
	 * 
	 * @param node
	 *            The node to attempt to remove the link from.
	 * @param tree
	 *            The tree to correct if the criteria is not longer met.
	 */
	public static void removeLink(StyleEditorNode node, StyleEditorTree tree)
	{
		if (node instanceof Linkable && ((Linkable) node).getLink() != null)
		{
			Link removedLink = ((Linkable) node).getLink();
			((Linkable) node).setLink(null);

			// Conditions for link still satisfied?
			if (!(LinkController.isCriteriaSatisfied(removedLink, tree)))
			// No:
			{
				// Destroy link:
				LinkController.destroyLink(removedLink, tree);
			}
		}
	}
}
