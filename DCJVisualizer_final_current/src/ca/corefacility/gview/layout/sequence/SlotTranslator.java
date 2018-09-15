package ca.corefacility.gview.layout.sequence;

/**
 * Used to access the thicknesses/heights of slots from the backbone.
 * 
 * @author Aaron Petkau
 */
public interface SlotTranslator
{
	// constants defining range of heightInSlot
	// TODO find where these should go
	
	/**
	 * The very top of a slot.
	 */
	public final static double TOP = 1;
	
	/**
	 * The center of a slot.
	 */
	public final static double CENTER = 0;
	
	/**
	 * The bottom of a slot.
	 */
	public final static double BOTTOM = -1;
	
	/**
	 * Gives the point on the actual screen from the passed parameters.
	 * 
	 * @param slot  The slot we are in.
	 * @param heightInSlot	The height within the slot.  Range of [BOTTOM, TOP]
	 * 
	 * @return  The actual height above the backbone.
	 */
	public double getHeightFromBackbone(int slot, double heightInSlot);
	
	/**
	 * @return  The height above the backbone of the edge of the top most slot.
	 */
	public double getTopMostHeight();
	
	/**
	 * @return  The height above the backbone of the edge of the bottom most slot.
	 */
	public double getBottomMostHeight();

	public double getTopTickThickness();

	public double getBottomTickThickness();
	
//	/**
//	 * Returns the thickness of the passed slot.
//	 * 
//	 * @param slot  The slot to obtain the thickness from.
//	 * @return  The thickness of the slots.
//	 */
//	public double getThickness(int slot);
//	
//	/**
//	 * Returns the height from the backbone of all the slots up to the passed slot.
//	 * 
//	 * @param slot  The slot to calculate the thickness up to.
//	 * @return  The height of all the slots from the backbone up to the passed slot,
//	 * 				positive for slots above, negative for slots below.
//	 */
//	public double getHeightUpTo(int slot);
//	
//	// obtains heights at the very top/bottom of the slots
//	public double getTopMostHeight();
//	public double getBottomMostHeight();
//	
//	/**
//	 * @return  The minimum possible slot.
//	 */
//	public int getMinSlot();
//	
//	/**
//	 * @return  The maximum possible slot.
//	 */
//	public int getMaxSlot();
	
	// determines if the passed slot is valid
//	public boolean slotIsValid(int slot);
}
