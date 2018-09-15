package ca.corefacility.gview.layout.sequence;


import java.awt.geom.Point2D;

import ca.corefacility.gview.layout.prototype.SequenceOffset;
import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;
import ca.corefacility.gview.map.event.ZoomEvent;

public abstract class AbstractBackbone implements Backbone
{
	// should I store the interface for this and add fireEvent() to interface?
	protected GViewEventSubjectImp eventSubject;

	protected double scale;

	protected LocationConverter locationConverter;

	protected static final double MAX_LENGTH_PER_BASE = 1000;

	public AbstractBackbone(LocationConverter locationConverter)
	{
		if (locationConverter == null)
			throw new IllegalArgumentException("locationConverter is null");
		else
		{
			this.locationConverter = locationConverter; // if it is null, we can't do anything

			this.eventSubject = new GViewEventSubjectImp();

			this.scale = 1.0;
		}
	}


	public Point2D translate(SequencePoint point)
	{
		if (point != null)
		{
			if (point instanceof SequenceOffset) // is this best way?  Checking if it's instance of a class?
			{
				SequenceOffset offset = (SequenceOffset)point;
				return translate(offset.getBase(), offset.getHeightFromBackbone(), offset.getLengthFromBase());
			}

			return translate(point.getBase(), point.getHeightFromBackbone());
		}
		else
			return null;
	}

	public double getScale()
	{
		return this.scale;
	}

	public Point2D translate(double base, double heightFromBackbone, double lengthFromBase)
	{
		double basePairOffset = getSpannedBases(lengthFromBase);

		return translate(base + basePairOffset, heightFromBackbone);
	}

	public void addEventListener(GViewEventListener listener)
	{
		this.eventSubject.addEventListener(listener);
	}


	public void removeAllEventListeners()
	{
		this.eventSubject.removeAllEventListeners();
	}


	public void removeEventListener(GViewEventListener listener)
	{
		this.eventSubject.removeEventListener(listener);
	}


	public void eventOccured(GViewEvent event)
	{
		if (event instanceof ZoomEvent)
		{
			ZoomEvent zoomEvent = (ZoomEvent)event;

			setScale(zoomEvent.getScale());
		}
	}

	protected abstract void setScale(double scale);
}
