package ca.corefacility.gview.map.gui.editor.communication.legendEvent;


/**
 * An action for modifying the visibility of a swatch of a legend.
 * 
 * @author Eric Marinier
 *
 */
public class LegendItemShowSwatchEvent extends LegendItemEvent
{
	private final boolean visible;
	
	/**
	 * 
	 * @param visible Whether or not the swatch is visible.
	 */
	public LegendItemShowSwatchEvent(boolean visible)
	{
		this.visible = visible;
	}

	@Override
	public Boolean getData()
	{
		return this.visible;
	}
}
