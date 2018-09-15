package ca.corefacility.gview.map.controllers;

import ca.corefacility.gview.style.datastyle.SlotStyle;

/**
 * This class is responsible for providing access to it's associated style to ONLY 
 * the controllers, which is achieved by using "protected final" method specifiers and 
 * by having the controllers and tokens in the same package.
 * 
 * @author Eric Marinier
 *
 */
public final class SlotStyleToken
{
	private final SlotStyle slotStyle;
	
	/**
	 * 
	 * @param slotStyle The style to wrap the token around.
	 */
	public SlotStyleToken(SlotStyle slotStyle)
	{
		this.slotStyle = slotStyle;
	}
	
	/**
	 * @return The associated style.
	 */
	protected final SlotStyle getStyle()
	{
		return this.slotStyle;
	}
}
 