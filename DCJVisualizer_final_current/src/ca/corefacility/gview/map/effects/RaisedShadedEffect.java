package ca.corefacility.gview.map.effects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import ca.corefacility.gview.map.items.MapItemState;

public class RaisedShadedEffect extends AbstractAllEffectRenderer
{
	private Paint outlinePaint;
	private Stroke stroke;
	
	private AffineTransform raisedTransform;
	
	public RaisedShadedEffect()
	{
		this.outlinePaint = new Color(0,0,0,100);
		this.stroke = new BasicStroke();
		
		raisedTransform = new AffineTransform();
		raisedTransform.setToTranslation(0, 10);
	}
	
	protected void paintNormal(Shape shape, Graphics2D g, Paint paint)
	{
		g.setPaint(paint);
		g.fill(shape);
		
		g.setStroke(stroke);
		g.setPaint(outlinePaint);
		g.draw(shape);
	}

	protected void paintSelected(Shape shape, Graphics2D g, Paint paint)
	{
		g.setPaint(Color.YELLOW);
		g.fill(shape);
		
		g.setStroke(stroke);
		g.setPaint(outlinePaint);
		g.draw(shape);
	}
	
	protected void paintMouseOver(Shape shape, Graphics2D g, Paint paint)
	{
		AffineTransform previousTransform = g.getTransform();
		g.transform(raisedTransform);
		
		g.setPaint(Color.GREEN);
		g.fill(shape);
		
		g.setStroke(stroke);
		g.setPaint(outlinePaint);
		g.draw(shape);
		
		g.setTransform(previousTransform);
	}
	
	protected void paintMouseOverSelected(Shape shape, Graphics2D g, Paint paint)
	{
		AffineTransform previousTransform = g.getTransform();
		g.transform(raisedTransform);
		
		g.setPaint(Color.GREEN);
		g.fill(shape);
		
		g.setStroke(stroke);
		g.setPaint(outlinePaint);
		g.draw(shape);
		
		g.setTransform(previousTransform);
	}

	@Override
	public Shape getDrawnShape(Shape shape, MapItemState state)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((outlinePaint == null) ? 0 : outlinePaint.hashCode());
		result = prime * result
				+ ((raisedTransform == null) ? 0 : raisedTransform.hashCode());
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
		RaisedShadedEffect other = (RaisedShadedEffect) obj;
		if (outlinePaint == null)
		{
			if (other.outlinePaint != null)
				return false;
		} else if (!outlinePaint.equals(other.outlinePaint))
			return false;
		if (raisedTransform == null)
		{
			if (other.raisedTransform != null)
				return false;
		} else if (!raisedTransform.equals(other.raisedTransform))
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
