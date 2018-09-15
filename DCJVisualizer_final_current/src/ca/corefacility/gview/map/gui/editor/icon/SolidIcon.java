package ca.corefacility.gview.map.gui.editor.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;

public class SolidIcon extends SimpleIcon
{	
	public SolidIcon(Paint paint)
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
	        gg.setPaint(getPaint());
	    }
	    else
	    {
	        if (getPaint() instanceof Color)
	        {
	            g.setColor((Color)getPaint());
	        }
	        else
	        {
	            g.setColor(Color.white);
	            
	            System.err.println("Graphics class " + g.getClass() + " not of type "
	                    + Graphics2D.class + " : cannot support icon with paint type " + getPaint().getClass());
	        }
	    }
	    
	    g.drawImage(StyleEditorUtility.CHECKBOARD, 0, 0, c.getWidth(), c.getHeight(), null);

		g.fillRect(0, 0, c.getWidth(), c.getHeight());
		
		g.setColor(new Color(0, 0, 0, 255));
		
		g.drawLine(x, y, x, y + c.getHeight() - 1);
		g.drawLine(x, y, x + c.getWidth() - 1, y);
		g.drawLine(x + c.getWidth() - 1, y + c.getHeight() - 1, x, y + c.getHeight() - 1);
		g.drawLine(x + c.getWidth() - 1, y + c.getHeight() - 1, x + c.getWidth() - 1, y);
	}
	
	@Override
	public int getIconWidth() 
	{
		return 0;
	}

	@Override
	public int getIconHeight() 
	{
		return 0;
	}
}
