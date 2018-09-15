package ca.corefacility.gview.managers.ruler;


/**
 * Used to store information about a particular tick mark, such as base and label.
 * @author aaron
 *
 */
public class TickMark
{
	private int base;
	private float lengthProportion;
	private String label;
	
	private Type type;
	
	public enum Type
	{
		LONG,
		SHORT;
	}

	public TickMark(int base, float lengthProportion, String label, Type type)
	{
		assert lengthProportionInRange(lengthProportion);
		
		this.base = base;
		this.label = label;
		this.type = type;
		
		if (lengthProportionInRange(lengthProportion))
		{
			this.lengthProportion = lengthProportion;
		}
		else
		{
			this.lengthProportion = 1.0f;
		}
	}
	
	public Type getType()
	{
		return type;
	}

	// gets the base this mark is located at
	public int getBase()
	{
		return base;
	}
	
	// gets lengthProportion as proportion of slot this mark is in
	// from 0 to 1
	public float getlengthProportion()
	{
		return lengthProportion;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	private boolean lengthProportionInRange(float lengthProportion)
	{
		if ((lengthProportion < 0.0f) || (lengthProportion > 1.0f))
		{
			return false;
		}
		
		return true;
	}

	@Override
	public String toString()
	{
		return "TickMark [base=" + base + ", lengthProportion="
				+ lengthProportion + ", label=" + label + ", type=" + type
				+ "]";
	}
}
