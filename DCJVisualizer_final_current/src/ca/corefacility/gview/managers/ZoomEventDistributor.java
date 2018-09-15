package ca.corefacility.gview.managers;

import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;

/**
 * This class receives zoom events from the backbone, determines if they would result in a context switch or not
 * 	and distributes the appropriate events to any listeners.
 * @author Aaron Petkau
 *
 */
public class ZoomEventDistributor implements GViewEventSubject, GViewEventListener
{
	private GViewEventSubjectImp eventSubject;
	private ResolutionManager resolutionManager;
	
	public ZoomEventDistributor(ResolutionManager resolutionManager)
	{
		eventSubject = new GViewEventSubjectImp();
		this.resolutionManager = resolutionManager;
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
	
	public void eventOccured(GViewEvent event)
	{
		if (event instanceof BackboneZoomEvent)
		{
			BackboneZoomEvent zoomEvent = (BackboneZoomEvent)event;
			
			if (resolutionManager.isNewResolutionLevel(zoomEvent.getBackbone().getScale()))
			{
				resolutionManager.performSwitch(zoomEvent.getBackbone().getScale());
				eventSubject.fireEvent(resolutionManager.createResolutionSwitchEvent(zoomEvent.getBackbone()));
			}
			
			eventSubject.fireEvent(event);
		}
	}
}
