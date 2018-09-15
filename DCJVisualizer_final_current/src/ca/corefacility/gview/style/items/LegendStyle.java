package ca.corefacility.gview.style.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * A style defining how this legend item is printed out.
 */
public class LegendStyle
{
	private Font defaultFont = new Font("SansSerif", Font.PLAIN, 12);
	private Paint defaultFontPaint = Color.black;
	
	private Paint backgroundPaint = Color.white;
	private Paint outlinePaint = new Color(0, 0, 0, 0);
	
	private LegendAlignment alignment = LegendAlignment.UPPER_RIGHT;
	private boolean displayLegend = true;
	
	private List<LegendItemStyle> legendItems;
	
	public LegendStyle()
	{
		legendItems = new LinkedList<LegendItemStyle>();
	}
	
	public LegendStyle(LegendStyle legendStyle)
	{
		this.defaultFont = legendStyle.defaultFont;
		this.defaultFontPaint = legendStyle.defaultFontPaint;
		this.backgroundPaint = legendStyle.backgroundPaint;
		this.outlinePaint = legendStyle.outlinePaint;
		this.alignment = legendStyle.alignment;
		this.displayLegend = legendStyle.displayLegend;
		
		if (legendStyle.legendItems == null)
		{
			this.legendItems = null;
		}
		else
		{
			this.legendItems = (List<LegendItemStyle>)((LinkedList<LegendItemStyle>)legendStyle.legendItems).clone();
		}
	}
	
	public void addLegendItem(LegendItemStyle legendItem)
	{
	    if (legendItem.getTextFont() == null)
	    {
	        legendItem.setTextFont(this.defaultFont);
	    }
		legendItems.add(legendItem);
	}
	
	public boolean removeLegendItem(LegendItemStyle legendItem)
	{
		boolean result = false;
		
		if(this.legendItems == null)
			throw new NullPointerException("LegendItems is null.");
		else
		{
			result = this.legendItems.remove(legendItem);
		}
		
		return result;
	}
	
	public Iterator<LegendItemStyle> legendItems()
	{
		return legendItems.iterator();
	}

	public Font getDefaultFont()
	{
		return defaultFont;
	}

	public void setDefaultFont(Font defaultFont)
	{
		this.defaultFont = defaultFont;
	}

	public Paint getDefaultFontPaint()
	{
		return defaultFontPaint;
	}

	public void setDefaultFontPaint(Paint defaultFontPaint)
	{
		this.defaultFontPaint = defaultFontPaint;
	}

	public Paint getBackgroundPaint()
	{
		return backgroundPaint;
	}

	public void setBackgroundPaint(Paint backgroundPaint)
	{
		this.backgroundPaint = backgroundPaint;
	}

	public boolean isDisplayLegend()
	{
		return displayLegend;
	}

	public void setDisplayLegend(boolean displayLegend)
	{
		this.displayLegend = displayLegend;
	}

	public Paint getOutlinePaint()
	{
		return outlinePaint;
	}

	public void setOutlinePaint(Paint outlinePaint)
	{
		this.outlinePaint = outlinePaint;
	}

	public LegendAlignment getAlignment()
	{
		return alignment;
	}

	public void setAlignment(LegendAlignment alignment)
	{
		this.alignment = alignment;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((alignment == null) ? 0 : alignment.hashCode());
		result = prime * result
				+ ((backgroundPaint == null) ? 0 : backgroundPaint.hashCode());
		result = prime * result
				+ ((defaultFont == null) ? 0 : defaultFont.hashCode());
		result = prime
				* result
				+ ((defaultFontPaint == null) ? 0 : defaultFontPaint.hashCode());
		result = prime * result + (displayLegend ? 1231 : 1237);
		result = prime * result
				+ ((legendItems == null) ? 0 : legendItems.hashCode());
		result = prime * result
				+ ((outlinePaint == null) ? 0 : outlinePaint.hashCode());
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
		LegendStyle other = (LegendStyle) obj;
		if (alignment != other.alignment)
			return false;
		if (backgroundPaint == null)
		{
			if (other.backgroundPaint != null)
				return false;
		} else if (!backgroundPaint.equals(other.backgroundPaint))
			return false;
		if (defaultFont == null)
		{
			if (other.defaultFont != null)
				return false;
		} else if (!defaultFont.equals(other.defaultFont))
			return false;
		if (defaultFontPaint == null)
		{
			if (other.defaultFontPaint != null)
				return false;
		} else if (!defaultFontPaint.equals(other.defaultFontPaint))
			return false;
		if (displayLegend != other.displayLegend)
			return false;
		if (legendItems == null)
		{
			if (other.legendItems != null)
				return false;
		} else if (!legendItems.equals(other.legendItems))
			return false;
		if (outlinePaint == null)
		{
			if (other.outlinePaint != null)
				return false;
		} else if (!outlinePaint.equals(other.outlinePaint))
			return false;
		return true;
	}

	public Object clone()
	{
		return new LegendStyle(this);
	}

	/**
	 * Returns the appropriate legend item style given its index.
	 * 
	 * @param index The index of the legend item style.
	 * @return The corresponding legend item style.
	 */
	public LegendItemStyle getLegendItemStyle(int index)
	{
		LegendItemStyle result = null;
		
		if(index < this.legendItems.size() && index >= 0)
		{
			result = this.legendItems.get(index);
		}
		
		return result;
	}

	/**
	 * Inserts the legend item style at the index. 
	 * Does not insert if the index is < 0 or > size.
	 * 
	 * @param legendItemStyle The legend item style to insert.
	 * @param index The location to insert the item at.
	 */
	public void insertLegendItem(LegendItemStyle legendItemStyle,
			int index)
	{
		if(legendItemStyle != null && index >= 0 && index <= this.legendItems.size())	//assuming the legendItems starts indexing at 0
		{
            if (legendItemStyle.getTextFont() == null)
            {
                legendItemStyle.setTextFont(this.defaultFont);
            }
			this.legendItems.add(index, legendItemStyle);
		}
		else
		{
			System.out.println("this shouldnt be happening..");
		}
	}
}
