package ca.corefacility.gview.layout.sequence;

/**
 * Defines a rectangle in terms of sequence coordinates (BaseRange, HeightRange)
 * @author Aaron Petkau
 *
 */
public class SequenceRectangle
{
	private BaseRange baseRange;
	private HeightRange heightRange;
	
	public SequenceRectangle(BaseRange baseRange, HeightRange heightRange)
	{
		super();
		this.baseRange = baseRange;
		this.heightRange = heightRange;
	}
	
	public SequenceRectangle(double startBase, double endBase, double startHeight, double endHeight)
	{
		baseRange = new BaseRange(startBase, endBase);
		heightRange = new HeightRange(startHeight, endHeight);
	}

	public BaseRange getBaseRange()
	{
		return baseRange;
	}

	public void setBaseRange(BaseRange baseRange)
	{
		this.baseRange = baseRange;
	}

	public HeightRange getHeightRange()
	{
		return heightRange;
	}

	public void setHeightRange(HeightRange heightRange)
	{
		this.heightRange = heightRange;
	}
	
	public double getStartBase()
	{
		return baseRange.getStartBase();
	}
	
	public double getEndBase()
	{
		return baseRange.getEndBase();
	}
	
	public double getStartHeight()
	{
		return heightRange.getStartHeight();
	}
	
	public double getEndHeight()
	{
		return heightRange.getEndHeight();
	}
	
	public String toString()
	{
		return baseRange + "b, " + heightRange + "h";
	}
}
