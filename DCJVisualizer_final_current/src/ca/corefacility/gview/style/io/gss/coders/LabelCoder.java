package ca.corefacility.gview.style.io.gss.coders;

import java.awt.Font;
import java.awt.Paint;
import java.net.URI;

import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.datastyle.DataStyle;
import ca.corefacility.gview.style.datastyle.LabelStyle;
import ca.corefacility.gview.style.io.gss.BooleanHandler;
import ca.corefacility.gview.style.io.gss.FontHandler;
import ca.corefacility.gview.style.io.gss.PaintHandler;
import ca.corefacility.gview.style.io.gss.TextExtractorHandler;
import ca.corefacility.gview.style.io.gss.FontHandler.MalformedFontStringException;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;

public class LabelCoder
{	
	private static final String TEXT_PAINT = "text-color";
	private static final String BACKGROUND_PAINT = "background-color";
	private static final String FONT_NAME = "font";
	private static final String LABEL_TEXT_EXTRACTOR = "label-extractor";
	private static final String SHOW_LABELS = "show-labels";
	
	public String getSelectorName()
	{
		return selectorName();
	}

	/**
	 * Decodes the passed LexicalUnit value into the passed LabelStyle
	 * @param labelStyle  The LabelStyle to decode into.
	 * @param name  The name of the property to decode.
	 * @param value  The value of the property to decode.
	 * @param sourceURI
	 * @throws ParseException 
	 */
	public void decodeProperty(LabelStyle labelStyle,
			String name, LexicalUnit value, URI sourceURI) throws ParseException
	{
		if (labelStyle != null)
		{
			if (name.equals(TEXT_PAINT))
			{
				Paint paint = PaintHandler.decode(value);
				labelStyle.setTextPaint(paint);
			}
			else if (name.equals(BACKGROUND_PAINT))
			{
				Paint paint = PaintHandler.decode(value);
				labelStyle.setBackgroundPaint(paint);
			}
			else if (name.equals(FONT_NAME))
			{
				Font labelFont;
				try
				{
					labelFont = FontHandler.decode(value);

					if (labelFont != null)
					{
						labelStyle.setFont(labelFont);
					}
				}
				catch (MalformedFontStringException e)
				{
					throw new ParseException(e);
				}
			}
			else if (name.equals(LABEL_TEXT_EXTRACTOR))
			{
				FeatureTextExtractor textExtractor = TextExtractorHandler.decode(value);
				if (textExtractor != null)
				{
					labelStyle.setLabelExtractor(textExtractor);
				}
			}
			else if (name.equals(SHOW_LABELS))
			{
				Boolean boolValue = BooleanHandler.decode(value);
				if (boolValue != null)
				{
					labelStyle.setShowLabels(boolValue);
				}
			}
			else
			{
				throw new UnknownPropertyException("\"" + name + "\" is not valid for selector \"" + selectorName() +"\"");
			}
		}
		else
		{
			throw new ParseException("labelStyle is null for property \"" + name + "\", \"" + value + "\"");
		}
	}

	public static String selectorName()
	{
		return "labels";
	}

	/**
	 * Encodes the passed LabelStyle into the passed GSSWriter
	 * @param labelStyle  The LabelStyle to encode.
	 * @param selectorBase  The base name of the selector we are writing (for example "slot#1")
	 * @param writer  The writer to write out the encoded values into.
	 */
	public void encodeStyle(LabelStyle labelStyle,
			String selectorBase, GSSWriter writer)
	{
		if (labelStyle == null)
		{
			throw new NullPointerException("labelStyle is null");
		}
		
		String labelSelectorName = writer.getDescendantName(selectorBase, getSelectorName());
		
		writer.startSelector(labelSelectorName);
		
		writer.writePropertyPaint(TEXT_PAINT, labelStyle.getTextPaint());
		writer.writePropertyPaint(BACKGROUND_PAINT, labelStyle.getBackgroundPaint());
		writer.writeProperty(FONT_NAME, FontHandler.encode(labelStyle.getFont()));
		writer.writeProperty(LABEL_TEXT_EXTRACTOR, TextExtractorHandler.encode(labelStyle.getLabelExtractor()));
		writer.writeProperty(SHOW_LABELS, BooleanHandler.encode(labelStyle.showLabels()));
		
		writer.endSelector();
	}

	public void startSelector(DescendantSelector selector, DataStyle dataStyle)
	{
		// TODO Auto-generated method stub
		
	}
}