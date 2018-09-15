package ca.corefacility.gview.map.gui.editor.communication.legendEvent;

import java.awt.Color;

/**
 * An action for modifying the text color of a legend.
 * 
 * @author Eric Marinier
 *
 */
public class LegendItemTextColorEvent extends LegendItemEvent
{
	private final Color color;
	
	/**
	 * 
	 * @param color The color of the legend.
	 */
	public LegendItemTextColorEvent(Color color)
	{
		this.color = color;
	}

	@Override
	public Color getData()
	{
		return this.color;
	}
}
