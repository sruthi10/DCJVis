package ca.corefacility.gview.map.gui.editor.icon;

import java.awt.Color;
import java.awt.Paint;

import javax.swing.Icon;

import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;

/**
 * An implementation of Icon that is a checkerboard pattern with a color painted overtop.
 * 
 * @author Eric Marinier
 *
 */
public abstract class SimpleIcon implements Icon
{
	private Paint paint;
	
	public SimpleIcon()
	{
		this.paint = StyleEditorUtility.DEFAULT_COLOR;
	}
	
	public SimpleIcon(Paint paint)
	{
		if(paint == null)
		    paint = StyleEditorUtility.DEFAULT_COLOR;
		
		this.paint = paint;
	}
	
	/**
	 * 
	 * @return The Paint of the icon.
	 */
	public Paint getPaint()
	{
		return paint;
	}
	
	public Color getColor()
	{
	    if (paint instanceof Color)
	    {
	        return (Color)paint;
	    }
	    else
	    {
	        return StyleEditorUtility.DEFAULT_COLOR;
	    }
	}
	
	/**
	 * 
	 * @param p The Paint to set the icon.
	 */
	public void setPaint(Paint p)
	{	
		if(p == null)
		{
			p = StyleEditorUtility.DEFAULT_COLOR;
		}
		
		this.paint = p;
	}
}
