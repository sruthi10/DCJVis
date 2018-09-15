package ca.corefacility.gview.map.gui.editor;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Hashtable;

import ca.corefacility.gview.utils.FileLocationHandler;

public class StyleNameManager
{
	public static final String DEFAULT_STYLE_NAME = "untitled";
	public static final String FILE_EXTENSION_DELIM = ".";
	
	private static final Hashtable<String, Integer> styleNames = new Hashtable<String, Integer>();
		//<style name> -> <number of styles with the same name>
	
	public static String getNewStyleName()
	{
		return appendName(DEFAULT_STYLE_NAME);
	}
	
	/**
	 * Derives the name of the style based on the passed file.
	 * 
	 * @param file The file to base the name of the style on.
	 * @return The name of the style. Will be unique.
	 */
	public static String getStyleNameFromFile(URI file)
	{
		String name;
		
		if(file == null)
		{
			return getNewStyleName();
		}
		else
		{
			try
			{
				name = FileLocationHandler.getFileName(file);
			}
			catch (MalformedURLException e)
			{
				name = DEFAULT_STYLE_NAME;
			}
			catch (IllegalArgumentException e)
			{
				name = DEFAULT_STYLE_NAME;
			}
		}
		
		return appendName(name);
	}
	
	/**
	 * Appends a suffix to the name (only if required) to ensure uniqueness.
	 * @param name
	 * @return The appended name, which will be unique.
	 */
	public static String appendName(String name)
	{
		int number;
		
		if(styleNames.containsKey(name))
			//then increment the number.. append the number to the file name
		{
			number = styleNames.get(name).intValue() + 1;
			styleNames.put(name, number);	//update the number
			
			//this is handling where to put the brackets; either before or after the '.'
			// in a file extension.			
			if(name.indexOf(FILE_EXTENSION_DELIM) > 0)
			{
				name = name.substring(0, name.indexOf(FILE_EXTENSION_DELIM)) + "(" + number + ")" + FILE_EXTENSION_DELIM 
						+ name.substring(name.indexOf(FILE_EXTENSION_DELIM) + 1);
			}
			else
			{
				name = name + "(" + number + ")";
			}				
		
		}
		else
			//just throw the name in as it is and leave the name alone
		{				
			styleNames.put(name, 1);
		}
		
		return name;
	}
}
