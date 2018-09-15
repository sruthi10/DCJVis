package ca.corefacility.gview.style.io.gss;


import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.io.gss.exceptions.ParseException;

public class BooleanHandler
{	
	public static String encode(boolean value)
	{
		return value ? "\"true\"" : "\"false\"";
	}
	
	public static Boolean decode(LexicalUnit value) throws ParseException
	{
		Boolean boolValue = null;
		
		if (value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
		{
			String stringValue = value.getStringValue();
			
			if (stringValue != null)
			{
				if (stringValue.equals("true"))
				{
					boolValue = true;
				}
				else if (stringValue.equals("false"))
				{
					boolValue = false;
				}
				else
				{
					throw new ParseException("invalid boolean value \"" + value + "\"");
				}
			}
			else
			{
				throw new ParseException("invalid boolean value \"" + value + "\"");
			}
		}
		else
		{
			throw new ParseException("invalid boolean value \"" + value + "\"");
		}

		return boolValue;
	}
}
