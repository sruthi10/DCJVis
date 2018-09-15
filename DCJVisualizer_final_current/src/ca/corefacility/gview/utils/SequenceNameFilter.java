package ca.corefacility.gview.utils;

import java.util.NoSuchElementException;

import org.biojava.bio.Annotation;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.FeatureFilter;

public class SequenceNameFilter implements FeatureFilter
{
    private static final long serialVersionUID = 1L;
    
    private String name;
	
	/**
	 * Builds a filter which will filter out by sequence name.  This will first examine the "SequenceName"
	 *  annotation value for the name, and if not found will use feature.getSequence().getName()
	 * @param name  The sequence name to filter out by.
	 */
	public SequenceNameFilter(String name)
	{
		if (name == null)
		{
			throw new IllegalArgumentException("name is null");
		}
		
		this.name = name;
	}
	
	public String getSequenceName()
	{
		return name;
	}

	@Override
	public boolean accept(Feature f)
	{
		boolean accepted = false;
		
		if (f != null)
		{
			Annotation a = f.getAnnotation();
			if (a != null)
			{
				try
				{
					Object o = a.getProperty("SequenceName");
					if (o instanceof String)
					{
						String currName = (String)o;
						
						if (name.equals(currName))
						{
							accepted = true;
						}
					}
				}
				catch (NoSuchElementException e)
				{
					accepted = false;
				}
			}
			
			if (!accepted)
			{
				accepted = f.getSequence().getName().equals(name);
			}
		}
		
		return accepted;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SequenceNameFilter other = (SequenceNameFilter) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
