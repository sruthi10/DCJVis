package ca.corefacility.gview.map.gui.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.editor.StyleEditorTree;

/**
 * 
 * The Style Editor view menu.
 * 
 * @author Eric Marinier
 * 
 */
public class ViewMenu extends JMenu implements MenuListener, ActionListener
{
	private static final long serialVersionUID = 1L;

	private static final String COLLAPSE_ALL = "Collapse All";
	private static final String COLLAPSE_ALL_TEXT = "Collapse Nodes";
	private static final String EXPAND_ALL = "Expand All";
	private static final String EXPAND_ALL_TEXT = "Expand Nodes";

	private final JMenuItem collapseAllItem;
	private final JMenuItem expandAllItem;

	private final SlotDetailMenu slotDetailMenu;

	private final GUIController guiController;

	public ViewMenu(GUIController guiController)
	{
		super("View");

		this.guiController = guiController;

		this.slotDetailMenu = new SlotDetailMenu();

		this.collapseAllItem = new JMenuItem(COLLAPSE_ALL_TEXT);
		this.collapseAllItem.setActionCommand(COLLAPSE_ALL);
		this.collapseAllItem.addActionListener(this);

		this.expandAllItem = new JMenuItem(EXPAND_ALL_TEXT);
		this.expandAllItem.setActionCommand(EXPAND_ALL);
		this.expandAllItem.addActionListener(this);

		// Add items:
		this.add(this.collapseAllItem);
		this.add(this.expandAllItem);

		this.addSeparator();

		this.add(this.slotDetailMenu);

		this.addMenuListener(this);
	}

	@Override
	public void menuSelected(MenuEvent e)
	{
		this.slotDetailMenu.update();
	}

	@Override
	public void menuDeselected(MenuEvent e)
	{
	}

	@Override
	public void menuCanceled(MenuEvent e)
	{
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		StyleEditorTree tree = this.guiController.getStyleEditor().getCurrentStyleTree();

		// Collapse All:
		if (this.collapseAllItem.equals(e.getSource()))
		{
			tree.collapse();
		}
		// Expand All:
		else if (this.expandAllItem.equals(e.getSource()))
		{
			for (int i = 0; i < tree.getRowCount(); i++)
			{
				tree.expandRow(i);
			}
		}
	}
}
