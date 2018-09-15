package ca.corefacility.gview.map.event;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A standalone implementation of the GViewEventSubject interface
 * @author aaron
 * @author Matthew
 *
 */
public class GViewEventSubjectImp implements GViewEventSubject
{
	private CopyOnWriteArrayList<GViewEventListener> eventListenersList;
	
	public GViewEventSubjectImp()
	{
		eventListenersList = new CopyOnWriteArrayList<GViewEventListener>();
	}
	
	public GViewEventSubjectImp(GViewEventSubjectImp eventSubjImp)
	{
		if (eventSubjImp == null)
		{
			throw new NullPointerException("eventSubjImp is null");
		}
		
		this.eventListenersList = (CopyOnWriteArrayList<GViewEventListener>)eventSubjImp.eventListenersList.clone();
	}
	
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((eventListenersList == null) ? 0 : eventListenersList
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GViewEventSubjectImp other = (GViewEventSubjectImp) obj;
		if (eventListenersList == null)
		{
			if (other.eventListenersList != null)
				return false;
		}
		else if (!eventListenersList.equals(other.eventListenersList))
			return false;
		return true;
	}

	public void addEventListener(GViewEventListener listener)
	{
		eventListenersList.add(listener);
	}
	
	public void removeEventListenerType(Class<? extends GViewEventListener> c)
	{
	    if (c == null)
	    {
	        return;
	    }

	    boolean deleted = false;
	    do
	    {
	        deleted = false;
    
            for (int i = 0; !deleted && i < eventListenersList.size(); i++)
            {
                GViewEventListener listener = eventListenersList.get(i);
                if (listener != null && c.isAssignableFrom(listener.getClass()))
                {
                    eventListenersList.remove(i);
                    deleted = true;
                }
            }
	    } while (deleted);
	}
	
	public void removeEventListener(GViewEventListener listener)
	{
		eventListenersList.remove(listener);
	}

	
	public void removeAllEventListeners()
	{
		eventListenersList.clear();
	}

	public void fireEvent(GViewEvent event)
	{

		Iterator<GViewEventListener> listeners = eventListenersList.iterator();

		while (listeners.hasNext())
		{
			listeners.next().eventOccured( event );
		}
	}
}
