package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.GViewGUIFrame;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowAboutDialogAction;

/**
 * Responsible for creating the "About" menu item.
 * 
 * @author Eric Marinier
 *
 */
public class AboutMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;	
	private final GViewGUIFrame gViewGUIFrame;
	
	/**
	 * @param gViewGUIFrame The frame object this item is a part of. 
	 */
	public AboutMenuItem(GViewGUIFrame gViewGUIFrame)
	{
		super(GUIUtility.ABOUT_TEXT);
		
		if(gViewGUIFrame == null)
			throw new IllegalArgumentException("GViewGUIFrame is null.");
		
		this.gViewGUIFrame = gViewGUIFrame;
		
		this.setActionCommand(GUIUtility.ABOUT);
		this.addActionListener(this);
	}

	@Override
	/**
	 * Listens for "About" menu item actions.
	 */
	public void actionPerformed(ActionEvent e) 
	{		
		if (GUIUtility.ABOUT.equals(e.getActionCommand()))
		{
			(new ShowAboutDialogAction(gViewGUIFrame.getAboutDialog())).run();
		}		
	}
}
