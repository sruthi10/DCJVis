package ca.corefacility.gview.layout.prototype;

/**
 * Defines a single point on the sequence.
 * 
 * @author Aaron Petkau
 * 
 */
public class SequencePointImp implements SequencePoint, Cloneable
{
	/**
	 * The base from which the point this SequencePointImp describes is relative to.
	 */
	private double base;

	/**
	 * Defines the height that this point is above the backbone.
	 */
	private double heightFromBackbone;

	/**
	 * Constructs a new Sequence Point
	 * 
	 */
	public SequencePointImp(double base, double heightFromBackbone)
	{
		// TODO check for valid base?

		this.base = base;
		this.heightFromBackbone = heightFromBackbone;
	}

	public SequencePointImp()
	{
		this(0, 0);
	}

	/**
	 * Creates a copy of the passed point.
	 * 
	 * @param point
	 *            The SequencePointImp to copy from.
	 */
	public SequencePointImp(SequencePointImp point)
	{
		base = point.base;
		heightFromBackbone = point.heightFromBackbone;
	}

	public void setBase(double base)
	{
		this.base = base;
	}

	public void setHeightFromBackbone(double heightFromBackbone)
	{
		this.heightFromBackbone = heightFromBackbone;
	}

	public void translate(double base, double heightFromBackbone)
	{
		this.base += base;
		this.heightFromBackbone += heightFromBackbone;
	}

	public double getBase()
	{
		return base;
	}

	public double getHeightFromBackbone()
	{
		return heightFromBackbone;
	}

	@Override
	public String toString()
	{
		return "(" + base + ", " + heightFromBackbone + ")";
	}

	@Override
	public Object clone()
	{
		try
		{
			super.clone();
		}
		catch (CloneNotSupportedException e)
		{
		}
		return new SequencePointImp(this.base, this.heightFromBackbone);
	}
}
