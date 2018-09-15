package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.system.OpenAction;

/**
 * Responsible for creating the Open menu item.
 * 
 * @author Eric Marinier
 *
 */
public class OpenMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	public OpenMenuItem()
	{
		super(GUIUtility.OPEN_TEXT);
		
		this.setActionCommand(GUIUtility.OPEN);
		this.setAccelerator(KeyStroke.getKeyStroke(GUIUtility.OPEN_SHORTCUT));
		this.addActionListener(this);
	}

	@Override
	/**
	 * Listens for open dialog events.
	 */
	public void actionPerformed(ActionEvent e) 
	{		
		if (GUIUtility.OPEN.equals(e.getActionCommand()))
		{
			(new OpenAction()).run();
		}		
	}
}
