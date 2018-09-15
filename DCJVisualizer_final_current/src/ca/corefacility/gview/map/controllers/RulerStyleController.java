package ca.corefacility.gview.map.controllers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.style.items.LabelLocation;
import ca.corefacility.gview.style.items.RulerStyle;
import ca.corefacility.gview.utils.Util;

/**
 * This class is responsible for controlling access to the ruler style.
 * 
 * @author Eric Marinier
 *
 */
public class RulerStyleController extends Controller
{
	public static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
	public static final Color DEFAULT_TEXT_BACKGROUND_COLOR = new Color(0, 0, 0, 0);
	public static final Color DEFAULT_MAJOR_TICK_COLOR = Color.BLACK;
	public static final Color DEFAULT_MINOR_TICK_COLOR = Color.DARK_GRAY;
	
	public static final Font DEFAULT_FONT = StyleEditorUtility.DEFAULT_FONT;
	
	public static final ShapeEffectRenderer DEFAULT_SHAPE_EFFECT_RENDERER = ShapeEffectRenderer.BASIC_RENDERER;
	public static final LabelLocation DEFAULT_RULER_LABEL_LOCATION = LabelLocation.ABOVE_BACKBONE;
	
	private final StyleController styleController;
	
	private final RulerStyle rulerStyle; 
	
	public RulerStyleController(StyleController styleController, RulerStyle rulerStyle)
	{
		this.styleController = styleController;
		this.rulerStyle = rulerStyle;
	}
	
	/**
	 * 
	 * @return The major tick mark color or the default major tick mark color if NULL.
	 */
	public Paint getMajorTickColor()
	{
		Paint color = this.rulerStyle.getMajorTickPaint();
		
		if(color == null)
		{
			color = DEFAULT_MAJOR_TICK_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @return The minor tick mark color or the default minor tick mark color if NULL.
	 */
	public Paint getMinorTickColor()
	{
		Paint color = this.rulerStyle.getMinorTickPaint();
		
		if(color == null)
		{
			color = DEFAULT_MINOR_TICK_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @return The text color or the default text color if NULL.
	 */
	public Paint getTextColor()
	{
		Paint color = this.rulerStyle.getTextPaint();
		
		if(color == null)
		{
			color = DEFAULT_TEXT_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @return The text background color or the default text background color if NULL.
	 */
	public Paint getTextBackgroundColor()
	{
		Paint color = this.rulerStyle.getTextBackgroundPaint();
		
		if(color == null)
		{
			color = DEFAULT_TEXT_BACKGROUND_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @return The font or the default font if NULL.
	 */
	public Font getFont()
	{
		Font font = this.rulerStyle.getFont();
		
		if(font == null)
		{
			font = DEFAULT_FONT;
		}
		
		return font;
	}
	
	/**
	 * 
	 * @param color The new color of the major tick marks.
	 */
	public void setMajorTickColor(Paint color)
	{
		if(color == null)
		{
			color = DEFAULT_MAJOR_TICK_COLOR;
		}
		
		if(!Util.isEqual(this.rulerStyle.getMajorTickPaint(), color))
		{
			this.rulerStyle.setMajorTickPaint(color);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param color The new color of the minor tick marks.
	 */
	public void setMinorTickColor(Paint color)
	{
		if(color == null)
		{
			color = DEFAULT_MINOR_TICK_COLOR;
		}
		
		if(!Util.isEqual(this.rulerStyle.getMinorTickPaint(), color))
		{
			this.rulerStyle.setMinorTickPaint(color);
			
			this.notifyRebuildRequired();
		}
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
		
		if(!Util.isEqual(this.rulerStyle.getTextPaint(), color))
		{
			this.rulerStyle.setTextPaint(color);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param color The new text background color.
	 */
	public void setTextBackgroundColor(Paint color)
	{
		if(color == null)
		{
			color = DEFAULT_TEXT_BACKGROUND_COLOR;
		}
		
		if(!Util.isEqual(this.rulerStyle.getTextBackgroundPaint(), color))
		{
			this.rulerStyle.setTextBackgroundPaint(color);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param font The new ruler font.
	 */
	public void setFont(Font font)
	{
		if(font == null)
		{
			font = DEFAULT_FONT;
		}
		
		if(!Util.isEqual(this.rulerStyle.getFont(), font))
		{
			this.rulerStyle.setFont(font);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @return The major tick length or 0 if negative.
	 */
	public double getMajorTickLength()
	{
		double length = this.rulerStyle.getMajorTickLength();
		
		if(length < 0)
		{
			length = 0;
		}
		
		return length;
	}
	
	/**
	 * 
	 * @param length The new major tick length.
	 */
	public void setMajorTickLength(double length)
	{
		if(length < 0)
		{
			length = 0;
		}
		
		if(this.rulerStyle.getMajorTickLength() != length)
		{
			this.rulerStyle.setMajorTickLength(length);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @return The minor tick length or zero if negative.
	 */
	public double getMinorTickLength()
	{
		double length = this.rulerStyle.getMinorTickLength();
		
		if(length < 0)
		{
			length = 0;
		}
		
		return length;
	}
	
	/**
	 * 
	 * @param length The new minor tick length.
	 */
	public void setMinorTickLength(double length)
	{
		if(length < 0)
		{
			length = 0;
		}
		
		if(this.rulerStyle.getMinorTickLength() != length)
		{
			this.rulerStyle.setMinorTickLength(length);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @return The tick density bounded by [1, 0], or 0 if less than 0, or 1 if greater than 1.
	 */
	public float getTickDensity()
	{
		float density = this.rulerStyle.getTickDensity();
		
		if(density > 1)
		{
			density = 1;
		}
		else if(density < 0)
		{
			density = 0;
		}
		
		return density;
	}
	
	/**
	 * 
	 * @return The tick thickness or 0 if negative.
	 */
	public double getTickThickness()
	{
		double thickness = this.rulerStyle.getTickThickness();
		
		if(thickness < 0)
		{
			thickness = 0;
		}
		
		return thickness;
	}
	
	/**
	 * 
	 * @param density The new tick density between [0, 1].
	 */
	public void setTickDensity(float density)
	{
		if(density > 1)
		{
			density = 1;
		}
		else if(density < 0)
		{
			density = 0;
		}
		
		if(this.getTickDensity() != density)
		{
			this.rulerStyle.setTickDensity(density);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param thickness The tick thickess greater than 0.
	 */
	public void setTickThickness(double thickness)
	{
		if(thickness < 0)
		{
			thickness = 0;
		}
		
		if(this.rulerStyle.getTickThickness() != thickness)
		{
			this.rulerStyle.setTickThickness(thickness);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @return The shape effect renderer or the default renderer if NULL.
	 */
	public ShapeEffectRenderer getShapeEffectRenderer()
	{
		ShapeEffectRenderer renderer = this.rulerStyle.getShapeEffectRenderer();
		
		if(renderer == null)
		{
			renderer = DEFAULT_SHAPE_EFFECT_RENDERER;
		}
		
		return renderer;
	}
	
	/**
	 * 
	 * @param renderer The new shape effect renderer.
	 */
	public void setShapeEffectRenderer(ShapeEffectRenderer renderer)
	{
		if(renderer == null)
		{
			renderer = DEFAULT_SHAPE_EFFECT_RENDERER;
		}
		
		if(!Util.isEqual(this.rulerStyle.getShapeEffectRenderer(), renderer))
		{
			this.rulerStyle.setShapeEffectRenderer(renderer);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @return The label location or the default label location if NULL.
	 */
	public LabelLocation getRulerLabelLocation()
	{
		LabelLocation location = this.rulerStyle.getRulerLabelsLocation();
		
		if(location == null)
		{
			location = DEFAULT_RULER_LABEL_LOCATION;
		}
		
		return location;
	}
	
	/**
	 * 
	 * @param location The new ruler label location.
	 */
	public void setRulerLabelLocation(LabelLocation location)
	{
		if(location == null)
		{
			location = DEFAULT_RULER_LABEL_LOCATION;
		}
		
		if(!Util.isEqual(this.rulerStyle.getRulerLabelsLocation(), location))
		{
			this.rulerStyle.setRulerLabelsLocation(location);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
}
