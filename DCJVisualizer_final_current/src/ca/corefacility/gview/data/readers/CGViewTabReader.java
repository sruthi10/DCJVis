package ca.corefacility.gview.data.readers;


import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.Hashtable;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.utils.Util;

/**
 * FileFormatReader for reading in cgview tab delimited files. Still incomplete. Code taken from
 * CGView tab file reader.
 * 
 * @author Aaron Petkau
 * 
 */
@SuppressWarnings( { "unchecked", "unused" })
public class CGViewTabReader extends AbstractFileFormatReader
{
	private GenomeData genomeData;

	private String filename;

	private static final Hashtable COLORS = new Hashtable();
	private static final Hashtable MAP_ITEM_COLORS = new Hashtable();
	private static final Hashtable FEATURE_COLORS = new Hashtable();
	// private static final Hashtable FEATURE_DECORATIONS_DIRECT = new Hashtable();
	// private static final Hashtable FEATURE_DECORATIONS_REVERSE = new Hashtable();
	private static final Hashtable LEGEND_ITEM_NAMES = new Hashtable();
	// private static final Hashtable DRAW_LEGEND_ITEMS = new Hashtable();

	private static final Hashtable DEFAULT_MAP_SIZES = new Hashtable();
	private static final Hashtable MIN_MAP_SIZES = new Hashtable();
	private static final Hashtable MAX_MAP_SIZES = new Hashtable();

	// private static final Hashtable DEFAULT_MAP_MODES = new Hashtable();
	// private static final Hashtable SMALL_MAP_MODES = new Hashtable();
	// private static final Hashtable LARGE_MAP_MODES = new Hashtable();

	private int lineNumber;
	private int columnNumber = -1;

	private static final String NAME_STRING = "#";
	private static final String LENGTH_STRING = "%";
	private static final String COL_NAMES_STRING = "!";
	// private static final String BLANK_STRING = "-";

	private static final int INVALID_INDEX = -1;

	private static final int NUM_REQUIRED_COLUMNS = 5;

	private int strandColumnIndex = INVALID_INDEX;
	private int slotColumnIndex = INVALID_INDEX;
	private int startColumnIndex = INVALID_INDEX;
	private int stopColumnIndex = INVALID_INDEX;
	private int opacityColumnIndex = INVALID_INDEX;
	private int thicknessColumnIndex = INVALID_INDEX;
	private int radiusColumnIndex = INVALID_INDEX;
	private int typeColumnIndex = INVALID_INDEX;
	private int labelColumnIndex = INVALID_INDEX;
	private int mouseoverColumnIndex = INVALID_INDEX;
	private int hyperlinkColumnIndex = INVALID_INDEX;

	private String title = null;
	private int length = -1;

	static
	{
		COLORS.put("black", new Color(0, 0, 0));
		COLORS.put("silver", new Color(192, 192, 192));
		COLORS.put("gray", new Color(128, 128, 128));
		COLORS.put("white", new Color(255, 255, 255));
		COLORS.put("maroon", new Color(128, 0, 0));
		COLORS.put("red", new Color(255, 0, 0));
		COLORS.put("pink", new Color(255, 153, 204));
		COLORS.put("purple", new Color(128, 0, 128));
		COLORS.put("fuchsia", new Color(255, 0, 255));
		COLORS.put("orange", new Color(255, 153, 0));
		COLORS.put("green", new Color(0, 128, 0));
		COLORS.put("spring", new Color(204, 255, 204));
		COLORS.put("lime", new Color(0, 255, 0));
		COLORS.put("olive", new Color(128, 128, 0));
		COLORS.put("yellow", new Color(255, 255, 0));
		COLORS.put("navy", new Color(0, 0, 128));
		COLORS.put("blue", new Color(0, 0, 255));
		COLORS.put("azure", new Color(51, 153, 255));
		COLORS.put("lightBlue", new Color(102, 204, 255));
		COLORS.put("teal", new Color(153, 255, 204));
		COLORS.put("aqua", new Color(0, 255, 255));

		MAP_ITEM_COLORS.put("tick", COLORS.get("black"));
		MAP_ITEM_COLORS.put("rulerFont", COLORS.get("black"));
		MAP_ITEM_COLORS.put("titleFont", COLORS.get("black"));
		MAP_ITEM_COLORS.put("messageFont", COLORS.get("black"));
		MAP_ITEM_COLORS.put("backbone", COLORS.get("gray"));
		MAP_ITEM_COLORS.put("partialTick", COLORS.get("gray"));
		MAP_ITEM_COLORS.put("zeroLine", COLORS.get("black"));
		MAP_ITEM_COLORS.put("background", COLORS.get("white"));

		FEATURE_COLORS.put("forward_gene", COLORS.get("red"));
		FEATURE_COLORS.put("reverse_gene", COLORS.get("blue"));
		FEATURE_COLORS.put("origin_of_replication", COLORS.get("black"));
		FEATURE_COLORS.put("promoter", COLORS.get("green"));
		FEATURE_COLORS.put("terminator", COLORS.get("maroon"));
		FEATURE_COLORS.put("regulatory_sequence", COLORS.get("olive"));
		FEATURE_COLORS.put("unique_restriction_site", COLORS.get("purple"));
		FEATURE_COLORS.put("restriction_site", COLORS.get("azure"));
		FEATURE_COLORS.put("open_reading_frame", COLORS.get("pink"));
		// FEATURE_COLORS.put("gene", COLORS.get("blue"));
		FEATURE_COLORS.put("predicted_gene", COLORS.get("orange"));
		FEATURE_COLORS.put("sequence_similarity", COLORS.get("silver"));
		FEATURE_COLORS.put("score", COLORS.get("fuchsia"));
		FEATURE_COLORS.put("score_II", COLORS.get("gray"));
		FEATURE_COLORS.put("primer", COLORS.get("teal"));

		// FEATURE_DECORATIONS_DIRECT.put("forward_gene", new Integer(DECORATION_CLOCKWISE_ARROW));
		// FEATURE_DECORATIONS_DIRECT.put("origin_of_replication", new
		// Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_DIRECT.put("promoter", new Integer(DECORATION_CLOCKWISE_ARROW));
		// FEATURE_DECORATIONS_DIRECT.put("terminator", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_DIRECT.put("regulatory_sequence", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_DIRECT.put("unique_restriction_site", new
		// Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_DIRECT.put("restriction_site", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_DIRECT.put("open_reading_frame", new
		// Integer(DECORATION_CLOCKWISE_ARROW));
		// //FEATURE_DECORATIONS_DIRECT.put("gene", new Integer(DECORATION_CLOCKWISE_ARROW));
		// FEATURE_DECORATIONS_DIRECT.put("predicted_gene", new
		// Integer(DECORATION_CLOCKWISE_ARROW));
		// FEATURE_DECORATIONS_DIRECT.put("sequence_similarity", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_DIRECT.put("score", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_DIRECT.put("score_II", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_DIRECT.put("primer", new Integer(DECORATION_CLOCKWISE_ARROW));
		//
		// FEATURE_DECORATIONS_REVERSE.put("reverse_gene", new
		// Integer(DECORATION_COUNTERCLOCKWISE_ARROW));
		// FEATURE_DECORATIONS_REVERSE.put("origin_of_replication", new
		// Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_REVERSE.put("promoter", new
		// Integer(DECORATION_COUNTERCLOCKWISE_ARROW));
		// FEATURE_DECORATIONS_REVERSE.put("terminator", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_REVERSE.put("regulatory_sequence", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_REVERSE.put("unique_restriction_site", new
		// Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_REVERSE.put("restriction_site", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_REVERSE.put("open_reading_frame", new
		// Integer(DECORATION_COUNTERCLOCKWISE_ARROW));
		// //FEATURE_DECORATIONS_REVERSE.put("gene", new
		// Integer(DECORATION_COUNTERCLOCKWISE_ARROW));
		// FEATURE_DECORATIONS_REVERSE.put("predicted_gene", new
		// Integer(DECORATION_COUNTERCLOCKWISE_ARROW));
		// FEATURE_DECORATIONS_REVERSE.put("sequence_similarity", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_REVERSE.put("score", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_REVERSE.put("score_II", new Integer(DECORATION_STANDARD));
		// FEATURE_DECORATIONS_REVERSE.put("primer", new
		// Integer(DECORATION_COUNTERCLOCKWISE_ARROW));

		LEGEND_ITEM_NAMES.put("forward_gene", "Forward gene");
		LEGEND_ITEM_NAMES.put("reverse_gene", "Reverse gene");
		LEGEND_ITEM_NAMES.put("origin_of_replication", "Origin of replication");
		LEGEND_ITEM_NAMES.put("promoter", "Promoter");
		LEGEND_ITEM_NAMES.put("terminator", "Terminator");
		LEGEND_ITEM_NAMES.put("regulatory_sequence", "Regulatory sequence");
		LEGEND_ITEM_NAMES.put("unique_restriction_site", "Unique restriction site");
		LEGEND_ITEM_NAMES.put("restriction_site", "Restriction site");
		LEGEND_ITEM_NAMES.put("open_reading_frame", "Open reading frame");
		// LEGEND_ITEM_NAMES.put("gene", "Gene");
		LEGEND_ITEM_NAMES.put("predicted_gene", "Predicted gene");
		LEGEND_ITEM_NAMES.put("sequence_similarity", "Sequence similarity");
		LEGEND_ITEM_NAMES.put("score", "Score");
		LEGEND_ITEM_NAMES.put("score_II", "Score II");
		LEGEND_ITEM_NAMES.put("primer", "Primer");

		DEFAULT_MAP_SIZES.put("mapWidth", Integer.valueOf(900));
		DEFAULT_MAP_SIZES.put("mapHeight", Integer.valueOf(900));
		DEFAULT_MAP_SIZES.put("labelFontSize", Integer.valueOf(10));
		DEFAULT_MAP_SIZES.put("titleFontSize", Integer.valueOf(15));
		DEFAULT_MAP_SIZES.put("legendFontSize", Integer.valueOf(13));
		DEFAULT_MAP_SIZES.put("rulerFontSize", Integer.valueOf(8));
		DEFAULT_MAP_SIZES.put("messageFontSize", Integer.valueOf(12));
		DEFAULT_MAP_SIZES.put("featureThickness", Float.valueOf(12.0f));
		DEFAULT_MAP_SIZES.put("backboneThickness", Float.valueOf(4.0f));
		DEFAULT_MAP_SIZES.put("featureSlotSpacing", Float.valueOf(2.0f));
		DEFAULT_MAP_SIZES.put("tickLength", Float.valueOf(6.0f));
		DEFAULT_MAP_SIZES.put("tickThickness", Float.valueOf(2.0f));
		DEFAULT_MAP_SIZES.put("shortTickThickness", Float.valueOf(2.0f));
		DEFAULT_MAP_SIZES.put("labelLineLength", Double.valueOf(50.0d));
		DEFAULT_MAP_SIZES.put("labelLineThickness", Float.valueOf(1.0f));
		DEFAULT_MAP_SIZES.put("arrowheadLength", Double.valueOf(5.0d));
		DEFAULT_MAP_SIZES.put("maxLabels", Integer.valueOf(5000));

		MIN_MAP_SIZES.put("mapWidth", Integer.valueOf(500));
		MIN_MAP_SIZES.put("mapHeight", Integer.valueOf(500));
		MIN_MAP_SIZES.put("labelFontSize", Integer.valueOf(1));
		MIN_MAP_SIZES.put("titleFontSize", Integer.valueOf(1));
		MIN_MAP_SIZES.put("legendFontSize", Integer.valueOf(1));
		MIN_MAP_SIZES.put("rulerFontSize", Integer.valueOf(1));
		MIN_MAP_SIZES.put("messageFontSize", Integer.valueOf(1));
		MIN_MAP_SIZES.put("featureThickness", Float.valueOf(0.5f));
		MIN_MAP_SIZES.put("backboneThickness", Float.valueOf(0.1f));
		MIN_MAP_SIZES.put("featureSlotSpacing", Float.valueOf(0.1f));
		MIN_MAP_SIZES.put("tickLength", Float.valueOf(1.0f));
		MIN_MAP_SIZES.put("tickThickness", Float.valueOf(0.5f));
		MIN_MAP_SIZES.put("shortTickThickness", Float.valueOf(0.5f));
		MIN_MAP_SIZES.put("labelLineLength", Double.valueOf(2.0f));
		MIN_MAP_SIZES.put("labelLineThickness", Float.valueOf(0.5f));
		MIN_MAP_SIZES.put("arrowheadLength", Double.valueOf(0.5d));
		MIN_MAP_SIZES.put("maxLabels", Integer.valueOf(10));

		MAX_MAP_SIZES.put("mapWidth", Integer.valueOf(30000));
		MAX_MAP_SIZES.put("mapHeight", Integer.valueOf(30000));
		MAX_MAP_SIZES.put("labelFontSize", Integer.valueOf(10));
		MAX_MAP_SIZES.put("titleFontSize", Integer.valueOf(100));
		MAX_MAP_SIZES.put("legendFontSize", Integer.valueOf(100));
		MAX_MAP_SIZES.put("rulerFontSize", Integer.valueOf(8));
		MAX_MAP_SIZES.put("messageFontSize", Integer.valueOf(100));
		MAX_MAP_SIZES.put("featureThickness", Float.valueOf(80.0f));
		MAX_MAP_SIZES.put("backboneThickness", Float.valueOf(5.0f));
		MAX_MAP_SIZES.put("featureSlotSpacing", Float.valueOf(5.0f));
		MAX_MAP_SIZES.put("tickLength", Float.valueOf(6.0f));
		MAX_MAP_SIZES.put("tickThickness", Float.valueOf(2.0f));
		MAX_MAP_SIZES.put("shortTickThickness", Float.valueOf(2.0f));
		MAX_MAP_SIZES.put("labelLineLength", Double.valueOf(80.0f));
		MAX_MAP_SIZES.put("labelLineThickness", Float.valueOf(1.0f));
		MAX_MAP_SIZES.put("arrowheadLength", Double.valueOf(18.0d));
		MAX_MAP_SIZES.put("maxLabels", Integer.valueOf(50000));

		// DEFAULT_MAP_MODES.put("giveFeaturePositions", Integer.valueOf(POSITIONS_NO_SHOW));
		// DEFAULT_MAP_MODES.put("useInnerLabels", Integer.valueOf(INNER_LABELS_AUTO));
		//	
		// SMALL_MAP_MODES.put("giveFeaturePositions", Integer.valueOf(POSITIONS_NO_SHOW));
		// SMALL_MAP_MODES.put("useInnerLabels", Integer.valueOf(INNER_LABELS_AUTO));
		//	
		// LARGE_MAP_MODES.put("giveFeaturePositions", Integer.valueOf(POSITIONS_NO_SHOW));
		// LARGE_MAP_MODES.put("useInnerLabels", Integer.valueOf(INNER_LABELS_SHOW));
	}

	public CGViewTabReader()
	{
	}

	public GViewFileData read(File file) throws IOException
	{
		if (file == null)
		{
			throw new NullPointerException("file is null");
		}

		return read(new FileReader(file));
	}

	private void parseLine(String line) throws Exception
	{
		if (line.startsWith(NAME_STRING))
		{
			title = line.substring(NAME_STRING.length());
		}
		else if (line.startsWith(LENGTH_STRING))
		{
			try
			{
				// TODO account for multiply defined lengths on different lines
				length = Integer.parseInt(line.substring(LENGTH_STRING.length()));
			}
			catch (NumberFormatException e)
			{
				throw new CGViewTabException(appendLocation("Problem parsing length value " + line.substring(LENGTH_STRING.length())));
			}
		}
		else if (line.startsWith(COL_NAMES_STRING))
		{
			setColumnIndexes(line.substring(COL_NAMES_STRING.length()));
		}
		else
		{
			try
			{
				handleTabbedData(line);
			}
			catch (Exception e)
			{
				System.err.println(e);
			}
		}
	}

	private void handleTabbedData(String line) throws Exception
	{
		String strand;
		int slot;
		int start;
		int stop;
		String type;

		String[] lineItems = line.split("\\s*\\t+\\s*");

		if (lineItems.length != columnNumber)
		{
			lineItems = line.split("(?:\\s*\\t+\\s*)|(?:\\s{2,})");
		}

		if (lineItems.length < NUM_REQUIRED_COLUMNS)
		{
			throw new Exception(appendLocation("Line could not be parsed, not enough columns defined."));
		}

		if ((strandColumnIndex < 0) || (strandColumnIndex >= lineItems.length))
		{
			throw new Exception(appendLocation("Strand column not found."));
		}
		else
		{
			strand = lineItems[strandColumnIndex];
		}

		if ((slotColumnIndex < 0) || (slotColumnIndex >= lineItems.length))
		{
			throw new Exception(appendLocation("Slot column not found."));
		}
		else
		{
			try
			{
				slot = Integer.parseInt(lineItems[slotColumnIndex]);
			}
			catch (NumberFormatException e)
			{
				throw new Exception(appendLocation("Invalid slot number " + lineItems[slotColumnIndex]));
			}
		}

		if ((startColumnIndex < 0) || (startColumnIndex >= lineItems.length))
		{
			throw new Exception(appendLocation("Start column not found."));
		}
		else
		{
			try
			{
				start = Integer.parseInt(lineItems[startColumnIndex]);
			}
			catch (NumberFormatException e)
			{
				throw new Exception(appendLocation("Invalid start number " + lineItems[startColumnIndex]));
			}
		}

		if (stopColumnIndex == INVALID_INDEX)
		{
			throw new Exception("A \"stop\" column has not been defined in the data file.");
		}

		if ((stopColumnIndex < 0) || (stopColumnIndex >= lineItems.length))
		{
			throw new Exception(appendLocation("Stop column not found."));
		}
		else
		{
			try
			{
				stop = Integer.parseInt(lineItems[stopColumnIndex]);
			}
			catch (NumberFormatException e)
			{
				throw new Exception(appendLocation("Invalid stop number " + lineItems[stopColumnIndex]));
			}
		}

		if ((typeColumnIndex < 0) || (typeColumnIndex > lineItems.length))
		{
			throw new Exception("Type column not found.");
		}
		else
		{
			type = lineItems[typeColumnIndex];
		}

		System.out.println(strand + "," + slot + "," + start + "," + stop + "," + type);
	}

	private void setColumnIndexes(String columnNames) throws Exception
	{
		String[] nameTokens = columnNames.split("(?:\\s*\\t+\\s*)|(?:\\s{2,})");

		columnNumber = nameTokens.length;
		for (int i = 0; i < nameTokens.length; i++)
		{
			String columnName = nameTokens[i].trim();
			if (columnName.equalsIgnoreCase("strand"))
			{
				strandColumnIndex = i;
			}
			else if (columnName.equalsIgnoreCase("slot"))
			{
				slotColumnIndex = i;
			}
			else if (columnName.equalsIgnoreCase("start"))
			{
				startColumnIndex = i;
			}
			else if (columnName.equalsIgnoreCase("stop"))
			{
				stopColumnIndex = i;
			}
			else if (columnName.equalsIgnoreCase("type"))
			{
				typeColumnIndex = i;
			}
			else if (columnName.equalsIgnoreCase("opacity"))
			{
				opacityColumnIndex = i;
			}
			else if (columnName.equalsIgnoreCase("thickness"))
			{
				thicknessColumnIndex = i;
			}
			else if (columnName.equalsIgnoreCase("radius"))
			{
				radiusColumnIndex = i;
			}
			else if (columnName.equalsIgnoreCase("label"))
			{
				labelColumnIndex = i;
			}
			else if (columnName.equalsIgnoreCase("mouseover"))
			{
				mouseoverColumnIndex = i;
			}
			else if (columnName.equalsIgnoreCase("hyperlink"))
			{
				hyperlinkColumnIndex = i;
			}
		}

		if (strandColumnIndex == INVALID_INDEX)
		{
			throw new Exception("A \"strand\" column has not been defined in the data file.");
		}
		if (slotColumnIndex == INVALID_INDEX)
		{
			throw new Exception("A \"slot\" column has not been defined in the data file.");
		}
		if (startColumnIndex == INVALID_INDEX)
		{
			throw new Exception("A \"start\" column has not been defined in the data file.");
		}
		if (stopColumnIndex == INVALID_INDEX)
		{
			throw new Exception("A \"stop\" column has not been defined in the data file.");
		}
		if (typeColumnIndex == INVALID_INDEX)
		{
			throw new Exception("A \"type\" column has not been defined in the data file.");
		}
	}

	private String appendLocation(String message)
	{
		return filename + ":" + lineNumber + ": " + message;
	}

	// private void printError(String message)
	// {
	// System.err.println(appendLocation(message));
	// }

	private static class CGViewTabException extends Exception
	{
		private static final long serialVersionUID = 8676745420196184510L;

		public CGViewTabException(String message)
		{
			super(message);
		}

		@Override
		public String toString()
		{
			return getMessage();
		}
	}

	public boolean canRead(URI uri)
	{
		if (uri == null)
		{
			throw new IllegalArgumentException("uri is null");
		}
		
		String extension = Util.extractExtension(uri.getPath());

		return extension != null && extension.equals("tab");
	}

	@Override
	public GViewFileData read(Reader reader) throws IOException
	{
		if (reader == null)
		{
			throw new IllegalArgumentException("Reader is null");
		}
		
		BufferedReader bufReader = new BufferedReader(reader);
		String line;

		System.out.println("Parsing tab delimited input.");

		try
		{
			line = bufReader.readLine();
			lineNumber = 1;
			while (line != null)
			{
				parseLine(line);

				line = bufReader.readLine();
				lineNumber++;
			}
		}
		catch (Exception e)
		{
			throw new IOException(e);
		}

		bufReader.close();

		if (genomeData != null)
		{
			return new GViewFileData(genomeData, null); // no MapStyle is tabbed format
		}
		else
		{
			return null;
		}
	}

	@Override
	public GViewFileData read(InputStream inputStream) throws IOException,
			GViewDataParseException
	{
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		GViewFileData data = read(inputStreamReader);
		inputStreamReader.close();
		
		return data;
	}

	@Override
	public boolean canRead(BufferedInputStream inputStream)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
