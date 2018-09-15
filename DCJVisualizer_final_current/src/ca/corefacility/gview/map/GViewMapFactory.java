package ca.corefacility.gview.map;


import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.biojava.bio.seq.Sequence;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.data.GenomeDataImp;
import ca.corefacility.gview.data.readers.GFF3Reader;
import ca.corefacility.gview.data.readers.GViewDataParseException;
import ca.corefacility.gview.data.readers.GViewFileData;
import ca.corefacility.gview.data.readers.GViewFileReader;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.StyleFactory;
import ca.corefacility.gview.style.io.StyleIO;
import ca.corefacility.gview.style.io.StyleIOGSS;

/**
 * Responsible for creating a new gViewMap with the appropriate info.
 */
public class GViewMapFactory
{
	/**
	 * Creates and sets up a new gViewMap from the passed data.
	 * 
	 * @param genomeData  The data defining the genome.
	 * @param style  The style defining how the data should be displayed.
	 * @param layoutFactory  An objected used to construct the layout of the GView map.
	 * 
	 * @return The newly created GView map.
	 */
	public static GViewMap createMap(GenomeData genomeData, MapStyle style, LayoutFactory layoutFactory)
	{
		GViewMap gViewMap = new GViewMapImp(genomeData, style, layoutFactory);

		return gViewMap;
	}

	/**
	 * Creates and sets up a new GView map from the passed sequence data.
	 * @param sequence  The sequence data to create the map around.
	 * @param style  The style information to build the map with.
	 * @param layoutFactory  The LayoutFactory used to construct the layout of the GView map.
	 * @return  The newly created gview map.
	 */
	public static GViewMap createMap(Sequence sequence, MapStyle style, LayoutFactory layoutFactory)
	{
		GenomeData gData = new GenomeDataImp(sequence);
		return createMap(gData, style, layoutFactory);
	}
	
	/**
	 * Creates and sets up a new GViewMap from the parameters provided.
	 * 
	 * @param sequence The sequence input.
	 * @param style The style input.
	 * @param gffs The associated GFF files.
	 * @param layout The initial layout.
	 * @return The newly created GViewMap.
	 * @throws IOException
	 * @throws GViewDataParseException
	 */
	public static GViewMap createMap(URI sequence, URI style, ArrayList<URI> gffs, LayoutFactory layout) throws IOException, GViewDataParseException
	{
		GFF3Reader gffReader;
		StyleIO styleIO = new StyleIOGSS();
		
		GViewFileData gViewFileData = GViewFileReader.read(sequence.toURL());
		GenomeData genomeData = gViewFileData.getGenomeData();
		MapStyle mapStyle = null;
		
		//MapStyle:
		//Handling XML case:
		if (gViewFileData.getMapStyle() == null)
		{
			if(style == null)
			{
				//Use a default style:
				mapStyle = StyleFactory.createDefaultStyle2();
			}
			else
			{
				//Use the provided style:
				mapStyle = styleIO.readMapStyle(style.toURL());
			}
		}
		else
		{
			mapStyle = gViewFileData.getMapStyle();
		}
		
		//GFF features:
		if (gffs != null)
		{
			gffReader = new GFF3Reader();
			
			//Iterate over all possible GFF files:
			for(URI gff : gffs)
			{
				//Non-null GFFs:
				if(gff != null)
				{
					//Can we read it?
					if (gffReader.canRead(gff))
					{
						try
						{
							gffReader.injectGFFFeatures(genomeData, gff.toString());
						}
						catch (GViewDataParseException e)
						{
							e.printStackTrace();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
					//Can't read the GFF:
					else
					{
						System.err.println("Cannot read GFF file: " + gff);
					}
				}
			}
		}
		
		return GViewMapFactory.createMap(genomeData, mapStyle, layout);
	}
}
