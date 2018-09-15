package ca.corefacility.gview.map.gui.editor.communication.legendEvent;

import java.awt.Paint;

/**
 * An action for modifying the border color of a legend.
 * 
 * @author Eric Marinier
 * 
 */
public class LegendBorderColorEvent extends LegendEvent
{
	private final Paint color;

	/**
	 * 
	 * @param color
	 *            The color of the swatch.
	 */
	public LegendBorderColorEvent(Paint color)
	{
		this.color = color;
	}

	@Override
	public Paint getData()
	{
		return this.color;
	}
}
