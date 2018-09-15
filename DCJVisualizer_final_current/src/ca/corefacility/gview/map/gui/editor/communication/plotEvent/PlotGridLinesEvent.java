package ca.corefacility.gview.map.gui.editor.communication.plotEvent;

/**
 * An action for modifying the number of plot grid lines.
 * 
 * @author Eric Marinier
 * 
 */
public class PlotGridLinesEvent extends PlotEvent
{
	private final int lines;

	/**
	 * 
	 * @param lines
	 *            The number of plot grid lines.
	 */
	public PlotGridLinesEvent(int lines)
	{
		this.lines = lines;
	}

	@Override
	public Integer getData()
	{
		return this.lines;
	}
}
