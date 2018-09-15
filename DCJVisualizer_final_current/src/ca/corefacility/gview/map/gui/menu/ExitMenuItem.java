package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.system.SystemExitAction;

/**
 * Creates the Exit menu item.
 * 
 * @author Eric Marinier
 *
 */
public class ExitMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	public ExitMenuItem()
	{
		super(GUIUtility.EXIT_TEXT);
		
		this.setActionCommand(GUIUtility.EXIT);
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(GUIUtility.EXIT_TEXT.equals(e.getActionCommand()))
		{
			(new SystemExitAction()).run();
		}
	}
}
