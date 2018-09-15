package ca.corefacility.gview.style.datastyle;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stores style information specific to a set of items on the map (may or may not be features).
 */
public abstract class SlotItemStyle
{
	protected Paint[] paint = new Paint[]{}; // maybe don't have paint here, in case we need more paint styles for plots
	protected float transparency = 1.0f;

	/**
	 * Defines the adjusted height of the center of this item in the slot.
	 *   -1.0 = bottom, 0.0 = center, 1.0 = top.
	 */
	protected double heightAdjust = 0.0;

	/**
	 * Describes the proportion of thickness of this set of items in a slot.
	 */
	protected double thickness = 1.0;

	/**
	 * Creates a new ItemHolderStyle.
	 */
	public SlotItemStyle()
	{
	}

	protected SlotItemStyle(SlotItemStyle style)
	{
		this.paint = style.paint;
		this.transparency = style.transparency;
		this.heightAdjust = style.heightAdjust;
		this.thickness = style.thickness;
	}

	/**
	 * Gets the paint associated with this feature holder.
	 * 
	 * @return  The paint of this feature holder.
	 */
	public Paint[] getPaint()
	{
		if( this.paint != null && this.paint.length > 0 )
			return this.paint;
		else
		{
			this.paint = new Paint[]{Color.BLUE};
			return this.paint;
		}
	}

	/**
	 * Opacity defining how the features in this holder will be drawn.
	 * 
	 * @return  The opacity of this feature holder.
	 */
	public float getTransparency()
	{
		return this.transparency;
	}

	/**
	 * Gets the thickness of the features in this holder (in range [0,1])
	 * 
	 * @return  The default thickness of the items in this holder.
	 */
	public double getThickness()
	{
		return this.thickness;
	}

	/**
	 * Sets the paint of items in this holder.
	 * 
	 * @param paint2  The paint.
	 */
	public void setPaint(Paint paint2)
	{
		this.paint = new Paint[] {paint2};
	}

	public void setPaint( Paint[] paint2 )
	{
		if( paint2 == null)
			throw new NullPointerException("paint is null");

		this.paint = paint2;
	}

	public void addPaint( Paint paint2 )
	{
		if( this.paint == null )
		{
			setPaint( paint2 );
		}
		else
		{
			ArrayList<Paint> newPaint = new ArrayList<Paint>();
			for( Paint p : this.paint )
			{
				newPaint.add( p );
			}
			newPaint.add( paint2 );

			this.paint = newPaint.toArray( this.paint );
		}
	}

	/**
	 * Sets the transparency of items in this holder (in range [0,1])
	 * @param transparency
	 */
	public void setTransparency(float transparency)
	{
		if (0.0f <= transparency && transparency <= 1.0f)
		{
			this.transparency = transparency;
		}
		else
			throw new IllegalArgumentException("transparency=" + transparency + " out of range of [0.0f,1.0f]");
	}

	/**
	 * Sets the thickness of items as a proportion of the height of the containing slot.  Must be in range [0,1]
	 * @param thickness
	 */
	public void setThickness(double thickness)
	{
		if (0.0 <= thickness && thickness <= 1.0)
		{
			this.thickness = thickness;
		}
		else
			throw new IllegalArgumentException("thickness=" + thickness + " out of range of [0.0f,1.0f]");
	}

	/**
	 * Gets the heightAdjust in the slot.  Must be between [-1,1], with -1 being bottom of slot, 0 being center, and 1 being top.
	 * @return  The height adjust of items in there containing slot.
	 */
	public double getHeightAdjust()
	{
		return this.heightAdjust;
	}

	/**
	 * Sets the height adjust of items in there containing slot.
	 * @param heightAdjust  The height adjust.  Must be between [-1,1], with -1 being bottom of slot, 0 being center, and 1 being top.
	 */
	public void setHeightAdjust(double heightAdjust)
	{
		if (heightAdjust > 1.0 || heightAdjust < -1.0)
			throw new IllegalArgumentException("heightAdjust=" + heightAdjust + " not in [-1,1]");

		this.heightAdjust = heightAdjust;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(heightAdjust);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(paint);
		temp = Double.doubleToLongBits(thickness);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(transparency);

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
		SlotItemStyle other = (SlotItemStyle) obj;
		if (Double.doubleToLongBits(heightAdjust) != Double
				.doubleToLongBits(other.heightAdjust))
			return false;
		if (!Arrays.equals(paint, other.paint))
			return false;
		if (Double.doubleToLongBits(thickness) != Double
				.doubleToLongBits(other.thickness))
			return false;
		if (Float.floatToIntBits(transparency) != Float
				.floatToIntBits(other.transparency))
			return false;
		return true;
	}
}
