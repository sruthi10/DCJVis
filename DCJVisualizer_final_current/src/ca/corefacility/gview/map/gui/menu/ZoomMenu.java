package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.ZoomEvent;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowZoomDialogAction;
import ca.corefacility.gview.map.gui.action.map.zoom.ZoomCustomAction;
import ca.corefacility.gview.map.gui.action.map.zoom.ZoomInAction;
import ca.corefacility.gview.map.gui.action.map.zoom.ZoomOutAction;

/**
 * Responsible for creating and managing the Zoom menu on GView's menu bar.
 * 
 * @author Eric Marinier
 *
 */
public class ZoomMenu extends JMenu implements ActionListener, GViewEventListener
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private final GUIController guiController;	
	private final JCheckBoxMenuItem[] checkItems;
	
	private JMenuItem zoomIn;
	private JMenuItem zoomOut;

	public ZoomMenu(GUIController guiController)
	{
		super(GUIUtility.ZOOM_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;	
		
		this.checkItems = new JCheckBoxMenuItem[GUIUtility.specificZoomItems.length];
		
		addMenuItems();
		
		//Needs to be after menu items are created!
		this.guiController.addEventListener(this);
		updateZoom(this.guiController.getCurrentStyleMapManager().getZoomFactor());
	}
	
	/**
	 * Adds the individual menu items to the zoom sub menu.
	 */
	private void addMenuItems()
	{
		JMenuItem currMenuItem;
		
		this.zoomIn = new JMenuItem(GUIUtility.ZOOM_IN);
		this.zoomIn.addActionListener(this);		
		this.add(this.zoomIn);
		
		this.zoomOut = new JMenuItem(GUIUtility.ZOOM_OUT);		
		this.zoomOut.addActionListener(this);
		this.add(this.zoomOut);
		
		this.add(new JSeparator());		
		
		//Add the items to the menu and checkItems
		for (int i = 0; i < GUIUtility.specificZoomItems.length; i++)
		{
			currMenuItem = new JCheckBoxMenuItem(GUIUtility.specificZoomItems[i]);
			currMenuItem.addActionListener(this);
			this.add(currMenuItem);
			
			if(currMenuItem instanceof JCheckBoxMenuItem)
				checkItems[i] = (JCheckBoxMenuItem)currMenuItem;
		}
		
		this.add(new JSeparator());
		
		currMenuItem = new JMenuItem(GUIUtility.CUSTOM_TEXT);
		currMenuItem.setActionCommand(GUIUtility.ZOOM_CUSTOM);
		currMenuItem.setAccelerator(KeyStroke.getKeyStroke(GUIUtility.ZOOM_CUSTOM_SHORTCUT));
		currMenuItem.addActionListener(this);
		this.add(currMenuItem);
	}
	
	/**
	 * Updates the menu by comparing all of the 'checkable' items against the current zoom level and sets the 'check' state appropriately for each.
	 */
	public void updateMenu()
	{
		int currZoomLevel = (int)(this.guiController.getCurrentStyleMapManager().getZoomFactor() * GUIUtility.SCALE_FACTOR); // to coordinate with %'s, also rounding to ints
		
		for (int i = 0; i < GUIUtility.specificZoomLevels.length; i++)
		{	
			if(currZoomLevel != GUIUtility.specificZoomLevels[i])
			{
				checkItems[i].setSelected(false);
			}
			else
			{
				checkItems[i].setSelected(true);
			}
		}	
		
		updateZoom(this.guiController.getCurrentStyleMapManager().getZoomFactor());
	}

	@Override
	/**
	 * Listens for the menu items.
	 */
	public void actionPerformed(ActionEvent e)
	{		
		if (GUIUtility.ZOOM_IN.equals(e.getActionCommand()))
		{
			(new ZoomInAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if (GUIUtility.ZOOM_OUT.equals(e.getActionCommand()))
		{
			(new ZoomOutAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if (GUIUtility.ZOOM_100.equals(e.getActionCommand()))
		{
			(new ZoomCustomAction(this.guiController.getCurrentStyleMapManager(), 100.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.ZOOM_200.equals(e.getActionCommand()))
		{
			(new ZoomCustomAction(this.guiController.getCurrentStyleMapManager(), 200.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.ZOOM_400.equals(e.getActionCommand()))
		{
			(new ZoomCustomAction(this.guiController.getCurrentStyleMapManager(), 400.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.ZOOM_800.equals(e.getActionCommand()))
		{
			(new ZoomCustomAction(this.guiController.getCurrentStyleMapManager(), 800.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.ZOOM_1600.equals(e.getActionCommand()))
		{
			(new ZoomCustomAction(this.guiController.getCurrentStyleMapManager(), 1600.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.ZOOM_3200.equals(e.getActionCommand()))
		{
			(new ZoomCustomAction(this.guiController.getCurrentStyleMapManager(), 3200.0/GUIUtility.SCALE_FACTOR)).run();
		}
		else if (GUIUtility.ZOOM_CUSTOM.equals(e.getActionCommand()))
		{
			(new ShowZoomDialogAction(this.guiController.getGViewGUIFrame().getZoomDialog())).run();
		}	
	}
	
	@Override
	public void eventOccured(GViewEvent event)
	{		
		//Zooming
		if(event instanceof ZoomEvent)
		{
			double zoomFactor = ((ZoomEvent)event).getScale();
			
			updateZoom(zoomFactor);
		}
	}
	
	public void updateZoom(double zoomFactor)
	{
		if(zoomFactor >= this.guiController.getCurrentStyleMapManager().getMaxZoomFactor())
		{
			this.zoomIn.setEnabled(false);
			this.zoomOut.setEnabled(true);
		}
		else if(zoomFactor <= this.guiController.getCurrentStyleMapManager().getMinZoomFactor())
		{
			this.zoomIn.setEnabled(true);
			this.zoomOut.setEnabled(false);
		}
		else
		{
			this.zoomIn.setEnabled(true);
			this.zoomOut.setEnabled(true);
		}
	}
}
