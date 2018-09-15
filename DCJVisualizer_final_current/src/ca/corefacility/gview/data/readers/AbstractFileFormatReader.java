package ca.corefacility.gview.data.readers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public abstract class AbstractFileFormatReader implements FileFormatReader
{
	public AbstractFileFormatReader()
	{
		super();
	}
	
	@Override
	public GViewFileData read(URL url) throws IOException, GViewDataParseException
	{
		if (url == null)
		{
			throw new IllegalArgumentException("url is null");
		}
		
		GViewFileData fileData = null;
		
		InputStream iStream = url.openStream();
		InputStreamReader iStreamReader = new InputStreamReader(iStream);
			
		fileData = read(iStreamReader);
		
		iStreamReader.close();
		
		return fileData;
	}
}