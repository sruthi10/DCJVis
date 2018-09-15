package ca.corefacility.gview.map.gui.editor.panel.proxy;

import ca.corefacility.gview.map.controllers.SlotStyleController;
import ca.corefacility.gview.map.controllers.SlotStyleToken;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.panel.SlotPanel;

public class ProxySlotPanel extends SlotPanel
{
	private static final long serialVersionUID = 1L;

	public ProxySlotPanel(SlotStyleController controller, SlotStyleToken slotStyle,
			SelectionController selectionController)
	{
		super(controller, slotStyle, selectionController);
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
