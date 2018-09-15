package ca.corefacility.gview.layout.sequence.circular;

import java.awt.geom.Point2D;

/**
 * Class of utilities for doing geometry-related operations. All methods here operate with
 * coordinates in java coordinates (that is, y goes to negative values upwards).
 * 
 * @author Aaron Petkau
 * 
 */
public class Geometry
{
	/**
	 * Finds closest point on rectangle defined by top-left = (topLeftX, topLeftY) and width/height
	 * to the point (tx, ty). Operates in java coordinates with upwards y being negative.
	 * 
	 * @param topLeftX
	 * @param topLeftY
	 * @param width
	 * @param height
	 * @param tx
	 * @param ty
	 * @return The closest point to this rectangle.
	 */
	public static Point2D findClosestOnRectangle(double topLeftX, double topLeftY, double width, double height, double tx, double ty)
	{
		double topRightX = topLeftX + width;
		double topRightY = topLeftY;
		double bottomLeftX = topLeftX;
		double bottomLeftY = topLeftY + height;
		double bottomRightX = topLeftX + width;
		double bottomRightY = topLeftY + height;

		// check all 4 lines on rectangle for closest point and take the minimum of these points
		Point2D minPoint = findClosestOnLine(topLeftX, topLeftY, topRightX, topRightY, tx, ty);
		Point2D currPoint = minPoint;
		double currDistanceSquared = (currPoint.getX() - tx) * (currPoint.getX() - tx) + (currPoint.getY() - ty) * (currPoint.getY() - ty);
		double minDistanceSquared = currDistanceSquared;

		currPoint = findClosestOnLine(topRightX, topRightY, bottomRightX, bottomRightY, tx, ty);
		currDistanceSquared = (currPoint.getX() - tx) * (currPoint.getX() - tx) + (currPoint.getY() - ty) * (currPoint.getY() - ty);
		if (currDistanceSquared < minDistanceSquared)
		{
			minPoint = currPoint;
			minDistanceSquared = currDistanceSquared;
		}

		currPoint = findClosestOnLine(bottomRightX, bottomRightY, bottomLeftX, bottomLeftY, tx, ty);
		currDistanceSquared = (currPoint.getX() - tx) * (currPoint.getX() - tx) + (currPoint.getY() - ty) * (currPoint.getY() - ty);
		if (currDistanceSquared < minDistanceSquared)
		{
			minPoint = currPoint;
			minDistanceSquared = currDistanceSquared;
		}

		currPoint = findClosestOnLine(bottomLeftX, bottomLeftY, topLeftX, topLeftY, tx, ty);
		currDistanceSquared = (currPoint.getX() - tx) * (currPoint.getX() - tx) + (currPoint.getY() - ty) * (currPoint.getY() - ty);
		if (currDistanceSquared < minDistanceSquared)
		{
			minPoint = currPoint;
			// minDistanceSquared = currDistanceSquared;
		}

		return minPoint;
	}

	// finds farthest point on rectangle from origin
	public static Point2D findFurthestOnRectangle(double topLeftX, double topLeftY, double width, double height, double tx, double ty)
	{
		double furthestPointX, furthestPointY, furthestDistanceSquared;
		double currPointX, currPointY, currDistanceSquared;

		// farthest point is one of the 4 corners, so test distance of all 4 corners

		// topLeft
		furthestPointX = topLeftX;
		furthestPointY = topLeftY;
		furthestDistanceSquared = (furthestPointX - tx) * (furthestPointX - tx) + (furthestPointY - ty) * (furthestPointY - ty);

		// topRight
		currPointX = topLeftX + width;
		currPointY = topLeftY;
		currDistanceSquared = (currPointX - tx) * (currPointX - tx) + (currPointY - ty) * (currPointY - ty);
		if (currDistanceSquared > furthestDistanceSquared)
		{
			furthestPointX = currPointX;
			furthestPointY = currPointY;
			furthestDistanceSquared = currDistanceSquared;
		}

		// bottomRight
		currPointX = topLeftX + width;
		currPointY = topLeftY + height;
		currDistanceSquared = (currPointX - tx) * (currPointX - tx) + (currPointY - ty) * (currPointY - ty);
		if (currDistanceSquared > furthestDistanceSquared)
		{
			furthestPointX = currPointX;
			furthestPointY = currPointY;
			furthestDistanceSquared = currDistanceSquared;
		}

		// bottomLeft
		currPointX = topLeftX;
		currPointY = topLeftY + height;
		currDistanceSquared = (currPointX - tx) * (currPointX - tx) + (currPointY - ty) * (currPointY - ty);
		if (currDistanceSquared > furthestDistanceSquared)
		{
			furthestPointX = currPointX;
			furthestPointY = currPointY;
			// farthestDistanceSquared = currDistanceSquared;
		}

		return new Point2D.Double(furthestPointX, furthestPointY);
	}

	/**
	 * Finds the point on the line defined from (x1,y1) to (x2,y2) that is closest to the point
	 * defined by (tx, ty). Assumes coords in java coords.
	 * 
	 * @param x1
	 *            The first x coord.
	 * @param y1
	 *            The first y coord.
	 * @param x2
	 *            The second x coord.
	 * @param y2
	 *            The second y coord.
	 * @return The minimum distanced point the line to the origin.
	 */
	public static Point2D findClosestOnLine(double x1, double y1, double x2, double y2, double tx, double ty)
	{
		Point2D minPoint = null;

		// flip y-coords since java has y-coord going negative as you go up
		y1 = -y1;
		y2 = -y2;
		ty = -ty;

		// note: equation of line from (x1,y1) to (x2,y2) is r(t) = (x1,y1) +
		// (lineParameter)*((x2,y2) - (x1,y1))
		// note: we are trying to find the closest point on the line to (tx,ty)

		double segmentLengthSquare = (y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1);
		double closestLineParameter = ((tx - x1) * (x2 - x1) + (ty - y1) * (y2 - y1)) / segmentLengthSquare;

		// defines point closest to line defined by (x1,y1) and (x2,y2)
		double xClosest = x1 + closestLineParameter * (x2 - x1);
		double yClosest = y1 + closestLineParameter * (y2 - y1);

		// used for checking if a point on the line defined by (x1,y1) and (x2,y2) lies on the line
		// segment defined by those points.
		double xmin;
		double xmax;
		double ymin;
		double ymax;

		if (x1 < x2)
		{
			xmin = x1;
			xmax = x2;
		}
		else
		{
			xmin = x2;
			xmax = x1;
		}
		if (y1 < y2)
		{
			ymin = y1;
			ymax = y2;
		}
		else
		{
			ymin = y2;
			ymax = y1;
		}

		// if point closest to line is within the line segment, it is the closest point
		if ((xmin <= xClosest) && (xClosest <= xmax) && (ymin <= yClosest) && (yClosest <= ymax))
		{
			minPoint = new Point2D.Double(xClosest, -yClosest);
		}
		// else, min distance is the distance to one of the endpoints
		else
		{
			double distancePoint1ToT = ((tx - x1) * (tx - x1) + (ty - y1) * (ty - y1));
			double distancePoint2ToT = ((tx - x2) * (tx - x2) + (ty - y2) * (ty - y2));
			if (distancePoint1ToT < distancePoint2ToT)
			{
				minPoint = new Point2D.Double(x1, -y1);
			}
			else
			{
				minPoint = new Point2D.Double(x2, -y2);
			}
		}

		return minPoint;
	}

	/**
	 * Find displacement from "point" to intersection of circle of radius r (centered at
	 * circleCenter), along vector "direction". Assumes coordinates in java coordinates (y values go
	 * negative in up direction).
	 * 
	 * @param point
	 *            The starting point from which to find the displacement.
	 * @param direction
	 *            The direction along which the displacement should be.
	 * @param circleCenter
	 *            The center of the circle to find the intersection with.
	 * @param radius
	 *            The radius of the circle centered at circleCenter.
	 * 
	 * @return The (possibly multiple) displacements from "point" to the intersection of the circle
	 *         of radius r, along vector "direction", or null if solution does not exist.
	 */
	public static DisplacementSolution findDisplacementToIntersection(Point2D point, Point2D direction, Point2D circleCenter, double radius)
	{
		// note: we solve for t in the equation: |point + t*direction - circleCenter|^2 = radius^2
		// (intersection of line with circle)
		// then we use t to solve for the correct displacement:
		// point + t*direction = intersection point, so
		// t*direction = offset from "point" to the intersection, so
		// |t*direction| (and appending on sign of t) = displacement from "point" in direction of
		// "direction" to intersection

		DisplacementSolution solution = null;

		// note: since we multiply y-values by y-values here, there's no need to change each y-value
		// to negative (to convert to cartesian coords)
		// since (-1)*(-1)=1
		double pointDotDirection = point.getX() * direction.getX() + point.getY() * direction.getY();
		double lengthPointSquare = point.getX() * point.getX() + point.getY() * point.getY();
		double lengthDirectionSquare = direction.getX() * direction.getX() + direction.getY() * direction.getY();
		double pointDotCenter = point.getX() * circleCenter.getX() + point.getY() * circleCenter.getY();
		double lengthCenterSquare = circleCenter.getX() * circleCenter.getX() + circleCenter.getY() * circleCenter.getY();
		double directionDotCenter = direction.getX() * circleCenter.getX() + direction.getY() * circleCenter.getY();

		double a = lengthDirectionSquare;
		double b = 2 * (pointDotDirection - directionDotCenter);
		double c = (lengthPointSquare + lengthCenterSquare - 2 * pointDotCenter - radius * radius);

		double discriminent = b * b - 4 * a * c;

		if (discriminent >= 0)
		{
			double t1 = (-b + Math.sqrt(discriminent)) / (2 * a);
			double t2 = (-b - Math.sqrt(discriminent)) / (2 * a);

			double xOffset = t1 * direction.getX();
			double yOffset = t1 * direction.getY();

			double displacement1 = Math.sqrt(xOffset * xOffset + yOffset * yOffset);

			xOffset = t2 * direction.getX();
			yOffset = t2 * direction.getY();

			double displacement2 = Math.sqrt(xOffset * xOffset + yOffset * yOffset);

			// take into account sign of solution
			if (t1 < 0)
			{
				displacement1 = -displacement1;
			}

			if (t2 < 0)
			{
				displacement2 = -displacement2;
			}

			solution = new DisplacementSolution(displacement1, displacement2);
		}

		return solution;
	}
}
