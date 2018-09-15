package ca.corefacility.gview.map.gui.editor.communication.plotEvent;

/**
 * An action for modifying the maximum value of the plot's range.
 * 
 * @author Eric Marinier
 * 
 */
public class PlotRangeMaximumEvent extends PlotEvent
{
	private final int max;

	/**
	 * 
	 * @param max
	 *            The minimum value of the range.
	 */
	public PlotRangeMaximumEvent(int max)
	{
		this.max = max;
	}

	@Override
	public Integer getData()
	{
		return this.max;
	}
}
