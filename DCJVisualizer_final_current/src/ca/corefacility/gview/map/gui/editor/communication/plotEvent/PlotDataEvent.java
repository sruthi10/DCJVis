package ca.corefacility.gview.map.gui.editor.communication.plotEvent;

/**
 * An action for modifying the plot data.
 * 
 * @author Eric Marinier
 * 
 */
public class PlotDataEvent extends PlotEvent
{
	private final String data;

	/**
	 * 
	 * @param data
	 *            The data.
	 */
	public PlotDataEvent(String data)
	{
		this.data = data;
	}

	@Override
	public String getData()
	{
		return this.data;
	}
}
