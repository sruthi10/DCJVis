package ca.corefacility.gview.style.items;


import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;

public class RulerStyle implements GViewEventSubject
{
	private Font font = new Font("SansSerif", Font.PLAIN, 12);
	private Paint textPaint = Color.BLACK;
	private double padding = 10.0;
	//private int rulerUnits; // = bases
	
	private Paint majorTickPaint = Color.BLACK;
	
	private Paint minorTickPaint = Color.BLACK;
	private double minorTickThickness; // = medium
	
	private float tickDensity = 1.0f;
	private double majorTickLength = 10.0;
	private double tickThickness = 1.0; // = medium
	
	private LabelLocation rulerLabelsLocation = LabelLocation.BELOW_BACKBONE;
	
	//private Paint zeroTickPaint = Color.BLACK;
	
	private ShapeEffectRenderer shapeEffectRenderer = ShapeEffectRenderer.BASIC_RENDERER;
	
	// events
	private GViewEventSubjectImp eventSubject;
	private Paint textBackgroundPaint = new Color(0, 0, 0, 0);
	private double minorTickLength = 3.0;
	
	public RulerStyle()
	{
		eventSubject = new GViewEventSubjectImp();
	}
	
	public RulerStyle(RulerStyle rulerStyle)
	{
		if (rulerStyle == null)
		{
			throw new NullPointerException("rulerStyle is null");
		}
		
		this.font = rulerStyle.font;
		this.textPaint = rulerStyle.textPaint;
		this.padding = rulerStyle.padding;
		this.majorTickPaint = rulerStyle.majorTickPaint;
		this.minorTickPaint = rulerStyle.minorTickPaint;
		this.tickDensity = rulerStyle.tickDensity;
		this.majorTickLength = rulerStyle.majorTickLength;
		this.minorTickLength = rulerStyle.minorTickLength;
		this.tickThickness = rulerStyle.tickThickness;
		this.shapeEffectRenderer = rulerStyle.shapeEffectRenderer;
		this.eventSubject = new GViewEventSubjectImp(rulerStyle.eventSubject);
		this.textBackgroundPaint = rulerStyle.textBackgroundPaint;
	}
	
	/**
	 * @return  The font used to display the labels on the ruler.
	 */
	public Font getFont()
	{
		return font;
	}

	/**
	 * Sets the font used to display the labels on the ruler.
	 * @param font
	 */
	public void setFont(Font font)
	{
		this.font = font;
	}

	/**
	 * @return  The paint used to color the labels on the ruler.
	 */
	public Paint getTextPaint()
	{
		return textPaint;
	}

	/**
	 * Gets the location of the labels for the ruler.
	 * @return  A LabelLocation object describing where the labels are located.
	 */
	public LabelLocation getRulerLabelsLocation()
	{
		return rulerLabelsLocation;
	}

	/**
	 * Sets the location for the labels on the ruler.
	 * @param rulerLabelsLocation  The LabelLocation object describing where the labels are located.
	 */
	public void setRulerLabelsLocation(LabelLocation rulerLabelsLocation)
	{
		this.rulerLabelsLocation = rulerLabelsLocation;
	}

	/**
	 * Sets the paint used to color the labels on the ruler.
	 * @param paint  The paint to set.
	 */
	public void setTextPaint(Paint paint)
	{
		this.textPaint = paint;
	}

	public double getPadding()
	{
		return padding;
	}

	public void setPadding(double padding)
	{
		this.padding = padding;
	}

//	public int getRulerUnits()
//	{
//		return rulerUnits;
//	}
//
//	public void setRulerUnits(int rulerUnits)
//	{
//		this.rulerUnits = rulerUnits;
//	}

	/**
	 * @return The paint used to color the minor tick marks.
	 */
	public Paint getMinorTickPaint()
	{
		return minorTickPaint;
	}

	/**
	 * Sets the paint used to color the minor tick marks.
	 * @param shortTickPaint
	 */
	public void setMinorTickPaint(Paint shortTickPaint)
	{
		this.minorTickPaint = shortTickPaint;
	}

	/**
	 * @return  The thickness of the major tick marks.
	 */
	public double getMinorTickThickness()
	{
		return minorTickThickness;
	}

	/**
	 * Sets the thickness of the minor tick marks.
	 * @param minorTickThickness
	 */
	public void setMinorTickThickness(double minorTickThickness)
	{
		this.minorTickThickness = minorTickThickness;
	}

	public float getTickDensity()
	{
		return tickDensity;
	}

	/**
	 * Sets the tick mark density (from 0 to 1).
	 * 
	 * @param tickDensity
	 */
	public void setTickDensity(float tickDensity)
	{
		if(tickDensity < 0 || tickDensity > 1)
		{
			throw new IllegalArgumentException("Tick density is out of range: " + tickDensity);
		}
		
		this.tickDensity = tickDensity;
	}

	/**
	 * Gets the length of the major tick marks.
	 * @return  The length of the major tick marks.
	 */
	public double getMajorTickLength()
	{
		return majorTickLength;
	}

	/**
	 * Sets the length of the major tick marks.
	 * @param tickLength  The length of the major tick marks.
	 */
	public void setMajorTickLength(double tickLength)
	{
		this.majorTickLength = tickLength;
	}

	/**
	 * The thickness of the tick marks.
	 * @return  The thickness of the tick marks.
	 */
	public double getTickThickness()
	{
		return tickThickness;
	}

	public void setTickThickness(double tickThickness)
	{
		this.tickThickness = tickThickness;
	}

//	public Paint getZeroTickPaint()
//	{
//		return zeroTickPaint;
//	}
//
//	public void setZeroTickPaint(Paint zeroTickPaint)
//	{
//		this.zeroTickPaint = zeroTickPaint;
//	}

	public Paint getMajorTickPaint()
	{
		return majorTickPaint;
	}

	public void setMajorTickPaint(Paint majorTickPaint)
	{
		this.majorTickPaint = majorTickPaint;
	}
	
	public ShapeEffectRenderer getShapeEffectRenderer()
	{
		return shapeEffectRenderer;
	}

	public void setShapeEffectRenderer(ShapeEffectRenderer shapeEffectRenderer)
	{
		if (shapeEffectRenderer != null)
		{
			this.shapeEffectRenderer = shapeEffectRenderer;
		}
		else
		{
			throw new NullPointerException("shapeEffectRenderer is null");
		}
	}
	
	/**
	 * Sets the text background paint for the ruler labels.
	 * @param paint  The paint to use in the background of the ruler labels.
	 */
	public void setTextBackgroundPaint(Paint paint)
	{
		this.textBackgroundPaint = paint;
	}
	
	/**
	 * Gets the text background paint for the ruler labels.
	 * @return  The text background paint.
	 */
	public Paint getTextBackgroundPaint()
	{
		return textBackgroundPaint;
	}
	
	/**
	 * Gets the length of the minor tick marks.
	 * @return  The length of the minor tick marks.
	 */
	public double getMinorTickLength()
	{
		return minorTickLength;
	}
	
	/**
	 * Sets the length of the minor tick marks.
	 * @param tickLength  The length of the minor tick marks.
	 */
	public void setMinorTickLength(double tickLength)
	{
		this.minorTickLength = tickLength;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((font == null) ? 0 : font.hashCode());
		long temp;
		temp = Double.doubleToLongBits(majorTickLength);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((majorTickPaint == null) ? 0 : majorTickPaint.hashCode());
		temp = Double.doubleToLongBits(minorTickLength);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((minorTickPaint == null) ? 0 : minorTickPaint.hashCode());
		temp = Double.doubleToLongBits(minorTickThickness);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(padding);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((rulerLabelsLocation == null) ? 0 : rulerLabelsLocation
						.hashCode());
		result = prime
				* result
				+ ((shapeEffectRenderer == null) ? 0 : shapeEffectRenderer
						.hashCode());
		result = prime
				* result
				+ ((textBackgroundPaint == null) ? 0 : textBackgroundPaint
						.hashCode());
		result = prime * result
				+ ((textPaint == null) ? 0 : textPaint.hashCode());
		result = prime * result + Float.floatToIntBits(tickDensity);
		temp = Double.doubleToLongBits(tickThickness);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RulerStyle other = (RulerStyle) obj;
		if (font == null)
		{
			if (other.font != null)
				return false;
		}
		else if (!font.equals(other.font))
			return false;
		if (Double.doubleToLongBits(majorTickLength) != Double
				.doubleToLongBits(other.majorTickLength))
			return false;
		if (majorTickPaint == null)
		{
			if (other.majorTickPaint != null)
				return false;
		}
		else if (!majorTickPaint.equals(other.majorTickPaint))
			return false;
		if (Double.doubleToLongBits(minorTickLength) != Double
				.doubleToLongBits(other.minorTickLength))
			return false;
		if (minorTickPaint == null)
		{
			if (other.minorTickPaint != null)
				return false;
		}
		else if (!minorTickPaint.equals(other.minorTickPaint))
			return false;
		if (Double.doubleToLongBits(minorTickThickness) != Double
				.doubleToLongBits(other.minorTickThickness))
			return false;
		if (Double.doubleToLongBits(padding) != Double
				.doubleToLongBits(other.padding))
			return false;
		if (rulerLabelsLocation == null)
		{
			if (other.rulerLabelsLocation != null)
				return false;
		}
		else if (!rulerLabelsLocation.equals(other.rulerLabelsLocation))
			return false;
		if (shapeEffectRenderer == null)
		{
			if (other.shapeEffectRenderer != null)
				return false;
		}
		else if (!shapeEffectRenderer.equals(other.shapeEffectRenderer))
			return false;
		if (textBackgroundPaint == null)
		{
			if (other.textBackgroundPaint != null)
				return false;
		}
		else if (!textBackgroundPaint.equals(other.textBackgroundPaint))
			return false;
		if (textPaint == null)
		{
			if (other.textPaint != null)
				return false;
		}
		else if (!textPaint.equals(other.textPaint))
			return false;
		if (Float.floatToIntBits(tickDensity) != Float
				.floatToIntBits(other.tickDensity))
			return false;
		if (Double.doubleToLongBits(tickThickness) != Double
				.doubleToLongBits(other.tickThickness))
			return false;
		return true;
	}

	/**
	 * Event methods
	 */

	public void addEventListener(GViewEventListener listener)
	{
		eventSubject.addEventListener(listener);
	}

	public void removeAllEventListeners()
	{
		eventSubject.removeAllEventListeners();
	}

	public void removeEventListener(GViewEventListener listener)
	{
		eventSubject.removeEventListener(listener);
	}
}
