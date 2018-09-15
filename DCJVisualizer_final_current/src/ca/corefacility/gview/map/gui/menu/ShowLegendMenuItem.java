package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.KeyStroke;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.map.hide.HideLegendAction;
import ca.corefacility.gview.map.gui.action.map.show.ShowLegendAction;

/**
 * Responsible for creating and managing the "Show Legend" menu item.
 * 
 * @author Eric Marinier
 *
 */
public class ShowLegendMenuItem extends JCheckBoxMenuItem implements ItemListener
{
	private static final long serialVersionUID = 1L;
	
	private final GUIController guiController ;
	
	public ShowLegendMenuItem(GUIController guiController)
	{
		super(GUIUtility.SHOW_LEGEND_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;
		
		this.setAccelerator(KeyStroke.getKeyStroke(GUIUtility.LEGEND_SHORTCUT));
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
			(new ShowLegendAction(this.guiController.getCurrentStyleMapManager().getElementControl())).run();
		}
		else if (e.getStateChange() == ItemEvent.DESELECTED)
		{
			(new HideLegendAction(this.guiController.getCurrentStyleMapManager().getElementControl())).run();
		}
	}
	
	/**
	 * Updates the selected state of the menu item.
	 */
	public void update()
	{
		if(this.guiController.getCurrentStyleMapManager().getElementControl().getLegendDisplayed())
		{
			this.setSelected(true);
		}
		else
		{
			this.setSelected(false);
		}			
	}
}
