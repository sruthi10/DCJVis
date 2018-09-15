package ca.corefacility.gview.layout.sequence;

/**
 * Defines a range in terms of height coordinates on the sequence.
 * @author Aaron Petkau
 *
 */
public class HeightRange
{
	private double startHeight; // the minimum height
	private double endHeight; // the maximum height
	
	public HeightRange(double startHeight, double endHeight)
	{
		super();
		this.endHeight = endHeight;
		this.startHeight = startHeight;
	}

	public double getStartHeight()
	{
		return startHeight;
	}

	public void setStartHeight(double startHeight)
	{
		this.startHeight = startHeight;
	}

	public double getEndHeight()
	{
		return endHeight;
	}

	public void setEndHeight(double endHeight)
	{
		this.endHeight = endHeight;
	}
	
	public double getDeltaHeight()
	{
		return endHeight-startHeight;
	}
	
	public String toString()
	{
		return "(" + startHeight + "," + endHeight + ")";
	}
}
