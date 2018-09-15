package ca.corefacility.gview.map.controllers;

import ca.corefacility.gview.style.datastyle.SlotItemStyle;

/**
 * This class is responsible for providing access to it's associated style to ONLY 
 * the controllers, which is achieved by using "protected final" method specifiers and 
 * by having the controllers and tokens in the same package.
 * 
 * @author Eric Marinier
 *
 */
public abstract class SlotItemStyleToken
{	
	/**
	 * @return The associated style.
	 */
	protected abstract SlotItemStyle getStyle();
}

