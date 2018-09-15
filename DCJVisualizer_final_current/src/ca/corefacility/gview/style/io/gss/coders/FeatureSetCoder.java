package ca.corefacility.gview.style.io.gss.coders;


import java.awt.Paint;
import java.net.URI;
import java.util.Iterator;

import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.SlotItemStyle;
import ca.corefacility.gview.style.datastyle.StyleHolder;
import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleMapper;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.LinkHandler;
import ca.corefacility.gview.style.io.gss.LinkMapper;
import ca.corefacility.gview.style.io.gss.PaintHandler;
import ca.corefacility.gview.style.io.gss.PropertyMapperHandler;
import ca.corefacility.gview.style.io.gss.ShapeEffectHandler;
import ca.corefacility.gview.style.io.gss.ShapeHandler;
import ca.corefacility.gview.style.io.gss.TextExtractorHandler;
import ca.corefacility.gview.style.io.gss.exceptions.NoStyleExistsException;
import ca.corefacility.gview.style.io.gss.exceptions.NoSuchFilterException;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;

public class FeatureSetCoder
{
	private static final String FEATURE_COLOR_NAME = "color";
	private static final String THICKNESS_NAME = "thickness-proportion";
	private static final String FEATURE_EFFECT_NAME = "feature-effect";
	private static final String FEATURE_SHAPE_NAME = "feature-shape";
	private static final String TEXT_EXTRACTOR_NAME = "tooltip-text";
	private static final String HYPERLINK_NAME = "hyperlink";
	private static final String PROPERTY_MAPPER_NAME = "property-mapper";
	private static final String LINK_NAME = "link";

	private static final LabelCoder labelCoder = new LabelCoder();
	
	private final LinkMapper linkMapper;
	
	public FeatureSetCoder(LinkMapper linkMapper)
	{
		this.linkMapper = linkMapper;
	}
	
	/**
	 * Extracts a FeatureFilter from the passed FeatureSet#set selector.
	 * @param selector
	 * @return  The FeatureFilter, or null if not found.
	 */
	public static FeatureFilter extractFeatureFilter(ConditionalSelector selector, FeatureSetMap filtersMap)
	{
		FeatureFilter filter = null;

		if (selector.getSimpleSelector() instanceof ElementSelector)
		{
			ElementSelector holderSelector = (ElementSelector)selector.getSimpleSelector();

			if (FeatureSetCoder.selectorName().equals(holderSelector.getLocalName()))
			{
				Condition condition = selector.getCondition();

				if (condition.getConditionType() == Condition.SAC_ID_CONDITION
						&& condition instanceof AttributeCondition) // should be instance off attribute condition if its an id condition
				{
					AttributeCondition attCondition = (AttributeCondition)condition;

					String condValue = attCondition.getValue(); // value should be the set name for the feature filter

					filter = filtersMap.get(condValue);
				}
			}
			else
			{
				System.err.println("name not " + FeatureSetCoder.selectorName());
			}
		}
		else
		{
			System.err.println("holderSelector not instanceof ElementSelector");
		}

		return filter;
	}

	public void startSelector(StyleHolder parentHolder, FeatureFilter filter) throws NoStyleExistsException, NoSuchFilterException
	{
		// if the FeatureHolderStyle with the passed filter does not exist yet, then create it
		if (parentHolder.getFeatureHolderStyle(filter) == null)
		{
			parentHolder.createFeatureHolderStyle(filter);
		}
	}

	public String getSelectorName()
	{
		return selectorName();
	}

	public void decodeProperty(FeatureHolderStyle holderStyle,
			String name, LexicalUnit value, URI sourceURI) throws ParseException
	{
		if (holderStyle != null)
		{
			if (name.equals(FEATURE_COLOR_NAME))
			{
				Paint paint = PaintHandler.decode(value);
				holderStyle.setPaint(paint);
			}
			else if (name.equals(THICKNESS_NAME))
			{
				if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL || value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
				{
					float number = value.getFloatValue();

					holderStyle.setThickness(number);
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
					holderStyle.setShapeEffectRenderer(shapeEffect);
				}
			}
			else if (name.equals(FEATURE_SHAPE_NAME))
			{
				FeatureShapeRealizer shapeRealizer = ShapeHandler.decodeShape(value);
				if (shapeRealizer != null)
				{
					holderStyle.setFeatureShapeRealizer(shapeRealizer);
				}
			}
			else if (name.equals(TEXT_EXTRACTOR_NAME))
			{
				FeatureTextExtractor textExtractor = TextExtractorHandler.decode(value);
				if (textExtractor != null)
				{
					holderStyle.setToolTipExtractor(textExtractor);
				}
			}
			else if (name.equals(HYPERLINK_NAME))
			{
				FeatureTextExtractor hyperlinkExtractor = TextExtractorHandler.decode(value);
				if (hyperlinkExtractor != null)
				{
					holderStyle.setHyperlinkExtractor(hyperlinkExtractor);
				}
			}
			else if (name.equals(PROPERTY_MAPPER_NAME))
			{
				PropertyStyleMapper propertyStyle = PropertyMapperHandler.decode(value);
				if (propertyStyle != null)
				{
					holderStyle.setPropertyStyleMapper(propertyStyle);
				}
			}
			else if (name.equals(LINK_NAME))
			{
				Link link = LinkHandler.decode(value.getStringValue(), this.linkMapper);
				
				if(link != null)
				{
					holderStyle.setLink(link);
				}
			}
			else
			{
				throw new UnknownPropertyException("\"" + name + "\" is not valid for selector \"" + selectorName() + "\"");
			}
		}
		else
			throw new NullPointerException("holderStyle is null");
	}

	public static String selectorName()
	{
		return "FeatureSet";
	}

	public void encodeStyle(FeatureHolderStyle featureHolderStyle,
			String selectorBase, FeatureSetMap setMap, GSSWriter writer)
	{
		String id = setMap.get(featureHolderStyle.getFilter());

		if (id != null)
		{
			String featureSelectorName = writer.getSelectorName(writer.getDescendantName(selectorBase, getSelectorName()), id);

			writer.startSelector(featureSelectorName);

			writer.writePropertyPaint(FEATURE_COLOR_NAME, featureHolderStyle.getPaint()[0]);
			writer.writeProperty(FEATURE_EFFECT_NAME, ShapeEffectHandler.encodeShapeEffect(featureHolderStyle.getShapeEffectRenderer()));
			writer.writeProperty(FEATURE_SHAPE_NAME, ShapeHandler.encodeShape(featureHolderStyle.getFeatureShapeRealizer()));
			
			if(featureHolderStyle.getHyperlinkExtractor() != null && !featureHolderStyle.getHyperlinkExtractor().equals(FeatureTextExtractor.BLANK))
				writer.writeProperty(HYPERLINK_NAME, TextExtractorHandler.encode(featureHolderStyle.getHyperlinkExtractor()));
			
			writer.writeProperty(THICKNESS_NAME, Double.toString(featureHolderStyle.getThickness()));
			
			if(featureHolderStyle.getToolTipExtractor() != null && !featureHolderStyle.getToolTipExtractor().equals(FeatureTextExtractor.BLANK))
			writer.writeProperty(TEXT_EXTRACTOR_NAME, TextExtractorHandler.encode(featureHolderStyle.getToolTipExtractor()));
			
			//property mapper
			if(featureHolderStyle.getPropertyStyleMapper() != null)
			{
				writer.writeProperty(PROPERTY_MAPPER_NAME, PropertyMapperHandler.encode(featureHolderStyle.getPropertyStyleMapper()));
			}
			
			//link
			if(featureHolderStyle.getLink() != null)
			{
				writer.writeProperty(LINK_NAME, LinkHandler.encode(featureHolderStyle.getLink(), this.linkMapper));
			}

			writer.endSelector();

			// now encode corresponding labelStyle
			labelCoder.encodeStyle(featureHolderStyle.getLabelStyle(), featureSelectorName, writer);

			// now encode any sub-feature styles within this FeatureHolderStyle
			Iterator<SlotItemStyle> itemStyles = featureHolderStyle.styles();
			while (itemStyles.hasNext())
			{
				SlotItemStyle currStyle = itemStyles.next();

				if (currStyle instanceof FeatureHolderStyle)
				{
					encodeStyle((FeatureHolderStyle)currStyle, featureSelectorName, setMap, writer);
				}
			}
		}
	}
}
