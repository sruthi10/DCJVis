package ca.corefacility.gview.map.gui.editor.communication.labelEvent;

/**
 * An action for modifying the label visibilty.
 * 
 * @author Eric Marinier
 * 
 */
public class LabelVisibleEvent extends LabelEvent
{
	private final boolean visible;

	/**
	 * 
	 * @param visible
	 *            Whether or not the labels are visible.
	 */
	public LabelVisibleEvent(boolean visible)
	{
		this.visible = visible;
	}

	@Override
	public Boolean getData()
	{
		return this.visible;
	}
}
