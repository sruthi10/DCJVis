package ca.corefacility.gview.map.controllers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.style.items.TooltipStyle;
import ca.corefacility.gview.utils.Util;

/**
 * This class is responsible for controlling access to the tool tip style.
 * 
 * @author Eric Marinier
 *
 */
public class TooltipStyleController extends Controller
{
	public static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
	public static final Color DEFAULT_OUTLINE_COLOR = Color.BLACK;
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
	
	public static final Font DEFAULT_FONT = StyleEditorUtility.DEFAULT_FONT;
	
	private final TooltipStyle tooltipStyle; 
	
	public TooltipStyleController(StyleController styleController, TooltipStyle tooltipStyle)
	{
		this.tooltipStyle = tooltipStyle;		
	}
	
	/**
	 * 
	 * @return The text color or the default text color if NULL.
	 */
	public Paint getTextColor()
	{
		Paint color = this.tooltipStyle.getTextPaint();
		
		if(color == null)
		{
			color = DEFAULT_TEXT_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @return The outline color or the default outline color if NULL.
	 */
	public Paint getOutlineColor()
	{
		Paint color = this.tooltipStyle.getOutlinePaint();
		
		if(color == null)
		{
			color = DEFAULT_OUTLINE_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @return The background color or the default text background if NULL.
	 */
	public Paint getBackgroundColor()
	{
		Paint color = this.tooltipStyle.getBackgroundPaint();
		
		if(color == null)
		{
			color = DEFAULT_BACKGROUND_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @return The font or the default font if NULL.
	 */
	public Font getFont()
	{
		Font font = this.tooltipStyle.getFont();
		
		if(font == null)
		{
			font = DEFAULT_FONT;
		}
		
		return font;
	}

	/**
	 * 
	 * @param color The new text color.
	 */
	public void setTextColor(Paint color)
	{
		if(color == null)
		{
			color = DEFAULT_TEXT_COLOR;
		}
		
		if(!Util.isEqual(this.tooltipStyle.getTextPaint(), color))
		{
			this.tooltipStyle.setTextPaint(color);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param color The new outline color.
	 */
	public void setOutlineColor(Paint color)
	{
		if(color == null)
		{
			color = DEFAULT_OUTLINE_COLOR;
		}
		
		if(!Util.isEqual(this.tooltipStyle.getOutlinePaint(), color))
		{
			this.tooltipStyle.setOutlinePaint(color);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param color The new background color.
	 */
	public void setBackgroundColor(Paint color)
	{
		if(color == null)
		{
			color = DEFAULT_BACKGROUND_COLOR;
		}
		
		if(!Util.isEqual(this.tooltipStyle.getBackgroundPaint(), color))
		{
			this.tooltipStyle.setBackgroundPaint(color);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param font The new font.
	 */
	public void setFont(Font font)
	{
		if(font == null)
		{
			font = DEFAULT_FONT;
		}
		
		if(!Util.isEqual(this.tooltipStyle.getFont(), font))
		{
			this.tooltipStyle.setFont(font);
			
			this.notifyRebuildRequired();
		}
	}
}
