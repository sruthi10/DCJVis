package ca.corefacility.gview.map.effects;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import ca.corefacility.gview.map.items.MapItemState;

public class StandardEffectSelect implements ShapeEffectRenderer
{
	private static final Stroke outline = new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	public void paint(Shape shape, Graphics2D g, Paint paint, MapItemState state)
	{
		if (paint != null)
		{
			if (state.isSelected())
			{
				g.setPaint(paint);
				g.fill(shape);
				
				Stroke prevStroke = g.getStroke();
				g.setStroke(outline);
				g.setPaint(Color.YELLOW);
				g.draw(shape);
				g.setStroke(prevStroke);
			}
			else
			{
				g.setPaint(paint);
				g.fill(shape);
			}
		}
	}

	public boolean paintChanged(MapItemState previousState,
			MapItemState nextState)
	{
		return (previousState.isSelected() && !nextState.isSelected()) || (!previousState.isSelected() && nextState.isSelected());
	}

	@Override
	public Shape getDrawnShape(Shape shape, MapItemState state)
	{
		return outline.createStrokedShape(shape);
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
