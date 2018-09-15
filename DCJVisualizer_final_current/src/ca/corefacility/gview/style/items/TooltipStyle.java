package ca.corefacility.gview.style.items;


import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;

public class TooltipStyle implements GViewEventSubject
{
	private Font font = new Font("SansSerif", Font.PLAIN, 12);
	private Paint paint = null;
	private Paint outlinePaint = null;
	private Paint textPaint = Color.RED;
	
	// events
	private GViewEventSubjectImp eventSubject;
	
	public TooltipStyle()
	{
		eventSubject = new GViewEventSubjectImp();
	}
	
	public TooltipStyle(TooltipStyle toolTipStyle)
	{
		if (toolTipStyle == null)
		{
			throw new NullPointerException("toolTipStyle is null");
		}
		
		this.font = toolTipStyle.font;
		this.paint = toolTipStyle.paint;
		this.outlinePaint = toolTipStyle.outlinePaint;
		this.textPaint = toolTipStyle.textPaint;
		this.eventSubject = new GViewEventSubjectImp(toolTipStyle.eventSubject);
	}

	/**
	 * @return  The font used for the tool tip.
	 */
	public Font getFont()
	{
		return font;
	}

	/**
	 * Sets the font for the tool tip.
	 * @param font
	 */
	public void setFont(Font font)
	{
		if (font == null)
		{
			throw new NullPointerException("font is null");
		}
		
		this.font = font;
	}

	/**
	 * @return  The paint used as a background for tool tip.
	 */
	public Paint getBackgroundPaint()
	{
		return paint;
	}

	/**
	 * Sets the paint used as a background for the tool tip.  Null for none.
	 * @param paint
	 */
	public void setBackgroundPaint(Paint paint)
	{
		this.paint = paint;
	}

	/**
	 * The paint used for the text.
	 * @return  The paint used for the text.
	 */
	public Paint getTextPaint()
	{
		return textPaint;
	}
	
	/**
	 * @return  The paint used for the outline.
	 */
	public Paint getOutlinePaint()
	{
		return outlinePaint;
	}

	/**
	 * Sets the paint used for an outline around the tool tip.
	 * @param outlinePaint
	 */
	public void setOutlinePaint(Paint outlinePaint)
	{
		this.outlinePaint = outlinePaint;
	}

	/**
	 * Sets the paint used for text of the tool tip.
	 * @param textPaint
	 */
	public void setTextPaint(Paint textPaint)
	{
		this.textPaint = textPaint;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((font == null) ? 0 : font.hashCode());
		result = prime * result
				+ ((outlinePaint == null) ? 0 : outlinePaint.hashCode());
		result = prime * result + ((paint == null) ? 0 : paint.hashCode());
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
		TooltipStyle other = (TooltipStyle) obj;
		if (font == null)
		{
			if (other.font != null)
				return false;
		}
		else if (!font.equals(other.font))
			return false;
		if (outlinePaint == null)
		{
			if (other.outlinePaint != null)
				return false;
		}
		else if (!outlinePaint.equals(other.outlinePaint))
			return false;
		if (paint == null)
		{
			if (other.paint != null)
				return false;
		}
		else if (!paint.equals(other.paint))
			return false;
		if (textPaint == null)
		{
			if (other.textPaint != null)
				return false;
		}
		else if (!textPaint.equals(other.textPaint))
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
