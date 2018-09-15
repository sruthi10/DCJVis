package ca.corefacility.gview.style.io.gss;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CharacterDataSelector;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.ProcessingInstructionSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

import ca.corefacility.gview.style.io.gss.coders.*;

import com.steadystate.css.parser.selectors.ConditionalSelectorImpl;
import com.steadystate.css.parser.selectors.DescendantSelectorImpl;
import com.steadystate.css.parser.selectors.ElementSelectorImpl;

public class GSSSelectorFactory implements SelectorFactory
{
	@Override
	public ConditionalSelector createConditionalSelector(
			SimpleSelector selector, Condition condition) throws CSSException
	{
		ConditionalSelector c = null;
		
		if (selector instanceof ElementSelector)
		{
			ElementSelector e = (ElementSelector)selector;
			
			String selectorName = e.getLocalName();
			if (selectorName.equals(FeatureSetCoder.selectorName()) ||
				selectorName.equals(LegendCoder.selectorName()) ||
				selectorName.equals(LegendItemCoder.selectorName()) ||
				selectorName.equals(SlotCoder.selectorName()))
			{
				c = new ConditionalSelectorImpl(selector, condition);
			}
			else
			{
				throw new CSSException("error in selector \"" + selector + "\" : condition \"" + condition + "\" not allowed");
			}
		}
		else
		{
			throw new CSSException("error in selector \"" + selector + "\" : invalid selector");
		}
		
		return c;
	}

	@Override
	public SimpleSelector createAnyNodeSelector() throws CSSException
	{
		throw new CSSException("[error] - createAnyNodeSelector not supported");
	}

	@Override
	public SimpleSelector createRootNodeSelector() throws CSSException
	{
		throw new CSSException("[error] - createRootNodeSelector not supported");
	}

	@Override
	public NegativeSelector createNegativeSelector(SimpleSelector selector)
			throws CSSException
	{
		throw new CSSException("[error] - createNegativeSelector not supported for " + selector);
	}

	@Override
	public ElementSelector createElementSelector(String namespaceURI,
			String tagName) throws CSSException
	{
		ElementSelector e = null;
		
		if (tagName.equals(BackboneCoder.selectorName()) ||
			tagName.equals(BackgroundCoder.selectorName()) ||
			tagName.equals(FeatureSetCoder.selectorName()) ||
			tagName.equals(FilterCoder.selectorName()) ||
			tagName.equals(LabelCoder.selectorName()) ||
			tagName.equals(LegendCoder.selectorName()) ||
			tagName.equals(LegendItemCoder.selectorName()) ||
			tagName.equals(PlotCoder.selectorName()) ||
			tagName.equals(RulerCoder.selectorName()) ||
			tagName.equals(SlotCoder.selectorName()) ||
			tagName.equals(TooltipCoder.selectorName()))
		{
			e = new ElementSelectorImpl(tagName);
		}
		else
		{
			throw new CSSException("error in selector definition \"" + tagName + "\" : invalid selector name");
		}
		
		return e;
	}

	@Override
	public CharacterDataSelector createTextNodeSelector(String data)
			throws CSSException
	{
		throw new CSSException("[error] - createTextNodeSelector not supported for " + data);

	}

	@Override
	public CharacterDataSelector createCDataSectionSelector(String data)
			throws CSSException
	{
		throw new CSSException("[error] - createCDataSectionSelector not supported for " + data);
	}

	@Override
	public ProcessingInstructionSelector createProcessingInstructionSelector(
			String target, String data) throws CSSException
	{
		throw new CSSException("[error] - createProcessingInstructionSelector not supported for " 
				+ target + "," + data);
	}

	@Override
	public CharacterDataSelector createCommentSelector(String data)
			throws CSSException
	{
		throw new CSSException("[error] - createCommentSelector not supported for " + data);
	}

	@Override
	public ElementSelector createPseudoElementSelector(String namespaceURI,
			String pseudoName) throws CSSException
	{
		throw new CSSException("[error] - createPseudoElementSelector not supported for " + namespaceURI + "," + pseudoName);
	}
	
	private String extractSelectorName(Selector parent)
	{
		String name = null;
		
		if (parent instanceof ElementSelector)
		{
			name = ((ElementSelector)parent).getLocalName();
		}
		else if (parent instanceof ConditionalSelector)
		{
			SimpleSelector s = ((ConditionalSelector)parent).getSimpleSelector();
			
			if (s instanceof ElementSelector)
			{
				name = ((ElementSelector)s).getLocalName();
			}
		}
		else if (parent instanceof DescendantSelector)
		{
			SimpleSelector selector = ((DescendantSelector)parent).getSimpleSelector();
			name = extractSelectorName(selector);
		}
		
		return name;
	}

	@Override
	public DescendantSelector createDescendantSelector(Selector parent,
			SimpleSelector descendant) throws CSSException
	{
		DescendantSelector d = null;
		
		String errorMessage = "error in descendant selector definition parent:\"" + parent +
		"\" child:\"" + descendant + "\": ";
		
		String parentName = extractSelectorName(parent);
		String childName = extractSelectorName(descendant);

		if (parentName == null)
		{
			throw new CSSException(errorMessage + "invalid parent selector");
		}
		else if (childName == null)
		{
			throw new CSSException(errorMessage + "invalid child selector");
		}
		else
		{				
			if (parentName.equals(SlotCoder.selectorName()))
			{
				if (childName.equals(FeatureSetCoder.selectorName()) ||
					childName.equals(PlotCoder.selectorName()) ||
					childName.equals(LabelCoder.selectorName()))
				{
					d = new DescendantSelectorImpl(parent, descendant);
				}
				else
				{
					throw new CSSException(errorMessage + "invalid child selector for this parent");
				}
			}
			else if (parentName.equals(FeatureSetCoder.selectorName()))
			{
				if (childName.equals(FeatureSetCoder.selectorName()) ||
						childName.equals(LabelCoder.selectorName()))
				{
					d = new DescendantSelectorImpl(parent, descendant);
				}
				else
				{
					throw new CSSException(errorMessage + "invalid child selector for this parent");
				}
			}
			else if (parentName.equals(LegendCoder.selectorName()))
			{
				if (childName.equals(LegendItemCoder.selectorName()))
				{
					d = new DescendantSelectorImpl(parent, descendant);
				}
				else
				{
					throw new CSSException(errorMessage + "invalid child selector for this parent");
				}
			}
			else
			{
				throw new CSSException("[error] - selector \"" + parent + "\" cannot have any children selectors");
			}
		}
		
		return d;
	}

	@Override
	public DescendantSelector createChildSelector(Selector parent,
			SimpleSelector child) throws CSSException
	{
		throw new CSSException("[error] - createChildSelector not supported for " + parent + "," + child);
	}

	@Override
	public SiblingSelector createDirectAdjacentSelector(short nodeType,
			Selector child, SimpleSelector directAdjacent) throws CSSException
	{
		throw new CSSException("[error] - createDirectAdjacentSelector not supported for "
				+ nodeType + "," + child + "," + directAdjacent);
	}

}
