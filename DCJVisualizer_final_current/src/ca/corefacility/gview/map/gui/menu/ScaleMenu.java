package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowScaleDialogAction;
import ca.corefacility.gview.map.gui.action.map.scale.ScaleCustomAction;
import ca.corefacility.gview.map.gui.action.map.scale.ScaleInAction;
import ca.corefacility.gview.map.gui.action.map.scale.ScaleOutAction;

/**
 * Responsible for creating the Scale menu.
 * 
 * @author Eric Marinier
 *
 */
public class ScaleMenu extends JMenu implements ActionListener
{	
	private static final long serialVersionUID = 1L;
	
	private final JCheckBoxMenuItem[] checkItems; 
	
	private final GUIController guiController;

	public ScaleMenu(GUIController guiController)
	{
		super(GUIUtility.SCALE_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;			
		this.checkItems = new JCheckBoxMenuItem[GUIUtility.specificScaleItems.length];
		
		addMenuItems();
	}
	
	/**
	 * Adds the individual menu items to the scale menu.
	 */
	private void addMenuItems()
	{
		JMenuItem currMenuItem;		
		
		currMenuItem = new JMenuItem(GUIUtility.SCALE_IN);
		currMenuItem.setActionCommand(GUIUtility.SCALE_IN);
		currMenuItem.addActionListener(this);
		this.add(currMenuItem);
		
		currMenuItem = new JMenuItem(GUIUtility.SCALE_OUT);
		currMenuItem.addActionListener(this);
		this.add(currMenuItem);
		
		this.add(new JSeparator());
		
		//Add the items to the menu and checkItems
		for (int i = 0; i < GUIUtility.specificScaleItems.length; i++)
		{
			currMenuItem = new JCheckBoxMenuItem(GUIUtility.specificScaleItems[i]);
			currMenuItem.addActionListener(this);
			this.add(currMenuItem);
			
			if(currMenuItem instanceof JCheckBoxMenuItem)
				checkItems[i] = (JCheckBoxMenuItem)currMenuItem;
		}
		
		this.add(new JSeparator());
		
		currMenuItem = new JMenuItem(GUIUtility.CUSTOM_TEXT);
		currMenuItem.setActionCommand(GUIUtility.SCALE_CUSTOM);
		currMenuItem.setAccelerator(KeyStroke.getKeyStroke(GUIUtility.SCALE_CUSTOM_SHORTCUT));
		currMenuItem.addActionListener(this);
		this.add(currMenuItem);		
	}
	
	/**
	 * Updates the menu by comparing all of the 'checkable' items against the current scale level and sets the 'check' state appropriately for each.
	 */
	public void updateMenu()
	{
		double currScaleLevel = (this.guiController.getCurrentStyleMapManager().getZoomNormalFactor());
		
		for (int i = 0; i < GUIUtility.specificScaleLevels.length; i++)
		{	
			if((int)Math.round((currScaleLevel * GUIUtility.SCALE_FACTOR)) != GUIUtility.specificScaleLevels[i])
			{
				checkItems[i].setSelected(false);
			}
			else
			{
				checkItems[i].setSelected(true);
			}
		}	
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (GUIUtility.SCALE_IN.equals(e.getActionCommand()))
		{
			(new ScaleInAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if (GUIUtility.SCALE_OUT.equals(e.getActionCommand()))
		{
			(new ScaleOutAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if (GUIUtility.SCALE_10.equals(e.getActionCommand()))
		{
			(new ScaleCustomAction(this.guiController.getCurrentStyleMapManager(), 10.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.SCALE_25.equals(e.getActionCommand()))
		{
			(new ScaleCustomAction(this.guiController.getCurrentStyleMapManager(), 25.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.SCALE_50.equals(e.getActionCommand()))
		{
			(new ScaleCustomAction(this.guiController.getCurrentStyleMapManager(), 50.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.SCALE_100.equals(e.getActionCommand()))
		{
			(new ScaleCustomAction(this.guiController.getCurrentStyleMapManager(), 100.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.SCALE_200.equals(e.getActionCommand()))
		{
			(new ScaleCustomAction(this.guiController.getCurrentStyleMapManager(), 200.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.SCALE_400.equals(e.getActionCommand()))
		{
			(new ScaleCustomAction(this.guiController.getCurrentStyleMapManager(), 400.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.SCALE_800.equals(e.getActionCommand()))
		{
			(new ScaleCustomAction(this.guiController.getCurrentStyleMapManager(), 800.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.SCALE_1600.equals(e.getActionCommand()))
		{
			(new ScaleCustomAction(this.guiController.getCurrentStyleMapManager(), 1600.0/GUIUtility.SCALE_FACTOR)).run();
		}		
		else if( (GUIUtility.SCALE_CUSTOM).equals(e.getActionCommand()) )
		{	
			(new ShowScaleDialogAction(this.guiController.getGViewGUIFrame().getScaleDialog())).run();
		}		
	}
}
