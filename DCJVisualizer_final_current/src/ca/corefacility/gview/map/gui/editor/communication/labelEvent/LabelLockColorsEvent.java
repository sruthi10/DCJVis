package ca.corefacility.gview.map.gui.editor.communication.labelEvent;

/**
 * An action for modifying the label's locked colors state.
 * 
 * @author Eric Marinier
 * 
 */
public class LabelLockColorsEvent extends LabelEvent
{
	private final boolean locked;

	/**
	 * 
	 * @param locked
	 *            Whether or not the labels are locked.
	 */
	public LabelLockColorsEvent(boolean locked)
	{
		this.locked = locked;
	}

	@Override
	public Boolean getData()
	{
		return this.locked;
	}
}
