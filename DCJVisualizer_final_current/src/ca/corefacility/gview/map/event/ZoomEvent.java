package ca.corefacility.gview.map.event;

import java.awt.geom.Point2D;

/**
 * This even describes a change in the scale of the map.
 * 
 * @author Aaron Petkau
 * 
 */
public class ZoomEvent extends GViewEvent
{
	private static final long serialVersionUID = 3937631653213546088L;
	private double scale;
	private Point2D point;

	/**
	 * Creates a new zoom event.
	 * 
	 * @param scale
	 *            The scale to zoom to.
	 * @param point
	 *            The cursor point the zoom event occurred on.
	 */
	public ZoomEvent(double scale, Point2D point, Object source)
	{
		super(source);

		assert (scale > 0);
		assert (point != null);

		if (scale > 0)
		{
			this.scale = scale;
		}

		this.point = point;
	}

	/**
	 * Gets the new scale of the map.
	 * @return The new scale of the map.
	 */
	public double getScale()
	{
		return scale;
	}

	/**
	 * Gets the point the zoom event occured on.
	 * @return The point this zoom event occurred on.
	 */
	public Point2D getZoomPoint()
	{
		return point;
	}
}
