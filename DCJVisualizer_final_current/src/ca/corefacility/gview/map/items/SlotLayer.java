package ca.corefacility.gview.map.items;

import java.awt.Color;

import ca.corefacility.gview.map.controllers.SlotStyleController;
import ca.corefacility.gview.map.controllers.SlotStyleToken;
import ca.corefacility.gview.style.datastyle.SlotStyle;

public class SlotLayer extends OptimizedLayer
{
	private static final long serialVersionUID = 1L;
	
	private final SlotStyleToken slotStyleToken;
	
	/**
	 * 
	 * @param slotStyle The associated slot style for the slot.
	 */
	public SlotLayer(SlotStyle slotStyle)
	{
		this.slotStyleToken = new SlotStyleToken(slotStyle);
	}

	@Override
	public Color getConsensusColor()
	{
		return SlotStyleController.getConsensusColor(this.slotStyleToken);
	}
}
