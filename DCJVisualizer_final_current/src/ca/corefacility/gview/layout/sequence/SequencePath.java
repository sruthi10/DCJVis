package ca.corefacility.gview.layout.sequence;

import java.awt.Shape;
import java.awt.Stroke;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.prototype.BackboneShape;


/**
 * Allows drawing of arbitrary shapes along the Sequence.
 * @author Aaron Petkau
 *
 */
public interface SequencePath
{
    /**
     * Defines the orientation of particular lines along the path, used for some effects.
     * @author aaron
     *
     */
    public enum Orientation
    {
        TOP,
        BOTTOM,
        NONE
    }
    
	/**
	 * Adds a point at the passed location.
	 * 
	 * @param base  The base on the sequence.
	 * @param heightFromBackbone  The height from the backbone of this point.
	 */
	public void moveTo(double base, double heightFromBackbone);
	
    /**
     * Adds a point at the passed location.
     * 
     * @param base  The base on the sequence.
     * @param heightFromBackbone  The height from the backbone of this point.
     * @param orientation  The orientation of the point we moved to on the overall shape.
     */
    public void moveTo(double base, double heightFromBackbone, Orientation orientation);
	
	/**
	 * Move to the location defined by (base, heightFromBackbone) + (lengthFromBase, 0)
	 * @param base  The base pair value to move to.
	 * @param heightFromBackbone  The height from the backbone to move to.
	 * @param lengthFromBase  The length from the base pair (in sequence coordinates) to move to.
	 */
	public void moveTo(double base, double heightFromBackbone, double lengthFromBase);
	
    /**
     * Move to the location defined by (base, heightFromBackbone) + (lengthFromBase, 0)
     * @param base  The base pair value to move to.
     * @param heightFromBackbone  The height from the backbone to move to.
     * @param lengthFromBase  The length from the base pair (in sequence coordinates) to move to.
     * @param orientation  The orientation of the point we moved to on the overall shape.
     */
    public void moveTo(double base, double heightFromBackbone, double lengthFromBase, Orientation orientation);
	
	/**
	 * Draws a line from the current coordinates to the new coordinates.  The line will be straight in
	 * 	sequence coordinates, but may be curved in actual coordinates.
	 * 
	 * @param base  The base on the sequence.
	 * @param heightFromBackbone  The height from the backbone of this point.
	 * @param orientation  The orientation of the line.
	 */
	public void lineTo(double base, double heightFromBackbone, Direction direction, Orientation orientation);
	
	/**
     * Draws a line from the current coordinates to the new coordinates.  The line will be straight in
     *  sequence coordinates, but may be curved in actual coordinates.
     * 
     * @param base  The base on the sequence.
     * @param heightFromBackbone  The height from the backbone of this point.
     */
    public void lineTo(double base, double heightFromBackbone, Direction direction);
	
	/**
	 * Defines a true line to the point defined by (base,heightInSlot) + an offset (in terms of lengthFromBase, height).
	 * @param base  The base pair value to draw to.
	 * @param heightFromBackbone  The height from the backbone to draw to.
	 * @param lengthFromBase  The length from the base pair (in sequence coordinates) to draw to.
	 * @param orientation  The orientation of the line.
	 */
	public void realLineTo(double base, double heightFromBackbone, double lengthFromBase, Orientation orientation);
	
	/**
     * Defines a true line to the point defined by (base,heightInSlot) + an offset (in terms of lengthFromBase, height).
     * @param base  The base pair value to draw to.
     * @param heightFromBackbone  The height from the backbone to draw to.
     * @param lengthFromBase  The length from the base pair (in sequence coordinates) to draw to.
     */
    public void realLineTo(double base, double heightFromBackbone, double lengthFromBase);
	
	/**
	 * Defines a line (in terms of sequence coordinates) to the point defined by (base,heightFromBackbone) + an offset (in terms of lengthFromBase, height).
	 * @param base  The base pair value to draw to.
	 * @param heightFromBackbone  The height from the backbone to draw to.
	 * @param lengthFromBase  The length from the base pair (in sequence coordinates) to draw to.
	 * @param direction  The direction to draw the passed line.
	 * @param orientation  The orientation of the line.
	 */
	public void lineTo(double base, double heightFromBackbone, double lengthFromBase, Direction direction, Orientation orientation);
	
	   /**
     * Defines a line (in terms of sequence coordinates) to the point defined by (base,heightFromBackbone) + an offset (in terms of lengthFromBase, height).
     * @param base  The base pair value to draw to.
     * @param heightFromBackbone  The height from the backbone to draw to.
     * @param lengthFromBase  The length from the base pair (in sequence coordinates) to draw to.
     * @param direction  The direction to draw the passed line.
     */
    public void lineTo(double base, double heightFromBackbone, double lengthFromBase, Direction direction);
	
	/**
	 * Draws a line from the current coordinates to the new coordinates using the previous heightFromBackbone
	 * 	(or 0 if no previous heightFromBackbone).
	 * 
	 * @param base  The base on the sequence.
	 * @param direction  The direction to draw the passed line.
	 * @param orientation  The orientation of the line.
	 */
	public void lineTo(double base, Direction direction, Orientation orientation);
	
	   /**
     * Draws a line from the current coordinates to the new coordinates using the previous heightFromBackbone
     *  (or 0 if no previous heightFromBackbone).
     * 
     * @param base  The base on the sequence.
     * @param direction  The direction to draw the passed line.
     */
    public void lineTo(double base, Direction direction);
	
	/**
	 * Closes the path, draws line back to coords of last moveTo.
	 */
	public void closePath();
	
	/**
     * Closes the path, draws line back to coords of last moveTo.
     * 
     * @param orientation  The orientation of the line used to close.
     */
    public void closePath(Orientation orientation);
	
	/**
	 * Gets the shape described by this SequencePath.
	 * @return  The shape described by this SequencePath.
	 */
	public BackboneShape getShape();
	
	/**
	 * Clears this SequencePath.
	 */
	public void clear();

	/**
	 * Returns the created shape with the passed stroke applied to it.
	 * @param s  The stroke to apply
	 * @return  The shape with the stroke s applied to it (if s is null, then returns same as getShape()).
	 */
	public BackboneShape createStrokedShape(Stroke s);

	/**
	 * Creates a shape that stretches the full length of the backbone with the passed top/bottom values.
	 * @param topHeight
	 * @param bottomHeight
	 * @param trueLocation 
	 * @return A shape that stretches the full length of the backbone.
	 */
	public Shape createFullBackboneShape(double topHeight,
			double bottomHeight, Location trueLocation);
}
