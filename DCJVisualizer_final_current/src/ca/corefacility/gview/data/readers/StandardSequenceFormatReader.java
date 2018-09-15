package ca.corefacility.gview.data.readers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.biojava.bio.BioException;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.io.FastaFormat;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;
import org.biojavax.bio.seq.io.RichSequenceFormat;
import org.biojavax.bio.seq.io.RichStreamReader;
import org.biojavax.bio.seq.io.UniProtFormat;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.data.GenomeDataFactory;
import ca.corefacility.gview.utils.FileLocationHandler;

public class StandardSequenceFormatReader implements FileFormatReader
{
	private static List<Class<? extends RichSequenceFormat>> standardFormats;
	private static RichSequenceBuilderFactory defaultFactory = RichSequenceBuilderFactory.FACTORY;
	
	static
	{
		standardFormats = new LinkedList<Class<? extends RichSequenceFormat>>();
		standardFormats.add(EMBLFormatModified.class);
		standardFormats.add(FastaFormat.class);
		standardFormats.add(GenbankFormatModified.class);
		standardFormats.add(UniProtFormat.class);
	}

	@Override
	public GViewFileData read(File file) throws IOException,
			GViewDataParseException
	{
		FileInputStream fileInputStream = new FileInputStream(file);
		GViewFileData data;
		
		try
		{
			data = read(fileInputStream);
			fileInputStream.close();
		}
		catch (IOException e)
		{
			fileInputStream.close();
			throw new IOException("For file " + file.getAbsolutePath(), e);
		}
		catch (GViewDataParseException e)
		{
			fileInputStream.close();
			throw new GViewDataParseException("For file " + file.getAbsolutePath(), e);
		}
		
		return data;
	}
	
	private RichSequenceFormat getFormat(BufferedInputStream iStream) throws IOException
	{
		RichSequenceFormat format = null;
		
		try
		{			
			for (Class<? extends RichSequenceFormat> c : standardFormats)
			{
				format = c.newInstance();
				
				if (format.canRead(iStream))
				{
					return format;
				}
			}
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	private RichStreamReader getStreamReader(BufferedInputStream iStream) throws IOException
	{
		RichSequenceFormat format = getFormat(iStream);
		RichStreamReader r = null;
		
		if (format != null)
		{
			SymbolTokenization token = format.guessSymbolTokenization(iStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
			
			r = new RichStreamReader(br, format, token, defaultFactory, null);
		}
		
		return r;
	}

	@Override
	public GViewFileData read(Reader reader) throws IOException,
			GViewDataParseException
	{
		 // TODO fix this by somehow linking Reader to InputStream so we can use biojava file format readers
		throw new IOException("Cannot read from class Reader in StandardSequenceFormatReader");
	}

	@Override
	public boolean canRead(URI uri)
	{
		InputStream iStream;
		boolean canRead = false;
		
		try
		{
			iStream = FileLocationHandler.getInputStream(uri.toURL());
			
			BufferedInputStream bis = new BufferedInputStream(iStream);
			canRead = canRead(bis);
			bis.close();
		}
		catch (IOException e)
		{
			return false;
		}
		
		return canRead;
	}

	@Override
	public GViewFileData read(InputStream inputStream) throws IOException,
			GViewDataParseException
	{
		BufferedInputStream bInputStream = new BufferedInputStream(inputStream);
		GViewFileData gviewFileData = null;
		
		try
		{
			RichStreamReader r = getStreamReader(bInputStream);
			
			if (r == null)
			{
				bInputStream.close();
				throw new GViewDataParseException("No RichSequenceFormat class registered for this input stream " + bInputStream);
			}
			
			RichSequence seq = r.nextRichSequence();
			
			GenomeData data = GenomeDataFactory.createGenomeData(seq);
			gviewFileData = new GViewFileData(data, null);
			
			bInputStream.close();
		}
		catch (BioException b)
		{
			throw new GViewDataParseException(b);
		}
		catch (IllegalArgumentException e)
		{
			throw new GViewDataParseException(e);
		}
		
		return gviewFileData;
	}

	@Override
	public GViewFileData read(URL url) throws IOException,
			GViewDataParseException
	{
		GViewFileData data = null;
		
		if (url == null)
		{
			throw new IllegalArgumentException("passed url is null");
		}
		else
		{
			InputStream inputStream = FileLocationHandler.getInputStream(url);
			
			try
			{
				data = read(inputStream);
				inputStream.close();
				
				return data;
			}
			catch (IOException e)
			{
				inputStream.close();
				throw new IOException("For file " + url, e);
			}
			catch (GViewDataParseException e)
			{
				inputStream.close();
				throw new GViewDataParseException("For file " + url, e);
			}
		}
	}

	@Override
	public boolean canRead(BufferedInputStream inputStream)
	{
		RichSequenceFormat format = null;
		try
		{
			format = getFormat(inputStream);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return format != null;
	}
}
