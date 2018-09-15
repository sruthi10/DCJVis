package ca.corefacility.gview.writers;

import java.io.File;
import java.io.IOException;

import ca.corefacility.gview.map.GViewMap;


/**
 * Writes the map to an image.
 */
public abstract class ImageWriter
{
	/**
	 * Writes the passed map to a file.
	 * @param gviewMap  The map to write.
	 * @param filename  The name of the file (under the current working directory) to write to.
	 * 
	 * @throws IOException
	 */
	public abstract void writeToImage(GViewMap gviewMap, String filename)
		throws IOException;
	
	/**
	 * Writes the passed map to a file.
	 * @param gviewMap  The map to write.
	 * @param file  The file to write to.
	 * 
	 * @throws IOException
	 */
	public abstract void writeToImage(GViewMap gviewMap, File file)
		throws IOException;
	
	/**
	 * Writes the legend of the passed map to a file.
	 * @param gviewMap  The map to write.
	 * @param filename  The name of the file (under the current working directory) to write to.
	 * 
	 * @throws IOException
	 */
	public abstract void writeLegendToImage(GViewMap gviewMap, String filename)
		throws IOException;
	
	/**
	 * Writes the legend of the passed map to a file.
	 * @param gviewMap  The map to write.
	 * @param file  The file to write to.
	 * 
	 * @throws IOException
	 */
	public abstract void writeLegendToImage(GViewMap gviewMap, File file)
		throws IOException;
	
	/**
	 * Determines if this ImageWriter accepts the passed file format.
	 * @param fileFormat  The file format to check for acceptance.
	 * @return  True if this ImageWriter accepts the passed file format, false otherwise.
	 */
	public boolean isValidFileFormat(String fileFormat)
	{
		String[] acceptedImageFormats = getAcceptedImageFormats();
		
		for (String format : acceptedImageFormats)
		{
			if (format.equalsIgnoreCase(fileFormat))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the image formats accepted by this ImageWriter.
	 * @return  A String array containing the accepted image formats.
	 */
	public abstract String[] getAcceptedImageFormats();
}
