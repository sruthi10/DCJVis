package ca.corefacility.gview.utils.thread;

import java.util.concurrent.Callable;

import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.items.MapItem;
import ca.corefacility.gview.map.items.ShapeItem;

/**
 * This class is responsible for executing a portion of the multithreaded zoom event on the shape items.
 * It is implemented as both a Thread and a Callable. The call() method will execute the run() method.
 * 
 * The multithreaded execution of this should be trivial and safe, as we are performing only mathematical 
 * operations on the individual shapes and not yet painting them to any graphics object.
 * 
 * @author Eric Marinier
 *
 */
public class ZoomEventThread extends Thread implements Callable<Object>
{
	private final int start;
	private final int end;
	
	private final MapItem[] items;
	private final BackboneZoomEvent event;
	
	/**
	 * 
	 * @param start The start index of the shapes to perform a zoom event on.
	 * @param end The end index of the shapes to perform a zoom event on.
	 * @param items The map items, of which the range [start:end] will receive zoom events.
	 * @param event The zoom event.
	 */
	public ZoomEventThread(int start, int end, MapItem[] items, BackboneZoomEvent event)
	{
		this.start = start;
		this.end = end;
		
		this.items = items;
		this.event = event;
	}

	@Override
	public void run()
	{
		MapItem current;
		
        for (int i = this.start; i <= this.end; i++) 
        {
        	current = this.items[i];
        	
			if (current instanceof ShapeItem)
			{
				((ShapeItem) current).eventOccured(this.event);
			}
        }
	}

	@Override
	public Boolean call() throws Exception
	{
		this.run();
		
		return true;
	}
}
