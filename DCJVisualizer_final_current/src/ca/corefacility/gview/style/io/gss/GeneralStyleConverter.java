package ca.corefacility.gview.style.io.gss;


import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SelectorFactory;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.coders.FilterCoder;
import ca.corefacility.gview.style.io.gss.coders.GSSWriter;

import com.steadystate.css.parser.SACParserCSS2;

public class GeneralStyleConverter
{
	private StyleConverter globalConverter = new StyleConverter();
	private FilterCoder filterCoder = new FilterCoder();
	private static final SelectorFactory gssSelectorFactory = new GSSSelectorFactory();
	private static final GSSErrorHandler gssErrorHandler = new GSSErrorHandler();
	
	public GeneralStyleConverter()
	{
	}
	
	/**
	 * Encodes the passed style and writes to the passed writer.
	 * @param style  The MapStyle to encode.
	 * @param writer  The writer to encode into.
	 * @throws IOException
	 */
	public void encode(MapStyle style, Writer writer) throws IOException
	{
		GSSWriter gssWriter = new GSSWriter(writer);
		
		// extracts/encodes FeatureSetMap
		FeatureSetMap featureSetMap = filterCoder.getFeatureFilters(style);
		filterCoder.encodeProperties(featureSetMap, gssWriter);
		
		// encodes other properties
		globalConverter.encodeSelector(style, featureSetMap, gssWriter);
		writer.flush();
	}
	
	public void encode(MapStyle style, String filename) throws IOException
	{
		encode(style, new FileWriter(filename));
	}
	
	/**
	 * Decodes the style information read from the passed reader into a new MapStyle.
	 * @param reader
	 * @return  The new MapStyle.
	 */
	public MapStyle decode(Reader reader, URI uri)
	{
		MapStyle mapStyle = new MapStyle();
		
		decode(mapStyle, reader, uri);
		
		return mapStyle;
	}
	
	public void decode(MapStyle mapStyle, Reader reader)
	{
		decode(mapStyle, reader, null);
	}
	
	/**
	 * Decodes the style information read from the passed reader into the passed MapStyle.
	 * 
	 * @param mapStyle  The MapStyle to modify based upon what was passed in the reader.
	 * @param reader  The reader to read the style information from.
	 * @param uri  The uri where we are reading from (null if no such value)
	 */
	public void decode(MapStyle mapStyle, Reader reader, URI uri)
	{
		Parser parser = new SACParserCSS2();
		StyleHandler styleHandler = new StyleHandler(globalConverter, gssErrorHandler, mapStyle, uri);
		
		try
		{			
			parser.setDocumentHandler(styleHandler);
			parser.setErrorHandler(gssErrorHandler);
			parser.setSelectorFactory(gssSelectorFactory);
			parser.parseStyleSheet(new InputSource(reader));
		}
		catch (CSSException e)
		{
			if (e.getException() != null)
			{
				if (e.getMessage() != null)
				{
					System.err.println(e.getException() + ": " + e.getMessage());
				}
				else
				{
					System.err.println(e);
				}
			}
			else
			{
				System.err.println(e);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
