package ca.corefacility.gview.layout.prototype;

import ca.corefacility.gview.layout.prototype.segments.StretchableSegment;
import ca.corefacility.gview.layout.sequence.Backbone;


public abstract class StretchableShape extends BackboneShape
{

	public StretchableShape(Backbone backbone)
	{
		super(backbone);
	}

	/**
	 * Appends the passed segment onto this shape prototype.
	 * 
	 * @param segment  The segment to append.
	 */
	public abstract void appendSegment(StretchableSegment segment);

	protected abstract void modifyShape(Backbone backbone);
	
    /**
     * Updates the given backbone height for this shape, used for determining the maximum thickness of this shape.
     * @param height  The height to update.
     */
    public abstract void updateHeight(float height);
}