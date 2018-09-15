package ca.corefacility.gview.map.gui.editor.communication.plotEvent;


/**
 * An action for modifying the minimum value of the plot's range.
 * 
 * @author Eric Marinier
 * 
 */
public class PlotRangeMinimumEvent extends PlotEvent
{
	private final int min;

	/**
	 * 
	 * @param min
	 *            The minimum value of the range.
	 */
	public PlotRangeMinimumEvent(int min)
	{
		this.min = min;
	}

	@Override
	public Integer getData()
	{
		return this.min;
	}
}
