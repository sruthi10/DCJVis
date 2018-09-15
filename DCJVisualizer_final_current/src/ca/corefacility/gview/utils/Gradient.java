package ca.corefacility.gview.utils;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

public class Gradient implements Paint
{
	private Color c1;
	private Color c2;
	
	public Gradient(Color c1, Color c2)
	{
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public Color getColor1()
	{
		return c1;
	}
	
	public Color getColor2()
	{
		return c2;
	}
	
	public GradientPaint getGradientPaint(Shape s)
	{
		Rectangle2D b = s.getBounds2D();
		float leftX = (float)b.getX();
		float leftY = (float)b.getY();
		
		float rightX = (float)(leftX + b.getWidth());
		float rightY = leftY;
		
		return new GradientPaint(leftX, leftY, c1, rightX, rightY, c2);
	}

	@Override
	public int getTransparency()
	{
        int a1 = c1.getAlpha();
        int a2 = c2.getAlpha();
        return (((a1 & a2) == 0xff) ? OPAQUE : TRANSLUCENT);
	}

	@Override
	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
			Rectangle2D userBounds, AffineTransform xform, RenderingHints hints)
	{		
		GradientPaint grad = getGradientPaint(userBounds);
		
		return grad.createContext(cm, deviceBounds, userBounds, xform, hints);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((c1 == null) ? 0 : c1.hashCode());
		result = prime * result + ((c2 == null) ? 0 : c2.hashCode());
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
		Gradient other = (Gradient) obj;
		if (c1 == null)
		{
			if (other.c1 != null)
				return false;
		} else if (!c1.equals(other.c1))
			return false;
		if (c2 == null)
		{
			if (other.c2 != null)
				return false;
		} else if (!c2.equals(other.c2))
			return false;
		return true;
	}
}
