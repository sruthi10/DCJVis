package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.map.hide.HideAllAction;
import ca.corefacility.gview.map.gui.action.map.show.ShowAllAction;

/**
 * Responsible for creating and managing the "Show All" menu item.
 * 
 * @author Eric Marinier
 *
 */
public class ShowAllMenuItem extends JCheckBoxMenuItem implements ItemListener
{
	private static final long serialVersionUID = 1L;
	
	private final GUIController guiController;
	
	private boolean updating = false;	

	public ShowAllMenuItem(GUIController guiController)
	{
		super(GUIUtility.SHOW_ALL_TEXT);
		
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
		if (e.getStateChange() == ItemEvent.SELECTED && !this.updating)
		{
			(new ShowAllAction(this.guiController.getCurrentStyleMapManager().getElementControl())).run();
		}
		else if (e.getStateChange() == ItemEvent.DESELECTED && !this.updating)
		{
			if(this.guiController.getCurrentStyleMapManager().getElementControl().getAllDisplayed())
			{
				(new HideAllAction(this.guiController.getCurrentStyleMapManager().getElementControl())).run();
			}
		}
	}
	
	/**
	 * Updates the selected state of the menu item.
	 */
	public void update()
	{
		if(this.guiController.getCurrentStyleMapManager().getElementControl().getAllDisplayed())
		{
			this.updating = true;
			this.setSelected(true);
			this.updating = false;
		}
		else
		{
			this.updating = true;
			this.setSelected(false);
			this.updating = false;
		}			
	}	
}
