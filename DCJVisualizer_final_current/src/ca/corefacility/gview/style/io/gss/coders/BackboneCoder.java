package ca.corefacility.gview.style.io.gss.coders;


import java.awt.Paint;
import java.net.URI;

import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;

import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.PaintHandler;
import ca.corefacility.gview.style.io.gss.ShapeEffectHandler;
import ca.corefacility.gview.style.io.gss.PaintHandler.UnknownPaintException;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;
import ca.corefacility.gview.style.items.BackboneStyle;

public class BackboneCoder extends GSSCoder
{
	private static final String COLOR_NAME = "color";
	private static final String THICKENESS_NAME = "thickness";
	private static final String BACKBONE_EFFECT_NAME = "backbone-effect";
	
	protected void encodeProperties(MapStyle style, FeatureSetMap setMap, GSSWriter writer)
	{
		BackboneStyle bStyle = style.getGlobalStyle().getBackboneStyle();
		
		Paint paint = null;
		
		writer.startSelector(getSelectorName());
		
		paint = bStyle.getPaint();
		try
		{
			writer.writeProperty(COLOR_NAME, PaintHandler.encode(paint));
		}
		catch (UnknownPaintException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.writeProperty(THICKENESS_NAME, Double.toString(bStyle.getThickness()));
		writer.writeProperty(BACKBONE_EFFECT_NAME, ShapeEffectHandler.encodeShapeEffect(bStyle.getShapeEffectRenderer()));
		
		writer.endSelector();
	}
	
	@Override
	protected void performDecodeProperty(Selector selector, MapStyle mapStyle,
			FeatureSetMap filtersMap, String name, LexicalUnit value, URI sourceURI) throws ParseException
	{
		BackboneStyle bStyle = mapStyle.getGlobalStyle().getBackboneStyle();
		
		if (name.equals(COLOR_NAME))
		{
			Paint paint = PaintHandler.decode(value);
			bStyle.setPaint(paint);
		}
		else if (name.equals(THICKENESS_NAME))
		{
			if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL || value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
			{
				double thickness = value.getFloatValue();
				
				bStyle.setThickness(thickness);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid number");
			}
		}
		else if (name.equals(BACKBONE_EFFECT_NAME))
		{
			ShapeEffectRenderer shapeEffect = ShapeEffectHandler.decodeShapeEffect(value);
			if (shapeEffect != null)
			{
				bStyle.setShapeEffectRenderer(shapeEffect);
			}
			else
			{
				throw new ParseException("\"" + value + "\" is not valid for " + BACKBONE_EFFECT_NAME);
			}
		}
		else
		{
			throw new UnknownPropertyException("\"" + name + "\" is not valid for selector \"" + selector + "\"");
		}
	}
	
	@Override
	public void startSelector(Selector selector, FeatureSetMap filtersMap, MapStyle mapStyle)
	{
		// TODO Auto-generated method stub
		
	}

	public String getSelectorName()
	{
		return selectorName();
	}
	
	public static String selectorName()
	{
		return "backbone";
	}
}
