package ca.corefacility.gview.layout.sequence;


import java.awt.Shape;
import java.awt.Stroke;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.prototype.BackboneShape;

/**
 * Builds a general path by transforming the sequence-specific coords,
 *   to coords on the screen in an appropriate slot.
 */
public interface SlotPath extends Cloneable
{
    public Backbone getBackbone();
    
	/**
	 * Adds a point at the passed location.
	 * 
	 * @param base  The base on the sequence.
	 * @param heightInSlot  The height in the slot (0 = center) (-1 to 1)
	 */
	public void moveTo(double base, double heightInSlot);
	
    /**
     * Adds a point at the passed location.
     * 
     * @param base  The base on the sequence.
     * @param heightInSlot  The height in the slot (0 = center) (-1 to 1)
     * @param orientation  The orientation of the point on the overall shape.
     */
    public void moveTo(double base, double heightInSlot, SequencePath.Orientation orientation);

	/**
	 * Move to the location defined by (base, heightInSlot) + offset = (lengthFromBase, height)
	 * @param base  The base pair value to move to.
	 * @param heightInSlot  The height in the slot to move to.
	 * @param lengthFromBase  The length from the base pair (in sequence coordinates) to move to.
	 */
	public void moveTo(double base, double heightInSlot, double lengthFromBase);
	
    /**
     * Move to the location defined by (base, heightInSlot) + offset = (lengthFromBase, height)
     * @param base  The base pair value to move to.
     * @param heightInSlot  The height in the slot to move to.
     * @param lengthFromBase  The length from the base pair (in sequence coordinates) to move to.
     * @param orientation  The orientation of the point on the overall shape.
     */
    public void moveTo(double base, double heightInSlot, double lengthFromBase, SequencePath.Orientation orientation);

	/**
	 * Draws a line from the current coordinates to the new coordinates.  The line will be straight in
	 * 	sequence coordinates, but may be curved in actual coordinates.
	 * 
	 * @param base  The base on the sequence.
	 * @param heightInSlot  The height in the slot (0 = center) (-1 to 1)
	 * @param direction  The direction to draw the line along the sequence.
	 * @param orientation  The orientation of the line.
	 */
	public void lineTo(double base, double heightInSlot, Direction direction, SequencePath.Orientation orientation);
	
    /**
     * Draws a line from the current coordinates to the new coordinates.  The line will be straight in
     *  sequence coordinates, but may be curved in actual coordinates.
     * 
     * @param base  The base on the sequence.
     * @param heightInSlot  The height in the slot (0 = center) (-1 to 1)
     * @param direction  The direction to draw the line along the sequence.
     */
    public void lineTo(double base, double heightInSlot, Direction direction);

	/**
	 * Defines a true line to the point defined by (base,heightInSlot) + an offset (in terms of lengthFromBase, height).
	 * @param base  The base pair value to draw to.
	 * @param heightInSlot  The height in the slot to draw to.
	 * @param lengthFromBase  The length from the base pair (in sequence coordinates) to draw to.
	 * @param orientation  The orientation of the line.
	 */
	public void realLineTo(double base, double heightInSlot, double lengthFromBase, SequencePath.Orientation orientation);
	
    /**
     * Defines a true line to the point defined by (base,heightInSlot) + an offset (in terms of lengthFromBase, height).
     * @param base  The base pair value to draw to.
     * @param heightInSlot  The height in the slot to draw to.
     * @param lengthFromBase  The length from the base pair (in sequence coordinates) to draw to.
     */
    public void realLineTo(double base, double heightInSlot, double lengthFromBase);

	/**
	 * Defines a line (in terms of sequence coordinates) to the point defined by (base,heightInSlot) + an offset (in terms of lengthFromBase, height).
	 * @param base  The base pair value to draw to.
	 * @param heightInSlot  The height in the slot to draw to.
	 * @param lengthFromBase  The length from the base pair (in sequence coordinates) to draw to.
	 * @param direction  The direction to draw the line along the sequence.
	 * @param orientation  The orientation of the line.
	 */
	public void lineTo(double base, double heightInSlot, double lengthFromBase, Direction direction, SequencePath.Orientation orientation);
	
    /**
     * Defines a line (in terms of sequence coordinates) to the point defined by (base,heightInSlot) + an offset (in terms of lengthFromBase, height).
     * @param base  The base pair value to draw to.
     * @param heightInSlot  The height in the slot to draw to.
     * @param lengthFromBase  The length from the base pair (in sequence coordinates) to draw to.
     * @param direction  The direction to draw the line along the sequence.
     */
    public void lineTo(double base, double heightInSlot, double lengthFromBase, Direction direction);

	/**
	 * Draws a line from the current coordinates to the new coordinates using the previous heightInSlot
	 * 	(or 0 if no previous heightInSlot).
	 * 
	 * @param base  The base on the sequence.
	 * @param direction  The direction to draw the line along the sequence.
	 * @param orientation  The orientation of the line.
	 */
	public void lineTo(double base, Direction direction, SequencePath.Orientation orientation);
	
	/**
     * Draws a line from the current coordinates to the new coordinates using the previous heightInSlot
     *  (or 0 if no previous heightInSlot).
     * 
     * @param base  The base on the sequence.
     * @param direction  The direction to draw the line along the sequence.
     */
    public void lineTo(double base, Direction direction);

	/**
	 * Closes the path, draws line back to coords of last moveTo.
	 * 
	 * @param orientation  The orientation of the close segment.
	 */
	public void closePath(SequencePath.Orientation orientation);

    /**
     * Closes the path, draws line back to coords of last moveTo.
     */
    public void closePath();

	/**
	 * @return  The shape described by this SlotPath.
	 */
	public BackboneShape getShape();

	/**
	 * Clears SlotPath
	 */
	public void clear();

	/**
	 * Creates a shape by stroking the path defined with the passed stroke.
	 * 
	 * @param stroke  The stroke defining how to create the shape.
	 * 
	 * @return  A shape stroked by the passed parameter.
	 */
	public Shape createStrokedShape(Stroke stroke);

	/**
	 * Builds a shape which stretches all the way along the backbone.
	 * @param top  The top of the shape to build in this slot.
	 * @param bottom  The bottom of the shape to build in this slot.
	 * @param trueLocation 
	 * @return  The shape built.
	 */
	public Shape createFullBackboneShape(double top, double bottom, Location trueLocation);
}
