package ca.corefacility.gview.utils.thread;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.swing.SwingUtilities;

/**
 * This class is a collection of utilities for executing runnables on threads.
 * 
 * @author Eric Marinier
 * 
 */
public class ThreadService
{
	private static int threads = Runtime.getRuntime().availableProcessors();
	private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(ThreadService.threads);

	/**
	 * This method will return a list of equal ranges, determined by the passed
	 * count parameter and the number of active threads.
	 * 
	 * @param count
	 *            The total number of items to be divided over the threads.
	 * @return A list of equal ranges. More specifically, each range will
	 *         contain (count / threads) items and the first (count % threads)
	 *         items will contain an extra item. The ranges will be organized in
	 *         ascending order.
	 */
	public static ArrayList<ThreadRange> getRanges(int count)
	{
		ArrayList<ThreadRange> ranges = new ArrayList<ThreadRange>();

		int extra = count % ThreadService.threads;
		int start = 0;
		int end = start;

		// CREATE EXTRA LENGTH
		for (int i = 0; i < extra; i++)
		{
			end = start + (count / ThreadService.threads);

			ranges.add(i, new ThreadRange(start, end));

			start = end + 1;
		}

		// CREATE NORMAL LENGTH
		for (int i = extra; i < ThreadService.threads; i++)
		{
			end = start + (count / ThreadService.threads) - 1;

			ranges.add(i, new ThreadRange(start, end));

			start = end + 1;
		}

		return ranges;
	}

	/**
	 * Sets the number of threads to use.
	 * 
	 * WARNING: This should be called as early as possible and ideally before
	 * any threads have ever begun execution.
	 * 
	 * @param threads
	 *            The number of the threads to use.
	 */
	public static synchronized void setNumThreads(int threads)
	{
		if (ThreadService.threads > 0 && ThreadService.threads != threads)
		{
			ThreadService.threads = threads;

			ThreadService.executor.shutdownNow();
			ThreadService.executor = new ScheduledThreadPoolExecutor(threads);
		}
	}

	/**
	 * Invokes all the callables and returns a list of their Future objects or
	 * NULL list if there was an exception.
	 * 
	 * @param callables
	 *            The callables to invoke.
	 */
	public static List<Future<Object>> invokeAll(List<Callable<Object>> callables)
	{
		try
		{
			return ThreadService.executor.invokeAll(callables);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * This class will execute the passed runnable on the Swing Event Dispatch
	 * Thread or the current thread if the current thread is the Swing Event
	 * Dispatch Thread.
	 * 
	 * In either case, the Runnable will be executed SYNCHRONOUSLY.
	 * 
	 * @param runnable
	 *            The runnable object to run.
	 */
	public static void executeOnEDT(Runnable runnable)
	{
		// Is the current thread the event dispatch thread?
		// This is important, because we can't modify swing components outside
		// of the EDT
		// and guarantee how things will be executed or resolved.

		// INSIDE: Run normally.
		if (SwingUtilities.isEventDispatchThread())
		{
			runnable.run();
		}
		// DIFFERENT THREAD: Block thread, have necessary code run on the EDT,
		// then resume thread.
		else
		{
			try
			{
				SwingUtilities.invokeAndWait(runnable);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
				e.getTargetException().printStackTrace();
			}
		}
	}
}
