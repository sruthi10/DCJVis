package ca.corefacility.gview.layout.sequence;


import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.circular.BackboneCircular.ClashShiftException;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;

/**
 * Backbone stores the actual shape of the backbone, and how items should "wrap" themselves around backbone.
 * 
 * @author Aaron Petkau
 *
 */
// observer of backbone change events, listener to zoom events
// TODO fix which package this should be in
public interface Backbone extends GViewEventSubject, GViewEventListener
{
	public static enum ShiftDirection
	{
		ABOVE,
		BELOW
	}

	/**
	 * Translates the passed point to actual coordinates.
	 * 
	 * @param base  The base on the sequence.
	 * @param heightFromBackbone  The height to this point perpendicular from the backbone.
	 * 
	 * @return A point containing the actual coordinates on the map, or null if any value was out of range.
	 */
	public Point2D translate(double base, double heightFromBackbone);

	/**
	 * Translates the passed point to actual coordinates.
	 * 
	 * @param point  A point on this sequence's backbone to translate.
	 * 
	 * @return A point containing the actual coordinates on the map, or null if any value was out of range.
	 */
	public Point2D translate(SequencePoint point);

	/**
	 * Translates the passed point into coordinates on the backbone.
	 * 
	 * @param point  The point to translate.
	 * 
	 * @return  A SequencePoint describing the passed point in sequence coordinates, or null if passed point is null.
	 * 				May return negative values for base if point is outside of backbone bounds (past sequenceLength etc).
	 */
	public SequencePoint translate(Point2D point);

	/**
	 * Translates (base,height) + offset (length from base)
	 * @param base The base pair value to translate.
	 * @param heightFromBackbone  The height from the backbone of the particular base pair.
	 * @param lengthFromBase  The length (in screen coordinates) from the base pair value passed.
	 * @return  The Point on the screen representing this sequence point at the current scale.
	 */
	public Point2D translate(double base, double heightFromBackbone, double lengthFromBase);

	/**
	 * Obtains the spanned SequenceRectangle on the Backbone of the passed rectangle.
	 * 
	 * @param rectangle  The rectangle to obtain a SequenceRectangle from.
	 * 
	 * @return  A SequenceRectangle which completely contains the passed rectangle (in the current zoom context).
	 * 				Null if passed rectangle is null.
	 */
	public SequenceRectangle getSpannedRectangle(Rectangle2D rectangle);

	// TODO do I only need one of these?
	/**
	 * @return  The current scale the backbone is at.
	 */
	public double getScale();

	/**
	 * @return  The apparent length of the backbone, based upon the scale.
	 */
	public double getBackboneLength();

	/**
	 * Determines the zoom level where the on screen length for the base range deltaBase is equal to length.
	 * @param deltaBase
	 * @param length
	 * @return  The zoom level where the on screen length is equal to passed length.
	 */
	public double findZoomForLength(double deltaBase, double length);

	/**
	 * Converts from a given length along the backbone to the spanned base pairs this length covers at the current scale.
	 * @param lengthAlongBackbone  The length along the backbone to convert.
	 * @return  The base pairs covered by the passed length (negative if lengthAlongBackbone is negative).
	 */
	public double getSpannedBases(double lengthAlongBackbone);

	/**
	 * @return  The maximum scale this backbone can support.
	 */
	public double getMaxScale();

	/**
	 * @return  The minimum scale this backbone can support.
	 */
	public double getMinScale();

	/**
	 * Calculates the necessary shift needed for a rectangle centred at pinnedPoint so it is either just above or below (depending on passed direction) the height
	 * 	defined by heightLimitFromBackbone.
	 * @param pinnedPoint  The centre point of the rectangle to check.
	 * @param heightLimitFromBackbone  The height from the backbone that the rectangle should not clash with.
	 * @param width  The width of the rectangle.
	 * @param height  The height of the rectangle.
	 * @param direction  The direction to shift the rectangle in (above or below the height).
	 * 
	 * @return  The height from the backbone to add onto the pinnedPoint, or null if passed point is null.
	 * 
	 * @throws ClashShiftException
	 */
	public double calculateClashShift(SequencePoint pinnedPoint, double heightLimitFromBackbone, double width, double height, Backbone.ShiftDirection direction)
	throws ClashShiftException;

	/**
	 * Calculates the scale at which the rectangle R1 (defined by a pinned point, and a size) would not be overlapping rectangle R2
	 *  (defined by a pinned point and size) anymore.  So at any scale value beyond this scale, the rectangles don't overlap.
	 *  Assumes the passed sequencePoints (pinnedPoints) define the center of the rectangle width the passed dimensions.
	 * @param pinnedR1  The centre point of rectangle R1.
	 * @param sizeR1  The dimension of rectangle R1.
	 * @param pinnedR2  The centre point of rectangle R2.
	 * @param sizeR2  The dimension of rectangle R2.
	 * @return  The scale at which the two rectangles do not overlap anymore.
	 */
	public double calculateIntersectionScale(SequencePoint pinnedR1, Dimension2D sizeR1, SequencePoint pinnedR2, Dimension2D sizeR2);

	/**
	 * Gets the apparent length of the backbone at the passed scale.
	 * @param scale  The scale to get the backbone length for.
	 * @return  The apparent length of the backbone.
	 */
	public double getBackboneLengthAt(double scale);
}
