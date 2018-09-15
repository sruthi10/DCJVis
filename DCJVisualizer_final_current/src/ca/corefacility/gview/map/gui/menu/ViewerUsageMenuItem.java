package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.system.OpenURLAction;

/**
 * The Viewer Usage menu item.
 * 
 * @author Eric Marinier
 *
 */
public class ViewerUsageMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	public ViewerUsageMenuItem()
	{
		super(GUIUtility.VIEWER_USAGE_TEXT);
		
		this.addActionListener(this);
		this.setActionCommand(GUIUtility.VIEWER_USAGE);
		this.setAccelerator(KeyStroke.getKeyStroke(GUIUtility.HELP_SHORTCUT));
	}

	@Override
	/**
	 * Listens for the menu item.
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if (GUIUtility.VIEWER_USAGE.equals(e.getActionCommand()))
		{
			(new OpenURLAction(GUIUtility.USAGE_WEBSITE)).run();
		}
	}
}
