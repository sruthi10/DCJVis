package ca.corefacility.gview.map.controllers.link;

import ca.corefacility.gview.map.controllers.LegendItemStyleToken;
import ca.corefacility.gview.map.controllers.LegendStyleController;

/**
 * A link represents an association between sets and legend items.
 * 
 * Note: Links are only equivalent if they are the same object.
 * 
 * @author Eric Marinier
 * 
 */
public class Link
{
	private LegendItemStyleToken legend;

	public Link()
	{

	}

	public Link(LegendItemStyleToken legend)
	{
		this.legend = legend;
	}

	/**
	 * Sets the legend associated with the link.
	 * 
	 * Note: The legend should only ever be set once, either through this method
	 * or through the constructor.
	 * 
	 * @param legend
	 */
	public void setLegend(LegendItemStyleToken legend)
	{
		if (this.legend != null)
		{
			throw new IllegalArgumentException("The legend can only be set once per Link object.");
		}

		this.legend = legend;
	}

	/**
	 * 
	 * @return The name of the link.
	 */
	public String getName()
	{
		return LegendStyleController.getText(this.legend);
	}

	@Override
	public boolean equals(Object other)
	{
		// We ONLY want them to be equal if they are the same object!
		if (other == null)
		{
			return false;
		}
		else
		{
			return (this == other);
		}
	}

	/**
	 * 
	 * @param link1
	 * @param link2
	 * 
	 * @return Whether or not the links are equal. If either parameter is null
	 *         this will return false.
	 */
	public static boolean isEqual(Link link1, Link link2)
	{
		if (link1 == null || link2 == null)
		{
			return false;
		}
		else
		{
			return link1.equals(link2);
		}
	}
}
