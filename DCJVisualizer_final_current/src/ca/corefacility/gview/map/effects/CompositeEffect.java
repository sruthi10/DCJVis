package ca.corefacility.gview.map.effects;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.LinkedList;
import java.util.List;

import ca.corefacility.gview.map.items.MapItemState;

public class CompositeEffect implements ShapeEffectRenderer
{
	private List<ShapeEffectRenderer> effects = new LinkedList<ShapeEffectRenderer>();

	public void addEffect(ShapeEffectRenderer effect)
	{
		if (effect != null)
		{
			effects.add(effect);
		}
	}
	
	@Override
	public void paint(Shape shape, Graphics2D g, Paint paint, MapItemState state)
	{
		for (ShapeEffectRenderer effectRenderer : effects)
		{
			effectRenderer.paint(shape, g, paint, state);
		}
	}

	@Override
	public boolean paintChanged(MapItemState previousState,
			MapItemState nextState)
	{
		boolean changed = false;
		
		for (ShapeEffectRenderer effectRenderer : effects)
		{
			changed |= effectRenderer.paintChanged(previousState, nextState);
		}
		
		return changed;
	}

	@Override
	public Shape getDrawnShape(Shape shape, MapItemState state)
	{
		Area currArea = new Area();
		
		for (ShapeEffectRenderer effectRenderer : effects)
		{
			currArea.add(new Area(effectRenderer.getDrawnShape(shape, state)));
		}
		
		return currArea;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((effects == null) ? 0 : effects.hashCode());
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
		CompositeEffect other = (CompositeEffect) obj;
		if (effects == null)
		{
			if (other.effects != null)
				return false;
		} else if (!effects.equals(other.effects))
			return false;
		return true;
	}
}
