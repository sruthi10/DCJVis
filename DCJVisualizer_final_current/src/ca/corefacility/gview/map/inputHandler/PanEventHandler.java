package ca.corefacility.gview.map.inputHandler;

import ca.corefacility.gview.map.event.DisplayUpdated;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PPanEventHandler;

/**
 * Handles pan events on the GView map.
 * @author aaron
 *
 */
public class PanEventHandler extends PPanEventHandler implements
		GViewEventSubject
{
	private GViewEventSubjectImp eventSubject;
	
	/**
	 * Creates a new PanEventHandler.
	 */
	public PanEventHandler()
	{
		setAutopan(false);
		
		eventSubject = new GViewEventSubjectImp();
	}
	
	@Override
	protected void pan(PInputEvent e)
	{
		PCamera camera = e.getCamera();
		
		// fire display updated event
		eventSubject.fireEvent(new DisplayUpdated(this, camera.getViewBounds(), camera.getBounds(), false));
		
		super.pan(e);
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
