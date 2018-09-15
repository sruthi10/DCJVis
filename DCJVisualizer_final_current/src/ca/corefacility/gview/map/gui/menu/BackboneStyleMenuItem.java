package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ca.corefacility.gview.map.controllers.BackboneStyleController;
import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowBackboneStyleDialogAction;
import ca.corefacility.gview.map.gui.dialog.StyleDialog;
import ca.corefacility.gview.map.gui.editor.panel.BackbonePanel;

/**
 * The backbone style menu item.
 * Displays a dialog to allow global changes to the backbone style.
 * 
 * @author Eric Marinier
 *
 */
public class BackboneStyleMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private final GUIController guiController;

	public BackboneStyleMenuItem(GUIController guiController)
	{
		super(GUIUtility.BACKBONE_STYLE_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;
		
		this.setActionCommand(GUIUtility.BACKBONE_STYLE);
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		BackboneStyleController controller = this.guiController.getCurrentStyle().getStyleController().getBackboneStyleController();
		BackbonePanel backbonePanel = new BackbonePanel(controller);
		
		if(e.getActionCommand().equals(GUIUtility.BACKBONE_STYLE))
		{
			(new ShowBackboneStyleDialogAction(new StyleDialog(this.guiController, backbonePanel))).run();
		}
	}
}
