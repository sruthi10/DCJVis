package ca.corefacility.gview.map.event;

import ca.corefacility.gview.layout.sequence.SlotRegion;

public class LayoutChangedEvent extends GViewEvent
{
	private static final long serialVersionUID = -1675604014513189758L;
	
	private SlotRegion slotRegion;
//	private ResolutionManager resolutionManager;

	public LayoutChangedEvent(Object source, SlotRegion slotRegion)
	{
		super(source);
		
		this.slotRegion = slotRegion;
	}
	
	// TODO should I have my own method here, or should the user just use getSource() and cast it?
	public SlotRegion getSlotRegion()
	{
		return slotRegion;
	}
}
