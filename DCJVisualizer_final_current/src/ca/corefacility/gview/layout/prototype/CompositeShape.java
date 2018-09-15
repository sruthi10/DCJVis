package ca.corefacility.gview.layout.prototype;

import java.awt.Shape;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;


/**
 * A Backbone shape that can be composed from two different backbone shapes, and controls switching between them at certain zoom levels.
 * @author Aaron Petkau
 *
 */
public class CompositeShape extends BackboneShape
{
	/**
	 * The two shapes in the composite shape.
	 */
	private BackboneShape firstShape;
	private BackboneShape secondShape;
	
	// the value defining where we should switch from first shape to second shape
	private double zoomSwitch;
	
	/**
	 * Current shape in display.
	 */
	private BackboneShape currentShape;

	/**
	 * Creates a CompositeShape which may contain other shapes, and switches between them at the passed zoom level.
	 * 
	 * @param firstShape  The first shape to display.
	 * @param secondShape  The second shape to display.
	 * @param zoomSwitch  The zoom level where a switch should be made from the first to the second shape.
	 * @param initialZoomLevel  The initial zoom level of the map (to determine which shape should be displayed first).
	 */
	public CompositeShape(BackboneShape firstShape, BackboneShape secondShape, double zoomSwitch, double initialZoomLevel)
	{
		super(null);
		
		if (firstShape == null || secondShape == null)
		{
			throw new IllegalArgumentException("Passed BackboneShapes are null");
		}
		else if (zoomSwitch <= 0.0)
		{
			throw new IllegalArgumentException("zoomSwitch=" + zoomSwitch + " is invalid");
		}
		else
		{
			this.firstShape = firstShape;
			this.secondShape = secondShape;
			this.zoomSwitch = zoomSwitch;
			
			if (initialZoomLevel < zoomSwitch)
			{
				currentShape = firstShape;
			}
			else
			{
				currentShape = secondShape;
			}
		}
	}
	
	public void eventOccured(GViewEvent event)
	{
		// make sure in this order so that when we send ShapeChangeEvent, we've already updated the currentShape.
		currentShape.eventOccured(event);
		super.eventOccured(event);
	}
	
	protected Shape getCurrentShape()
	{
		return currentShape;
	}

	protected void modifyShape(Backbone backbone)
	{
		if (!shapeChanged) {
			return;
		}
		
		double currentScale = backbone.getScale();
		
		if (currentShape == firstShape && currentScale >= zoomSwitch)
		{
			currentShape = secondShape;
			currentShape.eventOccured(new BackboneZoomEvent(backbone)); // make sure to update with new zoom level
			// note:  we depend on Backbone being constantly updated at every zoom level (in case backbone change occured, or
			//	for placing in initial backbone) for this method to work.
		}
		else if (currentShape == secondShape && currentScale < zoomSwitch)
		{
			currentShape = firstShape;
			currentShape.eventOccured(new BackboneZoomEvent(backbone)); // make sure to update with new zoom level (maybe not best method)
		}
	}

    @Override
    public Shape getRegion(SequencePath.Orientation orientation)
    {
        if (currentShape != null)
        {
            return currentShape.getRegion(orientation);
        }
        
        return null;
    }
}
