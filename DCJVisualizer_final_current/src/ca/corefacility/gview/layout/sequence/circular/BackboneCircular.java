package ca.corefacility.gview.layout.sequence.circular;


import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.prototype.SequencePointImp;
import ca.corefacility.gview.layout.sequence.AbstractBackbone;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.LocationConverter;
import ca.corefacility.gview.layout.sequence.SequenceRectangle;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.utils.Util;

public class BackboneCircular extends AbstractBackbone
{
	private double maxScale;
	private double minScale;

	private final double initialRadius; // initial radius of the backbone from the center of the map
	private double backboneRadius; // scaled radius of the backbone

	private final double zeroAngle; // angle of the zero base (radians)

	private final double anglePerBase; // angle (radians) per a single base on the backbone

	// not sure if I need this
	// maybe use a AffineTransform instead
	private Point2D centerPoint; // center of the circular map in screen coords

	/**
	 * Creates a new circular backbone.
	 * 
	 * @param locationConverter
	 *            A LocationConverter objected used to convert from constant locations, so actual locations on the sequence.
	 * @param initialRadius
	 *            The initial radius of the backbone.
	 * @param minHeightFromBackbone
	 *            The minimum height from the backbone of the slots (for calculating minScale).
	 */
	public BackboneCircular(LocationConverter locationConverter, double initialRadius, double minHeightFromBackbone)
	{
		super(locationConverter);

		if (initialRadius <= 0)
			throw new IllegalArgumentException("initialRadius=" + initialRadius + " is invalid");
		else
		{
			// make sure initial radius is large enough to fit items without overlap
			double absoluteMinScale = Math.abs(minHeightFromBackbone) / initialRadius;		
			if (absoluteMinScale > 1.0)
			{
				System.err.println("[warning] initialBackboneLength of " + (2*Math.PI*initialRadius) + " is too small, adjusting...");
				absoluteMinScale = 1.0;
				
				initialRadius = Math.abs(minHeightFromBackbone)/absoluteMinScale;
			}
			
			// restrict minimum scale
			this.minScale = 1.0;
			
			this.initialRadius = initialRadius;
			this.backboneRadius = initialRadius;

			this.anglePerBase = 2 * Math.PI / locationConverter.getSequenceLength();

			// TODO extract this better
			this.zeroAngle = Math.PI / 2;
			
			setMaxScale(this.anglePerBase, initialRadius);

			this.centerPoint = new Point2D.Double(0, 0);
		}
	}

	public double findZoomForLength(double deltaBase, double length)
	{
		if (deltaBase != 0)
			return length / (deltaBase * this.initialRadius * this.anglePerBase);

		return 0;
	}

	public double getBackboneLength()
	{
		return 2 * Math.PI * this.backboneRadius;
	}

	private void setMaxScale(double anglePerBase, double initialRadius)
	{
		// max scale is when the lengthPerBase value is large enough
		double initalLengthPerBase = anglePerBase * initialRadius;

		this.maxScale = MAX_LENGTH_PER_BASE / initalLengthPerBase;
	}

	@Override
	protected void setScale(double scale)
	{
		if (scale > 0 && scale <= getMaxScale())
		{
			this.scale = scale;

			this.backboneRadius = scale * this.initialRadius;
			// System.out.println("InitialRadius = " + initialRadius + ", scale=" + scale +
			// ", maxScale=" + getMaxScale());
			this.eventSubject.fireEvent(new BackboneZoomEvent(this));
		}
		else
			throw new IllegalArgumentException("scale=" + scale + " not in range [0," + getMaxScale() + "]");
	}

	public double getSpannedBases(double lengthAlongBackbone)
	{
		// lengthAlongBackbone in this case represents arc length of a circle
		double theta = lengthAlongBackbone / this.backboneRadius;

		return theta / this.anglePerBase;
	}

	public double getMaxScale()
	{
		return this.maxScale;
	}

	public double getMinScale()
	{
		return this.minScale;
	}

	public Point2D translate(double realBase, double heightFromBackbone)
	{
		// assumes that the centre of the map is point (0,0)

		// position of the point in polar coords
		double radius;
		double angle;

		// position of the point
		double x;
		double y;

		double angleFromZeroAngle; // the angle relative to the zeroAngle

		angleFromZeroAngle = realBase * this.anglePerBase;

		angle = this.zeroAngle - angleFromZeroAngle;
		radius = this.backboneRadius + heightFromBackbone;

		x = radius * Math.cos(angle);
		y = -radius * Math.sin(angle); // negative to account for y-coords being opposite

		return new Point2D.Double(x, y);
	}

	public SequencePoint translate(Point2D point)
	{
		SequencePoint seqPoint = null;

		if (point != null)
		{
			double x = point.getX();
			double y = -point.getY();// accounts for mirrored y-coords

			Polar polarPoint = Polar.createPolar(x, y);

			double angleLength = calculateAngleFromZeroAngle(polarPoint.getTheta());

			double baseLength = angleLength / this.anglePerBase;
			double heightFromBackbone = polarPoint.getRadius() - this.backboneRadius;

			seqPoint = new SequencePointImp(baseLength, heightFromBackbone);
		}
		return seqPoint;
	}

	// translation at a particular scale
	private Point2D translateAt(double base, double heightFromBackbone, double scale)
	{
		// assumes that the centre of the map is point (0,0)

		// position of the point in polar coords
		double radius;
		double backboneRadius = scale*this.initialRadius;
		double angle;

		// position of the point
		double x;
		double y;

		double angleFromZeroAngle; // the angle relative to the zeroAngle

		angleFromZeroAngle = base * this.anglePerBase;

		angle = this.zeroAngle - angleFromZeroAngle;
		radius = backboneRadius + heightFromBackbone;

		x = radius * Math.cos(angle);
		y = -radius * Math.sin(angle); // negative to account for y-coords being opposite

		return new Point2D.Double(x, y);
	}

	/**
	 * Converts the passed angle (in radians) to a base value.
	 * 
	 * @param radians
	 *            The angle to convert.
	 * @return The base value on the sequence with the angle corresponding to the passed angle.
	 */
	private double angleToBase(double radians)
	{
		radians = Polar.thetaToStandardRange(radians);
		double clockwiseAngleFromZeroAngle = calculateAngleFromZeroAngle(radians);

		return clockwiseAngleFromZeroAngle / this.anglePerBase;
	}

	public SequenceRectangle getSpannedRectangle(Rectangle2D rectangle)
	{
		SequenceRectangle spannedRectangle = null;

		// TODO finish this
		if (rectangle != null)
		{
			// TODO what about (rare) case of one corner being on the point (0,0)?
			// negative y to account for switched y direction
			Point2D topLeft = new Point2D.Double(rectangle.getX(), -rectangle.getY());
			Point2D topRight = new Point2D.Double(rectangle.getX() + rectangle.getWidth(), -rectangle.getY());
			Point2D bottomLeft = new Point2D.Double(rectangle.getX(), -rectangle.getY() - rectangle.getHeight());
			Point2D bottomRight = new Point2D.Double(rectangle.getX() + rectangle.getWidth(), -rectangle.getY() - rectangle.getHeight());

			double leftBase;
			double rightBase;

			SequencePoint closestHeightPoint;
			SequencePoint furthestHeightPoint;

			double closestHeight;
			double furthestHeight;

			// if we have 0 width or 0 height, then we don't cover any area
			if (rectangle.getWidth() <= 0 || rectangle.getHeight() <= 0)
			{
				leftBase = 0;
				rightBase = 0;
				closestHeight = 0;
				furthestHeight = 0;
			}
			// if rectangle above y=centerY
			else if (bottomLeft.getY() >= this.centerPoint.getY())
			{
				Polar leftBasePolar;
				Polar rightBasePolar;

				// if left side of rectangle is right of x = centerX
				if (topLeft.getX() >= this.centerPoint.getX())
				{
					leftBasePolar = Polar.createPolar(topLeft);
					rightBasePolar = Polar.createPolar(bottomRight);
				}
				// if right side of rectangle is left of x = centerX
				else if (topRight.getX() <= this.centerPoint.getX())
				{
					leftBasePolar = Polar.createPolar(bottomLeft);
					rightBasePolar = Polar.createPolar(topRight);
				}
				// in between x = centerX
				else
				{
					leftBasePolar = Polar.createPolar(bottomLeft);
					rightBasePolar = Polar.createPolar(bottomRight);
				}

				leftBase = angleToBase(leftBasePolar.getTheta());
				rightBase = angleToBase(rightBasePolar.getTheta());

				Point2D closestPoint = Geometry.findClosestOnRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle
						.getHeight(), this.centerPoint.getX(), this.centerPoint.getY());
				Point2D furthestPoint = Geometry.findFurthestOnRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle
						.getHeight(), this.centerPoint.getX(), this.centerPoint.getY());

				closestHeightPoint = translate(closestPoint);
				furthestHeightPoint = translate(furthestPoint);

				closestHeight = closestHeightPoint.getHeightFromBackbone();
				furthestHeight = furthestHeightPoint.getHeightFromBackbone();
			}
			// if rectangle below y = centerY
			else if (topLeft.getY() <= this.centerPoint.getY())
			{
				Polar leftBasePolar;
				Polar rightBasePolar;

				// if left side of rectangle is right of x = centerX
				if (topLeft.getX() >= this.centerPoint.getX())
				{
					leftBasePolar = Polar.createPolar(topRight);
					rightBasePolar = Polar.createPolar(bottomLeft);
				}
				// if right side of rectangle is left of x = centerX
				else if (topRight.getX() <= this.centerPoint.getX())
				{
					leftBasePolar = Polar.createPolar(bottomRight);
					rightBasePolar = Polar.createPolar(topLeft);
				}
				// in between x = centerX
				else
				{
					leftBasePolar = Polar.createPolar(topRight);
					rightBasePolar = Polar.createPolar(topLeft);
				}

				leftBase = angleToBase(leftBasePolar.getTheta());
				rightBase = angleToBase(rightBasePolar.getTheta());

				Point2D closestPoint = Geometry.findClosestOnRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle
						.getHeight(), this.centerPoint.getX(), this.centerPoint.getY());
				Point2D furthestPoint = Geometry.findFurthestOnRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle
						.getHeight(), this.centerPoint.getX(), this.centerPoint.getY());

				closestHeightPoint = translate(closestPoint);
				furthestHeightPoint = translate(furthestPoint);

				closestHeight = closestHeightPoint.getHeightFromBackbone();
				furthestHeight = furthestHeightPoint.getHeightFromBackbone();
			}
			// rectangle spans y = centerY
			else
			{
				Polar leftBasePolar;
				Polar rightBasePolar;

				// if left side of rectangle is right of x = centerX
				if (topLeft.getX() >= this.centerPoint.getX())
				{
					leftBasePolar = Polar.createPolar(topLeft);
					rightBasePolar = Polar.createPolar(bottomLeft);

					leftBase = angleToBase(leftBasePolar.getTheta());
					rightBase = angleToBase(rightBasePolar.getTheta());

					Point2D closestPoint = Geometry.findClosestOnRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle
							.getHeight(), this.centerPoint.getX(), this.centerPoint.getY());
					Point2D furthestPoint = Geometry.findFurthestOnRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle
							.getHeight(), this.centerPoint.getX(), this.centerPoint.getY());

					closestHeightPoint = translate(closestPoint);
					furthestHeightPoint = translate(furthestPoint);

					closestHeight = closestHeightPoint.getHeightFromBackbone();
					furthestHeight = furthestHeightPoint.getHeightFromBackbone();
				}
				// if right side of rectangle is left of x = centerX
				else if (topRight.getX() <= this.centerPoint.getX())
				{
					leftBasePolar = Polar.createPolar(bottomRight);
					rightBasePolar = Polar.createPolar(topRight);

					leftBase = angleToBase(leftBasePolar.getTheta());
					rightBase = angleToBase(rightBasePolar.getTheta());

					Point2D closestPoint = Geometry.findClosestOnRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle
							.getHeight(), this.centerPoint.getX(), this.centerPoint.getY());
					Point2D furthestPoint = Geometry.findFurthestOnRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle
							.getHeight(), this.centerPoint.getX(), this.centerPoint.getY());

					closestHeightPoint = translate(closestPoint);
					furthestHeightPoint = translate(furthestPoint);

					closestHeight = closestHeightPoint.getHeightFromBackbone();
					furthestHeight = furthestHeightPoint.getHeightFromBackbone();
				}
				// in between x = centerX
				else
				{
					leftBase = 0;
					rightBase = this.locationConverter.getSequenceLength();

					Point2D furthestPoint = Geometry.findFurthestOnRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle
							.getHeight(), this.centerPoint.getX(), this.centerPoint.getY());

					furthestHeightPoint = translate(furthestPoint);

					closestHeight = -this.initialRadius;
					furthestHeight = furthestHeightPoint.getHeightFromBackbone();
				}
			}

			// TODO what about if we wish to change direction of bases (clockwise to counter
			// clockwise)?
			spannedRectangle = new SequenceRectangle(leftBase, rightBase, closestHeight, furthestHeight);
		}

		return spannedRectangle;
	}

	/**
	 * Calculates the difference from the passed angle to the zero angle (in the clockwise
	 * direction).
	 * 
	 * @param angle
	 *            The angle to calculate the difference from.
	 * @return The difference between the two angles (clockwise).
	 */
	private double calculateAngleFromZeroAngle(double angle)
	{
		double delta = Math.abs(angle - this.zeroAngle);

		if (angle > this.zeroAngle)
		{
			delta = 2 * Math.PI - delta;
		}

		return delta;
	}
	
	public double shiftOutsideCenterPoint(double heightLimit, SequencePoint pinnedPoint, double width, double height)
	{
		return shiftOutsideCenterPoint(this.scale, this.scale*this.initialRadius+heightLimit, pinnedPoint, width, height);
	}
	
	/**
	 * Determines shift on the defined rectangle until it is centered at seq-point(b,h+delta)
	 *   where delta is a shift necessary to make sure the rectangle does not overlap center point
	 *   (and is outside of heightLimitFromBackbone).
	 *   This is used to setup the initial condition for the calculateClashShiftAt method.
	 * @param scale  The current backbone scale
	 * @param pinnedPoint  The pinnedPoint of the rectangle.
	 * @param width  The width of the rectangle.
	 * @param height  The height of the rectangle.
	 * @return  The shift value to apply to the height of the point.
	 */
	private double shiftOutsideCenterPoint(double scale, double radiusLimit, SequencePoint pinnedPoint, double width, double height)
	{
		double shift = 0;
		
		double boundingRadius = Math.sqrt((width*width/4) + (height*height/4)); // length from center of rectangle to a diagonal
		double centerDistanceAtZero = this.initialRadius*scale;
		
		shift = (radiusLimit + boundingRadius) - pinnedPoint.getHeightFromBackbone() - centerDistanceAtZero;
		
		return shift;
	}
	
	private double calculateClashShiftAt(double scale, SequencePoint pinnedPoint, double heightLimitFromBackbone, double width, double height,
			Backbone.ShiftDirection direction)  throws ClashShiftException
	{
		double shiftHeight = 0;

		if (pinnedPoint != null)
		{
			double radiusLimit = Math.abs(heightLimitFromBackbone+this.initialRadius*scale);
			
			// if rectangle is shifted by this amount, will be located at seq-point(b,h+delta)
			double initialShiftHeight = shiftOutsideCenterPoint(scale, radiusLimit, pinnedPoint, width, height);
			double shiftedRadius = this.initialRadius*scale + pinnedPoint.getHeightFromBackbone() + initialShiftHeight;
			if (shiftedRadius < radiusLimit)
			{
				throw new ClashShiftException("shiftOutsideCenterPoint failed, height=" 
						+ shiftedRadius + " < " + radiusLimit);
			}
			
			Point2D rectangleCenter = translateAt(pinnedPoint.getBase(), pinnedPoint.getHeightFromBackbone() + initialShiftHeight, scale);
			DisplacementSolution displacement;

			// negative to account for y-axis negative going up
			double topLeftX = rectangleCenter.getX() - width / 2;
			double topLeftY = rectangleCenter.getY() - height / 2;

			Rectangle2D rectangle = new Rectangle2D.Double(topLeftX, topLeftY, width, height);

			// rectangle does not overlap center of circle
			// should not happen due to above shift
			if (rectangle.contains(this.centerPoint))
			{
				throw new ClashShiftException("Rectangle defined by (b,ht,w,h) = (" + pinnedPoint.getBase()
						+ "," + pinnedPoint.getHeightFromBackbone() + "," + width + "," + height + ") overlaps center point");
			}
			else
			{
				Point2D extremePoint;
				
				if (direction == ShiftDirection.ABOVE)
				{
					extremePoint = Geometry.findClosestOnRectangle(topLeftX, topLeftY, width, height, this.centerPoint.getX(), this.centerPoint.getY());
				}
				else
				{
					// finds point on rectangle which is closest to the center
					extremePoint = Geometry.findFurthestOnRectangle(topLeftX, topLeftY, width, height, this.centerPoint.getX(), this.centerPoint
							.getY());
				}
				
				displacement = Geometry.findDisplacementToIntersection(extremePoint, rectangleCenter, this.centerPoint, radiusLimit);
				if (displacement == null)
					throw new ClashShiftException("No shift, displacement is null");
				else
				{
					// both solutions should not be negative
					if (displacement.getSolution1() <= 0 && displacement.getSolution2() <= 0)
					{
						shiftHeight = Math.max(displacement.getSolution1(), displacement.getSolution2()) + initialShiftHeight;
					}
					else
					{
						throw new ClashShiftException("Not both displacement solutions are negative, sol = " + displacement);
					}
				}
			}
		}

		return shiftHeight;
	}

	public double calculateClashShift(SequencePoint pinnedPoint, double heightLimitFromBackbone, double width, double height,
			Backbone.ShiftDirection direction) throws ClashShiftException
	{
		return this.calculateClashShiftAt(scale, pinnedPoint, heightLimitFromBackbone, width, height, direction);
	}
	
	/**
	 * Calculates the scale at which a rectangle with the given bounds and centre is just outside the overlap radius.
	 * @param bounds
	 * @param pinnedPoint
	 * @param overlapRadius
	 * @return  The scale at which the defined rectangle is just outside the overlap radius.
	 */
	public double calculateScaleOverlap(Dimension2D bounds, SequencePoint pinnedPoint, double overlapRadius)
	{
		double scale = getMinScale();
		
		if (overlapRadius < 0)
		{
			throw new IllegalArgumentException("overlap radius is not positive");
		}
		
		try
		{
			double heightLimitFromBackbone = overlapRadius - initialRadius;
			double shiftRadius = calculateClashShiftAt(1.0, pinnedPoint, heightLimitFromBackbone,
					bounds.getWidth(), bounds.getHeight(), ShiftDirection.ABOVE);
			
			scale = (initialRadius + shiftRadius)/initialRadius;
			
			scale = Math.max(getMinScale(), scale);
		}
		catch (ClashShiftException e)
		{
		}

		return scale;
	}

	public static class ClashShiftException extends Exception
	{
		private static final long serialVersionUID = -5440912255869949007L;

		public ClashShiftException(String message)
		{
			super(message);
		}
	}

	@Override
	public double calculateIntersectionScale(SequencePoint pinnedR1, Dimension2D sizeR1,
			SequencePoint pinnedR2, Dimension2D sizeR2)
	{
		double intersectionScale = -1.0;

		double w = (sizeR1.getWidth() + sizeR2.getWidth())/2;
		double h = (sizeR1.getHeight() + sizeR2.getHeight())/2;

		Point2D zeroHeight1 = translateAt(pinnedR1.getBase(), 0, 1);
		Point2D zeroHeight2 = translateAt(pinnedR2.getBase(), 0, 1);

		double x1 = zeroHeight1.getX();
		double x2 = zeroHeight2.getX();

		// negative due to negative y-coords in java
		double y1 = -zeroHeight1.getY();
		double y2 = -zeroHeight2.getY();

		double deltaX = x1 - x2;
		double deltaY = y1 - y2;

		double h1x = pinnedR1.getHeightFromBackbone()*Math.cos(this.zeroAngle - pinnedR1.getBase()*this.anglePerBase);
		double h2x = pinnedR2.getHeightFromBackbone()*Math.cos(this.zeroAngle - pinnedR2.getBase()*this.anglePerBase);
		double h1y = pinnedR1.getHeightFromBackbone()*Math.sin(this.zeroAngle - pinnedR1.getBase()*this.anglePerBase);
		double h2y = pinnedR2.getHeightFromBackbone()*Math.sin(this.zeroAngle - pinnedR2.getBase()*this.anglePerBase);

		double deltaZeroHeightX = h1x - h2x;
		double deltaZeroHeightY = h1y - h2y;

		double scaleWLeft, scaleWRight, scaleHLeft, scaleHRight;

		if (Util.isEqual(deltaX, 0) && Util.isEqual(deltaY, 0))
		{
			intersectionScale = getMaxScale();
		}
		else if (Util.isEqual(deltaX, 0))
		{
			if (deltaY > 0)
			{
				scaleHLeft = (-h - deltaZeroHeightY)/deltaY;
				scaleHRight = (h - deltaZeroHeightY)/deltaY;
			}
			else
			{
				scaleHLeft = (h - deltaZeroHeightY)/deltaY;
				scaleHRight = (-h - deltaZeroHeightY)/deltaY;
			}

			if (scaleHLeft <= scaleHRight)
			{
				intersectionScale = scaleHRight;
			}
			else
			{
				intersectionScale = getMaxScale(); // all solutions overlap
			}
		}
		else if (Util.isEqual(deltaY, 0))
		{
			if (deltaX > 0)
			{
				scaleWLeft = (-w - deltaZeroHeightX)/deltaX;
				scaleWRight = (w - deltaZeroHeightX)/deltaX;
			}
			else
			{
				scaleWLeft = (w - deltaZeroHeightX)/deltaX;
				scaleWRight = (-w - deltaZeroHeightX)/deltaX;
			}

			if (scaleWLeft <= scaleWRight)
			{
				intersectionScale = scaleWRight;
			}
			else
			{
				intersectionScale = getMaxScale(); // all solutions overlap
			}
		}
		else
		{
			if (deltaX > 0 && deltaY > 0)
			{
				scaleWLeft = (-w - deltaZeroHeightX)/deltaX;
				scaleWRight = (w - deltaZeroHeightX)/deltaX;
				scaleHLeft = (-h - deltaZeroHeightY)/deltaY;
				scaleHRight = (h - deltaZeroHeightY)/deltaY;

			}
			else if (deltaX > 0 && deltaY < 0)
			{
				scaleWLeft = (-w - deltaZeroHeightX)/deltaX;
				scaleWRight = (w - deltaZeroHeightX)/deltaX;
				scaleHLeft = (h - deltaZeroHeightY)/deltaY;
				scaleHRight = (-h - deltaZeroHeightY)/deltaY;
			}
			else if (deltaX < 0 && deltaY > 0)
			{
				scaleWLeft = (w - deltaZeroHeightX)/deltaX;
				scaleWRight = (-w - deltaZeroHeightX)/deltaX;
				scaleHLeft = (-h - deltaZeroHeightY)/deltaY;
				scaleHRight = (h - deltaZeroHeightY)/deltaY;
			}
			else
			{
				scaleWLeft = (w - deltaZeroHeightX)/deltaX;
				scaleWRight = (-w - deltaZeroHeightX)/deltaX;
				scaleHLeft = (h - deltaZeroHeightY)/deltaY;
				scaleHRight = (-h - deltaZeroHeightY)/deltaY;

			}


			// solve scaleWLeft <= z <= scaleWRight and scaleHLeft <= z <= scaleHRight for "z"
			// basically, this defines two "ranges" on the number line where z must be defined ---| z  |---| z  |---
			// simply solve for where these ranges intersect

			intersectionScale = findNonNegativeMaxWithin(scaleWLeft, scaleWRight, scaleHLeft, scaleHRight);
		}

		return intersectionScale;
	}

	// find max value in intersection of [x,y] and [u,v]
	// if max is negative (or no max), then return's 0
	private double findNonNegativeMaxWithin(double x, double y, double u, double v)
	{
		double max = 0;

		// y is max of all values
		if (y >= x && y >= u && y >= v)
		{
			if (x < v) // if x less than range [u,v]
			{
				if (u <= v) // if range [u,v] has at least a single point in it
				{
					max = v;
				}
			} // else, no solution
		}
		// v is max of all values
		else if (v >= x && v >= y && v >= u)
		{
			if (u <= y) // if u less than range [x,y]
			{
				if (x <= y) // if range [x,y] has at least a single point in it
				{
					max = y;
				}
			} // else, no solution
		} // else, no solution

		return max;
	}
	
	/**
	 * Returns the angle of the circle given the difference of base pairs.
	 * 
	 * @param deltaBase The delta of the base pairs.
	 * @return The angle given the delta base pairs.
	 */
	public double getAngle(double deltaBase)
	{
		double result = deltaBase * anglePerBase;
		
		return result;
	}
	
	/**
	 * Appends the radius of the backbone to the passed height from the backbone.
	 * 
	 * @param height The height from the backbone.
	 * @return The calculated radius.
	 */
	public double getRadiusFromHeight(double height)
	{
		double result = height + backboneRadius;
		
		return result;
	}

	@Override
	public double getBackboneLengthAt(double scale)
	{
		if (scale <= 0)
		{
			throw new IllegalArgumentException("scale <= 0");
		}
		
		return 2*Math.PI*this.initialRadius*scale;
	}
}
