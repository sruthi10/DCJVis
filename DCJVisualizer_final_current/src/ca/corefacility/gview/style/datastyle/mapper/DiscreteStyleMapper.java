package ca.corefacility.gview.style.datastyle.mapper;

import ca.corefacility.gview.map.items.FeatureItem;

public interface DiscreteStyleMapper
{
	public void mapValueToStyle(int value, FeatureItem featureItem) throws IndexOutOfBoundsException;
	
	/**
	 * If the passed value is within the bounds of style properties to map to.
	 * @param value
	 * @return  True if within bounds, false otherwise.
	 */
	public boolean valueInBounds(int value);
	
	public int getNumberOfCategories();
}
