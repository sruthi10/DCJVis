package ca.corefacility.gview.style.io;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;

import au.com.bytecode.opencsv.CSVReader;
import ca.corefacility.gview.style.datastyle.PlotBuilderType;
import ca.corefacility.gview.style.io.gss.exceptions.MalformedPlotDataException;
import ca.corefacility.gview.utils.FileLocationHandler;

public class PlotIO
{
    /**
     * Parses the file defined by the given URI as a point file, and returns a plot builder type.
     * @param uri  The URI to process.
     * @return  The corresponding PlotBuilderType, or null if an error.
     * @throws IOException
     * @throws MalformedPlotDataException
     */
	public static PlotBuilderType readPointFile(URI uri) throws IOException, MalformedPlotDataException
	{
		final int BASE_INDEX = 0;
		final int VALUE_INDEX = 1;
		final int NUMBER_ELEMENTS = 2;

		PlotBuilderType.Points plotBuilderType = new PlotBuilderType.Points();

		Reader urireader = FileLocationHandler.getReader(uri);
		CSVReader reader = new CSVReader(urireader);
		String[] currLine = null;

		try
		{
			while ((currLine = reader.readNext()) != null)
			{
				if (currLine.length == NUMBER_ELEMENTS)
				{
					plotBuilderType.addPoint(Integer.parseInt(currLine[BASE_INDEX]),
							Double.parseDouble(currLine[VALUE_INDEX]));
				}
				else
				{
					throw new MalformedPlotDataException("incorrect number of columns per line in " + uri);
				}
			}
		}
		catch (NumberFormatException e)
		{
			throw new MalformedPlotDataException("in file " + uri, e);
		}
		
		plotBuilderType.setURI(uri);

		return plotBuilderType;
	}

    /**
     * Parses the file defined by the given URI as a range file, and returns a plot builder type.
     * @param uri  The URI to process.
     * @return  The corresponding PlotBuilderType, or null if an error.
     * @throws IOException
     * @throws MalformedPlotDataException
     */
	public static PlotBuilderType readRangeFile(URI uri) throws IOException, MalformedPlotDataException
	{
		final int BASE_START_INDEX = 0;
		final int BASE_END_INDEX = 1;
		final int VALUE_INDEX = 2;
		final int NUMBER_ELEMENTS = 3;

		Reader urireader = FileLocationHandler.getReader(uri);
		CSVReader reader = new CSVReader(urireader);
		String[] currLine = null;

		PlotBuilderType.Range plotBuilderType = new PlotBuilderType.Range();

		try
		{
			while ((currLine = reader.readNext()) != null)
			{
				if (currLine.length == NUMBER_ELEMENTS)
				{
				    plotBuilderType.addRange(Integer.parseInt(currLine[BASE_START_INDEX]),
							Integer.parseInt(currLine[BASE_END_INDEX]),
							Double.parseDouble(currLine[VALUE_INDEX]));
				}
				else
				{
					throw new MalformedPlotDataException("incorrect number of columns per line in " + uri);
				}
			}
		}
		catch (NumberFormatException e)
		{
			throw new MalformedPlotDataException("in file " + uri, e);
		}
		
		plotBuilderType.setURI(uri);

		return plotBuilderType;
	}

    /**
     * Parses the file defined by the given URI as an annotation file, and returns a plot builder type.
     * @param uri  The URI to process.
     * @return  The corresponding PlotBuilderType, or null if an error.
     * @throws IOException
     * @throws MalformedPlotDataException
     */
	public static PlotBuilderType readAnnotationFile(URI uri) throws IOException, MalformedPlotDataException
	{
		final int ANNOTATION_TYPE_INDEX = 0;
		final int ANNOTATION_VALUE_INDEX = 1;
		final int VALUE_INDEX = 2;
		final int NUMBER_ELEMENTS = 3;

		Reader urireader = FileLocationHandler.getReader(uri);
		CSVReader reader = new CSVReader(urireader);
		String[] currLine = null;

		PlotBuilderType.Annotation plotBuilderType = new PlotBuilderType.Annotation();

		try
		{
			while ((currLine = reader.readNext()) != null)
			{
				if (currLine.length == NUMBER_ELEMENTS)
				{
				    plotBuilderType.addAnnotationValue(currLine[ANNOTATION_TYPE_INDEX],
							currLine[ANNOTATION_VALUE_INDEX], Double.valueOf(currLine[VALUE_INDEX]));
				}
				else
					throw new MalformedPlotDataException("incorrect number of columns per line in " + uri);
			}
		}
		catch (NumberFormatException e)
		{
			throw new MalformedPlotDataException("in file " + uri, e);
		}
		
		plotBuilderType.setURI(uri);

		return plotBuilderType;
	}
}
