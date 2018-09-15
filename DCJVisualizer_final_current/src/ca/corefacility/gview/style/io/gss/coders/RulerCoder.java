package ca.corefacility.gview.style.io.gss.coders;


import java.awt.Font;
import java.awt.Paint;
import java.net.URI;

import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;

import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.FontHandler;
import ca.corefacility.gview.style.io.gss.LabelLocationHandler;
import ca.corefacility.gview.style.io.gss.PaintHandler;
import ca.corefacility.gview.style.io.gss.ShapeEffectHandler;
import ca.corefacility.gview.style.io.gss.FontHandler.MalformedFontStringException;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;
import ca.corefacility.gview.style.items.LabelLocation;
import ca.corefacility.gview.style.items.RulerStyle;

public class RulerCoder extends GSSCoder
{
	private static final String MAJOR_TICK_COLOR_NAME = "major-tick-color";
	private static final String MINOR_TICK_COLOR_NAME = "minor-tick-color";
	private static final String LABEL_COLOR_NAME = "label-color";
	private static final String LABEL_BACKGROUND_COLOR_NAME = "label-background-color";
	private static final String LABEL_LOCTION_NAME = "label-location";
	private static final String MAJOR_TICK_LENGTH_NAME = "major-tick-length";
	private static final String MINOR_TICK_LENGTH_NAME = "minor-tick-length";
	private static final String TICK_DENSITY_NAME = "tick-density";
	private static final String TICK_THICKNESS_NAME = "tick-thickness";
	private static final String TICK_PADDING_NAME = "tick-padding";
	private static final String TICK_EFFECT_NAME = "tick-effect";
	private static final String LABEL_FONT_NAME = "label-font";

	
	protected void encodeProperties(MapStyle style, FeatureSetMap setMap, GSSWriter writer)
	{
		RulerStyle rulerStyle = style.getGlobalStyle().getRulerStyle();
		
		Paint paint = null;
		
		writer.startSelector(getSelectorName());
		
		paint = rulerStyle.getMajorTickPaint();
		writer.writePropertyPaint(MAJOR_TICK_COLOR_NAME, paint);
	
		paint = rulerStyle.getMinorTickPaint();
		writer.writePropertyPaint(MINOR_TICK_COLOR_NAME, paint);
	
		paint = rulerStyle.getTextPaint();
		writer.writePropertyPaint(LABEL_COLOR_NAME, paint);
		
		paint = rulerStyle.getTextBackgroundPaint();
		writer.writePropertyPaint(LABEL_BACKGROUND_COLOR_NAME, paint);
		
		writer.writeProperty(LABEL_LOCTION_NAME, LabelLocationHandler.encode(rulerStyle.getRulerLabelsLocation()));
		
		// encode other properties
		writer.writeProperty(MAJOR_TICK_LENGTH_NAME, Double.toString(rulerStyle.getMajorTickLength()));
		writer.writeProperty(MINOR_TICK_LENGTH_NAME, Double.toString(rulerStyle.getMinorTickLength()));
		writer.writeProperty(TICK_DENSITY_NAME, Float.toString(rulerStyle.getTickDensity()));
		writer.writeProperty(TICK_THICKNESS_NAME, Double.toString(rulerStyle.getTickThickness()));
		writer.writeProperty(TICK_PADDING_NAME, Double.toString(rulerStyle.getPadding()));
		writer.writeProperty(LABEL_FONT_NAME, FontHandler.encode(rulerStyle.getFont()));
		writer.writeProperty(TICK_EFFECT_NAME, ShapeEffectHandler.encodeShapeEffect(rulerStyle.getShapeEffectRenderer()));
		
		writer.endSelector();
	}
	
	@Override
	public void startSelector(Selector selector, FeatureSetMap filtersMap, MapStyle mapStyle)
	{
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void performDecodeProperty(Selector selector, MapStyle mapStyle,
			FeatureSetMap filtersMap, String name, LexicalUnit value, URI sourceURI) throws ParseException
	{
		RulerStyle rStyle = mapStyle.getGlobalStyle().getRulerStyle();
		
		if (name.equals(MAJOR_TICK_COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			rStyle.setMajorTickPaint(paint);
		}
		else if (name.equals(MINOR_TICK_COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			rStyle.setMinorTickPaint(paint);
		}
		else if (name.equals(LABEL_COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			rStyle.setTextPaint(paint);
		}
		else if (name.equals(LABEL_BACKGROUND_COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			rStyle.setTextBackgroundPaint(paint);
		}
        else if (name.equals(LABEL_LOCTION_NAME))
        {
            LabelLocation rulerLabelsLocation = LabelLocationHandler.decode(value);
            rStyle.setRulerLabelsLocation(rulerLabelsLocation);
        }
		else if (name.equals(MAJOR_TICK_LENGTH_NAME))
		{
			if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL || value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
			{
				float number = value.getFloatValue();
				
				rStyle.setMajorTickLength(number);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid number");
			}
		}
		else if (name.equals(MINOR_TICK_LENGTH_NAME))
		{
			if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL || value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
			{
				float number = value.getFloatValue();
				
				rStyle.setMinorTickLength(number);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid number");
			}
		}
		else if (name.equals(TICK_DENSITY_NAME))
		{
			if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL || value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
			{
				float number = value.getFloatValue();
				
				rStyle.setTickDensity(number);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid number");
			}
		}
		else if (name.equals(TICK_THICKNESS_NAME))
		{
			if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL || value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
			{
				float number = value.getFloatValue();
				
				rStyle.setTickThickness(number);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid number");
			}
		}
		else if (name.equals(TICK_THICKNESS_NAME))
		{
			if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL || value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
			{
				float number = value.getFloatValue();
				
				rStyle.setTickThickness(number);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid number");
			}
		}
		else if (name.equals(TICK_EFFECT_NAME))
		{
			ShapeEffectRenderer shapeEffect = ShapeEffectHandler.decodeShapeEffect(value);
			if (shapeEffect != null)
			{
				rStyle.setShapeEffectRenderer(shapeEffect);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid for " + TICK_EFFECT_NAME);
			}
		}
		else if (name.equals(LABEL_FONT_NAME))
		{
			Font labelFont;
			try
			{
				labelFont = FontHandler.decode(value);

				if (labelFont != null)
				{
					rStyle.setFont(labelFont);
				}
			}
			catch (MalformedFontStringException e)
			{
				throw new ParseException(e);
			}
		}
		else if (name.equals(TICK_PADDING_NAME))
		{
			if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL || value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
			{
				float number = value.getFloatValue();
				
				rStyle.setPadding(number);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid number");
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
		return "ruler";
	}
}
