package ca.corefacility.gview.map.effects;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import ca.corefacility.gview.map.items.MapItemState;

public class OutlineEffect extends AbstractAllEffectRenderer
{
	private static final BasicStroke stroke = new BasicStroke(0.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	protected void paintNormal(Shape shape, Graphics2D g, Paint paint)
	{
		Stroke prevStroke = g.getStroke();
		g.setStroke(stroke);
		g.setPaint(paint);
		g.draw(shape);
		g.setStroke(prevStroke);
	}

	protected void paintSelected(Shape shape, Graphics2D g, Paint paint)
	{
		paintMouseOver(shape,g,paint);
	}
	
	protected void paintMouseOver(Shape shape, Graphics2D g, Paint paint)
	{
		Stroke temp = g.getStroke();
		Composite tempComposite = g.getComposite();
		
		g.setComposite(AlphaComposite.Src);
		g.setStroke(stroke);
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
