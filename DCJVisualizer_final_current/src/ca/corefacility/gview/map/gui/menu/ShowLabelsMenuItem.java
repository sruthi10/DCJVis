package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.map.hide.HideLabelsAction;
import ca.corefacility.gview.map.gui.action.map.show.ShowLabelsAction;

/**
 * Responsible for creating and managing the "Show Labels" menu item.
 * 
 * @author Eric Marinier
 *
 */
public class ShowLabelsMenuItem extends JCheckBoxMenuItem implements ItemListener
{
	private static final long serialVersionUID = 1L;
	
	private final GUIController guiController;
	
	public ShowLabelsMenuItem(GUIController guiController)
	{
		super(GUIUtility.SHOW_LABELS_TEXT);
		
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
			(new ShowLabelsAction(this.guiController.getCurrentStyleMapManager().getElementControl())).run();
		}
		else if (e.getStateChange() == ItemEvent.DESELECTED)
		{
			(new HideLabelsAction(this.guiController.getCurrentStyleMapManager().getElementControl())).run();
		}
	}
	
	/**
	 * Updates the selected state of the menu item.
	 */
	public void update()
	{
		if(this.guiController.getCurrentStyleMapManager().getElementControl().getLabelsDisplayed())
		{
			this.setSelected(true);
		}
		else
		{
			this.setSelected(false);
		}			
	}	
}
