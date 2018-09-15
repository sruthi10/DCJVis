package ca.corefacility.gview.map.gui.editor.node;

import java.awt.Paint;

import ca.corefacility.gview.map.controllers.LegendStyleController;
import ca.corefacility.gview.map.controllers.LegendStyleToken;
import ca.corefacility.gview.map.gui.editor.panel.LegendPanel;

/**
 * The node class for the legend style.
 * 
 * Intended to be used within a StyleEditorTree.
 * 
 * @author Eric Marinier
 * 
 */
public class LegendStyleNode extends StyleEditorNode
{
	private static final long serialVersionUID = 1L; // requested by java
	private static final String LEGEND_STYLE = "Legend";

	private final LegendPanel legendStylePanel;

	/**
	 * 
	 * @param legendStylePanel
	 *            The related panel.
	 */
	public LegendStyleNode(LegendPanel legendStylePanel)
	{
		super(legendStylePanel, LEGEND_STYLE);

		if (legendStylePanel == null)
			throw new IllegalArgumentException("LegendStylePanel is null");

		this.legendStylePanel = legendStylePanel;
	}

	@Override
	public LegendPanel getPanel()
	{
		if (this.legendStylePanel == null)
			throw new IllegalArgumentException("LegendStylePanel is null");

		return this.legendStylePanel;
	}

	/**
	 * 
	 * @return The legend style.
	 */
	public LegendStyleToken getLegendStyle()
	{
		return this.legendStylePanel.getLegendStyle();
	}

	/**
	 * 
	 * @return The colour of the node.
	 */
	public Paint getNodeColor()
	{
		LegendStyleController controller = this.legendStylePanel.getLegendStyleController();
		LegendStyleToken style = this.legendStylePanel.getLegendStyle();

		return controller.getBackgroundColor(style);
	}
}
