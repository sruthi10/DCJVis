package ca.corefacility.gview.map.gui.editor.communication.labelEvent;

/**
 * An action for modifying the label font size.
 * 
 * @author Eric Marinier
 * 
 */
public class LabelFontSizeEvent extends LabelEvent
{
	private final int size;

	/**
	 * 
	 * @param size
	 *            The font size.
	 */
	public LabelFontSizeEvent(int size)
	{
		this.size = size;
	}

	@Override
	public Integer getData()
	{
		return this.size;
	}
}
