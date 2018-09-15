package ca.corefacility.gview.map.controllers;

import ca.corefacility.gview.style.items.LegendItemStyle;

/**
 * This class is responsible for providing access to it's associated style to
 * ONLY the controllers, which is achieved by using "protected final" method
 * specifiers and by having the controllers and tokens in the same package.
 * 
 * @author Eric Marinier
 * 
 */
public final class LegendItemStyleToken
{
	private final LegendItemStyle legendItemStyle;

	/**
	 * 
	 * @param legendItemStyle
	 *            The style to wrap the token around.
	 */
	public LegendItemStyleToken(LegendItemStyle legendItemStyle)
	{
		this.legendItemStyle = legendItemStyle;
	}

	/**
	 * 
	 * @return The associated style.
	 */
	protected final LegendItemStyle getStyle()
	{
		return this.legendItemStyle;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}

		if (obj == null)
		{
			return false;
		}

		if (getClass() != obj.getClass())
		{
			return false;
		}

		LegendItemStyleToken other = (LegendItemStyleToken) obj;

		if (this.legendItemStyle == null)
		{
			if (other.legendItemStyle != null)
			{
				return false;
			}
		}
		else if (!legendItemStyle.equals(other.legendItemStyle))
		{
			return false;
		}

		return true;
	}

	/**
	 * Determines whether or not the legend item style objects within the tokens
	 * are both pointing to the same object. That is, the both reference exactly
	 * the same object.
	 * 
	 * @param other
	 * @return Whether or not the style references are the same reference.
	 */
	public boolean sameStyleReference(LegendItemStyleToken other)
	{
		// Null check:
		if (other == null)
		{
			return false;
		}
		// Null style check:
		else if (this.legendItemStyle == null || other.legendItemStyle == null)
		{
			return false;
		}

		return (this.legendItemStyle == other.legendItemStyle);
	}
}
