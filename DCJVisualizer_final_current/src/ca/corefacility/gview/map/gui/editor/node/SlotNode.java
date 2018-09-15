package ca.corefacility.gview.map.gui.editor.node;

import java.awt.Paint;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import ca.corefacility.gview.map.controllers.SlotStyleController;
import ca.corefacility.gview.map.controllers.SlotStyleToken;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.controllers.link.Linkable;
import ca.corefacility.gview.map.gui.GUISettings;
import ca.corefacility.gview.map.gui.GUISettings.SlotNodeDetail;
import ca.corefacility.gview.map.gui.editor.panel.SlotPanel;

/**
 * The node class for the slot styles. Intended to be used within a
 * StyleEditorTree.
 * 
 * @author Eric Marinier
 * 
 */
public class SlotNode extends FeatureContainerNode implements Slotable, Linkable
{
	private static final long serialVersionUID = 1L; // requested by java
	private static final String SLOT = "Slot";

	private final SlotPanel slotPanel;

	/**
	 * 
	 * @param slotPanel
	 *            The related panel.
	 */
	public SlotNode(SlotPanel slotPanel)
	{
		super(slotPanel, SLOT);

		if (slotPanel == null)
		{
			throw new IllegalArgumentException("SlotPanel is null.");
		}
		else
		{
			this.slotPanel = slotPanel;
			this.updateName();
		}
	}

	@Override
	public SlotPanel getPanel()
	{
		if (this.slotPanel == null)
			throw new IllegalArgumentException("SlotPanel is null.");

		return this.slotPanel;
	}

	/**
	 * Determines whether or not the slot node has a plot node child.
	 * 
	 * @return Whether or not the slot node has a plot node child.
	 */
	public boolean containsPlotNode()
	{
		boolean result = false;

		@SuppressWarnings("rawtypes")
		Enumeration nodes = this.children();

		while (nodes.hasMoreElements() && result == false)
		{
			if (nodes.nextElement() instanceof PlotNode)
				result = true;
		}

		return result;
	}

	/**
	 * Determines whether or not the slot node has a set node child.
	 * 
	 * @return Whether or not the slot node has a set node child.
	 */
	public boolean containsSetNode()
	{
		boolean result = false;

		@SuppressWarnings("rawtypes")
		Enumeration nodes = this.children();

		while (nodes.hasMoreElements() && result == false)
		{
			if (nodes.nextElement() instanceof SetNode)
				result = true;
		}

		return result;
	}

	@Override
	public void updateName()
	{
		String name = SLOT + " " + getSlotNumber();
		StyleEditorNode node;

		SlotNodeDetail detail = GUISettings.getSlotNodeDetail();

		if (this.getChildCount() > 0 && this.getChildAt(0) instanceof StyleEditorNode)
		{
			node = (StyleEditorNode) this.getChildAt(0);

			if (detail == GUISettings.SlotNodeDetail.FULL)
			{
				name += formatSetName(node.getNodeName());
			}
			else if (detail == GUISettings.SlotNodeDetail.MINIMAL)
			{
				if (node instanceof SetNode)
				{
					name += formatSetName(((SetNode) node).getShortNodeName());
				}
				else
				{
					name += formatSetName(node.getNodeName());
				}
			}
			else if (detail == GUISettings.SlotNodeDetail.LINK)
			{
				Link link = getFirstChildLink();

				if (link != null && link.getName() != null)
				{
					name += " : " + link.getName();
				}
			}
			else if (detail == GUISettings.SlotNodeDetail.NONE)
			{

			}
			else
			{
				// Unknown
			}
		}

		super.rename(name);
	}

	/**
	 * 
	 * @return The first link that exists in a linkable child node.
	 */
	private Link getFirstChildLink()
	{
		Link link = null;

		for (int i = 0; i < this.getChildCount() && link == null; i++)
		{
			Object child = this.getChildAt(i);

			if (child instanceof Linkable)
			{
				link = ((Linkable) child).getLink();
			}
		}

		return link;
	}

	/**
	 * 
	 * @param name
	 * @return Properly formats the given set name.
	 */
	private String formatSetName(String name)
	{
		String result;

		if (this.getChildCount() > 1)
		{
			result = " : {" + name + ", ... }";
		}
		else
		{
			result = " : {" + name + "}";
		}

		return result;
	}

	@Override
	public int getSlotNumber()
	{
		SlotStyleController controller = this.slotPanel.getSlotStyleController();
		SlotStyleToken style = this.slotPanel.getSlotStyle();

		int slotNumber = controller.getSlotNumber(style);

		return slotNumber;
	}

	@Override
	public boolean canAddSetNodeAsChild()
	{
		boolean result = true;

		if (this.containsPlotNode())
		{
			result = false;
		}

		return result;
	}

	/**
	 * 
	 * @return The slot style as a token.
	 */
	public SlotStyleToken getSlotStyle()
	{
		return this.slotPanel.getSlotStyle();
	}

	/**
	 * 
	 * This returns the color of the node. This will return the color the node
	 * SHOULD be, not necessarily the color node currently is.
	 * 
	 * @return The color of the node.
	 */
	public Paint getNodeColor()
	{
		SlotStyleToken style = this.slotPanel.getSlotStyle();
		return SlotStyleController.getConsensusColor(style);
	}

	@Override
	/**
	 * Only return a non-null link if all the links are non-null AND the same link.
	 */
	public Link getLink()
	{
		Link result = null;
		boolean consistent = true;

		@SuppressWarnings("rawtypes")
		Enumeration children = this.children();
		Object child;

		// Enumerate children:
		while (children.hasMoreElements() && consistent)
		{
			child = children.nextElement();

			// Child linkable?
			if (child instanceof Linkable)
			{
				// Grab the link:
				Link link = ((Linkable) child).getLink();

				// There is a link in the node:
				if (link != null)
				{
					// We haven't found a link yet:
					if (result == null)
					{
						result = link;
					}
					// We have found a link, and the link is different:
					else if (!Link.isEqual(link, result))
					{
						consistent = false;
					}
				}
				// There is no link in the node:
				else
				{
					consistent = false;
				}
			}
		}

		// Were the links consistent?
		if (!consistent)
		{
			result = null;
		}

		return result;
	}

	/**
	 * 
	 * @return All the links that are in child nodes. Not recursive.
	 */
	public List<Link> getLinks()
	{
		List<Link> links = new ArrayList<Link>();

		@SuppressWarnings("rawtypes")
		Enumeration children = this.children();
		Object child;

		// Enumerate children:
		while (children.hasMoreElements())
		{
			child = children.nextElement();

			// Child linkable?
			if (child instanceof Linkable)
			{
				// Grab the link:
				Link link = ((Linkable) child).getLink();

				// There is a link in the node:
				if (link != null)
				{
					links.add(link);
				}
			}
		}

		return links;
	}

	@Override
	/**
	 * Set the link as all of the immediate children's links.
	 */
	public void setLink(Link link)
	{
		@SuppressWarnings("rawtypes")
		Enumeration children = this.children();
		Object child;

		// Enumerate children:
		while (children.hasMoreElements())
		{
			child = children.nextElement();

			// Child linkable?
			if (child instanceof Linkable)
			{
				// Grab the link:
				((Linkable) child).setLink(link);
			}
		}
	}

	@Override
	public void update()
	{
		super.update();

		this.updateName();
	}
}
