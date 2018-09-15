package ca.corefacility.gview.map.gui.editor.communication.labelEvent;

import java.awt.Color;

/**
 * An action for modifying the label's text color.
 * 
 * @author Eric Marinier
 * 
 */
public class LabelTextColorEvent extends LabelEvent
{
	private final Color color;

	/**
	 * 
	 * @param color
	 *            The color.
	 */
	public LabelTextColorEvent(Color color)
	{
		this.color = color;
	}

	@Override
	public Color getData()
	{
		return this.color;
	}
}
