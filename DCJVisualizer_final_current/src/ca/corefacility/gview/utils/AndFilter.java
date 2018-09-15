package ca.corefacility.gview.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.FeatureFilter;

public class AndFilter implements FeatureFilter
{
	private static final long serialVersionUID = 1L;
	
	ArrayList<FeatureFilter> filters = new ArrayList<FeatureFilter>();
	
	public AndFilter()
	{
		
	}
	
	public AndFilter(FeatureFilter filter1, FeatureFilter filter2)
	{
		this.filters.add(filter1);
		this.filters.add(filter2);
	}
	
	public AndFilter(Collection<FeatureFilter> filters)
	{
		this.filters.addAll(filters);
	}
	
	@Override
	public boolean accept(Feature f)
	{
		boolean result = true;
		
		for(FeatureFilter filter : this.filters)
		{
			result = (result && filter.accept(f));
		}
		
		return result;
	}

	public void add(FeatureFilter filter)
	{
		this.filters.add(filter);
	}
	
	public void add(Collection<FeatureFilter> filters)
	{
		this.filters.addAll(filters);
	}
	
	public List<FeatureFilter> getFilters()
	{
		return this.filters;
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
		AndFilter other = (AndFilter) obj;
		if (filters == null)
		{
			if (other.filters != null)
				return false;
		}
		else if (!filters.equals(other.filters))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
		return result;
	}

	@Override
	public String toString() 
	{
		String result =  "And(";
		
		for(int i = 0; i < this.filters.size(); i++)
		{
			result += this.filters.get(i).toString();
					
			if(i < this.filters.size() - 1)
			{
				result += " , ";
			}
		}
		
		result += ")";
		
		return result;
	}
}
