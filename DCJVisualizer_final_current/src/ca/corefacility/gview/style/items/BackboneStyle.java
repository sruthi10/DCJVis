package ca.corefacility.gview.style.items;


import java.awt.Color;
import java.awt.Paint;

import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;

/**
 * Style information for the backbone.
 * @author Aaron Petkau
 */
public class BackboneStyle implements GViewEventSubject
{
	private Paint paint = Color.GRAY;
	private double thickness = 0.5; // = medium
	private ShapeEffectRenderer shapeEffectRenderer = ShapeEffectRenderer.BASIC_RENDERER;
	
	private double initialBackboneLength = 1000;
	
	// events
	private GViewEventSubjectImp eventSubject;
	
	//private double backboneRadius = 190.0;  What to change this to?  maybe backboneLength.
	
	public BackboneStyle()
	{
		eventSubject = new GViewEventSubjectImp();
	}
	
	public BackboneStyle(BackboneStyle backboneStyle)
	{
		if (backboneStyle == null)
		{
			throw new NullPointerException("backboneStyle is null");
		}
		
		this.paint = backboneStyle.paint;
		this.shapeEffectRenderer = backboneStyle.shapeEffectRenderer;
		this.thickness = backboneStyle.thickness;
		this.eventSubject = new GViewEventSubjectImp(backboneStyle.eventSubject);
	}
	
	public Paint getPaint()
	{
		return paint;
	}

	/**
	 * @return  Thickness of backbone item.
	 */
	public double getThickness()
	{
		return thickness;
	}

	/**
	 * Sets the paint of the backbone.
	 * @param paint
	 */
	public void setPaint(Paint paint)
	{
		this.paint = paint;
	}

	public void setThickness(double thickness)
	{
		if (thickness < 0)
		{
			throw new IllegalArgumentException("thickness=" + thickness + " is negative");
		}
		
		this.thickness = thickness;
	}
	
	public double getInitialBackboneLength()
	{
		return initialBackboneLength;
	}

	public void setInitialBackboneLength(double initialBackboneLength)
	{
		this.initialBackboneLength = initialBackboneLength;
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
			this.shapeEffectRenderer = ShapeEffectRenderer.BASIC_RENDERER;
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(initialBackboneLength);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((paint == null) ? 0 : paint.hashCode());
		result = prime
				* result
				+ ((shapeEffectRenderer == null) ? 0 : shapeEffectRenderer
						.hashCode());
		temp = Double.doubleToLongBits(thickness);
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
		BackboneStyle other = (BackboneStyle) obj;
		if (Double.doubleToLongBits(initialBackboneLength) != Double
				.doubleToLongBits(other.initialBackboneLength))
			return false;
		if (paint == null)
		{
			if (other.paint != null)
				return false;
		} else if (!paint.equals(other.paint))
			return false;
		if (shapeEffectRenderer == null)
		{
			if (other.shapeEffectRenderer != null)
				return false;
		} else if (!shapeEffectRenderer.equals(other.shapeEffectRenderer))
			return false;
		if (Double.doubleToLongBits(thickness) != Double
				.doubleToLongBits(other.thickness))
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
