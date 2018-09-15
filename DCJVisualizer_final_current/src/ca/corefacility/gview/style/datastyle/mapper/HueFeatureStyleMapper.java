package ca.corefacility.gview.style.datastyle.mapper;

import java.awt.Color;
import java.awt.Paint;

import ca.corefacility.gview.map.items.FeatureItem;

public class HueFeatureStyleMapper implements ContinuousStyleMapper
{
	private float lower;
	private float upper;
	
	public HueFeatureStyleMapper()
	{
		this(0.0f, 1.0f);
	}
	
	public HueFeatureStyleMapper(float lower, float upper)
	{
		if (lower > upper)
		{
			throw new IllegalArgumentException("lower > upper");
		}
		else if (lower < 0.0 || upper > 1.0)
		{
			throw new IllegalArgumentException("values are out of range of 0.0 to 1.0");
		}
		
		this.lower = lower;
		this.upper = upper;
	}

	@Override
	public void mapValueToStyle(float value, FeatureItem featureItem)
	{
		if (value < 0.0 || value > 1.0)
		{
			throw new IllegalArgumentException("value is out of range of [0.0,1.0]");
		}
		
		if (featureItem == null)
		{
			throw new IllegalArgumentException("featureItem is null");
		}
		
		float hue;
		
		if (value <= lower)
		{
			hue = 0.0f;
		}
		else if (value >= upper)
		{
			hue = 1.0f;
		}
		else
		{
			hue = (value - lower) / (upper - lower);
		}
		
		Paint p = featureItem.getPaint();
		
		if (p instanceof Color)
		{
			Color c = (Color)p;
			
			float[] hsb = new float[3];
			hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
			
			Color newColor = Color.getHSBColor(hue, hsb[1], hsb[2]);
			
			featureItem.setPaint(newColor);
		}
	}
}
