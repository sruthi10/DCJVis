package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;

/**
 * 
 * 
 * @author Eric Marinier
 *
 */
public class StyleEditorMenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;	
	private final GUIController guiController;
	
	public StyleEditorMenuItem(GUIController guiController)
	{
		super(GUIUtility.STYLE_EDITOR_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;		
		this.addActionListener(this);
		this.setAccelerator(KeyStroke.getKeyStroke(GUIUtility.STYLE_EDITOR_SHORTCUT));
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		this.guiController.setStyleEditorVisibile(true);
	}
}

