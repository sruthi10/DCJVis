package ca.corefacility.gview.layout.sequence;

import ca.corefacility.gview.managers.labels.LabelsManager;
import ca.corefacility.gview.style.MapStyle;

/**
 * Provides for a way to construct a particular Layout.  Has specific implementations for each layout possible.
 * @author aaron
 *
 */
public abstract class LayoutFactory
{
	// should I create a slot region here?  Or maybe create a backbone/slotpath builder?
	
	/**
	 * Builds the SlotRegion for this layout.
	 * 
	 * @param mapStyle  The style to use.
	 * @param locationConverter  Object used to convert location constants/give sequence length of data.
	 * @param initialBackboneLength  The length on screen of the backbone initially.
	 * 
	 * @return SlotRegion  The SlotRegion used to control how to draw objects within the slots.
	 */
	public abstract SlotRegion createSlotRegion(MapStyle mapStyle, LocationConverter locationConverter, double initialBackboneLength);
	
	/**
	 * Builds the LabelsManager used to position labels.  This is placed in here since the LabelsManager to use depends on the layout.
	 * @return  The LabelsManager used to position labels.
	 */
	public abstract LabelsManager createLabelsManager();
}
