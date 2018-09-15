package ca.corefacility.gview.map.gui.editor.communication.labelEvent;

/**
 * An action for modifying the label font style.
 * 
 * @author Eric Marinier
 * 
 */
public class LabelFontStyleEvent extends LabelEvent
{
	private final String style;

	/**
	 * 
	 * @param style
	 *            The font style.
	 */
	public LabelFontStyleEvent(String style)
	{
		this.style = style;
	}

	@Override
	public String getData()
	{
		return this.style;
	}
}
