package ca.corefacility.gview.layout.prototype;


import java.awt.Shape;
import java.awt.Stroke;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;

public class StrokedBackboneShape extends BackboneShape
{
	private BackboneShape backboneShape;
	private Stroke stroke;
	
	private Shape cachedShape;

	public StrokedBackboneShape(Stroke stroke, BackboneShape backboneShape, Backbone backbone)
	{
		super(backbone);
		
		this.backboneShape = backboneShape;
		this.stroke = stroke;
		shapeChanged = true;
	}

	@Override
	protected Shape getCurrentShape()
	{
		return cachedShape;
	}

	@Override
	protected void modifyShape(Backbone backbone)
	{
		if (shapeChanged)
		{
			cachedShape = stroke.createStrokedShape(backboneShape);
			shapeChanged = false;
		}
	}
	
	@Override
	public void eventOccured(GViewEvent event)
	{
		if (event instanceof BackboneZoomEvent)
		{
			BackboneZoomEvent zoomEvent = (BackboneZoomEvent)event;
			this.backbone = zoomEvent.getBackbone(); // TODO do we need this, or should we store backbone once?
			shapeChanged = true; // do I need this if I fire event?  for now yes
			
			// send event to internal shape
			backboneShape.eventOccured(event);
		}
	}
}
