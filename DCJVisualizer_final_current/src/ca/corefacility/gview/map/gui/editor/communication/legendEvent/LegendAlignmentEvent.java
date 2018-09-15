package ca.corefacility.gview.map.gui.editor.communication.legendEvent;

/**
 * An action for modifying the alignment of a legend.
 * 
 * @author Eric Marinier
 * 
 */
public class LegendAlignmentEvent extends LegendEvent
{
	private final String alignment;

	/**
	 * 
	 * @param alignment
	 *            The alignment of the legend.
	 */
	public LegendAlignmentEvent(String alignment)
	{
		this.alignment = alignment;
	}

	@Override
	public String getData()
	{
		return this.alignment;
	}
}
