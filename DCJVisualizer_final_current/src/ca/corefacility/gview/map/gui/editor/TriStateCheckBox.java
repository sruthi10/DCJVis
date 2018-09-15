package ca.corefacility.gview.map.gui.editor;

import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import ca.corefacility.gview.map.gui.GUIUtility;

/**
 * This class extends the JCheckBox and implements custom tri-state checkbox
 * with custom ImageIcons for all checkbox states.
 * 
 * This implementation ignores Look & Feel changes.
 * 
 * @author Eric Marinier
 * 
 */
public class TriStateCheckBox extends JCheckBox implements ItemListener, MouseListener
{
	private static final long serialVersionUID = 1L;

	private static final ImageIcon unselectedIcon = new ImageIcon(
			GUIUtility.loadImage("images/icons/checkbox_nocheck.png"));
	private static final ImageIcon selectedIcon = new ImageIcon(GUIUtility.loadImage("images/icons/checkbox_check.png"));
	private static final ImageIcon mixedIcon = new ImageIcon(GUIUtility.loadImage("images/icons/checkbox_mixed.png"));

	private static final ImageIcon unselectedDarkIcon = new ImageIcon(
			GUIUtility.loadImage("images/icons/checkbox_nocheck_dark.png"));
	private static final ImageIcon selectedDarkIcon = new ImageIcon(
			GUIUtility.loadImage("images/icons/checkbox_check_dark.png"));
	private static final ImageIcon mixedDarkIcon = new ImageIcon(
			GUIUtility.loadImage("images/icons/checkbox_mixed_dark.png"));

	private boolean mixed = false;
	private boolean mouse = false;

	public TriStateCheckBox()
	{
		this.setSize(unselectedIcon.getIconWidth(), unselectedIcon.getIconHeight());

		this.addItemListener(this);
		this.addMouseListener(this);
	}

	@Override
	public void paint(Graphics g)
	{
		if (this.mouse)
		{
			// Check for mixed state first:
			if (this.mixed)
			{
				paintIcon(g, mixedDarkIcon);
			}
			else if (this.isSelected())
			{
				paintIcon(g, selectedDarkIcon);
			}
			else
			{
				paintIcon(g, unselectedDarkIcon);
			}
		}
		else
		{
			// Check for mixed state first:
			if (this.mixed)
			{
				paintIcon(g, mixedIcon);
			}
			else if (this.isSelected())
			{
				paintIcon(g, selectedIcon);
			}
			else
			{
				paintIcon(g, unselectedIcon);
			}
		}
	}

	/**
	 * Paints the image icon.
	 * 
	 * @param g
	 * @param icon
	 */
	private void paintIcon(Graphics g, ImageIcon icon)
	{
		int x = (this.getWidth() - icon.getIconWidth()) / 2;
		int y = (this.getHeight() - icon.getIconHeight()) / 2;

		icon.paintIcon(this, g, x, y);
	}

	/**
	 * Whether or not to show the mixed state in the check box.
	 * 
	 * This DOES NOT change the actual state of the checkbox, just the rendering
	 * of it.
	 * 
	 * @param mixed
	 */
	public void setMixed(boolean mixed)
	{
		this.mixed = mixed;
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		this.mixed = false;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		this.mouse = true;
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		this.mouse = false;
	}
}
