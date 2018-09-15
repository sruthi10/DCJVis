package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.controllers.TooltipStyleController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowTooltipStyleDialogAction;
import ca.corefacility.gview.map.gui.dialog.StyleDialog;
import ca.corefacility.gview.map.gui.editor.panel.TooltipPanel;

/**
 * The tool tip style menu item.
 * Displays a dialog to allow global changes to the tool tip style.
 * 
 * @author Eric Marinier
 *
 */
public class TooltipStyleMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;	//requested by java

	private final GUIController guiController;
	
	public TooltipStyleMenuItem(GUIController guiController)
	{
		super(GUIUtility.TOOLTIP_STYLE_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;
		
		this.setActionCommand(GUIUtility.TOOLTIP_STYLE);
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		TooltipStyleController controller = this.guiController.getCurrentStyle().getStyleController().getTooltipStyleController();
		TooltipPanel tooltipPanel = new TooltipPanel(controller);
		
		if(e.getActionCommand().equals(GUIUtility.TOOLTIP_STYLE))
		{
			(new ShowTooltipStyleDialogAction(new StyleDialog(this.guiController, tooltipPanel))).run();
		}
	}
}
