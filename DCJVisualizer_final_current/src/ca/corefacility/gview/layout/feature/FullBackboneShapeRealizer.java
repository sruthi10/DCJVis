package ca.corefacility.gview.layout.feature;

import java.awt.Shape;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.sequence.SlotPath;

/**
 * Used to build a shape that extends the full backbone width.
 * @author aaron
 *
 */
public class FullBackboneShapeRealizer extends FeatureShapeRealizer
{
	public static final FullBackboneShapeRealizer instance = new FullBackboneShapeRealizer();
	
	@Override
	protected Shape realizeFeatureShape(SlotPath path, Location trueLocation,
			double top, double bottom, int locationLength)
	{
		return path.createFullBackboneShape(top, bottom, trueLocation);
	}
}
