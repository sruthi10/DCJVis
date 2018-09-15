package ca.corefacility.gview.layout.prototype;

/**
 * Defines a SequencePoint which has an offset (in terms of length on map) from the sequence point.
 * This is used for arrow heads. We need the arrow head to have a consistent length from the point
 * no matter the zoom level. So we define the points on the arrow head in terms of one point (eg the
 * tip) then other points are defined in terms of the tip point and some distance behind tip so it
 * stays a consistent length.
 * 
 * @author Aaron Petkau
 * 
 */
public class SequenceOffset implements SequencePoint, Cloneable
{
	/**
	 * Reference point base.
	 */
	private double base;

	/**
	 * Reference point height.
	 */
	private double heightFromBackbone;

	/**
	 * The length offset on the map from the base stored in this sequence point.
	 */
	private double lengthFromBase;

	/**
	 * Constructs a new SequenceOffset with (base,heightFromBackbone) as the reference point, and
	 * lengthFromBase being the length behind the reference point.
	 * 
	 * @param base
	 *            The base of the reference point.
	 * @param heightFromBackbone
	 *            The height of the reference point.
	 * @param lengthFromBase
	 *            The length from the reference point (negative behind, positive ahead).
	 */
	public SequenceOffset(double base, double heightFromBackbone, double lengthFromBase)
	{
		this.base = base;
		this.heightFromBackbone = heightFromBackbone;
		this.lengthFromBase = lengthFromBase;
	}

	/**
	 * @return The length from the reference base.
	 */
	public double getLengthFromBase()
	{
		return lengthFromBase;
	}

	/**
	 * Sets the reference base.
	 */
	public void setBase(double base)
	{
		this.base = base;
	}

	/**
	 * Sets the height from the backbone.
	 */
	public void setHeightFromBackbone(double heightFromBackbone)
	{
		this.heightFromBackbone = heightFromBackbone;
	}

	/**
	 * Translates the reference point by the passed coordinates.
	 */
	public void translate(double base, double heightFromBackbone)
	{
		this.base += base;
		this.heightFromBackbone += heightFromBackbone;
	}

	/**
	 * Sets the length from the base.
	 * 
	 * @param lengthFromBase
	 */
	public void setLengthFromBase(double lengthFromBase)
	{
		this.lengthFromBase = lengthFromBase;
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
		return new SequenceOffset(getBase(), getHeightFromBackbone(), this.lengthFromBase);
	}

	/**
	 * @return The reference base.
	 */
	public double getBase()
	{
		return base;
	}

	/**
	 * @return The height from the backbone.
	 */
	public double getHeightFromBackbone()
	{
		return heightFromBackbone;
	}

	@Override
	public String toString()
	{
		return "SequenceOffset [base=" + base + ", heightFromBackbone="
				+ heightFromBackbone + ", lengthFromBase=" + lengthFromBase
				+ "]";
	}
}
