package ca.corefacility.gview.map.gui.editor.panel.proxy;

import ca.corefacility.gview.map.controllers.FeatureHolderStyleToken;
import ca.corefacility.gview.map.controllers.GenomeDataController;
import ca.corefacility.gview.map.controllers.SetStyleController;
import ca.corefacility.gview.map.controllers.link.LinkController;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.panel.SetPanel;

public class ProxySetPanel extends SetPanel
{
	private static final long serialVersionUID = 1L;

	public ProxySetPanel(SetStyleController setStyleController, FeatureHolderStyleToken setStyle,
			GenomeDataController genomeDataController, LinkController linkController,
			SelectionController selectionController)
	{
		super(setStyleController, setStyle, genomeDataController, linkController, selectionController);
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

	@Override
	protected void updateThicknessProportionGUI()
	{
		this.setUpdating(true);

		super.updateThicknessProportionGUI();

		this.setUpdating(false);
	}
}
