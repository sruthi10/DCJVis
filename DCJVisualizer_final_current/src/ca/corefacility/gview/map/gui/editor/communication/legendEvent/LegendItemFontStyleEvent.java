package ca.corefacility.gview.map.gui.editor.communication.legendEvent;

import ca.corefacility.gview.map.gui.editor.communication.labelEvent.LabelEvent;

/**
 * An action for modifying the legend item font style.
 * 
 * @author Eric Marinier
 * 
 */
public class LegendItemFontStyleEvent extends LabelEvent
{
	private final String style;

	/**
	 * 
	 * @param style
	 *            The font style.
	 */
	public LegendItemFontStyleEvent(String style)
	{
		this.style = style;
	}

	@Override
	public String getData()
	{
		return this.style;
	}
}
