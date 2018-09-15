package ca.corefacility.gview.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * A class which helps in locating files to read in for GView
 * @author aaron
 *
 */
public class FileLocationHandler
{
	private static final int SCHEME_INDEX = 0;
	private static final int SSP_INDEX = 1;
	private static final int FRAGMENT_INDEX = 2;
	
	private static boolean isValidScheme(String scheme)
	{
		final String[] valid = {"http", "https", "ftp", "file"};
		
		for (String s : valid)
		{
			if (s.equals(scheme))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Splits the given uri string into the 3 component parts.
	 * @param uriString  The uri string to fragment.
	 * @return  A string[] containing the component parts or null if error.
	 * A part may be null if it is missing.
	 */
	private static String[] split(String uriString)
	{
		String scheme = null;
		String ssp = null;
		
		String[] parts = null;
		
		if (uriString != null)
		{
			int schemeIndex;
			
			schemeIndex = uriString.indexOf(':');
			
			if (schemeIndex != -1)
			{
				scheme = uriString.substring(0, schemeIndex);
				
				if (isValidScheme(scheme))
				{
					// no fragment
					ssp = uriString.substring(schemeIndex + 1);
					
					parts = new String[] {scheme, ssp, null};
				}
				else
				{
					// assume only filename
					parts = new String[] {null, uriString, null};
				}
			}
			else
			{
				// assume only filename
				parts = new String[] {null, uriString, null};
			}
		}
		
		return parts;
	}
	
	public static Reader getReader(URI uri) throws IOException
	{
		if (uri == null)
		{
			throw new IllegalArgumentException("uri is null");
		}
		
		Reader reader = null;
		
		try
		{
			reader = getReader(uri.toURL());
		}
		catch (MalformedURLException e)
		{
			throw new IOException(e);
		}
		
		return reader;
	}
	
	public static String getFileName(URI uri) throws MalformedURLException
	{
		if (uri == null)
		{
			throw new IllegalArgumentException("URI is null");
		}
		
		URL url = uri.toURL();
		
		if (url != null)
		{
			String path = url.getPath();
			
			if (path != null)
			{
				String[] tokens = path.split("/");
				if (tokens != null)
				{
					return tokens[tokens.length - 1];
				}
			}
		}
		
		return null;
	}
	
	private static URI getURIFromFile(String fileStr) throws IOException
	{
		URI uri = null;
		
		if (fileStr != null)
		{
			// attempt to check if the file is located in the jar or not
			File file = new File( fileStr );
			
			if( !file.canRead() )
			{
				if (resourceExists(fileStr))
				{
					File tempFile = createTempFileFromClassLoaderResource( fileStr );
					uri = tempFile.toURI();
				}
				else
				{
					throw new FileNotFoundException("file located at " + fileStr + " not found");
				}
			}
			else
			{
				uri = file.toURI();
			}
		}
		
		return uri;
	}
	
	public static URI getURI(String location) throws IOException
	{
		URI uri = null;
		
		if (location == null)
		{
			throw new NullPointerException("location is null");
		}
		else
		{
			String[] splitURI = split(location);
			
			if (splitURI != null && splitURI.length == FRAGMENT_INDEX + 1)
			{
				if (splitURI[SCHEME_INDEX] == null)
				{
					if (splitURI[SSP_INDEX] != null)
					{
						uri = getURIFromFile(location);
					}
					else
					{
						throw new FileNotFoundException("location " + location + " not found");
					}
				}
				else if (splitURI[SSP_INDEX] == null)
				{
					throw new FileNotFoundException("location " + location + " not found");
				}
				else
				{
					try
					{
						if (splitURI[FRAGMENT_INDEX] != null)
						{
							uri = new URI(splitURI[SCHEME_INDEX], splitURI[SSP_INDEX], splitURI[FRAGMENT_INDEX]);
						}
						else
						{
							uri = new URI(splitURI[SCHEME_INDEX], splitURI[SSP_INDEX], null);
						}
					}
					catch (URISyntaxException e)
					{
						throw new FileNotFoundException(location + " not found due to: " + uri);
					}
				}
			}
		}
		
		return uri;
	}
	
	public static Reader getReader(String location) throws IOException
	{
		Reader reader = null;
		
		URI uri = getURI(location);
		reader = getReader(uri);
		
		return reader;
	}
	
	public static InputStream getInputStream(String location) throws IOException
	{	
		URI uri = getURI(location);
		
		return getInputStream(uri.toURL());
	}
	
	public static Reader getReader(URL url) throws IOException
	{
		return new InputStreamReader(getInputStream(url));
	}
	
	public static InputStream getInputStream(URL url) throws IOException
	{
		if (url == null)
		{
			throw new IllegalArgumentException("url is null");
		}
		
		InputStream iStream = null;
		URLConnection connection = null;
						
		ProxySelector proxySelector = ProxySelector.getDefault();
		
		try
		{
			if (!"file".equals(url.getProtocol()))
			{
				List<Proxy> proxies = proxySelector.select(url.toURI());
				
				if (proxies != null && proxies.size() == 1 && Proxy.NO_PROXY.equals(proxies.get(0)))
				{
					// no proxy
					connection = url.openConnection();
				}
				else
				{
					for (Proxy p : proxies)
					{
						if (p.type().equals(Proxy.Type.HTTP))
						{
							connection = url.openConnection(p);
							break;
						}
						else if (p.type().equals(Proxy.Type.SOCKS))
						{
							connection = url.openConnection(p);
							break;
						}
					}
				}
			}
			else
			{
				connection = url.openConnection();
			}
			
			iStream = connection.getInputStream();
		}
		catch (URISyntaxException e)
		{
			throw new IOException(e);
		}

		return iStream;
	}
	
	private static boolean resourceExists(String resource)
	{
		return (FileLocationHandler.class.getClassLoader().getResourceAsStream( resource )
				!= null);
	}
	
	/**
	 * This method will attempt to find the given resource by using the classloader.
	 * This will enable resources embedded in the jar file being executed to be located and used.
	 * Best used to load default files
	 * @param resource  The resource to load within the jar file.
	 * @return  A temporary file containing this resource.
	 * @throws IOException
	 */
	private static File createTempFileFromClassLoaderResource( String resource ) throws IOException
	{
		InputStream stream = FileLocationHandler.class.getClassLoader().getResourceAsStream( resource );
		if( stream != null )
		{
			String end = "";
			if (resource.lastIndexOf( "." ) != -1)
			{
				end = resource.substring(resource.lastIndexOf( "." ));
			}
			
			File tempfile = File.createTempFile( "gviewtemp", end );
			tempfile.deleteOnExit();

			InputStreamReader reader = new InputStreamReader( stream );
			FileWriter writer = new FileWriter( tempfile );

			int read = reader.read();
			while( read != -1 )
			{
				writer.write( read );
				read = reader.read();
			}

			reader.close();
			writer.close();
			return tempfile;
		}
		else
		{
			throw new FileNotFoundException("resource " + resource + " could not be found");
		}
	}
}
