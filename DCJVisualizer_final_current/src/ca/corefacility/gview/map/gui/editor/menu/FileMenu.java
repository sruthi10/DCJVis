package ca.corefacility.gview.map.gui.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.action.style.ExportStyleAction;
import ca.corefacility.gview.map.gui.action.style.ImportStyleAction;
import ca.corefacility.gview.map.gui.action.style.NewBlankStyleAction;
import ca.corefacility.gview.map.gui.action.style.NewDefaultStyleAction;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;


/**
 * The file menu class.
 * 
 * This file menu is intended to exist on a StyleEditorFrame menu.
 * 
 * @author Eric Marinier
 *
 */
public class FileMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private static final String EXIT_TEXT = "Exit";	//exit text
	private static final String EXIT = "Exit";	//exit action command
	private static final String OPEN_TEXT = "Open";	//load text
	private static final String OPEN = "Open";	//load action command
	private static final String SAVE_TEXT = "Save";	//save text
	private static final String SAVE = "Save";	//save action command
	private static final String NEW_DEFAULT_STYLE_TEXT = "Default Style";	//new default style text
	private static final String NEW_BLANK_STYLE_TEXT = "Blank Style";	//new blank style text
	private static final String NEW_TEXT = "New";	//the new menu text
	private static final String NEW_DEFAULT_STYLE = "New Default Style";	//new default style action command
	private static final String NEW_BLANK_STYLE = "New Blank Style";	//new blank action command

	private static final String LOAD_SHORTCUT = "ctrl O";
	private static final String SAVE_SHORTCUT = "ctrl S";
	
	private final GUIController guiController;
	
	private final JMenuItem exitItem;
	private final JMenuItem loadItem;
	private final JMenuItem saveItem;
	
	private final JMenu newMenu;
	private final JMenuItem newDefaultStyleItem;
	private final JMenuItem newBlankStyleItem;
	
	private static final String NEW_STYLE_SHORTCUT = "ctrl N";
	
	public FileMenu(GUIController guiController)
	{
		super(StyleEditorUtility.FILE_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null");
		
		this.guiController = guiController;
		
		//New Menu
		this.newMenu = new JMenu(NEW_TEXT);
		
		//New Style item
		this.newDefaultStyleItem = new JMenuItem(NEW_DEFAULT_STYLE_TEXT);
		this.newDefaultStyleItem.setActionCommand(NEW_DEFAULT_STYLE);
		this.newDefaultStyleItem.addActionListener(this);
		
		//New Style item
		this.newBlankStyleItem = new JMenuItem(NEW_BLANK_STYLE_TEXT);
		this.newBlankStyleItem.setActionCommand(NEW_BLANK_STYLE);
		this.newBlankStyleItem.addActionListener(this);
		this.newBlankStyleItem.setAccelerator(KeyStroke.getKeyStroke(NEW_STYLE_SHORTCUT));
		
		//Exit item
		this.exitItem = new JMenuItem(EXIT_TEXT);
		this.exitItem.setActionCommand(EXIT);
		this.exitItem.addActionListener(this);
		
		//Load item
		this.loadItem = new JMenuItem(OPEN_TEXT);
		this.loadItem.setActionCommand(OPEN);
		this.loadItem.addActionListener(this);
		this.loadItem.setAccelerator(KeyStroke.getKeyStroke(LOAD_SHORTCUT));
		
		//Save item
		this.saveItem = new JMenuItem(SAVE_TEXT);
		this.saveItem.setActionCommand(SAVE);
		this.saveItem.addActionListener(this);
		this.saveItem.setAccelerator(KeyStroke.getKeyStroke(SAVE_SHORTCUT));
		
		//add items to new menu
		this.newMenu.add(this.newBlankStyleItem);
		this.newMenu.add(this.newDefaultStyleItem);
		
		//Add items to file menu
		this.add(this.newMenu);
		this.addSeparator();
		this.add(this.loadItem);
		this.add(this.saveItem);
		this.addSeparator();
		this.add(this.exitItem);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{		
		if(e.getActionCommand().equals(EXIT))
		{
			this.guiController.setStyleEditorVisibile(false);
		}	
		else if(e.getActionCommand().equals(OPEN))
		{
			(new ImportStyleAction(this.guiController)).run();
		}
		else if(e.getActionCommand().equals(SAVE))
		{
			(new ExportStyleAction(this.guiController)).run();
		}
		else if(e.getActionCommand().equals(NEW_DEFAULT_STYLE))
		{
			(new NewDefaultStyleAction(this.guiController)).run();
		}
		else if(e.getActionCommand().equals(NEW_BLANK_STYLE))
		{
			(new NewBlankStyleAction(this.guiController)).run();
		}
	}	
}
