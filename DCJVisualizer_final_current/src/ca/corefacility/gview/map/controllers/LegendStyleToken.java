package ca.corefacility.gview.map.controllers;

import ca.corefacility.gview.style.items.LegendStyle;

/**
 * This class is responsible for providing access to it's associated style to ONLY 
 * the controllers, which is achieved by using "protected final" method specifiers and 
 * by having the controllers and tokens in the same package.
 * 
 * @author Eric Marinier
 *
 */
public final class LegendStyleToken
{
	private final LegendStyle legendStyle;
	
	/**
	 * 
	 * @param legendStyle The style to wrap the token around.
	 */
	public LegendStyleToken(LegendStyle legendStyle)
	{
		this.legendStyle = legendStyle;
	}
	
	/**
	 * 
	 * @return The associated style.
	 */
	protected final LegendStyle getStyle()
	{
		return this.legendStyle;
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
		
		LegendStyleToken other = (LegendStyleToken) obj;
		
		if (this.legendStyle == null)
		{
			if (other.legendStyle != null)
			{
				return false;
			}
		}
		else if (!legendStyle.equals(other.legendStyle))
		{
			return false;
		}
		
		return true;
	}
}
