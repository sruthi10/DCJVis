package ca.corefacility.gview.style.io.gss;


import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;

import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownFunctionException;
import ca.corefacility.gview.utils.Gradient;

public class PaintHandler
{
	private static final String RGBA_FUNCTION = "color";
	private static final String GRADIENT_FUNCTION = "gradient";
	private static final Map<String, Color> colorMap = new HashMap<String,Color>();
	
	private static final String NO_COLOR_NAME = "none";
	
	static
	{	
		colorMap.put("black", new Color(0, 0, 0));
		colorMap.put("silver", new Color(192, 192, 192));
		colorMap.put("gray", new Color(128, 128, 128));
		colorMap.put("grey", new Color(128, 128, 128));
		colorMap.put("white", new Color(255, 255, 255));
		colorMap.put("maroon", new Color(128, 0, 0));
		colorMap.put("red", new Color(255, 0, 0));
		colorMap.put("purple", new Color(128, 0, 128));
		colorMap.put("fuchsia", new Color(255, 0, 255));
		colorMap.put("green", new Color(0, 128, 0));
		colorMap.put("lime", new Color(0, 255, 0));
		colorMap.put("olive", new Color(128, 128, 0));
		colorMap.put("yellow", new Color(255, 255, 0));
		colorMap.put("orange", new Color(255, 153, 0));
		colorMap.put("navy", new Color(0, 0, 128));
		colorMap.put("blue", new Color(0, 0, 255)); 
		colorMap.put("teal", new Color(0, 128, 128));
		colorMap.put("aqua", new Color(0, 255, 255));
		
		colorMap.put("dark_green", Color.GREEN.darker().darker());
	}
	
	public static class UnknownPaintException extends Exception
	{
        private static final long serialVersionUID = 1L;

        public UnknownPaintException()
		{
			super();
			// TODO Auto-generated constructor stub
		}

		public UnknownPaintException(String message, Throwable cause)
		{
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public UnknownPaintException(String message)
		{
			super(message);
			// TODO Auto-generated constructor stub
		}

		public UnknownPaintException(Throwable cause)
		{
			super(cause);
			// TODO Auto-generated constructor stub
		}
	}
	
	/**
	 * Encodes the passed paint into a String (only handled paint type of Color for now).
	 * @param paint  The paint to encode.
	 * @return  The encoded paint.
	 * @throws UnknownPaintException If the passed paint cannot be encoded.
	 */
	public static String encode(Paint paint) throws UnknownPaintException
	{
		String encoding = null;
		
		if (paint == null)
		{
			encoding = RGBA_FUNCTION + "(\"" + NO_COLOR_NAME + "\")";
		}
		else if (paint.getClass() == Color.class)
		{
			Color color = (Color)paint;
			
			String colorConstant = encodeColorConstant(color);
			
			if (colorConstant != null)
			{
				encoding = RGBA_FUNCTION + "(\"" + colorConstant + "\")";
			}
			else
			{
				encoding = RGBA_FUNCTION + "(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha() + ")";
			}
		}
		else
		{
			// throw new exception, cannot handle
			throw new UnknownPaintException("Cannot handle Paint type " + paint.getClass());
		}
		
		return encoding;
	}
	
	private static String findColorStringFor(Color color)
	{
		for (String currColorString : colorMap.keySet())
		{
			if (color.equals(colorMap.get(currColorString)))
			{
				return currColorString;
			}
		}
		
		return null;
	}
	
	private static String encodeColorConstant(Color color)
	{
		String colorString = null;
		
		if (colorMap.containsValue(color))
		{
			colorString = findColorStringFor(color);
		}
		
		return colorString;
	}
	
	public static boolean canProcess(String functionName)
	{
		return RGBA_FUNCTION.equals(functionName);
	}
	
	private static Color getColorRGBA(LexicalUnit color) throws ParseException
	{
		final short RED = 0;
		final short GREEN = 1;
		final short BLUE = 2;
		final short ALPHA = 3;
		final short MAX = 4;
		
		int[] colorValues = new int[MAX];
		int currValue = RED;
		
		LexicalUnit currUnit = color;
		
		Color colorObj = null;
		
		while (currUnit != null && currValue < MAX)
		{			
			if (LexicalUnit.SAC_INTEGER == currUnit.getLexicalUnitType())
			{
				colorValues[currValue] = currUnit.getIntegerValue();
			}
			else if (LexicalUnit.SAC_OPERATOR_COMMA == currUnit.getLexicalUnitType())
			{
				currValue++;
			}
			
			currUnit = currUnit.getNextLexicalUnit();
		}
		
		// if we got a value for every color component (rgba)
		if (ALPHA == currValue)
		{
		    try
		    {
		        colorObj = new Color(colorValues[RED], colorValues[GREEN], colorValues[BLUE], colorValues[ALPHA]);
		    }
		    catch (IllegalArgumentException e)
		    {
		        throw new ParseException("error parsing color: " + e.getMessage());
		    }
		}
		else
		{
            throw new ParseException("error parsing color : incorrect number of parameters, must define red,green,blue,alpha values");
		}
		
		return colorObj;
	}
	
	public static Paint decode(LexicalUnit paint) throws ParseException
	{
		if (paint == null)
		{
			throw new NullPointerException("paint is null");
		}

		Paint colorObj = null;
		
		if (RGBA_FUNCTION.equals(paint.getFunctionName()))
		{
			LexicalUnit parameters = paint.getParameters();
			
			if (parameters != null)
			{
				LexicalUnit currUnit = parameters;
				
				// if we id color by a string (ex "red")
				if (currUnit.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
				{
					if (NO_COLOR_NAME.equals(currUnit.getStringValue())) // case of no color
					{
						colorObj = null;
					}
					else
					{
						colorObj = colorMap.get(currUnit.getStringValue());
						
						if (colorObj == null)
						{
							throw new ParseException("uknown color constant \"" + currUnit.getStringValue() + "\"");
						}
					}
				}
				else
				{
					colorObj = getColorRGBA(parameters);
				}
			}
			else
			{
				throw new ParseException("parameter is null");
			}
		}
		else if (GRADIENT_FUNCTION.equals(paint.getFunctionName()))
		{
			LexicalUnit parameters = paint.getParameters();

			if (parameters == null)
			{
				throw new ParseException("parameters for " + GRADIENT_FUNCTION + " are null");
			}
			else
			{
				LexicalUnit param1 = parameters;
				
				if (param1 == null || param1.getLexicalUnitType() != LexicalUnit.SAC_FUNCTION)
				{
					throw new ParseException("parameter \"" + param1 + "\" for " + GRADIENT_FUNCTION + " is not of type SAC_FUNCTION");
				}
				else
				{
					LexicalUnit param2 = param1.getNextLexicalUnit();
					
					if (param2 == null || param2.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA)
					{
						throw new ParseException("parameter for " + GRADIENT_FUNCTION + " is not of type SAC_OPERATOR_COMMA");
					}
					else
					{
						param2 = param2.getNextLexicalUnit();
						
						if (param2 == null || param2.getLexicalUnitType() != LexicalUnit.SAC_FUNCTION)
						{
							throw new ParseException("parameter \"" + param2 + "\" for " + GRADIENT_FUNCTION + " is not of type SAC_FUNCTION");
						}
						else
						{
							try
							{
								Paint p1 = PaintHandler.decode(param1);
								Paint p2 = PaintHandler.decode(param2);
								
								if (p1 instanceof Color && p2 instanceof Color)
								{
									colorObj = new Gradient((Color)p1, (Color)p2);
								}
								else
								{
									throw new ParseException("Error decoding \"" + paint + "\": parameters not of type Color");
								}
							}
							catch (ParseException e)
							{
								throw new ParseException("Error decoding \"" + paint + "\"", e);
							}
						}
					}
				}
			}
		}
		else
		{
			throw new UnknownFunctionException("unknown function name " + paint.getFunctionName());
		}
		
		return colorObj;
	}
}
