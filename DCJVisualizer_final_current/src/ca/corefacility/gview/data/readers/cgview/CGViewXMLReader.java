package ca.corefacility.gview.data.readers.cgview;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import ca.corefacility.gview.data.readers.AbstractFileFormatReader;
import ca.corefacility.gview.data.readers.GViewDataParseException;
import ca.corefacility.gview.data.readers.GViewFileData;
import ca.corefacility.gview.utils.Util;

/**
 * A FileFormatReader for handling CGView xml file formats.
 *
 * @author Aaron Petkau
 *
 */
public class CGViewXMLReader extends AbstractFileFormatReader
{

	/**
	 * Constructs an object to read in information from the CGView xml format.
	 */
	public CGViewXMLReader()
	{

	}

	@Override
	public GViewFileData read(File file) throws IOException, GViewDataParseException
	{
		if (file == null)
		{
			throw new NullPointerException("file is null");
		}

		return read(new FileReader(file));
	}

	@Override
	public GViewFileData read(Reader reader) throws IOException,
			GViewDataParseException
	{
		if (reader == null)
		{
			throw new IllegalArgumentException("reader is null");
		}

		GViewFileData gviewFileData = null;

		CGViewXMLHandler xmlHandler = new CGViewXMLHandler();

		gviewFileData = xmlHandler.read(reader);

		return gviewFileData;
	}

	@Override
	public boolean canRead(URI uri)
	{
		if (uri == null)
		{
			throw new IllegalArgumentException("uri is null");
		}
		
		return isXML(uri);
	}
	
	/**
	 * 
	 * @param uri
	 * @return Whether or not the provided file is a XML file.
	 */
	public static boolean isXML(URI uri)
	{
		if (uri == null)
		{
			return false;
		}
		
		String extension = Util.extractExtension(uri.getPath());

		return extension != null && extension.equals("xml");
	}

	@Override
	public GViewFileData read(InputStream inputStream) throws IOException,
			GViewDataParseException
	{
		return read(new InputStreamReader(inputStream));
	}

	@Override
	public boolean canRead(BufferedInputStream inputStream)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
