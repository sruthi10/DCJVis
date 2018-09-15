package ca.corefacility.gview.map.gui.editor.communication.legendEvent;


/**
 * An action for modifying the legend item font family.
 * 
 * @author Eric Marinier
 * 
 */
public class LegendItemFontFamilyEvent extends LegendItemEvent
{
	private final String family;

	/**
	 * 
	 * @param family
	 *            The font family.
	 */
	public LegendItemFontFamilyEvent(String family)
	{
		this.family = family;
	}

	@Override
	public String getData()
	{
		return this.family;
	}
}
