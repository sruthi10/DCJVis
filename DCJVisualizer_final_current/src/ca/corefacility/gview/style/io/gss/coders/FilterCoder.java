package ca.corefacility.gview.style.io.gss.coders;

import java.util.Iterator;


import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.SlotItemStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.FeatureFilterHandler;

public class FilterCoder
{
	private static final String FEATURE_FILTER_FUNCTION = "FeatureFilter";
	
	public void encodeProperties(FeatureSetMap setMap,
			GSSWriter writer)
	{
		// extract feature filters
		
		writer.startSelector(FEATURE_FILTER_FUNCTION);
		
		Iterator<String> sets = setMap.setNameIterator();
		
		while (sets.hasNext())
		{
			String currSetName = sets.next();
			
			writer.writeProperty(currSetName, FeatureFilterHandler.encode(setMap.get(currSetName)));
		}
		
		writer.endSelector();
	}

	public String getSelectorName()
	{
		return FEATURE_FILTER_FUNCTION;
	}

	/**
	 * Decoes the passed property (name/value) into the passed filtersMap
	 * @param filtersMap
	 * @param name
	 * @param value
	 */
	public void decodeProperty(FeatureSetMap filtersMap, String name, LexicalUnit value)
	{
		if (filtersMap == null)
		{
			throw new NullPointerException("filtersMap is null");
		}
		else if (name == null)
		{
			throw new NullPointerException("name is null");
		}
		else if (value == null)
		{
			throw new NullPointerException("value is null");
		}
		
		FeatureFilter filter = FeatureFilterHandler.decode(value);
		
		if (filter != null)
		{
			filtersMap.put(name, filter);
		}
	}
	
	public FeatureSetMap getFeatureFilters(MapStyle mapStyle)
	{
		FeatureSetMap setMap = new FeatureSetMap();
		
		Iterator<SlotStyle> slots = mapStyle.getDataStyle().slots();
		
		// goes through every slot, and adds all FeatureFilters to the setMap
		while(slots.hasNext())
		{
			SlotStyle currStyle = slots.next();
			
			Iterator<SlotItemStyle> itemStyles = currStyle.styles();
			// traverse all sub-styles, and add these sets to the map
			while (itemStyles.hasNext())
			{
				SlotItemStyle currItemStyle = itemStyles.next();
				
				if (currItemStyle instanceof FeatureHolderStyle)
				{
					FeatureHolderStyle featureStyle = (FeatureHolderStyle)currItemStyle;
					
					addFeatureFiltersR(featureStyle, setMap);
				}
			}
		}
		
		return setMap;
	}
	
	/**
	 * Recursivly adds any feature filters in the passed FeatureHolderStyle to the setMap.
	 * @param holderStyle
	 * @param setMap
	 */
	private void addFeatureFiltersR(FeatureHolderStyle holderStyle, FeatureSetMap setMap)
	{
		Iterator<SlotItemStyle> styles = holderStyle.styles();
		
		FeatureFilter currFilter = holderStyle.getFilter();
		
		if (!setMap.containsFilter(currFilter))
		{
			setMap.put(currFilter);
		}
		
		// traverse all sub-styles, and add these sets to the map
		while (styles.hasNext())
		{
			SlotItemStyle currItemStyle = styles.next();
			
			if (currItemStyle instanceof FeatureHolderStyle)
			{
				FeatureHolderStyle featureStyle = (FeatureHolderStyle)currItemStyle;
				
				addFeatureFiltersR(featureStyle, setMap);
			}
		}
	}
	
	public static String selectorName()
	{
		return FEATURE_FILTER_FUNCTION;
	}
}
