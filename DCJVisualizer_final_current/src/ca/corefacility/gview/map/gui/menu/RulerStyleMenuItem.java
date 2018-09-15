package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.controllers.RulerStyleController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowRulerStyleDialogAction;
import ca.corefacility.gview.map.gui.dialog.StyleDialog;
import ca.corefacility.gview.map.gui.editor.panel.RulerPanel;

/**
 * The ruler style menu item.
 * Displays a dialog to allow global changes to the ruler style.
 * 
 * @author Eric Marinier
 *
 */
public class RulerStyleMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L; //requested by java	
	
	private final GUIController guiController;	

	public RulerStyleMenuItem(GUIController guiController)
	{
		super(GUIUtility.RULER_STYLE_TEXT);	
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;
		
		this.setActionCommand(GUIUtility.RULER_STYLE);
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{	
		RulerStyleController controller = this.guiController.getCurrentStyle().getStyleController().getRulerStyleController();
		RulerPanel rulerPanel = new RulerPanel(controller);
		
		if(e.getActionCommand().equals(GUIUtility.RULER_STYLE))
		{
			(new ShowRulerStyleDialogAction(new StyleDialog(this.guiController, rulerPanel))).run() ;
		}
	}
}
