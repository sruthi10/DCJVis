package ca.corefacility.gview.managers.labels;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ca.corefacility.gview.map.items.BackboneLabelItem;

/**
 * Used to determine which labels to remove to minimize overlap.
 * @author aaron
 *
 */
public class LabelsFilterer
{		
	public List<Label> filter(LinkedList<Label> labels)
	{
		List<Label> remainingLabels = new LinkedList<Label>();
		
		return remainingLabels;
	}
	
	/**
	 * Searches for sets of overlapping labels.
	 * @param labels
	 * @return A set containing sets of overlapping labels.
	 */
	private Set<Set<Label>> findOverlappingSets(LinkedList<Label> labels)
	{
		Set<Set<Label>> overlappingSets = new HashSet<Set<Label>>();
		LinkedList<Label> workingLabels = (LinkedList<Label>)labels.clone();
		
		// take a label, check for any other labels intersecting it, add this group to overlappingSets
		for (Label currLabel : workingLabels)
		{
			Set<Label> currSet = getIntersectedLabel(currLabel, workingLabels);
			
			if (currSet.size() > 1) // if more than one label in this set
			{
				overlappingSets.add(currSet);
				workingLabels.remove((Collection<Label>)currSet); // remove these labels so we don't check again
			}
		}
		
		return overlappingSets;
	}
	
	// finds set of labels the passed label intersects
	private Set<Label> getIntersectedLabel(Label label, List<Label> labels)
	{
		Set<Label> labelsGroup = new HashSet<Label>();
		
		labelsGroup.add(label);
		
		for (Label otherLabel : labels)
		{
			if (otherLabel == label) // if same label
			{
				continue;
			}

			BackboneLabelItem otherLabelItem = otherLabel.getLabelTextItem();

			if (otherLabelItem.getGlobalBounds().intersects(label.getLabelTextItem().getGlobalBounds()))
			{
				labelsGroup.add(otherLabel);
			}
		}

		return labelsGroup;
	}
}
