package ca.corefacility.gview.utils.thread;

/**
 * This class represents a range of execution of a thread.
 * 
 * @author Eric Marinier
 *
 */
public class ThreadRange
{
	private final int start;
	private final int end;
	
	/**
	 * 
	 * @param start The start of the range.
	 * @param end The end of the range.
	 */
	public ThreadRange(int start, int end)
	{
		this.start = start;
		this.end = end;
	}
	
	/**
	 * 
	 * @return The start of the range.
	 */
	public int getStart()
	{
		return this.start;
	}
	
	/**
	 * 
	 * @return The end of the range.
	 */
	public int getEnd()
	{
		return this.end;
	}
}
