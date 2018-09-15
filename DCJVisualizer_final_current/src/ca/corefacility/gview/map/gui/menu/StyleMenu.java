package ca.corefacility.gview.map.gui.menu;

import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.Style;

/**
 * The Style menu.
 * 
 * @author Eric Marinier
 *
 */
public class StyleMenu extends JMenu implements MenuListener
{
	private static final long serialVersionUID = 1L;
	private static final String STYLES_TEXT = "Styles";
	
	private final JMenu stylesMenu;
	
	//Items in the File menu.
	private final StyleEditorMenuItem styleEditorItem;
	
	private final GUIController guiController;
	
	public StyleMenu(GUIController guiController)
	{
		super(GUIUtility.STYLE_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;
		
		//Styles Sub Menu
		this.stylesMenu = new JMenu(STYLES_TEXT);
		this.add(this.stylesMenu);
		
		this.addSeparator();
		
		//Style Editor Menu Item
		this.styleEditorItem = new StyleEditorMenuItem(guiController);
		this.add(this.styleEditorItem);
		
		this.addMenuListener(this);
	}
	
	/**
	 * Updates the styles menu.
	 */
	public void update()
	{
		StyleMenuItem currentMenuItem;
		Style style;
		ArrayList<Style> styles = this.guiController.getStyles();
		
		this.stylesMenu.removeAll();
		
		for(int i = 0; i < styles.size(); i++)
		{
			style = styles.get(i);
			currentMenuItem = new StyleMenuItem(this.guiController, style);			
			
			if(style.equals(this.guiController.getCurrentStyle()))
			{
				currentMenuItem.setSelected(true);
			}
			else
			{
				currentMenuItem.setSelected(false);
			}
			
			this.stylesMenu.add(currentMenuItem);
		}
	}

	@Override
	public void menuSelected(MenuEvent e)
	{
		update();	
	}

	@Override
	public void menuDeselected(MenuEvent e){}

	@Override
	public void menuCanceled(MenuEvent e){}
}
