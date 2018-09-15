package ca.corefacility.gview.map.gui.editor.communication.plotEvent;

/**
 * An action for modifying the plot's auto-scale property.
 * 
 * @author Eric Marinier
 * 
 */
public class PlotAutoScaleEvent extends PlotEvent
{
	private final boolean autoScale;

	/**
	 * 
	 * @param autoScale
	 *            Whether or not to auto-scale the plot.
	 */
	public PlotAutoScaleEvent(boolean autoScale)
	{
		this.autoScale = autoScale;
	}

	@Override
	public Boolean getData()
	{
		return this.autoScale;
	}
}
