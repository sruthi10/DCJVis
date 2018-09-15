package ca.corefacility.gview.style.io.gss;

import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.items.LegendAlignment;
import ca.corefacility.gview.style.items.LegendTextAlignment;

public class AlignmentHandler
{		
	public static String encode(LegendAlignment alignment)
	{
		String result = null;
		String alignmentString = null;
		
		if(alignment != null)
		{
			if (alignment == LegendAlignment.UPPER_LEFT)
			{
				alignmentString = "upper-left";
			}
			else if (alignment == LegendAlignment.UPPER_CENTER)
			{
				alignmentString = "upper-center";
			}
			else if (alignment == LegendAlignment.UPPER_RIGHT)
			{
				alignmentString = "upper-right";
			}
			else if (alignment == LegendAlignment.MIDDLE_LEFT)
			{
				alignmentString = "middle-left";
			}
			else if (alignment == LegendAlignment.MIDDLE_CENTER)
			{
				alignmentString = "middle-center";
			}
			else if (alignment == LegendAlignment.MIDDLE_RIGHT)
			{
				alignmentString = "middle-right";
			}
			else if (alignment == LegendAlignment.LOWER_LEFT)
			{
				alignmentString = "lower-left";
			}
			else if (alignment == LegendAlignment.LOWER_CENTER)
			{
				alignmentString = "lower-center";
			}
			else if (alignment == LegendAlignment.LOWER_RIGHT)
			{
				alignmentString = "lower-right";
			}
			
			if(alignmentString != null)
				result = "\"" + alignmentString + "\"";
		}
		
		return result;
	}
	
	public static LegendTextAlignment decodeTextAlignment(LexicalUnit value) throws ParseException
	{
		LegendTextAlignment alignment = null;
		
		if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT || value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
		{
			String name = value.getStringValue();
			
			if (name != null)
			{
				if (name.equals("left"))
				{
					alignment = LegendTextAlignment.LEFT;
				}
				else if (name.equals("center"))
				{
					alignment = LegendTextAlignment.CENTER;
				}
				else if (name.equals("right"))
				{
					alignment = LegendTextAlignment.RIGHT;
				}
				else
				{
					throw new ParseException("invalid alignment \"" + name + "\"");
				}
			}
			else
			{
				throw new ParseException("invalid alignment \"" + name + "\"");
			}
		}
		else
		{
			throw new ParseException("\"" + value + "\" is not valid alignment option");
		}
		
		return alignment;
	}

	public static LegendAlignment decode(LexicalUnit value) throws ParseException
	{
		LegendAlignment alignment = null;
		
		if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT || value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
		{
			String name = value.getStringValue();
			
			if (name != null)
			{
				if (name.equals("upper-left"))
				{
					alignment = LegendAlignment.UPPER_LEFT;
				}
				else if (name.equals("upper-center"))
				{
					alignment = LegendAlignment.UPPER_CENTER;
				}
				else if (name.equals("upper-right"))
				{
					alignment = LegendAlignment.UPPER_RIGHT;
				}
				else if (name.equals("middle-left"))
				{
					alignment = LegendAlignment.MIDDLE_LEFT;
				}
				else if (name.equals("middle-center"))
				{
					alignment = LegendAlignment.MIDDLE_CENTER;
				}
				else if (name.equals("middle-right"))
				{
					alignment = LegendAlignment.MIDDLE_RIGHT;
				}
				else if (name.equals("lower-left"))
				{
					alignment = LegendAlignment.LOWER_LEFT;
				}
				else if (name.equals("lower-center"))
				{
					alignment = LegendAlignment.LOWER_CENTER;
				}
				else if (name.equals("lower-right"))
				{
					alignment = LegendAlignment.LOWER_RIGHT;
				}
				else
				{
					throw new ParseException("invalid alignment \"" + name + "\"");
				}
			}
			else
			{
				throw new ParseException("invalid alignment \"" + name + "\"");
			}
		}
		else
		{
			throw new ParseException("\"" + value + "\" is not valid alignment option");
		}
		
		return alignment;
	}
}
