package ca.corefacility.gview.map.gui.editor.communication.plotEvent;

import java.awt.Color;

/**
 * An action for modifying the upper plot colour.
 * 
 * @author Eric Marinier
 * 
 */
public class PlotUpperColorEvent extends PlotEvent
{
	private final Color color;

	/**
	 * 
	 * @param color
	 *            The color.
	 */
	public PlotUpperColorEvent(Color color)
	{
		this.color = color;
	}

	@Override
	public Color getData()
	{
		return this.color;
	}
}
