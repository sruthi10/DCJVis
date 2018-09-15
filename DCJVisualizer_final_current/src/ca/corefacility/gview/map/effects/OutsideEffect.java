package ca.corefacility.gview.map.effects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import ca.corefacility.gview.map.items.MapItemState;

public class OutsideEffect extends AbstractAllEffectRenderer
{
	private Paint outlinePaint;
	private Stroke stroke;
	
	public OutsideEffect(Paint outlinePaint)
	{
		this.outlinePaint = outlinePaint;
		this.stroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	}
	
	public OutsideEffect(Paint outlinePaint, Stroke stroke)
	{
		this.outlinePaint = outlinePaint;
		this.stroke = stroke;
	}
	
	protected void paintNormal(Shape shape, Graphics2D g, Paint paint)
	{	
		g.setStroke(stroke);
		g.setPaint(outlinePaint);
		g.draw(shape);
		
		g.setPaint(paint);
		g.fill(shape);
	}

	protected void paintSelected(Shape shape, Graphics2D g, Paint paint)
	{
		g.setStroke(stroke);
		g.setPaint(outlinePaint);
		g.draw(shape);
		
		g.setPaint(Color.YELLOW);
		g.fill(shape);
	}
	
	protected void paintMouseOver(Shape shape, Graphics2D g, Paint paint)
	{
		g.setStroke(stroke);
		g.setPaint(outlinePaint);
		g.draw(shape);
		
		g.setPaint(Color.GREEN);
		g.fill(shape);
	}
	
	protected void paintMouseOverSelected(Shape shape, Graphics2D g, Paint paint)
	{		
		g.setStroke(stroke);
		g.setPaint(outlinePaint);
		g.draw(shape);
		
		g.setPaint(Color.GREEN);
		g.fill(shape);
	}

	@Override
	public Shape getDrawnShape(Shape shape, MapItemState state)
	{
		return stroke.createStrokedShape(shape);
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
		OutsideEffect other = (OutsideEffect) obj;
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
