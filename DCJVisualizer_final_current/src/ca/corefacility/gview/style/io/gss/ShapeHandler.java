package ca.corefacility.gview.style.io.gss;


import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;

public class ShapeHandler
{
	private static final String SHAPE_FUNCTION = "shape";

	private static final String NO_ARROW_NAME = "block";
	private static final String CLOCKWISE_ARROW_NAME = "clockwise-arrow";
	private static final String CLOCKWISE_ARROW_2_NAME = "clockwise-arrow2";
	private static final String COUNTERCLOCKWISE_ARROW_NAME = "counterclockwise-arrow";
	private static final String COUNTERCLOCKWISE_ARROW_2_NAME = "counterclockwise-arrow2";

	public static String encodeShape(FeatureShapeRealizer shapeRealizer)
	{
		return SHAPE_FUNCTION + "(\"" + getShapeString(shapeRealizer) + "\")";
	}

	private static String getShapeString(FeatureShapeRealizer shapeRealizer)
	{
		if (shapeRealizer.equals(FeatureShapeRealizer.NO_ARROW))
			return NO_ARROW_NAME;
		else if (shapeRealizer.equals(FeatureShapeRealizer.CLOCKWISE_ARROW))
			return CLOCKWISE_ARROW_NAME;
		else if (shapeRealizer.equals(FeatureShapeRealizer.CLOCKWISE_ARROW2))
			return CLOCKWISE_ARROW_2_NAME;
		else if (shapeRealizer.equals(FeatureShapeRealizer.COUNTERCLOCKWISE_ARROW))
			return COUNTERCLOCKWISE_ARROW_NAME;
		else if (shapeRealizer.equals(FeatureShapeRealizer.COUNTERCLOCKWISE_ARROW2))
			return COUNTERCLOCKWISE_ARROW_2_NAME;

		return null;
	}

	private static FeatureShapeRealizer getShapeFromString(String shape)
	{
		if (shape.equals(NO_ARROW_NAME))
			return FeatureShapeRealizer.NO_ARROW;
		else if (shape.equals(CLOCKWISE_ARROW_NAME))
			return FeatureShapeRealizer.CLOCKWISE_ARROW;
		else if (shape.equals(CLOCKWISE_ARROW_2_NAME))
			return FeatureShapeRealizer.CLOCKWISE_ARROW2;
		else if (shape.equals(COUNTERCLOCKWISE_ARROW_NAME))
			return FeatureShapeRealizer.COUNTERCLOCKWISE_ARROW;
		else if (shape.equals(COUNTERCLOCKWISE_ARROW_2_NAME))
			return FeatureShapeRealizer.COUNTERCLOCKWISE_ARROW2;

		return null;
	}

	public static FeatureShapeRealizer decodeShape(LexicalUnit shapeRealizer) throws ParseException
	{
		if (shapeRealizer == null)
			throw new NullPointerException("shapeRealizer is null");

		FeatureShapeRealizer shapeRealizerObj = null;

		if (SHAPE_FUNCTION.equals(shapeRealizer.getFunctionName()))
		{
			LexicalUnit parameters = shapeRealizer.getParameters();

			if (parameters != null)
			{
				if (parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
				{
					shapeRealizerObj = getShapeFromString(parameters.getStringValue());
					if (shapeRealizerObj == null)
					{
						throw new ParseException("unknown shape \"" + parameters.getStringValue() + "\"");
					}
				}
				else
				{
					throw new ParseException("invalid shape in \"" + shapeRealizer + "\"");
				}
			}
			else
			{
				throw new ParseException("invalid shape in \"" + shapeRealizer + "\"");
			}
		}
		else
		{
			throw new ParseException("invalid shape in \"" + shapeRealizer + "\"");
		}

		return shapeRealizerObj;
	}
}
