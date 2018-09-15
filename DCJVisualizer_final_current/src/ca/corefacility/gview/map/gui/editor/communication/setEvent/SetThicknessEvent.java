package ca.corefacility.gview.map.gui.editor.communication.setEvent;


/**
 * An action for modifying the thickness of a set.
 * 
 * @author Eric Marinier
 *
 */
public class SetThicknessEvent extends SetEvent
{
	private final double thickness;
	
	/**
	 * 
	 * @param thickness The thickness.
	 */
	public SetThicknessEvent(double thickness)
	{
		this.thickness = thickness;
	}
	
	@Override
	public Double getData()
	{
		return this.thickness;
	}
}
