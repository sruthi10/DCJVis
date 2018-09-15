package ca.corefacility.gview.style.io;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.gss.GeneralStyleConverter;

public class StyleIOGSS extends StyleIO
{
	private GeneralStyleConverter gssConverter;
	
	public StyleIOGSS()
	{
		gssConverter = new GeneralStyleConverter();
	}

	@Override
	public MapStyle readMapStyle(Reader styleReader, String uri)
	{
		URI uriObj = null;
		
		if (uri != null)
		{
			try
			{
				uriObj = new URI(uri);
			}
			catch (URISyntaxException e)
			{
				System.err.println(e);
				uriObj = null;
			}
		}
		
		return gssConverter.decode(styleReader, uriObj);
	}
	
	public void readMapStyle(MapStyle mapStyle, Reader styleReader, String uri)
	{
		URI uriObj = null;
		
		if (uri != null)
		{
			try
			{
				uriObj = new URI(uri);
			}
			catch (URISyntaxException e)
			{
				System.err.println(e);
				uriObj = null;
			}
		}
		
		gssConverter.decode(mapStyle, styleReader, uriObj);
	}

	@Override
	public void writeMapStyle(MapStyle mapStyle, Writer styleWriter) throws IOException
	{
		gssConverter.encode(mapStyle, styleWriter);
	}
}
