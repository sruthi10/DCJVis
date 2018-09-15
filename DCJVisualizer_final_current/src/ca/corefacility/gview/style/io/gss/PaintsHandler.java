package ca.corefacility.gview.style.io.gss;

import java.awt.Paint;
import java.util.ArrayList;

import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.io.gss.exceptions.MalformedDeclarationException;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownFunctionException;

public class PaintsHandler
{
	private static final String COLORS_FUNCTION = "colors";
	
	public static boolean canProcess(String functionName)
	{
		return COLORS_FUNCTION.equals(functionName);
	}

	public static Paint[] decode(LexicalUnit currUnit) throws ParseException
	{
		if (currUnit == null)
		{
			throw new NullPointerException("currUnit is null");
		}

		Paint[] paints = null;
		
		if (currUnit.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
		{
			if (COLORS_FUNCTION.equals(currUnit.getFunctionName()))
			{
				LexicalUnit parameters = currUnit.getParameters();
				ArrayList<Paint> colors = new ArrayList<Paint>(5);
				
				while (parameters != null && parameters.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
				{
					try
					{
						Paint paint = PaintHandler.decode(parameters);
						
						if (paint == null)
						{
							throw new MalformedDeclarationException("invalid color for " + currUnit);
						}
						else
						{
							colors.add(paint);
						}
					}
					catch (ParseException e)
					{
						throw new MalformedDeclarationException("invalid color for " + currUnit + " :",e);
					}
	
					parameters = parameters.getNextLexicalUnit();
					
					if (parameters != null && parameters.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA)
					{
						parameters = parameters.getNextLexicalUnit();
					}
					else if (parameters != null)
					{
						throw new MalformedDeclarationException("missing comma for " + currUnit);
					}
					// else, parameters == null
				}
				
				paints = new Paint[colors.size()];
				paints = colors.toArray(paints);
			}
			else
			{
				throw new UnknownFunctionException("function " + currUnit.getFunctionName() + " is unknown");
			}
		}
		else
		{
			throw new MalformedPaintsException("\"" + currUnit + "\": Not of type SAC_FUNCTION");
		}
		
		return paints;
	}
	
	private static class MalformedPaintsException extends ParseException
	{
        private static final long serialVersionUID = 1L;

        public MalformedPaintsException()
		{
			super();
			// TODO Auto-generated constructor stub
		}

		public MalformedPaintsException(String message, Throwable cause)
		{
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public MalformedPaintsException(String message)
		{
			super(message);
			// TODO Auto-generated constructor stub
		}

		public MalformedPaintsException(Throwable cause)
		{
			super(cause);
			// TODO Auto-generated constructor stub
		}
	}
}
