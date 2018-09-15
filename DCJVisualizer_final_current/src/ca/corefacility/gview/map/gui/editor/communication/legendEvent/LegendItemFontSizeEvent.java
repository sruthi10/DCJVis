package ca.corefacility.gview.map.gui.editor.communication.legendEvent;


/**
 * An action for modifying the legend item font size.
 * 
 * @author Eric Marinier
 * 
 */
public class LegendItemFontSizeEvent extends LegendItemEvent
{
	private final int size;

	/**
	 * 
	 * @param size
	 *            The font size.
	 */
	public LegendItemFontSizeEvent(int size)
	{
		this.size = size;
	}

	@Override
	public Integer getData()
	{
		return this.size;
	}
}
