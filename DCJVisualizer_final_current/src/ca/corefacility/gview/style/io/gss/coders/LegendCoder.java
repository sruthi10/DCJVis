package ca.corefacility.gview.style.io.gss.coders;

import java.awt.Font;
import java.awt.Paint;
import java.io.IOException;
import java.net.URI;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;

import ca.corefacility.gview.style.GlobalStyle;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.AlignmentHandler;
import ca.corefacility.gview.style.io.gss.FontHandler;
import ca.corefacility.gview.style.io.gss.FontHandler.MalformedFontStringException;
import ca.corefacility.gview.style.io.gss.LinkMapper;
import ca.corefacility.gview.style.io.gss.PaintHandler;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;
import ca.corefacility.gview.style.items.LegendAlignment;
import ca.corefacility.gview.style.items.LegendItemStyle;
import ca.corefacility.gview.style.items.LegendStyle;

public class LegendCoder extends GSSCoder
{
	private static final String BACKGROUND_COLOR_NAME = "background-color";
	private static final String BORDER_COLOR_NAME = "border-color";
	private static final String TEXT_COLOR_NAME = "text-color";
	private static final String FONT_NAME = "text-font";
	private static final String ALIGNMENT_NAME = "alignment";
	
	private final Map<String, LegendStyle> legendStylesMap;  // maps the id to a particular legend style
	private final LegendItemCoder legendItemCoder;
	
	private final LinkMapper linkMapper;
	
	public LegendCoder(LinkMapper linkMapper)
	{
		super();
		
		this.legendStylesMap = new Hashtable<String, LegendStyle>();
		this.linkMapper = linkMapper;
		this.legendItemCoder = new LegendItemCoder(linkMapper);
	}
	
	@Override
	public void startSelector(Selector selector, FeatureSetMap filtersMap,
			MapStyle mapStyle) throws ParseException
	{
		if (selector == null)
		{
			throw new NullPointerException("selector is null");
		}
		
		if (mapStyle == null)
		{
			throw new NullPointerException("mapStyle is null");
		}
		
		if (selector instanceof ConditionalSelector)
		{
			ConditionalSelector cSelector = (ConditionalSelector)selector;
			
			String id = extractSelectorId(cSelector);

			if (!legendStylesMap.containsKey(id))
			{
				LegendStyle legendStyle = new LegendStyle();
				
				legendStylesMap.put(id, legendStyle);
				
				GlobalStyle gStyle = mapStyle.getGlobalStyle();
				
				gStyle.addLegendStyle(legendStyle);
			}
		}
		else if (selector instanceof DescendantSelector)
		{	
			LegendStyleAndSelector legendStyleSelector = getLegendStyleAndSelector((DescendantSelector)selector, mapStyle);
			
			legendItemCoder.startSelector(legendStyleSelector.legendStyleId, legendStyleSelector.legendStyle, legendStyleSelector.legendItemSelector);
		}
	}
	
	private LegendStyleAndSelector getLegendStyleAndSelector(DescendantSelector selector,
			MapStyle mapStyle) throws ParseException
	{
		LegendStyleAndSelector legendStyleSelector = null;
		
		Stack<Selector> ancestors = AbstractDescendentCoder.getAncestorSelectors(selector);
		
		if (!ancestors.isEmpty())
		{
			Selector currSelector = ancestors.pop();

			ConditionalSelector currSelectorCond = null;

			if (currSelector instanceof ConditionalSelector)
			{
				LegendStyle legendStyle = null;
				
				currSelectorCond = (ConditionalSelector)currSelector;
				
				String legendId = extractSelectorId(currSelectorCond);
				
				// if no legend style, then create one and add to map style
				if (!legendStylesMap.containsKey(legendId))
				{
					legendStyle = new LegendStyle();
					
					mapStyle.getGlobalStyle().addLegendStyle(legendStyle);
					legendStylesMap.put(legendId, legendStyle);
				}
				else
				{
					legendStyle = legendStylesMap.get(legendId);
				}
				
				currSelector = ancestors.pop();
				
				if (currSelector instanceof ConditionalSelector)
				{
					currSelectorCond = (ConditionalSelector)currSelector;

					legendStyleSelector = new LegendStyleAndSelector(legendId, legendStyle, currSelectorCond);
				}
				else
				{
					throw new ParseException("2nd selector not of type ConditionalSelector in " + selector);
				}		
			}
			else
			{
				throw new ParseException("1st selector not of type ConditionalSelector in " + selector);
			}
		}
		
		return legendStyleSelector;
	}
	
	private class LegendStyleAndSelector
	{
		public LegendStyle legendStyle;
		public ConditionalSelector legendItemSelector;
		public String legendStyleId;
		
		public LegendStyleAndSelector(String legendStyleId, LegendStyle legendStyle,
				ConditionalSelector legendItemSelector)
		{
			super();
			this.legendStyleId = legendStyleId;
			this.legendStyle = legendStyle;
			this.legendItemSelector = legendItemSelector;
		}
	}

	@Override
	protected void performDecodeProperty(Selector selector, MapStyle mapStyle,
			FeatureSetMap filtersMap, String name, LexicalUnit value,
			URI sourceURI) throws IOException, ParseException
	{
		if (selector instanceof ElementSelector)
		{
			
		}
		else if (selector instanceof ConditionalSelector)
		{
			ConditionalSelector cSelector = (ConditionalSelector)selector;
			
			String id = extractSelectorId(cSelector);
			
			LegendStyle legendStyle = legendStylesMap.get(id);
			if (legendStyle == null) // attempt to create a new slot with the passed style
			{
				throw new ParseException("no legend selector with id " + id + " was found");
			}
			
			decodeLegend(legendStyle, name, value);
		}
		else if (selector instanceof DescendantSelector)
		{
			LegendStyleAndSelector legendStyleSelector = getLegendStyleAndSelector((DescendantSelector)selector, mapStyle);

			legendItemCoder.decodeProperty(legendStyleSelector.legendStyleId, legendStyleSelector.legendStyle, legendStyleSelector.legendItemSelector,
					name, value, sourceURI);
		}
	}

	private void decodeLegend(LegendStyle legendStyle, String name,
			LexicalUnit value) throws ParseException
	{
		if (name.equals(BACKGROUND_COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			legendStyle.setBackgroundPaint(paint);
		}
		else if (name.equals(BORDER_COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			legendStyle.setOutlinePaint(paint);
		}
		else if (name.equals(TEXT_COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			legendStyle.setDefaultFontPaint(paint);
		}
		else if (name.equals(FONT_NAME))
		{
			try
			{
				Font font = FontHandler.decode(value);
				legendStyle.setDefaultFont(font);
			}
			catch (MalformedFontStringException e)
			{
				throw new ParseException(e);
			}
		}
		else if (name.equals(ALIGNMENT_NAME))
		{
			LegendAlignment alignment = AlignmentHandler.decode(value);
			
			if (alignment == null)
			{
				throw new ParseException("alignment " + value + " is invalid for " + name);
			}
			else
			{
				legendStyle.setAlignment(alignment);
			}
		}
		else
		{
			throw new UnknownPropertyException("\"" + name + "\" is not valid for selector \"" + selectorName()+"\"");
		}
	}

	@Override
	protected void encodeProperties(MapStyle style, FeatureSetMap setMap, GSSWriter writer)
	{
		Iterator<LegendStyle> legendStyles = style.getGlobalStyle().legendStyles();
		LegendStyle currLegendStyle;
		int currLegendStyleID = 0;
		
		while(legendStyles.hasNext())
		{
			currLegendStyle = legendStyles.next();
			
			encodeLegendStyle(currLegendStyle, writer, currLegendStyleID);
			currLegendStyleID++;
		}
	}

	@Override
	public String getSelectorName()
	{
		return selectorName();
	}

	public static String selectorName()
	{
		return "legend";
	}
	
	private void encodeLegendStyle(LegendStyle legendStyle, GSSWriter writer, int id)
	{
		Iterator<LegendItemStyle> legendItemStyles = legendStyle.legendItems();
		LegendItemStyle currLegendItemStyle;
		LegendItemCoder legendItemCoder = new LegendItemCoder(this.linkMapper);
		
		int currLegendItemStyleID = 0;
		String name = writer.getSelectorName(getSelectorName(), Integer.toString(id));
		
		writer.startSelector(name);
		
		writer.writePropertyPaint(BACKGROUND_COLOR_NAME, legendStyle.getBackgroundPaint());
		writer.writePropertyPaint(BORDER_COLOR_NAME, legendStyle.getOutlinePaint());
		writer.writePropertyPaint(TEXT_COLOR_NAME, legendStyle.getDefaultFontPaint());
		writer.writeProperty(FONT_NAME, FontHandler.encode(legendStyle.getDefaultFont()));
		writer.writeProperty(ALIGNMENT_NAME, AlignmentHandler.encode(legendStyle.getAlignment()));
		
		writer.endSelector();
		
		while(legendItemStyles.hasNext())
		{
			currLegendItemStyle = legendItemStyles.next();
			
			legendItemCoder.encodeLegendItemStyle(currLegendItemStyle, writer, name, currLegendItemStyleID);
			currLegendItemStyleID++;
		}
	}
}
