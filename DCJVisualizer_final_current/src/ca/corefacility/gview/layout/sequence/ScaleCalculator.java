package ca.corefacility.gview.layout.sequence;

import ca.corefacility.gview.managers.labels.Label;

/**
 * Used to determine scale transisions for labels (dependent on the layout).
 * @author aaron
 *
 */
public abstract class ScaleCalculator
{
	public abstract double intersectsAtScale(Label label, SlotRegion slotRegion);
}
