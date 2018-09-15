package ca.corefacility.gview.map.gui.menu;

import javax.swing.JMenu;
import javax.swing.JSeparator;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;

/**
 * Responsible for creating the File menu.
 * 
 * @author Eric Marinier
 *
 */
public class FileMenu extends JMenu
{
	private static final long serialVersionUID = 1L;
	
	//Items in the File menu.
	private final OpenMenuItem openItem;
	private final ExportMenuItem exportItem;
	private final ExitMenuItem exitItem;
	
	public FileMenu(GUIController guiController)
	{
		super(GUIUtility.FILE_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		//Open Menu Item
		this.openItem = new OpenMenuItem();
		this.add(this.openItem);
		
		//Export Menu Item
		this.exportItem = new ExportMenuItem(guiController);
		this.add(this.exportItem);
		
		this.add(new JSeparator());
		
		//Exit Menu Item
		this.exitItem = new ExitMenuItem();
		this.add(this.exitItem);
	}
}
