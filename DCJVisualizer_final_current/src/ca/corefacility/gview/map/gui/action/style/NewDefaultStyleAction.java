package ca.corefacility.gview.map.gui.action.style;

import ca.corefacility.gview.data.GenomeDataImp;
import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.Style;
import ca.corefacility.gview.style.StyleFactory;

/**
 * New style action.
 * 
 * Creates a new default style.
 * 
 * @author Eric Marinier
 *
 */
public class NewDefaultStyleAction extends NewStyleAction
{
	public NewDefaultStyleAction(GUIController guiController)
	{		
		super(guiController);
	}

	@Override
	public Style createStyle(GenomeDataImp genomeData)
	{
		return StyleFactory.createNewDefaultStyle(genomeData);
	}
}
