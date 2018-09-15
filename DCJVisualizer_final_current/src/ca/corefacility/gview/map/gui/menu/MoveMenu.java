package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowMoveDialogAction;
import ca.corefacility.gview.map.gui.action.map.move.MoveEndAction;
import ca.corefacility.gview.map.gui.action.map.move.MoveFirstQuarterAction;
import ca.corefacility.gview.map.gui.action.map.move.MoveHalfAction;
import ca.corefacility.gview.map.gui.action.map.move.MoveStartAction;
import ca.corefacility.gview.map.gui.action.map.move.MoveThirdQuarterAction;

/**
 * Responsible for creating the move menu.
 * 
 * @author Eric Marinier
 * 
 */
public class MoveMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private final GUIController guiController;

	public MoveMenu(GUIController guiController)
	{
		super(GUIUtility.MOVE_TEXT);

		if (guiController == null)
			throw new IllegalArgumentException("GUIController is null.");

		this.guiController = guiController;

		addMenuItems();
	}

	/**
	 * Adds the individual menu items to the zoom sub menu.
	 */
	private void addMenuItems()
	{
		JMenuItem currMenuItem;

		// Start item.
		currMenuItem = new JMenuItem(GUIUtility.START);
		currMenuItem.setActionCommand(GUIUtility.START);
		currMenuItem.addActionListener(this);
		this.add(currMenuItem);

		// Other move items.
		for (int i = 1; i < GUIUtility.constantBaseMoves.length; i++)
		{
			currMenuItem = new JMenuItem(GUIUtility.constantBaseMoves[i]);
			currMenuItem.setActionCommand(GUIUtility.constantBaseMoves[i]);
			currMenuItem.addActionListener(this);
			this.add(currMenuItem);
		}

		this.add(new JSeparator());

		// Custom move item.
		currMenuItem = new JMenuItem(GUIUtility.MOVE_BASE_CUSTOM);
		currMenuItem.setActionCommand(GUIUtility.MOVE_BASE_CUSTOM);
		currMenuItem.setAccelerator(KeyStroke.getKeyStroke(GUIUtility.MOVE_CUSTOM_SHORTCUT));
		currMenuItem.addActionListener(this);
		this.add(currMenuItem);
	}

	@Override
	/**
	 * Listens for move events.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (GUIUtility.START.equals(e.getActionCommand()))
		{
			(new MoveStartAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if (GUIUtility.QUARTER_1.equals(e.getActionCommand()))
		{
			(new MoveFirstQuarterAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if (GUIUtility.HALF.equals(e.getActionCommand()))
		{
			(new MoveHalfAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if (GUIUtility.QUARTER_3.equals(e.getActionCommand()))
		{
			(new MoveThirdQuarterAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if (GUIUtility.END.equals(e.getActionCommand()))
		{
			(new MoveEndAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if (GUIUtility.MOVE_BASE_CUSTOM.equals(e.getActionCommand()))
		{
			(new ShowMoveDialogAction(this.guiController.getGViewGUIFrame().getMoveDialog())).run();
		}
	}
}
