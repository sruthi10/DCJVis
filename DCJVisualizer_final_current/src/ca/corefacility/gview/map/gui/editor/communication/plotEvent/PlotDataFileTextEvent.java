package ca.corefacility.gview.map.gui.editor.communication.plotEvent;

/**
 * An action for modifying the plot data file text.
 * 
 * @author Eric Marinier
 * 
 */
public class PlotDataFileTextEvent extends PlotEvent
{
	private final String dataFileText;

	/**
	 * 
	 * @param dataFileText
	 *            The data file text.
	 */
	public PlotDataFileTextEvent(String dataFileText)
	{
		this.dataFileText = dataFileText;
	}

	@Override
	public String getData()
	{
		return this.dataFileText;
	}
}
