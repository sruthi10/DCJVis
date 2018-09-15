package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.system.OpenURLAction;

/**
 * The website menu item.
 * 
 * @author Eric Marinier
 *
 */
public class WebsiteMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor.
	 */
	public WebsiteMenuItem()
	{
		super(GUIUtility.WEBSITE_TEXT);
		
		this.addActionListener(this);
		this.setActionCommand(GUIUtility.WEBSITE);
	}

	@Override
	/**
	 * Listens for the menu item.
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if (GUIUtility.WEBSITE.equals(e.getActionCommand()))
		{
			(new OpenURLAction(GUIUtility.URL)).run();
		}
	}
}
