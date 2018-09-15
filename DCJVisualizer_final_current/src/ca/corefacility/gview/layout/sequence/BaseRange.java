package ca.corefacility.gview.layout.sequence;

// defines a range in terms of base-pair coordinates
public class BaseRange
{
	private double startBase;
	private double endBase;
	
	public BaseRange(double startBase, double endBase)
	{
		this.endBase = endBase;
		this.startBase = startBase;
	}
	
	public double getStartBase()
	{
		return startBase;
	}
	public void setStartBase(double startBase)
	{
		this.startBase = startBase;
	}
	public double getEndBase()
	{
		return endBase;
	}
	public void setEndBase(double endBase)
	{
		this.endBase = endBase;
	}
	
	public double getDeltaBase()
	{
		return endBase-startBase;
	}
	
	public String toString()
	{
		return "(" + startBase + "," + endBase + ")";
	}
}
