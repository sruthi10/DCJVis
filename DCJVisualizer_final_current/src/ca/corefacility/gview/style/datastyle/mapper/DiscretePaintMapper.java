package ca.corefacility.gview.style.datastyle.mapper;

import java.awt.Paint;
import java.util.Arrays;

import ca.corefacility.gview.map.items.FeatureItem;

public class DiscretePaintMapper implements DiscreteStyleMapper
{
	private Paint[] colors;
	
	public DiscretePaintMapper(Paint[] colors)
	{
		if (colors == null)
		{
			throw new IllegalArgumentException("colors are null");
		}
		
		for (Paint c : colors)
		{
			if (c == null)
			{
				throw new IllegalArgumentException("an element of colors is null");
			}
		}
		
		this.colors = colors;
	}

	@Override
	public void mapValueToStyle(int value, FeatureItem featureItem)
			throws IndexOutOfBoundsException
	{
		if (featureItem == null)
		{
			throw new IllegalArgumentException("featureItem is null");
		}
		
		if (value < 0 || value >= colors.length)
		{
			throw new IndexOutOfBoundsException("value=" + value + " outside of bounds [0," + colors.length + ")");
		}
		
		featureItem.setPaint(colors[value]);
	}
	
	@Override
	public boolean valueInBounds(int value)
	{
		return value >= 0 && value < colors.length;
	}
	
	@Override
	public int getNumberOfCategories()
	{
		return colors.length;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(colors);
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
		DiscretePaintMapper other = (DiscretePaintMapper) obj;
		if (!Arrays.equals(colors, other.colors))
			return false;
		return true;
	}
	
	public Paint[] getColors()
	{
		return this.colors;
	}
}
