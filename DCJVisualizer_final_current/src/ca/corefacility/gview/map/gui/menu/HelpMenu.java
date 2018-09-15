package ca.corefacility.gview.map.gui.menu;

import javax.swing.JMenu;

import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.GViewGUIFrame;

/**
 * Responsible for creating the help menu.
 * 
 * @author Eric Marinier
 *
 */
public class HelpMenu extends JMenu
{
	private static final long serialVersionUID = 1L;
	
	//Items in the Help menu.
	private final ViewerUsageMenuItem usageItem;
	private final WebsiteMenuItem websiteItem;
	private final AboutMenuItem aboutItem;
	
	/**
	 * Creates the help menu.
	 */
	public HelpMenu(GViewGUIFrame gViewGUIFrame)
	{
		super(GUIUtility.HELP_TEXT);
		
		if(gViewGUIFrame == null)
			throw new IllegalArgumentException("GViewGUIFrame is null.");
		
		//Viewer Usage Menu Item
		this.usageItem = new ViewerUsageMenuItem();
		this.add(this.usageItem);
		
		//Website Menu Item
		this.websiteItem = new WebsiteMenuItem();
		this.add(this.websiteItem);
		
		this.addSeparator();
		
		//About Menu item
		this.aboutItem = new AboutMenuItem(gViewGUIFrame);
		this.add(aboutItem);
	}
}
