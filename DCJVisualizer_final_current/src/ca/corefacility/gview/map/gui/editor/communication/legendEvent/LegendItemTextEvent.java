package ca.corefacility.gview.map.gui.editor.communication.legendEvent;


/**
 * An Action for modifying the text of a legend.
 * 
 * @author Eric Marinier
 *
 */
public class LegendItemTextEvent extends LegendItemEvent
{
	private final String text;
	
	/**
	 * 
	 * @param text The text of the legend.
	 */
	public LegendItemTextEvent(String text)
	{
		this.text = text;
	}

	@Override
	public String getData()
	{
		return this.text;
	}
}
