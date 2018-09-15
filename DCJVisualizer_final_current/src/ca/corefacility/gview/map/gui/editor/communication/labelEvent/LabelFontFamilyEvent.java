package ca.corefacility.gview.map.gui.editor.communication.labelEvent;


/**
 * An action for modifying the label font family.
 * 
 * @author Eric Marinier
 * 
 */
public class LabelFontFamilyEvent extends LabelEvent
{
	private final String family;

	/**
	 * 
	 * @param family
	 *            The font family.
	 */
	public LabelFontFamilyEvent(String family)
	{
		this.family = family;
	}

	@Override
	public String getData()
	{
		return this.family;
	}
}
