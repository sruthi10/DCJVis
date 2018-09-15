package ca.corefacility.gview.layout.prototype.segments;


import java.awt.geom.Path2D;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;

public class CloseSegment extends StretchableSegment
{
	/**
	 * Creates a new CloseSegment type.
	 */
	public CloseSegment(SequencePath.Orientation orientation)
	{
	    this.orientation = orientation;		
	}

	@Override
	protected void performAppend(Path2D path, Backbone backbone)
	{
		path.closePath();
	}
}
