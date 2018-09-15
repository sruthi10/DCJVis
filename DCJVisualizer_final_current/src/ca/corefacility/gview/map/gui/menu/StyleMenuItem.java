package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.Style;

public class StyleMenuItem extends JCheckBoxMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private final GUIController guiController;
	private final Style style;
	
	public StyleMenuItem(GUIController guiController, Style style)
	{
		super(style.getName());
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.style = style;
		this.guiController = guiController;
		
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(style == null)
			throw new NullPointerException("StyleEditorTree is null.");
		
		this.guiController.setStyle(this.style);
	}
}
