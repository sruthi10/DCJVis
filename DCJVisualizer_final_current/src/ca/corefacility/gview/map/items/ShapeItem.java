package ca.corefacility.gview.map.items;


import java.awt.Shape;

import ca.corefacility.gview.map.effects.ShapeEffectRenderer;

/**
 * Describes an item on the map which has a shape.
 * 
 * @author Aaron Petkau
 *
 */
public interface ShapeItem extends MapItem // TODO probably have 2 diff shapes, BackboneShape, and Shapes
{
	/**
	 * Sets the shape of this item.
	 * @param shape  The shape to set.
	 */
	public void setShape(Shape shape); // TODO I want this to be Shape, but in order to handle events, it needs to be BackboneShape, change it later
	
	public Shape getShape(); // this is only done so we can get the shape to handle re-sizing
	
	public void setShapeEffectRenderer(ShapeEffectRenderer shapeEffectRenderer);
	
	// used for setting border stroke properties of the item
	//	mainly used to eliminate strokes on some items, but add them on other items (border)
	/*public void setStroke(Stroke stroke);
	public void setStrokePaint(Paint paint);*/
	// remove these for now, figure them out later
}
