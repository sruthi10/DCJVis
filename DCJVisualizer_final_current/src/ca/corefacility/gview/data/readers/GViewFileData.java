package ca.corefacility.gview.data.readers;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.style.MapStyle;

/**
 * Defines a class which can store the GView data that is extracted from a file.
 * 
 * @author Aaron Petkau
 *
 */
public class GViewFileData
{
	private GenomeData genomeData;
	private MapStyle mapStyle;
	
	/**
	 * Constructs a new GViewFileData object storing the passed information.
	 * @param genomeData
	 * @param mapStyle
	 */
	public GViewFileData(GenomeData genomeData, MapStyle mapStyle)
	{
		this.genomeData = genomeData;
		this.mapStyle = mapStyle;
	}

	/**
	 * @return  The genome data.
	 */
	public GenomeData getGenomeData()
	{
		return genomeData;
	}

	/**
	 * @return  The map style.
	 */
	public MapStyle getMapStyle()
	{
		return mapStyle;
	}
}
