package ca.corefacility.gview.map.effects;


import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import ca.corefacility.gview.map.items.MapItemState;

public class LabelLineRenderer implements ShapeEffectRenderer
{
	private static final Stroke stroke = new BasicStroke();

	public void paint(Shape shape, Graphics2D g, Paint paint, MapItemState state)
	{
		Shape line = stroke.createStrokedShape(shape);
		
		g.setPaint(paint);
		g.fill(line);
	}

	public boolean paintChanged(MapItemState previousState,
			MapItemState nextState)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Shape getDrawnShape(Shape shape, MapItemState state)
	{
		return stroke.createStrokedShape(shape);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		else if (this.getClass() != obj.getClass())
		{
			return false;
		}
		
		return true;
	}
}
