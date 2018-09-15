package ca.corefacility.gview.writers;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to build an ImageWriter to be used to write GView-generated images to a specific file format.
 */
public class ImageWriterFactory
{
	public static final String[] OUTPUT_FORMATS = { "png", "jpg", "svg", "svgz"};
	
	private static List<ImageWriter> imageWriters;

	static
	{
		imageWriters = new LinkedList<ImageWriter>();

		imageWriters.add(new ImageWriterJPG());
		imageWriters.add(new ImageWriterPNG());
		imageWriters.add(new ImageWriterSVG("svg"));
		imageWriters.add(new ImageWriterSVG("svgz"));
	}

	/**
	 * Create an ImageWriter for the passed file format.
	 * @param imageExtension  The file format (by extension of file) to use.
	 * @return  An ImageWriter for the passed file format, or null if format not supported.
	 */
	public static ImageWriter createImageWriter(String imageExtension)
	{
		ImageWriter writer = null;

		if (imageExtension != null)
		{
			for (ImageWriter imageWriter : imageWriters)
			{
				if (imageWriter.isValidFileFormat(imageExtension))
				{
					writer = imageWriter;
					break;
				}
			}

			if (writer == null)
			{
				System.err.println("No support for file format " + imageExtension);
			}
		}

		return writer;
	}

	/**
	 * Determines if we can construct an ImageWriter with the passed extension.
	 * @param extension  The extension to check against.
	 * @return  True if support is provided for this extension, false otherwise.
	 */
	public static boolean acceptsExtension(String extension)
	{
		if (extension == null)
			return false;

		for (ImageWriter imageWriter : imageWriters)
		{
			if (imageWriter.isValidFileFormat(extension))
				return true;
		}

		return false;
	}

	/**
	 * Gets a copy of the string array of accepted extensions.
	 * @return  A copy of the string array of accepted extensions.
	 */
	public static String[] getAcceptedExtensions()
	{
		ArrayList<String> acceptedExtensions = new ArrayList<String>();

		for (ImageWriter imageWriter : imageWriters)
		{
			String[] imageFormats = imageWriter.getAcceptedImageFormats();

			if (imageFormats != null)
			{
				for (String format : imageFormats)
				{
					acceptedExtensions.add(format);
				}
			}
		}
		String[] toReturn = null;
		return (String[])acceptedExtensions.toArray( toReturn );
	}

	/**
	 * Checks if the passed file can be saved to (format is correct).
	 * @param file  The file to save to.
	 * @return  True if this file format is supported, false otherwise.
	 */
	public static boolean acceptsFile(File file)
	{
		return acceptsExtension(extractExtension(file.getName()));
	}

	/**
	 * Creates an ImageWriter from the passed file, based on the file format.
	 * @param file  The file to create an ImageWriter around.
	 * @return  An ImageWriter to support writing to the passed file format.
	 */
	public static ImageWriter createImageWriter(File file)
	{
		if (file == null)
			throw new NullPointerException("file is null");

		String extension = extractExtension(file.getName());

		return createImageWriter(extension);
	}

	/**
	 * Extracts the extension from the passed file name.
	 * @param filename  The file name to extract the extension from.
	 * @return  The extension of the passed file name.
	 */
	public static String extractExtension(String filename)
	{
		return ImageWriterFilter.extractExtension(filename);
	}
}
