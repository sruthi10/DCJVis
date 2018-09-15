package ca.corefacility.gview.managers;

import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;

/**
 * Used to manage any style events, and handle swapping of style objects.
 * @author Aaron Petkau
 *
 */
public class StyleEventManager implements GViewEventListener,
		GViewEventSubject
{
	private GViewEventSubjectImp eventSubject;
	
	public StyleEventManager()
	{
		eventSubject = new GViewEventSubjectImp();
	}
	
	public void eventOccured(GViewEvent event)
	{
		eventSubject.fireEvent(event);
	}

	public void addEventListener(GViewEventListener listener)
	{
		eventSubject.addEventListener(listener);
	}

	public void removeAllEventListeners()
	{
		eventSubject.removeAllEventListeners();
	}

	public void removeEventListener(GViewEventListener listener)
	{
		eventSubject.removeEventListener(listener);
	}
}
