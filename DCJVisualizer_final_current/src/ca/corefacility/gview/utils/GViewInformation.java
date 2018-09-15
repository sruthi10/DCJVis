package ca.corefacility.gview.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Extracts GView version information from the GView jar.
 * @author aaron
 *
 */
public class GViewInformation
{
	private static final String PROPERTIES_FILE = "/META-INF/gview.properties";
		
	private static GViewInformation instance = null;
	
	private Properties properties = null;
		
	private GViewInformation(String propertiesFile)
	{
		try
		{
			InputStream stream = GViewInformation.class.getResourceAsStream(propertiesFile);
			
			if (stream == null)
			{
				System.err.println("Couldn't find properties file " + propertiesFile + ".");
				return;
			}
			
			properties = new Properties();
			properties.load(stream);
		}
		catch (IOException e)
		{
			System.err.println("Couldn't find properties file " + propertiesFile + " " + e);
		}
	}
	
	/**
	 * @return  An instance of this GViewInformation class.
	 */
	public static GViewInformation instance()
	{
		if (instance == null)
		{
			instance = new GViewInformation(PROPERTIES_FILE);
		}
		
		return instance;
	}
	
	/**
	 * @return  The current GView version.
	 */
	public String getVersion()
	{
		return (properties != null) ? properties.getProperty("version") : "";
	}
	
	/**
	 * @return  The build date of this GView jar.
	 */
	public String getBuildDate()
	{
		return (properties != null) ? properties.getProperty("build-date") : "";
	}
	
	/**
	 * @return  The SVN revision of the GView jar.
	 */
	public String getBuildRevision()
	{
		return (properties != null) ? properties.getProperty("build-revision") : "";
	}
	
	/**
	 * Converts the given relative path to a URL of a file inside the GView jar.
	 * @param relativePath  The relative path to convert.
	 * @return  A URL within the jar for this path, or null if no such resource is found.
	 */
	public URL toInternalJarURL(String relativePath)
	{
		URL url = null;
		
		if (relativePath != null)
		{
			url = this.getClass().getResource(relativePath);
		}
		
		return url;
	}
}
