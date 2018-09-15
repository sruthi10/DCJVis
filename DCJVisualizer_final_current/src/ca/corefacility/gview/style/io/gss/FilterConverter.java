package ca.corefacility.gview.style.io.gss;


import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.coders.FilterCoder;

public class FilterConverter extends SelectorConverter
{
	private FilterCoder filterCoder = new FilterCoder();
	private FeatureSetMap filtersMap;
	
	@Override
	public boolean canProcessSelector(Selector selector)
	{
		boolean canProcess = false;
		
		if (selector instanceof ElementSelector)
		{
			ElementSelector elementSelector = (ElementSelector)selector;
			canProcess = filterCoder.getSelectorName().equals(
					elementSelector.getLocalName());
		}
		else
		{
			canProcess = false;
		}
		
		return canProcess;
	}

	public void startSelector(SelectorList selectors, MapStyle mapStyle)
	{
		if (canProcessSelectors(selectors))
		{
			filtersMap = new FeatureSetMap();
		}
	}

	public void decode(MapStyle currentStyle, Selector selector, String name,
			LexicalUnit value, boolean important)
	{
		if (canProcessSelector(selector))
		{
			filterCoder.decodeProperty(filtersMap, name, value); // decoes new set into filtersMap
		}
	}
	
	public FeatureSetMap getFeatureSetMap()
	{
		return filtersMap;
	}
}
