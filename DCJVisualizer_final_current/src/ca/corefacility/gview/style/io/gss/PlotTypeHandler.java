package ca.corefacility.gview.style.io.gss;

import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.datastyle.PlotDrawerType;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPlotTypeException;

public class PlotTypeHandler
{	
	private static final String LINE_TYPE_NAME = "line";
	private static final String BAR_TYPE_NAME = "bar";
	private static final String	CENTER_TYPE_NAME = "center";

	/**
	 * Attempts to decode the given unit into a PlotDrawerType.
	 * @param currUnit  The unit to decode.
	 * @return  The corresponding PlotDrawerType
	 * @throws UnknownPlotTypeException  If there was an error decoding the plot type.
	 */
	public static PlotDrawerType decode(LexicalUnit currUnit) throws UnknownPlotTypeException
	{
		PlotDrawerType plotDrawerType = null;

		if (currUnit != null)
		{
			if (currUnit.getLexicalUnitType() == LexicalUnit.SAC_IDENT || currUnit.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
			{
				String value = currUnit.getStringValue();

				if ( LINE_TYPE_NAME.equalsIgnoreCase( value ) )
				{
					plotDrawerType = new PlotDrawerType.Line();
				}
				else if ( BAR_TYPE_NAME.equalsIgnoreCase( value ) )
				{
				    plotDrawerType = new PlotDrawerType.Bar();
				}
				else if ( CENTER_TYPE_NAME.equalsIgnoreCase( value ) )
				{
				    plotDrawerType = new PlotDrawerType.Center();
				}
				else
				{
					throw new UnknownPlotTypeException("unkown plot " + value);
				}
			}
			else
			{
				throw new UnknownPlotTypeException("unknown plot " + currUnit);
			}
		}
		else
		{
			throw new UnknownPlotTypeException("unknown plot " + currUnit);
		}

		return plotDrawerType;
	}

	/**
	 * Encodes the given plot drawer type to a GSS string.
	 * @param plotDrawerType  The specific plot drawer type to encode.
	 * @return  The encoded string, or null if plot drawer type is invalid.
	 */
	public static String encode(PlotDrawerType plotDrawerType)
	{
		String result = null;
		
		if(plotDrawerType instanceof PlotDrawerType.Bar)
		{
			result = BAR_TYPE_NAME;
		}
		else if(plotDrawerType instanceof PlotDrawerType.Center)
		{
			result = CENTER_TYPE_NAME;
		}
		else if(plotDrawerType instanceof PlotDrawerType.Line)
		{
			result = LINE_TYPE_NAME;
		}
		
		return result;
	}
}
