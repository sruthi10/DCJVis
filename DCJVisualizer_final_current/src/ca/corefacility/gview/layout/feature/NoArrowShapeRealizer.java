package ca.corefacility.gview.layout.feature;


import java.awt.Shape;
import java.util.Iterator;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.sequence.SlotPath;

/**
 * Draws a feature with no arrow head.  So draws a feature that looks like
 * 
 * ===
 * 
 * @author Aaron Petkau
 *
 */
public class NoArrowShapeRealizer extends FeatureShapeRealizer
{
	@Override
	protected Shape realizeFeatureShape(SlotPath path, Location location, double top, double bottom, int sequenceLength)
	{
		Iterator blocks = location.blockIterator();
		while (blocks.hasNext())
		{
			Location block = (Location)blocks.next();
			int start = extractStart(block);
			int stop = extractStop(block);
			
			if (start == stop)
			{
			    createPointBlock(path,start,top,bottom);
			}
			else
			{
			    createStandardBlock(path, start, stop, top, bottom);
			}
		}
		
		return path.getShape();
	}
}
