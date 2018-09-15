package ca.corefacility.gview.style.io.gss.coders;

import java.util.Stack;

import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;

public class AbstractDescendentCoder
{

	public AbstractDescendentCoder()
	{
		super();
	}

	/**
	 * Returns the type of selector, given the base selector.
	 *  returns the name of the selector (without any id part, etc)
	 *  ex:  if selector was "slot#1 FeatureSet#set1", would return "FeatureSet"
	 * @param baseSelector
	 * @return  The appropriate selector name.
	 */
	protected String getSelectorType(DescendantSelector baseSelector)
	{
		String selectorName = null;
		SimpleSelector simpleSelector;
		ElementSelector elementSelector;
	
		simpleSelector = baseSelector.getSimpleSelector();
	
		if (simpleSelector.getSelectorType() == Selector.SAC_CONDITIONAL_SELECTOR && simpleSelector instanceof ConditionalSelector)
		{
			ConditionalSelector condSelector = (ConditionalSelector)simpleSelector;
	
			simpleSelector = condSelector.getSimpleSelector();
	
			if (simpleSelector instanceof ElementSelector)
			{
				elementSelector = (ElementSelector)simpleSelector;
	
				selectorName = elementSelector.getLocalName();
			}
		}
		else if (simpleSelector.getSelectorType() == Selector.SAC_ELEMENT_NODE_SELECTOR && simpleSelector instanceof ElementSelector)
		{
			selectorName = ((ElementSelector)simpleSelector).getLocalName();
		}
	
		return selectorName;
	}

	public static Stack<Selector> getAncestorSelectors(DescendantSelector baseSelector)
	{
		Stack<Selector> ancestors = new Stack<Selector>(); // used to store up all the ancestor selectors, so we can traverse
		//	in reverse order
	
		Selector currentSelector = baseSelector.getSimpleSelector();
	
		ancestors.add(currentSelector);
	
		DescendantSelector descendantSelector;
		Selector ancestor = baseSelector.getAncestorSelector();
	
		// traverse through selectors until we hit the very end conditional selector (slot#id)
		while (ancestor instanceof DescendantSelector)
		{
			descendantSelector = (DescendantSelector)ancestor;
			currentSelector = descendantSelector.getSimpleSelector();
	
			// add the next selector
			ancestors.add(currentSelector);
	
			ancestor = descendantSelector.getAncestorSelector();
		}
	
		// should be at slot selector
		currentSelector = ancestor;
		ancestors.add(currentSelector);
	
		return ancestors;
	}
}