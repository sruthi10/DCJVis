package ca.corefacility.gview.style.datastyle;

import org.biojava.bio.seq.FeatureFilter;

/**
 * Used to assign unique id's to each featureHolderStyle.  This is used to "cache" features filtered out in GenomeData, and access them by this id.
 * @author aaron
 *
 */
public class FeatureHolderStyleFactory
{
	private int nextFreeId; // stores the next free unique id
	
	protected FeatureHolderStyleFactory()
	{
		nextFreeId = 0;
	}
	
	/**
	 * Creates a new FeatureHolderStyle with the passed information, sets the id properly
	 * @param filter
	 * @param parent
	 * @return  A FeatureHolderStyle with the passed filter, and passed parent.
	 */
	protected FeatureHolderStyle createFeatureHolderStyle(FeatureFilter filter, FeatureHolderStyle parent)
	{
		FeatureHolderStyle style = null;
		
		if (filter == null)
		{
			throw new NullPointerException("filter is null");
		}
		else
		{
			style = new FeatureHolderStyle(this, parent, filter, nextId());
		}
		
		return style;
	}
	
	/**
	 * @return  The current free id, and increments next free id.
	 */
	private int nextId()
	{
		int id = nextFreeId;
		nextFreeId++;
		
		return id;
	}
	
	public Object clone()
	{
		FeatureHolderStyleFactory factory = new FeatureHolderStyleFactory();
		factory.nextFreeId = nextFreeId;
		
		return factory;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + nextFreeId;
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
		FeatureHolderStyleFactory other = (FeatureHolderStyleFactory) obj;
		if (nextFreeId != other.nextFreeId)
			return false;
		return true;
	}
}
