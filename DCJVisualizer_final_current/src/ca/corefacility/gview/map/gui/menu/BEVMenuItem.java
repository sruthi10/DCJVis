package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.KeyStroke;

import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.GViewGUIFrame;
import ca.corefacility.gview.map.gui.action.dialog.hide.HideBEVDialogAction;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowBEVDialogAction;

/**
 * Responsible for creating and managing the Bird's Eye View menu item.
 * 
 * @author Eric Marinier
 *
 */
public class BEVMenuItem extends JCheckBoxMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private final GViewGUIFrame gViewGUIFrame;
	
	/**
	 * Creates a new BEV menu item within the specified frame.
	 * @param gViewGUIFrame The frame this menu item is to be placed in.
	 */
	public BEVMenuItem(GViewGUIFrame gViewGUIFrame)
	{
		super(GUIUtility.BEV_TEXT);
		
		if(gViewGUIFrame == null)
			throw new IllegalArgumentException("GViewGUIFrame is null.");
		
		this.gViewGUIFrame = gViewGUIFrame;		
		
		this.setAccelerator(KeyStroke.getKeyStroke(GUIUtility.BEV_SHORTCUT));
		this.setActionCommand(GUIUtility.BEV);
		this.addActionListener(this);
	}

	@Override
	/**
	 * Listens for the BEV Menu item being clicked.
	 */
	public void actionPerformed(ActionEvent e)
	{		
		if (e.getActionCommand().equals(GUIUtility.BEV))
		{
			if (!this.gViewGUIFrame.getBEVDialog().isVisible())
			{
				(new ShowBEVDialogAction(this.gViewGUIFrame.getBEVDialog())).run();
			}
			else
			{
				(new HideBEVDialogAction(this.gViewGUIFrame.getBEVDialog())).run();
			}
		}
	}

	/**
	 * Updates the BEV menu.
	 */
	public void update()
	{
		boolean isDisplayed = this.gViewGUIFrame.getBEVDialog().isVisible();
		
		this.setSelected(isDisplayed);
	}	
}
