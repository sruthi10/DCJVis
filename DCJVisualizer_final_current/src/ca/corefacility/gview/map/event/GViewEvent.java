package ca.corefacility.gview.map.event;

import java.util.EventObject;

/**
 * The root class for all GView events.
 * @author aaron
 *
 */
public abstract class GViewEvent extends EventObject
{
	private static final long serialVersionUID = -3402109361532274282L;

	/**
	 * Constructs an event object.
	 * @param source  The object on which the event initally occured.
	 */
	public GViewEvent(Object source)
	{
		super(source);
	}
}
