package ca.corefacility.gview.style.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import ca.corefacility.gview.map.controllers.link.Link;

/**
 * A class defining a single item on the Legend.
 * @author aaron
 *
 */
public class LegendItemStyle
{
	private String text;
	private Font textFont = null;
	private Paint textPaint = Color.black;
	private Paint swatchPaint = Color.WHITE;
	private boolean showSwatch = false;
	private LegendTextAlignment textAlignment = LegendTextAlignment.LEFT;
	
	private Link link = null;

	public LegendItemStyle(String text)
	{
		this.text = text;
	}
	
	public LegendItemStyle()
	{
		
	}
	
	public LegendItemStyle(LegendItemStyle itemStyle)
	{	
		this.text = itemStyle.text;
		this.textFont = itemStyle.textFont;
		this.textPaint = itemStyle.textPaint;
		this.swatchPaint = itemStyle.swatchPaint;
		this.showSwatch = itemStyle.showSwatch;
		
		this.textAlignment = itemStyle.textAlignment;
	}
	
	/**
	 * Creates a style for a LegendItem, allowing the user to override the swatchPaint.
	 * Null for no paint.
	 * @param text
	 * @param swatchPaint
	 */
	public LegendItemStyle(String text, Paint swatchPaint)
	{
		this.text = text;
		this.swatchPaint = swatchPaint;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public LegendTextAlignment getTextAlignment()
	{
		return textAlignment;
	}

	public void setTextAlignment(LegendTextAlignment legendTextAlignment)
	{
		this.textAlignment = legendTextAlignment;
	}

	public Font getTextFont()
	{
		return textFont;
	}

	/**
	 * Sets text font, if this is not set then takes on default font from global legend style.
	 * @param textFont
	 */
	public void setTextFont(Font textFont)
	{
		this.textFont = textFont;
	}

	public Paint getTextPaint()
	{
		return textPaint;
	}

	public void setTextPaint(Paint textPaint)
	{
		this.textPaint = textPaint;
	}

	public Paint getSwatchPaint()
	{
		return swatchPaint;
	}

	public void setSwatchPaint(Paint swatchPaint)
	{
		if(swatchPaint == null)
		{
			this.swatchPaint = Color.WHITE;
			this.showSwatch = false;
		}
		else
		{
			this.swatchPaint = swatchPaint;
		}
	}

	public void setShowSwatch(boolean showSwatch)
	{
		this.showSwatch = showSwatch;
	}
	
	public boolean isShowSwatch()
	{
		return this.showSwatch;
	}
	
	public void setLink(Link link)
	{
		this.link = link;
	}
	
	public Link getLink()
	{
		return this.link;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((textAlignment == null) ? 0 : textAlignment
						.hashCode());
		result = prime * result + (showSwatch ? 1231 : 1237);
		result = prime * result
				+ ((swatchPaint == null) ? 0 : swatchPaint.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result
				+ ((textFont == null) ? 0 : textFont.hashCode());
		result = prime * result
				+ ((textPaint == null) ? 0 : textPaint.hashCode());
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
		LegendItemStyle other = (LegendItemStyle) obj;
		if (textAlignment != other.textAlignment)
			return false;
		if (showSwatch != other.showSwatch)
			return false;
		if (swatchPaint == null)
		{
			if (other.swatchPaint != null)
				return false;
		} else if (!swatchPaint.equals(other.swatchPaint))
			return false;
		if (text == null)
		{
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (textFont == null)
		{
			if (other.textFont != null)
				return false;
		} else if (!textFont.equals(other.textFont))
			return false;
		if (textPaint == null)
		{
			if (other.textPaint != null)
				return false;
		} else if (!textPaint.equals(other.textPaint))
			return false;
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return new LegendItemStyle(this);
	}
}
