package ca.corefacility.gview.data.readers.cgview;

import org.biojava.bio.seq.StrandedFeature;

import ca.corefacility.gview.style.datastyle.DataStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;

/**
 * Keeps track of current free slot numbers
 * @author aaron
 *
 */
class SlotNumberManager
{
	private int currPositiveSlot = 1;
	private int currNegativeSlot = -1;
	
	public SlotStyle createNewSlot(StrandedFeature.Strand strand, DataStyle dataStyle)
	{
		if (strand == StrandedFeature.POSITIVE)
		{
			return dataStyle.createSlotStyle(currPositiveSlot++);
		}
		else if (strand == StrandedFeature.NEGATIVE)
		{
			return dataStyle.createSlotStyle(currNegativeSlot--);
		}
		else
		{
			return dataStyle.createSlotStyle(currPositiveSlot++);
		}
	}
}
