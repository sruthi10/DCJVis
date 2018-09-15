package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.controllers.GlobalStyleController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowGlobalStyleDialogAction;
import ca.corefacility.gview.map.gui.dialog.StyleDialog;
import ca.corefacility.gview.map.gui.editor.panel.GlobalPanel;

/**
 * The global style menu item.
 * Displays a dialog to allow global changes to the global style.
 * 
 * @author Eric Marinier
 *
 */
public class GlobalStyleMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private final GUIController guiController;	

	public GlobalStyleMenuItem(GUIController guiController)
	{
		super(GUIUtility.GLOBAL_STYLE_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;
		
		this.setActionCommand(GUIUtility.GLOBAL_STYLE);
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		GlobalStyleController controller = this.guiController.getCurrentStyle().getStyleController().getGlobalStyleController();
		GlobalPanel globalPanel = new GlobalPanel(controller);
		
		if(e.getActionCommand().equals(GUIUtility.GLOBAL_STYLE))
		{
			(new ShowGlobalStyleDialogAction(new StyleDialog(this.guiController, globalPanel))).run();
		}
	}
}
