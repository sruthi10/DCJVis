package ca.corefacility.gview.style.datastyle.mapper;

import ca.corefacility.gview.map.items.FeatureItem;

public interface ContinuousStyleMapper
{
	/**
	 * Maps a particular value in the range of 0.0 to 1.0 to some style property in the passed FeatureItem.
	 * @param value  The value to map, in the range of 0.0 to 1.0.
	 * @param featureItem  The FeatureItem to apply the style to.
	 */
	public void mapValueToStyle(float value, FeatureItem featureItem);
}
