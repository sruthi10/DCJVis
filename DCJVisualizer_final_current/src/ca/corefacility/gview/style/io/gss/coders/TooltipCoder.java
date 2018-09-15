package ca.corefacility.gview.style.io.gss.coders;


import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.net.URI;

import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.FontHandler;
import ca.corefacility.gview.style.io.gss.PaintHandler;
import ca.corefacility.gview.style.io.gss.FontHandler.MalformedFontStringException;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;
import ca.corefacility.gview.style.items.TooltipStyle;

public class TooltipCoder extends GSSCoder
{
	private static final String TEXT_COLOR_NAME = "text-color";
	private static final String OUTLINE_COLOR_NAME = "outline-color";
	private static final String BACKGROUND_COLOR_NAME = "background-color";
	private static final String FONT_NAME = "font";

	@Override
	protected void encodeProperties(MapStyle style, FeatureSetMap setMap, GSSWriter writer)
	{
		TooltipStyle tStyle = style.getGlobalStyle().getTooltipStyle();
		
		Paint paint = null;
		
		writer.startSelector(getSelectorName());
		
		paint = tStyle.getTextPaint();
		writer.writePropertyPaint(TEXT_COLOR_NAME, paint);
		
		paint = (Color)tStyle.getOutlinePaint();
		writer.writePropertyPaint(OUTLINE_COLOR_NAME, paint);
		
		paint = (Color)tStyle.getBackgroundPaint();
		writer.writePropertyPaint(BACKGROUND_COLOR_NAME, paint);
		
		writer.writeProperty(FONT_NAME, FontHandler.encode(tStyle.getFont()));
		
		writer.endSelector();
	}

	@Override
	public String getSelectorName()
	{
		return selectorName();
	}

	@Override
	protected void performDecodeProperty(Selector selector, MapStyle mapStyle,
			FeatureSetMap filtersMap, String name, LexicalUnit value, URI sourceURI) throws ParseException
	{
		TooltipStyle tStyle = mapStyle.getGlobalStyle().getTooltipStyle();
		
		if (name.equals(TEXT_COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			tStyle.setTextPaint(paint);
		}
		else if (name.equals(OUTLINE_COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			tStyle.setOutlinePaint(paint);
		}
		else if (name.equals(BACKGROUND_COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			tStyle.setBackgroundPaint(paint);
		}
		else if (name.equals(FONT_NAME))
		{
			Font font;
			try
			{
				font = FontHandler.decode(value);

				if (font != null)
				{
					tStyle.setFont(font);
				}
			}
			catch (MalformedFontStringException e)
			{
				throw new ParseException(e);
			}
		}
		else
		{
			throw new UnknownPropertyException("\"" + name + "\" is not valid for selector \"" + selectorName()+"\"");
		}
	}
	
	

	@Override
	public void startSelector(Selector selector, FeatureSetMap filtersMap, MapStyle mapStyle)
	{
		// TODO Auto-generated method stub
		
	}

	public static String selectorName()
	{
		return "tooltip";
	}
}
