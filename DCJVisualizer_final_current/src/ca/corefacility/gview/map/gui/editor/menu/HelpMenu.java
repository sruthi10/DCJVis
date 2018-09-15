package ca.corefacility.gview.map.gui.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;

/**
 * The help menu for the style editor.
 * 
 * @author Eric Marinier
 *
 */
public class HelpMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;	//requested by java	
	
	private final JMenuItem helpPageItem;	//help page menu item
	
	private static final String HELP = "Help";
	
	public HelpMenu()
	{
		super(StyleEditorUtility.HELP_TEXT);
		
		this.helpPageItem = new JMenuItem(StyleEditorUtility.HELP_PAGE_TEXT);
		this.helpPageItem.setActionCommand(HELP);
		this.helpPageItem.addActionListener(this);
		
		add(this.helpPageItem);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals(HELP))
		{
			URI uri;
			
			try
			{
				uri = new URI(StyleEditorUtility.gViewWebsite);
				java.awt.Desktop.getDesktop().browse(uri);
			}
			catch (URISyntaxException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (IOException e2)
			{
				e2.printStackTrace();
			}			
		}
	}	
}
