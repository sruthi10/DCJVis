package ca.corefacility.gview.map.gui.editor.node;

import java.awt.Paint;

import ca.corefacility.gview.map.controllers.FeatureHolderStyleToken;
import ca.corefacility.gview.map.controllers.SetStyleController;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.controllers.link.LinkListener;
import ca.corefacility.gview.map.controllers.link.Linkable;
import ca.corefacility.gview.map.gui.editor.communication.LinkEvent;
import ca.corefacility.gview.map.gui.editor.panel.SetPanel;

/**
 * The node class for the set styles. Intended to be used within a
 * StyleEditorTree.
 * 
 * @author Eric Marinier
 * 
 */
public class SetNode extends FeatureContainerNode implements LinkListener, Linkable
{
	private static final long serialVersionUID = 1L; // requested by java
	private static final String SET = "Set";

	private final SetPanel setPanel;

	/**
	 * 
	 * @param setPanel
	 *            The related panel.
	 */
	public SetNode(SetPanel setPanel)
	{
		super(setPanel, SET);

		if (setPanel == null)
			throw new IllegalArgumentException("SetPanel is null.");

		this.setPanel = setPanel;
		this.rename(getDefaultName());
	}

	/**
	 * Returns the default name of the set.
	 */
	private String getDefaultName()
	{
		SetStyleController setStyleController = this.setPanel.getController();

		return setStyleController.generateDefaultName(getSetStyle());
	}

	@Override
	public SetPanel getPanel()
	{
		if (this.setPanel == null)
			throw new IllegalArgumentException("SetPanel is null.");

		return this.setPanel;
	}

	@Override
	public boolean canAddSetNodeAsChild()
	{
		return true;
	}

	@Override
	public FeatureContainerNode getParent()
	{
		FeatureContainerNode parent = null;

		if (super.getParent() instanceof FeatureContainerNode)
		{
			parent = (FeatureContainerNode) super.getParent();
		}
		else if (super.getParent() != null)
		{
			throw new ClassCastException("SetNode's parent is not a FeatureContainerNode.");
		}

		return parent;
	}

	/**
	 * 
	 * @return The set style as token.
	 */
	public FeatureHolderStyleToken getSetStyle()
	{
		return this.setPanel.getSetStyle();
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
		SetStyleController controller = this.setPanel.getController();
		FeatureHolderStyleToken style = this.setPanel.getSetStyle();

		return controller.getColor(style);
	}

	@Override
	public void linkEvent(LinkEvent event)
	{
		Link eventLink = event.getLink();
		Link myLink = this.getLink();

		// Return if the links do not match:
		if (!Link.isEqual(eventLink, myLink))
		{
			return;
		}

		this.setPanel.handleGUIEvent(event.getGUIEvent());
	}

	@Override
	public Link getLink()
	{
		return this.setPanel.getLink();
	}

	@Override
	public void setLink(Link link)
	{
		this.setPanel.setLink(link);
	}

	/**
	 * 
	 * @return The short version of the node name.
	 */
	public String getShortNodeName()
	{
		SetStyleController setStyleController = this.setPanel.getController();

		return setStyleController.generateShortName(getSetStyle());
	}
}
