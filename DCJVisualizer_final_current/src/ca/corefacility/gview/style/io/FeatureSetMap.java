package ca.corefacility.gview.style.io;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.biojava.bio.seq.FeatureFilter;

/**
 * Ties together the names of Feature sets, and the corresponding FeatureFilters.
 * @author aaron
 *
 */
public class FeatureSetMap
{
	private Map<String, FeatureFilter> filtersMap;
	int setNumber;
	
	public FeatureSetMap()
	{
		filtersMap = new HashMap<String, FeatureFilter>();
		setNumber = 0;
	}
	
	/**
	 * Puts the passed filter into this map, and gives a name to it.  If filter already exists in this map, does nothing.
	 * @param filter
	 */
	public void put(FeatureFilter filter)
	{
		if (!filtersMap.containsValue(filter))
		{
			while (filtersMap.containsKey("set" + setNumber))
			{
				setNumber++;
			}
			
			filtersMap.put("set" + setNumber, filter);
			setNumber++;
		}
	}
	
	public Iterator<String> setNameIterator()
	{
		return filtersMap.keySet().iterator();
	}
	
	public boolean containsFilter(FeatureFilter filter)
	{
		return filtersMap.containsValue(filter);
	}
	
	public FeatureFilter get(String setName)
	{
		return filtersMap.get(setName);
	}
	
	public String get(FeatureFilter filter)
	{
		for (String setName : filtersMap.keySet())
		{
			FeatureFilter currFilter = filtersMap.get(setName);
			
			if (currFilter.equals(filter))
			{
				return setName;
			}
		}
		
		return null;
	}

	public void put(String currFeatureFilterID, FeatureFilter filter)
	{
		if (!filtersMap.containsKey(currFeatureFilterID))
		{
			filtersMap.put(currFeatureFilterID, filter);
		}
	}
	
	public String toString()
	{
		return filtersMap.toString();
	}
}
