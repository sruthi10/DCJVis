package ca.corefacility.gview.map.gui.editor.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;

/**
 * This class is intended to be used as a label icon for a custom JTree renderer.
 * 
 * It will create a rectangle shaped icon.
 * 
 * @author Eric Marinier
 *
 */
public class RectangleTreeIcon extends TreeIcon
{
	public RectangleTreeIcon(Paint paint)
	{
		super(paint);
	}
	
	public RectangleTreeIcon(Paint paint, boolean highlight)
	{
		super(paint, highlight);
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) 
	{
	    Graphics2D gg = null;
	    
	    if (g instanceof Graphics2D)
	    {
	        gg = (Graphics2D)g;
	        
	        Shape oldClip = gg.getClip();
	        Rectangle rectangle = new Rectangle(x + this.x_offset, y + this.y_offset, this.width, this.height);
	        
	        gg.setPaint(new Color(153, 153, 153, 255));
	        gg.fill(rectangle);
	        
	        gg.clip(rectangle);

		    if(StyleEditorUtility.CHECKBOARD != null)
		    {
		    	gg.drawImage(StyleEditorUtility.CHECKBOARD, x + this.x_offset, y + this.y_offset, null);
		    }
		    
		    gg.setClip(oldClip);
		    gg.setPaint(getPaint());
		    gg.fill(rectangle);			
			
			gg.setColor(new Color(0, 0, 0, 255));		
			gg.drawRect(x + this.x_offset, y + this.y_offset, this.width, this.height);
			
			super.paintHighlight(gg, x, y);
	    }
	}
}
