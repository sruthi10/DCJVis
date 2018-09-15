package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.map.ExportAction;

/**
 * Responsible for creating the Export menu item.
 * 
 * @author Eric Marinier
 * 
 */
public class ExportMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private final GUIController guiController;

	public ExportMenuItem(GUIController guiController)
	{
		super(GUIUtility.EXPORT_TEXT);

		if (guiController == null)
			throw new IllegalArgumentException("GUIController is null.");

		this.guiController = guiController;

		this.setActionCommand(GUIUtility.EXPORT);
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (GUIUtility.EXPORT.equals(e.getActionCommand()))
		{
			(new ExportAction(this.guiController.getCurrentStyleMapManager())).run();
		}
	}
}
