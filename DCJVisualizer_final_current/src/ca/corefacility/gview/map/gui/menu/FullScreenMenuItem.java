package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.GViewGUIFrame;
import ca.corefacility.gview.map.gui.action.map.FullScreenAction;
import ca.corefacility.gview.map.gui.action.map.ResizeScreenAction;

/**
 * Responsible for creating the "Full Screen" menu item.
 * 
 * @author Eric Marinier
 *
 */
public class FullScreenMenuItem extends JCheckBoxMenuItem implements ItemListener
{
	private static final long serialVersionUID = 1L;	
	private final GViewGUIFrame gViewGUIFrame;
	
	/**
	 * Creates a new menu item for Full Screen within the specified frame.
	 * 
	 * @param gViewGUIFrame The frame the full screen menu item will send its message to.
	 */
	public FullScreenMenuItem(GViewGUIFrame gViewGUIFrame)
	{
		super(GUIUtility.FULL_SCREEN);
		
		if(gViewGUIFrame == null)
			throw new IllegalArgumentException("GViewGUIFrame is null.");
		
		this.gViewGUIFrame = gViewGUIFrame;
		
		this.setActionCommand(GUIUtility.FULL_SCREEN);
		this.setAccelerator(KeyStroke.getKeyStroke(GUIUtility.FULL_SCREEN_SHORTCUT));
		this.addItemListener(this);
	}
	
	/**
	 * Updates the check mark.
	 */
	public void update()
	{
		if(gViewGUIFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH)
			this.setSelected(true);
		else
			this.setSelected(false);
	}

	@Override
	/**
	 * Listens for "Full Screen" (checkable) menu item(s).
	 */
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			(new FullScreenAction(gViewGUIFrame)).run();
		}
		else if (e.getStateChange() == ItemEvent.DESELECTED)
		{
			(new ResizeScreenAction(gViewGUIFrame)).run();
		}
	}
}
