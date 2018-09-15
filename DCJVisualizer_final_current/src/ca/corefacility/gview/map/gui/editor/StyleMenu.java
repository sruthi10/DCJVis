package ca.corefacility.gview.map.gui.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.action.style.NewLegendAction;
import ca.corefacility.gview.map.gui.action.style.NewPlotAction;
import ca.corefacility.gview.map.gui.action.style.NewSetAction;
import ca.corefacility.gview.map.gui.action.style.NewSlotAction;


/**
 * The edit menu class.
 * 
 * This edit menu is intended to exist on a StyleEditorFrame menu.
 * 
 * @author Eric Marinier
 *
 */
public class StyleMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private static final String RENAME_STYLE_TEXT = "Rename Style";	//style text
	private static final String RENAME_STYLE = "Rename Style";	//style action command
	
	private final JMenuItem renameStyleItem;	//the exit menu item
	private final GUIController guiController;
	
	private static final String NEW_PLOT_TEXT = "Plot";	//new plot text
	private static final String NEW_SET_TEXT = "Set";	//new set text
	private static final String NEW_SLOT_TEXT = "Slot";	//new slot text
	private static final String NEW_LEGEND_TEXT = "Legend Box";	//new legend text
	private static final String NEW_TEXT = "New";	//the new menu text
	private static final String NEW_SLOT = "New Slot";	//new slot action command
	private static final String NEW_SET = "New Set";	//new set action command
	private static final String NEW_PLOT = "New Plot";	//new plot action command
	private static final String NEW_LEGEND = "New Legend";	//new legend action command
	
	private final JMenu newMenu;
	private final JMenuItem newSlotItem;
	private final JMenuItem newSetItem;
	private final JMenuItem newPlotItem;
	private final JMenuItem newLegendItem;
	
	public StyleMenu(GUIController guiController)
	{
		super(StyleEditorUtility.STYLE_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null");
		
		this.guiController = guiController;
		
		//Style item
		this.renameStyleItem = new JMenuItem(RENAME_STYLE_TEXT);
		this.renameStyleItem.setActionCommand(RENAME_STYLE);
		this.renameStyleItem.addActionListener(this);
		
		//New Menu
		this.newMenu = new JMenu(NEW_TEXT);
		
		//New Slot item
		this.newSlotItem = new JMenuItem(NEW_SLOT_TEXT);
		this.newSlotItem.setActionCommand(NEW_SLOT);
		this.newSlotItem.addActionListener(this);
		
		//New Set item
		this.newSetItem = new JMenuItem(NEW_SET_TEXT);
		this.newSetItem.setActionCommand(NEW_SET);
		this.newSetItem.addActionListener(this);
		
		//New Plot item
		this.newPlotItem = new JMenuItem(NEW_PLOT_TEXT);
		this.newPlotItem.setActionCommand(NEW_PLOT);
		this.newPlotItem.addActionListener(this);
		
		//New Legend item
		this.newLegendItem = new JMenuItem(NEW_LEGEND_TEXT);
		this.newLegendItem.setActionCommand(NEW_LEGEND);
		this.newLegendItem.addActionListener(this);
		
		//add items to new menu
		this.newMenu.add(this.newSlotItem);
		this.newMenu.add(this.newSetItem);
		this.newMenu.add(this.newPlotItem);
		this.newMenu.add(this.newLegendItem);
		
		//add items to file menu
		this.add(this.newMenu);
		
		this.addSeparator();
		
		this.add(this.renameStyleItem);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals(RENAME_STYLE))
		{
			renameCurrentStyle();
		}
		else if(e.getActionCommand().equals(NEW_SLOT))
		{
			(new NewSlotAction(this.guiController.getStyleEditor().getCurrentStyleTree())).run();
		}
		else if(e.getActionCommand().equals(NEW_SET))
		{
			(new NewSetAction(this.guiController.getStyleEditor().getCurrentStyleTree())).run();
		}
		else if(e.getActionCommand().equals(NEW_PLOT))
		{
			(new NewPlotAction(this.guiController.getStyleEditor().getCurrentStyleTree())).run();
		}
		else if(e.getActionCommand().equals(NEW_LEGEND))
		{
			(new NewLegendAction(this.guiController.getStyleEditor().getCurrentStyleTree())).run();
		}
	}

	/**
	 * Renames the current style.
	 * Prompts the user with an input dialog.
	 */
	private void renameCurrentStyle()
	{
		String name = JOptionPane.showInputDialog(this.guiController.getStyleEditor(), "Enter a new name:", "Rename Style", JOptionPane.PLAIN_MESSAGE);
		
		if(name != null && name.length() > 0)
		{
			this.guiController.getStyleEditor().setCurrentStyleName(name);
			this.guiController.getStyleEditor().synchronizeStyles();
		}
		else
		{
			JOptionPane.showMessageDialog(this.guiController.getStyleEditor(), "Invalid name.");
		}
	}
}
