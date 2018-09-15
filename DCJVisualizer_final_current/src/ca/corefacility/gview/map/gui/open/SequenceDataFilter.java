package ca.corefacility.gview.map.gui.open;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class SequenceDataFilter extends FileFilter
{

	@Override
	public boolean accept(File f)
	{
		if (f != null)
		{
			if (f.exists())
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String getDescription()
	{
		return "Genbank, GFF, FASTA, EMBL, CGView XML";
	}
}
