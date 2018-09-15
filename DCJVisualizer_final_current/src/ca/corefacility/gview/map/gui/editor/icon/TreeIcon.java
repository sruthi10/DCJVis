package ca.corefacility.gview.map.gui.editor.icon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

/**
 * This class is intended to be used as a label icon for a custom JTree renderer.
 * 
 * @author Eric Marinier
 *
 */
public abstract class TreeIcon extends SimpleIcon
{	
	private final boolean highlight;
	
	public final int x_offset = 0;
	public final int y_offset = 0;
	
	public final int width = 14;
	public final int height = 14;
	
	public TreeIcon(Paint paint)
	{
		super(paint);
		
		this.highlight = false;
	}
	
	public TreeIcon(Paint paint, boolean highlight)
	{
		super(paint);
		
		this.highlight = highlight;
	}
	
	@Override
	public int getIconWidth() 
	{
		return this.width + x_offset + 1;
	}

	@Override
	public int getIconHeight() 
	{
		return this.height + y_offset + 2;
	}
	
	/**
	 * Paints a highlighted effect on the icon.
	 * 
	 * @param graphics
	 * @param x
	 * @param y
	 */
	protected void paintHighlight(Graphics2D graphics, int x, int y)
	{
		if(highlight)
		{
			graphics.setPaint(Color.WHITE);	
			graphics.fillOval(x + this.x_offset + 4, y + this.y_offset + 4, this.width - 8, this.height - 8);
			
			graphics.setPaint(Color.BLACK);	
			graphics.drawOval(x + this.x_offset + 4, y + this.y_offset + 4, this.width - 8, this.height - 8);
		}
	}
}
