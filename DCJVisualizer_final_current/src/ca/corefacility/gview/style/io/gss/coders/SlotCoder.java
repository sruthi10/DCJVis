package ca.corefacility.gview.style.io.gss.coders;


import java.awt.Paint;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.style.GlobalStyle;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.datastyle.DataStyle;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.PlotStyle;
import ca.corefacility.gview.style.datastyle.SlotItemStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleMapper;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.LinkMapper;
import ca.corefacility.gview.style.io.gss.PaintHandler;
import ca.corefacility.gview.style.io.gss.PropertyMapperHandler;
import ca.corefacility.gview.style.io.gss.ShapeEffectHandler;
import ca.corefacility.gview.style.io.gss.ShapeHandler;
import ca.corefacility.gview.style.io.gss.TextExtractorHandler;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;

public class SlotCoder extends GSSCoder
{
	private static final String SPACING_NAME = "spacing";
	private static final String FEATURE_COLOR_NAME = "color";
	private static final String THICKNESS_NAME = "thickness";
	private static final String FEATURE_EFFECT_NAME = "feature-effect";
	private static final String FEATURE_SHAPE_NAME = "feature-shape";
	private static final String TEXT_EXTRACTOR_NAME = "tooltip-text";
	private static final String HYPERLINK_NAME = "hyperlink";
	private static final String PROPERTY_MAPPER_NAME = "property-mapper";
	
	private static final LabelCoder labelCoder = new LabelCoder();
	private final DescendentCoder descendentCoder;
	
	private final FeatureSetCoder featureSetCoder; // used to encode FeatureSets (FeatureHolderStyles) in the slots
	private final PlotCoder plotCoder = new PlotCoder();
	
	public SlotCoder(LinkMapper linkMapper)
	{
		this.featureSetCoder = new FeatureSetCoder(linkMapper);
		this.descendentCoder = new DescendentCoder(linkMapper);
	}

	@Override
	protected void encodeProperties(MapStyle style, FeatureSetMap setMap,
			GSSWriter writer)
	{
		GlobalStyle gStyle = style.getGlobalStyle();
		
		// encode global "slot" selector first
		writer.startSelector(getSelectorName());

		if (gStyle.getSlotSpacing() >= 0.0)
		{
			writer.writeProperty(SPACING_NAME, Double.toString(gStyle.getSlotSpacing()));
		}
		
		writer.endSelector();
		// finished encoding global "slot"
		
		for(int i = -1; i >= style.getDataStyle().getLowerSlot(); i--)
		{
			encodeSlot(style.getDataStyle().getSlotStyle(i), setMap, writer);
		}
		
		for(int i = 1; i <= style.getDataStyle().getUpperSlot(); i++)
		{
			encodeSlot(style.getDataStyle().getSlotStyle(i), setMap, writer);
		}
	}
	
	private void encodeSlot(SlotStyle slotStyle, FeatureSetMap setMap, GSSWriter writer)
	{
		String slotSelectorName = writer.getSelectorName(getSelectorName(), "" + slotStyle.getSlot());
		
		writer.startSelector(slotSelectorName);
		
		Paint paint = slotStyle.getPaint();
		writer.writePropertyPaint(FEATURE_COLOR_NAME, paint);
		
		writer.writeProperty(FEATURE_EFFECT_NAME, ShapeEffectHandler.encodeShapeEffect(slotStyle.getShapeEffectRenderer()));
		writer.writeProperty(FEATURE_SHAPE_NAME, ShapeHandler.encodeShape(slotStyle.getFeatureShapeRealizer()));
		
		if(slotStyle.getHyperlinkExtractor() != null && !slotStyle.getHyperlinkExtractor().equals(FeatureTextExtractor.BLANK))
			writer.writeProperty(HYPERLINK_NAME, TextExtractorHandler.encode(slotStyle.getHyperlinkExtractor()));
		
		if (slotStyle.getThickness() >= 0)
		{
			writer.writeProperty(THICKNESS_NAME, Double.toString(slotStyle.getThickness()));
		}
		
		if(slotStyle.getToolTipExtractor() != null && !slotStyle.getToolTipExtractor().equals(FeatureTextExtractor.BLANK))
			writer.writeProperty(TEXT_EXTRACTOR_NAME, TextExtractorHandler.encode(slotStyle.getToolTipExtractor()));
		
		//property mapper
		if(slotStyle.getPropertyStyleMapper() != null)
		{
			writer.writeProperty(PROPERTY_MAPPER_NAME, PropertyMapperHandler.encode(slotStyle.getPropertyStyleMapper()));
		}
	
		writer.endSelector();
		
		// encode corresponding labelStyle
		labelCoder.encodeStyle(slotStyle.getLabelStyle(), slotSelectorName, writer);
		
		// now encode any items within this slot
		Iterator<SlotItemStyle> itemStyles = slotStyle.styles();
		while (itemStyles.hasNext())
		{
			SlotItemStyle currStyle = itemStyles.next();
			
			String baseSelectorName = writer.getSelectorName(getSelectorName(), "" + slotStyle.getSlot());
			
			// TODO need to handle more than just FeatureHolderStyles
			if (currStyle instanceof FeatureHolderStyle)
			{
				featureSetCoder.encodeStyle(((FeatureHolderStyle)currStyle), baseSelectorName, setMap, writer);
			}
			else if (currStyle instanceof PlotStyle)
			{
				plotCoder.encodeStyle((PlotStyle)currStyle, baseSelectorName, writer);
			}
		}
	}
	
//	@Override
	public void startSelector(Selector selector, FeatureSetMap filtersMap, MapStyle mapStyle) throws ParseException
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
			
			int id = extractSlotId(cSelector);

			DataStyle dataStyle = mapStyle.getDataStyle();
			
			// build SlotStyle if it doesn't already exist
			if(!dataStyle.containsSlotStyle(id))
			{
				dataStyle.createSlotStyle(id);
			}
		}
		else if (selector instanceof DescendantSelector)
		{	
			descendentCoder.startSelector((DescendantSelector)selector, mapStyle, filtersMap);
		}
	}

	/**
	 * Extracts a slot id from the passed selector (which should be a conditional selector of from "slot#id").
	 * @param cSelector
	 * @return  The slot id.
	 */
	private static int extractSlotId(ConditionalSelector cSelector)
	{
		if (cSelector == null)
		{
			throw new NullPointerException("cSelector is null");
		}
		
		Selector simpleSelector = cSelector.getSimpleSelector();
		if (!(simpleSelector instanceof ElementSelector))
		{
			throw new IllegalArgumentException("cSelector not selecting on an ElementSelector");
		}
		else
		{
			ElementSelector eSelector = (ElementSelector)simpleSelector;
			
			if (!selectorName().equals(eSelector.getLocalName()))
			{
				throw new IllegalArgumentException("cSelector is not selecting over an ElementSelector of type \"slot\"");
			}
		}
		
		int id = Integer.MIN_VALUE;
		
		Condition condition = cSelector.getCondition();
		if (condition.getConditionType() == Condition.SAC_ID_CONDITION
				&& condition instanceof AttributeCondition) // should be instance off attribute condition if its an id condition
		{
			AttributeCondition attCondition = (AttributeCondition)condition;
			
			try
			{
				String condValue = attCondition.getValue(); // value should the the "id" number
				
				id = Integer.parseInt(condValue);
			}
			catch (NumberFormatException e)
			{
				// throw exception about id not being a number?
				e.printStackTrace();
				throw new IllegalArgumentException("attribute not an integer");
			}
		}
		
		return id;
	}
	
	public static SlotStyle extractSlotStyle(ConditionalSelector cSelector, DataStyle dataStyle)
	{
		int id = extractSlotId(cSelector);

		SlotStyle slotStyle = null;
		
		if(dataStyle.containsSlotStyle(id))
		{
			slotStyle = dataStyle.getSlotStyle(id);
		}
		
		return slotStyle;
	}

	@Override
	protected void performDecodeProperty(Selector selector, MapStyle mapStyle,
			FeatureSetMap filtersMap, String name, LexicalUnit value, URI sourceURI) throws ParseException, IOException
	{
		// if selector was basic element selector, then we can just directly do decoding
		if (selector instanceof ElementSelector)
		{
			GlobalStyle gStyle = mapStyle.getGlobalStyle();
			
			// spacing is a special case, since it's set in GlobalStyle, not in SlotStyle
			if (name.equals(SPACING_NAME))
			{
				if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL)
				{
					float number = value.getFloatValue();
					
					gStyle.setSlotSpacing(number);
				}
			}
			else
			{
				decodeGlobalSlotsTag(mapStyle, name, value);
			}
		}
		// otherwise if it's a conditional selector, we must extract the condition first
		else if (selector instanceof ConditionalSelector)
		{
			ConditionalSelector cSelector = (ConditionalSelector)selector;
			
			SlotStyle slotStyle = extractSlotStyle(cSelector, mapStyle.getDataStyle());
			if (slotStyle == null) // attempt to create a new slot with the passed style
			{
				slotStyle = mapStyle.getDataStyle().createSlotStyle(extractSlotId(cSelector));
			}
			
			decodeSlot(slotStyle, name, value);
		}
		else if (selector instanceof DescendantSelector) // otherwise, descendant selector, so we need to find the correct feature holder style to decode into
		{
			descendentCoder.decodeProperty((DescendantSelector)selector, mapStyle, filtersMap, name, value, sourceURI);
		}
	}
	
	private void decodeGlobalSlotsTag(MapStyle mapStyle, String name, LexicalUnit value) throws ParseException
	{
		DataStyle dStyle = mapStyle.getDataStyle();
		
		Iterator<SlotStyle> slotIterator = dStyle.slots();
		
		while (slotIterator.hasNext())
		{
			SlotStyle slotStyle = slotIterator.next();
			
			decodeSlot(slotStyle, name, value);
		}
	}
	
	private void decodeSlot(SlotStyle slotStyle, String name, LexicalUnit value) throws ParseException
	{
		if (name.equals(FEATURE_COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			slotStyle.setPaint(paint);
		}
		else if (name.equals(THICKNESS_NAME))
		{
			if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL || value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
			{
				float number = value.getFloatValue();

				slotStyle.setThickness(number);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid number");
			}
		}
		else if (name.equals(FEATURE_EFFECT_NAME))
		{
			ShapeEffectRenderer shapeEffect = ShapeEffectHandler.decodeShapeEffect(value);
			if (shapeEffect != null)
			{
				slotStyle.setShapeEffectRenderer(shapeEffect);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid for " + FEATURE_EFFECT_NAME);
			}
		}
		else if (name.equals(FEATURE_SHAPE_NAME))
		{
			FeatureShapeRealizer shapeRealizer = ShapeHandler.decodeShape(value);
			if (shapeRealizer != null)
			{
				slotStyle.setFeatureShapeRealizer(shapeRealizer);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid for " + FEATURE_SHAPE_NAME);
			}
		}
		else if (name.equals(TEXT_EXTRACTOR_NAME))
		{
			FeatureTextExtractor textExtractor = TextExtractorHandler.decode(value);
			if (textExtractor != null)
			{
				slotStyle.setToolTipExtractor(textExtractor);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid for " + TEXT_EXTRACTOR_NAME);
			}
		}
		else if (name.equals(HYPERLINK_NAME))
		{
			FeatureTextExtractor hyperlinkExtractor = TextExtractorHandler.decode(value);
			if (hyperlinkExtractor != null)
			{
				slotStyle.setHyperlinkExtractor(hyperlinkExtractor);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid for " + HYPERLINK_NAME);
			}
		}
		else if (name.equals(PROPERTY_MAPPER_NAME))
		{
			PropertyStyleMapper propertyStyle = PropertyMapperHandler.decode(value);
			if (propertyStyle != null)
			{
				slotStyle.setPropertyStyleMapper(propertyStyle);
			}
		}
		else
		{
			throw new UnknownPropertyException("\"" + name + "\" is not valid for selector \"" + selectorName()+"\"");
		}
	}

	public String getSelectorName()
	{
		return selectorName();
	}
	
	public static String selectorName()
	{
		return "slot";
	}
}
