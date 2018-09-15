package ca.corefacility.gview.data.readers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;

/**
 * Defines an interface for reading in/extracting data and style for specific file formats.
 * @author Aaron Petkau
 *
 */
public interface FileFormatReader
{	
	/**
	 * Reads GView data from the passed file.
	 * @param file  The file to read from.
	 * @return  A GViewFileData object filled out with the appropriate data.
	 * @throws IOException if some error occurred when reading from the file.
	 * @throws GViewDataParseException  If an error occurred when parsing the data.
	 */
	public GViewFileData read(File file) throws IOException, GViewDataParseException;
	
	/**
	 * Reads GView data from the passed reader.
	 * @param reader  The reader to read the data from.
	 * @return  A GViewFileData object filled with the appropriate data.
	 * @throws IOException  If an I/O error occurred.
	 * @throws GViewDataParseException  If an error occurred when parsing the data.
	 */
	public GViewFileData read(Reader reader) throws IOException, GViewDataParseException;
	
	/**
	 * Reads GView data from the passed InputStream.
	 * @param inputStream  The InputStream to read the data from.
	 * @return  A GViewFileData object filled with the appropriate data.
	 * @throws IOException  If an I/O error occurred.
	 * @throws GViewDataParseException  If an error occurred when parsing the data.
	 */
	public GViewFileData read(InputStream inputStream) throws IOException, GViewDataParseException;
	
	/**
	 * Reads in the file from the passed URL object.
	 * @param url  The location to read from.
	 * @return  A GViewFileData object containing the data.
	 * @throws IOException
	 * @throws GViewDataParseException
	 */
	public GViewFileData read(URL url) throws IOException, GViewDataParseException;
	
	/**
	 * Determines if this reader can read the passed file.
	 * @param uri  The uri for the file to test.
	 * @return  True if we can read the file, false otherwise.
	 */
	public boolean canRead(URI uri);
	
	/**
	 * Determines if this reader can read the passed input stream.
	 * @param inputStream  The inputStream to test if it can be read.
	 * @return  True if we can read the file, false otherwise.
	 */
	public boolean canRead(BufferedInputStream inputStream);
}
