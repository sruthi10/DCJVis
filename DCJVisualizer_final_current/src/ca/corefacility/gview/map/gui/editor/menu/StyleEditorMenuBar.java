package ca.corefacility.gview.map.gui.editor.menu;

import javax.swing.JMenuBar;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.editor.StyleMenu;

/**
 * The menu bar for the style editor.
 * 
 * @author Eric Marinier
 * 
 */
public class StyleEditorMenuBar extends JMenuBar
{
	private static final long serialVersionUID = 1L; // requested by java

	private final FileMenu fileMenu; // the file submenu
	private final StyleMenu styleMenu; // the style submenu
	private final HelpMenu helpMenu; // the help submenu
	private final ViewMenu viewMenu; // the view submenu
	private final SelectionMenu selectionMenu; // the selection submenu

	public StyleEditorMenuBar(GUIController guiController)
	{
		super();

		if (guiController == null)
			throw new IllegalArgumentException("GUIController is null");

		// Add the File Menu
		fileMenu = new FileMenu(guiController);
		add(fileMenu);

		viewMenu = new ViewMenu(guiController);
		add(viewMenu);

		// Add the File Menu
		styleMenu = new StyleMenu(guiController);
		add(styleMenu);

		// Add the selection menu
		this.selectionMenu = new SelectionMenu(guiController.getStyleEditor());
		this.add(this.selectionMenu);

		// Add the help menu
		helpMenu = new HelpMenu();
		add(helpMenu);

	}
}
