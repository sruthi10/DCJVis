package ca.corefacility.gview.map.gui.editor.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;

import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;

/**
 * This class is intended to be used as a label icon for a custom JTree renderer.
 * 
 * It will create a triangle shaped icon.
 * 
 * @author Eric Marinier
 *
 */
public class TriangleTreeIcon extends TreeIcon
{
	public TriangleTreeIcon(Paint paint)
	{
		super(paint);
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) 
	{
	    Graphics2D gg = null;
	    if (g instanceof Graphics2D)
	    {
	        gg = (Graphics2D)g;
	        
	        Shape oldClip = gg.getClip();
	        gg.setPaint(new Color(153, 153, 153, 255));
	        
	        Polygon triangle = new Polygon();
	        triangle.addPoint(x + this.x_offset, y + this.y_offset + this.height);
	        triangle.addPoint(x + this.x_offset + this.width, y + this.y_offset + this.height);
	        triangle.addPoint(((x + this.x_offset) + (x + this.x_offset + this.width)) / 2, y + this.y_offset);
	        
	        gg.fill(triangle);
	        
	        gg.clip(triangle);

		    if(StyleEditorUtility.CHECKBOARD != null)
		    {
		    	g.drawImage(StyleEditorUtility.CHECKBOARD, x + this.x_offset, y + this.y_offset, null);
		    }
		    
		    gg.setClip(oldClip);
		    gg.setPaint(getPaint());
		    gg.fill(triangle);			
			
			g.setColor(new Color(0, 0, 0, 255));			
			gg.drawLine(x + this.x_offset, y + this.y_offset + this.height, x + this.x_offset + this.width, y + this.y_offset + this.height);
			gg.drawLine(x + this.x_offset + this.width, y + this.y_offset + this.height, ((x + this.x_offset) + (x + this.x_offset + this.width)) / 2, y + this.y_offset);
			gg.drawLine(((x + this.x_offset) + (x + this.x_offset + this.width)) / 2, y + this.y_offset, x + this.x_offset, y + this.y_offset + this.height);
	    }
	}

}
