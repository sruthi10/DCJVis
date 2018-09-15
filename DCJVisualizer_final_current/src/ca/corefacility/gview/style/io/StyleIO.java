package ca.corefacility.gview.style.io;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.utils.FileLocationHandler;

public abstract class StyleIO {
	/**
	 * Reads in the MapStyle from the passed URL.
	 * @param url  The location of the file to read the style from.
	 * @return  The constructed MapStyle, or null if error reading from the URL.
	 */
	public MapStyle readMapStyle(URL url) throws IOException
	{		
		MapStyle mapStyle = null;
		
		if (url == null)
		{
			throw new NullPointerException("location is null");
		}
		else
		{
			Reader reader = FileLocationHandler.getReader(url);
			
			System.out.println("Reading style from " + url + " ...");
			
			String uri = null;
			try
			{
				uri = url.toURI().toString();
			}
			catch (URISyntaxException e)
			{
				uri = null;
			}
			
			mapStyle = readMapStyle(reader, uri);
		}
		
		return mapStyle;
	}
	
	/**
	 * Reads in the MapStyle from the passed file.
	 * @param fileLocation  The location of the file to read the style from.
	 * @return  The constructed MapStyle, or null if error reading in file.
	 */
	public MapStyle readMapStyle(String fileLocation) throws IOException
	{		
		MapStyle mapStyle = null;
		
		if (fileLocation == null)
		{
			throw new NullPointerException("location is null");
		}
		else
		{
			URI uri = FileLocationHandler.getURI(fileLocation);
			Reader reader = FileLocationHandler.getReader(uri);
			String uriString = uri.toString();
			
			System.out.println("Reading style from " + fileLocation + " ... ");
			
			mapStyle = readMapStyle(reader, uriString);
		}
		
		return mapStyle;
	}
	
	/**
	 * Reads in the MapStyle from the passed file.
	 * @param file  The file to read the style from.
	 * @return  The constructed MapStyle, or null if error reading from the file.
	 */
	public MapStyle readMapStyle(File file) throws IOException
	{
		Reader reader = null;
		
		System.out.println("Reading style from " + file + " ...");
		
		reader = new BufferedReader(new FileReader(file));
		
		return readMapStyle(reader, file.toURI().toString());
	}
	
	public MapStyle readMapStyle(Reader styleReader)
	{
		return readMapStyle(styleReader, null);
	}

	/**
	 * Reads the information from the passed reader to a map style.
	 * @param styleReader  The reader to use access the style information.
	 * @param uri  The URI for the document this reader is reading.  Null if no such uri.
	 * @return  A MapStyle containing the style information if successfully read, or null if not.
	 */
	public abstract MapStyle readMapStyle(Reader styleReader, String uri);

	/**
	 * Writes the passed MapStyle to the passed file.
	 * @param mapStyle  The MapStyle to write.
	 * @param filename  The file to write to.
	 */
	public void writeMapStyle(MapStyle mapStyle, String filename)
	{
		try
		{
			System.out.println("Writing style to " + filename + " ...");
			writeMapStyle(mapStyle, new PrintWriter(new BufferedWriter(new FileWriter(filename))));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Writes the passed style using the passed writer.
	 * @param mapStyle  The style to write.
	 * @param styleWriter  The writer to use.
	 */
	public abstract void writeMapStyle(MapStyle mapStyle, Writer styleWriter) throws IOException;
}