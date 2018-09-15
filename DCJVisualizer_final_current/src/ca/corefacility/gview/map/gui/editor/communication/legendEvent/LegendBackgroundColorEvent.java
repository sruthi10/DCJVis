package ca.corefacility.gview.map.gui.editor.communication.legendEvent;

import java.awt.Paint;

/**
 * An action for modifying the background color of a legend.
 * 
 * @author Eric Marinier
 * 
 */
public class LegendBackgroundColorEvent extends LegendEvent
{
	private final Paint color;

	/**
	 * 
	 * @param color
	 *            The color of the swatch.
	 */
	public LegendBackgroundColorEvent(Paint color)
	{
		this.color = color;
	}

	@Override
	public Paint getData()
	{
		return this.color;
	}
}
