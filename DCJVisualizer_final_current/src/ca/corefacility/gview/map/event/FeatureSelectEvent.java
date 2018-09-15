package ca.corefacility.gview.map.event;


import java.util.Collection;

import ca.corefacility.gview.map.items.FeatureItem;

/**
 * Describes a event where the used clicked on a FeatureItem.
 * 
 * @author Aaron Petkau
 * 
 */
public class FeatureSelectEvent extends GViewEvent
{
	private static final long serialVersionUID = -4185447115069944324L;
	private Collection<FeatureItem> selectedItems;

	public FeatureSelectEvent(Object source, Collection<FeatureItem> selectedItems)
	{
		super(source);

		assert (selectedItems != null);
		this.selectedItems = selectedItems;
	}

	/**
	 * @return The collection of feature items currently selected.
	 */
	public Collection<FeatureItem> getSelectedItems()
	{
		return selectedItems;
	}
}
