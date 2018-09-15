package ca.corefacility.gview.style.io.gss.coders;

import java.io.IOException;
import java.net.URI;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;

public abstract class GSSCoder
{	
	public abstract void startSelector(Selector selector, FeatureSetMap filtersMap, MapStyle mapStyle) throws ParseException;
	
	protected String extractSelectorId(ConditionalSelector condSelector)
	{
		String id = null;
		
		if (condSelector == null)
		{
			throw new NullPointerException("condSelector is null");
		}
		
		Selector simpleSelector = condSelector.getSimpleSelector();
		if (!(simpleSelector instanceof ElementSelector))
		{
			throw new IllegalArgumentException("condSelector not selecting on an ElementSelector");
		}
		else
		{
			ElementSelector eSelector = (ElementSelector)simpleSelector;
			
			if (!getSelectorName().equals(eSelector.getLocalName()))
			{
				throw new IllegalArgumentException("condSelector is not selecting over an ElementSelector of type \""+getSelectorName()+"\"");
			}
		}
		
		Condition condition = condSelector.getCondition();
		if (condition.getConditionType() == Condition.SAC_ID_CONDITION
				&& condition instanceof AttributeCondition) // should be instance off attribute condition if its an id condition
		{
			AttributeCondition attCondition = (AttributeCondition)condition;
			
			id = attCondition.getValue();
		}
		
		return id;
	}
	
	public void encodeSelector(MapStyle style, FeatureSetMap setMap, GSSWriter writer)
	{
		if (style == null)
		{
			throw new NullPointerException("style is null");
		}
		
		if (writer == null)
		{
			throw new NullPointerException("writer is null");
		}
		
		encodeProperties(style, setMap, writer);
	}
	
	public void decodeProperty(Selector selector, MapStyle mapStyle, FeatureSetMap filtersMap, String name, LexicalUnit value, URI sourceURI) throws ParseException, IOException
	{
		if (mapStyle == null)
		{
			throw new NullPointerException("mapStyle is null");
		}
		
		if (name == null)
		{
			throw new NullPointerException("name is null");
		}
		
		if (value == null)
		{
			throw new NullPointerException("value is null");
		}
		
		if (selector == null)
		{
			throw new NullPointerException("selector is null");
		}
		
		// note: filtersMap can be null, since not every coder uses filtersMap
		
		performDecodeProperty(selector, mapStyle, filtersMap, name, value, sourceURI);
	}
	
	protected abstract void performDecodeProperty(Selector selector, MapStyle mapStyle,
			FeatureSetMap filtersMap, String name, LexicalUnit value, URI sourceURI) throws IOException, ParseException;

	/**
	 * Performs the actual encoding of the properties of the selector.
	 * @param style
	 * @param writer
	 */
	protected abstract void encodeProperties(MapStyle style, FeatureSetMap setMap, GSSWriter writer);
	
	/**
	 * @return  The name of the ElementSelector that this instance of GSSCoder can encode/decode.
	 */
	public abstract String getSelectorName();

	public void endSelector(Selector selector)
	{
		// TODO Auto-generated method stub
		
	}
}
