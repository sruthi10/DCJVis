package ca.corefacility.gview.data.readers;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import ca.corefacility.gview.data.readers.cgview.CGViewXMLReader;

/**
 * A factory to create GViewFileReaders from the passed input file.  Handles file format/file reading.
 * @author Aaron Petkau
 *
 */
public class GViewFileReader
{	
	/**
	 * Stores a list of registered file readers.
	 */
	private static List<FileFormatReader> fileReaders;
	
	/**
	 * Registers different file readers to use.  To register a new file reader, just add it here.
	 */
	static
	{
		fileReaders = new LinkedList<FileFormatReader>();

		fileReaders.add(new StandardSequenceFormatReader());
		fileReaders.add(new CGViewTabReader());
		fileReaders.add(new CGViewXMLReader());
		fileReaders.add(new GFF3Reader());
	}
	
	/**
	 * Reads in the passed GView file using the format defined by the particular FileFormatReader.
	 * 
	 * @param file  The file to read in.
	 * @param reader  The particular reader to use defining how to interpret the file.
	 * 
	 * @return A GViewFileData object with the extracted information from the file.
	 * 
	 * @throws IOException  If there was an error when reading the file.
	 * @throws GViewDataParseException  If a parsing error occured when reading the file.
	 */
	public static GViewFileData read(File file, FileFormatReader reader) throws IOException, GViewDataParseException
	{
		if (file == null)
		{
			throw new NullPointerException("file is null");
		}
		else if (reader == null)
		{
			throw new NullPointerException("reader is null");
		}
		else
		{
			return reader.read(file);
		}
	}
	
	/**
	 * Reads in the passed GView file using the format defined by the particular FileFormatReader.
	 * 
	 * @param filename  The name of the file to read in.
	 * @param reader  The particular reader to use defining how to interpret the file.
	 * 
	 * @return A GViewFileData object with the extracted information from the file.
	 * 
	 * @throws IOException  If there was an error when reading the file.
	 * @throws GViewDataParseException  If a parsing error occured when reading the file.
	 */
	public static GViewFileData read(String filename, FileFormatReader reader) throws IOException, GViewDataParseException
	{
		if (filename == null)
		{
			throw new NullPointerException("filename is null");
		}
		else if (reader == null)
		{
			throw new NullPointerException("reader is null");
		}
		else
		{
			return reader.read(new File(filename));
		}
	}
	
	/**
	 * Reads in the passed GView file and returns the extracted data.
	 * 
	 * @param fileLocation  The name and location of the file to read in (can be a URI, or a raw path).
	 * 
	 * @return A GViewFileData object with the extracted information from the file.
	 * 
	 * @throws IOException  If there was an error when reading the file.
	 * @throws GViewDataParseException  If a parsing error occurred when reading the file.
	 */
	public static GViewFileData read(String fileLocation) throws IOException, GViewDataParseException
	{
		GViewFileData fileData = null;
		
		if (fileLocation == null)
		{
			throw new NullPointerException("fileLocation is null");
		}
		else
		{
			URL url;
			try
			{
				url = new URL(fileLocation);
				fileData = read(url);
			}
			catch (MalformedURLException e)
			{
				fileData = read(new File(fileLocation));
			}
		}
		
		return fileData;
	}
	
	/**
	 * Reads in GView data from the passed reader.
	 * @param reader  The reader to read from.
	 * @param formatReader  A formatReader to use for the particular type of data (Genbank, CGView, etc).
	 * @return  A GViewFileData object containing the GenomeData and possibly the style.
	 * @throws IOException
	 * @throws GViewDataParseException  If a parsing error occurred when reading the data.
	 */
	public static GViewFileData read(Reader reader, FileFormatReader formatReader) throws IOException, GViewDataParseException
	{
		if (reader == null)
		{
			throw new IllegalArgumentException("reader cannot be null");
		}
		
		if (formatReader == null)
		{
			throw new IllegalArgumentException("formatReader cannot be null");
		}
		
		return formatReader.read(reader);
	}
	
	/**
	 * Reads in the passed GView file from a URL and returns the extracted data.
	 * 
	 * @param url  The file to read in.
	 * 
	 * @return A GViewFileData object with the extracted information from the file.
	 * 
	 * @throws IOException  If there was an error when reading the data.
	 * @throws GViewDataParseException  If a parsing error occurred when reading the file.
	 */
	public static GViewFileData read(URL url) throws IOException, GViewDataParseException
	{
		if (url == null)
		{
			throw new IllegalArgumentException("url is null");
		}
		else
		{
			FileFormatReader reader = getFileFormatReader(url);
			
			if (reader != null)
			{
				return reader.read(url);
			}
			else
			{
				throw new IOException("Unknown file format for " + url);
			}
		}
	}
	
	/**
	 * Reads in the passed GView file and returns the extracted data.
	 * 
	 * @param file  The file to read in.
	 * 
	 * @return A GViewFileData object with the extracted information from the file.
	 * 
	 * @throws IOException  If there was an error when reading the file.
	 * @throws GViewDataParseException  If a parsing error occurred when reading the file.
	 */
	public static GViewFileData read(File file) throws IOException, GViewDataParseException
	{
		if (file == null)
		{
			throw new NullPointerException("file is null");
		}
		else
		{
			FileFormatReader reader = getFileFormatReader(file.toURI().toURL());
			
			if (reader != null)
			{
				return reader.read(file);
			}
			else
			{
				throw new GViewDataParseException("Unknown file format for " + file);
			}
		}
	}
	
	/**
	 * Determines if GView can read the passed file.
	 * @param fileLocation  The file to test.
	 * @return  True if this file can be read, false otherwise.
	 */
	public static boolean canRead(URL fileLocation)
	{
		return getFileFormatReader(fileLocation) != null;
	}
	
	private static FileFormatReader getFileFormatReader(URL fileLocation)
	{	
		try
		{
			for (FileFormatReader reader: fileReaders)
			{
				if (reader.canRead(fileLocation.toURI()))
				{
					return reader;
				}
			}
		}
		catch (URISyntaxException e)
		{
			
		}
		
		return null;
	}
}
