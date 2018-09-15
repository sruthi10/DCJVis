package ca.corefacility.gview.map.effects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

import ca.corefacility.gview.map.items.MapItemState;

public abstract class AbstractAllEffectRenderer implements ShapeEffectRenderer
{
	public void paint(Shape shape, Graphics2D g, Paint paint, MapItemState state)
	{
		if (state == MapItemState.NORMAL)
		{
			paintNormal(shape, g, paint);
		}
		else if (state == MapItemState.SELECTED)
		{
			paintSelected(shape, g, paint);
		}
		else if (state == MapItemState.MOUSE_OVER)
		{
			paintMouseOver(shape, g, paint);
		}
		else if (state == MapItemState.MOUSE_OVER_SELECTED)
		{
			paintMouseOverSelected(shape, g, paint);
		}
		else if (state == MapItemState.STYLED || state == MapItemState.MOUSE_OVER_STYLED)
		{
			paintNormal(shape, g, paint);
		}
	}

	public boolean paintChanged(MapItemState previousState, MapItemState nextState)
	{
		return (previousState != nextState);
	}

	protected abstract void paintNormal(Shape shape, Graphics2D g, Paint paint);

	protected abstract void paintSelected(Shape shape, Graphics2D g, Paint paint);

	protected abstract void paintMouseOver(Shape shape, Graphics2D g, Paint paint);

	protected abstract void paintMouseOverSelected(Shape shape, Graphics2D g, Paint paint);
	
	protected Color getOutlineColor(Paint paint)
	{
		Color color = Color.RED;
		
		if(paint instanceof Color)
		{
			color = new Color(((Color) paint).getRGB());
			
			float[] hsb = new float[3];		
			Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
			
			if(hsb[2] > 0.5)
			{
				color = color.darker().darker();
			}
			else
			{
				color = color.brighter().brighter();
			}
		}
		
		return color;
	}
}
