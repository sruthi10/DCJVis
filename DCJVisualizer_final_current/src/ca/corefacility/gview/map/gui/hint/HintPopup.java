package ca.corefacility.gview.map.gui.hint;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * The custom implementation of the tooltip as a window.
 * 
 * @author Eric Marinier
 * 
 */
public class HintPopup extends JWindow implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;

	private final HintLabel parent;
	private final JLabel hint = new JLabel();

	public HintPopup(HintLabel parent)
	{
		super();

		this.parent = parent;

		this.getRootPane().setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),
						BorderFactory.createEmptyBorder(4, 4, 4, 4)));

		UIDefaults defaults = UIManager.getDefaults();
		Color color = defaults.getColor("ToolTip.background");

		if (color == null)
		{
			color = Color.GRAY;
		}

		this.getRootPane().setBackground(color);
		this.getContentPane().setBackground(color);

		this.getContentPane().add(this.hint);
		this.pack();

		this.setFocusableWindowState(false);
		this.setFocusable(false);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	@Override
	public void toFront()
	{
		// Java bug requires this.
		super.setAlwaysOnTop(true);
		super.toFront();
		super.setAlwaysOnTop(false);
	}

	/**
	 * Sets the hint of the popup.
	 * 
	 * Will automatically call .pack() after setting the text.
	 * 
	 * @param hint
	 */
	public void setHint(String hint)
	{
		this.hint.setText(hint);
		this.pack();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		this.parent.clear();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		this.parent.clear();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		this.parent.clear();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		this.parent.clear();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		this.parent.clear();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		this.parent.clear();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		this.parent.clear();
	}
}