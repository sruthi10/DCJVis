package ca.corefacility.gview.utils;

import java.awt.geom.Dimension2D;

/**
 * A class storing a dimension in double precision.
 * @author aaron
 *
 */
public class Dimension2DDouble extends Dimension2D
{
	double width, height;
	
	public Dimension2DDouble(double width, double height)
	{
		this.width = width;
		this.height = height;
	}

	@Override
	public double getHeight()
	{
		return height;
	}

	@Override
	public double getWidth()
	{
		return width;
	}

	@Override
	public void setSize(double width, double height)
	{
		this.width = width;
		this.height = height;
	}

	public String toString()
	{
		return "(" + width + "," + height + ")";
	}
}
