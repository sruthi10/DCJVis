package ca.corefacility.gview.map.controllers;

import java.awt.Color;
import java.awt.Paint;

import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.style.GlobalStyle;
import ca.corefacility.gview.utils.Util;

/**
 * This class is responsible for controlling access to the global style.
 * 
 * @author Eric Marinier
 *
 */
public class GlobalStyleController extends Controller
{
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
	public static final double DEFAULT_SLOT_SPACING = 4.0;
	
	private final StyleController styleController;
	private final GlobalStyle globalStyle;
	
	public GlobalStyleController(StyleController styleController, GlobalStyle globalStyle)
	{
		this.styleController = styleController;
		this.globalStyle = globalStyle;
	}
	
	/**
	 * 
	 * @return The background paint or the default paint if NULL.
	 */
	public Paint getBackgroundColor()
	{
		Paint color = this.globalStyle.getBackgroundPaint();
		
		if(color == null)
		{
			color = StyleEditorUtility.DEFAULT_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @return The slot spacing.
	 */
	public double getSlotSpacing()
	{
		return this.globalStyle.getSlotSpacing();
	}
	
	/**
	 * 
	 * @param color The background color.
	 */
	public void setBackgroundColor(Paint color)
	{
		if(color == null)
		{
			color = DEFAULT_BACKGROUND_COLOR;
		}
		
		if(!Util.isEqual(this.globalStyle.getBackgroundPaint(), color))
		{
			this.globalStyle.setBackgroundPaint(color);
			
			this.notifyRebuildRequired();
		}		
	}
	
	/**
	 * 
	 * @param spacing The slot spacing.
	 */
	public void setSlotSpacing(double spacing)
	{
		if(spacing < 0)
		{
			spacing = DEFAULT_SLOT_SPACING;
		}
		
		if(this.globalStyle.getSlotSpacing() != spacing)
		{
			this.globalStyle.setSlotSpacing(spacing);
			
			this.styleController.notifyFullRebuildRequired();
		}		
	}
}
