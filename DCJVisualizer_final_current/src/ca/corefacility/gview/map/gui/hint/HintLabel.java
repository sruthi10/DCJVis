package ca.corefacility.gview.map.gui.hint;

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.utils.thread.ThreadService;

/**
 * A label designed to show tooltip help information on mouse-over.
 * 
 * @author Eric Marinier
 * 
 */
public class HintLabel extends JLabel implements MouseListener, MouseMotionListener, WindowFocusListener,
		ComponentListener
{
	private static final long serialVersionUID = 1L;

	private static final int NUM_CHARACTERS_PER_LINE = 50;
	private HintPopup popup;

	private final String hint;

	private Component parent = null;

	public HintLabel(String hint)
	{
		this(hint, StyleEditorUtility.QUESTION);
	}

	public HintLabel(String hint, ImageIcon icon)
	{
		super(icon);

		this.hint = buildHintString(hint);

		this.setFocusable(false);

		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addComponentListener(this);
	}

	/**
	 * Clear the popup.
	 */
	public void clear()
	{
		if (this.popup != null)
		{
			this.popup.dispose();
			this.popup = null;
		}
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
		maybeShowPopup();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		clear();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		clear();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		maybeShowPopup();
	}

	/**
	 * Will show the popup if possible.
	 */
	private void maybeShowPopup()
	{
		if (this.popup == null)
		{
			this.popup = createPopup();
		}

		if (this.popup.isDisplayable())
		{
			// Execution:
			ThreadService.executeOnEDT(new Runnable()
			{
				@Override
				public void run()
				{
					showPopup();
				}
			});
		}
	}

	/**
	 * Show the popup.
	 */
	private void showPopup()
	{
		// This needs to be done at run time because components may not have
		// been added to their parents yet.
		// We only ever want to do this ONCE though.
		if (this.parent == null)
		{
			// Add the component listeners:
			Component current = this;

			while (current != null)
			{
				current.addComponentListener(this); // This will allow the popup
													// to hide or move
													// appropriately.
				current = current.getParent();
			}

			// Add the window focus listener:
			Component parent = SwingUtilities.getRoot(this);

			if (parent instanceof Window)
			{
				((Window) parent).addWindowFocusListener(this); // This will
																// allow the
																// popup to hide
																// on alt-tabs.

				this.parent = parent;
			}
		}

		// Order is important, need to determine the size first.
		this.popup.setHint(this.hint);

		// Position of the popup:
		Point location = getPopupLocation();

		// Show:
		this.popup.setLocation(location);
		this.popup.setVisible(true);
		this.popup.toFront();
		this.popup.repaint();
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		clear();
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
		if (e.getSource().equals(this.parent) && this.popup != null)
		// NEED the null check.. don't try to show it on a move if it's been
		// clear()'d!
		{
			maybeShowPopup();
		}
		else
		{
			clear();
		}
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
		clear();
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{
		clear();
	}

	/**
	 * 
	 * @param hint
	 *            The hint as a String.
	 * @return The hint as a String with HTML formatting for a better
	 *         appearance.
	 */
	private String buildHintString(String hint)
	{
		String result = "<html>" + hint + "</html>";

		StringBuilder stringBuilder = new StringBuilder(result);

		int i = 0;
		while ((i = stringBuilder.indexOf(" ", i + NUM_CHARACTERS_PER_LINE)) != -1)
		{
			stringBuilder.replace(i, i + 1, "<br>");
		}

		result = stringBuilder.toString();

		return result;
	}

	@Override
	public void windowGainedFocus(WindowEvent e)
	{
		clear();
	}

	@Override
	public void windowLostFocus(WindowEvent e)
	{
		clear();
	}

	protected Point getPopupLocation()
	{
		return new Point(this.getLocationOnScreen().x - this.popup.getWidth() - 1, this.getLocationOnScreen().y
				+ (this.getHeight() / 2) - (this.popup.getHeight() / 2));
	}

	protected HintPopup createPopup()
	{
		return new HintPopup(this);
	}

	protected HintPopup getPopup()
	{
		return this.popup;
	}
}
