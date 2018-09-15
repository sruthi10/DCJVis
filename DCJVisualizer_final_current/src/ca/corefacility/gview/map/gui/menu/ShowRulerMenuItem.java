package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.map.hide.HideRulerAction;
import ca.corefacility.gview.map.gui.action.map.show.ShowRulerAction;

/**
 * Responsible for creating and managing the "Show Ruler" menu item.
 * 
 * @author Eric Marinier
 *
 */
public class ShowRulerMenuItem extends JCheckBoxMenuItem implements ItemListener
{
	private static final long serialVersionUID = 1L;
	
	private final GUIController guiController;
	
	public ShowRulerMenuItem(GUIController guiController)
	{
		super(GUIUtility.SHOW_RULER_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;		
		this.addItemListener(this);
	}

	@Override
	/**
	 * Listens for the menu item.
	 */
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			(new ShowRulerAction(this.guiController.getCurrentStyleMapManager().getElementControl())).run();
		}
		else if (e.getStateChange() == ItemEvent.DESELECTED)
		{
			(new HideRulerAction(this.guiController.getCurrentStyleMapManager().getElementControl())).run();
		}
	}
	
	/**
	 * Updates the selected state of the menu item.
	 */
	public void update()
	{
		if(this.guiController.getCurrentStyleMapManager().getElementControl().getRulerDisplayed())
		{
			this.setSelected(true);
		}
		else
		{
			this.setSelected(false);
		}			
	}	
}
