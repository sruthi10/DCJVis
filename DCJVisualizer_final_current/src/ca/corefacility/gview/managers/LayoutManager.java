package ca.corefacility.gview.managers;

import java.awt.geom.Point2D;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.LocationConverter;
import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;
import ca.corefacility.gview.map.event.LayoutChangedEvent;
import ca.corefacility.gview.map.inputHandler.ZoomSubject;
import ca.corefacility.gview.style.MapStyle;

/**
 * Handles anything relating to layout (mainly the Backbone/SlotPath objects).
 * @author Aaron Petkau
 *
 */
public class LayoutManager implements GViewEventListener,
	GViewEventSubject
{
	private SlotRegion slotRegion;
	
	private GViewEventSubjectImp eventSubject;
	
	private LocationConverter locationConverter;
	
	public LayoutManager(LocationConverter locationConverter, LayoutFactory factory, MapStyle mapStyle, ZoomSubject zoomSubject)
	{
		eventSubject = new GViewEventSubjectImp();
		
		this.locationConverter = locationConverter;
		
		createLayout(factory, mapStyle, zoomSubject);
	}
	
	// creates new layout
	// if layout changed, send event
	public void createLayout(LayoutFactory layoutFactory, MapStyle mapStyle, ZoomSubject zoomSubject)
	{
		if (layoutFactory == null)
		{
			throw new NullPointerException("layoutFactory is null");
		}
		else if (mapStyle == null)
		{
			throw new NullPointerException("mayStyle is null");
		}
		else if (zoomSubject == null)
		{
			throw new NullPointerException("zoomSubject is null");
		}
		else
		{
			slotRegion = layoutFactory.createSlotRegion(mapStyle, locationConverter, mapStyle.getGlobalStyle().getBackboneStyle().getInitialBackboneLength());
			
			// zoom subject fires events to backbone about zooming in or out
			zoomSubject.removeEventListenerType(Backbone.class);
			zoomSubject.addEventListener(slotRegion.getBackbone()); // should we remove old listener from zoom subject?
			
			// LayoutManager fires events to zoom subject about a new slot region
			addEventListener(zoomSubject);
			
			eventSubject.fireEvent(new LayoutChangedEvent(this, slotRegion)); // need
		}
	}
	
	public Backbone getBackbone()
	{
		return slotRegion.getBackbone();
	}

	public void eventOccured(GViewEvent event)
	{
		if (event instanceof BackboneZoomEvent)
		{
			// forward event on
			eventSubject.fireEvent(event);	
		}
	}

	public void addEventListener(GViewEventListener listener)
	{
		eventSubject.addEventListener(listener);
		
		// should we send an event to this listener automatically, forcing it to re-draw as soon as it's added?
		// at least this forces each manager to properly layout it's items as soon as it's added.  Though
		// is this what we want?
		listener.eventOccured(new LayoutChangedEvent(this, slotRegion));
	}

	public void removeAllEventListeners()
	{
		eventSubject.removeAllEventListeners();
	}

	public void removeEventListener(GViewEventListener listener)
	{
		eventSubject.removeEventListener(listener);
	}

	public Point2D getCenterPoint()
	{
		return slotRegion.getCenter();
	}

	public SlotRegion getSlotRegion()
	{
		return slotRegion;
	}
}
