package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GViewMapManager;
import ca.corefacility.gview.map.gui.GViewMapManager.Layout;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;

public class LayoutMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private final GUIController guiController;

	private final JRadioButtonMenuItem linear;
	private final JRadioButtonMenuItem circular;

	private final ButtonGroup group = new ButtonGroup();

	public LayoutMenu(GUIController guiController)
	{
		super(StyleEditorUtility.LAYOUT_TEXT);

		this.guiController = guiController;

		this.linear = new JRadioButtonMenuItem(StyleEditorUtility.LINEAR_LAYOUT);
		this.linear.addActionListener(this);

		this.circular = new JRadioButtonMenuItem(StyleEditorUtility.CIRCULAR_LAYOUT);
		this.circular.addActionListener(this);

		this.group.add(this.linear);
		this.group.add(this.circular);

		this.add(this.linear);
		this.add(this.circular);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(this.linear))
		{
			this.guiController.displayProgressWhileRebuilding(new Runnable()
			{
				@Override
				public void run()
				{
					guiController.setCurrentLayout(GViewMapManager.Layout.LINEAR);
				}
			});
		}
		else if (e.getSource().equals(this.circular))
		{
			this.guiController.displayProgressWhileRebuilding(new Runnable()
			{
				@Override
				public void run()
				{
					guiController.setCurrentLayout(GViewMapManager.Layout.CIRCULAR);
				}
			});
		}
	}

	/**
	 * Updates the layout menu.
	 */
	public void update()
	{
		GViewMapManager gViewMapManager = this.guiController.getCurrentStyleMapManager();
		Layout layout = gViewMapManager.getLayout();

		switch (layout)
		{
			case LINEAR:
				this.linear.setSelected(true);
				break;
			case CIRCULAR:
				this.circular.setSelected(true);
				break;
      default:
        break;
		}
	}
}
