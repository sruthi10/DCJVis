package ca.corefacility.gview.map.gui.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import ca.corefacility.gview.map.gui.GUISettings;
import ca.corefacility.gview.map.gui.GUISettings.SlotNodeDetail;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;

/**
 * The menu item for how much detail to show on slot nodes.
 * 
 * @author Eric Marinier
 * 
 */
public class SlotDetailMenu extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private boolean updating = false;

	private final JRadioButtonMenuItem full;
	private final JRadioButtonMenuItem minimal;
	private final JRadioButtonMenuItem link;
	private final JRadioButtonMenuItem none;

	private final ButtonGroup group = new ButtonGroup();

	public SlotDetailMenu()
	{
		super(StyleEditorUtility.SLOT_DETAIL_TEXT);

		this.full = new JRadioButtonMenuItem(StyleEditorUtility.FULL_TEXT);
		this.full.addActionListener(this);

		this.minimal = new JRadioButtonMenuItem(StyleEditorUtility.MINIMAL_TEXT);
		this.minimal.addActionListener(this);

		this.link = new JRadioButtonMenuItem(StyleEditorUtility.LINK_TEXT);
		this.link.addActionListener(this);

		this.none = new JRadioButtonMenuItem(StyleEditorUtility.NONE_TEXT);
		this.none.addActionListener(this);

		this.group.add(this.full);
		this.group.add(this.minimal);
		this.group.add(this.link);
		this.group.add(this.none);

		this.add(this.full);
		this.add(this.minimal);
		this.add(this.link);
		this.add(this.none);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		SlotNodeDetail current = GUISettings.getSlotNodeDetail();

		if (!this.updating)
		{
			this.updating = true;

			if (e.getSource().equals(this.full) && current != SlotNodeDetail.FULL)
			{
				GUISettings.setSlotNodeDetail(SlotNodeDetail.FULL);
			}
			else if (e.getSource().equals(this.minimal) && current != SlotNodeDetail.MINIMAL)
			{
				GUISettings.setSlotNodeDetail(SlotNodeDetail.MINIMAL);
			}
			else if (e.getSource().equals(this.link) && current != SlotNodeDetail.LINK)
			{
				GUISettings.setSlotNodeDetail(SlotNodeDetail.LINK);
			}
			else if (e.getSource().equals(this.none) && current != SlotNodeDetail.NONE)
			{
				GUISettings.setSlotNodeDetail(SlotNodeDetail.NONE);
			}

			this.updating = false;
		}
	}

	/**
	 * Updates the selected state of the menu item.
	 */
	public void update()
	{
		this.updating = true;

		switch (GUISettings.getSlotNodeDetail())
		{
			case FULL:
				this.full.setSelected(true);
				break;

			case MINIMAL:
				this.minimal.setSelected(true);
				break;

			case LINK:
				this.link.setSelected(true);
				break;

			case NONE:
				this.none.setSelected(true);
				break;
		}

		this.updating = false;
	}
}
