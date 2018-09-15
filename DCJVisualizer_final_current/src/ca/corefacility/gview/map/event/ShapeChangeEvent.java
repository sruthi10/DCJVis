package ca.corefacility.gview.map.event;

/**
 * An event that occurs when a shape has changed.
 * 
 * @author Aaron Petkau
 *
 */
public class ShapeChangeEvent extends GViewEvent
{
	private static final long serialVersionUID = 8822791128678843452L;

	public ShapeChangeEvent(Object source)
	{
		super(source);
	}
}
