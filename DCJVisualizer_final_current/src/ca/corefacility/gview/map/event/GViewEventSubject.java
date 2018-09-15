package ca.corefacility.gview.map.event;

/**
 * Interface defining an observer for GView events.
 * 
 * @author Aaron Petkau
 *
 */
public interface GViewEventSubject
{
	public void addEventListener(GViewEventListener listener);
	public void removeEventListener(GViewEventListener listener);
	public void removeAllEventListeners();
	
	// TODO do I need fireEvent here?
//	public void fireEvent(GViewEvent event);
}
