package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.map.FITMAction;

/**
 * Responsible for creating the "Fit Image to Map" menu item.
 * 
 * @author Eric Marinier
 *
 */
public class FitMapToScreenMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private final GUIController guiController;
	
	public FitMapToScreenMenuItem(GUIController guiController)
	{
		super(GUIUtility.FMTS_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;
		
		this.setActionCommand(GUIUtility.FMTS);
		this.setAccelerator(KeyStroke.getKeyStroke(GUIUtility.FMTS_SHORTCUT));
		this.addActionListener(this);
	}

	@Override
	/**
	 * Listens for "Fit Image to Map" actions.
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if (GUIUtility.FMTS.equals(e.getActionCommand()))
		{
			(new FITMAction(this.guiController.getCurrentStyleMapManager())).run();
		}		
	}
}
