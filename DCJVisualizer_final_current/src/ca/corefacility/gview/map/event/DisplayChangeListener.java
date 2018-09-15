package ca.corefacility.gview.map.event;

import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;

/**
 * Listens/handles any events which signal a change in the display.
 * @author Aaron Petkau
 *
 */
public class DisplayChangeListener implements PropertyChangeListener, GViewEventListener, GViewEventSubject
{
	private GViewEventSubjectImp eventSubject;
	private PCamera camera; // used so that we can obtain the bounds in view coordinates
	
	/**
	 * Pass the camera so we can obtain the view bounds on any change.
	 * @param camera
	 */
	public DisplayChangeListener(PCamera camera)
	{
		eventSubject = new GViewEventSubjectImp();
		
		this.camera = camera;
	}

	// this should be triggered by any changes in camera view
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (PCamera.PROPERTY_VIEW_TRANSFORM.equals(evt.getPropertyName()))
		{
			Rectangle2D viewBounds = camera.getViewBounds();
			
			eventSubject.fireEvent(new DisplayUpdated(this, viewBounds, camera.getBounds(), false));
		}
		else if (PNode.PROPERTY_BOUNDS.equals(evt.getPropertyName()))
		{
			Rectangle2D viewBounds = camera.getViewBounds();
			
			eventSubject.fireEvent(new DisplayUpdated(this, viewBounds, camera.getBounds(), true));
		}
	}

	public void eventOccured(GViewEvent event)
	{
		if (event instanceof DisplayUpdated)
		{
			eventSubject.fireEvent(event);
		}
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
