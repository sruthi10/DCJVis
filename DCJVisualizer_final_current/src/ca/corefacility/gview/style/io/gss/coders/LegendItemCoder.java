package ca.corefacility.gview.style.io.gss.coders;

import java.awt.Font;
import java.awt.Paint;
import java.net.URI;
import java.util.Hashtable;
import java.util.Map;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;

import ca.corefacility.gview.map.controllers.LegendItemStyleToken;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.style.io.gss.AlignmentHandler;
import ca.corefacility.gview.style.io.gss.BooleanHandler;
import ca.corefacility.gview.style.io.gss.FontHandler;
import ca.corefacility.gview.style.io.gss.FontHandler.MalformedFontStringException;
import ca.corefacility.gview.style.io.gss.LinkHandler;
import ca.corefacility.gview.style.io.gss.LinkMapper;
import ca.corefacility.gview.style.io.gss.PaintHandler;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;
import ca.corefacility.gview.style.items.LegendItemStyle;
import ca.corefacility.gview.style.items.LegendStyle;
import ca.corefacility.gview.style.items.LegendTextAlignment;

public class LegendItemCoder
{
	private static final String SHOW_SWATCH_NAME = "show-swatch";
	private static final String SWATCH_COLOR_NAME = "swatch-color";
	private static final String TEXT_COLOR_NAME = "text-color";
	private static final String TEXT_FONT_NAME = "text-font";
	private static final String LEGEND_TEXT_NAME = "legend-text";
	private static final String LEGEND_ALIGNMENT_NAME = "alignment";
	private static final String LINK_NAME = "link";

	private final LinkMapper linkMapper;

	/**
	 * Maps the id for a particular legend and legendItem selector to a
	 * legendItemStyle
	 **/
	private Map<String, Map<String, LegendItemStyle>> legendStyleMap;

	// private Map<String, LegendItemStyle> legendItemStyleMap = new
	// Hashtable<String, LegendItemStyle>();

	public LegendItemCoder(LinkMapper linkMapper)
	{
		this.legendStyleMap = new Hashtable<String, Map<String, LegendItemStyle>>();
		this.linkMapper = linkMapper;
	}

	/**
	 * Decodes the passed LexicalUnit value representing the legendItemStyle
	 * under the passed legendStyle.
	 * 
	 * @param legendStyleId
	 *            The id of the legend style to decode into.
	 * @param legendStyle
	 *            The legendStyle to decode into.
	 * @param legendItemSelector
	 * @param name
	 *            The name of the property to decode.
	 * @param value
	 *            The value of the property to decode
	 * @param sourceURI
	 * @throws ParseException
	 */
	public void decodeProperty(String legendStyleId, LegendStyle legendStyle, ConditionalSelector legendItemSelector,
			String name, LexicalUnit value, URI sourceURI) throws ParseException
	{
		if (legendStyle == null)
		{
			System.err.println("legendStyle is null");
		}
		else
		{
			String legendItemId = extractLegendItemId(legendItemSelector);
			Map<String, LegendItemStyle> legendItemStyleMap = legendStyleMap.get(legendStyleId);
			if (legendItemStyleMap == null)
			{
				throw new ParseException("no mapping exists for legend id #" + legendStyleId + " to legenditem styles");
			}

			LegendItemStyle legendItemStyle = legendItemStyleMap.get(legendItemId);

			if (legendItemStyle == null)
			{
				throw new ParseException("no LegendItemStyle for legenditem id #" + legendItemId + " exists");
			}

			if (name.equals(SHOW_SWATCH_NAME))
			{
				boolean showSwatch = BooleanHandler.decode(value);
				legendItemStyle.setShowSwatch(showSwatch);
			}
			else if (name.equals(SWATCH_COLOR_NAME))
			{
				Paint paint = PaintHandler.decode(value);
				legendItemStyle.setSwatchPaint(paint);
			}
			else if (name.equals(TEXT_COLOR_NAME))
			{
				Paint paint = PaintHandler.decode(value);
				legendItemStyle.setTextPaint(paint);
			}
			else if (name.equals(TEXT_FONT_NAME))
			{
				try
				{
					Font font = FontHandler.decode(value);
					legendItemStyle.setTextFont(font);
				}
				catch (MalformedFontStringException e)
				{
					throw new ParseException(e);
				}
			}
			else if (name.equals(LEGEND_TEXT_NAME))
			{
				if (value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
				{
					legendItemStyle.setText(value.getStringValue());
				}
				else
				{
					throw new ParseException("invalid value " + value + " for " + name);
				}
			}
			else if (name.equals(LEGEND_ALIGNMENT_NAME))
			{
				if (value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE
						|| value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
				{
					LegendTextAlignment align = AlignmentHandler.decodeTextAlignment(value);
					legendItemStyle.setTextAlignment(align);
				}
				else
				{
					throw new ParseException("invalid value " + value + " for " + name);
				}
			}
			else if (name.equals(LINK_NAME))
			{
				Link link = LinkHandler.decode(value.getStringValue(), this.linkMapper);
				link.setLegend(new LegendItemStyleToken(legendItemStyle));

				if (link != null)
				{
					legendItemStyle.setLink(link);
				}
			}
			else
			{
				throw new UnknownPropertyException("\"" + name + "\" is not valid for selector \"" + selectorName()
						+ "\"");
			}
		}
	}

	public String getSelectorName()
	{
		return selectorName();
	}

	public static String selectorName()
	{
		return "legenditem";
	}

	private String extractLegendItemId(ConditionalSelector legendSelector)
	{
		String id = null;

		if (legendSelector == null)
		{
			throw new NullPointerException("legendSelector is null");
		}

		Selector simpleSelector = legendSelector.getSimpleSelector();
		if (!(simpleSelector instanceof ElementSelector))
		{
			throw new IllegalArgumentException("legendSelector not selecting on an ElementSelector");
		}
		else
		{
			ElementSelector eSelector = (ElementSelector) simpleSelector;

			if (!selectorName().equals(eSelector.getLocalName()))
			{
				throw new IllegalArgumentException("legendSelector is not selecting over an ElementSelector of type \""
						+ selectorName() + "\"");
			}
		}

		Condition condition = legendSelector.getCondition();
		if (condition.getConditionType() == Condition.SAC_ID_CONDITION && condition instanceof AttributeCondition) // should
																													// be
																													// instance
																													// off
																													// attribute
																													// condition
																													// if
																													// its
																													// an
																													// id
																													// condition
		{
			AttributeCondition attCondition = (AttributeCondition) condition;

			id = attCondition.getValue();
		}

		return id;
	}

	public void startSelector(String legendStyleId, LegendStyle legendStyle, ConditionalSelector legendItemSelector)
	{
		String legendItemId = extractLegendItemId(legendItemSelector);
		Map<String, LegendItemStyle> legendItemStyleMap = null;

		if (!legendStyleMap.containsKey(legendStyleId))
		{
			legendItemStyleMap = new Hashtable<String, LegendItemStyle>();
			legendStyleMap.put(legendStyleId, legendItemStyleMap);
		}
		else
		{
			legendItemStyleMap = legendStyleMap.get(legendStyleId);
		}

		// create new legendItemStyle if it does not exist
		if (!legendItemStyleMap.containsKey(legendItemId))
		{
			LegendItemStyle legendItemStyle = new LegendItemStyle(legendItemId);

			legendStyle.addLegendItem(legendItemStyle);

			legendItemStyleMap.put(legendItemId, legendItemStyle);
		}
	}

	public void encodeLegendItemStyle(LegendItemStyle legendItemStyle, GSSWriter writer, String baseName, int id)
	{
		writer.startSelector(writer.getDescendantName(baseName,
				writer.getSelectorName(getSelectorName(), Integer.toString(id))));

		writer.writeProperty(SHOW_SWATCH_NAME, BooleanHandler.encode(legendItemStyle.isShowSwatch()));
		writer.writePropertyPaint(SWATCH_COLOR_NAME, legendItemStyle.getSwatchPaint());
		writer.writePropertyPaint(TEXT_COLOR_NAME, legendItemStyle.getTextPaint());

		if (legendItemStyle.getTextFont() != null)
			writer.writeProperty(TEXT_FONT_NAME, FontHandler.encode(legendItemStyle.getTextFont()));

		writer.writeProperty(LEGEND_TEXT_NAME, "\"" + legendItemStyle.getText() + "\"");

		// link
		if (legendItemStyle.getLink() != null)
		{
			writer.writeProperty(LINK_NAME, LinkHandler.encode(legendItemStyle.getLink(), this.linkMapper));
		}

		writer.endSelector();
	}
}
