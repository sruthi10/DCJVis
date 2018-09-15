package ca.corefacility.gview.style.io.gss;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.datastyle.PlotBuilderType;
import ca.corefacility.gview.style.io.PlotIO;
import ca.corefacility.gview.style.io.gss.exceptions.MalformedDeclarationException;
import ca.corefacility.gview.style.io.gss.exceptions.MalformedPlotDataException;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPlotDataFormatException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;
import ca.corefacility.gview.utils.Util;

public class PlotDataHandler
{
	private static final String DATA_FILE_FUNCTION = "plot-file";
	private static final String PLOT_CALCULATED_FUNCTION = "plot-calculator";

	private static final String DATA_TYPE_POINT = "point";
	private static final String DATA_TYPE_RANGE = "range";
	private static final String DATA_TYPE_ANNOTATION = "annotation";

	private static final String EXTENSION = "csv";

	private static final String	GC_SKEW_FUNCTION	= "gcskew";
	private static final String	GC_CONTENT_FUNCTION	= "gccontent";
	
	// replaces all '\' with '/'
	private static String convertWindowsToUnixPath(String path)
	{
		String newPath = path;
		
		if (path.indexOf('\\') >= 0)
		{
			StringBuffer newPathBuffer = new StringBuffer(path);
			for (int oldIdx = 0; oldIdx < path.length(); oldIdx++)
			{
				if (path.charAt(oldIdx) == '\\')
				{
					newPathBuffer.setCharAt(oldIdx, '/');
				}
			}
			
			newPath = newPathBuffer.toString();
		}
		
		return newPath;
	}
	
	private static URI convertPathValueToURI(String pathValue, URI parentURI)
	{
		URI uriToRead = null;
		
		try
		{
			if (File.separatorChar == '\\' && pathValue.indexOf("\\") >= 0)
			{
				// if path contains '\' (windows path), then we must convert to a File first to get URI
				uriToRead = (new File(pathValue)).toURI();
			}
			else
			{
				uriToRead = new URI(pathValue);
			}
		}
		catch (URISyntaxException e)
		{
			try
			{
				uriToRead = new URI(parentURI.toString() +"/"+ convertWindowsToUnixPath(pathValue));
			}
			catch (URISyntaxException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if (!uriToRead.isAbsolute())
		{
			// if not absolute, assume relative to parentURI, unless if it refers to an absolute path of local file
			File fileToRead = new File(pathValue);
			if (!fileToRead.isAbsolute())
			{
				// if file not absolute, assume the name is relative to parent uri			
				try
				{
					uriToRead = new URI(parentURI.toString() +"/"+ convertWindowsToUnixPath(fileToRead.getPath()));
				}
				catch (URISyntaxException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else // else, assume refers to abolute pathname of local file
			{
				uriToRead = fileToRead.toURI();
			}
		}
		
		return uriToRead;
	}

	/**
	 * Decodes the passed LexicalUnit into a plot builder.
	 * @param parentURI  The uri to look for the file if the name in the LexicalUnit lists a relative file name.
	 * @param value  The LexicalUnit to decode.
	 * @return  A PlotBuilderType defining the actual data points to plot out.
	 * @throws MalformedDeclarationException
	 * @throws MalformedPlotDataException
	 */
	public static PlotBuilderType decode(URI parentURI, LexicalUnit value) throws IOException, ParseException
	{
		PlotBuilderType plotBuilderType = null;

		if (value == null)
			throw new NullPointerException("value is null");
		if (parentURI == null)
			throw new NullPointerException("parentURI is null");

		if (value.getLexicalUnitType() != LexicalUnit.SAC_FUNCTION)
		{
			throw new MalformedDeclarationException("for " + value + ": not of type SAC_FUNCTION");
		}
		else if( GC_SKEW_FUNCTION.equalsIgnoreCase( value.getFunctionName() ) )
		{
		    plotBuilderType = new PlotBuilderType.GCSkew();
		}
		else if( GC_CONTENT_FUNCTION.equalsIgnoreCase( value.getFunctionName() ) )
		{
		    plotBuilderType = new PlotBuilderType.GCContent();
		}
		else if (PLOT_CALCULATED_FUNCTION.equalsIgnoreCase(value.getFunctionName()))
		{
			LexicalUnit parameters = value.getParameters();

			if (parameters == null)
			{
				throw new MalformedDeclarationException("for " + value + ": no parameters");
			}
			else
			{
				if (parameters.getLexicalUnitType() != LexicalUnit.SAC_STRING_VALUE)
				{
					throw new MalformedDeclarationException("for " + value + ": value not of type SAC_STRING_VALUE");
				}
				else
				{
					String valueStr = parameters.getStringValue();
					
					if (GC_SKEW_FUNCTION.equalsIgnoreCase(valueStr))
					{
						plotBuilderType = new PlotBuilderType.GCSkew();
					}
					else if(GC_CONTENT_FUNCTION.equalsIgnoreCase(valueStr))
					{
						plotBuilderType = new PlotBuilderType.GCContent();
					}
					else
					{
						throw new UnknownPropertyException("for " + valueStr + ": unknown calculated type");
					}
				}
			}
		}
		else if (DATA_FILE_FUNCTION.equalsIgnoreCase( value.getFunctionName() ) )
		{
			LexicalUnit parameters = value.getParameters();

			if (parameters == null)
			{
				throw new MalformedDeclarationException("invalid plot declaration in \"" + value + "\"");
			}
			else if (parameters.getLexicalUnitType() != LexicalUnit.SAC_STRING_VALUE)
			{
				throw new MalformedDeclarationException("invalid plot declaration in \"" + value + "\"");
			}
			else
			{
				String dataType = parameters.getStringValue();

				parameters = parameters.getNextLexicalUnit();

				if (parameters.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA)
					throw new MalformedDeclarationException("missing \',\' in line " + value);
				else
				{
					parameters = parameters.getNextLexicalUnit();

					if (parameters.getLexicalUnitType() != LexicalUnit.SAC_STRING_VALUE)
						throw new MalformedDeclarationException("[" + parameters.getStringValue() + "] not of type SAC_STRING_VALUE");
					else
					{
						String pathValue = parameters.getStringValue();
						String extension = Util.extractExtension(pathValue);

						// read in file
						if (EXTENSION.equals(extension))
						{
							URI uriToRead = convertPathValueToURI(pathValue, parentURI);

							if (DATA_TYPE_POINT.equals(dataType))
							{
								plotBuilderType = PlotIO.readPointFile(uriToRead);
							}
							else if (DATA_TYPE_RANGE.equals(dataType))
							{
								plotBuilderType = PlotIO.readRangeFile(uriToRead);
							}
							else if (DATA_TYPE_ANNOTATION.equals(dataType))
							{
								plotBuilderType = PlotIO.readAnnotationFile(uriToRead);
							}
							else
							{
								throw new UnknownPlotDataFormatException("unknown data format " + dataType);
							}

							plotBuilderType.autoScale(true); // sets autoscaling of points
						}
						else
						{
							throw new ParseException("unknown plot file type \"" + pathValue + "\"");
						}
					}
				}
			}
		}
		else
		{
			throw new UnknownPropertyException("unknown plot file type \"" + value.getFunctionName() + "\"");
		}

		return plotBuilderType;
	}

	public static String encode(PlotBuilderType plotBuilderType)
	{
		String result = null;
		
		if(plotBuilderType instanceof PlotBuilderType.GCContent)
		{
			result = GC_CONTENT_FUNCTION + "(\"\")";
		}
		else if(plotBuilderType instanceof PlotBuilderType.GCSkew)
		{
			result = GC_SKEW_FUNCTION + "(\"\")";
		}
		else if(plotBuilderType instanceof PlotBuilderType.Points)
		{
			if(plotBuilderType.getURI() != null)
				result = DATA_FILE_FUNCTION + "(\"" + DATA_TYPE_POINT + "\"," + "\"" + plotBuilderType.getURI().getPath() + "\")";
		}
		else if(plotBuilderType instanceof PlotBuilderType.Range)
		{
			if(plotBuilderType.getURI() != null)
				result = DATA_FILE_FUNCTION + "(\"" + DATA_TYPE_RANGE + "\"," + "\"" + plotBuilderType.getURI().getPath() + "\")";
		}
		
		return result;
	}

}
