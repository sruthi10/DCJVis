package ca.corefacility.gview.managers.labels;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Keeps track of which labels are in which slots, so they can be positioned correctly
 * @author aaron
 *
 */
public class LabelSlotBounds
{
	private ArrayList<List<SequenceLabelBounds>> slotLabels;
	private int maxLabelSlots;
	
	public LabelSlotBounds(int maxLabelSlots)
	{
		slotLabels = new ArrayList<List<SequenceLabelBounds>>();
		this.maxLabelSlots = maxLabelSlots;
		
		// initialize all slot lists
		for (int i = 0; i < 2*maxLabelSlots; i++)
		{
			slotLabels.add(new LinkedList<SequenceLabelBounds>());
		}
	}
	
	public List<SequenceLabelBounds> getBoundsList(int slot)
	{
		List<SequenceLabelBounds> slotLabelsList = null;
		
		if (slot <= maxLabelSlots && slot >= -maxLabelSlots && slot != 0)
		{
			if (slot < 0)
			{
				slotLabelsList = slotLabels.get(slotLabels.size() + slot);
			}
			else
			{
				slotLabelsList = slotLabels.get(slot);
			}
		}
		
		return slotLabelsList;
	}
	
	public void addLabelBoundsTo(SequenceLabelBounds labelBounds, int slot)
	{
		if (slot <= maxLabelSlots && slot >= -maxLabelSlots && slot != 0)
		{
			List<SequenceLabelBounds> slotLabelsList = getBoundsList(slot);
			
			slotLabelsList.add(labelBounds);
		}
	}
}
