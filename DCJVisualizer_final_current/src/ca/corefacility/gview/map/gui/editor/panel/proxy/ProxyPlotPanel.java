package ca.corefacility.gview.map.gui.editor.panel.proxy;

import ca.corefacility.gview.map.controllers.PlotStyleController;
import ca.corefacility.gview.map.controllers.PlotStyleToken;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.panel.PlotPanel;

public class ProxyPlotPanel extends PlotPanel
{
	private static final long serialVersionUID = 1L;

	public ProxyPlotPanel(PlotStyleController controller, PlotStyleToken plotStyle,
			SelectionController selectionController)
	{
		super(controller, plotStyle, selectionController);
	}

	@Override
	public void update()
	{
		// Do nothing!
	}

	@Override
	public void doApply()
	{
		// Do nothing!
	}
}
