package ca.corefacility.gview.layout.prototype;

import java.awt.Shape;

import ca.corefacility.gview.layout.sequence.SequencePath;

/**
 * Describes a shape which can be broken up into sub regions of a particular orientation.
 * @author aaron
 *
 */
public interface OrientableShape extends Shape
{
    /**
     * Gets the sub region of this shape with the given orientation.
     * @param orientation  The orientation to use.
     * @return  The sub region of the shape with the given orientation, or null if no such region exists.
     */
    public Shape getRegion(SequencePath.Orientation orientation);
}
