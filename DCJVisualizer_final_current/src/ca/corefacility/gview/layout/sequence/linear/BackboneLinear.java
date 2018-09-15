package ca.corefacility.gview.layout.sequence.linear;


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

public class BackboneLinear extends AbstractBackbone
{
	private double maxScale;

	private final double initialWidth; // actual width of backbone
	// possibly need actual height too, in order to account for when
	// the map size is too small to fit all the slots with there defined thicknesses

	private double backboneWidth; // stores actual backbone width in order to perform scaleing correctly

	private double lengthPerBase; // the actual length per one base

	private final Point2D zeroBase; // stores the location on the backbone of the zero base in actual coords
	// for all translations, we assume that the zero base is located at (0,0), then add on this point.

	/**
	 * Creates a new circular backbone.
	 * 
	 * @param locationConverter  The object used to convert from constant to actual locations.
	 * @param initialWidth  The initial width of this backbone.
	 */
	public BackboneLinear(LocationConverter locationConverter, double initialWidth)
	{
		super(locationConverter);

		if (initialWidth <= 0)
			throw new IllegalArgumentException("width=" + initialWidth + " is not positive");

		this.initialWidth = initialWidth;
		this.backboneWidth = initialWidth;

		this.zeroBase = new Point2D.Double(-this.backboneWidth/2, 0);
		this.lengthPerBase = this.backboneWidth/locationConverter.getSequenceLength();

		setMaxScale(initialWidth, locationConverter.getSequenceLength());
	}


	public double getBackboneLength()
	{
		return this.backboneWidth;
	}

	private void setMaxScale(double initialWidth, int sequenceLength)
	{
		double initalLengthPerBase = initialWidth/sequenceLength;

		this.maxScale = MAX_LENGTH_PER_BASE/initalLengthPerBase;
	}

	public double getSpannedBases(double lengthAlongBackbone)
	{
		return lengthAlongBackbone/this.lengthPerBase;
	}

	public double getMaxScale()
	{
		return this.maxScale;
	}

	public double getMinScale()
	{
		return 1.0;
	}

	@Override
	protected void setScale(double scale)
	{
		if (scale > 0 && scale <= getMaxScale())
		{
			this.scale = scale;

			this.backboneWidth = scale*this.initialWidth;
			this.zeroBase.setLocation(-this.backboneWidth/2, 0);
			this.lengthPerBase = this.backboneWidth/this.locationConverter.getSequenceLength();

			this.eventSubject.fireEvent(new BackboneZoomEvent(this));
		}
		else
			throw new IllegalArgumentException("scale=" + scale + " not in range [" + 0 + ", " + getMaxScale());
	}

	// performs a translation at a specific scale
	private Point2D translateAt(double base, double heightFromBackbone, double scale)
	{
		double backboneWidth = scale*this.initialWidth;
		double zeroBaseX = -backboneWidth/2;
		double zeroBaseY = 0;
		double lengthPerBase = backboneWidth/this.locationConverter.getSequenceLength();

		double x = zeroBaseX + base*lengthPerBase;
		double y = zeroBaseY - heightFromBackbone; // negative to account for  y-coords being opposite

		return new Point2D.Double(x,y);
	}

	public Point2D translate(double base, double heightFromBackbone)
	{
		double x = this.zeroBase.getX() + base*this.lengthPerBase;
		double y = this.zeroBase.getY() - heightFromBackbone; // negative to account for  y-coords being opposite

		return new Point2D.Double(x,y);
	}

	public SequencePoint translate(Point2D point)
	{
		SequencePoint seqPoint = null;

		if (point != null)
		{
			double length = point.getX() - this.zeroBase.getX();
			double base = length/this.lengthPerBase;

			double heightFromBackbone = -point.getY(); // negative to account for  y-coords being opposite

			seqPoint = new SequencePointImp(base, heightFromBackbone);
		}

		return seqPoint;
	}


	// TODO not sure if I need this anymore
	public SequenceRectangle getSpannedRectangle(Rectangle2D rectangle)
	{
		SequenceRectangle spannedRectangle = null;

		if (rectangle != null)
		{
			Point2D topLeft = new Point2D.Double(rectangle.getX(), rectangle.getY());
			Point2D bottomRight = new Point2D.Double(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight());

			SequencePoint seqTopLeft = translate(topLeft);
			SequencePoint seqBottomRight = translate(bottomRight);

			spannedRectangle = new SequenceRectangle(seqTopLeft.getBase(), seqBottomRight.getBase(),
					seqBottomRight.getHeightFromBackbone(), seqTopLeft.getHeightFromBackbone());
		}

		return spannedRectangle;
	}

	public double findZoomForLength(double deltaBase, double length)
	{
		if (deltaBase != 0)
			return length*this.locationConverter.getSequenceLength()/(deltaBase*this.initialWidth);

		return 0;
	}

	public double calculateClashShift(SequencePoint pinnedPoint, double heightLimitFromBackbone, double width, double height, Backbone.ShiftDirection direction)
	{
		double shiftHeight = 0;

		if (pinnedPoint != null)
		{
			Point2D centerPoint = translate(pinnedPoint.getBase(), pinnedPoint.getHeightFromBackbone());

			// negative centerPoint to account for java coordinate system going upwards negative
			double topHeightFromBackbone = -centerPoint.getY() + height/2;
			double bottomHeightFromBackbone = -centerPoint.getY() - height/2;

			if (direction == Backbone.ShiftDirection.ABOVE)
			{
				shiftHeight = heightLimitFromBackbone - bottomHeightFromBackbone;
			}
			else if (direction == Backbone.ShiftDirection.BELOW)
			{
				shiftHeight = heightLimitFromBackbone - topHeightFromBackbone;
			}
		}

		return shiftHeight;
	}


	@Override
	public double calculateIntersectionScale(SequencePoint pinnedR1, Dimension2D sizeR1,
			SequencePoint pinnedR2, Dimension2D sizeR2)
	{	// what if starBase > endBase?
		// assumes pinned points are center of rectangle
		// at scale 1.0

		double scale = -1; // scale value we are searching for

		if (pinnedR1.getBase() > pinnedR2.getBase()) // if r1 to right of r2, swap values
		{
			SequencePoint temp = pinnedR1;
			Dimension2D tempRect = sizeR1;

			pinnedR1 = pinnedR2;
			sizeR1 = sizeR2;

			pinnedR2 = temp;
			sizeR2 = tempRect;
		}

		Point2D translatedR1Center = translateAt(pinnedR1.getBase(), pinnedR1.getHeightFromBackbone(), 1.0);

		Point2D translatedR2Center = translateAt(pinnedR2.getBase(), pinnedR2.getHeightFromBackbone(), 1.0);

		double leftR1X = translatedR1Center.getX() - sizeR1.getWidth()/2;
		double leftR2X = translatedR2Center.getX() - sizeR2.getWidth()/2;

		double topR1Y = translatedR1Center.getY() + sizeR1.getHeight()/2;
		double topR2Y = translatedR2Center.getY() + sizeR2.getHeight()/2;

		double bottomR1Y = translatedR1Center.getY() - sizeR1.getHeight()/2;
		double bottomR2Y = translatedR2Center.getY() - sizeR2.getHeight()/2;

		if (topR1Y >= topR2Y) // if R1 "above" R2
		{
			if (bottomR1Y >= topR2Y)
				// rectangles never overlap
				return 0.0;
		}
		else
		{
			if (topR1Y <= bottomR2Y)
				// rectangles never overlap
				return 0.0;
		}

		// else, rectangles are at correct heights to overlap, now check at which scale would they overlap.

		if (pinnedR1.getBase() < pinnedR2.getBase()) // either values are setup like this now, or they are equal
		{
			// let theta(b, x, s) = value in map coords of the translated "b" = base value, x = offset from base value (in map coords), s = scale
			//	(see figure 1).
			//
			// Then in this case, we want to know for which "s" do the two rectangles overlap.
			// That is for which "s" is the right side of R1 passed the left side of R2.
			//  (see figure 2).
			// That is for which "s" is theta(b1, xr1, s) >= theta(b2, xl2, s), where b1 = base of rectangle 1, x1 = offset from b1 to right of R1
			//	s = scale
			// The below code solves this for "s".
			//
			//  Figure 1:
			//            _____
			//           [     ] = x = distance (in map coords) from translated "b" value, and edge of rectangle (in this case the right edge)
			//       ----------
			//       |    *   |
			//       ----------
			//            * = b = base that rectangle is "pinned" to
			//
			//
			//  Figure 2:
			//       left R2
			//       |
			//	     ---------------
			//	-----+---right R1  |
			//  |    |  |          |
			//  -----+---          |
			//       ---------------
			if (leftR1X < leftR2X)
			{
				double offsetR1Right = sizeR1.getWidth()/2;
				double offsetR2Left = -sizeR2.getWidth()/2;

				double differenceOffset = offsetR1Right - offsetR2Left;

				double initialBasePerLength = this.locationConverter.getSequenceLength()/this.initialWidth;
				double pinnedBaseDifference = pinnedR2.getBase() - pinnedR1.getBase();

				double offsetDiffOverBaseDiff = differenceOffset/pinnedBaseDifference;

				scale = initialBasePerLength*offsetDiffOverBaseDiff;
			}
			// swap values from above and solve same equation (rectangles are simply reversed)
			// basically, in this case we want to solve for scale where right R2 is passed left R1
			//
			//       left R1
			//       |
			//	     ---------------
			//	-----+---right R2  |
			//  |    |  |          |
			//  -----+---          |
			//       ---------------
			else
			{
				double offsetR2Right = sizeR2.getWidth()/2;
				double offsetR1Left = -sizeR1.getWidth()/2;

				double differenceOffset = offsetR2Right - offsetR1Left;

				double initialBasePerLength = this.locationConverter.getSequenceLength()/this.initialWidth;
				double pinnedBaseDifference = pinnedR2.getBase() - pinnedR1.getBase();

				double offsetDiffOverBaseDiff = differenceOffset/pinnedBaseDifference;

				scale = initialBasePerLength*offsetDiffOverBaseDiff;
			}
		}
		else
		{
			scale = getMaxScale(); // rectangles centered on same point, so there is no scale where they don't overlap anymore
		}

		return scale;
	}


	@Override
	public double getBackboneLengthAt(double scale)
	{
		if (scale <= 0)
		{
			throw new IllegalArgumentException("scale <= 0");
		}

		return scale*this.initialWidth;
	}
}
