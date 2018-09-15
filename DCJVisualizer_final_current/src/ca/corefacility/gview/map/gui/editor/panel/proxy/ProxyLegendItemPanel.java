package ca.corefacility.gview.map.gui.editor.panel.proxy;

import ca.corefacility.gview.map.controllers.LegendItemStyleToken;
import ca.corefacility.gview.map.controllers.LegendStyleController;
import ca.corefacility.gview.map.controllers.LegendStyleToken;
import ca.corefacility.gview.map.controllers.link.LinkController;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.panel.LegendItemPanel;

public class ProxyLegendItemPanel extends LegendItemPanel
{
	private static final long serialVersionUID = 1L;

	public ProxyLegendItemPanel(LegendStyleController legendController, LegendStyleToken legendStyle,
			LegendItemStyleToken legendItemStyle, LinkController linkController, SelectionController selectionController)
	{
		super(legendController, legendStyle, legendItemStyle, linkController, selectionController);
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
