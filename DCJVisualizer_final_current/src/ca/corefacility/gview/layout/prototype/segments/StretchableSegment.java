package ca.corefacility.gview.layout.prototype.segments;


import java.awt.geom.Path2D;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;

/**
 * Defines a single Segment of a NonFragmentingStretchableShape.
 * 
 * @author Aaron Petkau
 *
 */
public abstract class StretchableSegment
{
    protected SequencePath.Orientation orientation;
    
	public StretchableSegment()
	{
	    orientation = SequencePath.Orientation.NONE;
	}
	
	/**
	 * Appends this StretchableSegment to the passed path.
	 * 
	 * @param path  The path to append this segment to.
	 * @param backbone  The backbone about which we are stretching this segment around.
	 */
	public void appendWithScale(Path2D path, Backbone backbone)
	{
		if ((path != null) && (backbone != null))
		{
			performAppend(path, backbone);
		}
		else
		{
			throw new IllegalArgumentException("path or backbone are null");
		}
	}
	
	protected abstract void performAppend(Path2D path, Backbone backbone);

    public SequencePath.Orientation getOrientation()
    {
        return orientation;
    }
}
