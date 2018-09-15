package ca.corefacility.gview.map.effects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import ca.corefacility.gview.map.items.MapItemState;

public class ShadowEffect extends AbstractAllEffectRenderer
{
	private Paint shadowPaint = new Color(0,0,0,100);
	private AffineTransform offset = new AffineTransform();
	
	public ShadowEffect()
	{
		offset.setToTranslation(5, 5);
	}
	
	private void paintShadow(Shape shape, Graphics2D g)
	{
		AffineTransform old = g.getTransform();
		g.transform(offset);
		
		g.setPaint(shadowPaint);
		g.fill(shape);
		
		g.setTransform(old);
	}
	
	protected void paintNormal(Shape shape, Graphics2D g, Paint paint)
	{
		paintShadow(shape, g);
		
		g.setPaint(paint);
		g.fill(shape);
	}

	protected void paintSelected(Shape shape, Graphics2D g, Paint paint)
	{
		paintShadow(shape, g);
		
		g.setPaint(Color.YELLOW);
		g.fill(shape);
	}
	
	protected void paintMouseOver(Shape shape, Graphics2D g, Paint paint)
	{
		paintShadow(shape, g);
		
		g.setPaint(Color.GREEN);
		g.fill(shape);
	}
	
	protected void paintMouseOverSelected(Shape shape, Graphics2D g, Paint paint)
	{
		paintShadow(shape, g);
		
		g.setPaint(Color.GREEN);
		g.fill(shape);
	}

	@Override
	public Shape getDrawnShape(Shape shape, MapItemState state)
	{
		Shape shadow;
		
		shadow = offset.createTransformedShape(shape);
		Area a = new Area(shadow);
		Area b = new Area(shape);
		a.add(b);
		
		return a;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offset == null) ? 0 : offset.hashCode());
		result = prime * result
				+ ((shadowPaint == null) ? 0 : shadowPaint.hashCode());
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
		ShadowEffect other = (ShadowEffect) obj;
		if (offset == null)
		{
			if (other.offset != null)
				return false;
		} else if (!offset.equals(other.offset))
			return false;
		if (shadowPaint == null)
		{
			if (other.shadowPaint != null)
				return false;
		} else if (!shadowPaint.equals(other.shadowPaint))
			return false;
		return true;
	}
}
