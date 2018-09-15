package ca.corefacility.gview.map.gui.editor.panel.proxy;

import ca.corefacility.gview.map.controllers.PropertyMappableToken;
import ca.corefacility.gview.map.controllers.PropertyMapperController;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.panel.propertyMapper.PropertyMapperPanel;

public class ProxyPropertyMapperPanel extends PropertyMapperPanel
{
	private static final long serialVersionUID = 1L;

	public ProxyPropertyMapperPanel(PropertyMapperController controller, PropertyMappableToken propertyMappable,
			SelectionController selectionController)
	{
		super(controller, propertyMappable, selectionController);
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
