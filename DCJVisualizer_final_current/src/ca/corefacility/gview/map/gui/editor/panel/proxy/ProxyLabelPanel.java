package ca.corefacility.gview.map.gui.editor.panel.proxy;

import ca.corefacility.gview.map.controllers.FeatureHolderStyleToken;
import ca.corefacility.gview.map.controllers.GenomeDataController;
import ca.corefacility.gview.map.controllers.LabelStyleController;
import ca.corefacility.gview.map.controllers.LabelStyleToken;
import ca.corefacility.gview.map.controllers.SetStyleController;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.panel.LabelPanel;

public class ProxyLabelPanel extends LabelPanel
{
	private static final long serialVersionUID = 1L;

	public ProxyLabelPanel(LabelStyleToken labelStyle, FeatureHolderStyleToken setStyle,
			LabelStyleController labelStyleController, SetStyleController setStyleController,
			GenomeDataController genomeDataController, SelectionController selectionController)
	{
		super(labelStyle, setStyle, labelStyleController, setStyleController, genomeDataController, selectionController);
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
