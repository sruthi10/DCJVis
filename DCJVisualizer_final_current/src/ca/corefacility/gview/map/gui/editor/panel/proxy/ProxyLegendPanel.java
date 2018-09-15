package ca.corefacility.gview.map.gui.editor.panel.proxy;

import ca.corefacility.gview.map.controllers.LegendStyleController;
import ca.corefacility.gview.map.controllers.LegendStyleToken;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.panel.LegendPanel;

public class ProxyLegendPanel extends LegendPanel
{
	private static final long serialVersionUID = 1L;

	public ProxyLegendPanel(LegendStyleController controller, LegendStyleToken legendStyle,
			SelectionController selectionController)
	{
		super(controller, legendStyle, selectionController);
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
