package ca.corefacility.gview.map.event.style;

import ca.corefacility.gview.map.event.GViewEvent;

public class StyleChangeEvent extends GViewEvent
{
	private static final long serialVersionUID = -2733944494359681200L;

	// Should I make source a general object, or something more specific  (like Style) which all styles must implmement?
	public StyleChangeEvent(Object source)
	{
		super(source);
	}
}
