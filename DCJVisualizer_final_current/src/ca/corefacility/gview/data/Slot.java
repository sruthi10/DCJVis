package ca.corefacility.gview.data;

/**
 * Encapsulates information about slot numbers.
 * 
 * @author Aaron Petkau
 *
 */
public class Slot
{
	/**
	 * Defines the maximum allowable slot.  Done so that there's free int values left for other constants.
	 */
	public static final int MAX_SLOT = 2000000;
	
	/**
	 * Defines the minimum allowable slot number.
	 */
	public static final int MIN_SLOT = -2000000;
	
	/**
	 * The backbone slot number.
	 */
	public static final int BACKBONE = 0;
	
	/**
	 * The upper ruler slot.
	 */
	public static final int UPPER_RULER = Integer.MAX_VALUE;
	
	/**
	 * The lower ruler slot.
	 */
	public static final int LOWER_RULER = Integer.MIN_VALUE;
	
	/**
	 * The first upper slot on the map.
	 */
	public static final int FIRST_UPPER = 1;
	
	/**
	 * The first lower slot on the map.
	 */
	public static final int FIRST_LOWER = -1;
	
	/**
	 * An invalid slot number.
	 */
	public static final int INVALID = Integer.MAX_VALUE-1;
	
	/**
	 * Determines if the passed slot is within the defined max/min real slot numbers.
	 * 
	 * @param realSlot  The slot to check.
	 * 
	 * @return  True if the passed slot is in the valid range for real slot numbers, false otherwise.
	 */
	public static boolean inSlotRange(int realSlot) // Do I need this?
	{
		return ((MIN_SLOT <= realSlot) && (realSlot <= MAX_SLOT));
	}
	
	/**
	 * Determines if the passed slot is a valid slot number.
	 * 
	 * @param slot  The slot to check.
	 * 
	 * @return  True if it is valid, false otherwise.
	 */
	public static boolean validSlot(int slot)
	{
		return inSlotRange(slot) || (BACKBONE == slot) || (UPPER_RULER == slot) || (LOWER_RULER == slot);
	}
}
