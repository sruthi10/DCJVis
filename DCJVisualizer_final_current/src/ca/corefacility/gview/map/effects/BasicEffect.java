package ca.corefacility.gview.map.effects;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import ca.corefacility.gview.map.items.MapItemState;

public class BasicEffect extends AbstractAllEffectRenderer
{
	private static final Stroke outline = new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	protected void paintNormal(Shape shape, Graphics2D g, Paint paint)
	{
		g.setPaint(paint);
		g.fill(shape);
	}

	protected void paintSelected(Shape shape, Graphics2D g, Paint paint)
	{
		paintMouseOver(shape,g,paint);
	}

	protected void paintMouseOver(Shape shape, Graphics2D g, Paint paint)
	{		
		g.setPaint(paint);
		g.fill(shape);
		
		Stroke temp = g.getStroke();
		Composite tempComposite = g.getComposite();
		
		g.setComposite(AlphaComposite.Src);
		g.setStroke(outline);
		g.setPaint(getOutlineColor(paint));
		g.draw(shape);
		
		g.setComposite(tempComposite);
		g.setStroke(temp);
	}
	
	protected void paintMouseOverSelected(Shape shape, Graphics2D g, Paint paint)
	{
		paintMouseOver(shape,g,paint);
	}

	@Override
	public Shape getDrawnShape(Shape shape, MapItemState state)
	{
		if (state.isSelected() || state.isMouseOver())
		{
			return outline.createStrokedShape(shape.getBounds2D());
		}
		else
		{
			return shape;
		}
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
