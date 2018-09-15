package ca.corefacility.gview.map.effects;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

import ca.corefacility.gview.map.items.MapItemState;

public interface ShapeEffectRenderer
{
	public static final ShapeEffectRenderer STANDARD_RENDERER = new StandardEffectMouseOverSelect(
	        new BasicStroke(0.3f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND),new Color(0,0,0,128));
	public static final ShapeEffectRenderer BASIC_RENDERER = new BasicEffect();
	public static final ShapeEffectRenderer SELECT_RENDERER = new StandardEffectSelect();
	public static final ShapeEffectRenderer OUTLINE_RENDERER = new OutlineEffect();
	public static final ShapeEffectRenderer SHADING_RENDERER = new ShadingEffect();
	
	/**
	 * Defines how to paint out the passed shape on the passed graphics context.
	 * @param shape  The shape to paint.
	 * @param g  The graphics context to paint onto.
	 * @param paint  The paint object to use when painting.
	 * @param state The current state of the shape to render.
	 */
	public void paint(Shape shape, Graphics2D g, Paint paint, MapItemState state);
	
	/**
	 * Indicates if the paint method is changed between the two states.
	 * @param previousState
	 * @param nextState
	 * @return  True if the paint needs to be changed between the two states, false otherwise.
	 */
	public boolean paintChanged(MapItemState previousState, MapItemState nextState);
	
	public boolean equals(Object o);

	/**
	 * Obtains the shape to draw, given a state and the original shape.  Used to determine bounds of drawn shape.
	 * @param shape  The shape to examine.
	 * @param state  The bounding box for the shape.
	 * @return  The complete shape to draw.
	 */
	public Shape getDrawnShape(Shape shape, MapItemState state);
}
