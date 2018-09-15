package ca.corefacility.gview.map.gui.open;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import ca.corefacility.gview.utils.Util;

public class GFFFilter extends FileFilter
{

	@Override
	public boolean accept(File f)
	{
		if (f != null)
		{
			if (f.isDirectory())
			{
				return true;
			}
			else if (f.exists())
			{
				String extension = Util.extractExtension(f.getName());
				
				return ("gff".equalsIgnoreCase(extension));
			}
		}
		
		return false;
	}

	@Override
	public String getDescription()
	{
		return "GFF File";
	}

}
