package ca.corefacility.gview.layout.feature;

import java.awt.Shape;


import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.prototype.StretchableShape;
import ca.corefacility.gview.layout.sequence.SlotPath;

/**
 * Draws no shape for the feature.
 * @author Aaron Petkau
 *
 */
public class HiddenShapeRealizer extends FeatureShapeRealizer
{
	// this is the only place where I need to override the public method
	public StretchableShape createFeaturePrototype(SlotPath path, Location location, double thickness)
	{
		return null;
	}
	
	protected Shape realizeFeatureShape(SlotPath path, Location location,
			double top, double bottom, int sequenceLength)
	{
		return null;
	}
}
