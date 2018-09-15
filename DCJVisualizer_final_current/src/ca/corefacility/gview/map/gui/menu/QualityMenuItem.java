package ca.corefacility.gview.map.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import ca.corefacility.gview.map.gui.GUISettings;
import ca.corefacility.gview.map.gui.GUISettings.RenderingQuality;
import ca.corefacility.gview.map.gui.GUIUtility;

/**
 * Responsible for creating and managing the Quality menu item.
 * 
 * The Quality menu item is responsible for modifying the rendering quality and
 * optimizations.
 * 
 * @author Eric Marinier
 * 
 */
public class QualityMenuItem extends JMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private boolean updating = false;

	private final JRadioButtonMenuItem high = new JRadioButtonMenuItem(GUIUtility.HIGH_QUALITY_TEXT);
	private final JRadioButtonMenuItem low = new JRadioButtonMenuItem(GUIUtility.LOW_QUALITY_TEXT);

	private final ButtonGroup group = new ButtonGroup();

	public QualityMenuItem()
	{
		super(GUIUtility.QUALITY_TEXT);

		this.high.addActionListener(this);
		this.low.addActionListener(this);

		this.group.add(this.high);
		this.group.add(this.low);

		this.add(this.high);
		this.add(this.low);
	}

	/**
	 * Updates the selected state of the menu item.
	 */
	public void update()
	{
		this.updating = true;

		if (GUISettings.getRenderingQuality() == RenderingQuality.LOW)
		{
			this.low.setSelected(true);
		}
		else
		{
			this.high.setSelected(true);
		}

		this.updating = false;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (!this.updating)
		{
			this.updating = true;

			// High:
			if (e.getSource().equals(this.high) && GUISettings.getRenderingQuality() != RenderingQuality.HIGH)
			{
				GUISettings.setRenderingQuality(RenderingQuality.HIGH);
			}
			// Low:
			else if (e.getSource().equals(this.low) && GUISettings.getRenderingQuality() != RenderingQuality.LOW)
			{
				GUISettings.setRenderingQuality(RenderingQuality.LOW);
			}

			this.updating = false;
		}
	}
}
