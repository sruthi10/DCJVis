package ca.corefacility.gview.map.gui.editor.communication.setEvent;

import java.awt.Color;

/**
 * An action for modifying the set color.
 * 
 * @author Eric Marinier
 *
 */
public class SetColorEvent extends SetEvent
{
	private final Color color;
	
	/**
	 *
	 * @param color The color.
	 */
	public SetColorEvent(Color color)
	{
		this.color = color;
	}
	
	@Override
	public Color getData()
	{
		return this.color;
	}
}
