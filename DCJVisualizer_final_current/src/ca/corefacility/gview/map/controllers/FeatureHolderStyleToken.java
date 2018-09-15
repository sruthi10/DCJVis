package ca.corefacility.gview.map.controllers;

import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;

/**
 * This class is responsible for providing access to it's associated style to
 * ONLY the controllers, which is achieved by using "protected final" method
 * specifiers and by having the controllers and tokens in the same package.
 * 
 * This allows controllers to access information that other classes may not.
 * 
 * @author Eric Marinier
 * 
 */
public final class FeatureHolderStyleToken extends SlotItemStyleToken
{
	private final FeatureHolderStyle feautureHolderStyle;

	/**
	 * 
	 * @param feautureHolderStyle
	 *            The style to wrap the token around.
	 */
	public FeatureHolderStyleToken(FeatureHolderStyle feautureHolderStyle)
	{
		this.feautureHolderStyle = feautureHolderStyle;
	}

	/**
	 * @return The associated style.
	 */
	protected final FeatureHolderStyle getStyle()
	{
		return this.feautureHolderStyle;
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

		FeatureHolderStyleToken other = (FeatureHolderStyleToken) obj;

		if (this.feautureHolderStyle == null)
		{
			if (other.feautureHolderStyle != null)
			{
				return false;
			}
		}
		else if (!feautureHolderStyle.equals(other.feautureHolderStyle))
		{
			return false;
		}

		return true;
	}

	/**
	 * Determines whether or not the feature holder style objects within the
	 * tokens are both pointing to the same object. That is, the both reference
	 * exactly the same object.
	 * 
	 * @param other
	 * @return Whether or not the style references are the same reference.
	 */
	public boolean hasSameStyleReference(FeatureHolderStyleToken other)
	{
		// Null check:
		if (other == null)
		{
			return false;
		}
		// Null style check:
		else if (this.feautureHolderStyle == null || other.feautureHolderStyle == null)
		{
			return false;
		}

		return (this.feautureHolderStyle == other.feautureHolderStyle);
	}
}
