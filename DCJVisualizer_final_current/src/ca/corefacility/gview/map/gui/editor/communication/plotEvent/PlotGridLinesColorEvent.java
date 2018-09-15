package ca.corefacility.gview.map.gui.editor.communication.plotEvent;

import java.awt.Color;

/**
 * An action for modifying the plot grid lines colour.
 * 
 * @author Eric Marinier
 * 
 */
public class PlotGridLinesColorEvent extends PlotEvent
{
	private final Color color;

	/**
	 * 
	 * @param color
	 *            The color.
	 */
	public PlotGridLinesColorEvent(Color color)
	{
		this.color = color;
	}

	@Override
	public Color getData()
	{
		return this.color;
	}
}
