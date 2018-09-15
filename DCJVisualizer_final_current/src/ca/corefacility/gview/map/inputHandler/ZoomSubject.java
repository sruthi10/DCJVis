package ca.corefacility.gview.map.inputHandler;

import java.awt.geom.Point2D;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.ZoomException;
import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;
import ca.corefacility.gview.map.event.LayoutChangedEvent;
import ca.corefacility.gview.map.event.ZoomEvent;
import ca.corefacility.gview.map.event.ZoomNormalEvent;
import edu.umd.cs.piccolo.PCamera;

/**
 * Handles zooming in GView. Methods here are called in order to zoom in on the
 * GView map.
 * 
 * @author Aaron Petkau
 * 
 */
public class ZoomSubject extends GViewEventSubjectImp implements GViewEventListener
{
	private float minStretchScale = 0;
	private float maxStretchScale = Float.MAX_VALUE;

	private double minNormalScale = 1e-13;
	private double maxNormalScale = Double.MAX_VALUE;

	/**
	 * Stores the previous scale we were at.
	 */
	private double previousScale;

	// these are only placed here so that we can set centre point correctly
	// after zooming
	private Backbone backbone;
	private GViewMap gViewMap;

	/**
	 * Creates a new ZoomSubject to control zooming on the passed GViewMap.
	 */
	public ZoomSubject(GViewMap gViewMap)
	{
		if (gViewMap == null)
		{
			throw new NullPointerException("gViewMap is null");
		}

		this.backbone = null;
		this.gViewMap = gViewMap;

		previousScale = 1.0;
	}

	/**
	 * Multiplies the current zoom factor by the passed delta.
	 * 
	 * @param scaleDelta
	 * @param zoomPoint
	 */
	public Point2D zoomStretch(double scaleDelta, Point2D zoomPoint)
	{
		Point2D newViewPoint = zoomPoint;

		double newScale = previousScale * scaleDelta;

		if (minStretchScale <= newScale && newScale <= maxStretchScale)
		{
			newViewPoint = zoomToFactor(newScale, zoomPoint);
		}
		else if (newScale < minStretchScale && previousScale > minStretchScale)
		{
			newScale = minStretchScale;
			newViewPoint = zoomToFactor(newScale, zoomPoint);
		}
		else if (newScale > maxStretchScale && previousScale < maxStretchScale)
		{
			newScale = maxStretchScale;
			newViewPoint = zoomToFactor(newScale, zoomPoint);
		}

		return newViewPoint;
	}

	/**
	 * Performs a "normal" zoom (scaling every point by the same amount).
	 * 
	 * @param scaleDelta
	 *            The factor we should scale by.
	 * @param zoomPoint
	 *            The center point for the zoom.
	 * @param camera
	 *            The camera used to view the GView map.
	 */
	public void zoomNormalDelta(double scaleDelta, Point2D zoomPoint, PCamera camera)
	{
		double previousScale = camera.getViewScale();
		double newScale = previousScale * scaleDelta;

		if (newScale < minNormalScale)
		{
			scaleDelta = minNormalScale / previousScale;
		}
		if ((maxNormalScale > 0) && (newScale > maxNormalScale))
		{
			scaleDelta = maxNormalScale / previousScale;
		}

		camera.scaleViewAboutPoint(scaleDelta, zoomPoint.getX(), zoomPoint.getY());

		newScale = camera.getViewScale();

		fireEvent(new ZoomNormalEvent(this, newScale));
	}

	public void zoomNormal(double scale, Point2D zoomPoint, PCamera camera)
	{
		if (scale < minNormalScale)
		{
			scale = minNormalScale;
		}
		else if (scale > maxNormalScale)
		{
			scale = maxNormalScale;
		}

		double delta = scale / camera.getViewScale();

		zoomNormalDelta(delta, zoomPoint, camera);
	}

	/**
	 * Zooms in to the specified factor. Performs a "stretch" style of zooming.
	 * 
	 * @param zoomFactor
	 *            The scale factor to zoom to.
	 * @param zoomPoint
	 *            The point we are zooming about.
	 */
	public Point2D zoomToFactor(double zoomFactor, Point2D zoomPoint)
	{
		Point2D newViewPoint = zoomPoint;

		assert (zoomFactor > 0);
		if (zoomFactor > 0)
		{
			previousScale = zoomFactor;

			SequencePoint seqPoint = backbone.translate(zoomPoint); // save old
																	// point on
																	// backbone
			// this is used so we can restore map to previous position after
			// zoom (otherwise it shifts all over the place)

			fireEvent(new ZoomEvent(zoomFactor, zoomPoint, this));

			if (seqPoint != null)
			{
				newViewPoint = backbone.translate(seqPoint.getBase(), seqPoint.getHeightFromBackbone());
				if (newViewPoint == null) // if old point was not associated
											// with a point on backbone
				{
					newViewPoint = zoomPoint;
				}
			}

			// the amount we should shift the gViewMap by
			Point2D zoomDelta = new Point2D.Double(zoomPoint.getX() - newViewPoint.getX(), zoomPoint.getY()
					- newViewPoint.getY());

			// center gViewMap on point
			gViewMap.translate(zoomDelta);
		}

		return newViewPoint;
	}

	public double getStretchZoomFactor()
	{
		return backbone.getScale();
	}

	// listens to SlotRegionChangedEvent in order to change backbone
	public void eventOccured(GViewEvent event)
	{
		if (event instanceof LayoutChangedEvent)
		{
			this.backbone = ((LayoutChangedEvent) event).getSlotRegion().getBackbone();

			maxStretchScale = (float) backbone.getMaxScale();
			minStretchScale = (float) backbone.getMinScale();
			// should I do anything else?
			// probably set minScale depending on number of number of slots in
			// backbone, and backbone type
		}
	}

	/**
	 * Zooms in to the specified factor. Performs a "stretch" style of zooming.
	 * 
	 * @param zoomFactor
	 *            The scale factor to zoom to.
	 * @param seqPoint
	 *            The point we are zooming about (in terms of sequence
	 *            coordinates).
	 * @throws ZoomException
	 */
	public void zoomToFactor(double zoomFactor, SequencePoint seqPoint) throws ZoomException

	{
		if (seqPoint == null)
		{
			throw new IllegalArgumentException("seqPoint is null");
		}

		if (zoomFactor > backbone.getMaxScale())
		{
			throw new ZoomException("zoomFactor=" + zoomFactor + " > max zoom factor " + backbone.getMaxScale());
		}

		if (zoomFactor < backbone.getMinScale())
		{
			throw new ZoomException("zoomFactor=" + zoomFactor + " < min zoom factor " + backbone.getMinScale());
		}

		if (zoomFactor > 0)
		{
			Point2D zoomPoint;
			Point2D newViewPoint;

			previousScale = zoomFactor;

			zoomPoint = backbone.translate(seqPoint);

			fireEvent(new ZoomEvent(zoomFactor, zoomPoint, this));

			newViewPoint = backbone.translate(seqPoint.getBase(), seqPoint.getHeightFromBackbone());
			if (newViewPoint == null) // if old point was not associated with a
										// point on backbone
			{
				newViewPoint = zoomPoint;
			}

			// the amount we should shift the gViewMap by
			Point2D zoomDelta = new Point2D.Double(zoomPoint.getX() - newViewPoint.getX(), zoomPoint.getY()
					- newViewPoint.getY());

			// center gViewMap on point
			gViewMap.translate(zoomDelta);
		}
	}
}
