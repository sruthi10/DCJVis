package ca.corefacility.gview.map.controllers;

import ca.corefacility.gview.style.datastyle.LabelStyle;

/**
 * This class is responsible for providing access to it's associated style to ONLY 
 * the controllers, which is achieved by using "protected final" method specifiers and 
 * by having the controllers and tokens in the same package.
 * 
 * @author Eric Marinier
 *
 */
public final class LabelStyleToken
{
	private final LabelStyle labelStyle;
	
	/**
	 * 
	 * @param labelStyle The style to wrap the token around.
	 */
	public LabelStyleToken(LabelStyle labelStyle)
	{
		this.labelStyle = labelStyle;
	}
	
	/**
	 * 
	 * @return The associated style.
	 */
	protected final LabelStyle getStyle()
	{
		return this.labelStyle;
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
		
		LabelStyleToken other = (LabelStyleToken) obj;
		
		if (this.labelStyle == null)
		{
			if (other.labelStyle != null)
			{
				return false;
			}
		}
		else if (!labelStyle.equals(other.labelStyle))
		{
			return false;
		}
		
		return true;
	}
}
