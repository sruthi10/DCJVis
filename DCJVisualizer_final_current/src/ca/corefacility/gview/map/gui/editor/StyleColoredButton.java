package ca.corefacility.gview.map.gui.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.editor.icon.SimpleIcon;
import ca.corefacility.gview.map.gui.editor.icon.SolidIcon;
import ca.corefacility.gview.utils.Gradient;

/**
 * A button that displays a solid color in the space that the JButton it is
 * extending would normally take up.
 * 
 * Overrides JButton's getBackground, setBackground and paint methods.
 * 
 * @author Eric Marinier
 * 
 */
public class StyleColoredButton extends JButton implements ActionListener, MouseListener
{
	private static final long serialVersionUID = 1L; // requested by java
	private final SimpleIcon icon; // a custom icon class that displays a solid
									// color
	private Paint displayedPaint;

	private static final BufferedImage lockImage = GUIUtility.loadImage("images/icons/lock.png");
	private static final BufferedImage notifyImage = GUIUtility.loadImage("images/icons/exclamation-white.png");

	private boolean notify = false;
	private boolean mouse = false;

	/**
	 * Default coloured button.
	 */
	public StyleColoredButton()
	{
		super();

		this.icon = new SolidIcon(StyleEditorUtility.DEFAULT_COLOR);
		this.setIcon(icon);

		displayedPaint = StyleEditorUtility.DEFAULT_COLOR;

		this.setSize(this.getHeight(), this.getHeight());

		this.addActionListener(this);
		this.addMouseListener(this);
	}

	@Override
	public void setSize(int width, int height)
	{
		super.setSize(height, height);
	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, height, height);
	}

	@Override
	public void setBounds(Rectangle r)
	{
		super.setBounds(r.x, r.y, r.height, r.height);
	}

	@Override
	public void setSize(Dimension d)
	{
		super.setSize(d.height, d.height);
	}

	@Override
	public void paint(Graphics g)
	{
		// forwards the paint to the icon, does NOT paint the button itself
		this.icon.paintIcon(this, g, 0, 0);

		if (!this.isEnabled())
		{
			g.setColor(new Color(128, 128, 128, 128));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			g.drawImage(lockImage, (this.getWidth() - StyleColoredButton.lockImage.getWidth()) / 2,
					(this.getHeight() - StyleColoredButton.lockImage.getHeight()) / 2,
					StyleColoredButton.lockImage.getWidth(), StyleColoredButton.lockImage.getHeight(), null);
		}
		else if (this.notify)
		{
			g.drawImage(notifyImage, (this.getWidth() - StyleColoredButton.notifyImage.getWidth()) / 2,
					(this.getHeight() - StyleColoredButton.notifyImage.getHeight()) / 2,
					StyleColoredButton.notifyImage.getWidth(), StyleColoredButton.notifyImage.getHeight(), null);
		}

		if (this.mouse)
		{
			g.setColor(Color.BLACK);
		}
		else
		{
			g.setColor(Color.GRAY.darker());
		}

		g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
	}

	/**
	 * Sets the paint for the button.
	 * 
	 * Should be either a Color or Gradient.
	 * 
	 * @param p
	 */
	public void setPaint(Paint p)
	{
		displayedPaint = p;

		if (p == null)
			p = StyleEditorUtility.DEFAULT_COLOR;

		if (this.icon != null)
		{
			this.icon.setPaint(p);
			this.setIcon(this.icon);
		}

		if (p instanceof Color)
		{
			super.setBackground((Color) p);
		}
		else if (p instanceof Gradient)
		{
			Gradient g = (Gradient) p;
			super.setBackground(g.getColor1());
		}
		else
		{
			System.err.println("Paint " + p.getClass() + " not of correct type to be displayed, setting to default");
			super.setBackground(StyleEditorUtility.DEFAULT_COLOR);
		}
	}

	@Override
	public void setBackground(Color c)
	{
		setPaint(c);
	}

	/**
	 * 
	 * @return The paint associated with the button.
	 */
	public Paint getPaint()
	{
		return displayedPaint;
	}

	/**
	 * Whether or not to show the notification on the button.
	 */
	public void setNotify(boolean notify)
	{
		this.notify = notify;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.notify = false;
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
