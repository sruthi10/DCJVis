package ca.corefacility.gview.map.gui.editor.communication.labelEvent;

import java.awt.Color;

/**
 * An action for modifying the label's background color.
 * 
 * @author Eric Marinier
 * 
 */
public class LabelBackgroundColorEvent extends LabelEvent
{
	private final Color color;

	/**
	 * 
	 * @param color
	 *            The color.
	 */
	public LabelBackgroundColorEvent(Color color)
	{
		this.color = color;
	}

	@Override
	public Color getData()
	{
		return this.color;
	}
}
