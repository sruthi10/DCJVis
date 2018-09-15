package ca.corefacility.gview.map.inputHandler;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.biojava.bio.seq.Feature;

import ca.corefacility.gview.managers.DisplayManager;
import ca.corefacility.gview.map.event.FeatureSelectEvent;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;
import ca.corefacility.gview.map.items.BackboneLabelItem;
import ca.corefacility.gview.map.items.FeatureItem;
import ca.corefacility.gview.map.items.MapItem;

/**
 * Handles selection of specific items and features on the GView map.
 * @author aaron
 *
 */
public class SelectHandler
{
	private GViewEventSubjectImp dispatcher; // reference to the gview event dispatcher so we can fire events.

	private Set<FeatureItem> selectedItems;
	private Set<BackboneLabelItem> selectedLabels; // used to keep track of selected labels, so we can select/unselect labels

	/**
	 * Creates a new SelectHandler which fires events to the passed dispatcher.
	 * @param dispatcher  The dispatcher to use to fire events to.
	 */
	public SelectHandler(GViewEventSubjectImp dispatcher)
	{
		assert dispatcher != null;

		this.dispatcher = dispatcher;

		this.selectedItems = new HashSet<FeatureItem>();
		this.selectedLabels = new HashSet<BackboneLabelItem>();
	}

	/**
	 * Signals that the passed item was selected, and we are only in single select mode (only selecting a single item).
	 * @param item  The item selected.
	 */
	public void singleItemSelect(MapItem item)
	{
		unselectAll();

		if (item instanceof FeatureItem)
		{
			select((FeatureItem)item);
		}
		else if (item instanceof BackboneLabelItem)
		{
			select((BackboneLabelItem)item);
		}

		this.dispatcher.fireEvent(new FeatureSelectEvent(this, this.selectedItems));
	}

	/**
	 * Signals that the passed item was selected, and we are in multi-item select mode.
	 * @param item  The item selected.
	 */
	public void multiItemSelect(MapItem item)
	{
		if (item instanceof FeatureItem)
		{
			select((FeatureItem)item);
			this.dispatcher.fireEvent(new FeatureSelectEvent(this, this.selectedItems));
		}
		else if (item instanceof BackboneLabelItem)
		{
			select((BackboneLabelItem)item);
			this.dispatcher.fireEvent(new FeatureSelectEvent(this, this.selectedItems));
		}
	}

	/**
	 * Signals that the passed collection of features should be selected (and only these should be selected).
	 * @param items  The collection of features to select.  Assumes a selection event should be fired
	 * @param displayManager  Used here to find the FeatureItem associated with the given features.
	 */
	public void selectOnlyFeatures(DisplayManager displayManager, Collection<Feature> items )
	{
		selectOnlyFeatures( displayManager, items, true );
	}
	/**
	 * Signals that the passed collection of features should be selected (and only these should be selected).
	 * @param items  The collection of features to select.
	 * @param displayManager  Used here to find the FeatureItem associated with the given features.
	 * @param fireUpdate Determines whether a selection event should be fired.  You may not want to fire an
	 * 		  event if you are responding to an event
	 */
	public void selectOnlyFeatures(DisplayManager displayManager, Collection<Feature> items, boolean fireUpdate)
	{
		if (items == null)
			return;

		unselectAll();

		Iterator<Feature> itemsIterator = items.iterator();

		while (itemsIterator.hasNext())
		{
			Feature feature = itemsIterator.next();

			List<FeatureItem> featureItems = displayManager.findFeatureItems(feature);

			if (featureItems != null)
			{
				Iterator<FeatureItem> featureItemsIterator = featureItems.iterator();

				while (featureItemsIterator.hasNext())
				{
					FeatureItem featureItem = featureItemsIterator.next();

					selectNoUnselect(featureItem);
				}
			}
		}
		if( fireUpdate )
		{
			this.dispatcher.fireEvent(new FeatureSelectEvent(this, this.selectedItems));
		}
	}

	/**
	 * Attempts to select the passed item, if already selected does nothing.
	 * @param item
	 */
	private void selectNoUnselect(FeatureItem item)
	{
		if (!this.selectedItems.contains(item))
		{
			item.setSelect();

			this.selectedItems.add((FeatureItem)item);
		}
	}

	private void select(FeatureItem item)
	{
		if (!this.selectedItems.contains(item))
		{
			item.setSelect();

			this.selectedItems.add((FeatureItem)item);
		}
		else
		{
			item.removeSelect();

			this.selectedItems.remove(item);
		}
	}

	private void select(BackboneLabelItem item)
	{
		if (!this.selectedItems.contains(item.getLabeledFeature()))
		{
			item.getLabeledFeature().setSelect();
			item.setSelect();

			this.selectedItems.add(item.getLabeledFeature());
			this.selectedLabels.add(item);
		}
		else
		{
			item.getLabeledFeature().removeSelect();
			item.removeSelect();

			this.selectedItems.remove(item.getLabeledFeature());
			this.selectedLabels.remove(item);
		}
	}

	/**
	 * Unselects all items that were selected.
	 */
	public void unselectAll()
	{
		// clear out selected features
		Iterator<FeatureItem> items = this.selectedItems.iterator();

		while (items.hasNext())
		{
			FeatureItem current = items.next();

			current.removeSelect();
		}

		this.selectedItems.clear();

		// clear out selected labels
		Iterator<BackboneLabelItem> labels = this.selectedLabels.iterator();

		while (labels.hasNext())
		{
			BackboneLabelItem current = labels.next();

			current.removeSelect();
		}

		this.selectedLabels.clear();
	}
}
