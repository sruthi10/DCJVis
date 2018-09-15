package ca.corefacility.gview.map.effects;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import ca.corefacility.gview.map.items.MapItemState;

public class StandardEffect implements ShapeEffectRenderer
{
	private Paint outlinePaint;
	private Stroke stroke;
	
	public StandardEffect()
	{
	}
	
	public StandardEffect(Paint outlinePaint, Stroke stroke)
	{
		this.outlinePaint = outlinePaint;
		this.stroke = stroke;
	}
	
	public void paint(Shape shape, Graphics2D g, Paint paint, MapItemState state)
	{
		g.setPaint(paint);
		g.fill(shape);
		
		if (outlinePaint != null)
		{
			g.setStroke(stroke);
			g.setPaint(outlinePaint);
			g.draw(shape);
		}
	}

	public boolean paintChanged(MapItemState previousState,
			MapItemState nextState)
	{
		return false;
	}

	@Override
	public Shape getDrawnShape(Shape shape, MapItemState state)
	{
		if (stroke != null && outlinePaint != null)
		{
			return stroke.createStrokedShape(shape);
		}
		else
		{
			return shape;
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((outlinePaint == null) ? 0 : outlinePaint.hashCode());
		result = prime * result + ((stroke == null) ? 0 : stroke.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StandardEffect other = (StandardEffect) obj;
		if (outlinePaint == null)
		{
			if (other.outlinePaint != null)
				return false;
		} else if (!outlinePaint.equals(other.outlinePaint))
			return false;
		if (stroke == null)
		{
			if (other.stroke != null)
				return false;
		} else if (!stroke.equals(other.stroke))
			return false;
		return true;
	}
}
