package ca.corefacility.gview.managers.labels;

import java.util.List;

import ca.corefacility.gview.layout.sequence.SlotRegion;

public abstract class LabelsPositioner
{	
	protected SlotRegion slotRegion; // need so we can run backbone.calculateClashShift
	
	protected int labelsToKeep = 1000;
	
	public LabelsPositioner(SlotRegion slotRegion)
	{
		this.slotRegion = slotRegion;
	}
	
	/**
	 * Positions the labels at the given zoom level (do I need this?)
	 * @param labels  List of labels to position
	 * @param scale
	 * @return  A list of labels that were activated.
	 */
	public abstract List<Label> positionLabels(List<Label> labels, double scale);
	
	public abstract void setSlotRegion(SlotRegion slotRegion, LabelSlots labelSlots);
	
	public abstract void placeInSlot(int labelSlot, Label label);
}
