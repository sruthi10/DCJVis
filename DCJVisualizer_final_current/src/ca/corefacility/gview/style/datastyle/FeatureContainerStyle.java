package ca.corefacility.gview.style.datastyle;

import ca.corefacility.gview.style.datastyle.mapper.PropertyMappable;

/**
 * This allows for (data) styles that contain features, such as sets and slots, to interface more easily.
 * 
 * @author Eric Marinier
 *
 */
public interface FeatureContainerStyle extends PropertyMappable
{
	/**
	 * Remove the passed slot item style from the feature container.
	 * 
	 * @param style The slot item style to remove.
	 * @return The success of the removal.
	 */
	public boolean removeSlotItemStyle(SlotItemStyle style);
	
	/**
	 * Removes the current property mapper from the feature container style.
	 * 
	 * @return The success of the removal.
	 */
	public boolean removePropertyStyleMapper();
	
	/**
	 * 
	 * @return The feature container's label style.
	 */
	public LabelStyle getLabelStyle();
}
