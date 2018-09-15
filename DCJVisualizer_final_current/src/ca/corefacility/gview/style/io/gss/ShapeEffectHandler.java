package ca.corefacility.gview.style.io.gss;

import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;

public class ShapeEffectHandler
{
	private static final String SHAPE_EFFECT_FUNCTION = "shape-effect";
	
	private static final String BASIC = "basic";
	private static final String STANDARD = "standard";
	private static final String SELECT = "select";
	private static final String OUTLINE = "outline";
	private static final String SHADE = "shaded";
	
	private static final ShapeEffectRenderer outlineShapeEffect = ShapeEffectRenderer.OUTLINE_RENDERER;
	private static final ShapeEffectRenderer shadeShapeEffect = ShapeEffectRenderer.SHADING_RENDERER;

	
	public static String encodeShapeEffect(ShapeEffectRenderer shapeEffect)
	{
		return SHAPE_EFFECT_FUNCTION + "(\"" + getShapeEffectString(shapeEffect) + "\")";
	}
	
	private static String getShapeEffectString(ShapeEffectRenderer shapeEffect)
	{
		if (shapeEffect.equals(ShapeEffectRenderer.BASIC_RENDERER))
		{
			return BASIC;
		}
		else if (shapeEffect.equals(ShapeEffectRenderer.STANDARD_RENDERER))
		{
			return STANDARD;
		}
		else if (shapeEffect.equals(ShapeEffectRenderer.SELECT_RENDERER))
		{
			return SELECT;
		}
		else if (shapeEffect.equals(outlineShapeEffect))
		{
			return OUTLINE;
		}
		else if (shapeEffect.equals(shadeShapeEffect))
		{
			return SHADE;
		}
		
		return null;
	}
	
	private static ShapeEffectRenderer getShapeEffectFromString(String shapeEffect)
	{
		if (shapeEffect.equals(BASIC))
		{
			return ShapeEffectRenderer.BASIC_RENDERER;
		}
		else if (shapeEffect.equals(STANDARD))
		{
			return ShapeEffectRenderer.STANDARD_RENDERER;
		}
		else if (shapeEffect.equals(SELECT))
		{
			return ShapeEffectRenderer.SELECT_RENDERER;
		}
		else if (shapeEffect.equals(OUTLINE))
		{
			return outlineShapeEffect;
		}
		else if (shapeEffect.equals(SHADE))
		{
			return shadeShapeEffect;
		}
		
		return null;
	}
	
	public static ShapeEffectRenderer decodeShapeEffect(LexicalUnit shapeEffect) throws ParseException
	{
		if (shapeEffect == null)
		{
			throw new NullPointerException("shapeEffect is null");
		}

		ShapeEffectRenderer shapeEffectObj = null;
		
		if (SHAPE_EFFECT_FUNCTION.equals(shapeEffect.getFunctionName()))
		{
			LexicalUnit parameters = shapeEffect.getParameters();
			
			if (parameters != null)
			{
				if (parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
				{
					shapeEffectObj = getShapeEffectFromString(parameters.getStringValue());
				}
				else
				{
					throw new ParseException("invalid shape effect in \"" + shapeEffect + "\"");
				}
			}
			else
			{
				throw new ParseException("invalid shape effect in \"" + shapeEffect + "\"");
			}
		}
		else
		{
			throw new ParseException("invalid shape effect in \"" + shapeEffect + "\"");
		}
		
		return shapeEffectObj;
	}
}
