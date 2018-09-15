package ca.corefacility.gview.map.gui.editor.communication.slotEvent;

/**
 * An action for modifying the thickness of a slot.
 * 
 * @author Eric Marinier
 * 
 */
public class SlotThicknessEvent extends SlotEvent
{
	private final double thickness;

	/**
	 * 
	 * @param thickness
	 *            The thickness.
	 */
	public SlotThicknessEvent(double thickness)
	{
		this.thickness = thickness;
	}

	@Override
	public Double getData()
	{
		return this.thickness;
	}
}
