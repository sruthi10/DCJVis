package ca.corefacility.gview.style.io.gss.coders;


import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.datastyle.DataStyle;
import ca.corefacility.gview.style.datastyle.PlotBuilderType;
import ca.corefacility.gview.style.datastyle.PlotDrawerType;
import ca.corefacility.gview.style.datastyle.PlotStyle;
import ca.corefacility.gview.style.io.gss.PaintHandler;
import ca.corefacility.gview.style.io.gss.PaintsHandler;
import ca.corefacility.gview.style.io.gss.PlotDataHandler;
import ca.corefacility.gview.style.io.gss.PlotTypeHandler;
import ca.corefacility.gview.style.io.gss.exceptions.MalformedDeclarationException;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPlotTypeException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;

public class PlotCoder
{
	private static final String PAINT = "color";
	
	private static final String PAINTS = "colors"; 

	private static final String DATA = "data";

	private static final String TYPE = "type";

	private static final String GRID_LINES = "grid-lines";

	private static final String GRID_COLOR = "grid-color";


	public String getSelectorName()
	{
		return selectorName();
	}

	/**
	 * Decodes the passed name, value into the passed PlotStyle.
	 * @param plotStyle
	 * @param name
	 * @param value
	 * @param sourceURI
	 * @throws IOException
	 * @throws UnknownPlotTypeException
	 */
	public void decodeProperty(PlotStyle plotStyle,
			String name, LexicalUnit value, URI sourceURI) throws ParseException, IOException
	{
		if (plotStyle != null)
		{
			if (PAINT.equals(name))
			{
				Paint paint = PaintHandler.decode(value);
				plotStyle.addPaint( paint );
			}
			else if (PAINTS.equals(name))
			{
				Paint[] paint = PaintsHandler.decode(value);
				plotStyle.setPaint( paint );
			}
			else if (DATA.equals(name))
			{
				URI parentURI = null;

				if (sourceURI == null)
				{
					parentURI = (new File(".")).toURI();
				}
				else // set parentURI to the parent path of sourceURI
				{
					String uriString = sourceURI.toString();
					
					String parentString;
					int lastIndexOf = uriString.lastIndexOf('/');
					
					// if parentString contains a '/' but does not end with a '/'
					if (lastIndexOf > 0 && lastIndexOf < (uriString.length()-1))
					{
						parentString = uriString.substring(0, lastIndexOf);
						try
						{
							parentURI = new URI(parentString);
						}
						catch (URISyntaxException e)
						{
							throw new ParseException(e);
						}
					}
					else
					{
						parentString = uriString;
					}
				}

				if (parentURI == null)
				{
					throw new ParseException("could not understand " + parentURI);
				}
				
				PlotBuilderType plotBuilderType = PlotDataHandler.decode(parentURI, value);

				plotStyle.setPlotBuilderType(plotBuilderType);
			}
			else if (TYPE.equals(name))
			{
				PlotDrawerType plotDrawerType = PlotTypeHandler.decode(value);

				plotStyle.setPlotDrawerType(plotDrawerType);
			}
			else if (GRID_LINES.equals(name))
			{
				if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
				{
					int lines = value.getIntegerValue();

					if (lines < 0)
						throw new MalformedDeclarationException(GRID_LINES + " value must be non-negative");
					else
					{
						plotStyle.setGridLines(lines);
					}
				}
				else
					throw new MalformedDeclarationException(GRID_LINES + " value must be an integer");
			}
			else if (name.equals(GRID_COLOR))
			{
				try
				{
					Paint paint = PaintHandler.decode(value);
					plotStyle.setGridPaint(paint);
				}
				catch (Exception e)
				{
					throw new MalformedDeclarationException(e);
				}
			}
			else
			{
				throw new UnknownPropertyException("\"" + name + "\" is not valid for selector \"" + selectorName()+"\"");
			}
		}
		else
			throw new NullPointerException("plotStyle is null");
	}

	public static String selectorName()
	{
		return "plot";
	}

	/**
	 * Encodes the passed style into the passed writer
	 * @param plotStyle
	 * @param selectorBase
	 * @param writer
	 */
	public void encodeStyle(PlotStyle plotStyle,
			String selectorBase, GSSWriter writer)
	{
		if (plotStyle == null)
			throw new NullPointerException("plotStyle is null");

		String labelSelectorName = writer.getDescendantName(selectorBase, getSelectorName());

		writer.startSelector(labelSelectorName);

		writer.writePropertyPaint(PAINT, plotStyle.getPaint()[0]);
		
		//secondary paint
		if(plotStyle.getPaint().length >= 2)
				writer.writePropertyPaint(PAINT, plotStyle.getPaint()[1]);
		
		writer.writeProperty(DATA, PlotDataHandler.encode(plotStyle.getPlotBuilderType()));
		writer.writeProperty(TYPE, PlotTypeHandler.encode(plotStyle.getPlotDrawerType()));
		writer.writeProperty(GRID_LINES, Integer.toString(plotStyle.getGridLines()));
		writer.writePropertyPaint(GRID_COLOR, plotStyle.getGridPaint());			

		writer.endSelector();
	}

	public void startSelector(DescendantSelector selector, DataStyle dataStyle)
	{
		// TODO Auto-generated method stub

	}
}
