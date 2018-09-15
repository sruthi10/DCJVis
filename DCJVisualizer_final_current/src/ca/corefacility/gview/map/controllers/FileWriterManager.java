package ca.corefacility.gview.map.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.StyleIOGSS;

/**
 * This class is responsible for controlling access to file writing.
 * 
 * @author Eric Marinier
 *
 */
public class FileWriterManager
{
	private final MapStyle mapStyle;
	
	/**
	 * 
	 * @param mapStyle The GViewMap MapStyle.
	 */
	public FileWriterManager(MapStyle mapStyle)
	{
		this.mapStyle = mapStyle;
	}
	
	/**
	 * Writes the associated MapStyle to the passed file location.
	 * 
	 * @param file The file to write to.
	 * @throws IOException
	 */
	public void writeStyleToFile(File file) throws IOException
	{
		StyleIOGSS styleIO = new StyleIOGSS();
		Writer styleWriter = new FileWriter(file);
		
		styleIO.writeMapStyle(this.mapStyle, styleWriter);
	}
}
