package ca.corefacility.gview.map.controllers;

import ca.corefacility.gview.style.datastyle.PlotStyle;
import ca.corefacility.gview.style.datastyle.SlotItemStyle;

/**
 * This class is responsible for providing access to it's associated style to ONLY 
 * the controllers, which is achieved by using "protected final" method specifiers and 
 * by having the controllers and tokens in the same package.
 * 
 * @author Eric Marinier
 *
 */
public final class PlotStyleToken extends SlotItemStyleToken 
{
	private final PlotStyle plotStyle;
	
	/**
	 * 
	 * @param plotStyle The style to wrap the token around.
	 */
	public PlotStyleToken(PlotStyle plotStyle)
	{
		this.plotStyle = plotStyle;
	}
	
	/**
	 * 
	 * @return The associated style.
	 */
	protected final SlotItemStyle getStyle()
	{
		return this.plotStyle;
	}
	
	/**
	 * 
	 * @return The associated style.
	 */
	protected final PlotStyle getPlotStyle()
	{
		return this.plotStyle;
	}
}
