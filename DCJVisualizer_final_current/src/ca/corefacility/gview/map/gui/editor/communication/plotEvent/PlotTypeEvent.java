package ca.corefacility.gview.map.gui.editor.communication.plotEvent;

/**
 * An action for modifying the plot type.
 * 
 * @author Eric Marinier
 * 
 */
public class PlotTypeEvent extends PlotEvent
{
	private final String type;

	/**
	 * 
	 * @param type
	 *            The type.
	 */
	public PlotTypeEvent(String type)
	{
		this.type = type;
	}

	@Override
	public String getData()
	{
		return this.type;
	}
}
