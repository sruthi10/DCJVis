 package ca.corefacility.gview.layout.prototype;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.circular.Polar;
import ca.corefacility.gview.utils.Util;


/**
 * Describes a shape that can be pinned to a location on the backbone and will stick there on zooming.
 * 	The initial shape is assumed to be vertical and is rotated depending on the point it is pinned to so that it is
 * 	perpendicular to the backbone (this is how tick mark rotation is handled in circular view).
 * @author Aaron Petkau
 *
 */
public class PinnableShape extends BackboneShape implements BackbonePinnable
{
	/**
	 * Where the shape is pinned to on the sequence.  The pin point is located at the very center of a bounding box of the shape.
	 */
	private SequencePoint pinnedPoint;
	
	// stores the original shape centered at the origin
	private Shape initialShape;
	
	private double initialAngle = Math.PI/2; // the initial angle of the shape (radians)
	
	private final AffineTransform transform = new AffineTransform(); // used to perform any transforms/rotations
	
	private Shape pinnedShape; // cache of shape after a pinTo operation
	private Shape currentShape; // stores the current position/bounds etc of the shape
	
	/**
	 * Creates a new Pinnable shape.
	 * 
	 * @param backbone  The backbone about which this shape is located.
	 * @param initialShape  The initial shape we will be pinning.
	 * @param pinnedPoint  The point where we are pinning the shape (the very center of a bounding box of the shape).
	 */
	public PinnableShape(Backbone backbone, Shape initialShape, SequencePoint pinnedPoint)
	{
		super(backbone);
		
		if (initialShape == null || pinnedPoint == null)
		{
			throw new IllegalArgumentException("parameters are null");
		}
		
		this.initialShape = centerOnOrigin(initialShape);
		
		pinTo(pinnedPoint);
	}
	
	/**
	 * Centers the passed shape at the origin (used so we can properly rotate shape).
	 * 
	 * @param shape  The shape to center.
	 * 
	 * @return  A new shape that has been centered on the origin. (or null if no passed shape).
	 */
	private Shape centerOnOrigin(Shape shape)
	{
		Shape centeredShape = null;
		
		Point2D initialPoint = getInitialPoint(shape);
		
		transform.setToTranslation(-initialPoint.getX(), -initialPoint.getY());
		
		centeredShape = transform.createTransformedShape(shape);
		
		// this is for testing
		initialPoint = getInitialPoint(centeredShape);
		assert(Util.isEqual(initialPoint.getX(), 0) && Util.isEqual(initialPoint.getY(), 0));
			
		return centeredShape;
	}
	
	/**
	 * Pins this shape onto the passed SequencePointImp.
	 * 
	 * @param pinnedPoint  A pinnedPoint on the sequence where we should pin this shape to.
	 */
	public void pinTo(SequencePoint pinnedPoint)
	{
		if (pinnedPoint != null)
		{
			this.pinnedPoint = pinnedPoint;
			
//			Point2D newPoint = backbone.translate(pinnedPoint);
//			if (newPoint == null) {
//				return;
//			}
			
//			transform.setToTranslation(newPoint.getX(), newPoint.getY());
			
			// used to properly rotate the shape so it's oriented perpendicularly along backbone
			transform.setToRotation(getOrientationAngleFor(pinnedPoint));
			
			// rotate the inital shape so it's oriented correctly
			pinnedShape = transform.createTransformedShape(initialShape);
			
			// set current shape
			shapeChanged = true;
		}
		else
		{
			throw new IllegalArgumentException("pinnedPoint is null");
		}
	}
	
	public SequencePoint getPinnedPoint()
	{
		return pinnedPoint;
	}
	
	public void translate(double base, double heightFromBackbone)
	{
		pinnedPoint.translate(base, heightFromBackbone);
		pinTo(pinnedPoint);
	}

	/**
	 * Gets the angle used to re-orient the shape at the passed pinnedPoint
	 * 
	 * @param pinnedPoint  The point where we want to re-orient to.
	 * 
	 * @return  An angle (in radians) to rotate the shape.
	 */
	private double getOrientationAngleFor(SequencePoint pinnedPoint)
	{
		// get two points on backbone, get delta vector from it and get angle of vector
		Point2D initialPoint = backbone.translate(pinnedPoint);
		Point2D finalPoint = backbone.translate(pinnedPoint.getBase(), pinnedPoint.getHeightFromBackbone() + 1);
		
		Polar delta = Polar.createPolar(getDelta(finalPoint, initialPoint));
		
		// assume inital shape is oriented vertically, so we must account for this angle
		return delta.getTheta() - initialAngle;
	}
	
	// gets the initial point from the passed shape
	private Point2D getInitialPoint(Shape initialShape)
	{		
		Rectangle2D bounds = initialShape.getBounds2D();
		double width = bounds.getWidth();
		double height = bounds.getHeight();
		
		// return point at very center
		return new Point2D.Double(bounds.getX() + width/2, bounds.getY() + height/2);
	}
	
	/**
	 * Returns a difference vector between both points.
	 * @param finalPoint
	 * @param initialPoint
	 * @return A Point2D object defining the difference between the final and inital points.
	 */
	private Point2D getDelta(Point2D finalPoint, Point2D initialPoint)
	{
		return new Point2D.Double(finalPoint.getX()-initialPoint.getX(), finalPoint.getY()-initialPoint.getY());
	}

	@Override
	protected Shape getCurrentShape()
	{
		return currentShape;
	}

	@Override
	protected void modifyShape(Backbone backbone)
	{
		if (!shapeChanged) {
			return;
		}
		
		if (backbone != null)
		{
			Point2D newPoint = backbone.translate(pinnedPoint);
			if (newPoint == null) {
				return;
			}
			
			transform.setToTranslation(newPoint.getX(), newPoint.getY());
			
			currentShape = transform.createTransformedShape(pinnedShape);
		}
	}
}
