package ca.corefacility.gview.map.effects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import ca.corefacility.gview.map.items.MapItemState;

public class HighlightEffect extends AbstractAllEffectRenderer
{
	private final static Paint HIGHLIGHT_PAINT = new Color(1.0f,1.0f,1.0f,0.5f);
	private final static Stroke stroke = new BasicStroke(3.0f);
	
	protected void paintNormal(Shape shape, Graphics2D g, Paint paint)
	{
		g.setPaint(paint);
		g.fill(shape);
		
		Stroke prevStroke = g.getStroke();
		g.setStroke(stroke);
		g.setPaint(HIGHLIGHT_PAINT);
		g.draw(shape);
		g.setStroke(prevStroke);
	}

	protected void paintSelected(Shape shape, Graphics2D g, Paint paint)
	{
		g.setPaint(Color.YELLOW);
		g.fill(shape);
		
		Stroke prevStroke = g.getStroke();
		g.setStroke(stroke);
		g.setPaint(HIGHLIGHT_PAINT);
		g.draw(shape);
		g.setStroke(prevStroke);
	}
	
	protected void paintMouseOver(Shape shape, Graphics2D g, Paint paint)
	{
		g.setPaint(Color.GREEN);
		g.fill(shape);
		
		Stroke prevStroke = g.getStroke();
		g.setStroke(stroke);
		g.setPaint(HIGHLIGHT_PAINT);
		g.draw(shape);
		g.setStroke(prevStroke);
	}
	
	protected void paintMouseOverSelected(Shape shape, Graphics2D g, Paint paint)
	{
		g.setPaint(Color.GREEN);
		g.fill(shape);
		
		Stroke prevStroke = g.getStroke();
		g.setStroke(stroke);
		g.setPaint(HIGHLIGHT_PAINT);
		g.draw(shape);
		g.setStroke(prevStroke);
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
