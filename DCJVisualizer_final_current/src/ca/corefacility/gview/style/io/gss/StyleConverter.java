package ca.corefacility.gview.style.io.gss;


import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.coders.BackboneCoder;
import ca.corefacility.gview.style.io.gss.coders.BackgroundCoder;
import ca.corefacility.gview.style.io.gss.coders.GSSCoder;
import ca.corefacility.gview.style.io.gss.coders.GSSWriter;
import ca.corefacility.gview.style.io.gss.coders.LegendCoder;
import ca.corefacility.gview.style.io.gss.coders.RulerCoder;
import ca.corefacility.gview.style.io.gss.coders.SlotCoder;
import ca.corefacility.gview.style.io.gss.coders.TooltipCoder;
import ca.corefacility.gview.style.io.gss.exceptions.MalformedDeclarationException;
import ca.corefacility.gview.style.io.gss.exceptions.MalformedSelectorException;
import ca.corefacility.gview.style.io.gss.exceptions.NoStyleExistsException;
import ca.corefacility.gview.style.io.gss.exceptions.NoSuchFilterException;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;

public class StyleConverter extends SelectorConverter
{
	private Map<String, GSSCoder> coderMap = new HashMap<String, GSSCoder>();
	private ArrayList<String> sortedCoders = new ArrayList<String>();
	private FeatureSetMap filtersMap;
	
	public StyleConverter()
	{
		LinkMapper linkMapper = new LinkMapper();
		
		coderMap.put(SlotCoder.selectorName(), new SlotCoder(linkMapper));
		coderMap.put(BackboneCoder.selectorName(), new BackboneCoder());
		coderMap.put(RulerCoder.selectorName(), new RulerCoder());
		coderMap.put(BackgroundCoder.selectorName(), new BackgroundCoder());
		coderMap.put(TooltipCoder.selectorName(), new TooltipCoder());
		coderMap.put(LegendCoder.selectorName(), new LegendCoder(linkMapper));
		
		sortedCoders.add(BackgroundCoder.selectorName());
		sortedCoders.add(BackboneCoder.selectorName());
		sortedCoders.add(RulerCoder.selectorName());
		sortedCoders.add(TooltipCoder.selectorName());
		sortedCoders.add(LegendCoder.selectorName());
		sortedCoders.add(SlotCoder.selectorName());
	}
	
	/**
	 * @return  True if we can process the passed selector name, false otherwise.
	 */
	@Override
	public boolean canProcessSelector(Selector selector) // TODO I may not even need this, since I do checks in decode section anyways?
	{
		boolean canProcess = false;
		
		if (selector instanceof ElementSelector)
		{
			canProcess = coderMap.containsKey(((ElementSelector)selector).getLocalName());
		}
		else if (selector instanceof ConditionalSelector)
		{
			ConditionalSelector cSelector = (ConditionalSelector)selector;
			
			Selector simpleSelector = cSelector.getSimpleSelector();
			
			if (simpleSelector instanceof ElementSelector)
			{
				canProcess = coderMap.containsKey(((ElementSelector)simpleSelector).getLocalName());
			}
		}
		else if (selector instanceof DescendantSelector)
		{
			DescendantSelector descendantSelector = (DescendantSelector)selector;
			
			// extract the very first ancestor in this chain of selectors
			Selector rootSelector = extractRootAncestor(descendantSelector);
			
			// root selector should now be at very top of hierarchy, which should be a conditional selector (ex. slot#1).
			if (rootSelector != null && rootSelector instanceof ConditionalSelector)
			{
				ConditionalSelector cSelector = (ConditionalSelector)rootSelector;
				
				Selector simpleSelector = cSelector.getSimpleSelector();
				
				if (simpleSelector instanceof ElementSelector)
				{
					canProcess = coderMap.containsKey(((ElementSelector)simpleSelector).getLocalName());
				}
			}
		}
		else
		{
			canProcess = false;
		}
		
		return canProcess;
	}
	
	public void startSelector(SelectorList selectors, MapStyle mapStyle) throws ParseException
	{
		for (int i = 0; i < selectors.getLength(); i++)
		{
			Selector selector = selectors.item(i);
			
			GSSCoder coder = getCoder(selector);
			
			if (coder != null)
			{
				coder.startSelector(selector, filtersMap, mapStyle);
			}
		}
	}
	
	private Selector extractRootAncestor(DescendantSelector dSelector)
	{
		Selector rootSelector = dSelector.getAncestorSelector();
		while (rootSelector != null && (rootSelector instanceof DescendantSelector))
		{
			rootSelector = ((DescendantSelector)rootSelector).getAncestorSelector();
		}
		
		return rootSelector;
	}
	
	public void encodeSelector(MapStyle style, FeatureSetMap filtersMap, GSSWriter writer)
	{
		if (style == null)
		{
			throw new NullPointerException("style is null");
		}
		
		if (writer == null)
		{
			throw new NullPointerException("writer is null");
		}
		
		// encode  styles
		for (String name : sortedCoders)
		{
			GSSCoder coder = coderMap.get(name);
			coder.encodeSelector(style, filtersMap, writer);
		}
	}
	
	private GSSCoder getCoder(Selector selector)
	{
		GSSCoder coder = null;
		
		// if element selector, then directly look up coder from the name
		if (selector instanceof ElementSelector)
		{
			coder = coderMap.get(((ElementSelector)selector).getLocalName());
		}
		else if (selector instanceof ConditionalSelector) // otherwise, conditional selector, so need to look up coder from the simple selector
		{
			ConditionalSelector cSelector = (ConditionalSelector)selector;
			
			Selector simpleSelector = cSelector.getSimpleSelector();
			
			if (simpleSelector instanceof ElementSelector)
			{
				String localName = ((ElementSelector)simpleSelector).getLocalName();
				coder = coderMap.get(localName);
			}
		}
		else if (selector instanceof DescendantSelector)
		{
			DescendantSelector descendantSelector = (DescendantSelector)selector;
			
			Selector ancestor = descendantSelector.getAncestorSelector();
			
			// traverse through selectors until we hit the very end conditional selector (slot#id)
			while (ancestor instanceof DescendantSelector)
			{
				descendantSelector = (DescendantSelector)ancestor;
				
				ancestor = descendantSelector.getAncestorSelector();
			}
			
			// should be at slot selector
			if (ancestor instanceof ConditionalSelector)
			{
				ConditionalSelector cSelector = (ConditionalSelector)ancestor;
				
				Selector simpleSelector = cSelector.getSimpleSelector();
				
				if (simpleSelector instanceof ElementSelector)
				{
					String localName = ((ElementSelector)simpleSelector).getLocalName();
					coder = coderMap.get(localName);
				}
			}
		}
		
		return coder;
	}
	
	/**
	 * Decodes the passed "name" and "value" within the passed selector, and writes the appropriate style information to the passed MapStyle.
	 * @param mapStyle  The style to decode into.
	 * @param selector  The selector to decode.
	 * @param name  The name of the property to decode.
	 * @param value  The value of the property to decode.
	 * @throws NoStyleExistsException 
	 * @throws NoSuchFilterException 
	 * @throws MalformedSelectorException 
	 * @throws MalformedDeclarationException 
	 * @throws IOException 
	 */
	public void decode(MapStyle mapStyle, Selector selector, String name, LexicalUnit value, URI sourceURI) throws ParseException, IOException
	{
		GSSCoder coder = getCoder(selector);
		
		if (coder != null)
		{
			coder.decodeProperty(selector, mapStyle, filtersMap, name, value, sourceURI);
		}
		else
		{
			throw new ParseException("no coder for selector " + selector);
		}
	}

	public void setFilterMap(FeatureSetMap filtersMap)
	{
		this.filtersMap = filtersMap;
	}
}
