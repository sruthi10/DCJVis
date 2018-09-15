package ca.corefacility.gview.map.gui.editor.node;

import java.awt.Paint;

import ca.corefacility.gview.map.controllers.LegendItemStyleToken;
import ca.corefacility.gview.map.controllers.LegendStyleController;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.controllers.link.LinkListener;
import ca.corefacility.gview.map.controllers.link.Linkable;
import ca.corefacility.gview.map.gui.editor.communication.LinkEvent;
import ca.corefacility.gview.map.gui.editor.panel.LegendItemPanel;

/**
 * The node class for the legend item styles. Intended to be used within a
 * StyleEditorTree.
 * 
 * @author Eric Marinier
 * 
 */
public class LegendItemNode extends StyleEditorNode implements LinkListener, Linkable
{
	private static final long serialVersionUID = 1L; // requested by java
	private static final String LEGEND_ITEM_STYLE = "Legend Item";

	private final LegendItemPanel legendItemStylePanel; // the related
														// legend item
														// style panel

	/**
	 * 
	 * @param legendItemStylePanel
	 *            The related panel.
	 */
	public LegendItemNode(LegendItemPanel legendItemStylePanel)
	{
		super(legendItemStylePanel, LEGEND_ITEM_STYLE);

		if (legendItemStylePanel == null)
			throw new IllegalArgumentException("LegendItemStylePanel is null");

		this.legendItemStylePanel = legendItemStylePanel;
		this.rename(this.legendItemStylePanel.getLegendText());
	}

	@Override
	public LegendItemPanel getPanel()
	{
		if (this.legendItemStylePanel == null)
			throw new IllegalArgumentException("legendItemStylePanel is null");

		return this.legendItemStylePanel;
	}

	@Override
	public LegendStyleNode getParent()
	{
		LegendStyleNode parent = null;

		if (super.getParent() instanceof LegendStyleNode)
		{
			parent = (LegendStyleNode) super.getParent();
		}
		else if (super.getParent() != null)
		{
			throw new ClassCastException("LegendItemStyleNode's parent is not a LegendStyleNode.");
		}

		return parent;
	}

	/**
	 * 
	 * @return The legend item style.
	 */
	public LegendItemStyleToken getLegendItemSyle()
	{
		return this.legendItemStylePanel.getLegendItemSyle();
	}

	/**
	 * 
	 * @return The node color.
	 */
	public Paint getNodeColor()
	{
		LegendStyleController controller = this.legendItemStylePanel.getLegendStyleController();
		LegendItemStyleToken style = this.legendItemStylePanel.getLegendItemSyle();

		return controller.getSwatchColor(style);
	}

	@Override
	public void update()
	{
		super.update();

		this.legendItemStylePanel.getLegendStyleController();
		String text = LegendStyleController.getText(this.legendItemStylePanel.getLegendItemSyle());

		if (text != null && text.length() > 0)
		{
			this.rename(text);
		}
		else
		{
			this.rename(" ");
		}
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

		this.legendItemStylePanel.handleGUIEvent(event.getGUIEvent());
	}

	@Override
	public Link getLink()
	{
		return this.legendItemStylePanel.getLink();
	}

	@Override
	public void setLink(Link link)
	{
		this.legendItemStylePanel.setLink(link);
	}
}
