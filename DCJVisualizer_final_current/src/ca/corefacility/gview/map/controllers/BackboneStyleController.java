package ca.corefacility.gview.map.controllers;

import java.awt.Color;
import java.awt.Paint;

import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.style.items.BackboneStyle;
import ca.corefacility.gview.utils.Util;

/**
 * This class is responsible for controlling access to the backbone style.
 * 
 * @author Eric Marinier
 *
 */
public class BackboneStyleController extends Controller
{
	public static final Color DEFAULT_BACKBONE_COLOR = Color.BLACK;
	public static final double DEFAULT_THICKNESS = 1.0;
	
	private final StyleController styleController;
	
	private final BackboneStyle backboneStyle; 
	
	public BackboneStyleController(StyleController styleController, BackboneStyle backboneStyle)
	{
		this.styleController = styleController;
		this.backboneStyle = backboneStyle;		
	}
	
	/**
	 * 
	 * @return The color of the backbone, or the default color if it is NULL.
	 */
	public Paint getColor()
	{
		Paint color = this.backboneStyle.getPaint();
		
		if(color == null)
		{
			color = StyleEditorUtility.DEFAULT_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @return The thickness of the backbone as a non-negative double.
	 */
	public double getThickness()
	{
		double thickness = this.backboneStyle.getThickness();
		
		if(thickness < 0)
		{
			thickness = 0;
		}
		
		return thickness;
	}
	
	/**
	 * 
	 * @param color The color of the backbone.
	 */
	public void setColor(Paint color)
	{
		if(color == null)
		{
			color = DEFAULT_BACKBONE_COLOR;
		}
		
		if(!Util.isEqual(this.backboneStyle.getPaint(), color))
		{
			this.backboneStyle.setPaint(color);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param thickness The thickness of the backbone.
	 */
	public void setThickness(double thickness)
	{
		if(thickness < 0)
		{
			thickness = DEFAULT_THICKNESS;
		}
		
		if(this.backboneStyle.getThickness() != thickness)
		{
			this.backboneStyle.setThickness(thickness);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
}
