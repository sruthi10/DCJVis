package ca.corefacility.gview.layout.prototype;


import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;

/**
 * Describes a shape that is layed out along the Backbone and which can stretch/shrink on zoom levels.
 * @author Aaron Petkau
 *
 */
public abstract class BackboneShape implements OrientableShape, GViewEventSubject, GViewEventListener
{
	protected boolean shapeChanged;
	protected Backbone backbone;
	
	// used to handle shapechange events
	private GViewEventSubjectImp eventSubject = new GViewEventSubjectImp();

	// TODO do I need to pass backbone here?
	/**
	 * Creates a new Backbone Shape about the passed backbone.
	 * @param backbone  The backbone about which this shape is attached.
	 */
	public BackboneShape(Backbone backbone)
	{
		assert (backbone != null);
		
		this.backbone = backbone;
		shapeChanged = false;
	}

	/**
	 * Overridden methods for Shape
	 */
	
	public boolean contains(double x, double y, double w, double h)
	{
		modifyShape(backbone);
		return getCurrentShape().contains(x,y,w,h);
	}

	
	public boolean contains(double x, double y)
	{
		modifyShape(backbone);
		return getCurrentShape().contains(x,y);
	}

	
	public boolean contains(Point2D p)
	{
		modifyShape(backbone);
		return getCurrentShape().contains(p);
	}

	
	public boolean contains(Rectangle2D r)
	{
		modifyShape(backbone);
		return getCurrentShape().contains(r);
	}

	
	public Rectangle getBounds()
	{
		modifyShape(backbone);
		return getCurrentShape().getBounds();
	}

	
	public Rectangle2D getBounds2D()
	{
		modifyShape(backbone);
		return getCurrentShape().getBounds2D();
	}

	
	public PathIterator getPathIterator(AffineTransform at, double flatness)
	{
		modifyShape(backbone);
		return getCurrentShape().getPathIterator(at, flatness);
	}

	
	public PathIterator getPathIterator(AffineTransform at)
	{
		modifyShape(backbone);
		return getCurrentShape().getPathIterator(at);
	}

	
	public boolean intersects(double x, double y, double w, double h)
	{
		modifyShape(backbone);
		return getCurrentShape().intersects(x,y,w,h);
	}

	
	public boolean intersects(Rectangle2D r)
	{
		modifyShape(backbone);
		return getCurrentShape().intersects(r);
	}

	@Override
    public Shape getRegion(SequencePath.Orientation orientation)
    {
        // TODO Auto-generated method stub
        return null;
    }
	
	public void eventOccured(GViewEvent event)
	{
		if (event instanceof BackboneZoomEvent)
		{
			BackboneZoomEvent zoomEvent = (BackboneZoomEvent)event;
			this.backbone = zoomEvent.getBackbone(); // TODO do we need this, or should we store backbone once?
			shapeChanged = true; // do I need this if I fire event?  for now yes
			
//			eventSubject.fireEvent(new ShapeChangeEvent(this));
		}
	}
	
	/**
	 * @return  The current shape in display.
	 */
	protected abstract Shape getCurrentShape();
	
	/**
	 * Modifies this shape relative to the passed backbone.
	 * 
	 * @param backbone  The backbone about which this shape is located.
	 */
	protected abstract void modifyShape(Backbone backbone);	
	
	/** Event Subject **/
	
	
	public void addEventListener(GViewEventListener listener)
	{
		eventSubject.addEventListener(listener);
	}

	
	public void removeAllEventListeners()
	{
		eventSubject.removeAllEventListeners();
	}

	
	public void removeEventListener(GViewEventListener listener)
	{
		eventSubject.removeEventListener(listener);
	}
}